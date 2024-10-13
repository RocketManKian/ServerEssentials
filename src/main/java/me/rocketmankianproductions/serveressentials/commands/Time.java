package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static me.rocketmankianproductions.serveressentials.ServerEssentials.hex;

public class Time implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (ServerEssentials.permissionChecker(player, "se.time")) {
                if (command.getName().equalsIgnoreCase("time") && args.length == 1) {
                    long time = Long.parseLong(args[0]);
                    player.getWorld().setTime(time);
                    String msg = Lang.fileConfig.getString("time-set").replace("<time>", String.valueOf(time));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                    return true;
                } else if (command.getName().equalsIgnoreCase("dawn") || command.getName().equalsIgnoreCase("sunrise")) {
                    player.getWorld().setTime(23041);
                    String msg = Lang.fileConfig.getString("time-sunrise");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                    return true;
                } else if (command.getName().equalsIgnoreCase("day")) {
                    player.getWorld().setTime(1000);
                    String msg = Lang.fileConfig.getString("time-day");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                    return true;
                } else if (command.getName().equalsIgnoreCase("sunset") || command.getName().equalsIgnoreCase("dusk")) {
                    player.getWorld().setTime(12610);
                    String msg = Lang.fileConfig.getString("time-sunset");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                    return true;
                } else if (command.getName().equalsIgnoreCase("night")){
                    player.getWorld().setTime(13000);
                    String msg = Lang.fileConfig.getString("time-night");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                    return true;
                } else if (command.getName().equalsIgnoreCase("midnight")) {
                    player.getWorld().setTime(18000);
                    String msg = Lang.fileConfig.getString("time-midnight");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                    return true;
                } else if (command.getName().equalsIgnoreCase("sun")) {
                    player.getWorld().setStorm(false);
                    String msg = Lang.fileConfig.getString("weather-sun");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                    return true;
                } else if (command.getName().equalsIgnoreCase("storm") || command.getName().equalsIgnoreCase("thunder")) {
                    player.getWorld().setStorm(true);
                    String msg = Lang.fileConfig.getString("weather-storm");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                    return true;
                }else{
                    String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/(time/dawn/day/sunset/midnight/sun/storm)");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                    return true;
                }
            }
        }else if (sender instanceof ConsoleCommandSender) {
            if (command.getName().equalsIgnoreCase("time") && args.length == 1){
                long time = Long.parseLong(args[0]);
                for (World world : Bukkit.getServer().getWorlds()) {
                    world.setTime(time);
                }
                String msg = Lang.fileConfig.getString("time-set").replace("<time>", String.valueOf(time));
                Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                return true;
            }else if (command.getName().equalsIgnoreCase("dawn") || command.getName().equalsIgnoreCase("sunrise")) {
                for (World world : Bukkit.getServer().getWorlds()) {
                    world.setTime(23041);
                }
                String msg = Lang.fileConfig.getString("time-sunrise");
                Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                return true;
            } else if (command.getName().equalsIgnoreCase("day")) {
                for (World world : Bukkit.getServer().getWorlds()) {
                    world.setTime(1000);
                }
                String msg = Lang.fileConfig.getString("time-day");
                Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                return true;
            } else if (command.getName().equalsIgnoreCase("sunset") || command.getName().equalsIgnoreCase("dusk")) {
                for (World world : Bukkit.getServer().getWorlds()) {
                    world.setTime(12610);
                }
                String msg = Lang.fileConfig.getString("time-sunset");
                Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                return true;
            } else if (command.getName().equalsIgnoreCase("night")){
                for (World world : Bukkit.getServer().getWorlds()) {
                    world.setTime(13000);
                }
                String msg = Lang.fileConfig.getString("time-night");
                Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                return true;
            } else if (command.getName().equalsIgnoreCase("midnight")) {
                for (World world : Bukkit.getServer().getWorlds()) {
                    world.setTime(18000);
                }
                String msg = Lang.fileConfig.getString("time-midnight");
                Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                return true;
            }else if (command.getName().equalsIgnoreCase("sun")) {
                for (World world : Bukkit.getServer().getWorlds()) {
                    world.setStorm(false);
                }
                String msg = Lang.fileConfig.getString("weather-sun");
                Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                return true;
            }else if (command.getName().equalsIgnoreCase("storm") || command.getName().equalsIgnoreCase("thunder")) {
                for (World world : Bukkit.getServer().getWorlds()) {
                    world.setStorm(true);
                }
                String msg = Lang.fileConfig.getString("weather-storm");
                Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                return true;
            }
            // Command Blocks
        } else if (sender instanceof BlockCommandSender){
            BlockCommandSender block = (BlockCommandSender) sender;
            if (command.getName().equalsIgnoreCase("time") && args.length == 1){
                long time = Long.parseLong(args[0]);
                block.getBlock().getWorld().setTime(time);
                String msg = Lang.fileConfig.getString("time-set").replace("<time>", String.valueOf(time));
                Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                return true;
            }else if (command.getName().equalsIgnoreCase("dawn") || command.getName().equalsIgnoreCase("sunrise")) {
                block.getBlock().getWorld().setTime(23041);
                String msg = Lang.fileConfig.getString("time-sunrise");
                Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                return true;
            } else if (command.getName().equalsIgnoreCase("day")) {
                block.getBlock().getWorld().setTime(1000);
                String msg = Lang.fileConfig.getString("time-day");
                Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                return true;
            } else if (command.getName().equalsIgnoreCase("sunset") || command.getName().equalsIgnoreCase("dusk")) {
                block.getBlock().getWorld().setTime(12610);
                String msg = Lang.fileConfig.getString("time-sunset");
                Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                return true;
            } else if (command.getName().equalsIgnoreCase("night")){
                block.getBlock().getWorld().setTime(13000);
                String msg = Lang.fileConfig.getString("time-night");
                Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                return true;
            } else if (command.getName().equalsIgnoreCase("midnight")) {
                block.getBlock().getWorld().setTime(18000);
                String msg = Lang.fileConfig.getString("time-midnight");
                Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                return true;
            }else if (command.getName().equalsIgnoreCase("sun")) {
                block.getBlock().getWorld().setStorm(false);
                String msg = Lang.fileConfig.getString("weather-sun");
                Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                return true;
            }else if (command.getName().equalsIgnoreCase("storm") || command.getName().equalsIgnoreCase("thunder")) {
                block.getBlock().getWorld().setStorm(true);
                String msg = Lang.fileConfig.getString("weather-storm");
                Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                return true;
            }
        }
        return false;
    }
}