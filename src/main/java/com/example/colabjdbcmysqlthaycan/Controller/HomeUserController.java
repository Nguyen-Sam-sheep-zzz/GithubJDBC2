package com.example.colabjdbcmysqlthaycan.Controller;

import com.example.colabjdbcmysqlthaycan.Application.LoginApplication;
import com.example.colabjdbcmysqlthaycan.Class.ProductDisplay;
import com.example.colabjdbcmysqlthaycan.Class.Session;
import com.example.colabjdbcmysqlthaycan.ConnectDB;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.ImageView;

public class HomeUserController {
    ConnectDB connectDB = new ConnectDB();
    @FXML
    private Button buttonSingOut;
    @FXML
    private GridPane gridPaneProductsUser;
    @FXML
    private Label nameProductLabel;
    @FXML
    private Label priceProductLabel;
    @FXML
    private Label descriptionProductLabel;
    @FXML
    private Label statusProductLabel;
    @FXML
    private ImageView imageProductImageView;
    @FXML
    private TextField searchProductUser;
    @FXML
    private TextField quantityProductTextField;
    @FXML
    private Button buttonCart;
    @FXML
    private Label idProductLabel;
    @FXML
    private Label quantityProductLabel;
    @FXML
    private Button buttonOrder;
    @FXML
    private Button buttonBill;


    public void initialize() {
        quantityProductLabel.setVisible(false);
        idProductLabel.setVisible(false );
        searchProductUser.textProperty().addListener((observable, oldValue, newValue) -> {
            handleSearchProduct();
        });
        quantityProductTextField.setText("1");
        getAllProduct();
    }

    public void loadToLoginScreenFromHomeUser() throws IOException {
        Session.clearSession();
        Parent root = FXMLLoader.load(LoginApplication.class.getResource("/com/example/colabjdbcmysqlthaycan/View/Login.fxml"));
        Stage stage = (Stage) buttonSingOut.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    public void loadToCartScreenFromHomeUser() throws IOException {
        Parent root = FXMLLoader.load(LoginApplication.class.getResource("/com/example/colabjdbcmysqlthaycan/View/Cart.fxml"));
        Stage stage = (Stage) buttonCart.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Cart");
        stage.setScene(scene);
        stage.show();
    }
    public void loadToOrderScreenFromHomeUser() throws IOException {
        Parent root = FXMLLoader.load(LoginApplication.class.getResource("/com/example/colabjdbcmysqlthaycan/View/Order.fxml"));
        Stage stage = (Stage) buttonOrder.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Order");
        stage.setScene(scene);
        stage.show();
    }
    public void loadToBillScreenFromHomeUser() throws IOException{
        Parent root = FXMLLoader.load(LoginApplication.class.getResource("/com/example/colabjdbcmysqlthaycan/View/BillUser.fxml"));
        Stage stage = (Stage) buttonBill.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Order");
        stage.setScene(scene);
        stage.show();
    }

    public List<ProductDisplay> getProductsUser() {
        List<ProductDisplay> products = new ArrayList<>();
        PreparedStatement preparedStatement;
        Connection connection = connectDB.connectionDB();
        String query = "SELECT " +
                "    p.idProduct , " +
                "    i.link , " +
                "    p.nameProduct , " +
                "    p.productDescription , " +
                "    p.price , " +
                "    p.quantity , " +
                "    p.status " +
                "FROM " +
                "    products p " +
                "JOIN " +
                "    imageProducts ip ON p.idProduct = ip.idProduct " +
                "JOIN " +
                "    images i ON ip.idImage = i.idImage";
        try {
            preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String id = resultSet.getString("idProduct");
                String name = resultSet.getString("nameProduct");
                String description = resultSet.getString("productDescription");
                double price = resultSet.getDouble("price");
                String status = resultSet.getString("status");
                String imageLink = resultSet.getString("link");
                int quantity = resultSet.getInt("quantity");
                products.add(new ProductDisplay(id, imageLink, name, description, price, quantity, status));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public void getAllProduct() {
        List<ProductDisplay> products = getProductsUser();
        int column = 0;
        int row = 1;
        try {
            for (ProductDisplay product : products) {
                if ("unavailable".equals(product.getStatus())) {
                    continue;
                }
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/colabjdbcmysqlthaycan/View/Product.fxml"));
                AnchorPane productPane = loader.load();

                ProductUserController controller = loader.getController();
                controller.setProductItem(product);

                productPane.setOnMouseClicked(event -> getItemProducts(product));
                gridPaneProductsUser.add(productPane, column++, row);

                if (column == 4) {
                    column = 0;
                    row++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getItemProducts(ProductDisplay productDisplay) {
        idProductLabel.setText(productDisplay.getId());
        nameProductLabel.setText(productDisplay.getName());
        priceProductLabel.setText(String.valueOf(productDisplay.getPrice()));
        descriptionProductLabel.setText(productDisplay.getDescription());
        statusProductLabel.setText(productDisplay.getStatus());
        if (productDisplay.getQuantity() == 0) {
            showAlert("ERROR","The product is out of stock, please choose another product.");
            return;
        }
        quantityProductLabel.setText(String.valueOf(productDisplay.getQuantity()));
        Image image = new Image(getClass().getResource("/com/example/colabjdbcmysqlthaycan/img/" + productDisplay.getImageLink()).toExternalForm());
        imageProductImageView.setImage(image);
    }

    public void handleSearchProduct() {
        String searchQuery = searchProductUser.getText().trim();
        List<ProductDisplay> searchResults = new ArrayList<>();
        String query = "SELECT p.idProduct, p.nameProduct, p.productDescription, p.price, p.status, p.quantity, i.idImage, i.link " +
                "FROM Products p " +
                "JOIN ImageProducts ip ON p.idProduct = ip.idProduct " +
                "JOIN Images i ON ip.idImage = i.idImage " +
                "WHERE p.nameProduct LIKE ? OR p.price LIKE ?";

        try (Connection connection = connectDB.connectionDB();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, "%" + searchQuery + "%");
            ps.setString(2, "%" + searchQuery + "%");
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                String id = resultSet.getString("idProduct");
                String name = resultSet.getString("nameProduct");
                String description = resultSet.getString("prodctDescription");
                double price = resultSet.getDouble("price");
                int quantity = resultSet.getInt("quantity");
                String status = resultSet.getString("status");
                String imageLink = resultSet.getString("link");
                searchResults.add(new ProductDisplay(id, imageLink, name, description, price, quantity, status));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        updateGridPane(searchResults);
    }

    private void updateGridPane(List<ProductDisplay> products) {
        gridPaneProductsUser.getChildren().clear();
        int column = 0;
        int row = 1;
        try {
            for (ProductDisplay product : products) {
                if ("unavailable".equals(product.getStatus())) {
                    continue;
                }
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/colabjdbcmysqlthaycan/View/Product.fxml"));
                AnchorPane productPane = loader.load();

                ProductUserController controller = loader.getController();
                controller.setProductItem(product);

                productPane.setOnMouseClicked(event -> getItemProducts(product));
                gridPaneProductsUser.add(productPane, column++, row);

                if (column == 4) {
                    column = 0;
                    row++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void reduceQuantity() {
        int currentQuantity = Integer.parseInt(quantityProductTextField.getText());
        if (currentQuantity > 1) {
            currentQuantity--;
            quantityProductTextField.setText(String.valueOf(currentQuantity));
        }
    }

    @FXML
    private void addQuantity() {
        int currentQuantity = Integer.parseInt(quantityProductTextField.getText());
        currentQuantity++;
        quantityProductTextField.setText(String.valueOf(currentQuantity));
    }

    public void addToOrderAndProductOrder(String idUser, String orderDate, String paymentStatus, String idProduct, int quantity) {
        Connection connection = connectDB.connectionDB();
        PreparedStatement preparedStatement;
        String addToOrder = "insert into `Order` (idUser, orderDate, paymentStatus) VALUES (?, ?, ?)";

        try {
            preparedStatement = connection.prepareStatement(addToOrder, PreparedStatement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, Integer.parseInt(idUser));
            preparedStatement.setString(2, orderDate);
            preparedStatement.setString(3, paymentStatus);

            int row = preparedStatement.executeUpdate();
            if (row > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        String idOrder = String.valueOf(generatedKeys.getInt(1));
                        String addToProductOrder = "insert into ProductOrder (idProduct, idOrder, quantity) values (?, ?, ?)";
                        try (PreparedStatement preparedStatementPO = connection.prepareStatement(addToProductOrder)) {
                            preparedStatementPO.setInt(1, Integer.parseInt(idProduct));
                            preparedStatementPO.setInt(2, Integer.parseInt(idOrder));
                            preparedStatementPO.setInt(3, quantity);

                            preparedStatementPO.executeUpdate();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    public void addToProductOrder(String idProduct, String idOrder, int quantity) {
//        Connection connection = connectDB.connectionDB();
//        PreparedStatement preparedStatement;
//        String addToOrder = "INSERT INTO ProductOrder (idProduct, idOrder, quantity) VALUES (?, ?, ?)";
//
//        try {
//            preparedStatement = connection.prepareStatement(addToOrder);
//
//            preparedStatement.setString(1, idProduct);
//            preparedStatement.setString(2, idOrder);
//            preparedStatement.setInt(3, quantity);
//
//            preparedStatement.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

//    public void addToCart(String idUser, String orderDate, String paymentStatus, String idProduct, String idOrder, int quantity) {
//        addToOrder(idUser, orderDate, paymentStatus);
//        addToProductOrder(idProduct, idOrder, quantity);
//    }

    private void showAlert(String tiltle, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(tiltle);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void handleAddToOrder() {
        if (idProductLabel.getText().isEmpty() || quantityProductTextField.getText().isEmpty()) {
            showAlert("ERROR", "Please select a product");
            return;
        }
        String idUser = Session.getLoggedInCustomerId();

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String orderDate = currentDate.format(formatter);

        String paymentStatus = "Pending";

        String idProduct = idProductLabel.getText();
        int quantity = Integer.parseInt(quantityProductTextField.getText());
        addToOrderAndProductOrder(idUser, orderDate, paymentStatus, idProduct, quantity);
        showAlert("Success", "Product successfully added to cart");
    }

    public void addToCart(String idUser, String idProduct, int quantity) {
        Connection connection = connectDB.connectionDB();
        PreparedStatement preparedStatement;
        String addToCart = "insert into Cart (idUser, idProduct, quantity) VALUES (?, ?, ?)";

        try {
            preparedStatement = connection.prepareStatement(addToCart);

            preparedStatement.setInt(1, Integer.parseInt(idUser));
            preparedStatement.setInt(2, Integer.parseInt(idProduct));
            preparedStatement.setInt(3, quantity);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void handleAddToCart() {
        if (idProductLabel.getText().isEmpty() || quantityProductTextField.getText().isEmpty()) {
            showAlert("ERROR", "Please select a product");
            return;
        }
        int idUser = Integer.parseInt(Session.getLoggedInCustomerId());
        String idUserString = Session.getLoggedInCustomerId();
        int idProduct = Integer.parseInt(idProductLabel.getText());
        String idProductString = idProductLabel.getText();
        int quantity = Integer.parseInt(quantityProductTextField.getText());
        int[] checkID = selectIdUserIdProductFromCart(idUserString,idProductString);
        if (checkID != null) {
            int idUserCheck = checkID[0];
            int idProductCheck = checkID[1];
            int quantityCheck = checkID[2];
            if (idUserCheck == idUser) {
                if (idProductCheck == idProduct) {
                    int total = quantity + quantityCheck;
                    addQuantityCart(idUserString,total,idProductString);
                    showAlert("Success", "Product successfully added to cart");
                    return;
                }
            }
        }
        addToCart(idUserString, idProductString, quantity);
        showAlert("Success", "Product successfully added to cart");
    }

    public int[] selectIdUserIdProductFromCart(String idUserCheck, String idProductCheck) {
        Connection connection = connectDB.connectionDB();
        PreparedStatement preparedStatement;
        String id = "SELECT idUser, idProduct, quantity \n" +
                "FROM Cart \n" +
                "WHERE idUser = ? AND idProduct = ?; \n";
        try {
            preparedStatement = connection.prepareStatement(id);
            preparedStatement.setInt(1, Integer.parseInt(idUserCheck));
            preparedStatement.setInt(2, Integer.parseInt(idProductCheck));
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int idUser = resultSet.getInt("idUser");
                int idProduct = resultSet.getInt("idProduct");
                int quantity = resultSet.getInt("quantity");

                return new int[]{idUser, idProduct, quantity};
            } else {
                return null;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void addQuantityCart(String idUser, int quantity,String idProduct) {
        Connection connection = connectDB.connectionDB();
        PreparedStatement preparedStatement;
        String add = "UPDATE Cart SET quantity = ? WHERE idProduct = ? AND idUser = ?";

        try {
            preparedStatement = connection.prepareStatement(add);

            preparedStatement.setInt(1, quantity);
            preparedStatement.setInt(2, Integer.parseInt(idProduct));
            preparedStatement.setInt(3, Integer.parseInt(idUser));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
