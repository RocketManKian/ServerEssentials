package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
import me.rocketmankianproductions.serveressentials.utils.AFKManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class AFK implements CommandExecutor, Listener {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            if (ServerEssentials.permissionChecker(player, "se.afk")){
                if (!AFKManager.isAFK(player)){
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.fileConfig.getString("afk-active")));
                    player.setSleepingIgnored(true);
                    AFKManager.setAFK(player, true);
                }else{
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.fileConfig.getString("afk-inactive")));
                    player.setSleepingIgnored(false);
                    AFKManager.setAFK(player, false);
                }
            }
        }
        return false;
    }

    @EventHandler
    public void onPlayerClick(PlayerInteractEvent event){
        // AFK Command
        if (event instanceof Player){
            Player player = event.getPlayer();
            if (AFKManager.isAFK(player)){
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.fileConfig.getString("afk-inactive")));
                player.setSleepingIgnored(false);
                AFKManager.setAFK(player, false);
            }
        }
    }

    @EventHandler
    public void onBlockBreak (BlockBreakEvent event){
        // AFK Command
        if (event instanceof Player){
            Player player = event.getPlayer();
            if (AFKManager.isAFK(player)){
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.fileConfig.getString("afk-inactive")));
                player.setSleepingIgnored(false);
                AFKManager.setAFK(player, false);
            }
        }
    }
}