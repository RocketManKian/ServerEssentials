package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.LoggerMessage;
import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class SilentJoin {

    public static ServerEssentials plugin;

    //settings
    public static String filepath = "silent.yml";

    public static File file;
    public static FileConfiguration fileConfig;

    //setup function
    public SilentJoin(ServerEssentials plugin) {
        this.plugin = plugin;
        //setup silent.yml
        file = new File(ServerEssentials.plugin.getDataFolder(), filepath);
        if (!file.exists()) {
            try {
                //create default file
                file.createNewFile();
                LoggerMessage.log(LoggerMessage.LogLevel.INFO, "Silent.yml file doesn't exist, creating now...");
                fileConfig = YamlConfiguration.loadConfiguration(file);
                LoggerMessage.log(LoggerMessage.LogLevel.SUCCESS, "Silent.yml file created");

            } catch (IOException e) {
                ServerEssentials.plugin.getLogger().warning(e.toString());
            }
        } else {
            fileConfig = YamlConfiguration.loadConfiguration(file);
        }
    }

    public void run(CommandSender sender, String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            boolean hasPerm = ServerEssentials.permissionChecker(player, "se.silentjoin");
            if (hasPerm) {
                if (fileConfig.getBoolean("silent." + player.getName(), false) == false) {
                    fileConfig.set("silent." + player.getName(), true);
                    String msg = Lang.fileConfig.getString("silentjoin-enabled");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                } else {
                    fileConfig.set("silent." + player.getName(), false);
                    String msg = Lang.fileConfig.getString("silentjoin-disabled");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                }
                try {
                    fileConfig.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else{
            String console = Lang.fileConfig.getString("console-invalid");
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', console));
        }
    }
    public static void reload() {
        if (file != null)
            fileConfig = YamlConfiguration.loadConfiguration(file);
    }
}
