package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.LoggerMessage;
import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
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
            ConfigurationSection inventorySection = Setwarp.fileConfig.getConfigurationSection("Warp.");
            if (inventorySection != null){
                for (String key : inventorySection.getKeys(false)) {
                    if (fileConfig.getString("Warp." + key + ".Block") == null){
                        Material material = Material.valueOf(ServerEssentials.plugin.getConfig().getString("warp-item"));
                        fileConfig.set("Warp." + key + ".Block", String.valueOf(material));
                        try {
                            fileConfig.save(Setwarp.file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        reload();
                    }
                }
            }
        }
    }

    public static void reload() {
        fileConfig = YamlConfiguration.loadConfiguration(file);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 1) {
                Boolean blacklistenabled = ServerEssentials.plugin.getConfig().getBoolean("enable-warp-blacklist");
                String world = player.getWorld().getName();
                if (ServerEssentials.permissionChecker(player, "se.setwarp")) {
                    if (blacklistenabled){
                        for (String worlds : ServerEssentials.plugin.getConfig().getStringList("warp-blacklist")){
                            if (player.getWorld().getName().equalsIgnoreCase(worlds)){
                                String msg = Lang.fileConfig.getString("warp-blacklisted-world");
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            }
                        }
                        String warpitem = ServerEssentials.plugin.getConfig().getString("warp-item");
                        fileConfig.set("Warp." + args[0] + ".World", world);
                        fileConfig.set("Warp." + args[0] + ".X", player.getLocation().getX());
                        fileConfig.set("Warp." + args[0] + ".Y", player.getLocation().getY());
                        fileConfig.set("Warp." + args[0] + ".Z", player.getLocation().getZ());
                        fileConfig.set("Warp." + args[0] + ".Yaw", player.getLocation().getYaw());
                        if (fileConfig.getString("Warp." + args[0] + ".Block") == null){
                            fileConfig.set("Warp." + args[0] + ".Block", warpitem);
                        }
                        try {
                            fileConfig.save(file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        reload();
                        String msg = Lang.fileConfig.getString("warp-set-successful");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    }else{
                        String warpitem = ServerEssentials.plugin.getConfig().getString("warp-item");
                        fileConfig.set("Warp." + args[0] + ".World", world);
                        fileConfig.set("Warp." + args[0] + ".X", player.getLocation().getX());
                        fileConfig.set("Warp." + args[0] + ".Y", player.getLocation().getY());
                        fileConfig.set("Warp." + args[0] + ".Z", player.getLocation().getZ());
                        fileConfig.set("Warp." + args[0] + ".Yaw", player.getLocation().getYaw());
                        if (fileConfig.getString("Warp." + args[0] + ".Block") == null){
                            fileConfig.set("Warp." + args[0] + ".Block", warpitem);
                        }
                        try {
                            fileConfig.save(file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        reload();
                        String msg = Lang.fileConfig.getString("warp-set-successful");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    }
                }
            }else{
                String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/setwarp (name)");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                return true;
            }
        } else {
            String console = Lang.fileConfig.getString("console-invalid");
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', console));
            return true;
        }
        return false;
    }
}