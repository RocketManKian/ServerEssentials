package me.rocketmankianproductions.serveressentials;

import org.bukkit.entity.Player;

public final class ServerEssentialsAPI {

    private static ServerEssentialsPlatform implementation;

    private ServerEssentialsAPI() {
        // Prevent instantiation
    }

    // INTERNAL USE ONLY: Core plugin will call this on startup
    public static void setImplementation(ServerEssentialsPlatform impl) {
        implementation = impl;
    }

    public static boolean isAFK(Player player) {
        if (implementation == null) throw new IllegalStateException("API is not initialized yet!");
        return implementation.isAFK(player);
    }

    public static void setAFK(Player player, boolean afk) {
        if (implementation == null) throw new IllegalStateException("API is not initialized yet!");
        implementation.setAFK(player, afk);
    }

    public static String getNickname(Player player) {
        if (implementation == null) throw new IllegalStateException("API is not initialized yet!");
        return implementation.getNickname(player);
    }

    public static long getLastActivity(Player player) {
        if (implementation == null) throw new IllegalStateException("API is not initialized yet!");
        return implementation.getLastActivity(player);
    }

    public static boolean isJailed(Player player) {
        if (implementation == null) throw new IllegalStateException("API is not initialized yet!");
        return implementation.isJailed(player);
    }

    public static boolean setJailed(Player player, String jailName, int durationSeconds, String durationUnconverted, String reason) {
        if (implementation == null) throw new IllegalStateException("API is not initialized yet!");
        return implementation.setJailed(player, jailName, durationSeconds, durationUnconverted, reason);
    }

    public static boolean releasePlayer(Player player) {
        if (implementation == null) throw new IllegalStateException("API is not initialized yet!");
        return implementation.releasePlayer(player);
    }
}