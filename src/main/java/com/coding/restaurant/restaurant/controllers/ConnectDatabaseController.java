package com.coding.restaurant.restaurant.controllers;

import java.sql.Connection;
import java.sql.SQLException;

import com.mysql.cj.jdbc.MysqlDataSource;
import io.github.cdimascio.dotenv.Dotenv;

public class ConnectDatabaseController {


  private static MysqlDataSource dataSource;

  private ConnectDatabaseController() {
  }

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
