package com.coding.restaurant.restaurant.Controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import io.github.cdimascio.dotenv.Dotenv;

public class ConnectDatabaseController {

    private static Connection connexion;

    public static Connection getConnexion() {
        if (connexion == null) {
            Dotenv dotenv = Dotenv.load();
            String url = dotenv.get("DATABASE_URL");
            String user = dotenv.get("DATABASE_USER");
            String password = dotenv.get("DATABASE_PASSWORD");
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connexion = DriverManager.getConnection(url, user, password);
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
        return connexion;
    }
}
