package me.fulcanelly.grdetector.lib;

import java.sql.Connection;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

@AllArgsConstructor
public
class ConnectionTester {
  Connection connection;

  @SneakyThrows
  public
  void testConnection() {
    var sql = "SELECT 1 as X";
    var stmt = connection.createStatement();
    var rs = stmt.executeQuery(sql);

    while (rs.next()) {
      System.out.printf("%d\n",
          rs.getInt("X"));
    }
  }

}