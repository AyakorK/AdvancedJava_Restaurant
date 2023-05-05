package com.coding.restaurant.restaurant.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the new meal view
 */
public class NewMealController implements Initializable, NewFormInterface {

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

  @FXML
  private Button btnBack;

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
      goBack();
    });

    btnBack.setOnAction(event -> goBack());
  }

  // Add a new meal to the database
  public void addMeal(String name, String description, Double price, String image, Boolean isActive, String type) {
    DatabaseManager.addMeal(name, description, price, image, isActive, type);
  }

  // Go back to the menu view
  public void goBack() {
    try {
      // Load the worker view & set it as the current view
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/coding/restaurant/restaurant/menu.fxml"));
      Node workerView = loader.load();

      AnchorPane acpInWorker = (AnchorPane) btnValidateMeal.getParent().getParent().getParent();

      acpInWorker.getChildren().setAll(workerView);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

