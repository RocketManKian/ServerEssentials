package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;

public class TeleportPos implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("se.tppos")) {
                if (args.length == 3) {
                    try {
                        World myworld = player.getWorld();
                        Location yourlocation = new Location(myworld, Double.parseDouble(args[0]), Double.parseDouble(args[1]), Double.parseDouble(args[2]));
                        player.teleport(yourlocation);
                        sender.sendMessage(ChatColor.GREEN + "Teleported to chosen coordinates...");
                    } catch (NumberFormatException e) {
                        player.sendMessage(ChatColor.RED + "Please enter valid coordinates.");
                    }
                } else if (args.length == 4){
                    Player target = Bukkit.getPlayerExact(args[0]);
                    if (target == null){
                        player.sendMessage(ChatColor.RED + "Player doesn't exist");
                    }else if (args[0].equalsIgnoreCase(target.getName())){
                        try {
                            World myworld = target.getWorld();
                            Location yourlocation = new Location(myworld, Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3]));
                            target.teleport(yourlocation);
                            target.sendMessage(ChatColor.GREEN + "You have been teleported");
                            sender.sendMessage(ChatColor.GREEN + "Teleported " + target.getName() + " to chosen coordinates...");
                        } catch (NumberFormatException e) {
                            player.sendMessage(ChatColor.RED + "Please enter valid coordinates.");
                        }
                    }
                }
            } else
                // If it doesn't succeed with either then it'll send the player a required permission message
                if (ServerEssentials.plugin.getConfig().getString("no-permission-message").length() == 0){
                    player.sendMessage(ChatColor.RED + "You do not have the required permission (se.tppos) to run this command.");
                    return true;
                }else{
                    String permission = ServerEssentials.getPlugin().getConfig().getString("no-permission-message");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', permission));
                }
            return true;
        } else if (sender instanceof ConsoleCommandSender){
            if (args.length == 4){
                Player target = Bukkit.getPlayerExact(args[0]);
                if (target == null){
                    System.out.println(ChatColor.RED + "Player doesn't exist");
                }else {
                    try {
                        World myworld = target.getWorld();
                        Location yourlocation = new Location(myworld, Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3]));
                        target.teleport(yourlocation);
                        target.sendMessage(ChatColor.GREEN + "You have been teleported");
                        System.out.println(ChatColor.GREEN + "Teleported " + target.getName() + " to chosen coordinates...");
                    } catch (NumberFormatException e) {
                        System.out.println(ChatColor.RED + "Please enter valid coordinates.");
                    }
                }
            }else{
                System.out.println(ChatColor.RED + "Incorrect format! Please use /tppos (target) (x) (y) (z)");
            }
        } else if (sender instanceof BlockCommandSender){
            if (args.length == 4){
                Player target = Bukkit.getPlayer(args[0]);
                if (target == null){
                    sender.sendMessage(ChatColor.RED + "Player doesn't exist");
                }else {
                    try {
                        World myworld = target.getWorld();
                        Location yourlocation = new Location(myworld, Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3]));
                        target.teleport(yourlocation);
                    } catch (NumberFormatException e) {
                        sender.sendMessage(ChatColor.RED + "Please enter valid coordinates.");
                    }
                }
            }else{
                sender.sendMessage(ChatColor.RED + "Incorrect format! Please use /tppos (target) (x) (y) (z)");
            }
        }
        return true;
    }
}