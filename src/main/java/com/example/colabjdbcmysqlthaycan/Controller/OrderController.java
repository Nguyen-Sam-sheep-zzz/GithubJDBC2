package com.example.colabjdbcmysqlthaycan.Controller;

import com.example.colabjdbcmysqlthaycan.Application.LoginApplication;
import com.example.colabjdbcmysqlthaycan.Class.ProductDisplay;
import com.example.colabjdbcmysqlthaycan.Class.Session;
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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderController {
    ConnectDB connectDB = new ConnectDB();
    @FXML
    private GridPane gridPaneOrder;
    @FXML
    private Button buttonSingOut;
    @FXML
    private Button homeUserButton;
    @FXML
    private Button homeCart;
    @FXML
    private TextField searchProductTextField;
    public void initialize() {
        getAllProductCart();
        searchProductTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            ObservableList<ProductDisplay> searchResults = handleSearchProduct();
            updateGridPaneOrder(searchResults);
        });

        updateGridPaneOrder(handleSearchProduct());
    }

    public List<ProductDisplay> getProductsOrder() {
        List<ProductDisplay> productsCart = new ArrayList<>();
        String id = Session.getLoggedInCustomerId();
        String query = "SELECT o.idOrder,o.orderDate, o.paymentStatus, po.quantity, p.nameProduct, i.link ,p.price " +
                "FROM `Order` o " +
                "JOIN ProductOrder po ON o.idOrder = po.idOrder " +
                "JOIN Products p ON po.idProduct = p.idProduct " +
                "JOIN ImageProducts ip ON p.idProduct = ip.idProduct " +
                "JOIN Images i ON ip.idImage = i.idImage " +
                "WHERE o.idUser = ?";

        try (Connection connection = connectDB.connectionDB();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String name = resultSet.getString("nameProduct");
                    String imageLink = resultSet.getString("link");
                    int quantity = resultSet.getInt("quantity");
                    int idOrder = resultSet.getInt("idOrder");
                    double price = resultSet.getDouble("price");
                    Date date = resultSet.getDate("orderDate");
                    ProductDisplay.PaymentStatus paymentStatus = ProductDisplay.PaymentStatus.valueOf(resultSet.getString("paymentStatus"));
                    productsCart.add(new ProductDisplay(imageLink, name,  quantity, idOrder, paymentStatus,price,date));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productsCart;
    }

    public void getAllProductCart() {
        List<ProductDisplay> products = getProductsOrder();
        int column = 0;
        int row = 1;
        try {
            for (ProductDisplay productCart : products) {
                FXMLLoader loaderOrder = new FXMLLoader(getClass().getResource("/com/example/colabjdbcmysqlthaycan/View/ProductOrder.fxml"));
                AnchorPane productPane = loaderOrder.load();
                ProductOrderController controllerOrder = loaderOrder.getController();
                controllerOrder.setProductOrder(productCart);
                gridPaneOrder.add(productPane, column++, row);

                if (column == 1) {
                    column = 0;
                    row++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public ObservableList<ProductDisplay> handleSearchProduct() {
        ObservableList<ProductDisplay> searchProduct = FXCollections.observableArrayList();
        String searchQuery = searchProductTextField.getText().trim();

        String query = "SELECT o.idOrder, o.orderDate, o.paymentStatus, po.quantity, p.nameProduct, i.link, p.price " +
                "FROM `Order` o " +
                "JOIN ProductOrder po ON o.idOrder = po.idOrder " +
                "JOIN Products p ON po.idProduct = p.idProduct " +
                "JOIN ImageProducts ip ON p.idProduct = ip.idProduct " +
                "JOIN Images i ON ip.idImage = i.idImage " +
                "WHERE o.idUser = ? AND p.nameProduct LIKE ?";

        try {
            PreparedStatement ps = connectDB.connectionDB().prepareStatement(query);
            ps.setString(1, Session.getLoggedInCustomerId());
            ps.setString(2, "%" + searchQuery + "%");
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                String name = resultSet.getString("nameProduct");
                String imageLink = resultSet.getString("link");
                int quantity = resultSet.getInt("quantity");
                int idOrder = resultSet.getInt("idOrder");
                double price = resultSet.getDouble("price");
                Date date = resultSet.getDate("orderDate");
                ProductDisplay.PaymentStatus paymentStatus = ProductDisplay.PaymentStatus.valueOf(resultSet.getString("paymentStatus"));

                searchProduct.add(new ProductDisplay(imageLink, name, quantity, idOrder, paymentStatus, price, date));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return searchProduct;
    }

    public void updateGridPaneOrder(ObservableList<ProductDisplay> products) {
        gridPaneOrder.getChildren().clear();

        int column = 0;
        int row = 1;

        try {
            for (ProductDisplay productCart : products) {
                FXMLLoader loaderOrder = new FXMLLoader(getClass().getResource("/com/example/colabjdbcmysqlthaycan/View/ProductOrder.fxml"));
                AnchorPane productPane = loaderOrder.load();
                ProductOrderController controllerOrder = loaderOrder.getController();
                controllerOrder.setProductOrder(productCart);
                gridPaneOrder.add(productPane, column++, row);

                if (column == 1) {
                    column = 0;
                    row++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadToLoginScreenFromOrder() throws IOException {
        Parent root = FXMLLoader.load(LoginApplication.class.getResource("/com/example/colabjdbcmysqlthaycan/View/Login.fxml"));
        Stage stage = (Stage) buttonSingOut.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }
    public void loadToHomeUserScreenFromOrder() throws IOException {
        Parent root = FXMLLoader.load(LoginApplication.class.getResource("/com/example/colabjdbcmysqlthaycan/View/HomeUser.fxml"));
        Stage stage = (Stage) homeUserButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Home user");
        stage.setScene(scene);
        stage.show();
    }
    public void loadToCartScreenFromHomeOrder() throws IOException {
        Parent root = FXMLLoader.load(LoginApplication.class.getResource("/com/example/colabjdbcmysqlthaycan/View/Cart.fxml"));
        Stage stage = (Stage) homeCart.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Cart");
        stage.setScene(scene);
        stage.show();
    }
}
