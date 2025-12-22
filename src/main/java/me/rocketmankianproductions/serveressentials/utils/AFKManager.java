package me.rocketmankianproductions.serveressentials.utils;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AFKManager {

    // AFK state
    private static final Map<UUID, Boolean> afkPlayers = new HashMap<>();

    // Last activity timestamp (ms)
    private static final Map<UUID, Long> lastActivity = new HashMap<>();

    /* ===============================
       AFK State
       =============================== */

    public static void setAFK(Player player, boolean afk) {
        afkPlayers.put(player.getUniqueId(), afk);
        updateActivity(player);
    }

    public static boolean isAFK(Player player) {
        return afkPlayers.getOrDefault(player.getUniqueId(), false);
    }

    /* ===============================
       Activity Tracking
       =============================== */

    public static void updateActivity(Player player) {
        lastActivity.put(player.getUniqueId(), System.currentTimeMillis());
    }

    public static long getLastActivity(Player player) {
        return lastActivity.getOrDefault(
                player.getUniqueId(),
                System.currentTimeMillis()
        );
    }

    /* ===============================
       Cleanup
       =============================== */

    public static void remove(Player player) {
        afkPlayers.remove(player.getUniqueId());
        lastActivity.remove(player.getUniqueId());
    }
}