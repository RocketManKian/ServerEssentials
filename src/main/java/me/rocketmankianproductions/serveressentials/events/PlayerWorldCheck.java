package me.rocketmankianproductions.serveressentials.events;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.commands.Back;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerWorldCheck implements Listener {

    Location loc;

    @EventHandler
    public void onPlayerWorldChange (PlayerTeleportEvent w){
        if (w.getFrom().getWorld() != w.getTo().getWorld()){
            Player player = w.getPlayer();
            double x = w.getFrom().getX();
            double z = w.getFrom().getZ();
            double y = w.getFrom().getY();
            loc = new Location(w.getFrom().getWorld(), x, y, z);
            if (ServerEssentials.plugin.getConfig().getBoolean("world-save")){
                if (Back.location.containsKey(player.getUniqueId())){
                    Back.location.remove(player.getUniqueId());
                    Back.location.put(player.getUniqueId(), loc);
                }else{
                    Back.location.put(player.getUniqueId(), loc);
                }
            }else if (player.hasPermission("se.back.bypass")){
                if (Back.location.containsKey(player.getUniqueId())){
                    Back.location.remove(player.getUniqueId());
                    Back.location.put(player.getUniqueId(), loc);
                }else{
                    Back.location.put(player.getUniqueId(), loc);
                }
            }
        }
    }
}
