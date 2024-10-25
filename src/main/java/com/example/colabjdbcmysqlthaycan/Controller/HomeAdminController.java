package com.example.colabjdbcmysqlthaycan.Controller;

import com.example.colabjdbcmysqlthaycan.Class.ProductDisplay;
import com.example.colabjdbcmysqlthaycan.ConnectDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


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

    public void initialize() {
        Connection connection = connectDB.connectionDB();

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

        Connection connection = connectDB.connectionDB();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

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
}

