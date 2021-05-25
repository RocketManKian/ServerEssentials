package me.rocketmankianproductions.serveressentials.commands;

import me.clip.placeholderapi.PlaceholderAPI;
import me.rocketmankianproductions.serveressentials.ServerEssentials;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Discord implements CommandExecutor {

    FileConfiguration config = ServerEssentials.getPlugin().getConfig();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if (player.hasPermission("se.discord")) {
            String prefix = ServerEssentials.getPlugin().getConfig().getString("prefix");
            String discord = ServerEssentials.getPlugin().getConfig().getString("discord-command");
            if (ServerEssentials.isConnectedToPlaceholderAPI) {
                String placeholder = PlaceholderAPI.setPlaceholders(player, discord);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " " + ChatColor.WHITE + placeholder));
            }else{
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " " + ChatColor.WHITE + discord));
            }
        } else {
            // If it doesn't succeed with either then it'll send the player a required permission message
            if (ServerEssentials.plugin.getConfig().getString("no-permission-message").length() == 0){
                player.sendMessage(ChatColor.RED + "You do not have the required permission (se.discord) to run this command.");
                return true;
            }else{
                String permission = ServerEssentials.getPlugin().getConfig().getString("no-permission-message");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', permission));
                return true;
            }
        }
        return false;
    }
}
