package me.skyblockcore.comands;

import me.skyblockcore.managers.Island;
import me.skyblockcore.managers.IslandManager;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class CommandManager implements CommandExecutor {

    private ArrayList<SubCommand> subCommands = new ArrayList<>();

    public CommandManager() {

    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            IslandManager manager = IslandManager.getIslandManager();
            if (args.length == 0) {
                if (!manager.hasIsland(player)) {
                    manager.createIsland(player);
                    return true;
                } else {
                    Island island = manager.getIsland(player);
                    Location spawn = island.getSpawn();
                    player.teleport(spawn);
                    return true;
                }
            }

        }
        return true;
    }
}
