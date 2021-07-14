package me.rocketmankianproductions.serveressentials.events;

import me.rocketmankianproductions.serveressentials.commands.Home;
import me.rocketmankianproductions.serveressentials.commands.Spawn;
import me.rocketmankianproductions.serveressentials.commands.TeleportRequest;
import me.rocketmankianproductions.serveressentials.commands.Warp;
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
            if (m.getFrom().getX() != m.getTo().getX() && m.getFrom().getZ() != m.getTo().getZ()){
                Home.cancel.remove(player.getUniqueId());
                player.sendMessage(ChatColor.RED + "Teleportation cancelled due to Movement");
            }
        }
        if (Warp.cancel.contains(player.getUniqueId())){
            if (m.getFrom().getX() != m.getTo().getX() && m.getFrom().getZ() != m.getTo().getZ()){
                Warp.cancel.remove(player.getUniqueId());
                player.sendMessage(ChatColor.RED + "Warping cancelled due to Movement");
            }
        }
        if (Spawn.cancel.contains(player.getUniqueId())){
            if (m.getFrom().getX() != m.getTo().getX() && m.getFrom().getZ() != m.getTo().getZ()){
                Spawn.cancel.remove(player.getUniqueId());
                player.sendMessage(ChatColor.RED + "Teleportation cancelled due to Movement");
            }
        }
        if (TeleportRequest.cancel.contains(player.getUniqueId()) || TeleportRequest.cancel.contains(Bukkit.getPlayer(TeleportRequest.tpa.get(player.getUniqueId())))){
            if (m.getFrom().getX() != m.getTo().getX() && m.getFrom().getZ() != m.getTo().getZ()){
                UUID player2 = getKey(TeleportRequest.tpa, player.getUniqueId());
                UUID player3 = getKey(TeleportRequest.tpa, player.getUniqueId());
                TeleportRequest.tpa.remove(player2);
                TeleportRequest.tpahere.remove(player3);
                TeleportRequest.teleportcancel.remove(player.getUniqueId());
                TeleportRequest.teleportcancel.remove(player2);
                TeleportRequest.teleportcancel.remove(player3);
                TeleportRequest.cancel.remove(player.getUniqueId());
                TeleportRequest.cancel.remove((Bukkit.getPlayer(TeleportRequest.tpa.get(player.getUniqueId()))));
                player.sendMessage(ChatColor.RED + "Teleportation cancelled due to Movement");
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
