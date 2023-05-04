package com.coding.restaurant.restaurant.Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NewMealController implements Initializable {

  @FXML
  private Button btnValidateMeal;

  @FXML
  private TextField txfName;

  @FXML
  private TextField txfDescription;

  @FXML
  private TextField txfPrice;

  @FXML
  private TextField txfImage;

  @FXML
  private CheckBox cbxIsActive;

  @FXML
  private TextField txfType;


  public void addMeal(String name, String description, Double price, String image, Boolean isActive, String type) {
    try (Connection connexion = ConnectDatabaseController.getConnection();
         PreparedStatement statement = connexion.prepareStatement("INSERT INTO Meal (UUID, name, description, price, image, isActive,Type) VALUES (?,?,?, ?, ?, ?, ?)")) {
      statement.setString(1, generateUUID());
      statement.setString(2, name);
      statement.setString(3, description);
      statement.setDouble(4, price);
      statement.setString(5, image);
      statement.setBoolean(6, isActive);
      statement.setString(7, type);
      statement.executeUpdate();
      System.out.println("Meal added");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  //  function generate UUID
  public static String generateUUID() {
    return java.util.UUID.randomUUID().toString();
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    btnValidateMeal.setOnAction(event -> {
      String name = txfName.getText();
      String description = txfDescription.getText();
      Double price = Double.parseDouble(txfPrice.getText());
      String image = txfImage.getText();
      Boolean isActive = cbxIsActive.isSelected();
      String type = txfType.getText();

      System.out.println(name + ' ' + description + ' ' + price + ' ' + image + ' ' + isActive + ' ' + type);
      addMeal(name, description, price, image, isActive, type);
//      txfName.getText();
//      System.out.println(txfName.getText());
    });
  }
}
