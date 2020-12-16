package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Playtime implements CommandExecutor {

    FileConfiguration config = ServerEssentials.getPlugin().getConfig();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if (!ServerEssentials.getPlugin().getConfig().getBoolean("enable-playtime-command"))
            player.sendMessage(ChatColor.RED + "Command is disabled. Please contact an Administrator.");
        else if (player.hasPermission("se.playtime")) {
            String prefix = ServerEssentials.getPlugin().getConfig().getString("prefix");
            int ticks = player.getStatistic(Statistic.PLAY_ONE_MINUTE);
            int tick = ticks;
            int days = ticks / (20*3600*24);
            int rest = 0;
            int hours = tick / (20*3600);
            rest = tick % (20*3600);
            int minutes = rest / (20*60);
            rest = rest % (20*60);
            int seconds = rest / 20;
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatColor.GOLD + "You have played for " + days + ChatColor.GOLD + " Days " + hours + ChatColor.GOLD + " Hours " + minutes + ChatColor.GOLD + " Minutes " + seconds + ChatColor.GOLD + " Seconds"));
        } else {
            player.sendMessage(ChatColor.RED + "You do not have the required permission for (se.playtime) to run this command.");
        }
        return false;
    }
}
