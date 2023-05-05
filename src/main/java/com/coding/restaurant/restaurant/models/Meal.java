package com.coding.restaurant.restaurant.models;

public class Meal {
  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public double getPrice() {
    return price;
  }

  public String getPriceString() {
    return String.format("%.2f", getPrice());
  }

  public String getImage() {
    return image;
  }

  public boolean isActive() {
    return isActive;
  }

  private final String name;
  private String description;
  private final double price;
  private final String image;
  private final boolean isActive;

  private String mealUUID;

  public String getMealUUID() {
    return mealUUID;
  }

  /**
   * 
   * @param name
   * @param description
   * @param price
   * @param image
   * @param isActive
   * @param mealUUID
   */
  public Meal(String name, String description, double price, String image, boolean isActive, String mealUUID) {
    this.mealUUID = mealUUID;
    this.name = name;
    this.description = description;
    this.price = price;
    this.image = image;
    this.isActive = isActive;
  }
}
