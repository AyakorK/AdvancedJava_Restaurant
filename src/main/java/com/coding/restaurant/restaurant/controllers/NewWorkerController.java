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
import java.util.Date;
import java.util.ResourceBundle;

public class NewWorkerController implements Initializable, NewFormInterface {

  @FXML
  private TextField txfName;
  @FXML
  private TextField txfFirstName;
  @FXML
  private TextField txfRole;
  @FXML
  private TextField txfAge;

  @FXML
  private CheckBox cbxActive;

  @FXML
  private Button btnSave;
  @FXML
  private Button btnBack;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {

    // Set the text field to only accept numbers
    txfAge.textProperty().addListener((observable, oldValue, newValue) -> {
      if (!newValue.matches("\\d*")) {
        txfAge.setText(newValue.replaceAll("[^\\d]", ""));
      }
    });

    // When the button is clicked, go back to the worker view
    btnBack.setOnAction(event -> goBack());

    // When the button is clicked, add a new worker to the database
    btnSave.setOnAction(event -> {
      String name = txfName.getText();
      String firstName = txfFirstName.getText();
      Boolean isActive = cbxActive.isSelected();
      Double hoursWorked = Double.parseDouble(String.valueOf(0));
      String role = txfRole.getText();
      Date arrivalDate = new Date();
      Date departureDate = new Date();
      Integer age = Integer.parseInt(txfAge.getText());

      addWorker(name, firstName, isActive, hoursWorked, role, arrivalDate, departureDate , age);

      goBack();

    });
  }

  // Call the method to add a worker
  public void addWorker(String name, String firstName, Boolean isActive, Double hoursWorked, String role, Date arrivalDate, Date departureDate, Integer age) {
    DatabaseManager.addWorker(name, firstName, isActive, hoursWorked, role, arrivalDate, departureDate, age);
  }

  // Go back to the worker view
  public void goBack() {
    try {
      // Load the worker view & set it as the current view
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/coding/restaurant/restaurant/worker-view.fxml"));
      Node workerView = loader.load();

      AnchorPane acpInWorker = (AnchorPane) btnSave.getParent().getParent().getParent();

      acpInWorker.getChildren().setAll(workerView);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
