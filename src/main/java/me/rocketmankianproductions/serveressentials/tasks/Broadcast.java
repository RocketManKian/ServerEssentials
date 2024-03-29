package me.rocketmankianproductions.serveressentials.tasks;

import me.clip.placeholderapi.PlaceholderAPI;
import me.rocketmankianproductions.serveressentials.ServerEssentials;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Broadcast extends BukkitRunnable {
    FileConfiguration config = ServerEssentials.plugin.getConfig();
    int counter = 0;
    private Player player;
    private final ServerEssentials plugin;

    public Broadcast(ServerEssentials plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        if (plugin.getConfig().getBoolean("broadcast")){
            int minplayers = ServerEssentials.getPlugin().getConfig().getInt("broadcast-min-players");
            if (Bukkit.getOnlinePlayers().size() >= minplayers) {
                String prefix = plugin.getConfig().getString("prefix");
                List<String> messages = (plugin.getConfig().getStringList("broadcast-messages"));
                if (ServerEssentials.isConnectedToPlaceholderAPI) {
                    @NotNull List<String> placeholder = PlaceholderAPI.setPlaceholders(player, messages);
                    for (Player player : Bukkit.getOnlinePlayers()){
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " " + ChatColor.WHITE + placeholder.get(counter)));
                    }
                } else {
                    for (Player player : Bukkit.getOnlinePlayers()){
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " " + ChatColor.WHITE + messages.get(counter)));
                    }
                }
                counter = counter + 1;
                if (counter == messages.size()) {
                    counter = 0;
                }
            }
        }
    }
}