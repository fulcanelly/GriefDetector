package me.fulcanelly.grdetector.warn;

import java.sql.Connection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import me.fulcanelly.grdetector.db.CoBlock;
import me.fulcanelly.grdetector.db.CoUser;
import me.fulcanelly.grdetector.warn.data.SimpleLocation;
import me.fulcanelly.grdetector.warn.data.WarnEvent;
import me.fulcanelly.grdetector.warn.data.WarnMessage;
import me.fulcanelly.grdetector.warn.data.WorldType;

@AllArgsConstructor
public class WarningNotifier {
  Supplier<Connection> connectionCreator;
  WarnDeduplicatorTgSender box;

  @SneakyThrows
  public void notify(WarnEvent event, String user, List<CoBlock> blocks, int wid) {
    Comparator<CoBlock> xCompare = (a, b) -> a.getX() - b.getX();
    Comparator<CoBlock> yCompare = (a, b) -> a.getY() - b.getY();
    Comparator<CoBlock> zCompare = (a, b) -> a.getZ() - b.getZ();

    var fromX = blocks.stream().min(xCompare).get().getX();
    var toX = blocks.stream().max(xCompare).get().getX();

    var fromY = blocks.stream().min(yCompare).get().getY();
    var toY = blocks.stream().max(yCompare).get().getY();

    var fromZ = blocks.stream().min(zCompare).get().getZ();
    var toZ = blocks.stream().max(zCompare).get().getZ();

    var conn = connectionCreator.get();
    var victims = blocks.stream()
        .map(CoBlock::getUserId)
        .distinct()
        .map(it -> CoUser.findById(conn, it))
        .filter(it -> it != null)
        .map(CoUser::getName)
        .toList();

    conn.close();

    box.addMessage(
        new WarnMessage()
            .withType(event)
            .withSuspect(user)
            .withVictims(victims)

            .withStart(new SimpleLocation(fromX, fromY, fromZ, wtypeFromId(wid)))
            .withEnd(new SimpleLocation(toX, toY, toZ, wtypeFromId(wid))));
  }

  WorldType wtypeFromId(int id) {
    if (id == 1) {
      return WorldType.REGUALAR;
    } else if (id == 2) {
      return WorldType.NETHER;
    } else if (id == 3) {
      return WorldType.THE_END;
    } else {
      return WorldType.UNKNOWN;
    }
  }
}
