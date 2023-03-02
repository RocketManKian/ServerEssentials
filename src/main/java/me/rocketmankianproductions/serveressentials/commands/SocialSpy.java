package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.LoggerMessage;
import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.Bukkit;
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
import java.util.ArrayList;

import static me.rocketmankianproductions.serveressentials.ServerEssentials.hex;

public class SocialSpy {

    public static ServerEssentials plugin;

    //settings
    public static String filepath = "spy.yml";

    public static File file;
    public static FileConfiguration fileConfig;

    //setup function
    public SocialSpy(ServerEssentials plugin) {
        this.plugin = plugin;
        //setup silent.yml
        file = new File(ServerEssentials.plugin.getDataFolder(), filepath);
        if (!file.exists()) {
            try {
                //create default file
                file.createNewFile();
                LoggerMessage.log(LoggerMessage.LogLevel.INFO, "Spy.yml file doesn't exist, creating now...");
                fileConfig = YamlConfiguration.loadConfiguration(file);
                fileConfig.createSection("Spy");
                try {
                    fileConfig.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                LoggerMessage.log(LoggerMessage.LogLevel.SUCCESS, "Spy.yml file created");

            } catch (IOException e) {
                ServerEssentials.plugin.getLogger().warning(e.toString());
            }
        } else {
            fileConfig = YamlConfiguration.loadConfiguration(file);
            if (fileConfig.getKeys(true).isEmpty()){
                fileConfig.createSection("Spy");
                try {
                    fileConfig.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static void reload() {
        fileConfig = YamlConfiguration.loadConfiguration(file);
    }

    public boolean run(CommandSender sender, @NotNull String[] args, Command command) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (command.getName().equalsIgnoreCase("socialspy")){
                if (ServerEssentials.permissionChecker(player, "se.socialspy")) {
                    if (!fileConfig.getBoolean("Spy." + player.getName())) {
                        fileConfig.set("Spy." + player.getName(), true);
                        String msg = Lang.fileConfig.getString("socialspy-enabled");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                    } else {
                        fileConfig.set("Spy." + player.getName(), false);
                        String msg = Lang.fileConfig.getString("socialspy-disabled");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                    }
                    try {
                        fileConfig.save(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }else{
            String console = Lang.fileConfig.getString("console-invalid");
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', hex(console)));
        }
        return false;
    }
}