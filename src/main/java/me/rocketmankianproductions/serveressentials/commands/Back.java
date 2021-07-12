package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

public class Back implements CommandExecutor {

    public static HashMap<UUID, Location> location = new HashMap<UUID, Location>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            if (player.hasPermission("se.back")){
                if (location.containsKey(player.getUniqueId())){
                    Boolean blacklistedworld = ServerEssentials.plugin.getConfig().getBoolean("enable-back-blacklist");
                    if (blacklistedworld){
                        for (String worlds : ServerEssentials.plugin.getConfig().getStringList("back-blacklist")){
                            if (player.getWorld().getName().equalsIgnoreCase(worlds)){
                                player.sendMessage(ChatColor.RED + "Cannot use Back Command in a Blacklisted World");
                                return true;
                            }
                        }
                        player.teleport(location.get(player.getUniqueId()));
                        player.sendMessage(ChatColor.GOLD + "Teleported to previous location");
                        return true;
                    }else{
                        player.teleport(location.get(player.getUniqueId()));
                        player.sendMessage(ChatColor.GOLD + "Teleported to previous location");
                        return true;
                    }
                }else{
                    player.sendMessage(ChatColor.RED + "You have no location to return to");
                    return true;
                }
            }else{
                if (ServerEssentials.plugin.getConfig().getString("no-permission-message").length() == 0){
                    player.sendMessage(ChatColor.RED + "You do not have the required permission (se.back) to run this command.");
                    return true;
                }else{
                    String permission = ServerEssentials.getPlugin().getConfig().getString("no-permission-message");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', permission));
                    return true;
                }
            }
        }
        return false;
    }
}
