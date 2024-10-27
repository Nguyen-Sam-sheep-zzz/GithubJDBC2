package com.example.colabjdbcmysqlthaycan.Application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.io.IOException;

public class HomeAdminApplication extends Application {
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HomeAdminApplication.class.getResource("/com/example/colabjdbcmysqlthaycan/View/HomeAdmin.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Home admin");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load FXML: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
