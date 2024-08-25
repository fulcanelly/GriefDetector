package me.fulcanelly.grdetector.warn;

import java.util.Comparator;
import java.util.List;

import org.bukkit.Bukkit;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import me.fulcanelly.grdetector.db.CoBlock;
import me.fulcanelly.grdetector.db.CoUser;
import me.fulcanelly.grdetector.lib.ConnectionCreator;

@AllArgsConstructor
public class WarningNotifier {
  ConnectionCreator connectionCreator;

  static public WarningNotifier getInstance(ConnectionCreator cc) {
    return new WarningNotifier(cc);
  }

  @SneakyThrows
  public void notify(WarnEvent event, String user, List<CoBlock> blocks) {
    Comparator<CoBlock> xCompare = (a, b) -> a.getX() - b.getX();
    Comparator<CoBlock> yCompare = (a, b) -> a.getY() - b.getY();
    Comparator<CoBlock> zCompare = (a, b) -> a.getZ() - b.getZ();

    var fromX = blocks.stream().min(xCompare).get().getX();
    var toX = blocks.stream().max(xCompare).get().getX();

    var fromY = blocks.stream().min(yCompare).get().getY();
    var toY = blocks.stream().max(yCompare).get().getY();

    var fromZ = blocks.stream().min(zCompare).get().getZ();
    var toZ = blocks.stream().max(zCompare).get().getZ();

    var conn = connectionCreator.create();
    var victims = blocks.stream()
        .map(CoBlock::getUserId)
        .distinct()
        .map(it -> CoUser.findById(conn, it))
        .filter(it -> it != null)
        .map(CoUser::getName)
        .toList();

    conn.close();

    Bukkit.broadcastMessage(
        String.format(
            "x: %d..%d y: %d..%d z: %d..%d victims: %s suspect: %s\n",
            fromX, toX,
            fromY, toY,
            fromZ, toZ,
            victims.toString(),
            user));
  }
}
