package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
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
            if (player.hasPermission("se.version")) {
                String version = ServerEssentials.getPlugin().getDescription().getVersion();
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&bServer Essentials&7] " + ChatColor.WHITE + "Version: " + ChatColor.GREEN + version));
            } else {
                if (ServerEssentials.plugin.getConfig().getString("no-permission-message").length() == 0){
                    player.sendMessage(ChatColor.RED + "You do not have the required permission (se.version) to run this command.");
                }else{
                    String permission = ServerEssentials.getPlugin().getConfig().getString("no-permission-message");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', permission));
                }
            }
        }else if (sender instanceof ConsoleCommandSender) {
            String version = ServerEssentials.getPlugin().getDescription().getVersion();
            System.out.println(ChatColor.translateAlternateColorCodes('&',  "&7[&bServer Essentials&7] " + ChatColor.WHITE + "Version: " + ChatColor.GREEN + version));
        }
    }
}
