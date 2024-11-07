package com.example.colabjdbcmysqlthaycan.Controller;

import com.example.colabjdbcmysqlthaycan.Application.LoginApplication;
import com.example.colabjdbcmysqlthaycan.Class.ProductDisplay;
import com.example.colabjdbcmysqlthaycan.Class.Session;
import com.example.colabjdbcmysqlthaycan.ConnectDB;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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

public class CartController {

    ConnectDB connectDB = new ConnectDB();
    @FXML
    private Button buttonSingOut;
    @FXML
    private GridPane gridPaneProductsUser;
    @FXML
    private CheckBox checkBoxAll;
    @FXML
    private Button homeUserButton;

    public void initialize() {
        getAllProductCart();
    }

    public void loadToLoginScreenFromCart() throws IOException {
        Parent root = FXMLLoader.load(LoginApplication.class.getResource("/com/example/colabjdbcmysqlthaycan/View/Login.fxml"));
        Stage stage = (Stage) buttonSingOut.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    public List<ProductDisplay> getProductsCart() {
        List<ProductDisplay> productsCart = new ArrayList<>();
        String id = Session.getLoggedInCustomerId();
        String query = "SELECT p.nameProduct, p.price, i.link, c.idCart, c.idProduct,c.quantity " +
                "FROM products p " +
                "JOIN ImageProducts ip ON p.idProduct = ip.idProduct " +
                "JOIN Images i ON ip.idImage = i.idImage " +
                "JOIN cart c ON c.idProduct = ip.idProduct " +
                "WHERE c.idUser = ?";

        try (Connection connection = connectDB.connectionDB();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String name = resultSet.getString("nameProduct");
                    double price = resultSet.getDouble("price");
                    String imageLink = resultSet.getString("link");
                    int quantity = resultSet.getInt("quantity");
                    int idCart = resultSet.getInt("idCart");
                    productsCart.add(new ProductDisplay(imageLink, name, price, quantity, idCart));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productsCart;
    }

    public void getAllProductCart() {
        List<ProductDisplay> products = getProductsCart();
        int column = 0;
        int row = 1;
        try {
            for (ProductDisplay productCart : products) {
                FXMLLoader loaderCart = new FXMLLoader(getClass().getResource("/com/example/colabjdbcmysqlthaycan/View/ProductCart.fxml"));
                AnchorPane productPane = loaderCart.load();
                ProductCartController controllerCart = loaderCart.getController();
                controllerCart.setProductItemCart(productCart);
                gridPaneProductsUser.add(productPane, column++, row);

                if (column == 1) {
                    column = 0;
                    row++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadToHomeUserScreenFromCart() throws IOException {
        Parent root = FXMLLoader.load(LoginApplication.class.getResource("/com/example/colabjdbcmysqlthaycan/View/HomeUser.fxml"));
        Stage stage = (Stage) homeUserButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Home user");
        stage.setScene(scene);
        stage.show();
    }
}
