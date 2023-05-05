package com.coding.restaurant.restaurant.models;

import java.util.Date;

public class Bill {
  public Boolean getType() {
    return type;
  }

  public double getAmount() {
    return amount;
  }

  public Date getBillDate() {
    return billDate;
  }

  // 	UUID	Type	Amount	BillDate
  private final Boolean type;
  private final double amount;
  private final Date billDate;

  /**
   * 
   * @param type
   * @param amount
   * @param billDate
   */
  public Bill(Boolean type, double amount, Date billDate) {
    this.type = type;
    this.amount = amount;
    this.billDate = billDate;
  }


}
