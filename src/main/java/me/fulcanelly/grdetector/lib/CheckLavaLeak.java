package me.fulcanelly.grdetector.lib;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.math3.util.Pair;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import me.fulcanelly.grdetector.db.CoBlock;
import me.fulcanelly.grdetector.warn.data.SimpleLocation;

@Data
@AllArgsConstructor
class IDDdk {
    SimpleLocation location;
    Integer level;
}

public class CheckLavaLeak {

    World world;

    void checkBlock() {
        world.getBlockAt(0, 0, 0);
        CoBlock.worldNameToInt(world.getName());
        CoBlock.findBlockHistory(null, 0, 0, 0, 0);
    }

    class Result {
        boolean actuallyAfectsSomeone;
        String[] futureVictims;
    }

    static final int STARTING_LEVEL = 9 ;
    public Result check(Block block) {
        // if ()

        World world = block.getWorld();
        
        SimpleLocation startLocation = new SimpleLocation(1, 2, 2, null);

        IDDdk startIdk = new IDDdk(startLocation, STARTING_LEVEL);
        List<IDDdk> list = new ArrayList<>();

        var result = Stream.of(getNarby(startIdk.getLocation()))
            .map(it -> new IDDdk(it, startIdk.level - 1))
            .filter(it -> !getBlockBySimpleLocation(world, it.getLocation()).getType().equals(Material.AIR))
            .toList();

        while (!list.isEmpty()) {

        }
        return null;
    }

    Block getBlockBySimpleLocation(World world, SimpleLocation location) {
        return world.getBlockAt(location.getX(), location.getY(), location.getZ());
    }

    // Pair<SimpleLocation, > ok;

    SimpleLocation[] getNarby(SimpleLocation loc) {
        return new SimpleLocation[] {
                new SimpleLocation(loc.getX() - 1, loc.getY(), loc.getZ(), loc.getWorldType()),
                new SimpleLocation(loc.getX() + 1, loc.getY(), loc.getZ(), loc.getWorldType()),
                new SimpleLocation(loc.getX(), loc.getY(), loc.getZ() - 1, loc.getWorldType()),
                new SimpleLocation(loc.getX(), loc.getY(), loc.getZ() + 1, loc.getWorldType()),
        };
    }
}
