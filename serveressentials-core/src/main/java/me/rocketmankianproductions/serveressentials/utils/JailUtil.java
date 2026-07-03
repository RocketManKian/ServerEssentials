package me.rocketmankianproductions.serveressentials.utils;

import org.bukkit.Location;

public class JailUtil {

    private final String name;
    private Location location;

    public JailUtil(String name, Location location) {
        this.name = name;
        this.location = location;
    }

    public String getName() { return name; }
    public Location getLocation() { return location; }

    public void setLocation(Location location) { this.location = location; }
}