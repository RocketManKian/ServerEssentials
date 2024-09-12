package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.LoggerMessage;
import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
import me.rocketmankianproductions.serveressentials.file.UserFile;
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

public class SocialSpy implements CommandExecutor {

    public static ServerEssentials plugin;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (command.getName().equalsIgnoreCase("socialspy")){
                if (ServerEssentials.permissionChecker(player, "se.socialspy")) {
                    if (!UserFile.fileConfig.getBoolean(player.getUniqueId() + ".spy")) {
                        UserFile.fileConfig.set(player.getUniqueId() + ".spy", true);
                        String msg = Lang.fileConfig.getString("socialspy-enabled");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                    } else {
                        UserFile.fileConfig.set(player.getUniqueId() + ".spy", false);
                        String msg = Lang.fileConfig.getString("socialspy-disabled");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                    }
                    try {
                        UserFile.fileConfig.save(UserFile.file);
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