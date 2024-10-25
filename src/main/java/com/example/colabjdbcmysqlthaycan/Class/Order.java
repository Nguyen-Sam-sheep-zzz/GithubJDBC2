package com.example.colabjdbcmysqlthaycan.Class;

import java.util.Date;

public class Order {
    private int idOrder;
    private int idUser;
    private Date orderDate;
    private String paymentStatus;

    public Order() {

    }

    public Order(int idOrder, int idUser, Date orderDate, String paymentStatus) {
        this.idOrder = idOrder;
        this.idUser = idUser;
        this.orderDate = orderDate;
        this.paymentStatus = paymentStatus;
    }

    public int getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(int idOrder) {
        this.idOrder = idOrder;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    // Override phương thức toString() để in thông tin đối tượng Order
    @Override
    public String toString() {
        return "Order{" +
                "idOrder=" + idOrder +
                ", idUser=" + idUser +
                ", orderDate=" + orderDate +
                ", paymentStatus='" + paymentStatus + '\'' +
                '}';
    }
}
