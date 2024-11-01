package com.example.colabjdbcmysqlthaycan.Controller;

import com.example.colabjdbcmysqlthaycan.Class.ProductDisplay;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ProductUserController {
    @FXML
    private ImageView imageViewUser;
    @FXML
    private Label nameProductUserLabel;
    @FXML
    private Label priceProductUserLabel;
    @FXML
    private Label idProductUserLabel;
    @FXML
    private Label descriptionProductUserLabel;
    @FXML
    private Label allQuantityProductUserLabel;

    public void initialize() {
        descriptionProductUserLabel.setVisible(false);
        allQuantityProductUserLabel.setVisible(false);
        idProductUserLabel.setVisible(false);
    }

    public void setProductItem(ProductDisplay productDisplay) {

        nameProductUserLabel.setText(productDisplay.getName());
        priceProductUserLabel.setText(String.valueOf(productDisplay.getPrice()));
        idProductUserLabel.setText(productDisplay.getId());
        descriptionProductUserLabel.setText(productDisplay.getDescription());
        allQuantityProductUserLabel.setText(String.valueOf(productDisplay.getQuantity()));

        Image image = new Image(getClass().getResource("/com/example/colabjdbcmysqlthaycan/img/" + productDisplay.getImageLink()).toExternalForm());
        imageViewUser.setImage(image);
    }
}