package me.rocketmankianproductions.serveressentials.utils;

import org.bukkit.OfflinePlayer;
import java.util.HashSet;
import java.util.Set;

public class AFKManager {

    private static final Set<OfflinePlayer> afkPlayers = new HashSet<>();

    public static void setAFK(OfflinePlayer player, boolean afk) {
        if (afk) {
            afkPlayers.add(player);
        } else {
            afkPlayers.remove(player);
        }
    }

    public static boolean isAFK(OfflinePlayer player) {
        return afkPlayers.contains(player);
    }
}
