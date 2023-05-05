package com.coding.restaurant.restaurant.controllers;

import com.coding.restaurant.restaurant.models.Service;
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
import java.util.stream.Collectors;

/**
 * Controller of the Table
 */
public class TableController {
  @FXML
  private AnchorPane acpTable;
  @FXML
  private Button btnAddTable;
  @FXML
  private ListView<Table> listView;

  @FXML
  private ToggleButton toggleFreeTables;

  @FXML
  private ToggleButton toggleIndoorTables;

  @FXML
  private ToggleButton toggleTerraceTables;

  @FXML
  private VBox vbxTable;

  @FXML
  private AnchorPane acpInWorker;
  @FXML
  private AnchorPane acpTable;

  @FXML
  private Button btnCreateOrder;

  // List all tables
  public ObservableList<Table> filterTables(boolean free, String location) throws SQLException {
    DatabaseManager db = new DatabaseManager();
    List<Table> tables = db.getTables();

    // Filter by free tables if the corresponding toggle is selected
    if (free) {
      tables = tables.stream().filter(table -> !table.isFull()).collect(Collectors.toList());
    }

    // Filter by location if a location is specified
    if (!location.isEmpty()) {
      tables = tables.stream().filter(table -> table.getLocation().equalsIgnoreCase(location)).collect(Collectors.toList());
    }

    return FXCollections.observableArrayList(tables);
  }

  public ObservableList<Table> showTables() throws SQLException {
    DatabaseManager db = new DatabaseManager();
    List<Table> tables = db.getTables();
    return FXCollections.observableArrayList(tables);
  }



  // Update the list view with the current filters
  public void updateTableList() {
    try {
      listView.setItems(filterTables(toggleFreeTables.isSelected(), toggleIndoorTables.isSelected() ? "Intérieur" : (toggleTerraceTables.isSelected() ? "Terrasse" : "")));
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void toggleIndoorTables() {
    if (toggleIndoorTables.isSelected()) {
      toggleTerraceTables.setDisable(true);
    } else {
      toggleTerraceTables.setDisable(false);
    }
    updateTableList();
  }


  public void toggleTerraceTables() {
      if (toggleTerraceTables.isSelected()) {
        toggleIndoorTables.setDisable(true);
      } else {
        toggleIndoorTables.setDisable(false);
      }
      updateTableList();
  }


  // Initialize everything
  public void initialize() throws SQLException {

    // Update the list view when the filters are changed
    toggleFreeTables.setOnAction(event -> updateTableList());
    toggleIndoorTables.setOnAction(event -> {
      updateTableList();
      toggleIndoorTables();
    });
    toggleTerraceTables.setOnAction(event -> {
      updateTableList();
      toggleTerraceTables();
    });

    listView.setItems(showTables());

    vbxTable.getChildren().remove(acpTable);

    // When the button is clicked, the new table form is displayed
    btnAddTable.setOnMouseClicked(e -> {
      vbxTable.getChildren().clear();
      vbxTable.getChildren().add(acpTable);
    });

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
          buttonBox.getChildren().addAll(deleteButton);

          // Add the Hbox and the Vbox to a BorderPane
          BorderPane borderPane = new BorderPane();
          borderPane.setLeft(hBox);
          borderPane.setRight(buttonBox);
          BorderPane.setAlignment(buttonBox, Pos.CENTER_RIGHT);
          BorderPane.setMargin(hBox, new Insets(5, 0, 5, 5));

          // Print the BorderPane
          setGraphic(borderPane);

          btnCreateOrder.setOnAction(event -> {
            acpTable.getChildren().remove(vbxTable);
            acpTable.getChildren().add(acpInWorker);
          });
        }
      }
    });

    acpTable.getChildren().remove(acpInWorker);

  }

  // Display the button to create an order
  public boolean displayButton() throws SQLException {
    HomeController homeController = new HomeController();
    Service service = homeController.startService();
    return isCurrent(service);
  }

  public boolean isCurrent(Service service) throws SQLException {
    DatabaseManager db = new DatabaseManager();
    return db.isCurrent(service);
  }
}

