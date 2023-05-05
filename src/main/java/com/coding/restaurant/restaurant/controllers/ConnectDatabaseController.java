package com.coding.restaurant.restaurant.controllers;

import java.sql.Connection;
import java.sql.SQLException;

import com.mysql.cj.jdbc.MysqlDataSource;
import io.github.cdimascio.dotenv.Dotenv;

/**
 * This class is used to connect to the database
 */
public class ConnectDatabaseController {


  private static MysqlDataSource dataSource;

  private ConnectDatabaseController() {
  }

  // Add a method to get a connection to the database using the credentials and jdbc

  /**
   * 
   * @return
   * @throws SQLException
   */
  public static Connection getConnection() throws SQLException {
    if (dataSource == null) {
      Dotenv dotenv = Dotenv.load();
      String url = dotenv.get("DATABASE_URL");
      String user = dotenv.get("DATABASE_USER");
      String password = dotenv.get("DATABASE_PASSWORD");
      dataSource = new MysqlDataSource();
      dataSource.setURL(url);
      dataSource.setUser(user);
      dataSource.setPassword(password);
    }
    return dataSource.getConnection();
  }
}
