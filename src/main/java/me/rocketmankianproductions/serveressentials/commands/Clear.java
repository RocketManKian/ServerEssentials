package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Clear implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            if (player.hasPermission("se.clear") || player.hasPermission("se.all")){
                if (args.length == 0){
                    player.getInventory().clear();
                    String msg = Lang.fileConfig.getString("clear-success");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    return true;
                }else if (args.length == 1){
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target == null){
                        String msg = Lang.fileConfig.getString("player-offline");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    }else if (sender == target){
                        String msg = Lang.fileConfig.getString("target-self");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    }else{
                        target.getInventory().clear();
                        String msg = Lang.fileConfig.getString("clear-target-success").replace("<target>", target.getName());
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    }
                }else{
                    String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/clear (player)");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    return true;
                }
            }else{
                String perm = Lang.fileConfig.getString("no-permission-message").replace("<permission>", "se.clear");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', perm));
                return true;
            }
        }
        return false;
    }
}
