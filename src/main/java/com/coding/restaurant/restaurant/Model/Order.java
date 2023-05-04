package com.coding.restaurant.restaurant.Model;

import javafx.scene.control.Tab;

import java.util.List;

public class Order {
    public Table getTable() {
        return table;
    }

    public boolean isWaiting() {
        return isWaiting;
    }

    public boolean isDelivered() {
        return isDelivered;
    }

    public List<Meal> getMeals() {
        return meals;
    }

    // 	UUID	TableID	isWaiting	isDelivered
    private Table table;
    private boolean isWaiting;
    private boolean isDelivered;

    private List<Meal> meals;

    public Order(Table table, boolean isWaiting, boolean isDelivered, List<Meal> meals) {
        this.table = table;
        this.isWaiting = isWaiting;
        this.isDelivered = isDelivered;
        this.meals = meals;
    }
}
