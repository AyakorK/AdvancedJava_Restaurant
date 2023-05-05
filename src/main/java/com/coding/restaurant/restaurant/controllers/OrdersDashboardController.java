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

/**
 * Controller of the Orders Dashboard
 */
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

  // Filter the orders by the waiting ones and the most urgent ones first

  /**
   *
   * @return
   * @throws SQLException
   */
  public ObservableList<Order> filteredOrders() throws SQLException {
    DatabaseManager db = new DatabaseManager();
    List<Order> orders = db.getOrders().stream().filter(Order::isWaiting).sorted(Comparator.comparing(Order::getTimer)).toList();
    ObservableList<Order> filteredOrder = FXCollections.observableArrayList();
    filteredOrder.addAll(orders);

    ordersListView.setItems(filteredOrder);
    return filteredOrder;
  }

  // Filter the orders by the passed ones

  /**
   *
   * @return
   * @throws SQLException
   */
  public ObservableList<Order> displayPassedOrders() throws SQLException {
    DatabaseManager db = new DatabaseManager();
    List<Order> orders = db.getOrders().stream().filter(order -> !order.isWaiting()).toList();
    ObservableList<Order> filteredOrder = FXCollections.observableArrayList();
    filteredOrder.addAll(orders);

    ordersListView.setItems(filteredOrder);
    return filteredOrder;
  }

  // Filter the orders by the last 5 passed ones

  /**
   *
   * @return
   * @throws SQLException
   */
  public ObservableList<Order> displayLastOrders() throws SQLException {
    List<Order> orders = displayPassedOrders().stream().limit(5).toList();
    ObservableList<Order> filteredOrder = FXCollections.observableArrayList();
    filteredOrder.addAll(orders);

    ordersListView.setItems(filteredOrder);
    return filteredOrder;
  }

  // Display all the orders into cells
  public void displayCells() {
    ordersListView.setCellFactory(cell -> new OrderListCell());
  }
  private class OrderListCell extends ListCell<Order> {

    private final GridPane totalGridPane = new GridPane();

    private final Label timerLabel = new Label();

    String outOfTime = "Out of time";

    /**
     * Constructor of the OrderListCell class
     * It creates a gridPane with the timerLabel, the validateButton and the cancelButton
     * It has been generated dynamically to match only this purpose but to simplify the usage and identification of functions
     * Needs to be empty but can be used to add other elements
     */
    public OrderListCell() {
      // This is empty because that's a constructor that is not useful for us
    }


    /**
     * Function to validate an order
     *
     * @param order the order to validate
     * @throws SQLException
     */
    @Override
    protected void updateItem(Order order, boolean empty) {

      super.updateItem(order, empty);

      if (empty || order == null) {
        setText(null);
        setGraphic(null);
        timerLabel.setText("");
      } else {
        // Add space between each lines of text
        setStyle("-fx-padding: 0 0 0 5;");

        setText(order.getOrderDate() + " " + order.getTable().getNumber() + " " + orderState(order) + " " + order.getTotal() + "€");
        setLineSpacing(10);
        totalGridPane.getChildren().clear();

        if (order.isWaiting()) {
          generateGridPane(order);
          setGraphic(totalGridPane);
        }
      }
    }

    // Generate the gridPane with the timerLabel, the validateButton and the cancelButton

    /**
     *
     * @param order
     */
    protected void generateGridPane(Order order) {
      GridPane gridPane = new GridPane();
      // Set space on the grid pane
      gridPane.setHgap(10);
      gridPane.setVgap(10);
      gridPane.add(timerLabel, 1, 0);
      Button validateButton = new Button("Valider");
      gridPane.add(validateButton, 2, 0);
      Button cancelButton = new Button("Annuler");
      gridPane.add(cancelButton, 3, 0);
      timerLabel.setText(order.getTimer());
      startTimerThread(order, timerLabel);
      totalGridPane.getChildren().add(gridPane);


      validateButton.setOnAction(event -> {
        try {
          validateOrder(getItem());
          filteredOrders();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      });

      // When the cancelButton is clicked, the order is cancelled
      cancelButton.setOnAction(event -> {
        try {
          cancelOrder(getItem());
        } catch (SQLException e) {
          e.printStackTrace();
        }
      });
    }

    // Get the Order State

    /**
     *
     * @param order
     * @return
     */
    private String orderState(Order order) {
      return order.isWaiting() ? "En Attente" : isDelivered(order);
    }

    // If not waiting get the Delivery State

    /**
     *
     * @param order
     * @return
     */
    private String isDelivered(Order order) {
      return order.isDelivered() ? "Commande Servie" : "Commande annulée";
    }

    // Validate the order function

    /**
     *
     * @param order
     * @throws SQLException
     */
    private void validateOrder(Order order) throws SQLException {
      order.setWaiting(false);
      order.setDelivered(true);
      order.setStatus("Payée");
      updateOrderInDatabase(order);
      createBill(order);
      filteredOrders();
    }

    // Cancel the order function

    /**
     *
     * @param order
     * @throws SQLException
     */
    private void cancelOrder(Order order) throws SQLException {
      order.setWaiting(false);
      order.setDelivered(false);
      order.setStatus("Annulée");
      updateOrderInDatabase(order);
      filteredOrders();
    }

    // Create the bill function

    /**
     *
     * @param order
     * @throws SQLException
     */
    private void createBill(Order order) throws SQLException {
      boolean isLate = order.getTimer().equals(outOfTime);
      double price = isLate ? order.getTotal() / 1.3 : order.getTotal();
      DatabaseManager db = new DatabaseManager();
      db.createBill(price, true);
    }


    // Update the order in the database

    /**
     *
     * @param order
     */
    private void updateOrderInDatabase(Order order) {
      try {
        new DatabaseManager().updateOrder(order);
        displayCells();
      } catch (SQLException e) {
        showError(e);
      }
    }

    // Start the timer thread to update the timerLabel each second

    /**
     *
     * @param order
     * @param label
     */
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
            label.setText(order.getTimer().equals(outOfTime) ? outOfTime : order.getTimer());
            checkColor(label, order);
          });
        }
      });

      thread.setDaemon(true);
      thread.start();
    }

    // Change the color based on the timer value

    /**
     *
     * @param label
     * @param order
     */
    private void checkColor(Label label, Order order) {
      if (order.getTimer().equals(outOfTime)) {
        label.setStyle("-fx-text-fill: red");
      } else if (order.getTimer().split(":")[0].matches("\\d+") && parseInt(order.getTimer().split(":")[0]) < 3) {
        label.setStyle("-fx-text-fill: orange");
      } else {
        label.setStyle("-fx-text-fill: green");
      }
    }

    /**
     *
     * @param e
     */
    private void showError(SQLException e) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Error");
      alert.setHeaderText("Error updating order in database");
      alert.setContentText(e.getMessage());
      alert.showAndWait();
    }
  }
}