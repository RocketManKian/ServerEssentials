package me.rocketmankianproductions.serveressentials.file;

import me.rocketmankianproductions.serveressentials.LoggerMessage;
import me.rocketmankianproductions.serveressentials.ServerEssentials;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class UserFile {
    public static ServerEssentials plugin;

    //settings
    public static String filepath = "userinfo.yml";

    public static File file;
    public static FileConfiguration fileConfig;

    //setup function
    public UserFile(ServerEssentials plugin) {
        this.plugin = plugin;
        //setup silent.yml
        file = new File(ServerEssentials.plugin.getDataFolder() + "", filepath);
        if (!file.exists()) {
            try {
                //create default file
                file.createNewFile();
                LoggerMessage.log(LoggerMessage.LogLevel.INFO, "userinfo.yml file doesn't exist, creating now...");
                fileConfig = YamlConfiguration.loadConfiguration(file);
                LoggerMessage.log(LoggerMessage.LogLevel.SUCCESS, "userinfo.yml file created");

            } catch (IOException e) {
                ServerEssentials.plugin.getLogger().warning(e.toString());
            }
        } else {
            fileConfig = YamlConfiguration.loadConfiguration(file);
        }
    }
    public static void reload() {
        if (file != null)
            fileConfig = YamlConfiguration.loadConfiguration(file);
    }
}
