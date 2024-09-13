package me.rocketmankianproductions.serveressentials.events;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;
import me.clip.placeholderapi.PlaceholderAPI;
import me.rocketmankianproductions.serveressentials.LoggerMessage;
import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.UpdateChecker.Update;
import me.rocketmankianproductions.serveressentials.commands.*;
import me.rocketmankianproductions.serveressentials.file.UserFile;
import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.IOException;

import static me.rocketmankianproductions.serveressentials.ServerEssentials.hex;

public class PlayerJoinListener implements Listener {
    Location loc;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent pj) {
        Player player = pj.getPlayer();

        // Economy
        if (UserFile.fileConfig.getString(String.valueOf(player.getUniqueId())) == null){
            UserFile.fileConfig.set(player.getUniqueId() + ".money", ServerEssentials.getPlugin().getConfig().getDouble("start-balance"));
            Eco.saveBalance();
        }
        ServerEssentials.getPlugin().playerBank.put(player.getUniqueId(), UserFile.fileConfig.getDouble(player.getUniqueId() + ".money"));

        // Check to see if Update Checker is enabled in Config
        if (ServerEssentials.getPlugin().getConfig().getBoolean("update-checker")){
            // Checking if the player is op and if the plugin has an update
            if ((player.isOp() || player.hasPermission("se.alert")) && ServerEssentials.getPlugin().hasUpdate()) {
                new Update(ServerEssentials.getPlugin(), 86675).getLatestVersion(version -> {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&5--------------------------------"));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7There is a new version of &6ServerEssentials &7available."));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Download on the &6Bukkit Website"));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bLatest version: " + "&a" + version + " &8| &bInstalled version: &c" + ServerEssentials.getPlugin().getDescription().getVersion()));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&5--------------------------------"));
                });
            }
        }

        // Sets default value if player has the permission.
        if (player.hasPermission("se.silentjoin")) {
            if (UserFile.fileConfig.getString("silent." + player.getName()) == null){
                boolean b = UserFile.fileConfig.getBoolean((player.getUniqueId() + ".silent"), false);
                UserFile.fileConfig.set((player.getUniqueId() + ".silent"), b);
                try {
                    UserFile.fileConfig.save(UserFile.file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // Decide if Join Message gets posted or not.
        if (player.hasPlayedBefore()) {
            if (ServerEssentials.getPlugin().getConfig().getBoolean("enable-join-message")) {
                if (!UserFile.fileConfig.getBoolean(player.getUniqueId() + ".silent")) {
                    String msg = ServerEssentials.hex(Lang.fileConfig.getString("join-symbol")).replace("<player>", player.getName());
                    if (Lang.fileConfig.getString("join-symbol").isEmpty()){
                        pj.setJoinMessage("");
                    }else{
                        if (ServerEssentials.isConnectedToPlaceholderAPI){
                            String placeholder = PlaceholderAPI.setPlaceholders(player, msg);
                            pj.setJoinMessage(ChatColor.translateAlternateColorCodes('&', hex(placeholder)));
                        }else{
                            pj.setJoinMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                        }
                    }
                } else {
                    pj.setJoinMessage("");
                }
            } else {
                if (UserFile.fileConfig.getBoolean(player.getUniqueId() + ".silent")) {
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

        // Staff
        if (ServerEssentials.getPlugin().getConfig().getBoolean("enable-staff-join-message")){
            if (player.hasPermission("se.staffchat")){
                String channel = ServerEssentials.getPlugin().getConfig().getString("staff-chat-channel-name");
                String servername = ServerEssentials.plugin.getConfig().getString("server-name");
                String server = (servername == null || servername.isEmpty()) ? " has joined the game" : " has joined the " + servername + " Server";
                if (ServerEssentials.isConnectedToDiscordSRV){
                    TextChannel textChannel = DiscordSRV.getPlugin().getDestinationTextChannelForGameChannelName(channel);
                    if (textChannel != null && ServerEssentials.plugin.getConfig().getBoolean("enable-staff-discord-integration")){
                        textChannel.sendMessage("**" + player.getName() + "**" + server).queue();
                    }
                    Bukkit.broadcast(ChatColor.translateAlternateColorCodes('&', hex(Lang.fileConfig.getString("staff-join-message").replace("<player>", player.getName()))), "se.staffchat");
                }else{
                    Bukkit.broadcast(ChatColor.translateAlternateColorCodes('&', hex(Lang.fileConfig.getString("staff-join-message").replace("<player>", player.getName()))), "se.staffchat");
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