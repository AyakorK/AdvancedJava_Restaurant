package com.coding.restaurant.restaurant.Controller;

import com.coding.restaurant.restaurant.Model.Meal;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MealController implements Initializable {


  @FXML
  private Button btnMeal;

  public List<Meal> getAllMeal() {
    try (Connection connexion = ConnectDatabaseController.getConnexion();
         PreparedStatement statement = connexion.prepareStatement("SELECT * FROM Meal");
         ResultSet resultat = statement.executeQuery()) {

      List<Meal> meals = new ArrayList<>();
      while (resultat.next()) {
        Meal meal = new Meal(resultat.getString("name"), resultat.getString("description"), resultat.getDouble("price"), resultat.getString("image"), resultat.getBoolean("isActive"));
        meals.add(meal);
      }
      return meals;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public void displayMeal() {
    List<Meal> meals = getAllMeal();
    meals.stream().forEach(m -> System.out.println(m.getName() + ' ' + m.getDescription() + ' ' + m.getPrice() + ' ' + m.getImage() + ' ' + m.isActive()));
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    btnMeal.setOnAction(event -> {
      displayMeal();
    });
  }
}
