package com.example.colabjdbcmysqlthaycan.Controller;

import com.example.colabjdbcmysqlthaycan.Application.LoginApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeUserController {
    @FXML
    private Button buttonSingOut;
    @FXML
    private TextField quantityTextField;
    @FXML
    public void initialize() {
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

