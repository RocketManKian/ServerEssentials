package me.rocketmankianproductions.serveressentials.utils;

import org.bukkit.Location;

import java.util.UUID;

public class JailPlayerUtil {

    private final UUID playerId;
    private final String jailName;
    private final Location previousLocation;
    private long releaseTime; // epoch millis
    private String reason;

    public JailPlayerUtil(UUID playerId, String jailName, Location previousLocation, long releaseTime, String reason) {
        this.playerId = playerId;
        this.jailName = jailName;
        this.previousLocation = previousLocation;
        this.releaseTime = releaseTime;
        this.reason = reason;
    }

    public UUID getPlayerId() { return playerId; }
    public String getJailName() { return jailName; }
    public Location getPlayerPreviousLocation() { return previousLocation; }
    public long getReleaseTime() { return releaseTime; }

    public void setReleaseTime(long releaseTime) {
        this.releaseTime = releaseTime;
    }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public boolean isExpired() {
        return System.currentTimeMillis() >= releaseTime;
    }
}