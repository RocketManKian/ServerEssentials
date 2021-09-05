package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.file.Lang;
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
                if (player.hasPermission("se.time") || player.hasPermission("se.all")){
                    if (command.getName().equalsIgnoreCase("dawn") || command.getName().equalsIgnoreCase("sunrise")) {
                        for (World world : Bukkit.getServer().getWorlds()) {
                            world.setTime(23041);
                        }
                        String msg = Lang.fileConfig.getString("time-sunrise");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    }else if (command.getName().equalsIgnoreCase("day")) {
                        for (World world : Bukkit.getServer().getWorlds()) {
                            world.setTime(1000);
                        }
                        String msg = Lang.fileConfig.getString("time-day");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    }else if (command.getName().equalsIgnoreCase("sunset") || command.getName().equalsIgnoreCase("dusk")) {
                        for (World world : Bukkit.getServer().getWorlds()) {
                            world.setTime(12610);
                        }
                        String msg = Lang.fileConfig.getString("time-sunset");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    }else if (command.getName().equalsIgnoreCase("midnight")) {
                        for (World world : Bukkit.getServer().getWorlds()) {
                            world.setTime(18000);
                        }
                        String msg = Lang.fileConfig.getString("time-midnight");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    }else if (command.getName().equalsIgnoreCase("sun")) {
                        for (World world : Bukkit.getServer().getWorlds()) {
                            world.setStorm(false);
                        }
                        String msg = Lang.fileConfig.getString("weather-sun");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    }else if (command.getName().equalsIgnoreCase("storm") || command.getName().equalsIgnoreCase("thunder")) {
                        for (World world : Bukkit.getServer().getWorlds()) {
                            world.setStorm(true);
                        }
                        String msg = Lang.fileConfig.getString("weather-storm");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    }
                }else{
                    String perm = Lang.fileConfig.getString("no-permission-message").replace("<permission>", "se.time");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', perm));
                    return true;
                }
            }
        }else if (sender instanceof ConsoleCommandSender) {
            if (command.getName().equalsIgnoreCase("dawn") || command.getName().equalsIgnoreCase("sunrise")) {
                for (World world : Bukkit.getServer().getWorlds()) {
                    world.setTime(23041);
                }
                String msg = Lang.fileConfig.getString("time-sunrise");
                Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', msg));
                return true;
            } else if (command.getName().equalsIgnoreCase("day")) {
                for (World world : Bukkit.getServer().getWorlds()) {
                    world.setTime(1000);
                }
                String msg = Lang.fileConfig.getString("time-day");
                Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', msg));
                return true;
            } else if (command.getName().equalsIgnoreCase("sunset") || command.getName().equalsIgnoreCase("dusk")) {
                for (World world : Bukkit.getServer().getWorlds()) {
                    world.setTime(12610);
                }
                String msg = Lang.fileConfig.getString("time-sunset");
                Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', msg));
                return true;
            } else if (command.getName().equalsIgnoreCase("midnight")) {
                for (World world : Bukkit.getServer().getWorlds()) {
                    world.setTime(18000);
                }
                String msg = Lang.fileConfig.getString("time-midnight");
                Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', msg));
                return true;
            }else if (command.getName().equalsIgnoreCase("sun")) {
                for (World world : Bukkit.getServer().getWorlds()) {
                    world.setStorm(false);
                }
                String msg = Lang.fileConfig.getString("weather-sun");
                Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', msg));
                return true;
            }else if (command.getName().equalsIgnoreCase("storm") || command.getName().equalsIgnoreCase("thunder")) {
                for (World world : Bukkit.getServer().getWorlds()) {
                    world.setStorm(true);
                }
                String msg = Lang.fileConfig.getString("weather-storm");
                Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', msg));
                return true;
            }
        }
        return false;
    }
}
