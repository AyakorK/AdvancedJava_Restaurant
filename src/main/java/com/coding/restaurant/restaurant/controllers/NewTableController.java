package com.coding.restaurant.restaurant.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class NewTableController implements Initializable {

  @FXML
  private TextField txfTableNumber;
  @FXML
  private TextField txfTableCapacity;

  @FXML
  private ChoiceBox cbxLocation;

  @FXML
  private Button btnSave;
  @FXML
  private Button btnBack;


  // Call the method to add a table
  public void addTable(int number, String location, int size, boolean isFull) {
    DatabaseManager.addTable(number, location, size, isFull);
  }


  // Go back to the table view
  public void goBack() {
    try {
      // Load the worker view & set it as the current view
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/coding/restaurant/restaurant/table-view.fxml"));
      Node workerView = loader.load();

      AnchorPane acpTable = (AnchorPane) btnSave.getParent().getParent();

      acpTable.getChildren().setAll(workerView);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {

    // Set the text field to only accept numbers
    txfTableNumber.textProperty().addListener((observable, oldValue, newValue) -> {
      if (!newValue.matches("\\d*")) {
        txfTableNumber.setText(newValue.replaceAll("[^\\d]", ""));
      }
    });

    txfTableCapacity.textProperty().addListener((observable, oldValue, newValue) -> {
      if (!newValue.matches("\\d*")) {
        txfTableCapacity.setText(newValue.replaceAll("[^\\d]", ""));
      }
    });

    // When the button is clicked, go back to the table view
    btnBack.setOnAction(event -> goBack());

    // When the button is clicked, add a new table to the database
    btnSave.setOnAction(event -> {
      // Get the values from the text fields
      int number = Integer.parseInt(txfTableNumber.getText());
      String location = (String) cbxLocation.getValue();
      int capacity = Integer.parseInt(txfTableCapacity.getText());
      boolean isFull = false;

      addTable(number, location, capacity, isFull);

      goBack();
    });

  }
}
