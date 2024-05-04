package me.skyblockcore.comands.sbmatic;

import me.skyblockcore.SkyblockCore;
import me.skyblockcore.worldedit.WorldEditHook;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sun.util.resources.cldr.ext.LocaleNames_tg;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SchematicCommand implements CommandExecutor {

    private final Map<UUID, Tuple<Location, Location>> selections = new HashMap<>();

    private final SkyblockCore plugin;

    public SchematicCommand(SkyblockCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        if (!(commandSender instanceof Player))
            return true;
        Player player = (Player) commandSender;

        if (args.length == 0) {
            player.sendMessage("Usage: /sbmatic <pos1|pos2|save <name>|paste <name>>");
            return true;
        }

        String param = args[0].toLowerCase();

        Tuple<Location, Location> selection = selections.getOrDefault(player.getUniqueId(), new Tuple<>(null, null));

        if (param.equalsIgnoreCase("pos1")) {
            selection.setFirst(player.getLocation());

            player.sendMessage(ChatColor.GREEN + "First location set!");
            selections.put(player.getUniqueId(), selection);
        } else if (param.equalsIgnoreCase("pos2")) {
            selection.setSecond(player.getLocation());

            player.sendMessage(ChatColor.GREEN + "Second location set!");
            selections.put(player.getUniqueId(), selection);

        } else if (param.equalsIgnoreCase("save")) {
            if (selection.getFirst() == null || selection.getSecond() == null) {
                player.sendMessage(ChatColor.RED + "You have to set both positions first!");
                return true;
            }

            if (args.length != 2) {
                player.sendMessage(ChatColor.RED + "Usage: /sbmatic save <name>");
                return true;
            }

            File file = new File(plugin.getDataFolder(), "schematic/" + args[1] + ".schem");
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            WorldEditHook.save(selection.getFirst(), selection.getSecond(), file);
            player.sendMessage(ChatColor.GREEN + "Schematic saved!");

        } else if (param.equalsIgnoreCase("paste")) {
            if (args.length != 2) {
                player.sendMessage(ChatColor.RED + "Wrong usage: /sbmatic paste <name>");
                return true;
            }

            File file = new File(plugin.getDataFolder(), "schematic/" + args[1] + ".schem");

            if (!file.exists()) {
                player.sendMessage(ChatColor.RED + "Schematic not found!");

                return true;
            }

            WorldEditHook.paste(player.getLocation(), file);
            player.sendMessage(ChatColor.GREEN + "Schematic pasted!");
        } else
            player.sendMessage("Usage: /sbmatic <pos1|pos2|save <name>|paste <name>>");

        return true;
    }


    private static class Tuple<A,B> {
        private A first;
        private B second;

        public Tuple(A first, B second) {
            this.first = first;
            this.second = second;
        }

        public A getFirst() {
            return first;
        }

        public void setFirst(A first) {
            this.first = first;
        }

        public B getSecond() {
            return second;
        }

        public void setSecond(B second) {
            this.second = second;
        }
    }
}
