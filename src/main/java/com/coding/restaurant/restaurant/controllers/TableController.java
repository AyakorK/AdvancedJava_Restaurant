package com.coding.restaurant.restaurant.controllers;

import com.coding.restaurant.restaurant.models.Table;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;


import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Controller of the Table
 */
public class TableController {

  @FXML
  private ListView<Table> listView;

  @FXML
  private ToggleButton toggleFreeTables;

  @FXML
  private ToggleButton toggleIndoorTables;

  @FXML
  private ToggleButton toggleTerraceTables;

  @FXML
  private Button createTable;

  @FXML
  private VBox vbxTable;

  // List all tables
  public ObservableList<Table> showTables() throws SQLException {
    DatabaseManager db = new DatabaseManager();
    List<Table> tables = db.getTables();
    ObservableList<Table> showTables = FXCollections.observableArrayList();
    showTables.addAll(tables);
    return showTables;
  }

  // List all free tables
  public void showFreeTables() {
    if (toggleFreeTables.isSelected()) {
      try {
        listView.setItems(showTables().filtered(table -> !table.isFull()));
      } catch (SQLException e) {
        e.printStackTrace();
      }
    } else {
      try {
        listView.setItems(showTables());
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  // List all tables located inside
  public void showIndoorTables() {
    if (toggleIndoorTables.isSelected()) {
      try {
        listView.setItems(showTables().filtered(table -> table.getLocation().equalsIgnoreCase("Intérieur")));
      } catch (SQLException e) {
        e.printStackTrace();
      }
    } else {
      try {
        listView.setItems(showTables());
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  // List all tables located outside
  public void showOutdoorTables() {
    if (toggleTerraceTables.isSelected()) {
      try {
        listView.setItems(showTables().filtered(table -> table.getLocation().equals("Terrasse")));
      } catch (SQLException e) {
        e.printStackTrace();
      }
    } else {
      try {
        listView.setItems(showTables());
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  // Create a new table in the database
  public void createTable() throws IOException {
    // Load the view newTable.fxml
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/coding/restaurant/restaurant/newTable-view.fxml"));
    Parent root = loader.load();

    // Create a new stage
    Stage stage = new Stage();
    stage.setScene(new Scene(root));

    // Show the stage
    stage.show();
  }


  // Initialize everything
  public void initialize() {

    try {
      listView.setItems(showTables());
    } catch (SQLException e) {
      e.printStackTrace();
    }

    // Personalise the listView's appearance
    listView.setCellFactory(param -> new ListCell<>() {
      @Override
      protected void updateItem(Table item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
          setText(null);
          setGraphic(null);
        } else {
          // Create a HBox that will contain the table's information
          HBox hBox = new HBox();
          hBox.setAlignment(Pos.CENTER_LEFT);
          hBox.setSpacing(10);
          String arialText = "Arial";

          // Put the table's number in a Text
          Text tableNumber = new Text("Table " + item.getNumber());
          tableNumber.setFont(Font.font(arialText, FontWeight.BOLD, 20));

          // Put the table's size in a Text
          Text tableSeats = new Text(item.getSize() + " places");
          tableSeats.setFont(Font.font(arialText, FontWeight.BOLD, 15));

          // Put the table's location in a Text
          Text tableLocation = new Text(item.getLocation());
          tableLocation.setFont(Font.font(arialText, FontWeight.BOLD, 15));

          // Put the table's status in a Text
          Text tableStatus = new Text(item.isFull() ? "Occupée" : "Libre");
          tableStatus.setFont(Font.font(arialText, FontWeight.BOLD, 15));
          tableStatus.setFill(item.isFull() ? Color.RED : Color.GREEN);

          // Add the Texts to the HBox
          hBox.getChildren().addAll(tableNumber, tableSeats, tableLocation, tableStatus);

          // Create the Buttons to delete and add a command
          Button deleteButton = new Button("Supprimer");
          deleteButton.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
          deleteButton.getStyleClass().add("btn-danger");
          Button addButton = new Button("Ajouter une commande");

          // Manage the events
          deleteButton.setOnAction(event -> {
            // Ask for confirmation
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppression");
            alert.setHeaderText("Supprimer la table " + item.getNumber() + " ?");
            alert.setContentText("Êtes-vous sûr de vouloir supprimer la table " + item.getNumber() + " ?");
            // With a button confirm and delete
            ButtonType deleteButtonType = new ButtonType("Supprimer", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButtonType = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(deleteButtonType, cancelButtonType);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == deleteButtonType) {
              // delete table from TableRestaurant and update the ListView
              try {
                DatabaseManager db = new DatabaseManager();
                // delete the chosen table from the database
                db.deleteTable(item.getNumber());
                // update the ListView
                listView.setItems(showTables());
              } catch (SQLException e) {
                e.printStackTrace();
              }
            }
          });

          // Create a Hbox for the buttons
          HBox buttonBox = new HBox();
          buttonBox.setAlignment(Pos.CENTER);
          buttonBox.setSpacing(10);
          buttonBox.getChildren().addAll(deleteButton, addButton);

          // Add the Hbox and the Vbox to a BorderPane
          BorderPane borderPane = new BorderPane();
          borderPane.setLeft(hBox);
          borderPane.setRight(buttonBox);
          BorderPane.setAlignment(buttonBox, Pos.CENTER_RIGHT);
          BorderPane.setMargin(hBox, new Insets(5, 0, 5, 5));

          // Print the BorderPane
          setGraphic(borderPane);
        }
      }
    });
  }
}

