package com.coding.restaurant.restaurant.controllers;

import com.coding.restaurant.restaurant.models.Order;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import org.kordamp.bootstrapfx.scene.layout.Panel;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;

public class OrdersDashboardController {
  @FXML
  private Panel ordersPanel;

  @FXML
  private GridPane ordersGridPane;

  public void initialize() {
    displayOrders();
  }

  private void displayOrders() {
    // Remove ordersGridPane rows
    ordersGridPane.getChildren().clear();
    try {
      List<Order> orders = new DatabaseManager().getOrders();

      orders.stream()
              .filter(Order::isWaiting)
              .sorted(Comparator.comparing(Order::getOrderDate))
              .forEach(this::addOrderRow);


    } catch (SQLException e) {
      showError("Error loading orders", e);
    }
  }

  private void showError(String errorLoadingOrders, SQLException e) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Error");
    alert.setHeaderText(errorLoadingOrders);
    alert.setContentText(e.getMessage());
    alert.showAndWait();
  }

  private void addOrderRow(Order order) {
    GridPane row = new GridPane();

    Label tableNumberLabel = new Label(String.valueOf(order.getTable().getNumber()));
    row.add(tableNumberLabel, 0, 0);

    Label timeLabel = new Label(order.getTimer());
    row.add(timeLabel, 1, 0);

    Label statusLabel = new Label(order.isWaiting() ? "En Attente" : "Servi");
    row.add(statusLabel, 2, 0);

    Label totalLabel = new Label(String.valueOf(order.getTotal()));
    row.add(totalLabel, 3, 0);

    Button detailsButton = new Button("Voir détails");
    row.add(detailsButton, 4, 0);

    Button validateButton = new Button("Valider");
    validateButton.setOnAction(event -> {
      try {
        validateOrder(order);
      } catch (SQLException e) {
        e.printStackTrace();
      }
    });
    row.add(validateButton, 5, 0);

    Button cancelButton = new Button("Annuler");
    cancelButton.setOnAction(event -> cancelOrder(order));
    row.add(cancelButton, 6, 0);

    ordersGridPane.addRow(ordersGridPane.getRowCount(), row);

    startTimerThread(order, timeLabel);
  }

  private void validateOrder(Order order) throws SQLException {
    order.setWaiting(false);
    order.setDelivered(true);
    order.setStatus("Payée");
    updateOrderInDatabase(order);
    createBill(order);
  }

  private void cancelOrder(Order order) {
    order.setWaiting(false);
    order.setDelivered(false);
    order.setStatus("Annulée");
    updateOrderInDatabase(order);
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
      displayOrders();
    } catch (SQLException e) {
      showError("Error updating order in database", e);
    }
  }

  private void startTimerThread(Order order, Label timeLabel) {
    Thread thread = new Thread(() -> {
      while (order.isWaiting()) {
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
          Thread.currentThread().interrupt();
        }
        Platform.runLater(() -> {
          // If the order is outdated stop the thread
          if (order.getTimer().equals("Out of time")) {
            Thread.currentThread().interrupt();
          }
          timeLabel.setText(order.getTimer());
        });
      }
    });

    thread.setDaemon(true);
    thread.start();
  }
}