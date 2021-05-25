package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public class Invsee implements CommandExecutor, Listener {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 1) {
                if (player.hasPermission("se.invsee")) {
                    Player targetPlayer = Bukkit.getServer().getPlayer(args[0]);
                    if (targetPlayer == sender) {
                        sender.sendMessage(ChatColor.RED + "You cannot open your own inventory.");
                        return true;
                    } else if (Bukkit.getPlayer(args[0]) == null) {
                        player.sendMessage(ChatColor.RED + "That player is not online!");
                        return true;
                    } else if (!(Bukkit.getPlayer(args[0]) == null)) {
                        player.openInventory(targetPlayer.getInventory());
                        return true;
                    }
                } else {
                    if (ServerEssentials.plugin.getConfig().getString("no-permission-message").length() == 0) {
                        player.sendMessage(ChatColor.RED + "You do not have the required permission (se.invsee) to run this command.");
                        return true;
                    } else {
                        String permission = ServerEssentials.getPlugin().getConfig().getString("no-permission-message");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', permission));
                        return true;
                    }
                }
            } else if (args.length == 2) {
                if (player.hasPermission("se.invsee.others")) {
                    Inventory myInventory = Bukkit.createInventory(player, 9, "Equipped Armor");
                    Player targetPlayer = Bukkit.getServer().getPlayer(args[0]);
                    if (targetPlayer == sender) {
                        sender.sendMessage(ChatColor.RED + "You cannot open your own inventory.");
                        return true;
                    } else if (Bukkit.getPlayer(args[0]) == null) {
                        player.sendMessage(ChatColor.RED + "That player is not online!");
                        return true;
                    } else if (!(Bukkit.getPlayer(args[0]) == null)) {
                        if (targetPlayer.getInventory().getHelmet() != null){
                            myInventory.setItem(0, targetPlayer.getInventory().getHelmet());
                        }if (targetPlayer.getInventory().getChestplate() != null){
                            myInventory.setItem(1, targetPlayer.getInventory().getChestplate());
                        }if (targetPlayer.getInventory().getLeggings() != null){
                            myInventory.setItem(2, targetPlayer.getInventory().getLeggings());
                        }if (targetPlayer.getInventory().getBoots() != null){
                            myInventory.setItem(3, targetPlayer.getInventory().getBoots());
                        }
                        player.openInventory(myInventory);
                        return true;
                    }
                } else {
                    if (ServerEssentials.plugin.getConfig().getString("no-permission-message").length() == 0) {
                        player.sendMessage(ChatColor.RED + "You do not have the required permission (se.invsee.others) to run this command.");
                        return true;
                    } else {
                        String permission = ServerEssentials.getPlugin().getConfig().getString("no-permission-message");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', permission));
                        return true;
                    }
                }
            }
        }
        return false;
    }
}