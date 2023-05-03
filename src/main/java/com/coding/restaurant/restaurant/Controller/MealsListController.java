package com.coding.restaurant.restaurant.Controller;

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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.net.URL;
import java.util.ResourceBundle;

import org.kordamp.bootstrapfx.BootstrapFX;

import com.coding.restaurant.restaurant.Model.Meal;

public class MealsListController implements Initializable {

    @FXML
    private ListView<Meal> mealListView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<Meal> mealList = FXCollections.observableArrayList();
        mealList.add(new Meal("Tomate Mozzarella", "Tomates coupées en quartiers accompagnés de sa mozzarelle marinée dans sa vinaigrette balsamique", 8.40, "https://cache.marieclaire.fr/data/photo/w1000_c17/cuisine/4c/mozza.jpg", true));
        mealList.add(new Meal("Tiramisiuuu", "Tiramisu à la cannelle", 9.30, "https://assets.afcdn.com/story/20220302/2163084_w2246h2246c1cx1123cy668cxt0cyt0cxb2246cyb1335.jpg", true));
        mealList.add(new Meal("Pâtes à l'ail", "Pâtes à l'ail sous son lit de crème forestière relevé par l'ail confit", 13.40, "https://cf-images.us-east-1.prod.boltdns.net/v1/static/1033249144001/87a2f1f3-95bf-4b4c-937e-ef25b238f06a/e77b0c67-8818-4426-b96e-ffb79ebf7309/1280x720/match/image.jpg", true));

        mealListView.setItems(mealList);

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

                    Label titleLabel = new Label(selectedMeal.getName());
                    titleLabel.setStyle("-fx-font-size: 24pt; -fx-font-weight: bold;");
                    Label descriptionLabel = new Label(selectedMeal.getDescription());
                    descriptionLabel.setStyle("-fx-font-size: 14pt;");

                    VBox vbox = new VBox();
                    vbox.setPadding(new Insets(10, 10, 10, 10));
                    vbox.setSpacing(10);
                    vbox.setAlignment(Pos.CENTER);
                    vbox.getChildren().addAll(titleLabel, descriptionLabel);

                    Scene popupScene = new Scene(vbox);
                    popupStage.setScene(popupScene);
                    popupStage.show();
                }
            }
        });
    }
}

