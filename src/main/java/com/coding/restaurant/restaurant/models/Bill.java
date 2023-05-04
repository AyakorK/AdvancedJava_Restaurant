package com.coding.restaurant.restaurant.models;

import java.util.Date;

public class Bill {
  public Boolean getType() {
    return type;
  }

  public void setType(Boolean type) {
    this.type = type;
  }

  public double getAmount() {
    return amount;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }

  public Date getBillDate() {
    return billDate;
  }

  public void setBillDate(Date billDate) {
    this.billDate = billDate;
  }

  // 	UUID	Type	Amount	BillDate
  private Boolean type;
  private double amount;
  private Date billDate;

  public Bill(Boolean type, double amount, Date billDate) {
    this.type = type;
    this.amount = amount;
    this.billDate = billDate;
  }


}
