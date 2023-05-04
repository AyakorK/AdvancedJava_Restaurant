module com.coding.restaurant.restaurant {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires java.naming;
    requires mysql.connector.java;
    requires dotenv.java;
    //requires eu.hansolo.tilesfx;

    opens com.coding.restaurant.restaurant to javafx.fxml;
    exports com.coding.restaurant.restaurant;
    exports com.coding.restaurant.restaurant.controllers;
    exports com.coding.restaurant.restaurant.models;
    opens com.coding.restaurant.restaurant.models to javafx.base;
    opens com.coding.restaurant.restaurant.controllers to javafx.fxml;
}