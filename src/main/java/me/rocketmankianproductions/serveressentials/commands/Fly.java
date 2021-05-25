package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Fly implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        if (sender instanceof Player){
            if (player.hasPermission("se.fly")){
                if (args.length == 0){
                    if (player.getAllowFlight() == true){
                        player.setFlying(true);
                        player.sendMessage(ChatColor.RED + "Flying disabled");
                        player.setAllowFlight(false);
                        return true;
                    }else {
                        player.setFlying(false);
                        player.sendMessage(ChatColor.GREEN + "Flying enabled");
                        player.setAllowFlight(true);
                        return true;
                    }
                }else if (args.length == 1){
                    Player targetPlayer = Bukkit.getServer().getPlayer(args[0]);
                    if (targetPlayer.getAllowFlight() == true){
                        targetPlayer.setFlying(true);
                        targetPlayer.sendMessage(ChatColor.RED + "Flying disabled");
                        sender.sendMessage(ChatColor.WHITE + targetPlayer.getName() + ChatColor.RED + " can no longer fly");
                        targetPlayer.setAllowFlight(false);
                        return true;
                    }else {
                        targetPlayer.setFlying(false);
                        targetPlayer.sendMessage(ChatColor.GREEN + "Flying enabled");
                        sender.sendMessage(ChatColor.WHITE + targetPlayer.getName() + ChatColor.GREEN + " can now fly!");
                        targetPlayer.setAllowFlight(true);
                        return true;
                    }
                }
            }else{
                if (ServerEssentials.plugin.getConfig().getString("no-permission-message").length() == 0){
                    player.sendMessage(ChatColor.RED + "You do not have the required permission (se.fly) to run this command.");
                    return true;
                }else{
                    String permission = ServerEssentials.getPlugin().getConfig().getString("no-permission-message");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', permission));
                    return true;
                }
            }
        }else{
            player.sendMessage(ChatColor.RED + "You are not a player.");
            return true;
        }
        return false;
    }
}