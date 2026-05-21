package me.rocketmankianproductions.serveressentials.utils;

import me.rocketmankianproductions.serveressentials.api.AFKEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AFKManager {

    private static final Map<UUID, Boolean> afkPlayers = new HashMap<>();
    private static final Map<UUID, Long> lastActivity = new HashMap<>();

    public static void setAFK(Player player, boolean afk) {

        boolean oldState = isAFK(player);

        // Prevent duplicate events
        if (oldState == afk) {
            return;
        }

        afkPlayers.put(player.getUniqueId(), afk);
        updateActivity(player);

        Bukkit.getPluginManager().callEvent(
                new AFKEvent(player, afk)
        );
    }

    public static boolean isAFK(Player player) {
        return afkPlayers.getOrDefault(player.getUniqueId(), false);
    }

    public static void updateActivity(Player player) {
        lastActivity.put(player.getUniqueId(), System.currentTimeMillis());
    }

    public static long getLastActivity(Player player) {
        return lastActivity.getOrDefault(
                player.getUniqueId(),
                System.currentTimeMillis()
        );
    }

    public static void remove(Player player) {
        afkPlayers.remove(player.getUniqueId());
        lastActivity.remove(player.getUniqueId());
    }
}