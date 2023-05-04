package com.coding.restaurant.restaurant.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.*;

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
  private Button btnWorker;

  @FXML
  private Button btnTable;

  @FXML
  private AnchorPane acpCentre;

  @FXML
  private VBox vboxTest;

  @FXML
  private AnchorPane acpHome;
  @FXML
  private AnchorPane acpWorker;

  @FXML
  private VBox vbxTable;

  @FXML
  private ImageView imgLogo;

  @FXML
  private AnchorPane acpMenu;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {


    List<Node> pages = Arrays.asList(acpHome, acpMenu, acpCommand, acpWorker, acpBills, vbxTable);
    Map<Node, Node> pageButtonMap = new HashMap<>();
    pageButtonMap.put(imgLogo, acpHome);
    pageButtonMap.put(btnMenu, acpMenu);
    pageButtonMap.put(btnCommand, acpCommand);
    pageButtonMap.put(btnWorker, acpWorker);
    pageButtonMap.put(btnBills, acpBills);
    pageButtonMap.put(btnTable, vbxTable);

    acpCentre.getChildren().removeAll(pages);
    acpCentre.getChildren().add(acpHome);

    pageButtonMap.keySet().stream().forEach(button ->
            button.setOnMouseClicked(event -> {
              acpCentre.getChildren().removeAll(pages);
              acpCentre.getChildren().add(pageButtonMap.get(button));
            })
    );
  }
}
