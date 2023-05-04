package com.coding.restaurant.restaurant.controllers;

import com.coding.restaurant.restaurant.models.Order;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

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

  public void displayCells() {
    ordersListView.setCellFactory(cell -> new OrderListCell());
  }

  private class OrderListCell extends ListCell<Order> {

    private final GridPane totalGridPane = new GridPane();

    private final Label timerLabel = new Label();

    String outOfTime = "Out of time";

    public OrderListCell() {
      GridPane gridPane = new GridPane();
      gridPane.add(timerLabel, 1, 0);
      Button validateButton = new Button("Valider");
      gridPane.add(validateButton, 2, 0);
      Button cancelButton = new Button("Annuler");
      gridPane.add(cancelButton, 3, 0);
      totalGridPane.getChildren().add(gridPane);

      validateButton.setOnAction(event -> {
        try {
          validateOrder(getItem());
          filteredOrders();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      });

      cancelButton.setOnAction(event -> {
        try {
          cancelOrder(getItem());
        } catch (SQLException e) {
          e.printStackTrace();
        }
      });
    }

    @Override
    protected void updateItem(Order order, boolean empty) {
      super.updateItem(order, empty);

      if (empty || order == null) {
        setText(null);
        setGraphic(null);
        timerLabel.setText("");
      } else {
        try {
          setText(order.getOrderDate() + " " + order.getTable().getNumber() + " " + orderState(order) + " " + order.getTotal() + "€");
        } catch (SQLException e) {
          throw new RuntimeException(e);
        }
        timerLabel.setText(order.getTimer());
        startTimerThread(order, timerLabel);
        setGraphic(totalGridPane);
      }
    }

    private String orderState(Order order) {
      return order.isWaiting() ? "En Attente" : isDelivered(order);
    }

    private String isDelivered(Order order) {
      return order.isDelivered() ? "Commande Servie" : "Commande annulée";
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
      boolean isLate = order.getTimer().equals(outOfTime);
      double price = isLate ? order.getTotal() / 1.3 : order.getTotal();
      DatabaseManager db = new DatabaseManager();
      db.createBill(price, true);
    }



    private void updateOrderInDatabase(Order order) {
      try {
        new DatabaseManager().updateOrder(order);
        displayCells();
      } catch (SQLException e) {
        showError(e);
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
            if (order.getTimer().equals(outOfTime)) {
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
      if (order.getTimer().equals(outOfTime)) {
        label.setStyle("-fx-text-fill: red");
      } else if (order.getTimer().split(":")[0].matches("\\d+") && parseInt(order.getTimer().split(":")[0]) < 3) {
        label.setStyle("-fx-text-fill: orange");
      } else {
        label.setStyle("-fx-text-fill: green");
      }
    }


    private void showError(SQLException e) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Error");
      alert.setHeaderText("Error updating order in database");
      alert.setContentText(e.getMessage());
      alert.showAndWait();
    }
  }
}