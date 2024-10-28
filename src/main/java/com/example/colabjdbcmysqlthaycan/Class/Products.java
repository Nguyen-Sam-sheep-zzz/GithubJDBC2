package com.example.colabjdbcmysqlthaycan.Class;

public class Products {
    private int idProduct;
    private String nameProduct;
    private String productDescription;
    private double price;
    private String status;

    public Products() {

    }

    public Products(int idProduct, String nameProduct, String productDescription, double price, String status) {
        this.idProduct = idProduct;
        this.nameProduct = nameProduct;
        this.productDescription = productDescription;
        this.price = price;
        this.status = status;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
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

    @Override
    public String toString() {
        return "Product{" +
                "idProduct=" + idProduct +
                ", nameProduct='" + nameProduct + '\'' +
                ", productDescription='" + productDescription + '\'' +
                ", price=" + price +
                ", status='" + status + '\'' +
                '}';
    }
}

