package me.skyblockcore.listener;

import me.skyblockcore.managers.IslandManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        IslandManager manager = IslandManager.getIslandManager();

        if (!player.getWorld().getName().equals("Skyblock"))
            return;

        if (!manager.hasIsland(player)) {
            System.out.println(manager);
        }
    }
}
