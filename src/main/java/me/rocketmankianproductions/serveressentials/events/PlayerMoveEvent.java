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

import static me.rocketmankianproductions.serveressentials.ServerEssentials.hex;

public class PlayerMoveEvent implements Listener {

    @EventHandler
    public void onMove(org.bukkit.event.player.PlayerMoveEvent m){
        Player player = m.getPlayer();

        if (Home.cancel.contains(player.getUniqueId())){
            if (m.getFrom().getBlockX() != m.getTo().getBlockX() || m.getFrom().getBlockZ() != m.getTo().getBlockZ() || m.getFrom().getBlockY() != m.getTo().getBlockY()) {
                Home.cancel.remove(player.getUniqueId());
                String msg = Lang.fileConfig.getString("home-movement-cancel");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
            }
        }
        if (Warp.cancel.contains(player.getUniqueId())){
            if (m.getFrom().getBlockX() != m.getTo().getBlockX() || m.getFrom().getBlockZ() != m.getTo().getBlockZ() || m.getFrom().getBlockY() != m.getTo().getBlockY()) {
                Warp.cancel.remove(player.getUniqueId());
                String msg = Lang.fileConfig.getString("warp-movement-cancel");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
            }
        }
        if (Spawn.cancel.contains(player.getUniqueId())){
            if (m.getFrom().getBlockX() != m.getTo().getBlockX() || m.getFrom().getBlockZ() != m.getTo().getBlockZ() || m.getFrom().getBlockY() != m.getTo().getBlockY()) {
                Spawn.cancel.remove(player.getUniqueId());
                String msg = Lang.fileConfig.getString("spawn-movement-cancel");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
            }
        }
        if (TeleportRequest.cancel.contains(player.getUniqueId())) {
            if (m.getFrom().getBlockX() != m.getTo().getBlockX() || m.getFrom().getBlockZ() != m.getTo().getBlockZ() || m.getFrom().getBlockY() != m.getTo().getBlockY()) {
                for (UUID playertpu : TeleportRequest.cancel){
                    if (playertpu.equals(player.getUniqueId())){
                        Player playertp = Bukkit.getPlayer(playertpu);
                        TeleportRequest.tpa.remove(playertp);
                        TeleportRequest.cancel.remove(playertpu);
                        String msg = Lang.fileConfig.getString("teleport-movement-cancel");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                        break;
                    }
                }
            }
        } else if (TeleportRequest.cancel2.contains(player.getUniqueId())) {
            if (m.getFrom().getBlockX() != m.getTo().getBlockX() || m.getFrom().getBlockZ() != m.getTo().getBlockZ() || m.getFrom().getBlockY() != m.getTo().getBlockY()) {
                for (UUID playertpahereu : TeleportRequest.cancel2){
                    if (playertpahereu.equals(player.getUniqueId())){
                        Player playertpahere = Bukkit.getPlayer(playertpahereu);
                        TeleportRequest.tpahere.remove(playertpahere);
                        TeleportRequest.cancel2.remove(playertpahereu);
                        String msg = Lang.fileConfig.getString("teleport-movement-cancel");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                        break;
                    }
                }
            }
        }
        if (Back.cancel.contains(player.getUniqueId())){
            if (m.getFrom().getBlockX() != m.getTo().getBlockX() || m.getFrom().getBlockZ() != m.getTo().getBlockZ() || m.getFrom().getBlockY() != m.getTo().getBlockY()) {
                Back.cancel.remove(player.getUniqueId());
                String msg = Lang.fileConfig.getString("back-movement-cancel");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
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