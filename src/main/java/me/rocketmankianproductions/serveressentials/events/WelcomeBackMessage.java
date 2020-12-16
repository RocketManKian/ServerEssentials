package me.rocketmankianproductions.serveressentials.events;

import me.clip.placeholderapi.PlaceholderAPI;
import me.rocketmankianproductions.serveressentials.ServerEssentials;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class WelcomeBackMessage implements Listener {

    @EventHandler
    void firstjoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!ServerEssentials.getPlugin().getConfig().getBoolean("enable-welcomeback-message"));
            //blah
        else {
            boolean hasjoined = player.hasPlayedBefore();
            String welcomeback = ServerEssentials.getPlugin().getConfig().getString("welcome-back-message");
            String placeholder = PlaceholderAPI.setPlaceholders(player, welcomeback);

            if (hasjoined) {
                event.setJoinMessage(ChatColor.translateAlternateColorCodes('&', "" + placeholder));
            } else {
                //blah
            }
        }
    }
}
