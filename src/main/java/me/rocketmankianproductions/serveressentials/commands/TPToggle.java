package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.LoggerMessage;
import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

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
        if (sender instanceof Player){
            Player player = (Player) sender;
            if (command.getName().equalsIgnoreCase("tptoggle")) {
                boolean hasPerm = ServerEssentials.permissionChecker(player, "se.tptoggle");
                if (hasPerm) {
                    if (sender instanceof Player) {
                        if (args.length == 0) {
                            if (fileConfig.getBoolean("tptoggle." + player.getName(), false) == false) {
                                fileConfig.set("tptoggle." + player.getName(), true);
                                try {
                                    fileConfig.save(file);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                String msg = Lang.fileConfig.getString("teleport-toggle-enabled");
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            } else {
                                fileConfig.set("tptoggle." + player.getName(), false);
                                try {
                                    fileConfig.save(file);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                String msg = Lang.fileConfig.getString("teleport-toggle-disabled");
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            }
                        } else {
                            String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/tptoggle");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                            return true;
                        }
                    } else {
                        String msg = Lang.fileConfig.getString("invalid-player");
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    }
                }
            }
        }else{
            String console = Lang.fileConfig.getString("console-invalid");
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', console));
            return true;
        }
        return true;
    }
}
