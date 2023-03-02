package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class AFK implements CommandExecutor, Listener {

    public static ArrayList<UUID> afk = new ArrayList();
    private static final HashMap<Player, Location> lastmovement = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            if (afk.contains(player.getUniqueId())){
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.fileConfig.getString("afk-inactive")));
                afk.remove(player.getUniqueId());
            }else{
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.fileConfig.getString("afk-active")));
                afk.add(player.getUniqueId());
            }
        }
        return false;
    }

    @EventHandler
    public void onPlayerMove (PlayerMoveEvent event){
        Player player = event.getPlayer();
        if (afk.contains(player.getUniqueId()) && (event.getFrom().getBlockX() != event.getTo().getBlockX() || event.getFrom().getBlockZ() != event.getTo().getBlockZ() || event.getFrom().getBlockY() != event.getTo().getBlockY())) {
            afk.remove(player.getUniqueId());
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.fileConfig.getString("afk-inactive")));
        }
        lastmovement.put(player, player.getLocation());
    }

    @EventHandler
    public void onPlayerQuit (PlayerQuitEvent event){
        Player player = event.getPlayer();
        if (!player.isOnline()) {
            afk.remove(player.getUniqueId());
        }
    }
}
