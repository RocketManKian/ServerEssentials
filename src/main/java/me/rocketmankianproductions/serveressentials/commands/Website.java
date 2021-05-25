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

public class Website implements CommandExecutor {

    FileConfiguration config = ServerEssentials.getPlugin().getConfig();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if (player.hasPermission("se.website")) {
            String prefix = ServerEssentials.getPlugin().getConfig().getString("prefix");
            String website = ServerEssentials.getPlugin().getConfig().getString("website-command");
            if (ServerEssentials.isConnectedToPlaceholderAPI) {
                String placeholder = PlaceholderAPI.setPlaceholders(player, website);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " " + ChatColor.WHITE + placeholder));
            }else{
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " " + ChatColor.WHITE + website));
            }
        } else {
            if (ServerEssentials.plugin.getConfig().getString("no-permission-message").length() == 0){
                player.sendMessage(ChatColor.RED + "You do not have the required permission (se.website) to run this command.");
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