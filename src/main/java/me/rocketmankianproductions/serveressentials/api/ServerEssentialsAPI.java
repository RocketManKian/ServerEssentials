package me.rocketmankianproductions.serveressentials.api;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.utils.AFKManager;
import me.rocketmankianproductions.serveressentials.utils.JailManagerService;
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

    /**
     * Returns whether a player is Jailed.
     */
    public static boolean isJailed(Player player) {
        return ServerEssentials.getInstance.jailManager.isJailed(player);
    }

    /**
     * Jails a player
     */

    public static boolean setJailed(Player player, String jailName, int durationSeconds) {
        if (ServerEssentials.getInstance.jailManager.getJail(jailName) == null){
            return false;
        }
        ServerEssentials.getInstance.jailManager.jailPlayer(player, jailName, durationSeconds);
        return true;
    }

    /**
     * Releases a player from Jail
     */

    public static boolean releasePlayer(Player player) {
        if (!ServerEssentials.getInstance.jailManager.isJailed(player)){
            return false;
        }
        ServerEssentials.getInstance.jailManager.releasePlayer(player.getUniqueId());
        return true;
    }
}