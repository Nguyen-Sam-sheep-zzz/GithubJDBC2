package com.example.colabjdbcmysqlthaycan.Controller;

import com.example.colabjdbcmysqlthaycan.Class.ProductDisplay;
import com.example.colabjdbcmysqlthaycan.ConnectDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HomeAdminController {
    private ConnectDB connectDB = new ConnectDB();
    @FXML
    private TextField idProductTextField;
    @FXML
    private ImageView imageProductImageView;
    @FXML
    private TextField nameProductTextField;
    @FXML
    private TextField descriptionProductTextField;
    @FXML
    private ComboBox statusProductComboBox;
    @FXML
    private TextField priceProductTextField;
    @FXML
    private TextField idImageProductTextField;
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
    private TableColumn<ProductDisplay, String> statusColumn;

    @FXML
    private TableColumn<ProductDisplay, String> idImageProduct;

    public void initialize() {
        imageColumn.setCellValueFactory(new PropertyValueFactory<>("imageView"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        idImageProduct.setCellValueFactory(new PropertyValueFactory<>("idImage"));
        statusProductComboBox.setValue("available");
        idImageProduct.setVisible(false);

        String defaultImage = "/com/example/colabjdbcmysqlthaycan/img/HoiCham-removebg-preview.png";
        Image image = new Image(getClass().getResource(defaultImage).toExternalForm());
        imageProductImageView.setImage(image);

        imageProductImageView.setFitWidth(122);
        imageProductImageView.setFitHeight(128);

        productTableView.setItems(getProductDisplayList());

        idProductTextField.setVisible(false);
        idImageProductTextField.setVisible(false);

        productTableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && productTableView.getSelectionModel().getSelectedItem() != null) {
                ProductDisplay selectedProduct = productTableView.getSelectionModel().getSelectedItem();
                populateFields(selectedProduct);
            }
        });
    }

    private void populateFields(ProductDisplay product) {
        idProductTextField.setText(String.valueOf(product.getId()));
        nameProductTextField.setText(product.getName());
        descriptionProductTextField.setText(product.getDescription());
        priceProductTextField.setText(String.valueOf(product.getPrice()));
        statusProductComboBox.setValue(product.getStatus());
        idImageProductTextField.setText(product.getIdImage());

        Image image = new Image(getClass().getResource("/com/example/colabjdbcmysqlthaycan/img/" + product.getImageLink()).toExternalForm());
        imageProductImageView.setImage(image);
    }

    public void handleUpdateProduct() {
        String id = idProductTextField.getText();
        String name = nameProductTextField.getText();
        String description = descriptionProductTextField.getText();
        double price = Double.parseDouble(priceProductTextField.getText());
        String status = statusProductComboBox.getValue().toString();

        String imagePath = imageProductImageView.getImage().getUrl();
        File file = new File(imagePath);
        String link = file.getName();

        String idImage = idImageProductTextField.getText();



        updateProductDisplay(id, name, description, price, status, link, idImage);
        clearHomeAdmin();
        productTableView.setItems(getProductDisplayList());
    }

    public void clearHomeAdmin() {
        idImageProductTextField.clear();
        nameProductTextField.clear();
        descriptionProductTextField.clear();
        priceProductTextField.clear();
        idImageProductTextField.clear();
        imageProductImageView = new ImageView(new Image(getClass().getResource("/com/example/colabjdbcmysqlthaycan/img/HoiCham-removebg-preview.png").toExternalForm()));
        statusProductComboBox.setValue("available");
    }

    private void updateProductDisplay(String id, String name, String description, double price, String status, String imagePath, String idImage) {
        updateProduct(id, name, description, price, status);
        updateImages(idImage, imagePath);
    }

    private void updateProduct(String id, String name, String description, double price, String status) {
        Connection connection = connectDB.connectionDB();
        PreparedStatement preparedStatement;
        String updateProduct = "update Products set nameProduct=?, productDescription=?, price=?, status=? where idProduct=?";

        try {
            preparedStatement = connection.prepareStatement(updateProduct);

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, description);
            preparedStatement.setDouble(3, price);
            preparedStatement.setString(4, status);
            preparedStatement.setString(5, id);

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

        String query = "select p.idProduct, p.nameProduct, p.productDescription, p.price, p.status, i.idImage, i.link from Products p join ImageProducts ip on p.idProduct = ip.idProduct join Images i on ip.idImage = i.idImage";


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

                productDisplayList.add(new ProductDisplay(imageLink, id, name, description, price, status, idImage));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productDisplayList;
    }
}

