package me.rocketmankianproductions.serveressentials.events;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {
    FileConfiguration config = ServerEssentials.getPlugin().getConfig();
    @EventHandler
    void onPlayerJoin(PlayerJoinEvent pj) {
        Player player = pj.getPlayer();
        String jm = ServerEssentials.getPlugin().getConfig().getString("join-symbol");
        pj.setJoinMessage(ChatColor.translateAlternateColorCodes('&', jm + " " + ChatColor.GOLD + player.getDisplayName()));
    }
}