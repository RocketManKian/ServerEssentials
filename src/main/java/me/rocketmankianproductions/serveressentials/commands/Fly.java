package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.file.Lang;
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
                        String msg = Lang.fileConfig.getString("fly-disabled");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        player.setAllowFlight(false);
                        return true;
                    }else {
                        player.setFlying(false);
                        String msg = Lang.fileConfig.getString("fly-enabled");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        player.setAllowFlight(true);
                        return true;
                    }
                }else if (args.length == 1){
                    Player targetPlayer = Bukkit.getServer().getPlayer(args[0]);
                    if (targetPlayer.getAllowFlight() == true){
                        targetPlayer.setFlying(true);
                        String msg = Lang.fileConfig.getString("fly-disabled");
                        targetPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        String msg2 = Lang.fileConfig.getString("fly-target-disabled").replace("<target>", targetPlayer.getName());
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg2));
                        targetPlayer.setAllowFlight(false);
                        return true;
                    }else {
                        targetPlayer.setFlying(false);
                        String msg = Lang.fileConfig.getString("fly-enabled");
                        targetPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        String msg2 = Lang.fileConfig.getString("fly-target-enabled").replace("<target>", targetPlayer.getName());
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg2));
                        targetPlayer.setAllowFlight(true);
                        return true;
                    }
                }
            }else{
                String perm = Lang.fileConfig.getString("no-permission-message").replace("<permission>", "se.fly");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', perm));
                return true;
            }
        }else{
            String msg = Lang.fileConfig.getString("player-offline");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
            return true;
        }
        return false;
    }
}