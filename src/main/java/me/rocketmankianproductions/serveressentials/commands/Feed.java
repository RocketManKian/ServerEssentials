package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

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
                            String msg = Lang.fileConfig.getString("target-offline");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                            return true;
                        } else if (target != null) {
                            if (target != player) {
                                target = Bukkit.getServer().getPlayer(args[0]);
                                target.setFoodLevel(20);
                                String msg = Lang.fileConfig.getString("feed-sender-message").replace("<target>", target.getName());
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                String msg2 = Lang.fileConfig.getString("feed-target-message".replace("<sender>", sender.getName()));
                                target.sendMessage(ChatColor.translateAlternateColorCodes('&', msg2));
                                return true;
                            }
                        }
                    } else if (args.length == 0) {
                        try {
                            String msg = Lang.fileConfig.getString("feed-self");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                            target = Bukkit.getServer().getPlayer(sender.getName());
                            target.setFoodLevel(20);
                            return true;
                        } catch (ArrayIndexOutOfBoundsException a) {
                            return true;
                        }
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "Incorrect format! Use /feed (name)");
                    return true;
                }
            }else{
                String perm = Lang.fileConfig.getString("no-permission-message").replace("<permission>", "se.feed");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', perm));
                return true;
            }
        }else if (sender instanceof ConsoleCommandSender){
            if (args.length == 1){
                Player target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    String msg = Lang.fileConfig.getString("player-offline");
                    System.out.println(ChatColor.translateAlternateColorCodes('&', msg));
                    return true;
                } else {
                    target = Bukkit.getServer().getPlayer(args[0]);
                    target.setFoodLevel(20);
                    String msg = Lang.fileConfig.getString("feed-sender-message").replace("<target>", target.getName());
                    System.out.println(ChatColor.translateAlternateColorCodes('&', msg));
                    String msg2 = Lang.fileConfig.getString("feed-target-message").replace("<sender>", "Console");
                    target.sendMessage(ChatColor.translateAlternateColorCodes('&', msg2));
                    return true;
                }
            }
        }
        return false;
    }
}
