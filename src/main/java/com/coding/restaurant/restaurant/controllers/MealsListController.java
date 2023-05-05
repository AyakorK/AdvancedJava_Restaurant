package com.coding.restaurant.restaurant.controllers;


import com.coding.restaurant.restaurant.models.Meal;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;

import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

//
  /**
   * Controller to manage the meals list view
   **/
public class MealsListController implements Initializable {

  @FXML
  private ListView<Meal> mealListView;

  @FXML
  private Button btnAscending;

  @FXML
  private Button btnDescending;

  @FXML
  private Label lblTotalPrice;

  @FXML
  private TextField txfSearch;

    /**
     *
     * @return
     * @throws SQLException
     */
  public ObservableList<Meal> filteredMeals() throws SQLException {
    DatabaseManager db = new DatabaseManager();
    List<Meal> meals = db.getMeals().stream().filter(Meal::isActive).toList();
    ObservableList<Meal> filteredMeals = FXCollections.observableArrayList();
    filteredMeals.addAll(meals);
    return filteredMeals;
  }

    /**
     *
     * @param searchTerm
     * @return
     */
  private Predicate<Meal> createSearchFilter(String searchTerm) {
    return meal -> meal.getDescription().toLowerCase().contains(searchTerm.toLowerCase());
  }

  public void searchMeal() {
// Filter the meals by the search term into the description
    try {
      // Empty mealListView
      mealListView.setItems(null);
      mealListView.setItems(filteredMeals().filtered(createSearchFilter(txfSearch.getText())));
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

    /**
     *
     * @param actionEvent
     */
  public void sortAscending(ActionEvent actionEvent) {
    // trier les plats par prix croissant
    // afficher les plats triés
    try {
      mealListView.setItems(filteredMeals().sorted(Comparator.comparing(Meal::getPrice)));
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

    /**
     *
     * @param actionEvent
     */
  public void sortDescending(ActionEvent actionEvent) {
    // trier les plats par prix décroissant
    // afficher les plats triés
    try {
      mealListView.setItems(filteredMeals().sorted(Comparator.comparing(Meal::getPrice).reversed()));
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

    /**
     *
     * @param actionEvent
     */
  public void showTotalPrice(ActionEvent actionEvent) {
    // afficher le prix total des plats
    try {
      Double totalPrice = filteredMeals().stream().mapToDouble(Meal::getPrice).sum();
      lblTotalPrice.setText("Prix total de la carte: " + totalPrice + "€");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

    /**
     *
     * @param location
     * The location used to resolve relative paths for the root object, or
     * {@code null} if the location is not known.
     *
     * @param resources
     * The resources used to localize the root object, or {@code null} if
     * the root object was not localized.
     */
  @Override
  public void initialize(URL location, ResourceBundle resources) {

    try {
      mealListView.setItems(filteredMeals());
    } catch (SQLException e) {
      e.printStackTrace();
    }

    showTotalPrice(null);

    mealListView.setCellFactory(listView -> new ListCell<Meal>() {
      private final ImageView imageView = new ImageView();

      /**
       *
       * @param meal The new item for the cell.
       * @param empty whether this cell represents data from the list. If it
       *        is empty, then it does not represent any domain data, but is a cell
       *        being used to render an "empty" row.
       */
      @Override
      protected void updateItem(Meal meal, boolean empty) {
        super.updateItem(meal, empty);

        if (empty || meal == null) {
          setText(null);
          setGraphic(null);
        } else {
          setText(meal.getName() + " - " + meal.getPriceString() + "€");
          imageView.setImage(new Image(meal.getImage(), true));
          imageView.setFitWidth(250);
          imageView.setFitHeight(200);
          setGraphic(imageView);
        }
      }
    });

    final Thread[] searchThread = {null};

  txfSearch.setOnKeyPressed((e) -> {
    // Do it 1 second after the user stopped typing (reset the timer if the user types again) using a Thread
    if (searchThread[0] != null && searchThread[0] != null) {
      searchThread[0].interrupt();
    }
    searchThread[0] = (new Thread(() -> {
      try {
        Thread.sleep(500);
        searchMeal();
      } catch (InterruptedException interruptedException) {
        interruptedException.printStackTrace();
      }
    }));
    searchThread[0].setDaemon(true);
    searchThread[0].start();
  });

  
    mealListView.setOnMouseClicked(event -> {
      if (event.getClickCount() == 2) {
        Meal selectedMeal = mealListView.getSelectionModel().getSelectedItem();
        if (selectedMeal != null) {
          Stage popupStage = new Stage();
          popupStage.initModality(Modality.APPLICATION_MODAL);
          popupStage.setTitle(selectedMeal.getName() + " - Description");

          ScrollPane scrollPane = new ScrollPane();
          scrollPane.setFitToWidth(true);
          scrollPane.setFitToHeight(true);
          scrollPane.setContent(new Label(selectedMeal.getDescription()));

          Label titleLabel = new Label(selectedMeal.getName());
          titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
          titleLabel.setAlignment(Pos.CENTER);
          titleLabel.setPadding(new Insets(20, 0, 10, 0));

          VBox vbox = new VBox();
          vbox.setPadding(new Insets(10, 10, 10, 10));
          vbox.setSpacing(10);
          vbox.setAlignment(Pos.CENTER);

          VBox textVBox = new VBox();
          textVBox.setSpacing(10);
          textVBox.setAlignment(Pos.CENTER);
          textVBox.getChildren().addAll(titleLabel, scrollPane);

          vbox.getChildren().addAll(textVBox);

          Scene popupScene = new Scene(vbox);
          popupStage.setScene(popupScene);
          popupStage.show();
        }
      }
    });
  }


}