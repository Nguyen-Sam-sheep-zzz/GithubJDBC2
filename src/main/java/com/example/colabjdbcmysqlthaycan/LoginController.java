package com.example.colabjdbcmysqlthaycan;

import javafx.fxml.FXML;
<<<<<<< HEAD
import javafx.scene.control.Label;

public class LoginController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}
=======
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label loginMessageLabel;

    private ConnectDB connectDB = new ConnectDB();

    @FXML
    public void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        String role = validateLogin(username, password);

        if (role != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Login Successful");
            alert.setHeaderText(null);
            alert.setContentText("Welcome " + role + "!");
            alert.showAndWait();
            loadNextScreen(role);
        } else {
            loginMessageLabel.setText("Invalid credentials. Try again.");
        }
    }

    private String validateLogin(String username, String password) {
        Connection connection = connectDB.connectionDB();
        PreparedStatement preparedStatement;
        String LoginCheck = "select * from user where username = ? and password = ?";

        try {
            preparedStatement = connection.prepareStatement(LoginCheck);

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("role");
            } else {
                return null;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void loadNextScreen(String role) {
        try {
            FXMLLoader loader;
            if (role.equals("admin")) {
                loader = new FXMLLoader(getClass().getResource("DisplayMainAdmin.fxml"));
            } else {
                loader = new FXMLLoader(getClass().getResource("DisplayMainUser.fxml"));
            }
            Parent root = loader.load();
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
>>>>>>> 2c7ee3576e99ddffb850ec49720f441f6ef5f314
