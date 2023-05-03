package com.coding.restaurant.restaurant.Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MainController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
//        welcomeText.setText("Welcome to JavaFX Application!");
        listerUtilisateurs();
    }
    public void listerUtilisateurs() {
        try (Connection connexion = ConnectDatabaseController.getConnexion();
             PreparedStatement statement = connexion.prepareStatement("SELECT * FROM Bill");
             ResultSet resultat = statement.executeQuery()) {
            while (resultat.next()) {
                System.out.println(resultat.getString("amount"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}