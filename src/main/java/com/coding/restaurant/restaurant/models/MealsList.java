package com.coding.restaurant.restaurant.models;

import java.util.Date;
import java.util.List;

public class MealsList {
    public List<Meal> getMeals() {
        return meals;
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
    }

    public Date getMenuDate() {
        return menuDate;
    }

    public void setMenuDate(Date menuDate) {
        this.menuDate = menuDate;
    }

    private List<Meal> meals;

    private Date menuDate;

    public MealsList(List<Meal> meals, Date menuDate) {
        this.meals = meals;
        this.menuDate = menuDate;
    }
}
