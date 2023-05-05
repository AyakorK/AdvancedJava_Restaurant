package com.coding.restaurant.restaurant.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.util.Arrays;
import java.util.List;

/**
 * Controller of the Menu
 */
public class MenuController {
  @FXML
  private Button btnNewMeal;

  @FXML
  private AnchorPane acpNewMeal;

  @FXML
  private AnchorPane acpMenu;

  @FXML
  private VBox vBoxMenu;

  public void initialize() {
    // Make sure to navigate through the pages
    List<Node> pages = Arrays.asList(acpNewMeal, vBoxMenu);
    acpMenu.getChildren().removeAll(pages);
    acpMenu.getChildren().add(vBoxMenu);
    btnNewMeal.setOnAction(event -> {
      acpMenu.getChildren().removeAll(pages);
      acpMenu.getChildren().add(acpNewMeal);
    });
  }
}
