package me.fulcanelly.grdetector;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import java.util.function.Supplier;

import org.bukkit.plugin.java.JavaPlugin;

import lombok.SneakyThrows;
import me.fulcanelly.grdetector.lib.ConnectionTester;
import me.fulcanelly.grdetector.listeners.ExplosionGriefListener;
import me.fulcanelly.grdetector.warn.WarnDeduplicatorTgSender;
import me.fulcanelly.grdetector.warn.WarningNotifier;
import me.fulcanelly.tgbridge.Bridge;

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
    var pluginManager = getServer().getPluginManager();

    var coreProtect = pluginManager.getPlugin("CoreProtect");

    if (coreProtect == null) {
      throw new RuntimeException("Need coreprotect");
    }

    Bridge tgBridge = (Bridge) pluginManager.getPlugin("tg-bridge");

    var dataFolder = coreProtect.getDataFolder();

    getLogger().info(dataFolder.getAbsolutePath());

    var folder = new File(dataFolder, "database.db");

    var connection = createConnection(folder);
    new ConnectionTester(connection).testConnection();

    Supplier<Connection> connectionCreator = () -> createConnection(folder);

    
    var sender = new WarnDeduplicatorTgSender(tgBridge.getBot(), Long.valueOf(tgBridge.getMainConfig().getChatId()));
    sender.start();
    var warningNotifier = new WarningNotifier(connectionCreator, sender);

    getServer()
        .getPluginManager()
        .registerEvents(
            new ExplosionGriefListener(connectionCreator, warningNotifier),
            this);
  }

}
