package com.coding.restaurant.restaurant.controllers;

import com.coding.restaurant.restaurant.models.Worker;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

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

    private ObservableList<Worker> observableWorkers;

    public List<Worker> getWorkers() throws SQLException {
            DatabaseManager db = new DatabaseManager();
            return db.getWorkers();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        acpInWorker.getChildren().remove(acpNewWorker);

        btnNewWorker.setOnMouseClicked(e -> {
            acpInWorker.getChildren().remove(vbxWorker);
            acpInWorker.getChildren().add(acpNewWorker);
        });

        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colActive.setCellValueFactory(cellData -> {
            Worker worker = cellData.getValue();
            String activeStatus = worker.isActive() ? "actif" : "inactif";
            return new SimpleStringProperty(activeStatus);
        });
        colHours.setCellValueFactory(new PropertyValueFactory<>("hoursWorked"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colArrived.setCellValueFactory(new PropertyValueFactory<>("arrivalDate"));
        colDeparture.setCellValueFactory(cellData -> {
            Worker worker = cellData.getValue();
            String departureDate = worker.getDepartureDate().equals(worker.getArrivalDate()) ? "Non indiqué" : String.valueOf(worker.getDepartureDate());
            return new SimpleStringProperty(departureDate);
        });


        try {
            List<Worker> workers = getWorkers();
            observableWorkers = FXCollections.observableArrayList(workers);
            tvbWorkers.setItems(observableWorkers);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

}
