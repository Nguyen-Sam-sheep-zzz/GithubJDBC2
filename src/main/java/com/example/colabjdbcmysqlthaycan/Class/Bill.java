package com.example.colabjdbcmysqlthaycan.Class;

import java.sql.Date;

public class Bill {
    private int idBill;          
    private int idOrder;         
    private int idUser;          
    private Date issueDate;      
    private double totalAmount; 

    public Bill() {}

    public Bill(int idBill, int idOrder, int idUser, Date issueDate, double totalAmount) {
        this.idBill = idBill;
        this.idOrder = idOrder;
        this.idUser = idUser;
        this.issueDate = issueDate;
        this.totalAmount = totalAmount;
    }


    public int getIdBill() {
        return idBill;
    }

    public void setIdBill(int idBill) {
        this.idBill = idBill;
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

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Override
    public String toString() {
        return "Bill{" +
                "idBill=" + idBill +
                ", idOrder=" + idOrder +
                ", idUser=" + idUser +
                ", issueDate=" + issueDate +
                ", totalAmount=" + totalAmount +
                '}';
    }
}

