package me.rocketmankianproductions.serveressentials;

import org.bukkit.entity.Player;

public final class ServerEssentialsAPI {

    private static ServerEssentialsPlatform instance = null;

    private ServerEssentialsAPI() {
        // Prevent instantiation
    }

    public static ServerEssentialsPlatform getImplementation() {
        return instance;
    }

    // INTERNAL USE ONLY: Core plugin will call this on startup
    public static void setImplementation(ServerEssentialsPlatform platform) {
        instance = platform;
    }

    public static boolean isAFK(Player player) {
        if (instance == null) throw new IllegalStateException("API is not initialized yet!");
        return instance.isAFK(player);
    }

    public static void setAFK(Player player, boolean afk) {
        if (instance == null) throw new IllegalStateException("API is not initialized yet!");
        instance.setAFK(player, afk);
    }

    public static String getNickname(Player player) {
        if (instance == null) throw new IllegalStateException("API is not initialized yet!");
        return instance.getNickname(player);
    }

    public static long getLastActivity(Player player) {
        if (instance == null) throw new IllegalStateException("API is not initialized yet!");
        return instance.getLastActivity(player);
    }

    public static boolean isJailed(Player player) {
        if (instance == null) throw new IllegalStateException("API is not initialized yet!");
        return instance.isJailed(player);
    }

    public static boolean setJailed(Player player, String jailName, int durationSeconds, String durationUnconverted, String reason) {
        if (instance == null) throw new IllegalStateException("API is not initialized yet!");
        return instance.setJailed(player, jailName, durationSeconds, durationUnconverted, reason);
    }

    public static boolean releasePlayer(Player player) {
        if (instance == null) throw new IllegalStateException("API is not initialized yet!");
        return instance.releasePlayer(player);
    }
}