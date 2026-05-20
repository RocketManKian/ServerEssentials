package me.rocketmankianproductions.serveressentials.api;

import me.rocketmankianproductions.serveressentials.utils.AFKManager;
import org.bukkit.entity.Player;

public final class ServerEssentialsAPI {

    private ServerEssentialsAPI() {
        // Prevent instantiation
    }

    /**
     * Returns whether a player is AFK.
     */
    public static boolean isAFK(Player player) {
        return AFKManager.isAFK(player);
    }

    /**
     * Sets a player's AFK status.
     */
    public static void setAFK(Player player, boolean afk) {
        AFKManager.setAFK(player, afk);
    }

    /**
     * Returns the player's last activity timestamp.
     */
    public static long getLastActivity(Player player) {
        return AFKManager.getLastActivity(player);
    }
}