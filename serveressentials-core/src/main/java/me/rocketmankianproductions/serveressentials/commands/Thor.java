package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import org.jetbrains.annotations.NotNull;

import static me.rocketmankianproductions.serveressentials.ServerEssentials.hex;

public class Thor implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (commandSender instanceof Player){
            Player player = (Player) commandSender;
            if (ServerEssentials.permissionChecker(player, "se.thor")){
                if (args.length == 0){
                    RayTraceResult result = player.getWorld().rayTraceBlocks(player.getEyeLocation(), player.getEyeLocation().getDirection(), 50);
                    if (result != null && result.getHitBlock() != null) {
                        player.getWorld().strikeLightning(result.getHitPosition().toLocation(player.getWorld()));
                        String thormsg = Lang.fileConfig.getString("thor-location");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(thormsg)));
                    }else{
                        String thormsg = Lang.fileConfig.getString("thor-invalid");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(thormsg)));
                    }
                    return true;
                }else if (args.length >= 1){
                    OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
                    if (target.isOnline()){
                        String thormsg = Lang.fileConfig.getString("thor-message");
                        target.getPlayer().getWorld().strikeLightning(target.getPlayer().getLocation());
                        target.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', hex(thormsg)));
                        return true;
                    }else{
                        String msg = Lang.fileConfig.getString("target-offline");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                        return true;
                    }
                }
            }
        }
        return false;
    }
}