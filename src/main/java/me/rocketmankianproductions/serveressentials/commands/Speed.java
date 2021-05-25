package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Speed implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        if (sender instanceof Player){
            if (player.hasPermission("se.speed")){
                if (args.length == 1){
                    int speed;
                    try {
                        speed = Integer.parseInt(args[0]);
                    } catch (NumberFormatException e){
                        player.sendMessage(ChatColor.RED + "Please provide a speed from 1-10");
                        return true;
                    }
                    if (speed <1 || speed > 10){
                        player.sendMessage(ChatColor.RED + "Please provide a speed from 1-10");
                        return true;
                    }
                    if (player.isFlying()){
                        player.setFlySpeed((float) speed / 10);
                        player.sendMessage(ChatColor.GOLD + "Set" + ChatColor.RED + " flying" + ChatColor.GOLD + " speed to " + ChatColor.RED + (float)speed);
                        return true;
                    }else if (!player.isFlying()){
                        player.setWalkSpeed((float) speed / 10);
                        player.sendMessage(ChatColor.GOLD + "Set" + ChatColor.RED + " walking" + ChatColor.GOLD + " speed to " + ChatColor.RED + (float)speed);
                        return true;
                    }
                }
            }else{
                if (ServerEssentials.plugin.getConfig().getString("no-permission-message").length() == 0){
                    player.sendMessage(ChatColor.RED + "You do not have the required permission (se.speed) to run this command.");
                    return true;
                }else{
                    String permission = ServerEssentials.getPlugin().getConfig().getString("no-permission-message");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', permission));
                    return true;
                }
            }
        }
        return false;
    }
}