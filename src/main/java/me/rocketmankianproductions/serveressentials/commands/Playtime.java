package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Playtime implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        // Otherwise checking if the player has the correct permission
        if (player.hasPermission("se.playtime")) {
            if (args.length == 1){
                Player target = Bukkit.getPlayerExact(args[0]);
                if (target != null){
                    if (target != player){
                        // Converting the playtime stored as 20 ticks per second into Days, Hours, Minutes and Seconds.
                        int ticks = target.getStatistic(Statistic.PLAY_ONE_MINUTE);
                        int rest = 0;
                        // Ticks divided by 20 = seconds. Seconds x 60 = Minute. Minute x 60 = Hour. Hour x 24 = Day.
                        int days = ticks / (20 * 3600 * 24);
                        rest = ticks % (20 * 3600 * 24);
                        int hours = rest / (20 * 3600);
                        rest = rest % (20 * 3600);
                        int minutes = rest / (20 * 60);
                        rest = rest % (20 * 60);
                        int seconds = rest / 20;
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatColor.WHITE + target.getName() + ChatColor.GOLD + " has played for " + days + ChatColor.GOLD + " Days " + hours + ChatColor.GOLD + " Hours " + minutes + ChatColor.GOLD + " Minutes " + seconds + ChatColor.GOLD + " Seconds"));
                        return true;
                    }else{
                        player.sendMessage("Incorrect Format! Please use /playtime");
                    }
                } else if (target == null && Bukkit.getOfflinePlayer(args[0]).hasPlayedBefore()){
                    // Converting the playtime stored as 20 ticks per second into Days, Hours, Minutes and Seconds.
                    int ticks = Bukkit.getOfflinePlayer(args[0]).getStatistic(Statistic.PLAY_ONE_MINUTE);
                    int rest = 0;
                    // Ticks divided by 20 = seconds. Seconds x 60 = Minute. Minute x 60 = Hour. Hour x 24 = Day.
                    int days = ticks / (20 * 3600 * 24);
                    rest = ticks % (20 * 3600 * 24);
                    int hours = rest / (20 * 3600);
                    rest = rest % (20 * 3600);
                    int minutes = rest / (20 * 60);
                    rest = rest % (20 * 60);
                    int seconds = rest / 20;
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatColor.WHITE + args[0] + ChatColor.GOLD + " has played for " + days + ChatColor.GOLD + " Days " + hours + ChatColor.GOLD + " Hours " + minutes + ChatColor.GOLD + " Minutes " + seconds + ChatColor.GOLD + " Seconds"));
                    return true;
                }else{
                    player.sendMessage(ChatColor.RED + "Player doesn't exist!");
                    return true;
                }
            } else if (args.length == 0) {
                // Converting the playtime stored as 20 ticks per second into Days, Hours, Minutes and Seconds.
                int ticks = player.getStatistic(Statistic.PLAY_ONE_MINUTE);
                int rest = 0;
                // Ticks divided by 20 = seconds. Seconds x 60 = Minute. Minute x 60 = Hour. Hour x 24 = Day.
                int days = ticks / (20 * 3600 * 24);
                rest = ticks % (20 * 3600 * 24);
                int hours = rest / (20 * 3600);
                rest = rest % (20 * 3600);
                int minutes = rest / (20 * 60);
                rest = rest % (20 * 60);
                int seconds = rest / 20;
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatColor.GOLD + "You have played for " + days + ChatColor.GOLD + " Days " + hours + ChatColor.GOLD + " Hours " + minutes + ChatColor.GOLD + " Minutes " + seconds + ChatColor.GOLD + " Seconds"));
                return true;
            }
        } else {
            if (ServerEssentials.plugin.getConfig().getString("no-permission-message").length() == 0){
                player.sendMessage(ChatColor.RED + "You do not have the required permission (se.playtime) to run this command.");
                return true;
            }else{
                String permission = ServerEssentials.getPlugin().getConfig().getString("no-permission-message");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', permission));
            }
            return true;
        }
        return false;
    }
}
