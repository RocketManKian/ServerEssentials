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

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static me.rocketmankianproductions.serveressentials.ServerEssentials.hex;

public class Rules implements CommandExecutor {

    //settings
    private final String filepath = "rules.yml";
    private final String configpath = "Rules";
    private final List<String> default_values = Arrays.asList("Don't Swear", "Don't Grief");

    public static File file;
    public static FileConfiguration fileConfig;

    //setup function
    public Rules() {
        //setup rules.yml
        file = new File(ServerEssentials.plugin.getDataFolder(), filepath);
        if (!file.exists()) {
            try {
                //create default file
                file.createNewFile();
                LoggerMessage.log(LoggerMessage.LogLevel.INFO, "Rules.yml file doesn't exist, creating now...");
                fileConfig = YamlConfiguration.loadConfiguration(file);
                //set default values
                fileConfig.addDefault(configpath, default_values);
                fileConfig.options().copyDefaults(true);
                fileConfig.save(file);
                LoggerMessage.log(LoggerMessage.LogLevel.SUCCESS, "Rules.yml file created");

            } catch (IOException e) {
                ServerEssentials.plugin.getLogger().warning(e.toString());
            }
        } else {
            fileConfig = YamlConfiguration.loadConfiguration(file);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            //check if file exists
            if (fileConfig == null) {
                ServerEssentials.plugin.getLogger().warning(filepath + " not loaded, abort!");
                return false;
            }
            Player player = (Player) sender;
            if (ServerEssentials.permissionChecker(player, "se.rules")) {
                player.sendMessage(ChatColor.GREEN + "Server Rules:");
                for (String rule : fileConfig.getStringList(configpath)) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(rule)));
                }
                if (!fileConfig.getStringList("Tips").isEmpty()){
                    player.sendMessage(ChatColor.GREEN + "Tips:");
                    for (String rule : fileConfig.getStringList("Tips")) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(rule)));
                    }
                }
            }
        }else{
            String console = Lang.fileConfig.getString("console-invalid");
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', hex(console)));
            return true;
        }
        return false;
    }

    public static void reload() {
        fileConfig = YamlConfiguration.loadConfiguration(file);
    }
}
