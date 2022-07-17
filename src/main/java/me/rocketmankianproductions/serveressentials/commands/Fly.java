package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Fly implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (ServerEssentials.permissionChecker(player, "se.fly")) {
                if (args.length == 0) {
                    flyToggle(player, false, null);
                } else if (args.length == 1) {
                    Player targetPlayer = Bukkit.getServer().getPlayer(args[0]);
                    flyToggle(targetPlayer, true, player);
                } else {
                    String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/fly (player)");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    return true;
                }
            }
        } else if (sender instanceof ConsoleCommandSender || sender instanceof BlockCommandSender){
            if (args.length == 1) {
                Player targetPlayer = Bukkit.getServer().getPlayer(args[0]);
                flyToggle(targetPlayer, true, sender);
            }
        }
        return false;
    }
    public void flyToggle(Player player, boolean target, CommandSender sender){
        if (player != null){
            if (player.getAllowFlight() == true) {
                player.setFlying(true);
                String msg = Lang.fileConfig.getString("fly-disabled");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                if (target){
                    String msg2 = Lang.fileConfig.getString("fly-target-disabled").replace("<target>", player.getName());
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg2));
                }
                player.setAllowFlight(false);
            } else {
                player.setFlying(false);
                String msg = Lang.fileConfig.getString("fly-enabled");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                player.setAllowFlight(true);
                if (target){
                    String msg2 = Lang.fileConfig.getString("fly-target-enabled").replace("<target>", player.getName());
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg2));
                }
            }
        }else{
            String msg = Lang.fileConfig.getString("target-offline");
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
        }
    }
}
