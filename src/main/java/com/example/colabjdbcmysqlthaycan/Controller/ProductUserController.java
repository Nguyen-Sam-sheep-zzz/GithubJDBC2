package com.example.colabjdbcmysqlthaycan.Controller;

import com.example.colabjdbcmysqlthaycan.Class.ProductDisplay;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;


public class ProductUserController {
    @FXML
    private ImageView imageViewUser;
    @FXML
    private Label nameProductUserLabel;
    @FXML
    private Label priceProductUserLabel;
    @FXML
    private Label stastusProductUserLabel;
    @FXML
    private AnchorPane productUserAnchorPane;


    public void initialize() {
        stastusProductUserLabel.setVisible(false);
    }

    public void setProductItem(ProductDisplay productDisplay) {
        nameProductUserLabel.setText(productDisplay.getName());
        priceProductUserLabel.setText(String.valueOf(productDisplay.getPrice()));
        stastusProductUserLabel.setText(productDisplay.getStatus());
        Image image = new Image(getClass().getResource("/com/example/colabjdbcmysqlthaycan/img/" + productDisplay.getImageLink()).toExternalForm());
        imageViewUser.setImage(image);
        if (productDisplay.getStatus().equals("unavailable")) {
            productUserAnchorPane.setStyle("-fx-border-color: red; " + "-fx-border-width: 2px; " + "-fx-border-radius: 10px; " + "-fx-background-color: #fff;");
        }
    }
}