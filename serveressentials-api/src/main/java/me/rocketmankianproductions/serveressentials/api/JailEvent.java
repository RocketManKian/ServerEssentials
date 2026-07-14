package me.rocketmankianproductions.serveressentials.api;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class JailEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final String jailName;
    private final int duration;
    private final String reason;

    private boolean cancelled;

    public JailEvent(Player player, String jailName, int duration, String reason) {
        this.player = player;
        this.jailName = jailName;
        this.duration = duration;
        this.reason = reason;
    }

    public Player getPlayer() { return player; }
    public String getJailName() { return jailName; }
    public int getDuration() { return duration; }
    public String getReason() { return reason; }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}