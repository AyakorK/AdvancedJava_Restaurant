package com.coding.restaurant.restaurant.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;

public class NewBillController implements Initializable {

  private ToggleGroup toggleGroup;

  @FXML
  private TextField txfAmount;

  @FXML
  private RadioButton rdbBenef;

  @FXML
  private RadioButton rdbDepense;

  @FXML
  private Button btnSave;

  @FXML
  private Button btnBack;

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    // Set the text field to only accept numbers
    txfAmount.textProperty().addListener((observable, oldValue, newValue) -> {
      if (!newValue.matches("\\d*")) {
        txfAmount.setText(newValue.replaceAll("[^\\d]", ""));
      }
    });

    btnBack.setOnAction(event -> {
      goBack();
    });

    // Initialisation du ToggleGroup et des autres éléments
    toggleGroup = new ToggleGroup();

    // Associer les RadioButtons au ToggleGroup
    rdbBenef.setToggleGroup(toggleGroup);
    rdbDepense.setToggleGroup(toggleGroup);

    // Mettre les valeurs retournées par les RadioButtons par 0 et 1
    rdbBenef.setUserData(1);
    rdbDepense.setUserData(0);

    // Sélectionner un RadioButton par défaut
    rdbBenef.setSelected(true);

    btnSave.setOnAction(event -> {
      Double price = Double.parseDouble(txfAmount.getText());
      Integer typeValue = (Integer) toggleGroup.getSelectedToggle().getUserData();
      Boolean type = typeValue == 1;

      try {
        createBill(price, type);
      } catch (SQLException e) {
        e.printStackTrace();
      }

      goBack();

    });

  }

  public void createBill(Double price, Boolean type) throws SQLException {
    DatabaseManager db = new DatabaseManager();
    db.createBill(price, type);
  }

  public void goBack() {
    try {
      // Load the worker view & set it as the current view
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/coding/restaurant/restaurant/bills-view.fxml"));
      Node workerView = loader.load();

      AnchorPane acpInWorker = (AnchorPane) btnSave.getParent().getParent().getParent();

      acpInWorker.getChildren().setAll(workerView);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}