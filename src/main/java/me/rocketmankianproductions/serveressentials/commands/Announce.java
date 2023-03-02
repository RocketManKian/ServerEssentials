package me.rocketmankianproductions.serveressentials.commands;

import me.clip.placeholderapi.PlaceholderAPI;
import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static me.rocketmankianproductions.serveressentials.ServerEssentials.hex;
import static me.rocketmankianproductions.serveressentials.ServerEssentials.prefix;

public class Announce implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (ServerEssentials.permissionChecker(player, "se.announce")) {
                if (args.length > 0) {
                    String announce = "";
                    if (ServerEssentials.isConnectedToPlaceholderAPI) {
                        for (String message : args) {
                            announce = (announce + message + " ");
                        }
                        @NotNull String placeholder = PlaceholderAPI.setPlaceholders(player, announce);
                        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', prefix + ChatColor.WHITE + " " + hex(placeholder)));
                    } else {
                        for (String message : args) {
                            announce = (announce + message + " ");
                        }
                        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', prefix + ChatColor.WHITE + " " + hex(announce)));
                    }
                    return true;
                } else {
                    String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/announce <message>");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                    return true;
                }
            }
        } else if (sender instanceof ConsoleCommandSender) {
            Player target = Bukkit.getPlayer(args[0]);
            if (args.length > 0) {
                String announce = "";
                if (ServerEssentials.isConnectedToPlaceholderAPI) {
                    for (String message : args) {
                        announce = (announce + message + " ");
                    }
                    @NotNull String placeholder = PlaceholderAPI.setPlaceholders(target, announce);
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', prefix + ChatColor.WHITE + " " + hex(placeholder)));
                    return true;
                } else {
                    for (String message : args) {
                        announce = (announce + message + " ");
                    }
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', prefix + ChatColor.WHITE + " " + hex(announce)));
                    return true;
                }
            } else {
                Bukkit.getLogger().info("Incorrect Format! Usage: /announce <Message>");
                return true;
            }
        }
        return false;
    }
}