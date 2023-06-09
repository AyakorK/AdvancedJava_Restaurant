package com.coding.restaurant.restaurant.controllers;

import com.coding.restaurant.restaurant.models.Meal;
import com.coding.restaurant.restaurant.models.Order;
import com.coding.restaurant.restaurant.models.Table;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

public class CreateOrdersController implements Initializable {

  @FXML
  private TableView<Meal> tvbMeals;
  @FXML
  private TableView<Meal> tvbOrder;

  @FXML
  private Button btnBack;

  @FXML
  private TableColumn<Meal, String> colName;
  @FXML
  private TableColumn<Meal, String> colPrice;
  @FXML
  private TableColumn<Meal, String> colActive;
  @FXML
  private TableColumn<Meal, String> colActions;

  @FXML
  private TableColumn<Meal, String> colOrderName;

  @FXML
  private TableColumn<Meal, String> colOrderPrice;

  @FXML
  private TableColumn<Meal, String> colOrderQuantity;

  @FXML
  private Button btnValidateOrder;

  @FXML
  private ComboBox cbxTable;

  @FXML
  private Label selectedTableInfo = new Label();

  List<HashMap> meals = new ArrayList<>();

  private ObservableList<Meal> observableMeal;
  private ObservableList<Meal> observableOrder;

  public List<Meal> getMeals() throws SQLException {
    DatabaseManager db = new DatabaseManager();
    return db.getMeals();
  }

  /**
   *
   * @return
   * @throws SQLException
   */
  public List<Table> getTables() throws SQLException {
    DatabaseManager db = new DatabaseManager();
    return db.getTables();
  }

  /**
   *
   * @param number
   * @return
   * @throws SQLException
   */
  public List<Table> getTablesByNumber(int number) throws SQLException {
    DatabaseManager db = new DatabaseManager();
    return db.getTablesByNumber(number);
  }

  /**
   *
   * @param url
   * The location used to resolve relative paths for the root object, or
   * {@code null} if the location is not known.
   *
   * @param resourceBundle
   * The resources used to localize the root object, or {@code null} if
   * the root object was not localized.
   */
  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {

    // When the button is clicked, go back to the worker view
    btnBack.setOnAction(event -> goBack());

    colName.setCellValueFactory(new PropertyValueFactory<>("name"));
    colOrderName.setCellValueFactory(new PropertyValueFactory<>("name"));
    colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    colOrderPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    colActive.setCellValueFactory(cellData -> {
      Meal meal = cellData.getValue();
      String activeStatus = meal.isActive() ? "actif" : "inactif";
      return new SimpleStringProperty(activeStatus);
    });

    colActions.setCellFactory(param -> new TableCell<Meal, String>() {
      private final Button btnDelete = new Button("Ajouter");

      {
        btnDelete.setOnAction(event -> {
          Meal meal = getTableView().getItems().get(getIndex());

          try {
            addMealToHash(meal, meals);
          } catch (SQLException e) {
            e.printStackTrace();
          }

        });
      }

      /**
       *
       * @param tempMeal
       * @param meals
       * @throws SQLException
       */
      protected void addMealToHash(Meal tempMeal, List<HashMap> meals) throws SQLException {
        HashMap<String, Object> mealMap = new HashMap<>();

        DatabaseManager db = new DatabaseManager();
        Meal meal = db.getMeal(tempMeal.getMealUUID());
        boolean mealAlreadyInList = observableOrder.stream().anyMatch(meal1 -> meal1.getMealUUID().equals(meal.getMealUUID()));

        addToHash(mealMap, meal, mealAlreadyInList, meals);
        // Search in tvbOrder if meal is already in the list, if yes, increment quantity
        // if no, add new meal
        //tvbOrder.getItems().stream()
      }

      /**
       *
       * @param mealMap
       * @param meal
       * @param mealAlreadyInList
       * @param meals
       */
      protected void addToHash(HashMap<String, Object> mealMap, Meal meal, boolean mealAlreadyInList, List<HashMap> meals) {
        if (mealAlreadyInList) {
          for (HashMap mealMap1 : meals) {
            if (mealMap1.get("meal").equals(meal.getMealUUID())) {
              int newQuantity = (int) mealMap1.get("quantity") + 1;
              mealMap1.put("quantity", newQuantity);
            }
          }

        } else {
          mealMap.put("meal", meal.getMealUUID());
          mealMap.put("quantity", 1);
          meals.add(mealMap);
        }
        observableOrder.add(meal);
      }

      /**
       *
       * @param item The new item for the cell.
       * @param empty whether this cell represents data from the list. If it
       *        is empty, then it does not represent any domain data, but is a cell
       *        being used to render an "empty" row.
       */
      @Override
      protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        setGraphic(empty ? null : btnDelete);
      }
    });

    btnValidateOrder.setOnAction(event -> {

      try {
        Order order = new Order(java.util.UUID.randomUUID().toString(), getTablesByNumber(Integer.parseInt(selectedTableInfo.getText())).get(0), true, false, meals, new Timestamp(new Date().getTime()), null);
        DatabaseManager db = new DatabaseManager();
        db.addOrder(order);
      } catch (SQLException e) {
        e.printStackTrace();
      }
    });

    try {
      List<Table> tables = getTables();
      tables.stream().filter(table -> !table.isFull()).forEach(table -> cbxTable.getItems().add(table.getNumber() + " - " + table.getLocation()));
//              .forEach(table -> cbxTable.getItems().add(table.getNumber() + " - " + table.getLocation()))

      cbxTable.setOnAction(event -> {
        String selectedTable = (String) cbxTable.getSelectionModel().getSelectedItem();
        String[] tableNumber = selectedTable.split(" - ");
        selectedTableInfo.setText(tableNumber[0]);
      });


      List<Meal> meals = getMeals();
      List<Meal> activeMeals = meals.stream().filter(Meal::isActive).toList();
      observableMeal = FXCollections.observableArrayList(activeMeals);
      observableOrder = FXCollections.observableArrayList();
      tvbMeals.setItems(observableMeal);
      tvbOrder.setItems(observableOrder);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

  }
  
  public void goBack() {
    try {
      // Load the order view & set it as the current view
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/coding/restaurant/restaurant/table-view.fxml"));
      Node tableView = loader.load();

      AnchorPane acpInTable = (AnchorPane) btnValidateOrder.getParent().getParent().getParent().getParent().getParent();

      acpInTable.getChildren().setAll(tableView);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}