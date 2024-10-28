module com.example.colabjdbcmysqlthaycan {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires mysql.connector.java;
    opens com.example.colabjdbcmysqlthaycan to javafx.fxml;
    exports com.example.colabjdbcmysqlthaycan;
    exports com.example.colabjdbcmysqlthaycan.Controller;
    opens com.example.colabjdbcmysqlthaycan.Controller to javafx.fxml;
    exports com.example.colabjdbcmysqlthaycan.Application;
    opens com.example.colabjdbcmysqlthaycan.Application to javafx.fxml;
    opens com.example.colabjdbcmysqlthaycan.Class to javafx.base;
    opens com.example.colabjdbcmysqlthaycan.View to javafx.fxml;

}