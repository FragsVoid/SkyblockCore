package me.skyblockcore.managers;

import me.skyblockcore.SkyblockCore;
import me.skyblockcore.comands.sbmatic.SchematicCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class IslandManager {

    private final SkyblockCore plugin;

    private static IslandManager islandManager;

    private Map<UUID, Island> islands = new HashMap<>();

    private int totalIslands;



    public IslandManager(SkyblockCore plugin) {
        islandManager = this;
        this.plugin = plugin;
        loadStats();
    }

    public Location lastLocation() {
        FileConfiguration data = plugin.data.getConfig();
        if (data.getLocation("LastLocation") == null) {
            return new Location(Bukkit.getWorld("Skyblock"), 0, 72, 0);
        }
        return data.getLocation("LastLocation");
    }

    public Location nextLocation() {
        int maxRows = 1000;

        double xIndex = (totalIslands%maxRows)*1000;
        double zIndex = Math.floor(totalIslands/maxRows)*1000;

        return new Location(Bukkit.getWorld("Skyblock"), xIndex, 72, zIndex);
    }

    public void onDisable() {
        plugin.data.getConfig().set("Total-Islands", totalIslands);
        plugin.data.saveConfig();
    }

    private void loadStats() {
        totalIslands = plugin.data.getConfig().getInt("Total-Islands");
    }

    public void createIsland(Player player)  {
        totalIslands++;
        Location newLocation = nextLocation();
        FileConfiguration data = plugin.data.getConfig();
        Location playerLoc = newLocation.clone().add(1.5, 3, 5.5);
        playerLoc.setYaw(-160);
        player.sendMessage(ChatColor.GREEN + "Creating island...");
        new BukkitRunnable() {

            @Override
            public void run() {
                player.teleport(playerLoc);
                player.sendMessage(ChatColor.GREEN + "Island created!");
            }
        }.runTaskLater(plugin, 40L);

        data.set("LastLocation", newLocation);

        Island island = new Island(player.getName(), newLocation, true, plugin);
        islands.put(player.getUniqueId(), island);

        data.set("islands." + player.getUniqueId() + ".owner", player.getName());
        //center of the island
        data.set("islands." + player.getUniqueId() + ".x", newLocation.getX());
        data.set("islands." + player.getUniqueId() + ".z", newLocation.getZ());

        data.set("islands." + player.getUniqueId() + ".radius", 50);

        plugin.data.saveConfig();
    }

    public void deleteIsland(Player player) {
        FileConfiguration data = plugin.data.getConfig();
        if (player.getWorld().getName().equals("Skyblock")) {
            player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
        }
        data.set("islands." + player.getUniqueId(), null);
        plugin.data.saveConfig();
    }

    public void loadPlayer(Player player) {
        if (plugin.data.getConfig().contains(player.getUniqueId().toString())){
            ConfigurationSection s = plugin.data.getConfig().getConfigurationSection("islands");
            ConfigurationSection section = s.getConfigurationSection(player.getUniqueId().toString());
            double x = section.getDouble("x");
            double z = section.getDouble("z");
            Island island = new Island(player.getName(), new Location(Bukkit.getWorld("Skyblock"), x, 72D, z), plugin);
            islands.put(player.getUniqueId(), island);

        }
    }

    public void unloadPlayer(Player player) {
        if (hasIsland(player)) {
            islands.remove(player.getUniqueId());
        }
    }

    public boolean isInsideRadius(Player player) {
        if (player == null)
            return false;
        Location loc = player.getLocation();
        int moveRadius = plugin.data.getConfig().getInt("islands." + player.getUniqueId() + ".radius");
        ConfigurationSection section = plugin.data.getConfig().getConfigurationSection("islands");
        int size = section.getKeys(false).size();
        int counter = 0;
        for (String keys : section.getKeys(false)) {

            ConfigurationSection locations = section.getConfigurationSection(keys);
            int xIndex = locations.getInt("x");
            int zIndex = locations.getInt("z");
            int x = Math.abs(loc.getBlockX() - xIndex);
            int z = Math.abs(loc.getBlockZ() - zIndex);
            if (x < moveRadius && z < moveRadius) {
                break;
            }
            if (++counter == size) {
                return false;
            }
        }
        return true;
    }

    public Island getIsland(Player player) {
        if (hasIsland(player)) {
            return islands.get(player.getUniqueId());
        }
        return null;
    }

    public boolean hasIsland(Player player) {
        return islands.containsKey(player.getUniqueId());
    }

    public static IslandManager getIslandManager() {
        return islandManager;
    }

}
