package com.example.colabjdbcmysqlthaycan.Controller;

import com.example.colabjdbcmysqlthaycan.Application.LoginApplication;
import com.example.colabjdbcmysqlthaycan.Class.ProductDisplay;
import com.example.colabjdbcmysqlthaycan.ConnectDB;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ProductCartController {
    ConnectDB connectDB = new ConnectDB();
    @FXML
    private ImageView imageProduct;
    @FXML
    private Label productName;
    @FXML
    private Label productPrice;
    @FXML
    private TextField productQuantityTextField;
    @FXML
    private Label productAmount;
    @FXML
    private Label idCart;
    @FXML
    private Button deleteProductCart;

    private ProductDisplay productDisplay;

    public void initialize() {
        productQuantityTextField.setText("0");
    }

    public void setProductItemCart(ProductDisplay productDisplay) {
        this.productDisplay = productDisplay;
        productName.setText(productDisplay.getName());
        productPrice.setText(String.valueOf(productDisplay.getPrice()));
        Image image = new Image(getClass().getResource("/com/example/colabjdbcmysqlthaycan/img/" + productDisplay.getImageLink()).toExternalForm());
        imageProduct.setImage(image);
        productQuantityTextField.setText(String.valueOf(productDisplay.getQuantity()));
        productAmount.setText(String.valueOf(productDisplay.getAmount()));
        idCart.setText(String.valueOf(productDisplay.getIdCart()));
        updateProductAmount();
    }

    @FXML
    private void reduce() throws IOException {
        int currentQuantity = Integer.parseInt(productQuantityTextField.getText());
        if (currentQuantity == 1) {
            confirmDelete();
            loadToCartUserScreen();
        }
        if (currentQuantity > 0) {
            currentQuantity--;
            productQuantityTextField.setText(String.valueOf(currentQuantity));
            productDisplay.setQuantity(currentQuantity);
            updateProductAmount();
        }

    }

    @FXML
    private void more() {
        int currentQuantity = Integer.parseInt(productQuantityTextField.getText());
        currentQuantity++;
        productQuantityTextField.setText(String.valueOf(currentQuantity));
        productDisplay.setQuantity(currentQuantity);
        updateProductAmount();
    }

    private void updateProductAmount() {
        productAmount.setText(String.valueOf(productDisplay.getAmount()));
    }

    public void deleteProductOrder(String idProductOrder) {
        Connection connection = connectDB.connectionDB();
        PreparedStatement preparedStatement;
        String deleteProductOrder = "DELETE FROM ProductOrder WHERE idOrder = ?";
        try {
            preparedStatement = connection.prepareStatement(deleteProductOrder);
            preparedStatement.setInt(1, Integer.parseInt(idProductOrder));
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteOrder(String idProductOrder) {
        Connection connection = connectDB.connectionDB();
        PreparedStatement preparedStatement;
        String deleteProductOrder = "DELETE FROM `Order` WHERE idOrder = ?";
        try {
            preparedStatement = connection.prepareStatement(deleteProductOrder);
            preparedStatement.setInt(1, Integer.parseInt(idProductOrder));
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteProductInCart(String idProductOrder) {
        deleteProductOrder(idProductOrder);
        deleteOrder(idProductOrder);
    }

    public void handleDeleteProductInCart() throws IOException {
        deleteProductInCart(idCart.getText());
        showAlert("Success", "Delete successful");
        loadToCartUserScreen();

    }

    private void showAlert(String tiltle, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(tiltle);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void loadToCartUserScreen() throws IOException {
        Parent root = FXMLLoader.load(LoginApplication.class.getResource("/com/example/colabjdbcmysqlthaycan/View/Cart.fxml"));
        Stage stage = (Stage) deleteProductCart.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Cart user");
        stage.setScene(scene);
        stage.show();
    }

    public void confirmDelete() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm deletion");
        alert.setHeaderText("Are you sure you want to delete this item?");
        alert.setContentText("Select OK to delete or Cancel to keep.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            deleteProductInCart(idCart.getText());
        }
    }
}
