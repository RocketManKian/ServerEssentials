package me.rocketmankianproductions.serveressentials.events;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;
import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.commands.StaffChat;
import me.rocketmankianproductions.serveressentials.commands.StaffChatBungee;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatEventBungee implements Listener {

    String chatcolour = ServerEssentials.getPlugin().getConfig().getString("chat-colour");

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent c) {
        Player player = c.getPlayer();
        if (StaffChat.staffchat.contains(player)) {
            String scmessage = c.getMessage();
            if (ServerEssentials.isBungeeCordEnabled) {
                if (ServerEssentials.isConnectedToDiscordSRV && ServerEssentials.getPlugin().getConfig().getBoolean("enable-discord-integration") == true) {
                    String channel = ServerEssentials.getPlugin().getConfig().getString("staff-chat-channel-name");
                    TextChannel textChannel = DiscordSRV.getPlugin().getDestinationTextChannelForGameChannelName(channel);
                    if (textChannel != null) {
                        String player1 = player.getDisplayName();
                        player1 = ChatColor.stripColor(player1);
                        textChannel.sendMessage("**" + player1 + "** » " + scmessage).queue();
                        StaffChatBungee.msg = scmessage;
                        StaffChatBungee.getServer();
                        c.setCancelled(true);
                    } else {
                        StaffChatBungee.msg = scmessage;
                        StaffChatBungee.getServer();
                        c.setCancelled(true);
                    }
                } else {
                    StaffChatBungee.msg = scmessage;
                    StaffChatBungee.getServer();
                    c.setCancelled(true);
                }
            } else {
                if (ServerEssentials.isConnectedToDiscordSRV && ServerEssentials.getPlugin().getConfig().getBoolean("enable-discord-integration") == true) {
                    String channel = ServerEssentials.getPlugin().getConfig().getString("staff-chat-channel-name");
                    TextChannel textChannel = DiscordSRV.getPlugin().getDestinationTextChannelForGameChannelName(channel);
                    if (textChannel != null) {
                        String player1 = player.getDisplayName();
                        player1 = ChatColor.stripColor(player1);
                        textChannel.sendMessage("**" + player1 + "** » " + scmessage).queue();
                        Bukkit.broadcast(ChatColor.translateAlternateColorCodes('&', "&d(&5&lStaff&d) ") + ChatColor.LIGHT_PURPLE + player.getName() + ": " + ChatColor.GRAY + scmessage, "se.staffchat");
                        c.setCancelled(true);
                    } else {
                        Bukkit.broadcast(ChatColor.translateAlternateColorCodes('&', "&d(&5&lStaff&d) ") + ChatColor.LIGHT_PURPLE + player.getName() + ": " + ChatColor.GRAY + scmessage, "se.staffchat");
                        c.setCancelled(true);
                    }
                } else {
                    Bukkit.broadcast(ChatColor.translateAlternateColorCodes('&', "&d(&5&lStaff&d) ") + ChatColor.LIGHT_PURPLE + player.getName() + ": " + ChatColor.GRAY + scmessage, "se.staffchat");
                    c.setCancelled(true);
                }
            }
        }
        if (ServerEssentials.plugin.getConfig().getBoolean("chat-features")) {
            if (ServerEssentials.plugin.getConfig().getString("chat-colour").length() == 2) {
                String message = c.getMessage();
                message = ChatColor.translateAlternateColorCodes('&', chatcolour + message);
                String playername = player.getDisplayName();
                c.setFormat("<" + playername + "> " + message);
            }
            if (ServerEssentials.plugin.getConfig().getString("chat-format").length() != 0) {
                String message = c.getMessage();
                String format = ServerEssentials.getPlugin().getConfig().getString("chat-format");
                format = format.replace("%playername%", player.getDisplayName());
                if (ServerEssentials.plugin.getConfig().getString("chat-colour").length() == 2) {
                    message = ChatColor.translateAlternateColorCodes('&', chatcolour + message);
                    c.setFormat(format + message);
                }
                c.setFormat(format + message);
            }
        }
    }
}
