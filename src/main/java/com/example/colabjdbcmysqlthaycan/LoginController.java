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

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


public class LoginController {
    @FXML
    private PasswordField reEnterPassword;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label loginMessageLabel;
    @FXML
    private Button signUp;
    @FXML
    private Pane pnSignUp;
    @FXML
    private Pane pnSignIn;
    @FXML
    private TextField registerFullName;
    @FXML
    private TextField registerUsername;
    @FXML
    private PasswordField registerPassword;

    private ConnectDB connectDB = new ConnectDB();

    @FXML
    public void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("ERROR", "Please fill in your login information");
            return;
        }
        String[] loginInfo = validateLogin(username, password);
        if (loginInfo != null) {
            String role = loginInfo[0];
            String status = loginInfo[1];
            if ("active".equalsIgnoreCase(status)) {
                showAlert("Login successful", "Welcome: " + role);
                loadNextScreen(role);
            } else {
                loginMessageLabel.setText("Account is not active.");
                System.out.println(username + " " + password + " " + status);
            }
        } else {
            loginMessageLabel.setText("Invalid credentials. Try again.");
        }
    }

    public boolean validatePassword(String password) {
        String rePassword = reEnterPassword.getText();
        if (!password.equals(rePassword)) {
            showAlert("ERROR", "Passwords do not match, please re-enter your password");
            return false;
        }
        if (password.length() < 6) {
            showAlert("ERROR", "Password must be at least 6 characters");
            return false;
        }
        if (password.length() > 100) {
            showAlert("ERROR", "Password limit 100 characters");
            return false;
        }
        if (!password.matches(".*[0-9].*")) {
            showAlert("EROROR", "Password must have at least one number");
            return false;
        }
        return true;
    }

    private String[] validateLogin(String username, String password) {
        Connection connection = connectDB.connectionDB();
        PreparedStatement preparedStatement;
        String LoginCheck = "select * from user where username = ? and password = ?";

        try {
            preparedStatement = connection.prepareStatement(LoginCheck);

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String role = resultSet.getString("role");
                String status = resultSet.getString("status");
                return new String[]{role, status};
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
                loader = new FXMLLoader(getClass().getResource("/com/example/colabjdbcmysqlthaycan/View/DisplayMainAdmin.fxml"));
            } else {
                loader = new FXMLLoader(getClass().getResource("/com/example/colabjdbcmysqlthaycan/View/DisplayMainUser.fxml"));
            }
            Parent root = loader.load();
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadToLoginScreenFromRegister() throws IOException {
        Parent root = FXMLLoader.load(LoginApplication.class.getResource("/com/example/colabjdbcmysqlthaycan/View/Login.fxml"));
        Stage stage = (Stage) signUp.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    public void register(ActionEvent actionEvent) {
        if (actionEvent.getSource().equals(signUp)) {
            pnSignUp.toFront();
        }
    }

    public void handleBack(MouseEvent mouseEvent) {
        pnSignIn.toFront();
    }

    public boolean validateUsername(String username) {
        String regex = "^[a-zA-Z0-9]*$";
        return username.matches(regex);
    }

    public boolean validateUFullName(String uFullName) {
        String regex = "^[^0-9]*$";
        return uFullName.matches(regex);
    }

    public void handleRegister(ActionEvent actionEvent) throws IOException {
        String username = registerUsername.getText();
        String password = registerPassword.getText();
        String uFullName = registerFullName.getText();

        if (username.isEmpty() || password.isEmpty() || uFullName.isEmpty()) {
            showAlert("Registration failed", "Please fill in the registration information completely");
            return;
        }
        if (username.length() <= 6) {
            showAlert("ERROR", "Username must be at least 6 characters");
            return;
        }
        if (username.length() > 30) {
            showAlert("ERROR", "Username cannot exceed 30 characters");
            return;
        }
        if (!validateUsername(username)) {
            showAlert("ERROR", "Spaces and diacritics cannot exist in username");
            return;
        }
        if (!validatePassword(password)) {
            return;
        }
        if (uFullName.length() > 100) {
            showAlert("ERROR", "Your full name cannot exceed 100 characters");
            return;
        }
        if (!validateUFullName(uFullName)) {
            showAlert("ERROR", "You cannot have numbers in your full name.");
            return;
        }
        if (registerUser(username, password, uFullName)) {
            showAlert("Registration successful", "Please login again");
            loadToLoginScreenFromRegister();
        } else {
            showAlert("Registration failed", "Username already exists. Please choose another name.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public boolean registerUser(String username, String password, String uFullname) {
        Connection connection = connectDB.connectionDB();
        PreparedStatement preparedStatement;
        String register = "insert into user (username, password, name, role, status) values (?,?,?,?,?)";

        if (checkUsername(username)) {
            return false;
        }
        try {
            preparedStatement = connection.prepareStatement(register);

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, uFullname);
            preparedStatement.setString(4, "user");
            preparedStatement.setString(5, "active");

            int row = preparedStatement.executeUpdate();

            if (row > 0) {
                System.out.println("Registration succsessful");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    private boolean checkUsername(String username) {
        Connection connection = connectDB.connectionDB();
        PreparedStatement preparedStatement;
        String Check = "select * from user where username = ?";

        try {
            preparedStatement = connection.prepareStatement(Check);

            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                if (count > 0) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
}

