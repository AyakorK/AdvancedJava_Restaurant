package com.coding.restaurant.restaurant.controllers;

import com.coding.restaurant.restaurant.models.Service;
import com.coding.restaurant.restaurant.models.Worker;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Date;
import java.util.List;

/**
 * Controller of the home page
 */
public class HomeController {
  @FXML
  Label serviceTimeLeft;

  /**
   *
   * @throws SQLException
   */
  public void initialize() throws SQLException {
    Service service = startService();

    serviceTimeLeft.setText(service.getTimer(service));

    startTimerThread(service, serviceTimeLeft);
  }

  // When arriving on the home page, start a new service

  /**
   *
   * @return
   * @throws SQLException
   */
  public Service startService() throws SQLException {
    DatabaseManager db = new DatabaseManager();
    List<Worker> workers = db.getWorkers();
    // Stream workers to get only those actives
    // Create a new service with the workers
    List<Worker> activeWorkers = workers.stream()
            .filter(Worker::isActive)
            .toList();
    Date beginDate = new Date(System.currentTimeMillis());
    Timestamp createdAt = new Timestamp(System.currentTimeMillis());
    String period = getPeriod(createdAt);
    Boolean isPaid = false;
    return db.createService(new Service(DatabaseManager.generateUUID(), beginDate, createdAt, period, activeWorkers, isPaid));
  }

  // Get the period of the day (midday or evening)

  /**
   *
   * @param beginDate
   * @return
   */
  public String getPeriod(Timestamp beginDate) {
    // If the beginDate is between 11:00 and 14:00, the period is midday and if between 18:00 and 21:00, the period is evening
    int hour = beginDate.getHours();
    return hour < 14 ? "midday" : "evening";
  }

  // Start a thread to update the timer every second

  /**
   *
   * @param service
   * @param serviceTimeLeft
   */
  public void startTimerThread(Service service, Label serviceTimeLeft) {
    Thread timerThread = new Thread(() -> {
      while (true) {
        try {
          if (service.getTimer(service).equals("Service terminé !")) break;
        } catch (SQLException e) {
          e.printStackTrace();
          Thread.currentThread().interrupt();
        }
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
          Thread.currentThread().interrupt();
        }
        Platform.runLater(() -> {
          try {
            serviceTimeLeft.setText(service.getTimer(service));
          } catch (SQLException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
          }
        });
      }
    });
    timerThread.start();
  }
}
