package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class Version {
    public static ServerEssentials plugin;

    public Version(ServerEssentials plugin) {
        this.plugin = plugin;
    }


    public void run(CommandSender sender, String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            boolean hasPerm = ServerEssentials.permissionChecker(player, "se.version");
            if (hasPerm) {
                String version = ServerEssentials.getPlugin().getDescription().getVersion();
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&bServer Essentials&7] " + ChatColor.WHITE + "Version: " + ChatColor.GREEN + version));
            }
        }else if (sender instanceof ConsoleCommandSender) {
            String version = ServerEssentials.getPlugin().getDescription().getVersion();
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&',  "&7[&bServer Essentials&7] " + ChatColor.WHITE + "Version: " + ChatColor.GREEN + version));
        }
    }
}