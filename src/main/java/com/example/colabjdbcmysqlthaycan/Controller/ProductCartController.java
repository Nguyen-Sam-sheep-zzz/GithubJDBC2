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
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
    private Label idProductCart;
    @FXML
    private Button deleteProductCart;
    @FXML
    private CheckBox productCartCheckBox;

    private ProductDisplay productDisplay;

    private CartController cartController;

    public void initialize() {
        productQuantityTextField.setText("0");
    }

    public CheckBox getProductCartCheckBox() {
        return productCartCheckBox;
    }

    public void setProductItemCart(ProductDisplay productDisplay, CartController cartController) {
        this.productDisplay = productDisplay;
        this.cartController = cartController;
        productName.setText(productDisplay.getName());
        productPrice.setText(String.valueOf(productDisplay.getPrice()));
        Image image = new Image(getClass().getResource("/com/example/colabjdbcmysqlthaycan/img/" + productDisplay.getImageLink()).toExternalForm());
        imageProduct.setImage(image);
//        productQuantityTextField.textProperty().addListener((observable, oldValue, newValue) -> {
//
//        });
        productQuantityTextField.setText(String.valueOf(productDisplay.getQuantity()));
        productAmount.setText(String.valueOf(productDisplay.getAmount()));
        idProductCart.setText(String.valueOf(productDisplay.getIdCart()));
        productCartCheckBox.setSelected(productDisplay.getCheckBox());
    }

    public void handleProductSelection() {
        if (productCartCheckBox.isSelected()) {
            cartController.addSelectProduct(productDisplay);
        } else {
            cartController.removeSelectedProduct(productDisplay);
            cartController.getSelectAllProductCartUserCheckBox().setSelected(false);
        }
    }

    @FXML
    private void reduce() throws IOException {
        int currentQuantity = Integer.parseInt(productQuantityTextField.getText());
        if (currentQuantity == 1) {
            confirmDeleteProductInCart();
            loadToCartUserScreen();
            return;
        }
        if (currentQuantity > 0) {
            currentQuantity--;
            productQuantityTextField.setText(String.valueOf(currentQuantity));
            productDisplay.setQuantity(currentQuantity);
            updateProductAmount();
            String query = "update cart set quantity = quantity - 1 where idCart = ?";
            try {
                PreparedStatement ps = connectDB.connectionDB().prepareStatement(query);
                ps.setInt(1, productDisplay.getIdCart());
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            cartController.updateTotalPrice();
        }
    }

    @FXML
    private void more() {
        int currentQuantity = Integer.parseInt(productQuantityTextField.getText());
        currentQuantity++;
        productQuantityTextField.setText(String.valueOf(currentQuantity));
        productDisplay.setQuantity(currentQuantity);
        updateProductAmount();
        String query = "update cart set quantity = quantity + 1 where idCart = ?";
        try {
            PreparedStatement ps = connectDB.connectionDB().prepareStatement(query);
            int id = Integer.parseInt(idProductCart.getText());
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        cartController.updateTotalPrice();
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

    public void deleteProductInCart(String idCart) {
        Connection connection = connectDB.connectionDB();
        PreparedStatement preparedStatement;
        String deleteProductCart = "DELETE FROM cart WHERE idCart = ?";

        try {
            preparedStatement = connection.prepareStatement(deleteProductCart);
            preparedStatement.setInt(1, Integer.parseInt(idCart));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void handleDeleteProductInCart() throws IOException {
        confirmDeleteProductInCart();
        loadToCartUserScreen();
    }

    public void loadToCartUserScreen() throws IOException {
        Parent root = FXMLLoader.load(LoginApplication.class.getResource("/com/example/colabjdbcmysqlthaycan/View/Cart.fxml"));
        Stage stage = (Stage) deleteProductCart.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Cart user");
        stage.setScene(scene);
        stage.show();
    }


    public void confirmDeleteProductInCart() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm deletion");
        alert.setHeaderText("Are you sure you want to delete this item?");
        alert.setContentText("Select OK to delete or Cancel to keep.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            deleteProductInCart(idProductCart.getText());
        }
    }
    public void updateTextField(int newquantity){
        String query = "update cart set quantity =  ?";
        try{
            PreparedStatement ps = connectDB.connectionDB().prepareStatement(query);
            ps.setInt(1, newquantity);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
