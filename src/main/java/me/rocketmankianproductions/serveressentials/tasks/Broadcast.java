package me.rocketmankianproductions.serveressentials.tasks;

import me.clip.placeholderapi.PlaceholderAPI;
import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.commands.Message;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static org.bukkit.Bukkit.getServer;

public class Broadcast extends BukkitRunnable {
    ServerEssentials plugin;
    FileConfiguration config = ServerEssentials.plugin.getConfig();
    int counter = 0;
    private Player player;

    public Broadcast(ServerEssentials plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        if (config.getBoolean("broadcast")) {
            String prefix = plugin.getConfig().getString("prefix");
            List<String> messages = (plugin.getConfig().getStringList("broadcast-messages"));
            if (ServerEssentials.isConnectedToPlaceholderAPI) {
                @NotNull List<String> placeholder = PlaceholderAPI.setPlaceholders(player, messages);
                getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', prefix + " " + ChatColor.WHITE + placeholder.get(counter)));
            } else {
                getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', prefix + " " + ChatColor.WHITE + messages.get(counter)));
            }
            counter = counter + 1;
            if (counter == messages.size()) {
                counter = 0;
            }
        }
    }
}