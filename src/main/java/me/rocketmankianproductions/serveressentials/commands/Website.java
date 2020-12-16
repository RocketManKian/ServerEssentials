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
        if (!ServerEssentials.getPlugin().getConfig().getBoolean("enable-website-command"))
            player.sendMessage(ChatColor.RED + "Command is disabled. Please contact an Administrator.");
        else if (player.hasPermission("se.website")) {
            String prefix = ServerEssentials.getPlugin().getConfig().getString("prefix");
            String website = ServerEssentials.getPlugin().getConfig().getString("website-command");
            String placeholder = PlaceholderAPI.setPlaceholders(player, website);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " " + ChatColor.WHITE + placeholder));
        } else {
            player.sendMessage(ChatColor.RED + "You do not have the required permission for (se.website) to run this command.");
        }
        return false;
    }
}
