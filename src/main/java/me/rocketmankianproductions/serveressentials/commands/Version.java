package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Version {
    FileConfiguration config = ServerEssentials.getPlugin().getConfig();
    public static ServerEssentials plugin;

    public Version(ServerEssentials plugin) {
        this.plugin = plugin;
    }


    public void run(CommandSender sender, String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            if (player.hasPermission("se.version")) {
                String prefix = ServerEssentials.getPlugin().getConfig().getString("prefix");
                String version = ServerEssentials.getPlugin().getDescription().getVersion();
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " " + ChatColor.WHITE + "Version: " + ChatColor.GREEN + version));
            } else {
                if (ServerEssentials.plugin.getConfig().getString("no-permission-message").length() == 0){
                    player.sendMessage(ChatColor.RED + "You do not have the required permission (se.version) to run this command.");
                }else{
                    String permission = ServerEssentials.getPlugin().getConfig().getString("no-permission-message");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', permission));
                }
            }
        }else if (sender instanceof ConsoleCommandSender) {
            String prefix = ServerEssentials.getPlugin().getConfig().getString("prefix");
            String version = ServerEssentials.getPlugin().getDescription().getVersion();
            System.out.println(ChatColor.translateAlternateColorCodes('&', prefix + " " + ChatColor.WHITE + "Version: " + ChatColor.GREEN + version));
        }
    }
}
