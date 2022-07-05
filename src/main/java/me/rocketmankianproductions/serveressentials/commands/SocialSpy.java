package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SocialSpy implements CommandExecutor {

    public static ArrayList<Player> socialspy = new ArrayList<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (ServerEssentials.permissionChecker(player, "se.socialspy")) {
                // Check if Player isn't already included in ArrayList
                if (!socialspy.contains(player)) {
                    String msg = Lang.fileConfig.getString("socialspy-enabled");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    socialspy.add(player);
                    return true;
                } else {
                    String msg = Lang.fileConfig.getString("socialspy-disabled");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    socialspy.remove(player);
                    return true;
                }
            }
        }else{
            String console = Lang.fileConfig.getString("console-invalid");
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', console));
            return true;
        }
        return false;
    }
}