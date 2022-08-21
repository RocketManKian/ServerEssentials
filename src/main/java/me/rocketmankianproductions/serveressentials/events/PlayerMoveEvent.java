package me.rocketmankianproductions.serveressentials.events;

import me.rocketmankianproductions.serveressentials.commands.*;
import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Map;
import java.util.UUID;

public class PlayerMoveEvent implements Listener {

    @EventHandler
    public void onMove(org.bukkit.event.player.PlayerMoveEvent m){
        Player player = m.getPlayer();
        if (Home.cancel.contains(player.getUniqueId())){
            if (m.getFrom().getBlockX() != m.getTo().getBlockX() || m.getFrom().getBlockZ() != m.getTo().getBlockZ() || m.getFrom().getBlockY() != m.getTo().getBlockY()) {
                Home.cancel.remove(player.getUniqueId());
                String msg = Lang.fileConfig.getString("home-movement-cancel");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
            }
        }
        if (Warp.cancel.contains(player.getUniqueId())){
            if (m.getFrom().getBlockX() != m.getTo().getBlockX() || m.getFrom().getBlockZ() != m.getTo().getBlockZ() || m.getFrom().getBlockY() != m.getTo().getBlockY()) {
                Warp.cancel.remove(player.getUniqueId());
                String msg = Lang.fileConfig.getString("warp-movement-cancel");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
            }
        }
        if (Spawn.cancel.contains(player.getUniqueId())){
            if (m.getFrom().getBlockX() != m.getTo().getBlockX() || m.getFrom().getBlockZ() != m.getTo().getBlockZ() || m.getFrom().getBlockY() != m.getTo().getBlockY()) {
                Spawn.cancel.remove(player.getUniqueId());
                String msg = Lang.fileConfig.getString("spawn-movement-cancel");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
            }
        }
        if (TeleportRequest.cancel.contains(player.getUniqueId()) || TeleportRequest.cancel.contains(Bukkit.getPlayer(TeleportRequest.tpa.get(player.getUniqueId())))){
            if (m.getFrom().getBlockX() != m.getTo().getBlockX() || m.getFrom().getBlockZ() != m.getTo().getBlockZ() || m.getFrom().getBlockY() != m.getTo().getBlockY()) {
                UUID playertpau = getKey(TeleportRequest.tpa, player.getUniqueId());
                Player playertpa = Bukkit.getPlayer(getKey(TeleportRequest.tpa, player.getUniqueId()));
                Player playertpahere = Bukkit.getPlayer(getKey(TeleportRequest.tpahere, player.getUniqueId()));
                UUID playertpahereu = getKey(TeleportRequest.tpahere, player.getUniqueId());
                if (player.getUniqueId().equals(playertpau)){
                    TeleportRequest.tpa.remove(playertpa);
                    TeleportRequest.cancelTimeout(playertpa);
                    TeleportRequest.cancel.remove((Bukkit.getPlayer(TeleportRequest.tpa.get(player.getUniqueId()))));
                }else if (player.getUniqueId().equals(playertpahereu)){
                    TeleportRequest.cancelTimeout(playertpahere);
                    TeleportRequest.tpahere.remove(playertpahere);
                    TeleportRequest.cancel.remove((Bukkit.getPlayer(TeleportRequest.tpahere.get(player.getUniqueId()))));
                }
                TeleportRequest.cancel.remove(player.getUniqueId());
                String msg = Lang.fileConfig.getString("teleport-movement-cancel");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
            }
        }
        if (Back.cancel.contains(player.getUniqueId())){
            if (m.getFrom().getBlockX() != m.getTo().getBlockX() || m.getFrom().getBlockZ() != m.getTo().getBlockZ() || m.getFrom().getBlockY() != m.getTo().getBlockY()) {
                Back.cancel.remove(player.getUniqueId());
                String msg = Lang.fileConfig.getString("back-movement-cancel");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
            }
        }
    }
    public <K, V> K getKey(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }
}
