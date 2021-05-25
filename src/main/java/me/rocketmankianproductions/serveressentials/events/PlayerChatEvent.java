package me.rocketmankianproductions.serveressentials.events;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.api.ListenerPriority;
import github.scarsz.discordsrv.api.Subscribe;
import github.scarsz.discordsrv.api.events.DiscordGuildMessagePreProcessEvent;
import github.scarsz.discordsrv.api.events.DiscordGuildMessageSentEvent;
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

    private final ServerEssentials plugin;

    public PlayerChatEvent(ServerEssentials plugin) {
        this.plugin = plugin;
    }

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

    @Subscribe(priority = ListenerPriority.MONITOR)
    public void discordMessageReceived(DiscordGuildMessagePreProcessEvent event) {
        TextChannel textChannel = DiscordSRV.getPlugin().getDestinationTextChannelForGameChannelName("staffchat");
        if (textChannel == null || ServerEssentials.getPlugin().getConfig().getBoolean("enable-discord-integration") == false){
            if (event.getMessage().getChannel().equals(textChannel)){
                textChannel.sendMessage("Discord Integration is disabled, or the channel is invalid!").queue();
            }
        }else{
            if (event.getMessage().getChannel().equals(textChannel)) {
                String message = event.getMessage().getContentDisplay();
                String name = event.getMessage().getAuthor().getName();
                Bukkit.broadcast(ChatColor.translateAlternateColorCodes('&', "&b&lDiscord" + " &f» " + "&d(&5&lStaff&d) " + ChatColor.LIGHT_PURPLE + name + "&f: " + ChatColor.GRAY + message), "se.staffchat");
            }
        }
    }
}
