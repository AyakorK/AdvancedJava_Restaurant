package com.coding.restaurant.restaurant.models;

import com.coding.restaurant.restaurant.controllers.DatabaseManager;

import java.sql.SQLException;
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

//  public void setOrderDate(Timestamp orderDate) {
//    this.orderDate = orderDate;
//  }

  public List<HashMap> getMeals() {
    return meals;
  }

//  addMeal

  /**
   *
   * @param meal
   * @param quantity
   */
  public void addMeal(Meal meal, int quantity) {
    HashMap<String, Object> mealMap = new HashMap<>();
    mealMap.put("meal", meal);
    mealMap.put("quantity", quantity);
    this.meals.add(mealMap);
  }

  /**
   *
   * @return
   */
  public double getTotal() {
    return this.meals.stream()
            .mapToDouble(meal -> ((Meal) meal.get("meal")).getPrice()
                    * (Integer) meal.get("quantity"))
            .reduce(0, Double::sum);
  }

  /**
   *
   * @return
   */
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
    String seconds = String.valueOf(diff / 1000 % 60);
    if (seconds.length() == 1) seconds = "0" + seconds;
    String minutes = String.valueOf(diff / (60 * 1000) % 60);
    if (minutes.length() == 1) minutes = "0" + minutes;

    return  minutes + ":" + seconds;
  }

  /**
   *
   * @return
   */
  public String getOrderUUID() {
    return orderUUID;
  }

  /**
   *
   * @param status
   */
  public void setStatus(String status) {
    this.status = status;
  }

  /**
   *
   * @return
   */
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

  /**
   * 
   * @param orderUUID
   * @param table
   * @param isWaiting
   * @param isDelivered
   * @param meals
   * @param orderDate
   * @param status
   */
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
