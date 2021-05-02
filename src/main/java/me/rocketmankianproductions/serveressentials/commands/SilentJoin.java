package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.LoggerMessage;
import me.rocketmankianproductions.serveressentials.ServerEssentials;
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
        Player player = (Player) sender;
        if (player.hasPermission("se.silentjoin")) {
            if (fileConfig.getBoolean("silent." + player.getName(), false) == false) {
                fileConfig.set("silent." + player.getName(), true);
                player.sendMessage(ChatColor.GREEN + "SilentJoin has been Enabled");
            } else {
                fileConfig.set("silent." + player.getName(), false);
                player.sendMessage(ChatColor.RED + "SilentJoin has been Disabled");
            }
            try {
                fileConfig.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            if (ServerEssentials.plugin.getConfig().getString("no-permission-message").length() == 0){
                player.sendMessage(ChatColor.RED + "You do not have the required permission (se.silentjoin) to run this command.");
            }else{
                String permission = ServerEssentials.getPlugin().getConfig().getString("no-permission-message");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', permission));
            }
        }
    }
    public static void reload() {
        if (file != null)
            fileConfig = YamlConfiguration.loadConfiguration(file);
    }
}
