package com.coding.restaurant.restaurant.models;

public class Table {
  public String getNumber() {
    return numero;
  }

  public void setNumber(String numero) {
    this.numero = numero;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public int getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }

  public boolean isFull() {
    return isFull;
  }

  public void setFull(boolean full) {
    isFull = full;
  }

  // UUID	Numero	Location	Size	isFull
  private String numero;
  private String location;
  private int size;
  private boolean isFull;

  public Table(String numero, String location, int size, boolean isFull) {
    this.numero = numero;
    this.location = location;
    this.size = size;
    this.isFull = isFull;
  }
}
