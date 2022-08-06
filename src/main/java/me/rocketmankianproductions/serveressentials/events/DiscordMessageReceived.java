package me.rocketmankianproductions.serveressentials.events;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.api.ListenerPriority;
import github.scarsz.discordsrv.api.Subscribe;
import github.scarsz.discordsrv.api.events.DiscordGuildMessagePreProcessEvent;
import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;
import me.rocketmankianproductions.serveressentials.ServerEssentials;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;


public class DiscordMessageReceived implements Listener {

    private final ServerEssentials plugin;

    public DiscordMessageReceived(ServerEssentials plugin) {
        this.plugin = plugin;
    }

    @Subscribe(priority = ListenerPriority.MONITOR)
    public void discordMessageReceived(DiscordGuildMessagePreProcessEvent event) {
        String channelname = ServerEssentials.getPlugin().getConfig().getString("staff-chat-channel-name");
        TextChannel textChannel = DiscordSRV.getPlugin().getDestinationTextChannelForGameChannelName(channelname);
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