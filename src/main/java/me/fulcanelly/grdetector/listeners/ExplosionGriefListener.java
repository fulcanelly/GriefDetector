package me.fulcanelly.grdetector.listeners;

import java.util.List;
import org.bukkit.block.Block;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.With;
import me.fulcanelly.grdetector.db.CoBlock;
import me.fulcanelly.grdetector.db.CoUser;
import me.fulcanelly.grdetector.lib.ConnectionCreator;
import me.fulcanelly.grdetector.warn.WarningNotifier;
import me.fulcanelly.grdetector.warn.data.WarnEvent;

@AllArgsConstructor
@With
@NoArgsConstructor
public class ExplosionGriefListener implements Listener {

  ConnectionCreator connectionCreator;
  WarningNotifier warningNotifier;

  @EventHandler
  void onExplosion(EntityExplodeEvent event) {
    var entity = event.getEntity();
    var blocks = event.blockList();
    var wid = CoBlock.worldNameToInt(event.getLocation().getWorld().getName());

    if (entity instanceof TNTPrimed tnt) {

      handleTntExplosion(tnt, blocks, wid);
    } else if (entity instanceof Creeper creeper) {

      handleCreeperExplosion(creeper, blocks, wid);
    }
  }

  @SneakyThrows
  void handleTntExplosion(TNTPrimed tnt, List<Block> blocks, int wid) {
    var primer = obtainPrimer(tnt);
    if (primer == null) {
      return;
    }
    try (var conn = connectionCreator.create()) {
      var targetUser = CoUser.findByName(conn, primer.getName());

      var brokenBlocks = blocks.stream()
          .map(it -> CoBlock.findBlockHistory(conn, it.getX(), it.getY(), it.getZ(), wid))
          .filter(it -> it != null)
          .filter(it -> it.getUserId() != targetUser.getId())
          .distinct()
          .toList();

      var victims = brokenBlocks.stream()
          .map(CoBlock::getUserId)
          .distinct()
          .map(it -> CoUser.findById(conn, it).getName())
          .filter(it -> !it.startsWith("#"))
          .toList();

      if (victims.isEmpty()) {
        return;
      }

      if (brokenBlocks.isEmpty()) {
        return;
      }

      warningNotifier.notify(
          WarnEvent.TNT_EXPLOSION,
          primer.getName(), brokenBlocks, wid);
    }
  }

  Entity obtainPrimer(TNTPrimed tnt) {
    var source = tnt.getSource();

    while (source != null && source instanceof TNTPrimed) {
      source = tnt.getSource();
    }

    return source;
  }

  @SneakyThrows
  void handleCreeperExplosion(Creeper creeper, List<Block> blocks, int wid) {
    var user = creeper.getTarget();

    if (user == null) {
      return;
    }

    if (!(user instanceof Player player)) {
      return;
    }

    var name = player.getName();

    try (var conn = connectionCreator.create()) {
      var targetUser = CoUser.findByName(conn, name);

      var brokenBlocks = blocks.stream()
          .map(it -> CoBlock.findBlockHistory(conn, it.getX(), it.getY(), it.getZ(), wid))
          .filter(it -> it != null)
          .filter(it -> it.getUserId() != targetUser.getId())
          .distinct()
          .toList();

      var victims = brokenBlocks.stream()
          .map(CoBlock::getUserId)
          .distinct()
          .map(it -> CoUser.findById(conn, it).getName())
          .filter(it -> !it.startsWith("#"))
          .toList();

      if (victims.isEmpty()) {
        return;
      }

      if (brokenBlocks.isEmpty()) {
        return;
      }

      warningNotifier.notify(
          WarnEvent.CREEPER_EXPLOSION,
          name,
          brokenBlocks, wid);
    }
  }
}
