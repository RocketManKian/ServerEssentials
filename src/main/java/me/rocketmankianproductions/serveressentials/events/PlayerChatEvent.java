package me.rocketmankianproductions.serveressentials.events;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;
import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.commands.StaffChat;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChatEvent implements Listener {

    String chatcolour = ServerEssentials.getPlugin().getConfig().getString("chat-colour");

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent c) {
        Player player = c.getPlayer();
        if (StaffChat.staffchat.contains(player)) {
            String scmessage = c.getMessage();
            if (ServerEssentials.isConnectedToDiscordSRV && ServerEssentials.getPlugin().getConfig().getBoolean("enable-discord-integration") == true) {
                TextChannel textChannel = DiscordSRV.getPlugin().getDestinationTextChannelForGameChannelName("staff-chat");
                if (textChannel != null) {
                    String player1 = player.getDisplayName();
                    player1 = ChatColor.stripColor(player1);
                    textChannel.sendMessage("**" + player1 + "** » " + scmessage).queue();
                    Bukkit.broadcast(ChatColor.translateAlternateColorCodes('&', "&d(&5&lStaff&d) ") + ChatColor.LIGHT_PURPLE + player.getName() + ": " + ChatColor.GRAY + scmessage, "se.staffchat");
                    c.setCancelled(true);
                }
            } else {
                Bukkit.broadcast(ChatColor.translateAlternateColorCodes('&', "&d(&5&lStaff&d) ") + ChatColor.LIGHT_PURPLE + player.getName() + ": " + ChatColor.GRAY + scmessage, "se.staffchat");
                c.setCancelled(true);
            }
        }
        if (ServerEssentials.isConnectedToLuckPerms) {
            if (ServerEssentials.plugin.getConfig().getString("chat-colour").length() == 2) {
                if (LuckPermsProvider.get().getUserManager().getUser(player.getUniqueId()).getCachedData().getMetaData().getPrefix() != null && LuckPermsProvider.get().getUserManager().getUser(player.getUniqueId()).getCachedData().getMetaData().getSuffix() != null) {
                    String prefix = LuckPermsProvider.get().getUserManager().getUser(player.getUniqueId()).getCachedData().getMetaData().getPrefix();
                    prefix = ChatColor.translateAlternateColorCodes('&', prefix);
                    String suffix = LuckPermsProvider.get().getUserManager().getUser(player.getUniqueId()).getCachedData().getMetaData().getSuffix();
                    suffix = ChatColor.translateAlternateColorCodes('&', suffix);
                    String message = c.getMessage();
                    message = ChatColor.translateAlternateColorCodes('&', chatcolour + message);
                    String playername = player.getDisplayName();
                    c.setFormat("<" + prefix + playername + suffix + "> " + message);
                } else if (LuckPermsProvider.get().getUserManager().getUser(player.getUniqueId()).getCachedData().getMetaData().getPrefix() != null) {
                    String prefix = LuckPermsProvider.get().getUserManager().getUser(player.getUniqueId()).getCachedData().getMetaData().getPrefix();
                    prefix = ChatColor.translateAlternateColorCodes('&', prefix);
                    String message = c.getMessage();
                    message = ChatColor.translateAlternateColorCodes('&', chatcolour + message);
                    String playername = player.getDisplayName();
                    c.setFormat("<" + prefix + playername + "> " + message);
                } else if (LuckPermsProvider.get().getUserManager().getUser(player.getUniqueId()).getCachedData().getMetaData().getSuffix() != null) {
                    String suffix = LuckPermsProvider.get().getUserManager().getUser(player.getUniqueId()).getCachedData().getMetaData().getSuffix();
                    suffix = ChatColor.translateAlternateColorCodes('&', suffix);
                    String message = c.getMessage();
                    message = ChatColor.translateAlternateColorCodes('&', chatcolour + message);
                    String playername = player.getDisplayName();
                    c.setFormat("<" + playername + suffix + "> " + message);
                }
            }else {
                String message = c.getMessage();
                message = ChatColor.translateAlternateColorCodes('&', chatcolour + message);
                String playername = player.getDisplayName();
                c.setFormat("<" + playername + message + "> ");
            }
        }else {
            if (ServerEssentials.plugin.getConfig().getString("chat-colour").length() == 2) {
                String message = c.getMessage();
                message = ChatColor.translateAlternateColorCodes('&', chatcolour + message);
                String playername = player.getDisplayName();
                c.setFormat("<" + playername + message + "> ");
            }
        }
    }
}
