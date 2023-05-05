package com.coding.restaurant.restaurant.models;

/**
 * Model of the meal
 * @param name : name of the meal (String)
 * @param description : description of the meal (String)
 * @param price : price of the meal (double)
 * @param image : image of the meal (String)
 * @param isActive : is the meal active (boolean)
 */
public class Meal {
  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public
  double getPrice() {
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
  private final String description;
  private final double price;
  private final String image;
  private final boolean isActive;


  public Meal(String name, String description, double price, String image, boolean isActive) {
    this.name = name;
    this.description = description;
    this.price = price;
    this.image = image;
    this.isActive = isActive;
  }
}
