module com.example.colabjdbcmysqlthaycan {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.colabjdbcmysqlthaycan to javafx.fxml;
    exports com.example.colabjdbcmysqlthaycan;
}