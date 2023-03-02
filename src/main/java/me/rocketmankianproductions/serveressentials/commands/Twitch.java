package me.rocketmankianproductions.serveressentials.commands;

import me.clip.placeholderapi.PlaceholderAPI;
import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static me.rocketmankianproductions.serveressentials.ServerEssentials.hex;

public class Twitch implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (ServerEssentials.permissionChecker(player, "se.twitch")) {
                String prefix = ServerEssentials.getPlugin().getConfig().getString("prefix");
                String twitch = Lang.fileConfig.getString("twitch-command");
                if (ServerEssentials.isConnectedToPlaceholderAPI) {
                    String placeholder = PlaceholderAPI.setPlaceholders(player, twitch);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " " + ChatColor.WHITE + hex(placeholder)));
                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " " + ChatColor.WHITE + hex(twitch)));
                }
            }
        } else {
            String console = Lang.fileConfig.getString("console-invalid");
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', hex(console)));
            return true;
        }
        return false;
    }
}