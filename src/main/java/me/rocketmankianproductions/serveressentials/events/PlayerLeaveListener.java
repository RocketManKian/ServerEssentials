package me.rocketmankianproductions.serveressentials.events;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;
import me.clip.placeholderapi.PlaceholderAPI;
import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
import me.rocketmankianproductions.serveressentials.file.UserFile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import static me.rocketmankianproductions.serveressentials.ServerEssentials.hex;

public class PlayerLeaveListener implements Listener {

    @EventHandler
    void onPlayerLeave(PlayerQuitEvent pj) {
        Player player = pj.getPlayer();

        if (ServerEssentials.getPlugin().getConfig().getBoolean("enable-leave-message")) {
            if (!UserFile.fileConfig.getBoolean(player.getUniqueId() + ".silent")) {
                String msg = hex(Lang.fileConfig.getString("leave-symbol")).replace("<player>", player.getName());
                if (Lang.fileConfig.getString("leave-symbol").isEmpty()){
                    pj.setQuitMessage("");
                }else{
                    if (ServerEssentials.isConnectedToPlaceholderAPI) {
                        String placeholder = PlaceholderAPI.setPlaceholders(player, msg);
                        pj.setQuitMessage(ChatColor.translateAlternateColorCodes('&', hex(placeholder)));
                    }else{
                        pj.setQuitMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                    }
                }
            }else{
                pj.setQuitMessage("");
            }
        }else{
            if (UserFile.fileConfig.getBoolean(player.getUniqueId() + ".silent")){
                pj.setQuitMessage("");
            }
        }

        // Vanish
        ServerEssentials.getPlugin().invisible_list.remove(player);

        // Staff
        if (ServerEssentials.getPlugin().getConfig().getBoolean("enable-staff-leave-message")){
            if (player.hasPermission("se.staffchat")){
                String channel = ServerEssentials.getPlugin().getConfig().getString("staff-chat-channel-name");
                String servername = ServerEssentials.plugin.getConfig().getString("server-name");
                String server = (servername == null || servername.isEmpty()) ? " has quit the game" : " has quit the " + servername + " Server";
                if (ServerEssentials.isConnectedToDiscordSRV){
                    TextChannel textChannel = DiscordSRV.getPlugin().getDestinationTextChannelForGameChannelName(channel);
                    if (textChannel != null && ServerEssentials.plugin.getConfig().getBoolean("enable-staff-discord-integration")){
                        textChannel.sendMessage("**" + player.getName() + "**" + server).queue();
                    }
                    Bukkit.broadcast(ChatColor.translateAlternateColorCodes('&', hex(Lang.fileConfig.getString("staff-leave-message").replace("<player>", player.getName()))), "se.staffchat");
                }else{
                    Bukkit.broadcast(ChatColor.translateAlternateColorCodes('&', hex(Lang.fileConfig.getString("staff-leave-message").replace("<player>", player.getName()))), "se.staffchat");
                }
            }
        }
    }
}