package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.tasks.Broadcast;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class Reload implements CommandExecutor {

    public static BukkitTask broadcastLoop;
    FileConfiguration config = ServerEssentials.getPlugin().getConfig();
    public static ServerEssentials plugin;


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("se.reload")) {
                String reload = config.getString("reload-command");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', reload));
                ServerEssentials.getPlugin().reloadConfig();
                Rules.reload();
                ServerEssentials.broadcastLoop.cancel();
                Long delay = ServerEssentials.getPlugin().getConfig().getLong("broadcast-delay");
                ServerEssentials.broadcastLoop = new Broadcast(ServerEssentials.plugin).runTaskTimer(ServerEssentials.plugin, delay, delay);
            } else {
                player.sendMessage(ChatColor.RED + "You do not have the required permission for (se.reload) to run this command.");
            }
        }
        else if(sender instanceof ConsoleCommandSender)
        {
            String reload = config.getString("reload-command");
            ServerEssentials.getPlugin().getLogger().info(ChatColor.translateAlternateColorCodes('&', reload));
            ServerEssentials.getPlugin().reloadConfig();
            Rules.reload();
            ServerEssentials.broadcastLoop.cancel();
            Long delay = ServerEssentials.getPlugin().getConfig().getLong("broadcast-delay");
            ServerEssentials.broadcastLoop = new Broadcast(ServerEssentials.plugin).runTaskTimer(ServerEssentials.plugin, delay, delay);
        }
        return false;
    }
}
