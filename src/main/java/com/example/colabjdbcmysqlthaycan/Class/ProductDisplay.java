package com.example.colabjdbcmysqlthaycan.Class;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class ProductDisplay {
    private String imageLink;
    private int id;
    private String name;
    private String description;
    private double price;
    private String status;
    private ImageView imageView;

    public ProductDisplay(String imageLink, int id, String name, String description, double price, String status) {
        this.imageLink = imageLink;
        this.imageView = new ImageView(new Image(getClass().getResource("/com/example/colabjdbcmysqlthaycan/img/" + imageLink).toExternalForm()));

        this.imageView.setFitWidth(50);
        this.imageView.setFitHeight(35);

        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.status = status;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

