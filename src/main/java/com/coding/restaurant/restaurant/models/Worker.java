package com.coding.restaurant.restaurant.models;

import java.util.Date;

public class Worker {
  // Worker is composed by Name	FirstName	isActive	HoursWorked	Role	ArrivalDate
  private String name;

  public String getName() {
    return name;
  }

  public String getFirstName() {
    return firstName;
  }

  public boolean isActive() {
    return isActive;
  }

  public int getHoursWorked() {
    return hoursWorked;
  }

  public String getRole() {
    return role;
  }

  public Date getArrivalDate() {
    return arrivalDate;
  }

  public Date getDepartureDate() {
    return departureDate;
  }

  public String getWorkerUUID() {
    return workerUUID;
  }

  public int getAge() {
    return age;
  }

  private final String firstName;

  private final String workerUUID;

  private final boolean isActive;
  private final int hoursWorked;
  private final String role;
  private final Date arrivalDate;

  private final int age;
  private final Date departureDate;

  /**
   * 
   * @param workerUUID
   * @param name
   * @param firstName
   * @param isActive
   * @param hoursWorked
   * @param role
   * @param arrivalDate
   * @param departureDate
   * @param age
   */
  public Worker(String workerUUID, String name, String firstName, boolean isActive, int hoursWorked, String role, Date arrivalDate, Date departureDate, int age) {
    this.workerUUID = workerUUID;
    this.name = name;
    this.firstName = firstName;
    this.isActive = isActive;
    this.hoursWorked = hoursWorked;
    this.role = role;
    this.arrivalDate = arrivalDate;
    this.departureDate = departureDate;
    this.age = age;
  }
}
