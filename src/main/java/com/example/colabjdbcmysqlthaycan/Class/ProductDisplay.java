package com.example.colabjdbcmysqlthaycan.Class;

import javafx.collections.ObservableList;
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
    private String idImage;

    public ProductDisplay(String imageLink, String id, String name, String description, double price, String status, String idImage) {
        this.imageLink = imageLink;
        this.imageView = new ImageView(new Image(getClass().getResource("/com/example/colabjdbcmysqlthaycan/img/" + imageLink).toExternalForm()));
        this.imageView.setFitWidth(50);
        this.imageView.setFitHeight(37);

        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.status = status;
        this.idImage = idImage;
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
}

