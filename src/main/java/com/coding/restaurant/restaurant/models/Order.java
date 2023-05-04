package com.coding.restaurant.restaurant.models;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

public class Order {
  public Table getTable() {
    return table;
  }

  public boolean isWaiting() {
    return isWaiting;
  }

  public boolean setWaiting(boolean waiting) {
    return this.isWaiting = waiting;
  }

  public boolean isDelivered() {
    return isDelivered;
  }

  public boolean setDelivered(boolean delivered) {
    return this.isDelivered = delivered;
  }


  public Timestamp getOrderDate() {
    return orderDate;
  }

  public Timestamp setOrderDate(Timestamp orderDate) {
    return this.orderDate = orderDate;
  }

  public List<HashMap> getMeals() {
    return meals;
  }

  public double getTotal() {
    return this.meals.stream()
            .mapToDouble(meal -> ((Meal) meal.get("meal")).getPrice()
                    * (Integer) meal.get("quantity"))
            .reduce(0, Double::sum);
  }

  public String getTimer() {
    // It should return the time between the order date and now (max 25 minutes after the order)
    Timestamp now = new Timestamp(System.currentTimeMillis());
    // We need to get order date in milliseconds and + 25 minutes in milliseconds
    long maxTime = this.orderDate.getTime() + (25 * 60 * 1000);
    long nowTime = now.getTime();

    // If the nowTime is greater than the maxTime, we return "out of time"
    if (nowTime > maxTime) {
      return "Out of time";
    }

    // Else we calculate the difference between the maxTime and the nowTime
    long diff = maxTime - nowTime;
    // And we return the difference in minutes and seconds
    return String.format("%d:%d", diff / 1000 / 60, diff / 1000 % 60);
  }

  public void setTable(Table table) {
    this.table = table;
  }

  public String getOrderUUID() {
    return orderUUID;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getStatus() {
    return this.status;
  }


  // 	UUID	TableID	isWaiting	isDelivered
  private Table table;
  private boolean isWaiting;
  private boolean isDelivered;

  private Timestamp orderDate;


  private String orderUUID;

  private String status;

  private List<HashMap> meals;

  public Order(String orderUUID, Table table, boolean isWaiting, boolean isDelivered, List<HashMap> meals, Timestamp orderDate, String status) {
    this.orderUUID = orderUUID;
    this.table = table;
    this.isWaiting = isWaiting;
    this.isDelivered = isDelivered;
    this.meals = meals;
    this.orderDate = orderDate;
    this.status = status;
  }
}
