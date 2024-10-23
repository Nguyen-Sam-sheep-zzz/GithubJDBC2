package com.example.colabjdbcmysqlthaycan.Class;

public class ImageProducts {
    private int idProduct;
    private int idImage;
    private int quantity;

    public ImageProducts() {

    }

    public ImageProducts(int idProduct, int idImage, int quantity) {
        this.idProduct = idProduct;
        this.idImage = idImage;
        this.quantity = quantity;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public int getIdImage() {
        return idImage;
    }

    public void setIdImage(int idImage) {
        this.idImage = idImage;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "ImageProducts{" +
                "idProduct=" + idProduct +
                ", idImage=" + idImage +
                ", quantity=" + quantity +
                '}';
    }
}
