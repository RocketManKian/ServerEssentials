package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TeleportPos implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("se.tppos") || player.hasPermission("se.all")) {
                if (args.length == 3) {
                    try {
                        World myworld = player.getWorld();
                        Location yourlocation = new Location(myworld, Double.parseDouble(args[0]), Double.parseDouble(args[1]), Double.parseDouble(args[2]));
                        player.teleport(yourlocation);
                        String msg = Lang.fileConfig.getString("teleport-pos-success");
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    } catch (NumberFormatException e) {
                        String msg = Lang.fileConfig.getString("teleport-pos-invalid");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    }
                } else if (args.length == 4){
                    Player target = Bukkit.getPlayerExact(args[0]);
                    if (target == null){
                        String msg = Lang.fileConfig.getString("target-offline");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    }else if (args[0].equalsIgnoreCase(target.getName())){
                        try {
                            World myworld = target.getWorld();
                            Location yourlocation = new Location(myworld, Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3]));
                            target.teleport(yourlocation);
                            String msg = Lang.fileConfig.getString("teleport-pos-target-success");
                            target.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                            String msg2 = Lang.fileConfig.getString("teleport-pos-target-message").replace("<target>", target.getName());
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg2));
                        } catch (NumberFormatException e) {
                            String msg = Lang.fileConfig.getString("teleport-pos-invalid");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        }
                    }
                }
            } else {
                String perm = Lang.fileConfig.getString("no-permission-message").replace("<permission>", "se.tppos");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', perm));
                return true;
            }
        } else if (sender instanceof ConsoleCommandSender){
            if (args.length == 4){
                Player target = Bukkit.getPlayerExact(args[0]);
                if (target == null){
                    String msg = Lang.fileConfig.getString("target-offline");
                    Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', msg));
                }else {
                    try {
                        World myworld = target.getWorld();
                        Location yourlocation = new Location(myworld, Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3]));
                        target.teleport(yourlocation);
                        String msg = Lang.fileConfig.getString("teleport-pos-target-success");
                        target.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        String msg2 = Lang.fileConfig.getString("teleport-pos-target-message").replace("<target>", target.getName());
                        Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', msg2));
                    } catch (NumberFormatException e) {
                        String msg = Lang.fileConfig.getString("teleport-pos-invalid");
                        Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', msg));
                    }
                }
            }else{
                Bukkit.getLogger().info(ChatColor.RED + "Incorrect format! Please use /tppos (target) (x) (y) (z)");
            }
        } else if (sender instanceof BlockCommandSender){
            if (args.length == 4){
                Player target = Bukkit.getPlayer(args[0]);
                if (target == null){
                    String msg = Lang.fileConfig.getString("target-offline");
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                }else {
                    try {
                        World myworld = target.getWorld();
                        Location yourlocation = new Location(myworld, Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3]));
                        target.teleport(yourlocation);
                    } catch (NumberFormatException e) {
                        String msg = Lang.fileConfig.getString("teleport-pos-invalid");
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    }
                }
            }else{
                sender.sendMessage(ChatColor.RED + "Incorrect format! Please use /tppos (target) (x) (y) (z)");
            }
        }
        return true;
    }
}