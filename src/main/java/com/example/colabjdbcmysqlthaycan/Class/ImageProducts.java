package com.example.colabjdbcmysqlthaycan.Class;

public class ImageProducts {
    private int idProduct;
    private int idImage;

    public ImageProducts() {

    }

    public ImageProducts(int idProduct, int idImage) {
        this.idProduct = idProduct;
        this.idImage = idImage;
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

    @Override
    public String toString() {
        return "ImageProducts{" +
                "idProduct=" + idProduct +
                ", idImage=" + idImage +
                '}';
    }
}
