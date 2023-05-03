package com.coding.restaurant.restaurant.Model;

import javafx.scene.control.Tab;

public class Order {
    // 	UUID	TableID	isWaiting	isDelivered
    private Table table;
    private boolean isWaiting;
    private boolean isDelivered;

    private Meal[] meals;

    public Order(Table table, boolean isWaiting, boolean isDelivered, Meal[] meals) {
        this.table = table;
        this.isWaiting = isWaiting;
        this.isDelivered = isDelivered;
        this.meals = meals;
    }
}
