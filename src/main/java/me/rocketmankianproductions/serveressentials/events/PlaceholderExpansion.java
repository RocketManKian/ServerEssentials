package me.rocketmankianproductions.serveressentials.events;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class PlaceholderExpansion extends me.clip.placeholderapi.expansion.PlaceholderExpansion {

    private final ServerEssentials plugin;

    public PlaceholderExpansion(ServerEssentials plugin) {
        this.plugin = plugin;
    }

    public static boolean isafk = false;

    @Override
    public @NotNull String getIdentifier() {
        return "se";
    }

    @Override
    public @NotNull String getAuthor() {
        return "RocketManKian";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public boolean persist() {
        return true; // This is required or else PlaceholderAPI will unregister the Expansion on reload
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if(params.equalsIgnoreCase("afk")) {
            if(isafk) {
                return "yes";
            } else {
                return "no";
            }
        }

        return null; // Placeholder is unknown by the Expansion
    }
}
