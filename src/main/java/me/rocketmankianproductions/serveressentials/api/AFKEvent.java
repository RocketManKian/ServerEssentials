package me.rocketmankianproductions.serveressentials.api;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class AFKEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final boolean afk;

    public AFKEvent(Player player, boolean afk) {
        this.player = player;
        this.afk = afk;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isAfk() {
        return afk;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}