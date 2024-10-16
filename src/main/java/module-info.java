module com.example.colabjdbcmysqlthaycan {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.colabjdbcmysqlthaycan to javafx.fxml;
    exports com.example.colabjdbcmysqlthaycan;
}