package me.rocketmankianproductions.serveressentials.commands;

import me.clip.placeholderapi.PlaceholderAPI;
import me.rocketmankianproductions.serveressentials.ServerEssentials;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Test implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;

        if (sender instanceof Player){
            if (player.hasPermission("se.test")){
                if (args.length == 1){
                    if (args[0].equalsIgnoreCase("permission")){
                        if (!(ServerEssentials.getPlugin().getConfig().getString("no-permission-message").length() == 0)){
                            String permission = ServerEssentials.getPlugin().getConfig().getString("no-permission-message");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', permission));
                            return true;
                        }else{
                            player.sendMessage(ChatColor.RED + "The 'no-permission-message' config value is empty.");
                            return true;
                        }
                    }else if (args[0].equalsIgnoreCase("join")){
                        if (!(ServerEssentials.getPlugin().getConfig().getString("join-symbol").length() == 0)){
                            String jm = ServerEssentials.getPlugin().getConfig().getString("join-symbol");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', jm + " " + player.getDisplayName()));
                            return true;
                        }else{
                            player.sendMessage(ChatColor.RED + "The 'join-symbol' config value is empty.");
                            return true;
                        }
                    }else if (args[0].equalsIgnoreCase("leave")){
                        if (!(ServerEssentials.getPlugin().getConfig().getString("leave-symbol").length() == 0)){
                            String lm = ServerEssentials.getPlugin().getConfig().getString("leave-symbol");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', lm + " " + player.getDisplayName()));
                            return true;
                        }else{
                            player.sendMessage(ChatColor.RED + "The 'leave-symbol' config value is empty.");
                            return true;
                        }
                    }else if (args[0].equalsIgnoreCase("welcome")){
                        if (!(ServerEssentials.getPlugin().getConfig().getString("first-time-join").length() == 0)){
                            String wm = ServerEssentials.getPlugin().getConfig().getString("first-time-join");
                            String placeholder = PlaceholderAPI.setPlaceholders(player, wm);
                            if (ServerEssentials.isConnectedToPlaceholderAPI) {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', placeholder));
                            } else {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', wm));
                            }
                            return true;
                        }else{
                            player.sendMessage(ChatColor.RED + "The 'first-time-join' config value is empty.");
                            return true;
                        }
                    }else if (args[0].equalsIgnoreCase("motd")){
                        if (!(ServerEssentials.getPlugin().getConfig().getString("motd-message").length() == 0)){
                            if (!ServerEssentials.isConnectedToPlaceholderAPI){
                                for (String message: ServerEssentials.plugin.getConfig().getStringList("motd-message")){
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                                }
                            }else{
                                for (String message: ServerEssentials.plugin.getConfig().getStringList("motd-message")){
                                    String placeholder = PlaceholderAPI.setPlaceholders(player, message);
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', placeholder));
                                }
                            }
                            return true;
                        }else{
                            player.sendMessage(ChatColor.RED + "The 'motd-message' config value is empty.");
                            return true;
                        }
                    }else{
                        return false;
                    }
                }
            }else{
                if (ServerEssentials.plugin.getConfig().getString("no-permission-message").length() == 0){
                    player.sendMessage(ChatColor.RED + "You do not have the required permission (se.test) to run this command.");
                    return true;
                }else{
                    String permission = ServerEssentials.getPlugin().getConfig().getString("no-permission-message");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', permission));
                }
            }
        }else{
            return false;
        }
        return false;
    }
}