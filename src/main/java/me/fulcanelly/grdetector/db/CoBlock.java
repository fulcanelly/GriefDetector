package me.fulcanelly.grdetector.db;

import java.sql.Connection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.ToString;

enum Action {
  PLACE, // 1
  BREAK, // 2 (?)
}

@AllArgsConstructor
@Data
@ToString
public class CoBlock {

  int x, y, z, wid, userId, actionId, time, type;

  // TODO we interested only in 1
  Action getActyion() {
    if (actionId == 1) {
      return Action.PLACE;
    }
    return null;//
  }

  String getWorld() {
    if (wid == 1) {
      return "world";
    } else if (wid == 2) {
      return "world_nether";
    } else if (wid == 3) {
      return "world_the_end";
    } else {
      throw new RuntimeException("Unknown world");
    }
  }

  static public int worldNameToInt(String worldName) {
    switch (worldName) {
      case "world":
        return 1;
      case "world_nether":
        return 2;
      case "world_the_end":
        return 3;
      default:
        throw new RuntimeException("Unknown world name");
    }
  }

  @SneakyThrows
  static public CoBlock findBlockHistory(Connection connection, int x, int y, int z, int wid) {
    var sql = "SELECT * FROM co_block WHERE x = ? AND y = ? AND z = ? AND wid = ? AND rolled_back = 0 AND action = 1 ORDER BY time DESC LIMIT 1";

    var pstmt = connection.prepareStatement(sql);

    pstmt.setInt(1, x);
    pstmt.setInt(2, y);
    pstmt.setInt(3, z);
    pstmt.setInt(4, wid);

    var result = pstmt.executeQuery();

    while (result.next()) {
      return new CoBlock(
          result.getInt("x"),
          result.getInt("y"),
          result.getInt("z"),

          result.getInt("wid"),
          result.getInt("user"),
          result.getInt("action"),

          result.getInt("time"),
          result.getInt("type"));
    }

    return null;
  }
}
