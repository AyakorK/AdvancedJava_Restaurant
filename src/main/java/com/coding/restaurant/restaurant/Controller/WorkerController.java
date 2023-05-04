package com.coding.restaurant.restaurant.Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.net.URL;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        acpInWorker.getChildren().remove(acpNewWorker);

        btnNewWorker.setOnMouseClicked(e -> {
            acpInWorker.getChildren().remove(vbxWorker);
            acpInWorker.getChildren().add(acpNewWorker);
        });
    }

}
