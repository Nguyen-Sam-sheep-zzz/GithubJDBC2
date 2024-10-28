package com.example.colabjdbcmysqlthaycan.Controller;

import com.example.colabjdbcmysqlthaycan.Class.ProductDisplay;
import com.example.colabjdbcmysqlthaycan.ConnectDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.sql.*;



public class HomeAdminController {
    private ConnectDB connectDB = new ConnectDB();
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
    private TextField nameLaptopTextField;
    @FXML
    private TextField descriptionLaptopTextField;
    @FXML
    private ComboBox<String> statusLaptopTextField;
    @FXML
    private TextField priceLaptopTextField;
    @FXML
    private ImageView imageLaptopImageView;

    public void initialize() {
        imageColumn.setCellValueFactory(new PropertyValueFactory<>("imageView"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        productTableView.setItems(getProductDisplayList());
    }

    public ObservableList<ProductDisplay> getProductDisplayList() {
        ObservableList<ProductDisplay> productDisplayList = FXCollections.observableArrayList();
        String query = "SELECT p.idProduct, p.nameProduct, p.productDescription, p.price, p.status, i.link " +
                "FROM Products p " +
                "JOIN ImageProducts ip ON p.idProduct = ip.idProduct " +
                "JOIN Images i ON ip.idImage = i.idImage";
        try (Connection connection = connectDB.connectionDB();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("idProduct");
                String name = resultSet.getString("nameProduct");
                String description = resultSet.getString("productDescription");
                double price = resultSet.getDouble("price");
                String status = resultSet.getString("status");
                String imageLink = resultSet.getString("link");

                productDisplayList.add(new ProductDisplay(imageLink, id, name, description, price, status));
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
            imageLaptopImageView.setImage(image);
        }
    }

    public void addProduct(ActionEvent actionEvent) {
        String queryProduct = "INSERT INTO Products (nameProduct, productDescription, price, status) VALUES (?, ?, ?, ?)";
        String queryImage = "INSERT INTO Images (link) VALUES (?)";
        String queryImageProduct = "INSERT INTO ImageProducts (idImage, idProduct) VALUES (?, ?)";

        try {
            PreparedStatement psProduct = connectDB.connectionDB().prepareStatement(queryProduct, Statement.RETURN_GENERATED_KEYS);
            psProduct.setString(1, nameLaptopTextField.getText());
            psProduct.setString(2, descriptionLaptopTextField.getText());
            psProduct.setDouble(3, Double.parseDouble(priceLaptopTextField.getText()));
            psProduct.setString(4, statusLaptopTextField.getValue());
            psProduct.executeUpdate();
            ResultSet generatedKeys = psProduct.getGeneratedKeys();
            int productId = 0;
            if (generatedKeys.next()) {
                productId = generatedKeys.getInt(1);
            }
            String imagePath = imageLaptopImageView.getImage().getUrl();
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

        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            throw new RuntimeException("Add product failed.", e);
        }
    }

}
