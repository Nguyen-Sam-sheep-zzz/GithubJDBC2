package com.example.colabjdbcmysqlthaycan;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB {
    private static final String urlConnection = "jdbc:mysql://localhost:3306/Thay_Can_CSDL_JDBC";

    public Connection connectionDB() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(urlConnection, "root", "123456");
            System.out.println("Successful! It's Sam.");
        } catch (SQLException e) {

        }
        try {
            connection = DriverManager.getConnection(urlConnection, "root", "root@123");
            System.out.println("Successful! It's Dung.");
        } catch (SQLException e) {

        }
        try {
            connection = DriverManager.getConnection(urlConnection, "root", "13122005");
            System.out.println("Successful! It's Dung.");
        } catch (SQLException e) {

        }
        return connection;
    }
}
