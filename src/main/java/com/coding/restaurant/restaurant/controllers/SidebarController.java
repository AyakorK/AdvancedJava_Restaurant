package com.coding.restaurant.restaurant.controllers;

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
  public VBox acpBills;
  @FXML
  private VBox vboxTest;

  @FXML
  private Button btnBills;
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
  private AnchorPane acpMenu;
  @FXML
  public AnchorPane acpCommand;

  @FXML
  private ImageView imgLogo;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {

        acpCentre.getChildren().removeAll(vboxTest, acpMenu, acpCommand, acpWorker, acpBills);

        imgLogo.setOnMouseClicked(e -> {
            acpCentre.getChildren().removeAll( acpMenu, acpCommand, acpWorker, acpBills);
            acpCentre.getChildren().add(acpHome);
        });

        btnMenu.setOnMouseClicked(e -> {
            acpCentre.getChildren().removeAll(acpHome, acpCommand, acpWorker, acpBills);
            acpCentre.getChildren().add(acpMenu);
        });

        btnCommand.setOnMouseClicked(e -> {
            acpCentre.getChildren().removeAll( acpHome, acpMenu, acpWorker, acpBills);
            acpCentre.getChildren().add(acpCommand);
        });

        btnWorker.setOnMouseClicked(e -> {
            acpCentre.getChildren().removeAll( acpHome, acpMenu, acpCommand, acpBills);
            acpCentre.getChildren().add(acpWorker);
        });

        btnBills.setOnMouseClicked(e -> {
          acpCentre.getChildren().removeAll(acpHome, acpMenu, acpWorker, acpCommand);
          acpCentre.getChildren().add(acpBills);
        });

    }


  }




