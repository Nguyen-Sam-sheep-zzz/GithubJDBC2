module com.example.colabjdbcmysqlthaycan {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    opens com.example.colabjdbcmysqlthaycan to javafx.fxml;
    exports com.example.colabjdbcmysqlthaycan;
    exports com.example.colabjdbcmysqlthaycan.Controller;
    opens com.example.colabjdbcmysqlthaycan.Controller to javafx.fxml;
    exports com.example.colabjdbcmysqlthaycan.Application;
    opens com.example.colabjdbcmysqlthaycan.Application to javafx.fxml;
}