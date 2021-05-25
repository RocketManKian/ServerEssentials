package me.rocketmankianproductions.serveressentials.events;

import me.clip.placeholderapi.PlaceholderAPI;
import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.UpdateChecker.Update;
import me.rocketmankianproductions.serveressentials.commands.*;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.IOException;

public class PlayerJoinListener implements Listener {
    Location loc;
    public static ServerEssentials plugin;

    @EventHandler
    void onPlayerJoin(PlayerJoinEvent pj) {
        Player player = pj.getPlayer();

        // Checking if the player is op and if the plugin has an update
        if ((player.isOp() || player.hasPermission("se.alert")) && ServerEssentials.getPlugin().hasUpdate()) {
            new Update(ServerEssentials.getPlugin(), 86675).getLatestVersion(version -> {
                player.sendMessage(ChatColor.RED + "Server Essentials Has An Update." + ChatColor.GREEN + "\nNew Version: " + version);
            });
        }

        // Sets default value if player has the permission.
        if (player.hasPermission("se.silentjoin")) {
            boolean b = SilentJoin.fileConfig.getBoolean("silent." + player.getName(), false);
            SilentJoin.fileConfig.set("silent." + player.getName(), b);
            try {
                SilentJoin.fileConfig.save(SilentJoin.file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Decide if Join Message gets posted or not.
        if (player.hasPlayedBefore()) {
            if (ServerEssentials.getPlugin().getConfig().getBoolean("enable-join-message")) {
                if (SilentJoin.fileConfig.getBoolean("silent." + player.getName(), false) == false) {
                    String jm = ServerEssentials.getPlugin().getConfig().getString("join-symbol");
                    pj.setJoinMessage(ChatColor.translateAlternateColorCodes('&', jm + " " + player.getDisplayName()));
                } else {
                    pj.setJoinMessage("");
                }
            } else {
                if (SilentJoin.fileConfig.getBoolean("silent." + player.getName(), true) == true) {
                    pj.setJoinMessage("");
                } else {
                    // Default Join Message
                }
            }
        } else if (!player.hasPlayedBefore()) {
            if (ServerEssentials.getPlugin().getConfig().getBoolean("enable-first-time-join-message")) {
                String wm = ServerEssentials.getPlugin().getConfig().getString("first-time-join");
                if (ServerEssentials.isConnectedToPlaceholderAPI) {
                    String placeholder = PlaceholderAPI.setPlaceholders(player, wm);
                    pj.setJoinMessage(ChatColor.translateAlternateColorCodes('&', placeholder));
                } else {
                    pj.setJoinMessage(ChatColor.translateAlternateColorCodes('&', wm));
                }
            } else {
                // Default Message
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

        if (player.hasPermission("se.vanish")) {
            if (ServerEssentials.getPlugin().getConfig().getBoolean("vanish-on-join")) {
                for (Player people : Bukkit.getOnlinePlayers()) {
                    people.hidePlayer(ServerEssentials.getPlugin(), player);
                }
                ServerEssentials.getPlugin().invisible_list.add(player);
                player.sendMessage(ChatColor.GREEN + "You are now invisible!");
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
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    }
                } else if (ServerEssentials.isConnectedToPlaceholderAPI && ServerEssentials.plugin.getConfig().getBoolean("enable-motd")){
                    if (ServerEssentials.plugin.getConfig().getBoolean("enable-motd")) {
                        for (String msg : ServerEssentials.plugin.getConfig().getStringList("motd-message")) {
                            String placeholder = PlaceholderAPI.setPlaceholders(player, msg);
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', placeholder));
                        }
                    }
                }
            }
        }, delay2);
    }
}