package me.rocketmankianproductions.serveressentials.events;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;
import me.rocketmankianproductions.serveressentials.LoggerMessage;
import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.commands.Mute;
import me.rocketmankianproductions.serveressentials.commands.StaffChat;
import me.rocketmankianproductions.serveressentials.file.Lang;
import me.rocketmankianproductions.serveressentials.file.UserFile;
import me.rocketmankianproductions.serveressentials.utils.AFKManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import static me.rocketmankianproductions.serveressentials.ServerEssentials.hex;

public class PlayerChatEvent implements Listener {

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent c) {
        Player player = c.getPlayer();
        // Get Mute Status
        String muteTime = Mute.getPlayerMuteTime(player);

        if (muteTime != null && !player.hasPermission("se.mute.bypass")) {
            c.setCancelled(true);

            String raw = Lang.fileConfig.getString("mute-message");

            // Add <time> placeholder support
            if (muteTime.equalsIgnoreCase("Permanent")) {
                raw = raw.replace("<time>", "Permanent");
            } else {
                raw = raw.replace("<time>", muteTime);
            }

            player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(raw)));
        }
    }
}