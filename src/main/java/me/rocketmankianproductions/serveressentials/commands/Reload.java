package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
import me.rocketmankianproductions.serveressentials.tasks.Broadcast;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;

public class Reload {

    public static ServerEssentials plugin;

    public Reload(ServerEssentials plugin) {
        this.plugin = plugin;
    }


    public void run(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("se.reload") || player.hasPermission("se.all")) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatColor.GREEN + "Server Essentials has been reloaded!"));
                // Reloading YML files
                ServerEssentials.getPlugin().reloadConfig();
                Rules.reload();
                Setspawn.reload();
                SilentJoin.reload();
                Sethome.reload();
                TPToggle.reload();
                MsgToggle.reload();
                Setwarp.reload();
                Lang.reload();
                // Applying Broadcast Changes
                ServerEssentials.broadcastLoop.cancel();
                Long delay = ServerEssentials.getPlugin().getConfig().getLong("broadcast-delay");
                ServerEssentials.broadcastLoop = new Broadcast(ServerEssentials.plugin).runTaskTimer(ServerEssentials.plugin, delay, delay);
                HandlerList.unregisterAll((Plugin) ServerEssentials.plugin);
                ServerEssentials.plugin.registerEvents();
            } else {
                String perm = Lang.fileConfig.getString("no-permission-message").replace("<permission>", "se.reload");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', perm));
            }
        } else if (sender instanceof ConsoleCommandSender) {
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', ChatColor.GREEN + "Server Essentials has been reloaded!"));
            ServerEssentials.getPlugin().reloadConfig();
            Rules.reload();
            Setspawn.reload();
            SilentJoin.reload();
            Sethome.reload();
            TPToggle.reload();
            MsgToggle.reload();
            Setwarp.reload();
            Lang.reload();
            ServerEssentials.broadcastLoop.cancel();
            Long delay = ServerEssentials.getPlugin().getConfig().getLong("broadcast-delay");
            ServerEssentials.broadcastLoop = new Broadcast(ServerEssentials.plugin).runTaskTimer(ServerEssentials.plugin, delay, delay);
            HandlerList.unregisterAll((Plugin) ServerEssentials.plugin);
            ServerEssentials.plugin.registerEvents();
        }
    }
}