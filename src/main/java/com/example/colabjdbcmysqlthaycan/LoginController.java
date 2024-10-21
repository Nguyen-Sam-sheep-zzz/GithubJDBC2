package com.example.colabjdbcmysqlthaycan;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
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
    @FXML
    private Button SignUp;
    @FXML
    private Pane pnSignUp;
    @FXML
    private Pane pnSignIn;

    private ConnectDB connectDB = new ConnectDB();

    @FXML
    public void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        String role = validateLogin(username, password);

        if (role != null) {
            loginMessageLabel.setText("Login successful as" + role + "!");
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

    public void register(ActionEvent actionEvent) {
        if (actionEvent.getSource().equals(SignUp)){
            pnSignUp.toFront();
        }

    }

    public void handleBack(MouseEvent mouseEvent) {
            pnSignIn.toFront();
    }
}