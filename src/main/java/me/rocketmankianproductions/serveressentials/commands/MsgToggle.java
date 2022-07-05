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
import java.util.HashMap;
import java.util.UUID;

public class MsgToggle {

    public static ServerEssentials plugin;

    //settings
    public static String filepath = "message.yml";

    public static File file;
    public static FileConfiguration fileConfig;

    private static HashMap<UUID, UUID> requests = new HashMap<>();

    //setup function
    public MsgToggle(ServerEssentials plugin) {
        this.plugin = plugin;
        //setup silent.yml
        file = new File(ServerEssentials.plugin.getDataFolder(), filepath);
        if (!file.exists()) {
            try {
                //create default file
                file.createNewFile();
                LoggerMessage.log(LoggerMessage.LogLevel.INFO, "Message.yml file doesn't exist, creating now...");
                fileConfig = YamlConfiguration.loadConfiguration(file);
                LoggerMessage.log(LoggerMessage.LogLevel.SUCCESS, "Message.yml file created");

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
            if (command.getName().equalsIgnoreCase("msgtoggle")) {
                if (ServerEssentials.permissionChecker(player, "se.msgtoggle")) {
                    if (args.length == 0) {
                        if (fileConfig.getBoolean("msgtoggle." + player.getName(), false) == false) {
                            fileConfig.set("msgtoggle." + player.getName(), true);
                            try {
                                fileConfig.save(file);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            String msg = Lang.fileConfig.getString("message-toggle-enabled");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                            return true;
                        } else {
                            fileConfig.set("msgtoggle." + player.getName(), false);
                            try {
                                fileConfig.save(file);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            String msg = Lang.fileConfig.getString("message-toggle-disabled");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                            return true;
                        }
                    } else {
                        String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/msgtoggle");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    }
                }
            }
        }else{
            String console = Lang.fileConfig.getString("console-invalid");
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', console));
        }
        return false;
    }
}