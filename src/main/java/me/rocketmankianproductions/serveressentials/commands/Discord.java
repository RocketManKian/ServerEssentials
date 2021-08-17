package me.rocketmankianproductions.serveressentials.commands;

import me.clip.placeholderapi.PlaceholderAPI;
import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Discord implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if (player.hasPermission("se.discord") || player.hasPermission("se.all")) {
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
            String perm = Lang.fileConfig.getString("no-permission-message").replace("<permission>", "se.discord");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', perm));
            return true;
        }
        return false;
    }
}
