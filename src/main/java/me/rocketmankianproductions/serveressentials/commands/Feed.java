package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class Feed implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (ServerEssentials.permissionChecker(player, "se.feed")) {
                Player target = Bukkit.getPlayer(args[0]);
                if (args.length <= 1) {
                    if (args.length == 1) {
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
                    String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/feed (player)");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    return true;
                }
            }
        }else if (sender instanceof ConsoleCommandSender){
            if (args.length == 1){
                Player target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    String msg = Lang.fileConfig.getString("player-offline");
                    Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', msg));
                    return true;
                } else {
                    target = Bukkit.getServer().getPlayer(args[0]);
                    target.setFoodLevel(20);
                    String msg = Lang.fileConfig.getString("feed-sender-message").replace("<target>", target.getName());
                    Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', msg));
                    String msg2 = Lang.fileConfig.getString("feed-target-message").replace("<sender>", "Console");
                    target.sendMessage(ChatColor.translateAlternateColorCodes('&', msg2));
                    return true;
                }
            }
        }else if (sender instanceof BlockCommandSender){
            if (args.length == 1){
                BlockCommandSender block = (BlockCommandSender) sender;
                Player target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    String msg = Lang.fileConfig.getString("player-offline");
                    block.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    return true;
                } else {
                    target = Bukkit.getServer().getPlayer(args[0]);
                    target.setFoodLevel(20);
                    block.sendMessage("Successfully fed " + target.getName());
                    return true;
                }
            }
        }
        return false;
    }
}
