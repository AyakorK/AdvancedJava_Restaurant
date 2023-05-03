package com.coding.restaurant.restaurant.models;

public class Bill {
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    // 	UUID	Type	Amount	BillDate
    private String type;
    private double amount;
    private String billDate;

    public Bill(String type, double amount, String billDate) {
        this.type = type;
        this.amount = amount;
        this.billDate = billDate;
    }



}
