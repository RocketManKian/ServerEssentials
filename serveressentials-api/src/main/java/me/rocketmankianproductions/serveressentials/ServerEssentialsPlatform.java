package me.rocketmankianproductions.serveressentials;

import org.bukkit.entity.Player;

public interface ServerEssentialsPlatform {
    boolean isAFK(Player player);
    void setAFK(Player player, boolean afk);
    String getNickname(Player player);
    long getLastActivity(Player player);
    boolean isJailed(Player player);
    boolean setJailed(Player player, String jailName, int durationSeconds, String durationUnconverted, String reason);
    boolean releasePlayer(Player player);
}