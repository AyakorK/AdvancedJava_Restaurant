package com.coding.restaurant.restaurant.models;

import java.util.Date;

/**
 * Model of the bill
 * @param type : type of the bill (true = paid, false = dropped)
 * @param amount : amount of the bill (double)
 * @param billDate : date of the bill (Date)
 */
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

  public Bill(Boolean type, double amount, Date billDate) {
    this.type = type;
    this.amount = amount;
    this.billDate = billDate;
  }


}
