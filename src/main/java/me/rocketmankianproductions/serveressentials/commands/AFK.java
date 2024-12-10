package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
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

    public static HashMap<Player, Boolean> afk = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            if (ServerEssentials.permissionChecker(player, "se.afk")){
                if (!afk.containsKey(player)){
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.fileConfig.getString("afk-active")));
                    player.setSleepingIgnored(true);
                    afk.put(player, true);
                }else{
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.fileConfig.getString("afk-inactive")));
                    player.setSleepingIgnored(false);
                    afk.remove(player);
                }
            }
        }
        return false;
    }

    @EventHandler
    public void onPlayerClick(PlayerInteractEvent player){
        // AFK Command
        if (AFK.afk.containsKey(player)){
            player.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.fileConfig.getString("afk-inactive")));
            player.getPlayer().setSleepingIgnored(false);
            AFK.afk.remove(player);
        }
    }

    @EventHandler
    public void onBlockBreak (BlockBreakEvent e){
        // AFK Command
        if (AFK.afk.containsKey(e.getPlayer())){
            e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.fileConfig.getString("afk-inactive")));
            e.getPlayer().setSleepingIgnored(false);
            AFK.afk.remove(e.getPlayer());
        }
    }
}
