package me.rocketmankianproductions.serveressentials.events;

import me.clip.placeholderapi.PlaceholderAPI;
import me.rocketmankianproductions.serveressentials.ServerEssentials;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class WelcomeMessage implements Listener {

    @EventHandler
    void firstjoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!ServerEssentials.getPlugin().getConfig().getBoolean("enable-firstjoin-message"));
            //blah
        else {
            boolean hasjoined = player.hasPlayedBefore();
            String welcome = ServerEssentials.getPlugin().getConfig().getString("first-join-message");
            String placeholder = PlaceholderAPI.setPlaceholders(player, welcome);

            if (!hasjoined) {
                event.setJoinMessage(ChatColor.translateAlternateColorCodes('&', "" + placeholder));
            } else {
                //blah
            }
        }
    }
}
