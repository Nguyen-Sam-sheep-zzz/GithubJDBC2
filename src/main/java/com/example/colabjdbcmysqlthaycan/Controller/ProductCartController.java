package com.example.colabjdbcmysqlthaycan.Controller;

import com.example.colabjdbcmysqlthaycan.Class.ProductDisplay;
import com.example.colabjdbcmysqlthaycan.ConnectDB;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;



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
        updateProductAmount();
    }
    @FXML
    private void reduce() {
        int currentQuantity = Integer.parseInt(productQuantityTextField.getText());
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
}
