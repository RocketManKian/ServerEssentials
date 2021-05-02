package me.rocketmankianproductions.serveressentials.commands;
import me.rocketmankianproductions.serveressentials.LoggerMessage;
import me.rocketmankianproductions.serveressentials.ServerEssentials;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class TPToggle {

    public static ServerEssentials plugin;

    //settings
    public static String filepath = "teleport.yml";

    public static File file;
    public static FileConfiguration fileConfig;

    //setup function
    public TPToggle(ServerEssentials plugin) {
        this.plugin = plugin;
        //setup silent.yml
        file = new File(ServerEssentials.plugin.getDataFolder(), filepath);
        if (!file.exists()) {
            try {
                //create default file
                file.createNewFile();
                LoggerMessage.log(LoggerMessage.LogLevel.INFO, "Teleport.yml file doesn't exist, creating now...");
                fileConfig = YamlConfiguration.loadConfiguration(file);
                LoggerMessage.log(LoggerMessage.LogLevel.SUCCESS, "Teleport.yml file created");

            } catch (IOException e) {
                ServerEssentials.plugin.getLogger().warning(e.toString());
            }
        } else if (file.exists()) {
            fileConfig = YamlConfiguration.loadConfiguration(file);
        }
    }

    public static void reload() {
        if (file != null)
            fileConfig = YamlConfiguration.loadConfiguration(file);
    }

    public boolean run(CommandSender sender, @NotNull String[] args, Command command) {
        Player player = (Player) sender;
        if (command.getName().equalsIgnoreCase("tptoggle")) {
            if (player.hasPermission("se.tptoggle")) {
                if ((sender instanceof Player)) {
                    if (args.length == 0) {
                        if (fileConfig.getBoolean("tptoggle." + player.getName(), false) == false) {
                            fileConfig.set("tptoggle." + player.getName(), true);
                            try {
                                fileConfig.save(file);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            player.sendMessage(ChatColor.RED + "Teleport Requests have been Disabled");
                            return true;
                        } else {
                            fileConfig.set("tptoggle." + player.getName(), false);
                            try {
                                fileConfig.save(file);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            player.sendMessage(ChatColor.GREEN + "Teleport Requests have been Enabled");
                            return true;
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "Incorrect format! Please use /tptoggle");
                        return true;
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "You aren't a player!");
                    return true;
                }
            } else {
                if (ServerEssentials.plugin.getConfig().getString("no-permission-message").length() == 0){
                    player.sendMessage(ChatColor.RED + "You do not have the required permission (se.tptoggle) to run this command.");
                    return true;
                }else{
                    String permission = ServerEssentials.getPlugin().getConfig().getString("no-permission-message");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', permission));
                }
                return true;
            }
        }
        return true;
    }
}
