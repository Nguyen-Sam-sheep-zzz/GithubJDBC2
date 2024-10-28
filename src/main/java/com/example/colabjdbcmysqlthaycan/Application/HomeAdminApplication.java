package com.example.colabjdbcmysqlthaycan.Application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.io.IOException;

public class HomeAdminApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("/com/example/colabjdbcmysqlthaycan/View/HomeAdmin.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Home admin");
        stage.setScene(scene);
        stage.show();
    }
    // đừng sửa vào đây chỉ lỗi ảnh
    public static void main(String[] args) {
        launch();
    }
}
