package me.rocketmankianproductions.serveressentials.events;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeave implements Listener {

    @EventHandler
    void onPlayerJoin(PlayerQuitEvent pj){
        Player player = pj.getPlayer();
        String lm = ServerEssentials.getPlugin().getConfig().getString("leave-symbol");
        pj.setQuitMessage(ChatColor.translateAlternateColorCodes('&', lm + " " + ChatColor.GOLD + player.getDisplayName()));
    }
}
