package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static me.rocketmankianproductions.serveressentials.ServerEssentials.hex;

public class Fly implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (ServerEssentials.permissionChecker(player, "se.fly")) {
                if (args.length == 0) {
                    if (blacklistCheck(player)){
                        String msg = Lang.fileConfig.getString("fly-blacklisted-world");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                        return false;
                    }
                    flyToggle(player, false, null);
                } else if (args.length == 1) {
                    if (ServerEssentials.permissionChecker(player, "se.fly.others")){
                        Player targetPlayer = Bukkit.getServer().getPlayer(args[0]);
                        if (blacklistCheck(player)){
                            String msg = Lang.fileConfig.getString("fly-blacklisted-world");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                            return false;
                        }
                        flyToggle(targetPlayer, true, player);
                    }
                } else {
                    String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/fly (player)");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
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
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                if (target){
                    String msg2 = Lang.fileConfig.getString("fly-target-disabled").replace("<target>", player.getName());
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg2)));
                }
                player.setAllowFlight(false);
            } else {
                player.setFlying(false);
                String msg = Lang.fileConfig.getString("fly-enabled");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                player.setAllowFlight(true);
                if (target){
                    String msg2 = Lang.fileConfig.getString("fly-target-enabled").replace("<target>", player.getName());
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg2)));
                }
            }
        }else{
            String msg = Lang.fileConfig.getString("target-offline");
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
        }
    }

    public static Boolean blacklistCheck(Player player){
        boolean blacklistedworld = false;
        if (ServerEssentials.plugin.getConfig().getBoolean("enable-fly-blacklist")){
            for (String worlds : ServerEssentials.plugin.getConfig().getStringList("fly-blacklist")) {
                if (player.getWorld().getName().equalsIgnoreCase(worlds)) {
                    blacklistedworld = true;
                }
            }
        }
        return blacklistedworld;
    }
}
