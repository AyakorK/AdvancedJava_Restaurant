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
  public AnchorPane acpCommand;

  @FXML
  public VBox acpBills;

  @FXML
  private Button btnBills;

  @FXML
  private Button btnMenu;

  @FXML
  private Button btnCommand;

  @FXML
  private AnchorPane acpCentre;

  @FXML
  private VBox vboxTest;

  @FXML
  private AnchorPane acpHome;

  @FXML
  private ImageView imgLogo;

  @FXML
  private AnchorPane acpMenu;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {

    acpCentre.getChildren().removeAll(vboxTest, acpMenu, acpCommand, acpBills);

    imgLogo.setOnMouseClicked(e -> {
      acpCentre.getChildren().removeAll(vboxTest, acpMenu, acpCommand, acpBills);
      acpCentre.getChildren().add(acpHome);
    });

    btnMenu.setOnMouseClicked(e -> {
      acpCentre.getChildren().removeAll(acpHome, vboxTest, acpCommand, acpBills);
      acpCentre.getChildren().add(acpMenu);
    });

    btnCommand.setOnMouseClicked(e -> {
      acpCentre.getChildren().removeAll(acpHome, acpMenu, acpCommand, acpBills);
      acpCentre.getChildren().add(acpCommand);
    });

    btnBills.setOnMouseClicked(e -> {
      acpCentre.getChildren().removeAll(acpHome, acpMenu, acpCommand, acpBills);
      acpCentre.getChildren().add(acpBills);
    });

  }



}
