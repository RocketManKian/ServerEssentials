package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.LoggerMessage;
import me.rocketmankianproductions.serveressentials.ServerEssentials;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
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

    @Override
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
                    if (fileConfig.getString("Home." + name + "." + args[0]) != null || player.hasPermission("se.sethome.unlimited")) {
                        createHome(name, args, world, player);
                        return true;
                    } else if (player.hasPermission("se.sethome")) {
                        if (homesAmount < maxHomes) {
                            createHome(name, args, world, player);
                        } else {
                            player.sendMessage(ChatColor.RED + "You cannot set any more homes. (Max Homes: " + maxHomes + ")");
                            return true;
                        }
                        return true;
                    } else if (!player.hasPermission("se.sethome")) {
                        if (ServerEssentials.plugin.getConfig().getString("no-permission-message").length() == 0) {
                            player.sendMessage(ChatColor.RED + "You do not have the required permission (se.sethome) to run this command.");
                            return true;
                        } else {
                            String permission = ServerEssentials.getPlugin().getConfig().getString("no-permission-message");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', permission));
                        }
                        return true;
                    }
                } else if (fileConfig.getString("Home." + name + "." + args[0]) == null && player.hasPermission("se.sethome")) {
                    createHome(name, args, world, player);
                    return true;
                } else {
                    if (ServerEssentials.plugin.getConfig().getString("no-permission-message").length() == 0) {
                        player.sendMessage(ChatColor.RED + "You do not have the required permission (se.sethome) to run this command.");
                        return true;
                    } else {
                        String permission = ServerEssentials.getPlugin().getConfig().getString("no-permission-message");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', permission));
                    }
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
        player.sendMessage(ChatColor.GREEN + "Successfully set home location!");
    }
}