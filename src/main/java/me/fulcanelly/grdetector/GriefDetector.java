package me.fulcanelly.grdetector;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import org.bukkit.plugin.java.JavaPlugin;

import lombok.SneakyThrows;
import me.fulcanelly.grdetector.lib.ConnectionTester;
import me.fulcanelly.grdetector.listeners.ExplosionGriefListener;

public class GriefDetector extends JavaPlugin {

  @SneakyThrows
  Connection createConnection(File path) {
    Properties config = new Properties();
    config.setProperty("open_mode", "1"); // 1 == readonly

    String url = "jdbc:sqlite:" + path.getAbsolutePath().toString();
    return DriverManager.getConnection(url, config);
  }

  @Override
  public void onEnable() {
    var coreProtect = getServer().getPluginManager().getPlugin("CoreProtect");

    if (coreProtect == null) {
      throw new RuntimeException("Need coreprotect");
    }

    var dataFolder = coreProtect.getDataFolder();

    getLogger().info(dataFolder.getAbsolutePath());

    var folder = new File(dataFolder, "database.db");

    var connection = createConnection(folder);
    new ConnectionTester(connection).testConnection();

    getServer()
        .getPluginManager()
        .registerEvents(
            new ExplosionGriefListener(() -> createConnection(folder)),
            this);
  }

}

