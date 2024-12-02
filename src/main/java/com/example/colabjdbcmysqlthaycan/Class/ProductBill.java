package com.example.colabjdbcmysqlthaycan.Class;

public class ProductBill {
    private int idBill;
    private int idProduct;
    private int quantity;

    public ProductBill() {
    }

    public ProductBill(int idBill, int idProduct, int quantity) {
        this.idBill = idBill;
        this.idProduct = idProduct;
        this.quantity = quantity;
    }

    public int getIdBill() {
        return idBill;
    }

    public void setIdBill(int idBill) {
        this.idBill = idBill;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "BillProduct{" +
                "idBill=" + idBill +
                ", idProduct=" + idProduct +
                ", quantity=" + quantity +
                '}';
    }
}
