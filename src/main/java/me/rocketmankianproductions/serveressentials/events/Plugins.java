package me.rocketmankianproductions.serveressentials.events;

import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import static me.rocketmankianproductions.serveressentials.ServerEssentials.hex;

public class Plugins implements Listener {

    @EventHandler
    public void onTypeCommand (PlayerCommandPreprocessEvent e){
        if (e.getMessage().equalsIgnoreCase("/plugins") || e.getMessage().equalsIgnoreCase("/pl")){
            Player player = e.getPlayer();
            if (!player.hasPermission("bukkit.command.plugins")){
                String msg = Lang.fileConfig.getString("plugins-command");
                if (!msg.isEmpty()){
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                    e.setCancelled(true);
                }
            }
        }
    }
}