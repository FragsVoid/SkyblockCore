package me.skyblockcore.managers;

import me.skyblockcore.SkyblockCore;
import me.skyblockcore.worldedit.WorldEditHook;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.io.File;

public class Island {

    private final SkyblockCore plugin;

    private String owner;

    private Location loc;
    private Location spawn;

    private int moveRadius = 75;

    private int buildRadius = 50;

    public Island(String owner, Location loc, SkyblockCore plugin) {
        this.plugin = plugin;
        this.owner = owner;
        this.loc = loc;
        this.spawn = loc.clone().add(1.5, 3, 5.5);
        spawn.setYaw(-180);
    }

    public Island(String owner, Location loc, boolean gen, SkyblockCore plugin) {
        this.plugin = plugin;
        this.owner = owner;
        this.loc = loc;
        this.spawn = loc.clone().add(1.5, 3, 5.5);
        spawn.setYaw(-180);
        if (gen)
            generateIsland();
    }

    public void generateIsland() {
        File file = new File(plugin.getDataFolder(), "schematic/test.schem");

        WorldEditHook.paste(loc, file);
    }





    public boolean isInsideRadius(Block block) {
        if (block == null) {
            return false;
        }
        Location blockLocation = block.getLocation();
        int x = Math.abs(blockLocation.getBlockX() - this.loc.getBlockX());
        int z = Math.abs(blockLocation.getBlockZ() - this.loc.getBlockZ());

        return x < buildRadius && z < buildRadius;
    }

    public String getOwner() {
        return owner;
    }

    public Location getLoc() {
        return loc;
    }

    public Location getSpawn() {
        return spawn;
    }

    public int getBuildRadius() {
        return buildRadius;
    }

    public int getMoveRadius() {
        return moveRadius;
    }
}
