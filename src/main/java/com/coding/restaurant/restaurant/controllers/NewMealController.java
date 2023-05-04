package com.coding.restaurant.restaurant.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

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
    DatabaseManager.addMeal(name, description, price, image, isActive, type);
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

      addMeal(name, description, price, image, isActive, type);
    });
  }
}

