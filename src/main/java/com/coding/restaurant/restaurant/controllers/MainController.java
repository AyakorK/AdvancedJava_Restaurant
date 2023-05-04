package com.coding.restaurant.restaurant.controllers;

import com.coding.restaurant.restaurant.models.Meal;
import com.coding.restaurant.restaurant.models.MealsList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class MainController {
  @FXML
  private Label welcomeText;

  @FXML
  protected void onHelloButtonClick() throws SQLException {
//        welcomeText.setText("Welcome to JavaFX Application!");
    listMeal();
  }

  public void listMeal() throws SQLException {
    DatabaseManager db = new DatabaseManager();
    List<MealsList> mealsLists = db.getMealsList();
    List<Meal> meals = mealsLists.stream().map(MealsList::getMeals).flatMap(List::stream).collect(Collectors.toList());
    meals.stream().forEach(meal -> System.out.println(meal.getName()));

  }
}