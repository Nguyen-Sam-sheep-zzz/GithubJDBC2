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

    private final ConnectDB connectDB = new ConnectDB();


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
            if (imageLaptopImageView != null) {
                imageLaptopImageView.setImage(image);
            }
        }
    }

    public void addProduct(ActionEvent actionEvent) {

        String queryProduct = "INSERT INTO Products (nameProduct, productDescription, price, status) VALUES (?, ?, ?, ?)";
        String queryImage = "INSERT INTO Images (link) VALUES (?)";
        String queryImageProduct = "INSERT INTO ImageProducts (idImage, idProduct) VALUES (?, ?)";

        try (Connection connection = connectDB.connectionDB();
             PreparedStatement psProduct = connection.prepareStatement(queryProduct, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement psImage = connection.prepareStatement(queryImage, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement psImageProduct = connection.prepareStatement(queryImageProduct)) {

            psProduct.setString(1, nameLaptopTextField.getText());
            psProduct.setString(2, descriptionLaptopTextField.getText());
            psProduct.setDouble(3, Double.parseDouble(priceLaptopTextField.getText()));
            psProduct.setString(4, statusLaptopTextField.getValue() != null ? statusLaptopTextField.getValue() : ""); // Kiểm tra null
            psProduct.executeUpdate();

            ResultSet generatedKeys = psProduct.getGeneratedKeys();
            long idProduct = 0;
            if (generatedKeys.next()) {
                idProduct = generatedKeys.getLong(1);
            }

            psImage.setString(1, imageLaptopImageView.getImage().getUrl());
            psImage.executeUpdate();

            ResultSet generatedImageKeys = psImage.getGeneratedKeys();
            long idImage = 0;
            if (generatedImageKeys.next()) {
                idImage = generatedImageKeys.getLong(1);
            }

            psImageProduct.setLong(1, idImage);
            psImageProduct.setLong(2, idProduct);
            psImageProduct.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Add product failed.", e);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            throw new RuntimeException("Price must be a valid number.", e);
        }
    }
}
