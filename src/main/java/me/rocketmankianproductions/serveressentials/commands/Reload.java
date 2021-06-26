package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.tasks.Broadcast;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class Reload {

    public static BukkitTask broadcastLoop;
    FileConfiguration config = ServerEssentials.getPlugin().getConfig();
    public static ServerEssentials plugin;

    public Reload(ServerEssentials plugin) {
        this.plugin = plugin;
    }


    public void run(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("se.reload")) {
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
                // Applying Broadcast Changes
                ServerEssentials.broadcastLoop.cancel();
                Long delay = ServerEssentials.getPlugin().getConfig().getLong("broadcast-delay");
                ServerEssentials.broadcastLoop = new Broadcast(ServerEssentials.plugin).runTaskTimer(ServerEssentials.plugin, delay, delay);
            } else {
                if (ServerEssentials.plugin.getConfig().getString("no-permission-message").length() == 0){
                    player.sendMessage(ChatColor.RED + "You do not have the required permission (se.reload) to run this command.");
                }else{
                    String permission = ServerEssentials.getPlugin().getConfig().getString("no-permission-message");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', permission));
                }
            }
        } else if (sender instanceof ConsoleCommandSender) {
            ServerEssentials.getPlugin().getLogger().info(ChatColor.translateAlternateColorCodes('&', ChatColor.GREEN + "Server Essentials has been reloaded!"));
            ServerEssentials.getPlugin().reloadConfig();
            Rules.reload();
            Setspawn.reload();
            SilentJoin.reload();
            Sethome.reload();
            TPToggle.reload();
            ServerEssentials.broadcastLoop.cancel();
            Long delay = ServerEssentials.getPlugin().getConfig().getLong("broadcast-delay");
            ServerEssentials.broadcastLoop = new Broadcast(ServerEssentials.plugin).runTaskTimer(ServerEssentials.plugin, delay, delay);
        }
    }
}