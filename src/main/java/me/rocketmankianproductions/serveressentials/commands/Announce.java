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

public class Announce implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("se.announce") || player.hasPermission("se.all")) {
                if (args.length > 0) {
                    String prefix = ServerEssentials.getPlugin().getConfig().getString("prefix");
                    String announce = "";
                    if (ServerEssentials.isConnectedToPlaceholderAPI) {
                        for (String message : args) {
                            announce = (announce + message + " ");
                        }
                        @NotNull String placeholder = PlaceholderAPI.setPlaceholders(player, announce);
                        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', prefix + ChatColor.WHITE + " " + placeholder));
                    } else {
                        for (String message : args) {
                            announce = (announce + message + " ");
                        }
                        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', prefix + ChatColor.WHITE + " " + announce));
                    }
                    return true;
                } else {
                    sender.sendMessage("Incorrect Format! Usage: /announce <Message>");
                    return true;
                }
            } else {
                String perm = Lang.fileConfig.getString("no-permission-message").replace("<permission>", "se.announce");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', perm));
                return true;
            }
        } else if (sender instanceof ConsoleCommandSender) {
            Player target = Bukkit.getPlayer(args[0]);
            if (args.length > 0) {
                String prefix = ServerEssentials.getPlugin().getConfig().getString("prefix");
                String announce = "";
                if (ServerEssentials.isConnectedToPlaceholderAPI) {
                    for (String message : args) {
                        announce = (announce + message + " ");
                    }
                    @NotNull String placeholder = PlaceholderAPI.setPlaceholders(target, announce);
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', prefix + ChatColor.WHITE + " " + placeholder));
                    return true;
                } else {
                    for (String message : args) {
                        announce = (announce + message + " ");
                    }
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', prefix + ChatColor.WHITE + " " + announce));
                    return true;
                }
            } else {
                System.out.println("Incorrect Format! Usage: /announce <Message>");
                return true;
            }
        }
        return false;
    }
}
