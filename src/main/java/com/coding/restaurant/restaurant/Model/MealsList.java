package com.coding.restaurant.restaurant.Model;

import java.util.Date;

public class MealsList {
    private Meal[] meals;

    private Date menuDate;

    public MealsList(Meal[] meals, Date menuDate) {
        this.meals = meals;
        this.menuDate = menuDate;
    }
}
