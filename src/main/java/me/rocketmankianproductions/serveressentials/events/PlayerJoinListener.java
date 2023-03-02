package me.rocketmankianproductions.serveressentials.events;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;
import me.clip.placeholderapi.PlaceholderAPI;
import me.rocketmankianproductions.serveressentials.LoggerMessage;
import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.UpdateChecker.Update;
import me.rocketmankianproductions.serveressentials.commands.*;
import me.rocketmankianproductions.serveressentials.file.Lang;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.IOException;

import static me.rocketmankianproductions.serveressentials.ServerEssentials.hex;

public class PlayerJoinListener implements Listener {
    Location loc;
    public static ServerEssentials plugin;

    @EventHandler
    void onPlayerJoin(PlayerJoinEvent pj) {
        Player player = pj.getPlayer();

        // Check to see if Update Checker is enabled in Config
        if (ServerEssentials.getPlugin().getConfig().getBoolean("update-checker")){
            // Checking if the player is op and if the plugin has an update
            if ((player.isOp() || player.hasPermission("se.alert")) && ServerEssentials.getPlugin().hasUpdate()) {
                new Update(ServerEssentials.getPlugin(), 86675).getLatestVersion(version -> {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&5--------------------------------"));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7There is a new version of &6ServerEssentials &7available."));
                    TextComponent textComponent = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&6&lDownload"));
                    textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                            new ComponentBuilder("Click to Download").create()));
                    textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.spigotmc.org/resources/server-essentials.86675/"));
                    player.spigot().sendMessage(textComponent);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bLatest version: " + "&a" + version + " &8| &bInstalled version: &c" + ServerEssentials.getPlugin().getDescription().getVersion()));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&5--------------------------------"));
                });
            }
        }

        // Sets default value if player has the permission.
        if (player.hasPermission("se.silentjoin")) {
            if (SilentJoin.fileConfig.getString("silent." + player.getName()) == null){
                boolean b = SilentJoin.fileConfig.getBoolean("silent." + player.getName(), false);
                SilentJoin.fileConfig.set("silent." + player.getName(), b);
                try {
                    SilentJoin.fileConfig.save(SilentJoin.file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // Decide if Join Message gets posted or not.
        if (player.hasPlayedBefore()) {
            if (ServerEssentials.getPlugin().getConfig().getBoolean("enable-join-message")) {
                if (SilentJoin.fileConfig.getBoolean("silent." + player.getName()) == false) {
                    if (Lang.fileConfig.getString("join-symbol").isEmpty()){
                        pj.setJoinMessage("");
                    }else{
                        String jm = ServerEssentials.hex(Lang.fileConfig.getString("join-symbol")).replace("<player>", player.getName());
                        pj.setJoinMessage(ChatColor.translateAlternateColorCodes('&', hex(jm)));
                    }
                } else {
                    pj.setJoinMessage("");
                }
            } else {
                if (SilentJoin.fileConfig.getBoolean("silent." + player.getName()) == true) {
                    pj.setJoinMessage("");
                }
            }
        } else if (!player.hasPlayedBefore()) {
            if (ServerEssentials.getPlugin().getConfig().getBoolean("enable-first-time-join-message")) {
                String msg = Lang.fileConfig.getString("first-time-join").replace("<player>", player.getName());
                if (ServerEssentials.isConnectedToPlaceholderAPI) {
                    String placeholder = PlaceholderAPI.setPlaceholders(player, msg);
                    pj.setJoinMessage(ChatColor.translateAlternateColorCodes('&', hex(placeholder)));
                } else {
                    pj.setJoinMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                }
            }
        }
        if (!player.hasPlayedBefore() && ServerEssentials.getPlugin().getConfig().getBoolean("spawn-on-first-join")){
            // Prioritise the Newbie Spawn
            if (Setspawn.fileConfig.getString("Newbies.Location.World")!= null){
                if (Bukkit.getWorld(Setspawn.fileConfig.getString("Newbies.Location.World")) != null){
                    // Gathering Location
                    float yaw = Setspawn.fileConfig.getInt("Newbies.Location.Yaw");
                    float pitch = Setspawn.fileConfig.getInt("Newbies.Location.Pitch");
                    // Combining location data
                    loc = new Location(Bukkit.getWorld(Setspawn.fileConfig.getString("Newbies.Location.World")), Setspawn.fileConfig.getDouble("Newbies.Location.X"), Setspawn.fileConfig.getDouble("Newbies.Location.Y"), Setspawn.fileConfig.getDouble("Newbies.Location.Z"), yaw, pitch);
                    // Teleporting player to location
                    player.teleport(loc);
                }else{
                    LoggerMessage.log(LoggerMessage.LogLevel.WARNING, "Spawn World isn't loaded");
                }
            }else if (Setspawn.fileConfig.getString("Location.World") != null){
                if (Bukkit.getWorld(Setspawn.fileConfig.getString("Location.World")) != null){
                    // Gathering Location
                    float yaw = Setspawn.fileConfig.getInt("Location.Yaw");
                    float pitch = Setspawn.fileConfig.getInt("Location.Pitch");
                    // Combining location data
                    loc = new Location(Bukkit.getWorld(Setspawn.fileConfig.getString("Location.World")), Setspawn.fileConfig.getDouble("Location.X"), Setspawn.fileConfig.getDouble("Location.Y"), Setspawn.fileConfig.getDouble("Location.Z"), yaw, pitch);
                    // Teleporting player to location
                    player.teleport(loc);
                }else{
                    LoggerMessage.log(LoggerMessage.LogLevel.WARNING, "Spawn World isn't loaded");
                }
            }else{
                LoggerMessage.log(LoggerMessage.LogLevel.WARNING, "World Spawn isn't set");
            }
        }
        // Handles spawn-on-join
        if (ServerEssentials.getPlugin().getConfig().getBoolean("spawn-on-join") && Setspawn.fileConfig.getString("Location.World") != null) {
            // Gathering Location
            float yaw = Setspawn.fileConfig.getInt("Location.Yaw");
            float pitch = Setspawn.fileConfig.getInt("Location.Pitch");
            // Combining location data
            loc = new Location(Bukkit.getWorld(Setspawn.fileConfig.getString("Location.World")), Setspawn.fileConfig.getDouble("Location.X"), Setspawn.fileConfig.getDouble("Location.Y"), Setspawn.fileConfig.getDouble("Location.Z"), yaw, pitch);
            // Teleporting player to location
            player.teleport(loc);
        }

        if (ServerEssentials.getPlugin().getConfig().getBoolean("enable-staff-join-message")){
            if (player.hasPermission("se.staffchat")){
                if (!ServerEssentials.plugin.getConfig().getString("server-name").isEmpty() && ServerEssentials.isConnectedToDiscordSRV){
                    String channel = ServerEssentials.getPlugin().getConfig().getString("staff-chat-channel-name");
                    TextChannel textChannel = DiscordSRV.getPlugin().getDestinationTextChannelForGameChannelName(channel);
                    String servername = ServerEssentials.plugin.getConfig().getString("server-name");
                    if (textChannel != null && ServerEssentials.plugin.getConfig().getBoolean("enable-discord-integration")){
                        textChannel.sendMessage("**" + player.getName() + "**" + " has joined the " + servername + " Server").queue();
                    }
                    Bukkit.broadcast(ChatColor.translateAlternateColorCodes('&', Lang.fileConfig.getString("staff-join-message").replace("<player>", player.getName())), "se.staffchat");
                }else{
                    if (ServerEssentials.isConnectedToDiscordSRV){
                        String channel = ServerEssentials.getPlugin().getConfig().getString("staff-chat-channel-name");
                        TextChannel textChannel = DiscordSRV.getPlugin().getDestinationTextChannelForGameChannelName(channel);
                        if (textChannel != null && ServerEssentials.getPlugin().getConfig().getBoolean("enable-discord-integration")){
                            textChannel.sendMessage("**" + player.getName() + "**" + " has joined the game").queue();
                        }
                        Bukkit.broadcast(ChatColor.translateAlternateColorCodes('&', Lang.fileConfig.getString("staff-join-message").replace("<player>", player.getName())), "se.staffchat");
                    }else{
                        Bukkit.broadcast(ChatColor.translateAlternateColorCodes('&', Lang.fileConfig.getString("staff-join-message").replace("<player>", player.getName())), "se.staffchat");
                    }
                }
            }
        }

        if (player.hasPermission("se.vanish")) {
            if (ServerEssentials.getPlugin().getConfig().getBoolean("vanish-on-join")) {
                for (Player people : Bukkit.getOnlinePlayers()) {
                    people.hidePlayer(ServerEssentials.getPlugin(), player);
                }
                ServerEssentials.getPlugin().invisible_list.add(player);
                String msg = Lang.fileConfig.getString("vanish-enabled");
                player.sendTitle(ChatColor.translateAlternateColorCodes('&', hex(msg)), null);
            }
        }

        // Vanish
        for (int i = 0; i < ServerEssentials.plugin.invisible_list.size(); i++) {
            player.hidePlayer(ServerEssentials.plugin, ServerEssentials.plugin.invisible_list.get(i));
        }
        Long delay = ServerEssentials.getPlugin().getConfig().getLong("motd-delay");
        Long delay2 = delay * 20;
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((ServerEssentials.getPlugin()), new Runnable() {
            public void run() {
                if (!ServerEssentials.isConnectedToPlaceholderAPI && ServerEssentials.plugin.getConfig().getBoolean("enable-motd")) {
                    for (String msg : ServerEssentials.plugin.getConfig().getStringList("motd-message")) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                    }
                } else if (ServerEssentials.isConnectedToPlaceholderAPI && ServerEssentials.plugin.getConfig().getBoolean("enable-motd")){
                    if (ServerEssentials.plugin.getConfig().getBoolean("enable-motd")) {
                        for (String msg : ServerEssentials.plugin.getConfig().getStringList("motd-message")) {
                            String placeholder = PlaceholderAPI.setPlaceholders(player, msg);
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(placeholder)));
                        }
                    }
                }
            }
        }, delay2);
    }
}