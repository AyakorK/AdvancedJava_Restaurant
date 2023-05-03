package com.coding.restaurant.restaurant.Controller;

import com.coding.restaurant.restaurant.Model.Meal;
import com.coding.restaurant.restaurant.Model.MealsList;
import com.coding.restaurant.restaurant.Model.Order;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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