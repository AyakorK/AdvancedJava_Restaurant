package com.coding.restaurant.restaurant.controllers;

import com.coding.restaurant.restaurant.models.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseManager {

  Connection db;

  public DatabaseManager() throws SQLException {
    this.db = ConnectDatabaseController.getConnection();
  }

  public List<Bill> getBills() {
    List<Bill> bills = new ArrayList<>();
    try (PreparedStatement statement = this.db.prepareStatement("SELECT * FROM Bill");
         ResultSet result = statement.executeQuery()) {
      while (result.next()) {
        Bill bill = new Bill(result.getBoolean("type"), result.getDouble("amount"), result.getDate("billDate"));
        bills.add(bill);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return bills;
  }

  public List<Bill> getBillsOfThisMonth() {
    List<Bill> bills = new ArrayList<>();
    // Get the bills of this month only
    try (PreparedStatement statement = this.db.prepareStatement("SELECT * FROM Bill WHERE MONTH(billDate) = MONTH(CURRENT_DATE()) AND YEAR(billDate) = YEAR(CURRENT_DATE())");
         ResultSet result = statement.executeQuery()) {
      while (result.next()) {
        Bill bill = new Bill(result.getBoolean("type"), result.getDouble("amount"), result.getDate("billDate"));
        bills.add(bill);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return bills;
  }

  // Get the meals (Meal)
  public List<Meal> getMeals() {
    List<Meal> meals = new ArrayList<>();
    try (PreparedStatement statement = this.db.prepareStatement("SELECT * FROM Meal");
         ResultSet result = statement.executeQuery()) {
      while (result.next()) {
        String name = result.getString("name");
        String description = result.getString("description");
        float price = result.getFloat("price");
        String image = result.getString("image");
        boolean isActiveMeal = findActive(result);
        Meal meal = new Meal(name, description, price, image, isActiveMeal);
        meals.add(meal);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return meals;
  }

  public static void addMeal(String name, String description, Double price, String image, Boolean isActive, String type) {
    try (Connection connexion = ConnectDatabaseController.getConnection();
         PreparedStatement statement = connexion.prepareStatement("INSERT INTO Meal (UUID, name, description, price, image, isActive,Type) VALUES (?,?,?, ?, ?, ?, ?)")) {
      statement.setString(1, DatabaseManager.generateUUID());
      statement.setString(2, name);
      statement.setString(3, description);
      statement.setDouble(4, price);
      statement.setString(5, image);
      statement.setBoolean(6, isActive);
      statement.setString(7, type);
      statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  //    // Get the meals list (MealsList)
  public List<MealsList> getMealsList() {
    List<MealsList> mealsLists = new ArrayList<>();
    try (PreparedStatement statement = this.db.prepareStatement("SELECT * FROM MealsList");
         ResultSet result = statement.executeQuery()) {
      while (result.next()) {
        Date menuDate = result.getDate("MenuDate");
        String mealListUUID = result.getString("UUID");
        List<Meal> meals = getMealsItems(mealListUUID);
        MealsList mealList = new MealsList(meals, menuDate);
        mealsLists.add(mealList);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return mealsLists;
  }

  // Get the meals items (Meal[])

  public List<Meal> getMealsItems(String mealListUUID) {
    List<Meal> mealsInList = new ArrayList<>();
    try (PreparedStatement statement = this.db.prepareStatement("SELECT * FROM Meal WHERE uuid IN (SELECT mealUUID FROM MealsListItems WHERE mealsListUUID = ?)")) {
      searchItems(mealListUUID, mealsInList, statement);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return mealsInList;
  }

  private void searchItems(String mealListUUID, List<Meal> mealsInList, PreparedStatement statement) throws SQLException {
    statement.setString(1, mealListUUID);
    ResultSet result = statement.executeQuery();
    while (result.next()) {
      Meal meal = new Meal(result.getString("name"), result.getString("description"), result.getFloat("price"), result.getString("image"), findActive(result));
      mealsInList.add(meal);
    }
  }

  //    // Get the orders (Order)
  public List<Order> getOrders() {
    List<Order> orders = new ArrayList<>();
    try (PreparedStatement statement = this.db.prepareStatement("SELECT * FROM Orders");
         ResultSet result = statement.executeQuery()) {
      while (result.next()) {
        String orderUUID = result.getString("UUID");
        boolean isWaiting = result.getBoolean("isWaiting");
        boolean isDelivered = result.getBoolean("isDelivered");
        List<Meal> orderItems = getOrdersItems(orderUUID);
        Table table = getTable(result.getString("TableUUID"));
        Timestamp timestamp = result.getTimestamp("dateCreation");
        String status = result.getString("finalStatus");
        Order order = new Order(orderUUID, table, isWaiting, isDelivered, orderItems, timestamp, status);
        orders.add(order);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return orders;
  }


  // Get the meals items (Meal[])
  public List<Meal> getOrdersItems(String orderUUID) {

    List<Meal> mealsInList = new ArrayList<>();
    try (PreparedStatement statement = this.db.prepareStatement("SELECT * FROM Meal WHERE uuid IN (SELECT mealUUID FROM OrdersItems WHERE ordersUUID = ?)")) {
      searchItems(orderUUID, mealsInList, statement);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return mealsInList;
  }

  // Update an order (Order)
  public void updateOrder(Order order) throws SQLException {
    try (PreparedStatement statement = this.db.prepareStatement("UPDATE Orders SET isWaiting = ?, isDelivered = ?, finalStatus = ? WHERE UUID = ?")) {
      statement.setBoolean(1, order.isWaiting());
      statement.setBoolean(2, order.isDelivered());
      statement.setString(3, order.getStatus());
      statement.setString(4, order.getOrderUUID());
      statement.executeUpdate();
    }
  }

  //    // Get the tables (Table)
  public List<Table> getTables() {
    List<Table> tables = new ArrayList<>();
    try (PreparedStatement statement = this.db.prepareStatement("SELECT * FROM TableRestaurant");
         ResultSet result = statement.executeQuery()) {
      while (result.next()) {
        int number = result.getInt("numero");
        String location = result.getString("location");
        int size = result.getInt("size");
        boolean isFull = result.getBoolean("isFull");
        Table table = new Table(number, location, size, isFull);
        tables.add(table);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return tables;
  }

  public Table createTable(int number, String location, int size, boolean isFull) {
    try (PreparedStatement statement = this.db.prepareStatement("INSERT INTO TableRestaurant (numero, location, size, isFull) VALUES (?, ?, ?, ?)")) {
      statement.setInt(1, number);
      statement.setString(2, location);
      statement.setInt(3, size);
      statement.setBoolean(4, isFull);
      statement.executeUpdate();
      return new Table(number, location, size, isFull);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public Table deleteTable(int number) {
    try (PreparedStatement statement = this.db.prepareStatement("DELETE FROM TableRestaurant WHERE numero = ?")) {
      statement.setInt(1, number);
      statement.executeUpdate();
      return new Table(number, null, 0, false);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  // Get the table (Table)
  public Table getTable(String tableUUID) {
    try (PreparedStatement statement = this.db.prepareStatement("SELECT * FROM TableRestaurant WHERE uuid = ?")) {
      statement.setString(1, tableUUID);
      ResultSet result = statement.executeQuery();
      if (result.next()) {
        return new Table(result.getInt("numero"), result.getString("location"), result.getInt("size"), result.getBoolean("isFull"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  // Get Workers
  public List<Worker> getWorkers() {
    List<Worker> workers = new ArrayList<>();
    try (PreparedStatement statement = this.db.prepareStatement("SELECT * FROM Worker")) {
      ResultSet result = statement.executeQuery();
      while (result.next()) {
        String workerUUID = result.getString("UUID");
        String name = result.getString("Name");
        String surname = result.getString("FirstName");
        Boolean isActive = findActive(result);
        Integer hoursWorked = result.getInt("HoursWorked");
        String role = result.getString("role");
        Date arrivalDate = result.getDate("arrivalDate");
        Date departureDate = result.getDate("departureDate");
        int age = result.getInt("Age");
        Worker worker = new Worker(workerUUID, name, surname, isActive, hoursWorked, role, arrivalDate, departureDate, age);
        workers.add(worker);
      }

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return workers;
  }

  public Worker getWorker(String name, String surname) {
    try (PreparedStatement statement = this.db.prepareStatement("SELECT * FROM Worker WHERE name = ? AND firstName = ?")) {
      statement.setString(1, name);
      statement.setString(2, surname);
      ResultSet result = statement.executeQuery();
      if (result.next()) {
        return new Worker(result.getString("UUID"), result.getString("Name"), result.getString("FirstName"), findActive(result), result.getInt("HoursWorked"), result.getString("role"), result.getDate("arrivalDate"), result.getDate("departureDate"), result.getInt("Age"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  private boolean findActive(ResultSet result) throws SQLException {
    return result.getBoolean("isActive");
  }

  public Service createService(Service service) {
    // Search if there is already a service with the same date and the same period AND that has been created more than 3h ago
    try (PreparedStatement statement = this.db.prepareStatement("SELECT * FROM Service WHERE ServiceDate = ? AND ServicePeriod = ? OR CreatedAt < DATE_SUB(NOW(), INTERVAL 3 HOUR)")) {
      statement.setDate(1, service.getBeginDate());
      statement.setString(2, service.getPeriod());
      ResultSet result = statement.executeQuery();
      if (result.next()) {
        // If there is one, we return the service
        return new Service(result.getString("UUID"), result.getDate("ServiceDate"), result.getTimestamp("CreatedAt"), result.getString("ServicePeriod"), getServiceWorkers(result.getString("UUID")), result.getBoolean("isPaid"));
      } else {
        // If there is none, we create a new one
        createNewService(service);
        return service;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  private void createNewService(Service service) throws SQLException {
    try (PreparedStatement statement = this.db.prepareStatement("INSERT INTO Service (UUID, ServiceDate, CreatedAt, ServicePeriod) VALUES (?, ?, ?, ?)")) {
      statement.setString(1, service.getServiceUUID());
      statement.setDate(2, service.getBeginDate());
      statement.setTimestamp(3, service.getCreatedAt());
      statement.setString(4, service.getPeriod());
      statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    addWorkersToService(service.getServiceUUID(), service.getWorkers());
  }

  // Get the workers of a service (Worker[])
  private List<Worker> getServiceWorkers(String serviceUUID) {
    List<Worker> workersInService = new ArrayList<>();
    try (PreparedStatement statement = this.db.prepareStatement("SELECT * FROM Worker WHERE uuid IN (SELECT workerUUID FROM ServiceWorkers WHERE serviceUUID = ?)")) {
      searchWorkers(serviceUUID, workersInService, statement);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return workersInService;
  }

  private void searchWorkers(String serviceUUID, List<Worker> workersInService, PreparedStatement statement) throws SQLException {
    statement.setString(1, serviceUUID);
    ResultSet result = statement.executeQuery();
    while (result.next()) {
      Worker worker = new Worker(result.getString("UUID"), result.getString("Name"), result.getString("FirstName"), findActive(result), result.getInt("HoursWorked"), result.getString("role"), result.getDate("arrivalDate"), result.getDate("departureDate"), result.getInt("Age"));
      workersInService.add(worker);
    }
  }

  private void addWorkersToService(String uuid, List<Worker> workers) {
    try (PreparedStatement statement = this.db.prepareStatement("INSERT INTO ServiceWorkers (serviceUUID, workerUUID) VALUES (?, ?)")) {
      for (Worker worker : workers) {
        statement.setString(1, uuid);
        statement.setString(2, worker.getWorkerUUID());
        statement.executeUpdate();
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static String generateUUID() {
    return java.util.UUID.randomUUID().toString();
  }

  public void endService(Service service) {
    try (PreparedStatement statement = this.db.prepareStatement("UPDATE Service SET isPaid = ? WHERE UUID = ?")) {
      statement.setBoolean(1, service.isPaid());
      statement.setString(2, service.getServiceUUID());
      updateWorkersHours(service);
      statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private void updateWorkersHours(Service service) {
    service.getWorkers().stream()
            .filter(Worker::isActive)
            .forEach(worker -> {
              try (PreparedStatement statement = this.db.prepareStatement("UPDATE Worker SET HoursWorked = ? WHERE UUID = ?")) {
                statement.setInt(1, (worker.getHoursWorked() + 3));
                statement.setString(2, worker.getWorkerUUID());
                statement.executeUpdate();
              } catch (SQLException e) {
                e.printStackTrace();
              }
            });
  }

}

