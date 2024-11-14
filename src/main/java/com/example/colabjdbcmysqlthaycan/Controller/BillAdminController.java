package com.example.colabjdbcmysqlthaycan.Controller;

import com.example.colabjdbcmysqlthaycan.Application.LoginApplication;
import com.example.colabjdbcmysqlthaycan.Class.ProductDisplay;
import com.example.colabjdbcmysqlthaycan.Class.Session;
import com.example.colabjdbcmysqlthaycan.ConnectDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BillAdminController {
    ConnectDB connectDB = new ConnectDB();

    @FXML
    private TableView<ProductDisplay> tableViewBill;
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
    private TableColumn<ProductDisplay, Date> orderDateColumn;
    @FXML
    private TableColumn<ProductDisplay, Date> deliveryDateColumn;

    @FXML
    private Button buttonProduct;
    @FXML
    private Button buttonOrder;
    @FXML
    private Button buttonSignOut;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idBill"));
        imageColumn.setCellValueFactory(new PropertyValueFactory<>("imageLink"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        orderDateColumn.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        deliveryDateColumn.setCellValueFactory(new PropertyValueFactory<>("issueDate"));

        tableViewBill.setItems(getBillDisplayList());

        imageColumn.setCellFactory(col -> new TableCell<ProductDisplay, String>() {
            @Override
            protected void updateItem(String imageLink, boolean empty) {
                super.updateItem(imageLink, empty);
                if (empty || imageLink == null) {
                    setGraphic(null);
                } else {
                    ImageView imageView = new ImageView(new Image(getClass().getResource("/com/example/colabjdbcmysqlthaycan/img/" + imageLink).toExternalForm()));
                    imageView.setFitWidth(50);
                    imageView.setFitHeight(37);
                    setGraphic(imageView);
                }
            }
        });
    }

    public ObservableList<ProductDisplay> getBillDisplayList() {
        ObservableList<ProductDisplay> orderDisplayList = FXCollections.observableArrayList();
        String sql = """
            SELECT 
                b.idBill, b.issueDate, po.quantity, p.nameProduct, i.link, p.price, o.orderDate
            FROM 
                Bill b
            JOIN 
                `Order` o ON b.idOrder = o.idOrder
            JOIN 
                ProductOrder po ON o.idOrder = po.idOrder
            JOIN 
                Products p ON po.idProduct = p.idProduct
            JOIN 
                ImageProducts ip ON p.idProduct = ip.idProduct
            JOIN 
                Images i ON ip.idImage = i.idImage;
            """;

        try (Connection connection = connectDB.connectionDB();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int idBill = resultSet.getInt("idBill");
                Date issueDate = resultSet.getDate("issueDate");
                int quantity = resultSet.getInt("quantity");
                String nameProduct = resultSet.getString("nameProduct");
                String link = resultSet.getString("link");
                double price = resultSet.getDouble("price");
                Date orderDate = resultSet.getDate("orderDate");

                orderDisplayList.add(new ProductDisplay(idBill, issueDate, quantity, nameProduct, link, price, orderDate));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderDisplayList;
    }

    public void loadToLoginScreenFromBillAdmin() throws IOException {
        Parent root = FXMLLoader.load(LoginApplication.class.getResource("/com/example/colabjdbcmysqlthaycan/View/Login.fxml"));
        Stage stage = (Stage) buttonSignOut.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    public void loadToProductScreenFromBillAdmin() throws IOException {
        Parent root = FXMLLoader.load(LoginApplication.class.getResource("/com/example/colabjdbcmysqlthaycan/View/HomeAdmin.fxml"));
        Stage stage = (Stage) buttonProduct.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Home Admin");
        stage.setScene(scene);
        stage.show();
    }

    public void loadToOrderScreenFromBillAdmin() throws IOException {
        Parent root = FXMLLoader.load(LoginApplication.class.getResource("/com/example/colabjdbcmysqlthaycan/View/BillAdmin.fxml"));
        Stage stage = (Stage) buttonOrder.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Bill");
        stage.setScene(scene);
        stage.show();
    }


}