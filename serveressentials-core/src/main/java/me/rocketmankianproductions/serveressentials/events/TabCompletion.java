package me.rocketmankianproductions.serveressentials.events;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.commands.Sethome;
import me.rocketmankianproductions.serveressentials.commands.Setspawn;
import me.rocketmankianproductions.serveressentials.commands.Setwarp;
import me.rocketmankianproductions.serveressentials.utils.JailUtil;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TabCompletion implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> autoCompletes = new ArrayList<>();

        // SE Commands
        if (command.getName().equalsIgnoreCase("se")) {
            if (args.length == 1) {
                Player player = (Player) sender;
                if (player.hasPermission("se.reload")) autoCompletes.add("reload");
                if (player.hasPermission("se.version")) autoCompletes.add("version");
                if (player.hasPermission("se.silentjoin")) autoCompletes.add("silentjoin");
            }
        }

        // Sudo Command
        if (command.getName().equalsIgnoreCase("sudo")) {
            if (args.length == 2) autoCompletes.add("<command>");
        }

        if (command.getName().equalsIgnoreCase("nickname")){
            if (args.length >= 1){
                autoCompletes.add("reset");
            }
        }

        // Jail Command
        if (command.getName().equalsIgnoreCase("jail")) {
            if (args.length >= 4){
                autoCompletes.add("<reason>");
            }else if (args.length == 3){
                autoCompletes.addAll(

                        ServerEssentials.getInstance.jailManager
                                .getAllJails()

                                .stream()

                                .sorted(Comparator.comparing(
                                        JailUtil::getName,
                                        String.CASE_INSENSITIVE_ORDER
                                ))

                                .map(JailUtil::getName)

                                .toList()
                );
            }else if (args.length == 2){
                autoCompletes.add("<duration>");
            }
        }

        // Gamemode Command
        if (command.getName().equalsIgnoreCase("gamemode")) {
            if (args.length == 1) {
                autoCompletes.add("survival");
                autoCompletes.add("creative");
                autoCompletes.add("adventure");
                autoCompletes.add("spectator");
            }
        }

        // Test Commands
        if (command.getName().equalsIgnoreCase("test")) {
            if (args.length == 1) {
                Player player = (Player) sender;
                if (player.hasPermission("se.test")) {
                    autoCompletes.add("join");
                    autoCompletes.add("leave");
                    autoCompletes.add("welcome");
                    autoCompletes.add("permission");
                    autoCompletes.add("motd");
                }
            }
        }

        // Speed Command
        if (command.getName().equalsIgnoreCase("speed")) {
            if (args.length == 1) {
                autoCompletes.add("walk");
                autoCompletes.add("fly");
                autoCompletes.add("reset");
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("reset")) {
                    autoCompletes.add("fly");
                    autoCompletes.add("walk");
                } else {
                    autoCompletes.add("<1-10>");
                    autoCompletes.add("<player>");
                }
            }
        }

        // World Command
        if (command.getName().equalsIgnoreCase("world")) {
            if (args.length == 1) {
                for (World world : Bukkit.getWorlds()) {
                    autoCompletes.add(world.getName());
                }
            }
        }

        // Spawn Command
        if (command.getName().equalsIgnoreCase("spawn")) {
            if (args.length == 2 && !args[0].equalsIgnoreCase("newbies")) {
                if (Setspawn.fileConfig.getString("Newbies") != null) {
                    autoCompletes.add("newbies");
                }
            }
        }

        // Time Command
        if (command.getName().equalsIgnoreCase("time")) {
            if (args.length == 1) {
                autoCompletes.add("set");
            } else if (args.length == 2) {
                autoCompletes.add("sunrise");
                autoCompletes.add("day");
                autoCompletes.add("sunset");
                autoCompletes.add("night");
                autoCompletes.add("midnight");
            }
        }

        // Weather Command
        if (command.getName().equalsIgnoreCase("weather")) {
            if (args.length == 1) {
                autoCompletes.add("sun");
                autoCompletes.add("storm");
            }
        }

        // Setspawn Command
        if (command.getName().equalsIgnoreCase("setspawn")) {
            if (args.length == 1) autoCompletes.add("newbies");
        }

        // Warp Command
        if (command.getName().equalsIgnoreCase("warp")) {
            Player player = (Player) sender;
            if (args.length == 1) {
                if (Setwarp.fileConfig.getConfigurationSection("Warp.") != null) {
                    ConfigurationSection warps = Setwarp.fileConfig.getConfigurationSection("Warp.");
                    for (String warp : warps.getKeys(false)) {
                        if (player.hasPermission("se.warps." + warp) || player.hasPermission("se.warps.all")) {
                            autoCompletes.add(warp);
                        }
                        if (player.hasPermission("se.setwarp.block")) {
                            autoCompletes.add("setblock");
                        }
                    }
                }
            }
        }
        // Delwarp Command
        if (command.getName().equalsIgnoreCase("deletewarp")) {
            Player player = (Player) sender;
            if (args.length == 1) {
                if (Setwarp.fileConfig.getConfigurationSection("Warp.") != null) {
                    ConfigurationSection warps = Setwarp.fileConfig.getConfigurationSection("Warp.");
                    for (String warp : warps.getKeys(false)) {
                        autoCompletes.add(warp);
                    }
                }
            }
        }

        // Home Command
        if (command.getName().equalsIgnoreCase("home")) {
            if (args.length == 1) {
                Player player = (Player) sender;
                if (Sethome.fileConfig.getConfigurationSection("Home." + player.getUniqueId()) != null) {
                    ConfigurationSection home = Sethome.fileConfig.getConfigurationSection("Home." + player.getUniqueId());
                    for (String playerhomes : home.getKeys(false)) {
                        autoCompletes.add(playerhomes);
                    }
                }
            }
        }

        // Delete Home Command
        if (command.getName().equalsIgnoreCase("deletehome")) {
            if (args.length == 1) {
                Player player = (Player) sender;
                if (Sethome.fileConfig.getConfigurationSection("Home." + player.getUniqueId()) != null) {
                    ConfigurationSection home = Sethome.fileConfig.getConfigurationSection("Home." + player.getUniqueId());
                    for (String playerhomes : home.getKeys(false)) {
                        autoCompletes.add(playerhomes);
                    }
                }
            }
        }

        // Delete Spawn Command
        if (command.getName().equalsIgnoreCase("deletespawn")) {
            if (args.length == 1) autoCompletes.add("newbies");
        }

        // Sendwarp Command
        if (command.getName().equalsIgnoreCase("sendwarp")) {
            if (args.length == 1) autoCompletes.add("<player>");
            if (args.length == 2) autoCompletes.add("<warp>");
        }

        // Sendhome Command
        if (command.getName().equalsIgnoreCase("sendhome")) {
            if (args.length == 1) autoCompletes.add("<player>");
            if (args.length == 2) autoCompletes.add("<home>");
        }

        // Message Command
        if (command.getName().equalsIgnoreCase("message")) {
            if (args.length == 1) autoCompletes.add("<player>");
        }

        // Reply Command
        if (command.getName().equalsIgnoreCase("reply")) {
            if (args.length == 1) autoCompletes.add("<message>");
        }

        // Report Bug Command
        if (command.getName().equalsIgnoreCase("reportbug")) {
            if (args.length == 1) autoCompletes.add("<bug>");
        }

        // Eco Command
        if (command.getName().equalsIgnoreCase("eco")) {
            if (args.length == 1) {
                autoCompletes.add("give");
                autoCompletes.add("take");
                autoCompletes.add("set");
                autoCompletes.add("reset");
            }
            if (args.length == 3) autoCompletes.add("<amount>");
        }

        // Delwarp Command
        if (command.getName().equalsIgnoreCase("delwarp")) {
            Player player = (Player) sender;
            if (args.length == 1) {
                if (Setwarp.fileConfig.getConfigurationSection("Warp.") != null) {
                    ConfigurationSection warps = Setwarp.fileConfig.getConfigurationSection("Warp.");
                    for (String warp : warps.getKeys(false)) {
                        if (player.hasPermission("se.warps." + warp) || player.hasPermission("se.warps.all")) {
                            autoCompletes.add(warp);
                        }
                    }
                }
            }
        }

        // Check autoCompletes list and dynamically filter based on input
        if (!autoCompletes.isEmpty()) {
            String input = args[args.length - 1].toLowerCase();
            return autoCompletes.stream()
                    .filter(option -> option.toLowerCase().startsWith(input))
                    .collect(Collectors.toList());
        }

        return null;
    }
}