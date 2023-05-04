package com.coding.restaurant.restaurant.controllers;

import com.coding.restaurant.restaurant.models.Order;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;

import static java.lang.Integer.parseInt;

public class OrdersDashboardController {
  @FXML
  public Button allOrdersButton;
  @FXML
  private ListView<Order> ordersListView;

  public void initialize() {
    try {
      ordersListView.setItems(filteredOrders());
    } catch (SQLException e) {
      e.printStackTrace();
    }

    displayCells();
  }

  public ObservableList<Order> filteredOrders() throws SQLException {
    DatabaseManager db = new DatabaseManager();
    List<Order> orders = db.getOrders().stream().filter(Order::isWaiting).sorted(Comparator.comparing(Order::getTimer)).toList();
    ObservableList<Order> filteredOrder = FXCollections.observableArrayList();
    filteredOrder.addAll(orders);

    ordersListView.setItems(filteredOrder);
    return filteredOrder;
  }

  public ObservableList<Order> displayPassedOrders() throws SQLException {
    DatabaseManager db = new DatabaseManager();
    List<Order> orders = db.getOrders().stream().filter(order -> !order.isWaiting()).sorted(Comparator.comparing(Order::getTimer)).toList();
    ObservableList<Order> filteredOrder = FXCollections.observableArrayList();
    filteredOrder.addAll(orders);

    ordersListView.setItems(filteredOrder);
    return filteredOrder;
  }

  private void showError(String errorLoadingOrders, SQLException e) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Error");
    alert.setHeaderText(errorLoadingOrders);
    alert.setContentText(e.getMessage());
    alert.showAndWait();
  }

  public void displayCells() {
    ordersListView.setCellFactory(new Callback<>() {
      @Override
      public ListCell<Order> call(ListView<Order> listView) {
        GridPane totalGridPane = new GridPane();
        return new ListCell<>() {
          @Override
          protected void updateItem(Order order, boolean empty) {
            super.updateItem(order, empty);
            if (empty || order == null) {
              setText(null);
              setGraphic(null);
            } else {

              setText(String.valueOf(order.getOrderDate()) + " " + String.valueOf(order.getTable().getNumber()) + " " + (order.isWaiting() ? "En Attente" : order.isDelivered() ? "Commande Servie" : "Commande annulée") + " " + order.getTotal() + "€");
              Label timerLabel = new Label(order.getTimer());
              // Add a label for the timer
              // Launch a new thread to update the timer using the function startTimerThread
              Platform.runLater(() -> startTimerThread(order, timerLabel));

              // Add buttons to each cell to validate or cancel the order
              Button validateButton = new Button("Valider");
              validateButton.setOnAction(event -> {
                try {
                  validateOrder(order);
                  filteredOrders();
                } catch (SQLException e) {
                  e.printStackTrace();
                }
              });
              Button cancelButton = new Button("Annuler");
              try {
                cancelButton.setOnAction(event -> {
                  try {
                    cancelOrder(order);
                  } catch (SQLException e) {
                    throw new RuntimeException(e);
                  }
                });
              } catch (Exception e) {
                e.printStackTrace();
              }

              GridPane gridPane = new GridPane();
              totalGridPane.getChildren().clear();
              if (order.isWaiting() && totalGridPane.getChildren().isEmpty()) {
                gridPane.add(timerLabel, 1, 0);
                gridPane.add(validateButton, 2, 0);
                gridPane.add(cancelButton, 3, 0);
                setGraphic(gridPane);
                totalGridPane.add(gridPane, 0, 0);
              } else {
                gridPane.getChildren().clear();
              }
              setGraphic(totalGridPane);
            }
          }
        };
      }
    });
  }

  private void validateOrder(Order order) throws SQLException {
    order.setWaiting(false);
    order.setDelivered(true);
    order.setStatus("Payée");
    updateOrderInDatabase(order);
    createBill(order);
    filteredOrders();
  }

  private void cancelOrder(Order order) throws SQLException {
    order.setWaiting(false);
    order.setDelivered(false);
    order.setStatus("Annulée");
    updateOrderInDatabase(order);
    filteredOrders();
  }

  private void createBill(Order order) throws SQLException {
    boolean isLate = order.getTimer().equals("Out of time");
    double price = isLate ? order.getTotal() / 1.3 : order.getTotal();
    DatabaseManager db = new DatabaseManager();
    db.createBill(price, true);
  }



  private void updateOrderInDatabase(Order order) {
    try {
      new DatabaseManager().updateOrder(order);
      displayCells();
    } catch (SQLException e) {
      showError("Error updating order in database", e);
    }
  }

  private void startTimerThread(Order order, Label label) {
    Thread thread = new Thread(() -> {
      while (order.isWaiting()) {
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
          Thread.currentThread().interrupt();
        }
        Platform.runLater(() -> {
          if (order.getTimer().equals("Out of time")) {
            Thread.currentThread().interrupt();
          }
          label.setText(order.getTimer());
          checkColor(label, order);
        });
      }
    });

    thread.setDaemon(true);
    thread.start();
  }

  private void checkColor(Label label, Order order) {
    if (order.getTimer().equals("Out of time")) {
      label.setStyle("-fx-text-fill: red");
    } else if (parseInt(order.getTimer().split(":")[0]) < 3) {
      label.setStyle("-fx-text-fill: orange");
    } else {
      label.setStyle("-fx-text-fill: green");
    }
  }
}