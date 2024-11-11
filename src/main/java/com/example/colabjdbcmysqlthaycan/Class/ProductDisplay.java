package com.example.colabjdbcmysqlthaycan.Class;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


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
    private int idOrder;
    private String paymentStatus;
    private double amount;

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

    public ProductDisplay(String imageLink, String name, double price, int quantity) {
        this.imageLink = imageLink;
        this.imageView = new ImageView(new Image(getClass().getResource("/com/example/colabjdbcmysqlthaycan/img/" + imageLink).toExternalForm()));
        this.imageView.setFitWidth(50);
        this.imageView.setFitHeight(37);

        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public ProductDisplay(String imageLink, String name, double price, int quantity, int idCart) {
        this.imageLink = imageLink;
        this.imageView = new ImageView(new Image(getClass().getResource("/com/example/colabjdbcmysqlthaycan/img/" + imageLink).toExternalForm()));
        this.imageView.setFitWidth(50);
        this.imageView.setFitHeight(37);

        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.idCart = idCart;

    }

    public ProductDisplay(String imageLink, String name, int quantity, int idOrder, String paymentStatus,double price) {
        this.imageLink = imageLink;
        this.imageView = new ImageView(new Image(getClass().getResource("/com/example/colabjdbcmysqlthaycan/img/" + imageLink).toExternalForm()));
        this.imageView.setFitWidth(75);
        this.imageView.setFitHeight(75);

        this.name = name;
        this.quantity = quantity;
        this.idOrder = idOrder;
        this.paymentStatus = paymentStatus;
        this.price = price;
    }

    public ProductDisplay(int idOrder, String link, String nameProduct, double price, int quantity, String paymentStatus) {
        this.imageLink = link;
        this.imageView = new ImageView(new Image(getClass().getResource("/com/example/colabjdbcmysqlthaycan/img/" + imageLink).toExternalForm()));
        this.imageView.setFitWidth(75);
        this.imageView.setFitHeight(75);

        this.name = nameProduct;
        this.quantity = quantity;
        this.idOrder = idOrder;
        this.paymentStatus = paymentStatus;
        this.price = price;
    }

    public String getpaymentStatus() {
        return paymentStatus;
    }

    public void setpaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
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

