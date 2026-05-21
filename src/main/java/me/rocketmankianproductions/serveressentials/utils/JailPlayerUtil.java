package me.rocketmankianproductions.serveressentials.utils;

import java.util.UUID;

public class JailPlayerUtil {

    private final UUID playerId;
    private final String jailName;
    private long releaseTime; // epoch millis

    public JailPlayerUtil(UUID playerId, String jailName, long releaseTime) {
        this.playerId = playerId;
        this.jailName = jailName;
        this.releaseTime = releaseTime;
    }

    public UUID getPlayerId() { return playerId; }
    public String getJailName() { return jailName; }
    public long getReleaseTime() { return releaseTime; }

    public void setReleaseTime(long releaseTime) {
        this.releaseTime = releaseTime;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() >= releaseTime;
    }
}
