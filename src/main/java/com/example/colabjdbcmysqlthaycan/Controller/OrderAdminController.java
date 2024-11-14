package com.example.colabjdbcmysqlthaycan.Controller;

import com.example.colabjdbcmysqlthaycan.Application.LoginApplication;
import com.example.colabjdbcmysqlthaycan.Class.ProductDisplay;
import com.example.colabjdbcmysqlthaycan.Class.Session;
import com.example.colabjdbcmysqlthaycan.ConnectDB;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

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
    private Button buttonProduct;
    @FXML
    private Button buttonBill;
    @FXML
    private TextField searchProductTextField;
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
    private TableColumn<ProductDisplay, String> paymentStatusColumn;
    @FXML
    private TableColumn<ProductDisplay, ProductDisplay> actionColumn;
    @FXML
    private TableColumn<ProductDisplay, Integer> idUserColumn;
    @FXML
    private TableColumn<ProductDisplay, String> nameUserColumn;

    @FXML
    public void initialize() {
        tableViewOrder.setItems(getProductDisplayList());
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idOrder"));
        imageColumn.setCellValueFactory(new PropertyValueFactory<>("imageLink"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        paymentStatusColumn.setCellValueFactory(new PropertyValueFactory<>("paymentStatus"));
        actionColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue()));
        idUserColumn.setCellValueFactory(new PropertyValueFactory<>("idUser"));
        nameUserColumn.setCellValueFactory(new PropertyValueFactory<>("nameUser"));
        searchProductTextField.setOnKeyReleased(event -> handleSearchProduct());
        actionColumn.setCellFactory(col -> new TableCell<ProductDisplay, ProductDisplay>() {
            @Override
            protected void updateItem(ProductDisplay item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    if (item.getPaymentStatus() == ProductDisplay.PaymentStatus.Paid) {
                        ImageView checkImageView = new ImageView(new Image(getClass().getResource("/com/example/colabjdbcmysqlthaycan/img/DauV.png").toExternalForm()));
                        checkImageView.setFitWidth(20);
                        checkImageView.setFitHeight(20);
                        setGraphic(checkImageView);

                    } else if (item.getPaymentStatus() == ProductDisplay.PaymentStatus.Cancelled) {
                        ImageView cancelImageView = new ImageView(new Image(getClass().getResource("/com/example/colabjdbcmysqlthaycan/img/DauX.png").toExternalForm()));
                        cancelImageView.setFitWidth(20);
                        cancelImageView.setFitHeight(20);
                        setGraphic(cancelImageView);

                    } else {
                        Button confirmButton = new Button("Confirm");
                        Button cancelButton = new Button("Cancel");

                        confirmButton.setOnAction(event -> {
                            confirmOrder(item);
                            tableViewOrder.setItems(getProductDisplayList()); // Refresh the table
                        });

                        cancelButton.setOnAction(event -> {
                            cancelOrder(item);
                            tableViewOrder.setItems(getProductDisplayList());
                        });
                        confirmButton.setPrefWidth(80);
                        setGraphic(confirmButton);
                        cancelButton.setPrefWidth(80);
                        setGraphic(cancelButton);

                        HBox hbox = new HBox(confirmButton, cancelButton);
                        hbox.setSpacing(10);

                        setGraphic(hbox);
                    }
                }
            }
        });
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
    private void confirmOrder(ProductDisplay product) {
        String query = "UPDATE `Order` SET paymentStatus = 'Paid' WHERE idOrder = ?";
        try (
             PreparedStatement preparedStatement = connectDB.connectionDB().prepareStatement(query)) {
            preparedStatement.setInt(1, product.getIdOrder());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tableViewOrder.setItems(getProductDisplayList());
    }

    public void cancelOrder(ProductDisplay productDisplay) {
        String query = "UPDATE `Order` SET paymentStatus = 'Cancelled' WHERE idOrder = ?";
        try {
            PreparedStatement preparedStatement = connectDB.connectionDB().prepareStatement(query);
            preparedStatement.setInt(1, productDisplay.getIdOrder());
            preparedStatement.executeUpdate();
            tableViewOrder.setItems(getProductDisplayList());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public ObservableList<ProductDisplay> getProductDisplayList() {
        ObservableList<ProductDisplay> orderDisplayList = FXCollections.observableArrayList();
        String query = "SELECT o.idOrder,u.idUser,u.name , o.paymentStatus, po.quantity, p.nameProduct, i.link, p.price " +
                "FROM `Order` o " +
                "JOIN user u ON o.idUser = u.idUser " +
                "JOIN ProductOrder po ON o.idOrder = po.idOrder " +
                "JOIN Products p ON po.idProduct = p.idProduct " +
                "JOIN ImageProducts ip ON p.idProduct = ip.idProduct " +
                "JOIN Images i ON ip.idImage = i.idImage ";

        try (Connection connection = connectDB.connectionDB();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int idOrder = resultSet.getInt("idOrder");
                ProductDisplay.PaymentStatus paymentStatus = ProductDisplay.PaymentStatus.valueOf(resultSet.getString("paymentStatus"));
                int quantity = resultSet.getInt("quantity");
                String nameProduct = resultSet.getString("nameProduct");
                String imageLink = resultSet.getString("link");
                double price = resultSet.getDouble("price");
                int idUser = resultSet.getInt("idUser");
                String nameUser = resultSet.getString("name");
                orderDisplayList.add(new ProductDisplay(idOrder, imageLink, nameProduct, price, quantity, paymentStatus,idUser,nameUser));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderDisplayList;
    }
    public void handleSearchProduct() {
        ObservableList<ProductDisplay> searchProduct = FXCollections.observableArrayList();
        String searchQuery = searchProductTextField.getText().trim();
        String query = "SELECT o.idOrder, u.idUser , u.name , o.paymentStatus, po.quantity, p.nameProduct, i.link, p.price " +
                "FROM `Order` o " +
                "JOIN user u on o.idUser = u.idUser " +
                "JOIN ProductOrder po ON o.idOrder = po.idOrder " +
                "JOIN Products p ON po.idProduct = p.idProduct " +
                "JOIN ImageProducts ip ON p.idProduct = ip.idProduct " +
                "JOIN Images i ON ip.idImage = i.idImage" +
                " WHERE p.nameProduct LIKE ? OR o.paymentStatus LIKE ?";
        try {
            PreparedStatement ps = connectDB.connectionDB().prepareStatement(query);
            ps.setString(1, "%" + searchQuery + "%");
            ps.setString(2, "%" + searchQuery + "%");
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                int idOrder = resultSet.getInt("idOrder");
                ProductDisplay.PaymentStatus paymentStatus = ProductDisplay.PaymentStatus.valueOf(resultSet.getString("paymentStatus"));
                int quantity = resultSet.getInt("quantity");
                String nameProduct = resultSet.getString("nameProduct");
                String imageLink = resultSet.getString("link");
                double price = resultSet.getDouble("price");
                int idUser = resultSet.getInt("idUser");
                String nameUser = resultSet.getString("name");
                searchProduct.add(new ProductDisplay(idOrder, imageLink, nameProduct, price, quantity, paymentStatus,idUser,nameUser));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tableViewOrder.setItems(searchProduct);
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
        Stage stage = (Stage) buttonProduct.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Home Admin");
        stage.setScene(scene);
        stage.show();
    }

    public void loadToBillScreenFromOrderAdmin() throws IOException {
        Parent root = FXMLLoader.load(LoginApplication.class.getResource("/com/example/colabjdbcmysqlthaycan/View/BillAdmin.fxml"));
        Stage stage = (Stage) buttonBill.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Bill");
        stage.setScene(scene);
        stage.show();
    }
}
