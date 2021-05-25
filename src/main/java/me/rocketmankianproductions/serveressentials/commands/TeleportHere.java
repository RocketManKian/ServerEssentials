package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TeleportHere implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        if (player.hasPermission("se.teleport")){
            if (args.length == 1) {
                Player target = Bukkit.getPlayer(args[0]);
                String sender2 = sender.getName();
                if (target == sender) {
                    sender.sendMessage(ChatColor.RED + "You cannot teleport yourself to yourself...");
                } else if (target != sender) {
                    try {
                        String target2 = target.getName();
                        if (sender.hasPermission("se.silenttp")) {
                            sender.sendMessage(ChatColor.GREEN + target2 + " has been teleported to you");
                        } else if (!sender.hasPermission("se.silenttp")) {
                            target.sendMessage(ChatColor.GREEN + "You have been teleported to " + sender2);
                            sender.sendMessage(ChatColor.GREEN + target2 + " has been teleported to you");
                        }
                        target.teleport(player.getLocation());
                    } catch (NullPointerException e) {
                        player.sendMessage(ChatColor.RED + "Player does not exist.");
                    }
                    return true;
                }
            }
        }else{
            // If it doesn't succeed with either then it'll send the player a required permission message
            if (ServerEssentials.plugin.getConfig().getString("no-permission-message").length() == 0){
                player.sendMessage(ChatColor.RED + "You do not have the required permission (se.teleport) to run this command.");
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
