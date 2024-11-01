package com.example.colabjdbcmysqlthaycan.Application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeUserApplication extends Application {
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HomeAdminApplication.class.getResource("/com/example/colabjdbcmysqlthaycan/View/HomeUser.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Home User");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
