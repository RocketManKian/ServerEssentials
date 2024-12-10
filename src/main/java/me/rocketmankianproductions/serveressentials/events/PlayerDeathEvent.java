package me.rocketmankianproductions.serveressentials.events;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.commands.AFK;
import me.rocketmankianproductions.serveressentials.commands.Back;
import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerDeathEvent implements Listener {

    @EventHandler
    public void onPlayerDeath(org.bukkit.event.entity.PlayerDeathEvent d) {
        Player player = d.getEntity();
        if (player.hasPermission("se.keepinventory")){
            d.setKeepInventory(true);
            d.setKeepLevel(true);
            d.getDrops().clear();
        }
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
        // AFK Command
        if (AFK.afk.containsKey(player)){
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.fileConfig.getString("afk-inactive")));
            player.setSleepingIgnored(false);
            AFK.afk.remove(player);
        }
    }
}