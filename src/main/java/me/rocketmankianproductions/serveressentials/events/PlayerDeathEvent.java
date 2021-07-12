package me.rocketmankianproductions.serveressentials.events;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.commands.Back;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerDeathEvent implements Listener {

    @EventHandler
    void onPlayerDeath(org.bukkit.event.entity.PlayerDeathEvent d) {
        Player player = d.getEntity();
        if (ServerEssentials.plugin.getConfig().getBoolean("death-save")){
            if (Back.location.containsKey(player.getUniqueId())){
                Back.location.remove(player.getUniqueId());
                Back.location.put(player.getUniqueId(), player.getLocation());
            }else{
                Back.location.put(player.getUniqueId(), player.getLocation());
            }
        }else if (player.hasPermission("se.back.bypass")){
            if (Back.location.containsKey(player.getUniqueId())){
                Back.location.remove(player.getUniqueId());
                Back.location.put(player.getUniqueId(), player.getLocation());
            }else{
                Back.location.put(player.getUniqueId(), player.getLocation());
            }
        }
    }
}
