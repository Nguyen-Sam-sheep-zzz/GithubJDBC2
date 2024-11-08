package com.example.colabjdbcmysqlthaycan.Controller;

import com.example.colabjdbcmysqlthaycan.Class.ProductDisplay;
import com.example.colabjdbcmysqlthaycan.Class.ProductOrder;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class ProductOrderController {
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
    private ImageView imageOrder;
    public void setProductOrder(ProductDisplay productDisplay) {
        idOrderLabel.setText(String.valueOf(productDisplay.getIdOrder()));
        nameLabel.setText(productDisplay.getName());
        quantityLabel.setText(String.valueOf(productDisplay.getQuantity()));
        amountLabel.setText(String.valueOf(productDisplay.getAmount()));
        priceLabel.setText(String.valueOf(productDisplay.getPrice()));
        deliveryStatusLabel.setText(productDisplay.getpaymentStatus());
        Image image = new Image(getClass().getResource("/com/example/colabjdbcmysqlthaycan/img/" + productDisplay.getImageLink()).toExternalForm());
        imageOrder.setImage(image);
    }
}
