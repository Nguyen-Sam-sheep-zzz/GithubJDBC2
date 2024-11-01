package com.example.colabjdbcmysqlthaycan.Controller;

import com.example.colabjdbcmysqlthaycan.Application.LoginApplication;
import com.example.colabjdbcmysqlthaycan.Class.ProductDisplay;
import com.example.colabjdbcmysqlthaycan.ConnectDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.*;

public class HomeAdminController {
    private ConnectDB connectDB = new ConnectDB();
    @FXML
    private TextField idProductTextField;
    @FXML
    private ImageView imageProductImageView;
    @FXML
    private TextField nameProductTextField;
    @FXML
    private TextArea descriptionProductTextArea;
    @FXML
    private ComboBox statusProductComboBox;
    @FXML
    private TextField priceProductTextField;
    @FXML
    private TextField quantityProductTextField;
    @FXML
    private TextField idImageProductTextField;
    @FXML
    private Button buttonSingOut;
    @FXML
    private TableView<ProductDisplay> productTableView;
    @FXML
    private TableColumn<ProductDisplay, String> imageColumn;
    @FXML
    private TableColumn<ProductDisplay, Integer> idColumn;
    @FXML
    private TableColumn<ProductDisplay, String> nameColumn;
    @FXML
    private TableColumn<ProductDisplay, String> descriptionColumn;
    @FXML
    private TableColumn<ProductDisplay, Double> priceColumn;
    @FXML
    private TableColumn<ProductDisplay, Integer> quantityColumn;
    @FXML
    private TableColumn<ProductDisplay, String> statusColumn;
    @FXML
    private TableColumn<ProductDisplay, String> idImageProduct;
    @FXML
    private TextField searchProductTextField;

    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        imageColumn.setCellValueFactory(new PropertyValueFactory<>("imageView"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("imageViewStatus"));
        idImageProduct.setCellValueFactory(new PropertyValueFactory<>("idImage"));
        descriptionColumn.setVisible(false);
        statusProductComboBox.setValue("available");
        idImageProduct.setVisible(false);

        searchProductTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            handleSearchProduct();
        });

        productTableView.setItems(getProductDisplayList());
        idProductTextField.setVisible(false);
        idImageProductTextField.setVisible(false);

        priceColumn.setCellFactory(column -> new TableCell<ProductDisplay, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty || price == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f $", price));
                }
            }
        });

        productTableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1 && productTableView.getSelectionModel().getSelectedItem() != null) {
                ProductDisplay selectedProduct = productTableView.getSelectionModel().getSelectedItem();
                populateFields(selectedProduct);
            } else if (event.getClickCount() == 2 && productTableView.getSelectionModel().getSelectedItem() != null) {
                ProductDisplay selectedProduct = productTableView.getSelectionModel().getSelectedItem();
                showProductDialog(selectedProduct);
            }
        });
    }

    private void populateFields(ProductDisplay product) {
        idProductTextField.setText(String.valueOf(product.getId()));
        nameProductTextField.setText(product.getName());
        descriptionProductTextArea.setText(product.getDescription());
        priceProductTextField.setText(String.valueOf(product.getPrice()));
        quantityProductTextField.setText(String.valueOf(product.getQuantity()));
        statusProductComboBox.setValue(product.getStatus());
        idImageProductTextField.setText(product.getIdImage());

        Image image = new Image(getClass().getResource("/com/example/colabjdbcmysqlthaycan/img/" + product.getImageLink()).toExternalForm());
        imageProductImageView.setImage(image);
    }

    public void handleUpdateProduct() {
        if (nameProductTextField.getText().isEmpty() || descriptionProductTextArea.getText().isEmpty() || priceProductTextField.getText().isEmpty() || imageProductImageView.getImage() == null) {
            showAlert("ERROR", "Please select a product");
        }
        if (nameProductTextField.getText().isEmpty()) {
            showAlert("ERROR", "Product name cannot be left blank");
            return;
        }
        if (descriptionProductTextArea.getText().isEmpty()) {
            showAlert("ERROR", "Description product cannot be left blank");
            return;
        }
        if (priceProductTextField.getText().isEmpty()) {
            showAlert("ERROR", "Price product cannot be left blank");
            return;
        } else if (!priceProductTextField.getText().matches("\\d+(\\.\\d+)?")) {
            showAlert("ERROR", "Price can only be numeric");
            return;
        }
        if (quantityProductTextField.getText().isEmpty()) {
            showAlert("ERROR", "Quantity product cannot be left blank");
            return;
        }
        if (imageProductImageView.getImage() == null) {
            showAlert("ERROR", "Image cannot be left blank");
            return;
        }
        String id = idProductTextField.getText();
        String name = nameProductTextField.getText();
        String description = descriptionProductTextArea.getText();
        double price = Double.parseDouble(priceProductTextField.getText());
        int quantity = Integer.parseInt(quantityProductTextField.getText());
        String status = statusProductComboBox.getValue().toString();

        String imagePath = imageProductImageView.getImage().getUrl();
        File file = new File(imagePath);
        String link = file.getName();

        String idImage = idImageProductTextField.getText();

        updateProductDisplay(id, name, description, price, quantity, status, link, idImage);
        clearHomeAdmin();
        productTableView.setItems(getProductDisplayList());
        showAlert("Add product successful", "Product has been added");
    }

    public void clearHomeAdmin() {
        idImageProductTextField.clear();
        nameProductTextField.clear();
        descriptionProductTextArea.clear();
        priceProductTextField.clear();
        idProductTextField.clear();
        imageProductImageView.setImage(null);
        statusProductComboBox.setValue("available");
    }

    private void updateProductDisplay(String id, String name, String description, double price, int quantity, String status, String imagePath, String idImage) {
        updateProduct(id, name, description, price, quantity, status);
        updateImages(idImage, imagePath);
    }

    private void updateProduct(String id, String name, String description, double price, int quantity, String status) {
        Connection connection = connectDB.connectionDB();
        PreparedStatement preparedStatement;
        String updateProduct = "update Products set nameProduct=?, productDescription=?, price=?, quantity=? , status=? where idProduct=?";

        try {
            preparedStatement = connection.prepareStatement(updateProduct);

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, description);
            preparedStatement.setDouble(3, price);
            preparedStatement.setInt(4, quantity);
            preparedStatement.setString(5, status);
            preparedStatement.setString(6, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateImages(String id, String link) {
        Connection connection = connectDB.connectionDB();
        PreparedStatement preparedStatement;
        String updateImages = "update Images set link = ? where idImage = ?";

        try {
            preparedStatement = connection.prepareStatement(updateImages);

            preparedStatement.setString(1, link);
            preparedStatement.setString(2, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public ObservableList<ProductDisplay> getProductDisplayList() {
        ObservableList<ProductDisplay> productDisplayList = FXCollections.observableArrayList();
        Connection connection = connectDB.connectionDB();
        String query = "SELECT \n" +
                "    p.idProduct, \n" +
                "    p.nameProduct, \n" +
                "    p.productDescription, \n" +
                "    p.price, \n" +
                "    p.status, \n" +
                "    p.quantity,\n" +
                "    i.idImage, \n" +
                "    i.link\n" +
                "FROM \n" +
                "    Products p\n" +
                "JOIN \n" +
                "    ImageProducts ip ON p.idProduct = ip.idProduct\n" +
                "JOIN \n" +
                "    Images i ON ip.idImage = i.idImage;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String id = resultSet.getString("idProduct");
                String name = resultSet.getString("nameProduct");
                String description = resultSet.getString("productDescription");
                double price = resultSet.getDouble("price");
                String status = resultSet.getString("status");
                String imageLink = resultSet.getString("link");
                String idImage = resultSet.getString("idImage");
                int quantity = resultSet.getInt("quantity");
                productDisplayList.add(new ProductDisplay(id, imageLink, name, description, price, quantity, status, idImage));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productDisplayList;
    }

    public void importImage(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            Image image = new Image(file.toURI().toString());
            imageProductImageView.setImage(image);
        }
    }

    public void addProduct(ActionEvent actionEvent) {
        String queryProduct = "INSERT INTO Products (nameProduct, productDescription, price,quantity, status) VALUES (?, ?, ? ,?, ?)";
        String queryImage = "INSERT INTO Images (link) VALUES (?)";
        String queryImageProduct = "INSERT INTO ImageProducts (idImage, idProduct) VALUES (?, ?)";

        try {
            PreparedStatement psProduct = connectDB.connectionDB().prepareStatement(queryProduct, Statement.RETURN_GENERATED_KEYS);
            if (nameProductTextField.getText().isEmpty()) {
                showAlert("ERROR", "Product name cannot be left blank");
                return;
            }
            if (descriptionProductTextArea.getText().isEmpty()) {
                showAlert("ERROR", "Description product cannot be left blank");
                return;
            }
            if (priceProductTextField.getText().isEmpty()) {
                showAlert("ERROR", "Price product cannot be left blank");
                return;
            } else if (!priceProductTextField.getText().matches("\\d+(\\.\\d+)?")) {
                showAlert("ERROR", "Price can only be numeric");
                return;
            }
            if (quantityProductTextField.getText().isEmpty()) {
                showAlert("ERROR", "Quantity product cannot be left blank");
                return;
            }
            if (imageProductImageView.getImage() == null) {
                showAlert("ERROR", "Image cannot be left blank");
                return;
            }
            psProduct.setString(1, nameProductTextField.getText());
            psProduct.setString(2, descriptionProductTextArea.getText());
            psProduct.setDouble(3, Double.parseDouble(priceProductTextField.getText()));
            psProduct.setInt(4, Integer.parseInt(quantityProductTextField.getText()));
            psProduct.setString(5, (String) statusProductComboBox.getValue());
            psProduct.executeUpdate();
            ResultSet generatedKeys = psProduct.getGeneratedKeys();
            int productId = 0;
            if (generatedKeys.next()) {
                productId = generatedKeys.getInt(1);
            }
            String imagePath = imageProductImageView.getImage().getUrl();
            int lastSlashIndex = imagePath.lastIndexOf("/");
            int imageId;
            if (lastSlashIndex != -1) {
                String imageName = imagePath.substring(lastSlashIndex + 1);
                PreparedStatement psImage = connectDB.connectionDB().prepareStatement(queryImage, Statement.RETURN_GENERATED_KEYS);
                psImage.setString(1, imageName);
                psImage.executeUpdate();
                ResultSet imageKeys = psImage.getGeneratedKeys();
                imageId = 0;
                if (imageKeys.next()) {
                    imageId = imageKeys.getInt(1);
                }
            } else {
                throw new RuntimeException("Invalid image path: " + imagePath);
            }

            PreparedStatement psImageProduct = connectDB.connectionDB().prepareStatement(queryImageProduct);
            psImageProduct.setInt(1, imageId);
            psImageProduct.setInt(2, productId);
            psImageProduct.executeUpdate();
            System.out.println("Product added successfully");

            productTableView.setItems(getProductDisplayList());
            clearHomeAdmin();
            showAlert("Add product successful", "Product has been added");


        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            throw new RuntimeException("Add product failed.", e);
        }
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void showProductDialog(ProductDisplay product) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Product Details");

        ButtonType closeButtonType = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(closeButtonType);

        VBox content = new VBox(10);
        content.setPadding(new Insets(10));

        Label nameLabel = new Label("Name: " + product.getName());
        Label descriptionLabel = new Label("Description: " + product.getDescription());
        Label priceLabel = new Label("Price: " + product.getPrice());
        Label quantityLabel = new Label("Quantity: " + product.getQuantity());
        Label status = new Label("Status: " + product.getStatus());

        ImageView productImageView = new ImageView();
        String imagePath = product.getImageLink();
        File file = new File(imagePath);
        String link = file.getName();

        if (link != null) {
            productImageView.setImage(new Image(getClass().getResource("/com/example/colabjdbcmysqlthaycan/img/" + link).toExternalForm()));
            productImageView.setFitHeight(100);
            productImageView.setPreserveRatio(true);
            productImageView.setSmooth(true);
        }

        content.getChildren().addAll(nameLabel, descriptionLabel, priceLabel, quantityLabel, status, productImageView);
        dialog.getDialogPane().setContent(content);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.showAndWait();
    }

    public void loadToLoginScreenFromHomeAdmin() throws IOException {
        Parent root = FXMLLoader.load(LoginApplication.class.getResource("/com/example/colabjdbcmysqlthaycan/View/Login.fxml"));
        Stage stage = (Stage) buttonSingOut.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();

    }

    public void handleSearchProduct() {
        ObservableList<ProductDisplay> searchProduct = FXCollections.observableArrayList();
        String searchQuery = searchProductTextField.getText().trim();
        String query = "SELECT p.idProduct, p.nameProduct, p.productDescription, p.price, p.status, p.quantity, i.idImage, i.link " +
                "FROM Products p " +
                "JOIN ImageProducts ip ON p.idProduct = ip.idProduct " +
                "JOIN Images i ON ip.idImage = i.idImage " +
                "WHERE p.nameProduct LIKE ? OR p.price LIKE ?";
        try {
            PreparedStatement ps = connectDB.connectionDB().prepareStatement(query);
            ps.setString(1, "%" + searchQuery + "%");
            ps.setString(2, "%" + searchQuery + "%");
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                String id = resultSet.getString("idProduct");
                String name = resultSet.getString("nameProduct");
                String description = resultSet.getString("productDescription");
                double price = resultSet.getDouble("price");
                int quantity = resultSet.getInt("quantity");
                String status = resultSet.getString("status");
                String imageLink = resultSet.getString("link");
                String idImage = resultSet.getString("idImage");
                searchProduct.add(new ProductDisplay(id, imageLink, name, description, price, quantity, status, idImage));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        productTableView.setItems(searchProduct);
    }
}