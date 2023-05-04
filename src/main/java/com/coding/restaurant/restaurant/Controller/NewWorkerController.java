package com.coding.restaurant.restaurant.Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class NewWorkerController implements Initializable {

    @FXML
    private TextField txfPrice;

    @FXML
    private Button btnSave;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Set the text field to only accept numbers
        txfPrice.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                txfPrice.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

    }
}
