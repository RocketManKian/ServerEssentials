package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.file.Lang;
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
        if (player.hasPermission("se.teleport") || player.hasPermission("se.all")){
            if (args.length == 1) {
                Player target = Bukkit.getPlayer(args[0]);
                String sender2 = sender.getName();
                if (target == sender) {
                    String msg = Lang.fileConfig.getString("teleport-self");
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                } else if (target != sender) {
                    try {
                        String target2 = target.getName();
                        if (sender.hasPermission("se.silenttp") || player.hasPermission("se.all")) {
                            String msg = Lang.fileConfig.getString("teleport-target-success").replace("<sender>", target2);
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        } else if (!sender.hasPermission("se.silenttp")) {
                            String msg = Lang.fileConfig.getString("teleport-force-target").replace("<target>", sender2);
                            target.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                            String msg2 = Lang.fileConfig.getString("teleport-target-success").replace("<sender>", target2);
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg2));
                        }
                        target.teleport(player.getLocation());
                    } catch (NullPointerException e) {
                        String msg = Lang.fileConfig.getString("target-offline");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    }
                    return true;
                }
            }
        }else{
            String perm = Lang.fileConfig.getString("no-permission-message").replace("<permission>", "se.teleport");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', perm));
            return true;
        }
        return false;
    }
}