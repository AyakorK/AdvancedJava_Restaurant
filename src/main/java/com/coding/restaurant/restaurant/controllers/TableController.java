package com.coding.restaurant.restaurant.controllers;

import com.coding.restaurant.restaurant.models.Table;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.Optional;

public class TableController {

  @FXML
  private ListView<Table> listView;

  @FXML
  private ToggleButton toggleButton;


  public void initialize() {
    // Remplir la ListView avec des données de test
    Table table1 = new Table(1, "Terrasse", 4, false);
    Table table2 = new Table(2, "Intérieur", 6, true);
    Table table3 = new Table(3, "Terrasse", 2, true);
    Table table4 = new Table(4, "Intérieur", 8, false);
    listView.getItems().addAll(table1, table2, table3, table4);

    // Personnaliser l'apparence de chaque cellule de la ListView
    listView.setCellFactory(param -> new ListCell<>() {
      @Override
      protected void updateItem(Table item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
          setText(null);
          setGraphic(null);
        } else {
          // Créer un rectangle pour représenter la table
          Rectangle tableShape = new Rectangle(200, 100);
          tableShape.setFill(item.isFull() ? Color.RED : Color.GREEN);

          // Créer un texte pour représenter le numéro de la table et mettre en dessous le nombre de places
          Text tableText = new Text("Table " + item.getNumber() + "\n" + item.getSize() + " places");
          tableText.setFont(Font.font("Arial", FontWeight.BOLD, 12));

          // Créer un texte pour représenter l'emplacement de la table
          Text locationText = new Text(item.getLocation());
          locationText.setFont(Font.font("Arial", FontWeight.NORMAL, 10));

          // Créer une pile pour superposer le rectangle et les textes
          StackPane tablePane = new StackPane();
          tablePane.getChildren().addAll(tableShape, tableText, locationText);

          // Aligner le texte de l'emplacement de la table en bas à gauche du rectangle
          StackPane.setAlignment(locationText, Pos.BOTTOM_CENTER);

          // Afficher la pile dans la cellule
          setGraphic(tablePane);

          setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
              // Créer un nouveau popup pour ajouter une commande ou supprimer la table
              Alert popup = new Alert(Alert.AlertType.CONFIRMATION);
              popup.setTitle("Options de la table");
              popup.setHeaderText("Table " + item.getNumber());
              popup.setContentText("Que voulez-vous faire avec cette table ?");

              // Add buttons to the popup
              ButtonType ajouterCommandeButton = new ButtonType("Ajouter une commande");
              ButtonType supprimerTableButton = new ButtonType("Supprimer la table");
              ButtonType cancelButton = new ButtonType("Annuler");
              popup.getButtonTypes().setAll(ajouterCommandeButton, supprimerTableButton, cancelButton);

              // Add a listener to the popup to handle the result of the user's choice
              popup.showAndWait().ifPresent(result -> {
                if (result == ajouterCommandeButton) {
                  System.out.println("Ajouter une commande");
                } else if (result == supprimerTableButton) {
                  // Créer un nouveau popup pour confirmer la suppression de la table
                  Alert confirmationPopup = new Alert(Alert.AlertType.CONFIRMATION);
                  confirmationPopup.setTitle("Supprimer la table");
                  confirmationPopup.setHeaderText("Table " + item.getNumber());
                  confirmationPopup.setContentText("Êtes-vous sûr de vouloir supprimer cette table ?");
                  confirmationPopup.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);

                  // Add a listener to the confirmation popup to handle the result of the user's choice
                  confirmationPopup.showAndWait().ifPresent(confirmationResult -> {
                    if (confirmationResult == ButtonType.YES) {
                      listView.getItems().remove(item);
                    }
                  });
                } else {
                  popup.showAndWait();
                }
              });
            }
          });
        }
      }
    });
  }

  public void showFreeTables(ActionEvent actionEvent) {
  }

  public void showIndoorTables(ActionEvent actionEvent) {
  }

  public void showOutdoorTables(ActionEvent actionEvent) {
  }
}

