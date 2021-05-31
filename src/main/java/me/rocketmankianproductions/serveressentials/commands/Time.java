package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.LoggerMessage;
import me.rocketmankianproductions.serveressentials.ServerEssentials;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Time implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            if (args.length == 0){
                if (player.hasPermission("se.time")){
                    if (command.getName().equalsIgnoreCase("dawn") || command.getName().equalsIgnoreCase("sunrise")) {
                        for (World world : Bukkit.getServer().getWorlds()) {
                            world.setTime(23041);
                        }
                        player.sendMessage(ChatColor.GREEN + "Time set to Sunrise");
                        return true;
                    }else if (command.getName().equalsIgnoreCase("day")) {
                        for (World world : Bukkit.getServer().getWorlds()) {
                            world.setTime(1000);
                        }
                        player.sendMessage(ChatColor.GREEN + "Time set to Day");
                        return true;
                    }else if (command.getName().equalsIgnoreCase("sunset") || command.getName().equalsIgnoreCase("dusk")) {
                        for (World world : Bukkit.getServer().getWorlds()) {
                            world.setTime(12610);
                        }
                        player.sendMessage(ChatColor.GREEN + "Time set to Sunset");
                        return true;
                    }else if (command.getName().equalsIgnoreCase("midnight")) {
                        for (World world : Bukkit.getServer().getWorlds()) {
                            world.setTime(18000);
                        }
                        player.sendMessage(ChatColor.GREEN + "Time set to Midnight");
                        return true;
                    }else if (command.getName().equalsIgnoreCase("sun")) {
                        for (World world : Bukkit.getServer().getWorlds()) {
                            world.setStorm(false);
                        }
                        player.sendMessage(ChatColor.GREEN + "Weather changed to " + ChatColor.GOLD + "Sun");
                        return true;
                    }else if (command.getName().equalsIgnoreCase("storm") || command.getName().equalsIgnoreCase("thunder")) {
                        for (World world : Bukkit.getServer().getWorlds()) {
                            world.setStorm(true);
                        }
                        player.sendMessage(ChatColor.GREEN + "Weather changed to " + ChatColor.GOLD + "Storm");
                        return true;
                    }
                }else{
                    if (ServerEssentials.plugin.getConfig().getString("no-permission-message").length() == 0){
                        player.sendMessage(ChatColor.RED + "You do not have the required permission (se.time) to run this command.");
                        return true;
                    }else{
                        String permission = ServerEssentials.getPlugin().getConfig().getString("no-permission-message");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', permission));
                    }
                    return true;
                }
            }
        }else if (sender instanceof ConsoleCommandSender) {
            if (command.getName().equalsIgnoreCase("dawn") || command.getName().equalsIgnoreCase("sunrise")) {
                for (World world : Bukkit.getServer().getWorlds()) {
                    world.setTime(23041);
                }
                System.out.println(ChatColor.GREEN + "Time set to Sunrise");
                return true;
            } else if (command.getName().equalsIgnoreCase("day")) {
                for (World world : Bukkit.getServer().getWorlds()) {
                    world.setTime(1000);
                }
                System.out.println(ChatColor.GREEN + "Time set to Day");
                return true;
            } else if (command.getName().equalsIgnoreCase("sunset") || command.getName().equalsIgnoreCase("dusk")) {
                for (World world : Bukkit.getServer().getWorlds()) {
                    world.setTime(12610);
                }
                System.out.println(ChatColor.GREEN + "Time set to Sunset");
                return true;
            } else if (command.getName().equalsIgnoreCase("midnight")) {
                for (World world : Bukkit.getServer().getWorlds()) {
                    world.setTime(18000);
                }
                System.out.println(ChatColor.GREEN + "Time set to Midnight");
                return true;
            }else if (command.getName().equalsIgnoreCase("sun")) {
                for (World world : Bukkit.getServer().getWorlds()) {
                    world.setStorm(false);
                }
                System.out.println(ChatColor.GREEN + "Weather changed to " + ChatColor.GOLD + "Sun");
                return true;
            }else if (command.getName().equalsIgnoreCase("storm") || command.getName().equalsIgnoreCase("thunder")) {
                for (World world : Bukkit.getServer().getWorlds()) {
                    world.setStorm(true);
                }
                System.out.println(ChatColor.GREEN + "Weather changed to " + ChatColor.GOLD + "Storm");
                return true;
            }
        }
        return false;
    }
}
