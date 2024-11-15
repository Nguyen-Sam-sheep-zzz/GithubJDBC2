package com.example.colabjdbcmysqlthaycan.Class;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.sql.Date;


public class ProductDisplay {
    private String imageLink;
    private String id;
    private String name;
    private String description;
    private double price;
    private String status;
    private ImageView imageView;
    private ImageView imageViewStatus;
    private String idImage;
    private int quantity;
    private int idCart;
    private boolean checkBox;
    private int idOrder;
    private PaymentStatus paymentStatus;
    private double amount;
    private Button confirmButton;
    private Button cancelButton;
    private Date orderDate;
    private int idBill;
    private Date issueDate;
    private String nameUser;
    private int idUser;

    public ProductDisplay() {
    }

    public ProductDisplay(int idBill, Date issueDate, int quantity, String nameProduct, String imageLink, double price, Date orderDate, int idUser, String nameUser) {
        this.idBill = idBill;
        this.issueDate = issueDate;
        this.quantity = quantity;
        this.name = nameProduct;
        this.price = price;
        this.imageLink = imageLink;
        this.imageView = new ImageView(new Image(getClass().getResource("/com/example/colabjdbcmysqlthaycan/img/" + imageLink).toExternalForm()));
        this.imageView.setFitWidth(60);
        this.imageView.setFitHeight(47);
        this.orderDate = orderDate;
        this.idUser = idUser;
        this.nameUser = nameUser;


    }
    public ProductDisplay(String id, String imageLink, String name, String description, double price, int quantity, String status, String idImage) {
        this.id = id;
        this.imageLink = imageLink;
        this.imageView = new ImageView(new Image(getClass().getResource("/com/example/colabjdbcmysqlthaycan/img/" + imageLink).toExternalForm()));
        this.imageView.setFitWidth(60);
        this.imageView.setFitHeight(47);

        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.status = status;
        if ("available".equals(status)) {
            this.imageViewStatus = new ImageView(new Image(getClass().getResource("/com/example/colabjdbcmysqlthaycan/img/DauV.png").toExternalForm()));
        } else if ("unavailable".equals(status)) {
            this.imageViewStatus = new ImageView(new Image(getClass().getResource("/com/example/colabjdbcmysqlthaycan/img/DauX.png").toExternalForm()));
        }
        this.imageViewStatus.setFitWidth(20);
        this.imageViewStatus.setFitHeight(20);
        this.idImage = idImage;
    }

    public ProductDisplay(String id, String imageLink, String name, String description, double price, int quantity, String status) {
        this.id = id;
        this.imageLink = imageLink;
        this.imageView = new ImageView(new Image(getClass().getResource("/com/example/colabjdbcmysqlthaycan/img/" + imageLink).toExternalForm()));
        this.imageView.setFitWidth(50);
        this.imageView.setFitHeight(37);

        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.status = status;

    }

    public ProductDisplay(Date issueDate, int quantity, String nameProduct, String link, double price, Date orderDate) {
        this.issueDate = issueDate;
        this.quantity = quantity;
        this.name = nameProduct;
        this.price = price;
        this.imageLink = link;
        this.imageView = new ImageView(new Image(getClass().getResource("/com/example/colabjdbcmysqlthaycan/img/" + imageLink).toExternalForm()));
        this.imageView.setFitWidth(60);
        this.imageView.setFitHeight(47);
        this.orderDate = orderDate;
    }



    public boolean getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(boolean checkBox) {
        this.checkBox = checkBox;
    }

    public ProductDisplay(String imageLink, String name, double price, int quantity) {
        this.imageLink = imageLink;
        this.imageView = new ImageView(new Image(getClass().getResource("/com/example/colabjdbcmysqlthaycan/img/" + imageLink).toExternalForm()));
        this.imageView.setFitWidth(50);
        this.imageView.setFitHeight(37);

        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public ProductDisplay(String id, String imageLink, String name, double price, int quantity, int idCart, boolean checkBox) {
        this.imageLink = imageLink;
        this.imageView = new ImageView(new Image(getClass().getResource("/com/example/colabjdbcmysqlthaycan/img/" + imageLink).toExternalForm()));
        this.imageView.setFitWidth(50);
        this.imageView.setFitHeight(37);

        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.idCart = idCart;
        this.checkBox = checkBox;
    }

    public ProductDisplay(String imageLink, String name, int quantity, int idOrder, PaymentStatus paymentStatus,double price,Date orderDate) {
        this.imageLink = imageLink;
        this.imageView = new ImageView(new Image(getClass().getResource("/com/example/colabjdbcmysqlthaycan/img/" + imageLink).toExternalForm()));
        this.imageView.setFitWidth(75);
        this.imageView.setFitHeight(75);

        this.name = name;
        this.quantity = quantity;
        this.idOrder = idOrder;
        this.paymentStatus = paymentStatus;
        this.price = price;
        this.orderDate = orderDate;
    }

    public ProductDisplay(int idOrder, String imageLink, String nameProduct, double price, int quantity, PaymentStatus paymentStatus,int idUser,String nameUser) {
        this.imageLink = imageLink;
        this.imageView = new ImageView(new Image(getClass().getResource("/com/example/colabjdbcmysqlthaycan/img/" + imageLink).toExternalForm()));
        this.imageView.setFitWidth(50);
        this.imageView.setFitHeight(37);

        this.name = nameProduct;
        this.quantity = quantity;
        this.idOrder = idOrder;
        this.paymentStatus = paymentStatus;
        this.price = price;
        this.idUser = idUser;
        this.nameUser = nameUser;
        this.confirmButton = new Button("confirm");
        this.cancelButton = new Button("cancel");
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getIdBill() {
        return idBill;
    }

    public void setIdBill(int idBill) {
        this.idBill = idBill;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public boolean isCheckBox() {
        return checkBox;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Button getConfirmButton() {
        return confirmButton;
    }

    public void setConfirmButton(Button confirmButton) {
        this.confirmButton = confirmButton;
    }

    public Button getCancelButton() {
        return cancelButton;
    }

    public void setCancelButton(Button cancelButton) {
        this.cancelButton = cancelButton;
    }

    public enum PaymentStatus {
        Pending,
        Paid,
        Cancelled
    }
        public PaymentStatus getPaymentStatus() {
            return paymentStatus;
    }
    public int getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(int idOrder) {
        this.idOrder = idOrder;
    }

    public int getIdCart() {
        return idCart;
    }

    public void setIdCart(int idCart) {
        this.idCart = idCart;
    }

    public String getIdImage() {
        return idImage;
    }

    public void setIdImage(String idImage) {
        this.idImage = idImage;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public ImageView getImageViewStatus() {
        return imageViewStatus;
    }

    public void setImageViewStatus(ImageView imageViewStatus) {
        this.imageViewStatus = imageViewStatus;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getAmount() {
        return quantity * price;
    }
}

