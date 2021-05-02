package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public class Feed implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;
            Player target;
            if (player.hasPermission("se.feed")) {
                if (args.length <= 1) {
                    if (args.length == 1) {
                        target = Bukkit.getPlayer(args[0]);
                        if (target == null) {
                            sender.sendMessage(ChatColor.RED + "Cannot find player " + args[0] + ".");
                            return true;
                        } else if (target != null) {
                            if (target != player) {
                                target = Bukkit.getServer().getPlayer(args[0]);
                                target.setFoodLevel(20);
                                sender.sendMessage(ChatColor.GREEN + "You have fed " + target.getName() + ".");
                                target.sendMessage(ChatColor.GREEN + "You have been fed by " + sender.getName() + ".");
                                return true;
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + "Player doesn't exist.");
                        }
                    } else if (args.length == 0) {
                        try {
                            sender.sendMessage(ChatColor.GREEN + "You have fed yourself");
                            target = Bukkit.getServer().getPlayer(sender.getName());
                            target.setFoodLevel(20);
                            return true;
                        } catch (ArrayIndexOutOfBoundsException a) {
                            return true;
                        }
                    }
                } else if (args.length > 1) {
                    player.sendMessage(ChatColor.RED + "Incorrect format! Use /feed (name)");
                    return true;
                }
            }else{
                if (ServerEssentials.plugin.getConfig().getString("no-permission-message").length() == 0){
                    player.sendMessage(ChatColor.RED + "You do not have the required permission (se.feed) to run this command.");
                    return true;
                }else{
                    String permission = ServerEssentials.getPlugin().getConfig().getString("no-permission-message");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', permission));
                }
                return true;
            }
        }else if (sender instanceof ConsoleCommandSender){
            if (args.length == 1){
                Player target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    System.out.println(ChatColor.RED + "Player doesn't exist.");
                    return true;
                } else {
                    target = Bukkit.getServer().getPlayer(args[0]);
                    target.setFoodLevel(20);
                    System.out.println(ChatColor.GREEN + "You have fed " + target.getName() + ".");
                    target.sendMessage(ChatColor.GREEN + "You have been fed by " + ChatColor.WHITE + "Console.");
                    return true;
                }
            }
        }
        return false;
    }
}
