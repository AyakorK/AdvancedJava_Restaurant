package com.coding.restaurant.restaurant.Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
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
    private Button btnWorker;

    @FXML
    private AnchorPane acpCentre;
    @FXML
    private AnchorPane acpHome;
    @FXML
    private AnchorPane acpWorker;

    @FXML
    private VBox vboxTest;

    @FXML
    private ImageView imgLogo;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        acpCentre.getChildren().removeAll(vboxTest, acpWorker);

        imgLogo.setOnMouseClicked(e -> {
            acpCentre.getChildren().removeAll(vboxTest, acpWorker);
            acpCentre.getChildren().add(acpHome);
        });

        btnMenu.setOnMouseClicked(e -> {
            acpCentre.getChildren().removeAll(acpHome, acpWorker);
            acpCentre.getChildren().add(vboxTest);
        });

        btnCommand.setOnMouseClicked(e -> acpCentre.getChildren().removeAll(vboxTest, acpHome, acpWorker));

        btnWorker.setOnMouseClicked(e -> {
            acpCentre.getChildren().removeAll(vboxTest, acpHome);
            acpCentre.getChildren().add(acpWorker);
        });
    }

}
