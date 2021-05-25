package me.rocketmankianproductions.serveressentials.events;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;
import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.commands.StaffChat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChatEvent implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent c) {
        Player player = c.getPlayer();
        if (StaffChat.staffchat.contains(player)) {
            String message = c.getMessage();
            if (ServerEssentials.isConnectedToDiscordSRV && ServerEssentials.getPlugin().getConfig().getBoolean("enable-discord-integration") == true) {
                TextChannel textChannel = DiscordSRV.getPlugin().getDestinationTextChannelForGameChannelName("staffchat");
                if (textChannel != null) {
                    String player1 = player.getDisplayName();
                    player1 = ChatColor.stripColor(player1);
                    textChannel.sendMessage("**" + player1 + "** » " + message).queue();
                    Bukkit.broadcast(ChatColor.translateAlternateColorCodes('&', "&d(&5&lStaff&d) ") + ChatColor.LIGHT_PURPLE + player.getDisplayName() + ": " + ChatColor.GRAY + message, "se.staffchat");
                    c.setCancelled(true);
                }
            } else {
                Bukkit.broadcast(ChatColor.translateAlternateColorCodes('&', "&d(&5&lStaff&d) ") + ChatColor.LIGHT_PURPLE + player.getDisplayName() + ": " + ChatColor.GRAY + message, "se.staffchat");
                c.setCancelled(true);
            }
        }
    }
}
