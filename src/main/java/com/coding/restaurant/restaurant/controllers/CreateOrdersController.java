package com.coding.restaurant.restaurant.controllers;

import com.coding.restaurant.restaurant.models.Meal;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class CreateOrdersController implements Initializable {

  @FXML
  private Button btnNewWorker;

  @FXML
  private AnchorPane acpInWorker;
  @FXML
  private AnchorPane acpNewWorker;

  @FXML
  private VBox vbxWorker;

  @FXML
  private TableView<Meal> tvbWorkers;

  @FXML
  private TableColumn<Meal, String> colName;
  @FXML
  private TableColumn<Meal, String> colPrice;
  @FXML
  private TableColumn<Meal, String> colActive;
  @FXML
  private TableColumn<Meal, String> colHours;
  @FXML
  private TableColumn<Meal, String> colRole;
  @FXML
  private TableColumn<Meal, String> colArrived;
  @FXML
  private TableColumn<Meal, String> colDeparture;
  @FXML
  private TableColumn<Meal, String> colActions;

  private ObservableList<Meal> observableMeal;

  public List<Meal> getMeals() throws SQLException {
    DatabaseManager db = new DatabaseManager();
    return db.getMeals();
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    acpInWorker.getChildren().remove(acpNewWorker);

    btnNewWorker.setOnMouseClicked(e -> {
      acpInWorker.getChildren().remove(vbxWorker);
      acpInWorker.getChildren().add(acpNewWorker);
    });

//    colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
    colName.setCellValueFactory(new PropertyValueFactory<>("name"));
    colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    colActive.setCellValueFactory(cellData -> {
      Meal meal = cellData.getValue();
      String activeStatus = meal.isActive() ? "actif" : "inactif";
      return new SimpleStringProperty(activeStatus);
    });
//    colHours.setCellValueFactory(new PropertyValueFactory<>("hoursWorked"));
//    colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
//    colArrived.setCellValueFactory(new PropertyValueFactory<>("arrivalDate"));
//    colDeparture.setCellValueFactory(cellData -> {
//      Meal meal = cellData.getValue();
//      String departureDate = meal.getDepartureDate().equals(worker.getArrivalDate()) ? "Non indiquÃ©" : String.valueOf(worker.getDepartureDate());
//      return new SimpleStringProperty(departureDate);
//    });
    // add 2 boutons in colActions : edit and delete
    colActions.setCellFactory(param -> new TableCell<Meal, String>() {
      private final Button btnEdit = new Button("Modifier");
      private final Button btnDelete = new Button("Supprimer");
      private final HBox pane = new HBox(btnEdit, btnDelete);

      {
        btnEdit.setOnAction(event -> {
          Meal meal = getTableView().getItems().get(getIndex());
          System.out.println("edit " + meal.getName());
        });
        btnDelete.setOnAction(event -> {
          Meal meal = getTableView().getItems().get(getIndex());
          System.out.println("delete " + meal.getName());
//                    try {
//                        DatabaseManager db = new DatabaseManager();
////                        db.deleteWorker(worker);
//                        observableWorkers.remove(worker);
//                    } catch (SQLException e) {
//                        throw new RuntimeException(e);
//                    }
        });
      }

      @Override
      protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        setGraphic(empty ? null : pane);
      }
    });


    try {
      List<Meal> meals = getMeals();
      observableMeal = FXCollections.observableArrayList(meals);
      tvbWorkers.setItems(observableMeal);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }


  }

}