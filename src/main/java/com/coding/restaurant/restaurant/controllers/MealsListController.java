package com.coding.restaurant.restaurant.controllers;


import com.coding.restaurant.restaurant.models.Meal;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.util.Callback;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;

import java.util.ResourceBundle;


public class MealsListController implements Initializable {

  @FXML
  private ListView<Meal> mealListView;

  public ObservableList<Meal> filteredMeals() throws SQLException {
    DatabaseManager db = new DatabaseManager();
    List<Meal> meals = db.getMeals().stream().filter(Meal::isActive).toList();
    ObservableList<Meal> filteredMeals = FXCollections.observableArrayList();
    filteredMeals.addAll(meals);
    return filteredMeals;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    try {
      mealListView.setItems(filteredMeals());
    } catch (SQLException e) {
      e.printStackTrace();
    }

    mealListView.setCellFactory(new Callback<>() {
      @Override
      public ListCell<Meal> call(ListView<Meal> listView) {
        return new ListCell<>() {
          private ImageView imageView = new ImageView();

          @Override
          protected void updateItem(Meal meal, boolean empty) {
            super.updateItem(meal, empty);

            if (empty || meal == null) {
              setText(null);
              setGraphic(null);
            } else {
              setText(meal.getName() + " - " + meal.getPrice() + "€");
              imageView.setImage(new Image(meal.getImage(), true));
              imageView.setFitWidth(250);
              imageView.setFitHeight(200);
              setGraphic(imageView);
            }
          }
        };
      }
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