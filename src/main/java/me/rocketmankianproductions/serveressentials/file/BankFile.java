package me.rocketmankianproductions.serveressentials.file;

import me.rocketmankianproductions.serveressentials.LoggerMessage;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class BankFile {
    public static File file;
    public static FileConfiguration config;

    public static void setup() {
        file = new File("plugins/ServerEssentials", "bankdata.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
                LoggerMessage.log(LoggerMessage.LogLevel.INFO, "bankdata.yml file doesn't exist, creating now...");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    public static void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void reload() {
        config = YamlConfiguration.loadConfiguration(file);
    }
}