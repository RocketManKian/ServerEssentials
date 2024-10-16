package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.UserFile;
import me.rocketmankianproductions.serveressentials.file.Lang;
import me.rocketmankianproductions.serveressentials.tasks.Broadcast;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class Reload {

    public static ServerEssentials plugin;

    public Reload(ServerEssentials plugin) {
        this.plugin = plugin;
    }


    public void run(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (ServerEssentials.permissionChecker(player, "se.reload")) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatColor.GREEN + "Server Essentials has been reloaded!"));
                // Reloading YML files
                ServerEssentials.getPlugin().reloadConfig();
                Rules.reload();
                Setspawn.reload();
                Sethome.reload();
                Setwarp.reload();
                Lang.reload();
                UserFile.reload();
                // Applying Broadcast Changes
                ServerEssentials.broadcastLoop.cancel();
                long delay = ServerEssentials.getPlugin().getConfig().getLong("broadcast-delay");
                ServerEssentials.broadcastLoop = new Broadcast(ServerEssentials.plugin).runTaskTimer(ServerEssentials.plugin, delay, delay);
            }
        } else if (sender instanceof ConsoleCommandSender) {
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', ChatColor.GREEN + "Server Essentials has been reloaded!"));
            ServerEssentials.getPlugin().reloadConfig();
            Rules.reload();
            Setspawn.reload();
            Sethome.reload();
            Setwarp.reload();
            Lang.reload();
            UserFile.reload();
            ServerEssentials.broadcastLoop.cancel();
            long delay = ServerEssentials.getPlugin().getConfig().getLong("broadcast-delay");
            ServerEssentials.broadcastLoop = new Broadcast(ServerEssentials.plugin).runTaskTimer(ServerEssentials.plugin, delay, delay);
        }
    }
}