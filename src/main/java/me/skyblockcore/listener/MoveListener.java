package me.skyblockcore.listener;

import me.skyblockcore.managers.IslandManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveListener implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        IslandManager manager = IslandManager.getIslandManager();
        if (!player.getWorld().getName().equals("Skyblock"))
            return;
        boolean canMove = manager.isInsideRadius(player);
        if (!canMove) {
            player.sendMessage(ChatColor.RED + "You can't continue in that direction!");
            e.setCancelled(true);
        }

    }

}
