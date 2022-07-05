package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
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
        if (sender instanceof Player){
            Player player = (Player) sender;
            // Otherwise checking if the player has the correct permission
            if (ServerEssentials.permissionChecker(player, "se.playtime")) {
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
                            String msg = Lang.fileConfig.getString("playtime-target").replace("<target>", target.getName()).replace("<days>", String.valueOf(days)).replace("<hours>", String.valueOf(hours)).replace("<minutes>", String.valueOf(minutes)).replace("<seconds>", String.valueOf(seconds));
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                            return true;
                        }else{
                            player.sendMessage("Incorrect Format! Please use /playtime");
                            return true;
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
                        String msg = Lang.fileConfig.getString("playtime-target").replace("<target>", target.getName()).replace("<days>", String.valueOf(days)).replace("<hours>", String.valueOf(hours)).replace("<minutes>", String.valueOf(minutes)).replace("<seconds>", String.valueOf(seconds));
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
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
                    String msg = Lang.fileConfig.getString("playtime-self").replace("<days>", String.valueOf(days)).replace("<hours>", String.valueOf(hours)).replace("<minutes>", String.valueOf(minutes)).replace("<seconds>", String.valueOf(seconds));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    return true;
                }else{
                    String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/playtime (player)");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    return true;
                }
            }
        }else{
            String console = Lang.fileConfig.getString("console-invalid");
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', console));
            return true;
        }
        return false;
    }
}
