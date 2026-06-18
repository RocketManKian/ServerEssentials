package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
import me.rocketmankianproductions.serveressentials.file.UserFile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Arrays;

import static me.rocketmankianproductions.serveressentials.ServerEssentials.hex;

public class Nickname implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            if (ServerEssentials.permissionChecker(player, "se.nickname")) {
                if (args.length == 1){
                    String msg = Lang.fileConfig.getString("nickname-set").replace("<nickname>", args[0]);
                    player.sendMessage(hex(msg));
                    player.setDisplayName(args[0]);
                    UserFile.config.set(player.getUniqueId() + ".nickname", args[0]);
                    try {
                        UserFile.config.save(UserFile.file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return true;
                }else if (args.length == 2){
                    if (ServerEssentials.permissionChecker(player, "se.nickname.target")) {
                        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
                        if (!target.isOnline() || target.getPlayer() == null){
                            String msg = Lang.fileConfig.getString("target-offline");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                            return true;
                        }
                        String msg = Lang.fileConfig.getString("nickname-set-target").replace("<nickname>", args[1]).replace("<player>", target.getName());
                        player.sendMessage(hex(msg));
                        target.getPlayer().setDisplayName(args[1]);
                        UserFile.config.set(target.getUniqueId() + ".nickname", args[0]);
                        try {
                            UserFile.config.save(UserFile.file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return true;
                    }
                }
            }
        }else{
            if (args.length == 2){
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
                if (!target.isOnline()){
                    String msg = Lang.fileConfig.getString("target-offline");
                    Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                    return true;
                }
                target.getPlayer().setDisplayName(args[0]);
                UserFile.config.set(target.getUniqueId() + ".nickname", args[0]);
                try {
                    UserFile.config.save(UserFile.file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String msg = Lang.fileConfig.getString("nickname-set-target").replace("<nickname>", args[0]).replace("<player>", target.getName());
                Bukkit.getLogger().info(hex(msg));
            }
        }
        return false;
    }
}