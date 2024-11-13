package com.example.colabjdbcmysqlthaycan.Controller;

import com.example.colabjdbcmysqlthaycan.Application.LoginApplication;
import com.example.colabjdbcmysqlthaycan.Class.ProductDisplay;
import com.example.colabjdbcmysqlthaycan.Class.Session;
import com.example.colabjdbcmysqlthaycan.ConnectDB;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
import java.util.Optional;

public class CartController {

    ConnectDB connectDB = new ConnectDB();
    @FXML
    private Button buttonSingOut;
    @FXML
    private GridPane gridPaneProductsUser;
    @FXML
    private Button homeUserButton;
    @FXML
    private Label totalPriceLabel;
    @FXML
    private Label deleteAllProductHomeUser;

    public CheckBox getSelectAllProductCartUserCheckBox() {
        return selectAllProductCartUserCheckBox;
    }

    @FXML
    protected CheckBox selectAllProductCartUserCheckBox;
    @FXML
    protected CheckBox selectAllProductCartUserCheckBox1;
    @FXML
    private Label selectAllProductUserCartLabel;

    private List<ProductDisplay> productsCart = new ArrayList<>();

    private List<ProductDisplay> selectProducts = new ArrayList<>();

    private List<CheckBox> productFullCheckBox = new ArrayList<>();
    @FXML
    private Button homeOrder;

    public void initialize() {
        selectAllProductCartUserCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            selectAllProductCartUserCheckBox1.setSelected(newValue);
        });
        selectAllProductCartUserCheckBox1.selectedProperty().addListener((observable, oldValue, newValue) -> {
            selectAllProductCartUserCheckBox.setSelected(newValue);
        });

        getAllProductCart();
    }

    public void loadToLoginScreenFromCart() throws IOException {
        Parent root = FXMLLoader.load(LoginApplication.class.getResource("/com/example/colabjdbcmysqlthaycan/View/Login.fxml"));
        Stage stage = (Stage) buttonSingOut.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    public void addSelectProduct(ProductDisplay productDisplay) {
        if (!selectProducts.contains(productDisplay)) {
            selectProducts.add(productDisplay);
        }
        updateTotalPrice();
    }

    public void removeSelectedProduct(ProductDisplay productDisplay) {
        selectProducts.remove(productDisplay);
        updateTotalPrice();
    }

    public void updateTotalPrice() {
        double totalPrice = selectProducts.stream()
                .mapToDouble(product -> product.getPrice() * product.getQuantity())
                .sum();
        totalPriceLabel.setText(totalPrice + " $");
    }

    public List<ProductDisplay> getProductsCart() {
        String idUser = Session.getLoggedInCustomerId();
        String query = "SELECT p.nameProduct, p.price, i.link, c.idCart, c.idProduct, c.quantity, c.status \n" +
                "FROM products p \n" +
                "JOIN ImageProducts ip ON p.idProduct = ip.idProduct \n" +
                "JOIN Images i ON ip.idImage = i.idImage \n" +
                "JOIN cart c ON c.idProduct = ip.idProduct \n" +
                "WHERE c.idUser = ?";

        try (Connection connection = connectDB.connectionDB();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, idUser);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String idProduct = resultSet.getString("idProduct");
                    String name = resultSet.getString("nameProduct");
                    double price = resultSet.getDouble("price");
                    String imageLink = resultSet.getString("link");
                    int quantity = resultSet.getInt("quantity");
                    int idCart = resultSet.getInt("idCart");
                    boolean status = resultSet.getBoolean("status");
                    productsCart.add(new ProductDisplay(idProduct, imageLink, name, price, quantity, idCart, status));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productsCart;
    }

    private boolean selectAllProduct = true;

    public void handleSelectAllProducts() {
        for (CheckBox productCheckBox : productFullCheckBox) {
            if (selectAllProduct) {
                if (!productCheckBox.isSelected()) {
                    productCheckBox.fire();
                }
            } else {
                if (productCheckBox.isSelected()) {
                    productCheckBox.fire();
                }
            }
        }
        selectAllProduct = !selectAllProduct;
        updateTotalPrice();
    }

    public void getAllProductCart() {
        List<ProductDisplay> products = getProductsCart();
        int column = 0;
        int row = 1;
        try {
            for (ProductDisplay productCart : products) {
                FXMLLoader loaderCart = new FXMLLoader(getClass().getResource("/com/example/colabjdbcmysqlthaycan/View/ProductCart.fxml"));
                AnchorPane productPane = loaderCart.load();
                ProductCartController controllerCart = loaderCart.getController();
                controllerCart.setProductItemCart(productCart, this);
                productFullCheckBox.add(controllerCart.getProductCartCheckBox());
                selectAllProductUserCartLabel.setText("Chọn tất cả (" + products.size() + ")");
                gridPaneProductsUser.add(productPane, column++, row);
                if (column == 1) {
                    column = 0;
                    row++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadToHomeUserScreenFromCart() throws IOException {
        Parent root = FXMLLoader.load(LoginApplication.class.getResource("/com/example/colabjdbcmysqlthaycan/View/HomeUser.fxml"));
        Stage stage = (Stage) homeUserButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Home user");
        stage.setScene(scene);
        stage.show();
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

    public void handleAddToOrder() throws IOException {
        for (ProductDisplay productDisplay : selectProducts) {
            String idUser = Session.getLoggedInCustomerId();

            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String orderDate = currentDate.format(formatter);

            String paymentStatus = "Pending";
            String idProduct = String.valueOf(productDisplay.getId());
            int quantity = productDisplay.getQuantity();
            addToOrderAndProductOrder(idUser, orderDate, paymentStatus, idProduct, quantity);
            deleteProductInCart(String.valueOf(productDisplay.getIdCart()));
        }
        loadToCartUserScreen();
        showAlert("Success", "Order successful");
    }

    public void deleteProductInCart(String idCart) {
        Connection connection = connectDB.connectionDB();
        PreparedStatement preparedStatement;
        String deleteProductCart = "DELETE FROM cart WHERE idCart = ?";

        try {
            preparedStatement = connection.prepareStatement(deleteProductCart);
            preparedStatement.setInt(1, Integer.parseInt(idCart));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void handleDeleteProducts() throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm deletion");
        alert.setHeaderText("Are you sure you want to delete " + selectProducts.size() + " products?");
        alert.setContentText("Select OK to delete or Cancel to keep.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            for (ProductDisplay productDisplay : selectProducts) {
                deleteProductInCart(String.valueOf(productDisplay.getIdCart()));
            }
        }
        loadToCartUserScreen();
    }


    public void loadToCartUserScreen() throws IOException {
        Parent root = FXMLLoader.load(LoginApplication.class.getResource("/com/example/colabjdbcmysqlthaycan/View/Cart.fxml"));
        Stage stage = (Stage) deleteAllProductHomeUser.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Cart user");
        stage.setScene(scene);
        stage.show();
    }


    private void showAlert(String tiltle, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(tiltle);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void loadToOrderScreenFromCart() throws IOException {
        Parent root = FXMLLoader.load(LoginApplication.class.getResource("/com/example/colabjdbcmysqlthaycan/View/Order.fxml"));
        Stage stage = (Stage) homeOrder.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Order");
        stage.setScene(scene);
        stage.show();
    }
}
