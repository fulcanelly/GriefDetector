package me.fulcanelly.grdetector.listeners;

import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class JavaLeakListener implements Listener {
Server server;
    @EventHandler

    void onJavaPlace(BlockPlaceEvent event) {
        if (!event.getBlockPlaced().getType().equals(Material.LAVA)) {
            return;
        }

        // server.getWorlds().get(0).getBlockAt(0, 0, 0)
        
    }

    
}
