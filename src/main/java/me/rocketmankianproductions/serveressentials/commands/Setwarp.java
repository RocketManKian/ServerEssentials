package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.LoggerMessage;
import me.rocketmankianproductions.serveressentials.ServerEssentials;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class Setwarp implements CommandExecutor {

    public static ServerEssentials plugin;

    //settings
    public static String filepath = "warp.yml";

    public static File file;
    public static FileConfiguration fileConfig;

    public Setwarp() {
        //setup location.yml
        file = new File(ServerEssentials.getPlugin().getDataFolder(), filepath);
        if (!file.exists()) {
            try {
                //create default file
                file.createNewFile();
                LoggerMessage.log(LoggerMessage.LogLevel.INFO, "Warp.yml file doesn't exist, creating now...");
                fileConfig = YamlConfiguration.loadConfiguration(file);
                LoggerMessage.log(LoggerMessage.LogLevel.SUCCESS, "Warp.yml file created");

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
        Player player = (Player) sender;
        if (sender instanceof Player) {
            if (args.length == 1) {
                Boolean blacklistenabled = ServerEssentials.plugin.getConfig().getBoolean("enable-warp-blacklist");
                String world = player.getWorld().getName();
                String name = player.getUniqueId().toString();
                if (player.hasPermission("se.setwarp")) {
                    if (blacklistenabled){
                        for (String worlds : ServerEssentials.plugin.getConfig().getStringList("warp-blacklist")){
                            if (player.getWorld().getName().equalsIgnoreCase(worlds)){
                                player.sendMessage(ChatColor.RED + "You cannot set a Warp in a Blacklisted World");
                                return true;
                            }
                        }
                        fileConfig.set("Warp." + args[0] + ".World", world);
                        fileConfig.set("Warp." + args[0] + ".X", player.getLocation().getX());
                        fileConfig.set("Warp." + args[0] + ".Y", player.getLocation().getY());
                        fileConfig.set("Warp." + args[0] + ".Z", player.getLocation().getZ());
                        fileConfig.set("Warp." + args[0] + ".Yaw", player.getLocation().getYaw());
                        try {
                            fileConfig.save(file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Setwarp.reload();
                        player.sendMessage(ChatColor.GREEN + "Successfully set warp location!");
                        return true;
                    }else{
                        fileConfig.set("Warp." + args[0] + ".World", world);
                        fileConfig.set("Warp." + args[0] + ".X", player.getLocation().getX());
                        fileConfig.set("Warp." + args[0] + ".Y", player.getLocation().getY());
                        fileConfig.set("Warp." + args[0] + ".Z", player.getLocation().getZ());
                        fileConfig.set("Warp." + args[0] + ".Yaw", player.getLocation().getYaw());
                        try {
                            fileConfig.save(file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Setwarp.reload();
                        player.sendMessage(ChatColor.GREEN + "Successfully set warp location!");
                        return true;
                    }
                } else {
                    if (ServerEssentials.plugin.getConfig().getString("no-permission-message").length() == 0){
                        player.sendMessage(ChatColor.RED + "You do not have the required permission (se.setwarp) to run this command.");
                        return true;
                    }else{
                        String permission = ServerEssentials.getPlugin().getConfig().getString("no-permission-message");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', permission));
                        return true;
                    }
                }
            }
        } else {
            player.sendMessage(ChatColor.RED + "Use \"/setwarp (name)\" to create a new warp.");
            return true;
        }
        return false;
    }
}
