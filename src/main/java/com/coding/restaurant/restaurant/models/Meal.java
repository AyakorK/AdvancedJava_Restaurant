package com.coding.restaurant.restaurant.models;

public class Meal {
  public String getName() {
    return name;
  }


  public void setName(String name) {
    this.name = name;
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

  public void setPrice(double price) {
    this.price = price;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public boolean isActive() {
    return isActive;
  }

  public void setActive(boolean active) {
    isActive = active;
  }

  private String name;
  private String description;
  private double price;
  private String image;
  private boolean isActive;


  public Meal(String name, String description, double price, String image, boolean isActive) {
    this.name = name;
    this.description = description;
    this.price = price;
    this.image = image;
    this.isActive = isActive;
  }
}
