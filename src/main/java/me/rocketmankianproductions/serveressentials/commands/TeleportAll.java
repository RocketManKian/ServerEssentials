package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TeleportAll implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("se.tpall")) {
                if (Bukkit.getServer().getOnlinePlayers().size() == 1) {
                    sender.sendMessage(ChatColor.GREEN + "No other players are on right now.");
                } else if (Bukkit.getServer().getOnlinePlayers().size() > 1 && args.length == 0) {
                    int numOfPlayer = 0;
                    for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                        numOfPlayer++;
                        p.teleport(player.getLocation());
                    }
                    player.sendMessage(ChatColor.GREEN + "Teleported " + (numOfPlayer - 1) + " player(s) to you.");
                }
            } else {
                if (ServerEssentials.plugin.getConfig().getString("no-permission-message").length() == 0){
                    player.sendMessage(ChatColor.RED + "You do not have the required permission (se.tpall) to run this command.");
                    return true;
                }else{
                    String permission = ServerEssentials.getPlugin().getConfig().getString("no-permission-message");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', permission));
                    return true;
                }
            }
        }
        return true;
    }
}