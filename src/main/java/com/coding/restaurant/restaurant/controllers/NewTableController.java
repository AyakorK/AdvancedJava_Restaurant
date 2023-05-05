package com.coding.restaurant.restaurant.controllers;

import com.coding.restaurant.restaurant.models.Table;
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
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class NewTableController implements Initializable, NewFormInterface {

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

  /**
   *
   * @param url
   * The location used to resolve relative paths for the root object, or
   * {@code null} if the location is not known.
   *
   * @param resourceBundle
   * The resources used to localize the root object, or {@code null} if
   * the root object was not localized.
   */
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

      try {
        addTable(number, location, capacity, isFull);
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }


      goBack();
    });
  }

  // Call the method to add a table

  /**
   * 
   * @param number
   * @param location
   * @param size
   * @param isFull
   * @return
   * @throws SQLException
   */
  public List<Table> addTable(int number, String location, int size, boolean isFull) throws SQLException {
    DatabaseManager.addTable(number, location, size, isFull);
    DatabaseManager db = new DatabaseManager();
    return db.getTables();
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
}
