package com.coding.restaurant.restaurant.Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class SidebarController implements Initializable {

    @FXML
    private Button btnMenu;

    @FXML
    private Button btnCommand;

    @FXML
    private AnchorPane acpCentre;

    @FXML
    private VBox vboxTest;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnMenu.setOnMouseClicked(e -> {
            acpCentre.getChildren().addAll(vboxTest);
        });

        btnCommand.setOnMouseClicked(e -> {
            acpCentre.getChildren().remove(vboxTest);
        });

    }

}
