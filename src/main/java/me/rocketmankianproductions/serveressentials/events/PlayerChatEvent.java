package me.rocketmankianproductions.serveressentials.events;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;
import me.rocketmankianproductions.serveressentials.LoggerMessage;
import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.commands.AFK;
import me.rocketmankianproductions.serveressentials.commands.StaffChat;
import me.rocketmankianproductions.serveressentials.file.Lang;
import me.rocketmankianproductions.serveressentials.file.UserFile;
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
        boolean isMuted = UserFile.fileConfig.getBoolean(player.getUniqueId() + ".muted");
        if (isMuted && !player.hasPermission("se.mute.bypass")){
            c.setCancelled(true);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(Lang.fileConfig.getString("mute-message"))));
        }
        // AFK Command
        if (AFK.afk.containsKey(player)){
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(Lang.fileConfig.getString("afk-inactive"))));
            player.setSleepingIgnored(false);
            AFK.afk.remove(player);
        }
        if (StaffChat.staffchat.contains(player)) {
            String scmessage = ChatColor.stripColor(c.getMessage());
            String msg = Lang.fileConfig.getString("staffchat-message").replace("<player>", player.getName()).replace("<message>", ChatColor.GRAY + scmessage);
            if (ServerEssentials.isConnectedToDiscordSRV && ServerEssentials.getPlugin().getConfig().getBoolean("enable-staff-discord-integration")) {
                String channel = ServerEssentials.getPlugin().getConfig().getString("staff-chat-channel-name");
                TextChannel textChannel = DiscordSRV.getPlugin().getDestinationTextChannelForGameChannelName(channel);
                if (textChannel != null) {
                    String player1 = player.getDisplayName();
                    player1 = ChatColor.stripColor(player1);
                    textChannel.sendMessage("**" + player1 + "** » " + scmessage).queue();
                    Bukkit.broadcast(ChatColor.translateAlternateColorCodes('&', hex(msg)), "se.staffchat");
                    c.setCancelled(true);
                }else{
                    LoggerMessage.log(LoggerMessage.LogLevel.WARNING, "Text Channel is null, cannot send StaffChat Message to specified channel.");
                    Bukkit.broadcast(ChatColor.translateAlternateColorCodes('&', hex(msg)), "se.staffchat");
                    c.setCancelled(true);
                }
            } else {
                Bukkit.broadcast(ChatColor.translateAlternateColorCodes('&', hex(msg)), "se.staffchat");
                c.setCancelled(true);
            }
        }
    }
}