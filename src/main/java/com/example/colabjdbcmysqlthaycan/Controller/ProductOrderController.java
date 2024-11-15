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
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;


public class ProductOrderController {
    ConnectDB connectDB = new ConnectDB();
    @FXML
    private Label idOrderLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label quantityLabel;
    @FXML
    private Label amountLabel;
    @FXML
    private Label deliveryStatusLabel;
    @FXML
    private Label priceLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Button buttonCancel;
    @FXML
    private ImageView imageOrder;
    public void setProductOrder(ProductDisplay productDisplay) {
        idOrderLabel.setText(String.valueOf(productDisplay.getIdOrder()));
        nameLabel.setText(productDisplay.getName());
        quantityLabel.setText(String.valueOf(productDisplay.getQuantity()));
        amountLabel.setText(String.valueOf(productDisplay.getAmount()));
        priceLabel.setText(String.valueOf(productDisplay.getPrice()));
        deliveryStatusLabel.setText(String.valueOf(productDisplay.getPaymentStatus()));
        dateLabel.setText(String.valueOf(productDisplay.getOrderDate()));

        Image image = new Image(getClass().getResource("/com/example/colabjdbcmysqlthaycan/img/" + productDisplay.getImageLink()).toExternalForm());
        imageOrder.setImage(image);
        if ("Cancelled".equalsIgnoreCase(String.valueOf(productDisplay.getPaymentStatus())) ||"Paid".equalsIgnoreCase(String.valueOf(productDisplay.getPaymentStatus())) ) {
            buttonCancel.setDisable(true);
        } else {
            buttonCancel.setDisable(false);
        }
    }
    public void cancelOrder(){
        String query = "UPDATE `Order` SET paymentStatus = 'Cancelled' WHERE idOrder = ?";
        try {
            PreparedStatement preparedStatement = connectDB.connectionDB().prepareStatement(query);
            preparedStatement.setInt(1, Integer.parseInt(idOrderLabel.getText()));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void confirmDeleteProductInCart() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm deletion");
        alert.setHeaderText("Are you sure you want to delete this item?");
        alert.setContentText("Select OK to delete or Cancel to keep.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            cancelOrder();
        }
    }
    public void handleCancelOrder() throws IOException {
        confirmDeleteProductInCart();
        loadOrderUser();

    }
    public void loadOrderUser() throws IOException {
        Parent root = FXMLLoader.load(LoginApplication.class.getResource("/com/example/colabjdbcmysqlthaycan/View/Order.fxml"));
        Stage stage = (Stage) buttonCancel.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Home Bill");
        stage.setScene(scene);
        stage.show();
    }
}
