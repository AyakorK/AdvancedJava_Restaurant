package com.coding.restaurant.restaurant.models;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

/**
 * Model of the order
 * @param table : table of the order (Table)
 * @param isWaiting : is the order waiting (boolean)
 * @param isDelivered : is the order delivered (boolean)
 * @param orderDate : date of the order (Timestamp)
 * @param meals : list of the meals of the order (List<HashMap>)
 */
public class Order {
  public Table getTable() {
    return table;
  }

  public boolean isWaiting() {
    return isWaiting;
  }

  public void setWaiting(boolean waiting) {
    this.isWaiting = waiting;
  }

  public boolean isDelivered() {
    return isDelivered;
  }

  public void setDelivered(boolean delivered) {
    this.isDelivered = delivered;
  }


  public Timestamp getOrderDate() {
    return orderDate;
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
  private final Table table;
  private boolean isWaiting;
  private boolean isDelivered;

  private final Timestamp orderDate;


  private final String orderUUID;

  private String status;

  private final List<HashMap> meals;

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
