package com.coding.restaurant.restaurant.Controller;

import com.coding.restaurant.restaurant.Model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
                Bill bill = new Bill(result.getString("type"), result.getDouble("amount"), result.getString("billDate"));
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
                boolean isActive = result.getBoolean("isActive");
                Meal meal = new Meal(name, description, price, image, isActive);
                meals.add(meal);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return meals;
    }

    //    // Get the meals list (MealsList)
    public List<MealsList> getMealsList() {
        List<MealsList> mealsLists = new ArrayList<>();
        try (PreparedStatement statement = this.db.prepareStatement("SELECT * FROM MealsList");
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                Date MenuDate = result.getDate("MenuDate");
                String mealListUUID = result.getString("UUID");
                List<Meal> meals = getMealsItems(mealListUUID);
                MealsList mealList = new MealsList(meals, MenuDate);
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
            SearchItems(mealListUUID, mealsInList, statement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mealsInList;
    }

    private void SearchItems(String mealListUUID, List<Meal> mealsInList, PreparedStatement statement) throws SQLException {
        statement.setString(1, mealListUUID);
        ResultSet result = statement.executeQuery();
        while (result.next()) {
            Meal meal = new Meal(result.getString("name"), result.getString("description"), result.getFloat("price"), result.getString("image"), result.getBoolean("isActive"));
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
            Order order = new Order(table, isWaiting, isDelivered, orderItems);
            orders.add(order);
        }
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
    return orders;
}


    // Get the meals items (Meal[])
    public List<Meal> getOrdersItems(String orderUUID) {

        List<Meal> mealsInList = new ArrayList<>();
        try (PreparedStatement statement = this.db.prepareStatement("SELECT * FROM Meal WHERE uuid IN (SELECT mealUUID FROM OrdersItems WHERE ordersUUID = ?)")) {
            SearchItems(orderUUID, mealsInList, statement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mealsInList;
    }
//
//    // Get the tables (Table)
    public ResultSet getTables() {
        try (PreparedStatement statement = this.db.prepareStatement("SELECT * FROM TableRestaurant");
             ResultSet result = statement.executeQuery()) {
            return result;
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
                return new Table(result.getString("numero"), result.getString("location"), result.getInt("size"), result.getBoolean("isFull"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

