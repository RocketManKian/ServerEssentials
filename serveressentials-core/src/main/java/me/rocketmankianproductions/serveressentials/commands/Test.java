package me.rocketmankianproductions.serveressentials.commands;

import me.clip.placeholderapi.PlaceholderAPI;
import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static me.rocketmankianproductions.serveressentials.ServerEssentials.hex;

public class Test implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (ServerEssentials.permissionChecker(player, "se.test")) {
                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("join")) {
                        if (!(Lang.fileConfig.getString("join-symbol").length() == 0)) {
                            String jm = Lang.fileConfig.getString("join-symbol");
                            player.sendMessage(hex(jm + " " + player.getDisplayName()));
                            return true;
                        } else {
                            player.sendMessage(ChatColor.RED + "The 'join-symbol' lang value is empty.");
                            return true;
                        }
                    } else if (args[0].equalsIgnoreCase("leave")) {
                        if (!(Lang.fileConfig.getString("leave-symbol").length() == 0)) {
                            String lm = Lang.fileConfig.getString("leave-symbol");
                            player.sendMessage(hex(lm + " " + player.getDisplayName()));
                            return true;
                        } else {
                            player.sendMessage(ChatColor.RED + "The 'leave-symbol' lang value is empty.");
                            return true;
                        }
                    } else if (args[0].equalsIgnoreCase("welcome")) {
                        if (!(Lang.fileConfig.getString("first-time-join").length() == 0)) {
                            String wm = Lang.fileConfig.getString("first-time-join");
                            if (ServerEssentials.isConnectedToPlaceholderAPI) {
                                String placeholder = PlaceholderAPI.setPlaceholders(player, wm);
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(placeholder)));
                            } else {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(wm)));
                            }
                            return true;
                        } else {
                            player.sendMessage(ChatColor.RED + "The 'first-time-join' lang value is empty.");
                            return true;
                        }
                    } else if (args[0].equalsIgnoreCase("motd")) {
                        if (!(ServerEssentials.getPlugin().getConfig().getString("motd-message").length() == 0)) {
                            if (!ServerEssentials.isConnectedToPlaceholderAPI) {
                                for (String message : ServerEssentials.plugin.getConfig().getStringList("motd-message")) {
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(message)));
                                }
                            } else {
                                for (String message : ServerEssentials.plugin.getConfig().getStringList("motd-message")) {
                                    String placeholder = PlaceholderAPI.setPlaceholders(player, message);
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(placeholder)));
                                }
                            }
                            return true;
                        } else {
                            player.sendMessage(ChatColor.RED + "The 'motd-message' config value is empty.");
                            return true;
                        }
                    } else if (args[0].equalsIgnoreCase("permission")) {
                        if (!(Lang.fileConfig.getString("no-permission-message").length() == 0)) {
                            String wm = Lang.fileConfig.getString("no-permission-message");
                            if (ServerEssentials.isConnectedToPlaceholderAPI) {
                                String placeholder = PlaceholderAPI.setPlaceholders(player, wm);
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(placeholder)));
                            } else {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(wm)));
                            }
                            return true;
                        } else {
                            player.sendMessage(ChatColor.RED + "The 'no-permission-message' lang value is empty.");
                            return true;
                        }
                    }
                } else {
                    String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/test (motd/welcome/join/leave)");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                    return true;
                }
            }
        } else {
            String console = Lang.fileConfig.getString("console-invalid");
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', hex(console)));
            return true;
        }
        return false;
    }
}
