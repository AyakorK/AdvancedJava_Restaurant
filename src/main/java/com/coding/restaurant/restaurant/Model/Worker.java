package com.coding.restaurant.restaurant.Model;

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

    public String getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    private String firstName;
    private boolean isActive;
    private int hoursWorked;
    private String role;
    private String arrivalDate;

    public Worker(String name, String firstName, boolean isActive, int hoursWorked, String role, String arrivalDate) {
        this.name = name;
        this.firstName = firstName;
        this.isActive = isActive;
        this.hoursWorked = hoursWorked;
        this.role = role;
        this.arrivalDate = arrivalDate;
    }

    public void addWorker(Worker worker) {
        // TODO -> add worker to database
    }

    public void removeWorker(Worker worker) {
        // TODO -> remove worker from database
    }

}
