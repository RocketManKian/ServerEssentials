package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.LoggerMessage;
import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class Setspawn implements CommandExecutor {

    //settings
    public static String filepath = "spawn.yml";

    public static File file;
    public static FileConfiguration fileConfig;


    public Setspawn() {
        //setup location.yml
        file = new File(ServerEssentials.getPlugin().getDataFolder(), filepath);
        if (!file.exists()) {
            try {
                //create default file
                file.createNewFile();
                LoggerMessage.log(LoggerMessage.LogLevel.INFO, "Spawn.yml file doesn't exist, creating now...");
                fileConfig = YamlConfiguration.loadConfiguration(file);
                LoggerMessage.log(LoggerMessage.LogLevel.SUCCESS, "Spawn.yml file created");

            } catch (IOException e) {
                ServerEssentials.plugin.getLogger().warning(e.toString());
            }
        } else {
            fileConfig = YamlConfiguration.loadConfiguration(file);
        }
    }

    public static void reload() {
        fileConfig = YamlConfiguration.loadConfiguration(file);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                if (player.hasPermission("se.setspawn") || player.hasPermission("se.all")) {
                    String world = player.getWorld().getName();
                    fileConfig.set("Location.World", world);
                    fileConfig.set("Location.X", player.getLocation().getX());
                    fileConfig.set("Location.Y", player.getLocation().getY());
                    fileConfig.set("Location.Z", player.getLocation().getZ());
                    fileConfig.set("Location.Yaw", player.getLocation().getYaw());
                    fileConfig.set("Location.Pitch", player.getLocation().getPitch());
                    try {
                        fileConfig.save(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Setspawn.reload();
                    String msg = Lang.fileConfig.getString("spawn-set-successful").replace("<world>", world);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                } else {
                    String perm = Lang.fileConfig.getString("no-permission-message").replace("<permission>", "se.setspawn");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', perm));
                    return true;
                }
            } else if (args.length == 1 && args[0].equalsIgnoreCase("newbies")){
                if (player.hasPermission("se.setspawn") || player.hasPermission("se.all")) {
                    String world = player.getWorld().getName();
                    fileConfig.set("Newbies.Location.World", world);
                    fileConfig.set("Newbies.Location.X", player.getLocation().getX());
                    fileConfig.set("Newbies.Location.Y", player.getLocation().getY());
                    fileConfig.set("Newbies.Location.Z", player.getLocation().getZ());
                    fileConfig.set("Newbies.Location.Yaw", player.getLocation().getYaw());
                    fileConfig.set("Newbies.Location.Pitch", player.getLocation().getPitch());
                    try {
                        fileConfig.save(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Setspawn.reload();
                    String msg = Lang.fileConfig.getString("newbie-spawn-set-successful").replace("<world>", world);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                } else {
                    String perm = Lang.fileConfig.getString("no-permission-message").replace("<permission>", "se.setspawn");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', perm));
                    return true;
                }
            } else if (args.length == 3){
                if (player.hasPermission("se.setspawn") || player.hasPermission("se.all")) {
                    String world = player.getWorld().getName();
                    fileConfig.set("Location.World", world);
                    try {
                        double x = Double.parseDouble(args[0]);
                        double y = Double.parseDouble(args[1]);
                        double z = Double.parseDouble(args[2]);
                        fileConfig.set("Location.X", x);
                        fileConfig.set("Location.Y", y);
                        fileConfig.set("Location.Z", z);
                        fileConfig.set("Location.Yaw", player.getLocation().getYaw());
                        fileConfig.set("Location.Pitch", player.getLocation().getPitch());
                        try {
                            fileConfig.save(file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Setspawn.reload();
                        String msg = Lang.fileConfig.getString("spawn-set-successful").replace("<world>", world);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    } catch (NumberFormatException n) {
                        String msg = Lang.fileConfig.getString("teleport-pos-invalid");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    }
                }else{
                    String perm = Lang.fileConfig.getString("no-permission-message").replace("<permission>", "se.setspawn");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', perm));
                    return true;
                }
            } else if (args.length == 4 && args[0].equalsIgnoreCase("newbies")){
                if (player.hasPermission("se.setspawn") || player.hasPermission("se.all")) {
                    String world = player.getWorld().getName();
                    fileConfig.set("Newbies.Location.World", world);
                    try {
                        double x = Double.parseDouble(args[0]);
                        double y = Double.parseDouble(args[1]);
                        double z = Double.parseDouble(args[2]);
                        fileConfig.set("Newbies.Location.X", x);
                        fileConfig.set("Newbies.Location.Y", y);
                        fileConfig.set("Newbies.Location.Z", z);
                        fileConfig.set("Newbies.Location.Yaw", player.getLocation().getYaw());
                        fileConfig.set("Newbies.Location.Pitch", player.getLocation().getPitch());
                        try {
                            fileConfig.save(file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Setspawn.reload();
                        String msg = Lang.fileConfig.getString("newbies-spawn-set-successful").replace("<world>", world);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    } catch (NumberFormatException n) {
                        String msg = Lang.fileConfig.getString("teleport-pos-invalid");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    }
                }else{
                    String perm = Lang.fileConfig.getString("no-permission-message").replace("<permission>", "se.setspawn");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', perm));
                    return true;
                }
            }else{
                String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/setspawn");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                return true;
            }
        }else{
            String console = Lang.fileConfig.getString("console-invalid");
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', console));
            return true;
        }
        return false;
    }
}