package me.skyblockcore;

import me.skyblockapi.Manager.EventManager;
import me.skyblockcore.comands.CommandManager;
import me.skyblockcore.comands.sbmatic.SchematicCommand;
import me.skyblockcore.files.DataManager;
import me.skyblockcore.managers.IslandManager;
import me.skyblockcore.worldgen.VoidGenerator;
import org.bukkit.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;



public final class SkyblockCore extends JavaPlugin {


    public DataManager data;


    @Override
    public void onEnable() {
        // Plugin startup logic
        this.data = new DataManager(this);
        createWorld();
        new IslandManager(this);



        registerCommands();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
            IslandManager.getIslandManager().onDisable();
    }


    public void registerCommands() {
        getCommand("sbmatic").setExecutor(new SchematicCommand(this));
        getCommand("island").setExecutor(new CommandManager());
    }


    public void createWorld() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Loading Skyblock world!");
        WorldCreator wc = new WorldCreator("Skyblock");
        wc.generator(new VoidGenerator());
        wc.createWorld();
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Skyblock world successfully loaded.");
            }
        }.runTaskLater(this, 15L);
    }

}
