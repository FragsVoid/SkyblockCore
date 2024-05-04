package me.skyblockcore.listener;

import me.skyblockcore.managers.IslandManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class LoadUnloadListener implements Listener {


    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        IslandManager.getIslandManager().loadPlayer(e.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        IslandManager.getIslandManager().unloadPlayer(e.getPlayer());
    }
}
