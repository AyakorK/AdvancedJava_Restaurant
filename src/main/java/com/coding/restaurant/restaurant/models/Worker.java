package com.coding.restaurant.restaurant.models;

import java.util.Date;

public class Worker {
    // Worker is composed by Name	FirstName	isActive	HoursWorked	Role	ArrivalDate
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getHoursWorked() {
        return hoursWorked;
    }

    public void setHoursWorked(int hoursWorked) {
        this.hoursWorked = hoursWorked;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Date getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public String getWorkerUUID() {
        return workerUUID;
    }
    private String firstName;

    private String workerUUID;

    private boolean isActive;
    private int hoursWorked;
    private String role;
    private Date arrivalDate;


    private Date departureDate;

    public Worker(String workerUUID ,String name, String firstName, boolean isActive, int hoursWorked, String role, Date arrivalDate, Date departureDate) {
        this.workerUUID = workerUUID;
        this.name = name;
        this.firstName = firstName;
        this.isActive = isActive;
        this.hoursWorked = hoursWorked;
        this.role = role;
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
    }
}
