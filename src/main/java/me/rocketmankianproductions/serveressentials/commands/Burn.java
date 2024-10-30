package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static me.rocketmankianproductions.serveressentials.ServerEssentials.hex;

public class Burn implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (commandSender instanceof Player){
            Player player = (Player) commandSender;
            if (ServerEssentials.permissionChecker(player, "se.burn")){
                if (args.length == 2){
                    OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
                    try{
                        int ticks = Integer.parseInt(args[1]);
                        if (target.isOnline()){
                            if (target == player){
                                String msg = Lang.fileConfig.getString("fire-success-self").replace("<time>", args[1]);
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                                player.setFireTicks(ticks * 20);
                            }else{
                                String msg = Lang.fileConfig.getString("fire-success-target").replace("<target>", target.getName()).replace("<time>", args[1]);
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                                String msg2 = Lang.fileConfig.getString("fire-receive-target").replace("<player>", player.getName()).replace("<time>", args[1]);
                                target.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg2)));
                                target.getPlayer().setFireTicks(ticks * 20);
                                return true;
                            }
                        }
                    }catch (NumberFormatException e){
                        commandSender.sendMessage(ChatColor.RED + "Invalid number.");
                        return false;
                    }
                }else{
                    String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/burn <player> <seconds>");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                    return true;
                }
            }
        }
        return false;
    }
}
