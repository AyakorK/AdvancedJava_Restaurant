package com.coding.restaurant.restaurant.controllers;

import com.coding.restaurant.restaurant.models.Meal;
import com.coding.restaurant.restaurant.models.Table;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;


import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

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

  @FXML
  private AnchorPane acpInWorker;
  @FXML
  private AnchorPane acpTable;

  public ObservableList<Table> showTables() throws SQLException {
    DatabaseManager db = new DatabaseManager();
    List<Table> tables = db.getTables();
    ObservableList<Table> showTables = FXCollections.observableArrayList();
    showTables.addAll(tables);
    return showTables;
  }

  public void showFreeTables() {
    if (toggleFreeTables.isSelected()) {
      try {
        listView.setItems(showTables().filtered(Table -> !Table.isFull()));
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

  public void showOutdoorTables(ActionEvent actionEvent) {
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

  public void createTable(ActionEvent actionEvent) throws IOException {
    // Charge la vue newTable.fxml
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/coding/restaurant/restaurant/newTable-view.fxml"));
    Parent root = loader.load();

    // Crée une nouvelle fenêtre pour la vue newTable.fxml
    Stage stage = new Stage();
    stage.setScene(new Scene(root));

    // Affiche la nouvelle fenêtre
    stage.show();
  }


  public void initialize() throws IOException {

    try {
      listView.setItems(showTables());
    } catch (SQLException e) {
      e.printStackTrace();
    }

    // Personnaliser l'apparence de chaque cellule de la ListView
    listView.setCellFactory(param -> new ListCell<>() {
      @Override
      protected void updateItem(Table item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
          setText(null);
          setGraphic(null);
        } else {
          // Créer un conteneur HBox pour contenir les informations de la table et les boutons
          HBox hBox = new HBox();
          hBox.setAlignment(Pos.CENTER_LEFT);
          hBox.setSpacing(10);

          // Mettre le numéro de la table dans un Text
          Text tableNumber = new Text("Table " + item.getNumber());
          tableNumber.setFont(Font.font("Arial", FontWeight.BOLD, 20));

          // Mettre le nombre de places dans un Text
          Text tableSeats = new Text(item.getSize() + " places");
          tableSeats.setFont(Font.font("Arial", FontWeight.BOLD, 15));

          // Mettre l'emplacement de la table dans un Text
          Text tableLocation = new Text(item.getLocation());
          tableLocation.setFont(Font.font("Arial", FontWeight.BOLD, 15));

          // Mettre le statut de la table dans un Text
          Text tableStatus = new Text(item.isFull() ? "Occupée" : "Libre");
          tableStatus.setFont(Font.font("Arial", FontWeight.BOLD, 15));
          tableStatus.setFill(item.isFull() ? Color.RED : Color.GREEN);

          // Ajouter les Texts à la HBox
          hBox.getChildren().addAll(tableNumber, tableSeats, tableLocation, tableStatus);

          // Créer les boutons de suppression et d'ajout de commande
          Button deleteButton = new Button("Supprimer");
          deleteButton.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
          deleteButton.getStyleClass().add("btn-danger");
          Button addButton = new Button("Ajouter une commande");

          // Ajouter les gestionnaires d'événements pour les boutons
          deleteButton.setOnAction(event -> {
            // Demander à l'utilisateur de confirmer la suppression
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppression");
            alert.setHeaderText("Supprimer la table " + item.getNumber() + " ?");
            alert.setContentText("Êtes-vous sûr de vouloir supprimer la table " + item.getNumber() + " ?");
            // avec un bouton SUPPRIMER en btn-danger et un bouton ANNULER
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

          // Créer un conteneur VBox pour contenir les boutons
          HBox buttonBox = new HBox();
          buttonBox.setAlignment(Pos.CENTER);
          buttonBox.setSpacing(10);
          buttonBox.getChildren().addAll(deleteButton, addButton);

          // Ajouter la HBox et la VBox dans un conteneur BorderPane pour les positionner correctement
          BorderPane borderPane = new BorderPane();
          borderPane.setLeft(hBox);
          borderPane.setRight(buttonBox);
          BorderPane.setAlignment(buttonBox, Pos.CENTER_RIGHT);
          BorderPane.setMargin(hBox, new Insets(5, 0, 5, 5));

          // Afficher le conteneur BorderPane dans la cellule de la ListView
          setGraphic(borderPane);


          addButton.setOnAction(event -> {
            acpTable.getChildren().remove(vbxTable);
            acpTable.getChildren().add(acpInWorker);
            Table itemNumber = item; // 1ère étape
            CreateOrdersController createOrderController = new CreateOrdersController(); // 2ème étape
            createOrderController.setItemNumber(itemNumber); // 3ème étape
//            System.out.println(item.getTableUUID());
          });
        }
      }
    });

    acpTable.getChildren().remove(acpInWorker);

  }
}

