package com.coding.restaurant.restaurant.controllers;

import com.coding.restaurant.restaurant.models.Worker;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class WorkerController implements Initializable {

  @FXML
  private Button btnNewWorker;

  @FXML
  private AnchorPane acpInWorker;
  @FXML
  private AnchorPane acpNewWorker;

  @FXML
  private VBox vbxWorker;

  @FXML
  private TableView<Worker> tvbWorkers;

  @FXML
  private TableColumn<Worker, String> colName;
  @FXML
  private TableColumn<Worker, String> colFirstName;
  @FXML
  private TableColumn<Worker, String> colActive;
  @FXML
  private TableColumn<Worker, String> colHours;
  @FXML
  private TableColumn<Worker, String> colRole;
  @FXML
  private TableColumn<Worker, String> colArrived;
  @FXML
  private TableColumn<Worker, String> colDeparture;
  @FXML
  private TableColumn<Worker, String> colActions;

  @FXML
  private TableColumn<Worker, String> colAge;

  private ObservableList<Worker> observableWorkers;

  // Call the method to get all the workers

  /**
   *
   * @return
   * @throws SQLException
   */
  public List<Worker> getWorkers() throws SQLException {
    DatabaseManager db = new DatabaseManager();
    return db.getWorkers();
  }

  // Call the method to delete a worker

  /**
   *
   * @param id
   * @throws SQLException
   */
  public void deleteWorker(String id) throws SQLException {
    DatabaseManager db = new DatabaseManager();
    db.deleteWorker(id);
  }

  // Call the method to activate a worker

  /**
   *
   * @param id
   * @throws SQLException
   */
  public void activeWorker(String id) throws SQLException {
    DatabaseManager db = new DatabaseManager();
    db.activeWorker(id);
  }

  // Call the method to add workers to the list
  public void addWorkerToList() {
    try {
      List<Worker> workers = getWorkers().stream()
              .sorted((w1, w2) -> w1.getName().compareToIgnoreCase(w2.getName()))
              .toList();
      observableWorkers = FXCollections.observableArrayList(workers);
      tvbWorkers.setItems(observableWorkers);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

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
    acpInWorker.getChildren().remove(acpNewWorker);

    // When the button is clicked, the new worker form is displayed
    btnNewWorker.setOnMouseClicked(e -> {
      acpInWorker.getChildren().remove(vbxWorker);
      acpInWorker.getChildren().add(acpNewWorker);
    });

    // Set the value of the column
    colName.setCellValueFactory(new PropertyValueFactory<>("name"));
    colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
    colActive.setCellValueFactory(cellData -> {
      Worker worker = cellData.getValue();
      String activeStatus = worker.isActive() ? "actif" : "inactif";
      return new SimpleStringProperty(activeStatus);
    });
    colActive.setCellFactory(column -> new TableCell<Worker, String>() {
      @Override
      protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        // If the cell is not empty, we change the text and the color depending on the value of the cell
        if (!empty) {
          setText(item);

          if (item.equals("actif")) {
            setTextFill(Color.GREEN);
          } else if (item.equals("inactif")) {
            setTextFill(Color.RED);
          }
        } else {
          setText(null);
        }
      }
    });
    // Set the value of each column using the infos we have in the worker object
    colHours.setCellValueFactory(new PropertyValueFactory<>("hoursWorked"));
    colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
    colArrived.setCellValueFactory(new PropertyValueFactory<>("arrivalDate"));
    colDeparture.setCellValueFactory(cellData -> {
      Worker worker = cellData.getValue();
      String departureDate = worker.getDepartureDate().equals(worker.getArrivalDate()) ? "..." : String.valueOf(worker.getDepartureDate());
      return new SimpleStringProperty(departureDate);
    });
    colActions.setCellFactory(param -> new TableCell<Worker, String>() {
      private final Label lblDelete = new Label("Not in society");
      private final Button btnDelete = new Button("Départ");
      private final Button btnActive = new Button("Activer");
      private final HBox pane = new HBox(7, lblDelete, btnDelete, btnActive);

      {
        // Set the alignment of the buttons
        pane.setAlignment(Pos.CENTER);
        setAlignment(Pos.CENTER);
        setGraphic(pane);

        // When btnDelete is clicked, the worker is deleted
        btnDelete.setOnAction(event -> {
          Worker worker = getTableView().getItems().get(getIndex());
          try {
            deleteWorker(worker.getWorkerUUID());
            observableWorkers.clear();
            addWorkerToList();

          } catch (SQLException e) {
            e.printStackTrace();
          }
        });

        // When btnActive is clicked, the worker is activated
        btnActive.setOnAction(event -> {
          Worker worker = getTableView().getItems().get(getIndex());
          try {
            activeWorker(worker.getWorkerUUID());
            observableWorkers.clear();
            addWorkerToList();

          } catch (SQLException e) {
            e.printStackTrace();
          }
        });
      }

      // If the worker is not active, the btnActive button is removed

      /**
       * 
       * @param item The new item for the cell.
       * @param empty whether or not this cell represents data from the list. If it
       *        is empty, then it does not represent any domain data, but is a cell
       *        being used to render an "empty" row.
       */
      @Override
      protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        setGraphic(empty ? null : pane);
        if (!empty) {
          Worker worker = getTableView().getItems().get(getIndex());

          // If the worker DepartureDate is different from the ArrivalDate, the btnDelete and btnActive buttons are removed
          if (!worker.getDepartureDate().equals(worker.getArrivalDate())) {
            pane.getChildren().removeAll(btnDelete, btnActive);
          } else if (worker.isActive()) {
            pane.getChildren().removeAll(btnActive, lblDelete);
          } else {
            pane.getChildren().removeAll(btnDelete, lblDelete);
          }

        }

      }
    });
    colAge.setCellValueFactory(new PropertyValueFactory<>("age"));
    addWorkerToList();

  }
}
