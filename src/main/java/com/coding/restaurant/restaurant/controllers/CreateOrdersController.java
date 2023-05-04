package com.coding.restaurant.restaurant.controllers;

import com.coding.restaurant.restaurant.models.Meal;
import com.coding.restaurant.restaurant.models.Order;
import com.coding.restaurant.restaurant.models.Table;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

public class CreateOrdersController implements Initializable {

  @FXML
  private Button btnNewWorker;

  @FXML
  private AnchorPane acpInWorker;
  @FXML
  private AnchorPane acpNewWorker;

  @FXML
  private VBox vbxWorker;

  @FXML
  private TableView<Meal> tvbMeals;
  @FXML
  private TableView<Meal> tvbOrder;

  @FXML
  private TableColumn<Meal, String> colName;
  @FXML
  private TableColumn<Meal, String> colPrice;
  @FXML
  private TableColumn<Meal, String> colActive;
  @FXML
  private TableColumn<Meal, String> colHours;
  @FXML
  private TableColumn<Meal, String> colRole;
  @FXML
  private TableColumn<Meal, String> colArrived;
  @FXML
  private TableColumn<Meal, String> colDeparture;
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

  List<HashMap> meals = new ArrayList<>();

  private ObservableList<Meal> observableMeal;
  private ObservableList<Meal> observableOrder;

  public List<Meal> getMeals() throws SQLException {
    DatabaseManager db = new DatabaseManager();
    return db.getMeals();
  }

  public List<Order> getOrders() throws SQLException {
    DatabaseManager db = new DatabaseManager();
    return db.getOrders();
  }

  public HashMap addMealHash(Meal meal, int quantity) {
    HashMap<String, Object> mealMap = new HashMap<>();
    mealMap.put("meal", meal);
    mealMap.put("quantity", quantity);
    return mealMap;
  }

  public HashMap addMealHash2(Meal meal) {
    HashMap<String, Object> mealMap = new HashMap<>();
    mealMap.put("meal", meal);
//    mealMap.put("quantity", quantity);
    return mealMap;
  }

  //      check if mealUUID is already in the list
//      if yes, increment quantity
//      if no, add new meal
  public List<HashMap> addMealToList(List<HashMap> list, Meal meal, int quantity) {
    List<HashMap> newList = new ArrayList<>();
    boolean mealAlreadyInList = false;
    for (HashMap mealMap : list) {
      if (mealMap.get("meal").equals(meal)) {
        mealAlreadyInList = true;
        int newQuantity = (int) mealMap.get("quantity") + quantity;
        newList.add(addMealHash(meal, newQuantity));
      } else {
        newList.add(mealMap);
      }
    }
    if (!mealAlreadyInList) {
      newList.add(addMealHash(meal, quantity));
    }
    return newList;
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    acpInWorker.getChildren().remove(acpNewWorker);

    btnNewWorker.setOnMouseClicked(e -> {
      acpInWorker.getChildren().remove(vbxWorker);
      acpInWorker.getChildren().add(acpNewWorker);
    });

//    colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
    colName.setCellValueFactory(new PropertyValueFactory<>("name"));
    colOrderName.setCellValueFactory(new PropertyValueFactory<>("name"));
    colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    colOrderPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    colActive.setCellValueFactory(cellData -> {
      Meal meal = cellData.getValue();
      String activeStatus = meal.isActive() ? "actif" : "inactif";
      return new SimpleStringProperty(activeStatus);
    });
//    colHours.setCellValueFactory(new PropertyValueFactory<>("hoursWorked"));
//    colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
//    colArrived.setCellValueFactory(new PropertyValueFactory<>("arrivalDate"));
//    colDeparture.setCellValueFactory(cellData -> {
//      Meal meal = cellData.getValue();f
//      String departureDate = meal.getDepartureDate().equals(worker.getArrivalDate()) ? "Non indiquÃ©" : String.valueOf(worker.getDepartureDate());
//      return new SimpleStringProperty(departureDate);
//    });
    // add 2 boutons in colActions : edit and delete
    colActions.setCellFactory(param -> new TableCell<Meal, String>() {
      //      private final Button btnEdit = new Button("Modifier");
      private final Button btnDelete = new Button("Ajouter");
//      private final HBox pane = new HBox(btnDelete);

      {
//        btnEdit.setOnAction(event -> {
//          Meal meal = getTableView().getItems().get(getIndex());
//          System.out.println("edit " + meal.getName());
//        });
        btnDelete.setOnAction(event -> {
          Meal meal = getTableView().getItems().get(getIndex());
          System.out.println("Ajout de " + meal.getMealUUID());

          try {
            addMealToHash(meal, meals);
          } catch (SQLException e) {
            e.printStackTrace();
          }

//          colOrderQuantity.setCellValueFactory(new PropertyValueFactory<>("name"));
//          tvbWorkers1.refresh();
//          System.out.println(tvbOrder.getItems());

//                    try {
//                        DatabaseManager db = new DatabaseManager();
////                        db.deleteWorker(worker);
//                        observableWorkers.remove(worker);
//                    } catch (SQLException e) {
//                        throw new RuntimeException(e);
//                    }
        });
      }

      protected void addMealToHash(Meal tempMeal, List<HashMap> meals) throws SQLException {
        HashMap<String, Object> mealMap = new HashMap<>();

        DatabaseManager db = new DatabaseManager();
        Meal meal = db.getMeal(tempMeal.getMealUUID());
        boolean mealAlreadyInList = observableOrder.stream().anyMatch(meal1 -> meal1.getMealUUID().equals(meal.getMealUUID()));

        addToHash(mealMap, meal, mealAlreadyInList, meals);
        // Search in tvbOrder if meal is already in the list, if yes, increment quantity
        // if no, add new meal
        //tvbOrder.getItems().stream()
        System.out.println(meals);
      }

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

      @Override
      protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        setGraphic(empty ? null : btnDelete);
      }
    });

    btnValidateOrder.setOnAction(event -> {
      System.out.println("validate order");
      meals.stream().forEach(System.out::println);


      Table table = new Table("0d86ab6e-e9bd-11ed-a7c3-525400008e03",1, "Terrasse", 4, false);

      Order order = new Order(java.util.UUID.randomUUID().toString(), table, true, false, meals, new Timestamp(new Date().getTime()), null);

      try {
        DatabaseManager db = new DatabaseManager();
        db.addOrder(order);
      } catch (SQLException e) {
        e.printStackTrace();
      }
    });

    try {
      List<Meal> meals = getMeals();
      List<Order> orders = getOrders();
      orders.forEach(order -> System.out.println(order.getMeals()));
//      orders.forEach(order -> order.getMeals().forEach(meal -> System.out.println(meal.getName())));
//      orders.forEach(order -> order.getMeals().forEach(meal -> System.out.println(((Meal) meal.get("meal")).getName())));

//      List<Order> ordersMeal = orders.forEach(order -> order.getMeals().forEach(meal -> ((Meal) meal.get("meal")).getName()));

      List<String> orderMeals = orders.stream()
              .flatMap(order -> order.getMeals().stream())
              .map(meal -> String.valueOf(((Meal) meal.get("meal"))))
              .toList();
//      System.out.println(orderMeals);

//      forEach orderMeals print meal's name
//      orderMeals.forEach(meal -> System.out.println(((Meal) meal.get("meal")).getName()));


      orderMeals.forEach(meal -> System.out.println(meal));

      orders.forEach(order -> order.getMeals());
//      orders.forEach(Order::getMeals);
//      System.out.println(ordersMeal);
//
//      meals.forEach(meal -> System.out.println(meal));
//      orders.forEach(order -> System.out.println(order));


      List<Meal> meals2 = orders.stream()
              .flatMap(order -> order.getMeals().stream())
              .map(meal -> (Meal) meal.get("meal"))
              .toList();
      observableMeal = FXCollections.observableArrayList(meals);
      observableOrder = FXCollections.observableArrayList();
      tvbMeals.setItems(observableMeal);
      tvbOrder.setItems(observableOrder);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }


  }

}