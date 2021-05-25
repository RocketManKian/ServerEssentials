package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.UUID;

public class SocialSpy implements CommandExecutor {

    public static ArrayList<Player> socialspy = new ArrayList<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("se.socialspy")) {
                // Check if Player isn't already included in ArrayList
                if (!socialspy.contains(player)) {
                    player.sendMessage(ChatColor.GREEN + "SocialSpy has been enabled");
                    socialspy.add(player);
                    return true;
                } else {
                    player.sendMessage(ChatColor.GREEN + "SocialSpy has been disabled");
                    socialspy.remove(player);
                    return true;
                }
            } else {
                if (ServerEssentials.plugin.getConfig().getString("no-permission-message").length() == 0) {
                    player.sendMessage(ChatColor.RED + "You do not have the required permission (se.socialspy) to run this command.");
                    return true;
                } else {
                    String permission = ServerEssentials.getPlugin().getConfig().getString("no-permission-message");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', permission));
                    return true;
                }
            }
        }
        return false;
    }
}