package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.LoggerMessage;
import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.ChatColor;
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

public class Sethome implements CommandExecutor {

    public static ServerEssentials plugin;

    //settings
    public static String filepath = "home.yml";

    public static File file;
    public static FileConfiguration fileConfig;

    public Sethome() {
        //setup location.yml
        file = new File(ServerEssentials.getPlugin().getDataFolder(), filepath);
        if (!file.exists()) {
            try {
                //create default file
                file.createNewFile();
                LoggerMessage.log(LoggerMessage.LogLevel.INFO, "Home.yml file doesn't exist, creating now...");
                fileConfig = YamlConfiguration.loadConfiguration(file);
                LoggerMessage.log(LoggerMessage.LogLevel.SUCCESS, "Home.yml file created");

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

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 1) {
                String world = player.getWorld().getName();
                String name = player.getUniqueId().toString();
                int maxHomes = ServerEssentials.plugin.getConfig().getInt("default-home-count");

                if (fileConfig.contains("Home." + name)) {
                    ConfigurationSection inventorySection = fileConfig.getConfigurationSection("Home." + name);
                    int homesAmount = 0;
                    if (inventorySection != null) {
                       homesAmount = inventorySection.getKeys(false).size();
                    }
                    Boolean blacklistenabled = ServerEssentials.getPlugin().getConfig().getBoolean("enable-home-blacklist");
                    if (blacklistenabled) {
                        for (String worlds : ServerEssentials.plugin.getConfig().getStringList("home-blacklist")) {
                            if (player.getWorld().getName().equalsIgnoreCase(worlds)) {
                                String msg = Lang.fileConfig.getString("home-blacklisted-world");
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            }
                        }
                        if (fileConfig.getString("Home." + name + "." + args[0]) != null || player.hasPermission("se.sethome.unlimited")) {
                            createHome(name, args, world, player);
                            return true;
                        } else if (player.hasPermission("se.sethome") || player.hasPermission("se.all")) {
                            if (homesAmount < maxHomes) {
                                createHome(name, args, world, player);
                            } else {
                                String msg = Lang.fileConfig.getString("home-max-homes").replace("<max>", String.valueOf(maxHomes));
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            }
                            return true;
                        } else if (!player.hasPermission("se.sethome")) {
                            String perm = Lang.fileConfig.getString("no-permission-message").replace("<permission>", "se.sethome");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', perm));
                            return true;
                        }
                    }else{
                        if (fileConfig.getString("Home." + name + "." + args[0]) != null || player.hasPermission("se.sethome.unlimited")) {
                            createHome(name, args, world, player);
                            return true;
                        } else if (player.hasPermission("se.sethome") || player.hasPermission("se.all")) {
                            if (homesAmount < maxHomes) {
                                createHome(name, args, world, player);
                            } else {
                                String msg = Lang.fileConfig.getString("home-max-homes").replace("<max>", String.valueOf(maxHomes));
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            }
                            return true;
                        } else if (!player.hasPermission("se.sethome")) {
                            String perm = Lang.fileConfig.getString("no-permission-message").replace("<permission>", "se.sethome");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', perm));
                            return true;
                        }
                    }
                } else if (fileConfig.getString("Home." + name + "." + args[0]) == null && player.hasPermission("se.sethome")) {
                    Boolean blacklistenabled = ServerEssentials.getPlugin().getConfig().getBoolean("enable-home-blacklist");
                    if (blacklistenabled){
                        for (String worlds : ServerEssentials.plugin.getConfig().getStringList("home-blacklist")) {
                            if (player.getWorld().getName().equalsIgnoreCase(worlds)) {
                                String msg = Lang.fileConfig.getString("home-blacklisted-world");
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            }
                        }
                        createHome(name, args, world, player);
                        return true;
                    }else{
                        createHome(name, args, world, player);
                        return true;
                    }
                } else {
                    String perm = Lang.fileConfig.getString("no-permission-message").replace("<permission>", "se.sethome");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', perm));
                    return true;
                }
            } else {
                player.sendMessage(ChatColor.RED + "Use \"/sethome (name)\" to set your home.");
            }
        }
        return false;
    }

    public void createHome(String name, String[] args, String world, Player player) {
        fileConfig.set("Home." + name + "." + args[0] + ".World", world);
        fileConfig.set("Home." + name + "." + args[0] + ".X", player.getLocation().getX());
        fileConfig.set("Home." + name + "." + args[0] + ".Y", player.getLocation().getY());
        fileConfig.set("Home." + name + "." + args[0] + ".Z", player.getLocation().getZ());
        fileConfig.set("Home." + name + "." + args[0] + ".Yaw", player.getLocation().getYaw());
        try {
            fileConfig.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Sethome.reload();
        String msg = Lang.fileConfig.getString("home-set-success");
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
    }
}