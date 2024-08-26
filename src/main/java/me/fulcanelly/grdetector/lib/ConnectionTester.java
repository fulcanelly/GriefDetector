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
      if (rs.getInt("X") != 1) {
        throw new RuntimeException("Something wrong with db connection");
      }
    }
  }

}