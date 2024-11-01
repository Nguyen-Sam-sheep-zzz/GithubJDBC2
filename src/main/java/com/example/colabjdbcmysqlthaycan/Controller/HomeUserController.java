package com.example.colabjdbcmysqlthaycan.Controller;

import com.example.colabjdbcmysqlthaycan.Application.LoginApplication;
import com.example.colabjdbcmysqlthaycan.Class.ProductDisplay;
import com.example.colabjdbcmysqlthaycan.ConnectDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HomeUserController {
    ConnectDB connectDB = new ConnectDB();
    @FXML
    private Button buttonSingOut;
    @FXML
    private GridPane gridPaneProductsUser;
    @FXML
    private TextField searchProductUser;
    @FXML
    private TextField quantityTextField;


    public void initialize() {
        getAllProduct();
        searchProductUser.textProperty().addListener((observable, oldValue, newValue) -> {
            handleSearchProduct();
        });
        quantityTextField.setText("0");
    }

    public void loadToLoginScreenFromHomeUser() throws IOException {
        Parent root = FXMLLoader.load(LoginApplication.class.getResource("/com/example/colabjdbcmysqlthaycan/View/Login.fxml"));
        Stage stage = (Stage) buttonSingOut.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();

    }

    public List<ProductDisplay> getProductsUser() {
        List<ProductDisplay> products = new ArrayList<>();
        PreparedStatement preparedStatement;
        Connection connection = connectDB.connectionDB();
        String query = "SELECT " +
                "    p.idProduct , " +
                "    i.link , " +
                "    p.nameProduct , " +
                "    p.productDescription , " +
                "    p.price , " +
                "    p.quantity , " +
                "    p.status " +
                "FROM " +
                "    products p " +
                "JOIN " +
                "    imageProducts ip ON p.idProduct = ip.idProduct " +
                "JOIN " +
                "    images i ON ip.idImage = i.idImage";
        try {
            preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String id = resultSet.getString("idProduct");
                String name = resultSet.getString("nameProduct");
                String description = resultSet.getString("productDescription");
                double price = resultSet.getDouble("price");
                String status = resultSet.getString("status");
                String imageLink = resultSet.getString("link");
                int quantity = resultSet.getInt("quantity");
                products.add(new ProductDisplay(id, imageLink, name, description, price, quantity, status));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public void getAllProduct() {
        List<ProductDisplay> products = getProductsUser();
        int column = 0;
        int row = 1;
        try {
            for (ProductDisplay product : products) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/colabjdbcmysqlthaycan/View/Product.fxml"));
                AnchorPane productPane = loader.load();

                ProductUserController controller = loader.getController();
                controller.setProductItem(product);

                gridPaneProductsUser.add(productPane, column++, row);

                if (column == 4) {
                    column = 0;
                    row++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleSearchProduct() {
        String searchQuery = searchProductUser.getText().trim();
        List<ProductDisplay> searchResults = new ArrayList<>();
        String query = "SELECT p.idProduct, p.nameProduct, p.productDescription, p.price, p.status, p.quantity, i.idImage, i.link " +
                "FROM Products p " +
                "JOIN ImageProducts ip ON p.idProduct = ip.idProduct " +
                "JOIN Images i ON ip.idImage = i.idImage " +
                "WHERE p.nameProduct LIKE ? OR p.price LIKE ?";

        try (Connection connection = connectDB.connectionDB();
             PreparedStatement ps = connection.prepareStatement(query)) {

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
                searchResults.add(new ProductDisplay(id, imageLink, name, description, price, quantity, status));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        updateGridPane(searchResults);
    }

    private void updateGridPane(List<ProductDisplay> products) {
        gridPaneProductsUser.getChildren().clear();
        int column = 0;
        int row = 1;
        try {
            for (ProductDisplay product : products) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/colabjdbcmysqlthaycan/View/Product.fxml"));
                AnchorPane productPane = loader.load();

                ProductUserController controller = loader.getController();
                controller.setProductItem(product);

                gridPaneProductsUser.add(productPane, column++, row);

                if (column == 4) {
                    column = 0;
                    row++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void reduceQuantity() {
        int currentQuantity = Integer.parseInt(quantityTextField.getText());
        if (currentQuantity > 0) {
            currentQuantity--;
            quantityTextField.setText(String.valueOf(currentQuantity));
        }
    }

    @FXML
    private void addQuantity() {
        int currentQuantity = Integer.parseInt(quantityTextField.getText());
        currentQuantity++;
        quantityTextField.setText(String.valueOf(currentQuantity));
    }
}


