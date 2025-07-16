package me.rocketmankianproductions.serveressentials.events;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.commands.Back;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerWorldCheck implements Listener {

    Location loc;

    @EventHandler
    public void onPlayerWorldChange (PlayerTeleportEvent e){
        Player player = e.getPlayer();

        if (e.getFrom().getWorld() != e.getTo().getWorld()){
            double x = e.getFrom().getX();
            double z = e.getFrom().getZ();
            double y = e.getFrom().getY();
            loc = new Location(e.getFrom().getWorld(), x, y, z);
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

    @EventHandler
    public void onWorldChange (PlayerChangedWorldEvent event){
        Player player = event.getPlayer();
        // Fly
        if (player.hasPermission("se.fly") && event.getPlayer().isFlying()) {
            player.setAllowFlight(true);
            player.setFlying(true);
        }else {
            player.setAllowFlight(false);
            player.setFlying(false);
        }
    }
}