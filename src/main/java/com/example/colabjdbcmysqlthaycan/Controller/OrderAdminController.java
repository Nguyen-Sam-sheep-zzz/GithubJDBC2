package com.example.colabjdbcmysqlthaycan.Controller;

import com.example.colabjdbcmysqlthaycan.Application.LoginApplication;
import com.example.colabjdbcmysqlthaycan.Class.ProductDisplay;
import com.example.colabjdbcmysqlthaycan.Class.Session;
import com.example.colabjdbcmysqlthaycan.ConnectDB;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableCell;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.scene.control.Button;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderAdminController {
    ConnectDB connectDB = new ConnectDB();

    @FXML
    private Button buttonSignOut;
    @FXML
    private TableView<ProductDisplay> tableViewOrder;
    @FXML
    private TableColumn<ProductDisplay, Integer> idColumn;
    @FXML
    private TableColumn<ProductDisplay, String> imageColumn;
    @FXML
    private TableColumn<ProductDisplay, String> nameColumn;
    @FXML
    private TableColumn<ProductDisplay, Double> priceColumn;
    @FXML
    private TableColumn<ProductDisplay, Integer> quantityColumn;
    @FXML
    private TableColumn<ProductDisplay, Double> amountColumn;
    @FXML
    private TableColumn<ProductDisplay, String> statusColumn;
    @FXML
    private TableColumn<ProductDisplay, ProductDisplay> actionColumn;

    @FXML
    public void initialize() {
        tableViewOrder.setItems(getProductDisplayList());
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        imageColumn.setCellValueFactory(new PropertyValueFactory<>("image"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        amountColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getAmount()).asObject());

    }

    public ObservableList<ProductDisplay> getProductDisplayList() {
        ObservableList<ProductDisplay> orderDisplayList = FXCollections.observableArrayList();
        String id = Session.getLoggedInCustomerId();
        String query = "SELECT o.idOrder, o.paymentStatus, po.quantity, p.nameProduct, i.link, p.price " +
                "FROM `Order` o " +
                "JOIN ProductOrder po ON o.idOrder = po.idOrder " +
                "JOIN Products p ON po.idProduct = p.idProduct " +
                "JOIN ImageProducts ip ON p.idProduct = ip.idProduct " +
                "JOIN Images i ON ip.idImage = i.idImage " +
                "WHERE o.idUser = ?";

        try (Connection connection = connectDB.connectionDB();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int idOrder = resultSet.getInt("idOrder");
                String paymentStatus = resultSet.getString("paymentStatus");
                int quantity = resultSet.getInt("quantity");
                String nameProduct = resultSet.getString("nameProduct");
                String link = resultSet.getString("link");
                double price = resultSet.getDouble("price");
                orderDisplayList.add(new ProductDisplay(idOrder, link, nameProduct, price, quantity, paymentStatus));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderDisplayList;
    }
    public void loadToLoginScreenFromOrderAdmin() throws IOException {
        Parent root = FXMLLoader.load(LoginApplication.class.getResource("/com/example/colabjdbcmysqlthaycan/View/Login.fxml"));
        Stage stage = (Stage) buttonSignOut.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    public void loadToProductScreenFromOrderAdmin() throws IOException {
        Parent root = FXMLLoader.load(LoginApplication.class.getResource("/com/example/colabjdbcmysqlthaycan/View/HomeAdmin.fxml"));
        Stage stage = (Stage) buttonSignOut.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Home Admin");
        stage.setScene(scene);
        stage.show();
    }
}
