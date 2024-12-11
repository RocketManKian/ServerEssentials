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
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import static me.rocketmankianproductions.serveressentials.ServerEssentials.hex;

public class Freeze implements CommandExecutor {

    public static HashMap<OfflinePlayer, Boolean> freeze = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            if (ServerEssentials.permissionChecker(player, "se.freeze")){
                if (args.length == 1){
                    OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
                    if (target.isOnline()){
                        if (freeze.containsKey(target)){
                            freeze.remove(target);
                            String msg = Lang.fileConfig.getString("unfreeze-target").replace("<player>", target.getName());
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                            return true;
                        }else{
                            freeze.put(target,  true);
                            String msg = Lang.fileConfig.getString("freeze-target").replace("<player>", target.getName());
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                            return true;
                        }
                    }else{
                        String msg = Lang.fileConfig.getString("target-offline");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                        return true;
                    }
                }else{
                    String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/freeze <player>");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                    return true;
                }
            }
        }
        return false;
    }
}