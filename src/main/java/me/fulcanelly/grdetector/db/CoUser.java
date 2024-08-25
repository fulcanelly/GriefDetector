package me.fulcanelly.grdetector.db;

import java.sql.Connection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;

@AllArgsConstructor @Data
public class CoUser {

  long id;
  long time;
  String name;
  String uuid;

  @SneakyThrows
  static public CoUser findById(Connection connection, int id) {
    var sql = "SELECT * FROM co_user WHERE id = ?";
    var pstmt = connection.prepareStatement(sql);
    pstmt.setInt(1, id);

    var result = pstmt.executeQuery();

    while (result.next()) {
      return new CoUser(
          result.getLong("id"),
          result.getLong("time"),
          result.getString("user"),
          result.getString("uuid"));
    }

    return null;
  }

  @SneakyThrows
  static public CoUser findByName(Connection connection, String name) {

    var sql = "SELECT * FROM co_user WHERE user = ?";
    var pstmt = connection.prepareStatement(sql);
    pstmt.setString(1, name);

    var result = pstmt.executeQuery();

    while (result.next()) {
      return new CoUser(
          result.getLong("id"),
          result.getLong("time"),
          result.getString("user"),
          result.getString("uuid"));
    }

    return null;
  }

}
