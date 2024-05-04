package me.skyblockcore.listener;

import me.skyblockcore.managers.Island;
import me.skyblockcore.managers.IslandManager;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BreakListener implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        Block block = e.getBlock();
        IslandManager manager = IslandManager.getIslandManager();
        if (!player.getWorld().getName().equals("Skyblock"))
            return;
        if (block.getLocation().getBlockY() >= 200) {
            e.setCancelled(true);
        }
        if (!manager.hasIsland(player)) {
            e.setCancelled(true);
        } else {
            Island island = manager.getIsland(player);
            boolean canPlace = island.isInsideRadius(block);
            if (!canPlace) {
                e.setCancelled(true);
            }
        }
    }
}
