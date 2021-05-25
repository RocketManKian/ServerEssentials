package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class Repair implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;

        if (player.hasPermission("se.repair")) {
            if (args.length == 0) {
                if (sender instanceof Player) {
                    if (!player.getItemInHand().getType().equals(Material.AIR)) {
                        if (player.getItemInHand().getDurability() == player.getItemInHand().getMaxItemUseDuration()) {
                            player.sendMessage(ChatColor.RED + "Durability is max");
                            return true;
                        } else {
                            player.getItemInHand().setDurability((short) 0);
                            player.sendMessage(ChatColor.BLUE + player.getItemInHand().getI18NDisplayName() + ChatColor.GREEN + " repaired!");
                            return true;
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "You cannot repair that item.");
                        return true;
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "You are not a player");
                    return true;
                }
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("all")) {
                    for (int i = 0; i <= 36; i++) {
                        try {
                            player.getInventory().getItem(i).setDurability((short) 0);
                        } catch (Exception e) {
                            //bleh
                        }
                    }
                    player.getInventory().getItemInOffHand().setDurability((short) 0);
                    if (player.getInventory().getBoots() != null) {
                        player.getInventory().getBoots().setDurability((short) 0);
                    } else if (player.getInventory().getLeggings() != null) {
                        player.getInventory().getLeggings().setDurability((short) 0);
                    } else if (player.getInventory().getChestplate() != null) {
                        player.getInventory().getChestplate().setDurability((short) 0);
                    } else if (player.getInventory().getHelmet() != null) {
                        player.getInventory().getHelmet().setDurability((short) 0);
                    } else {
                        // Bleh
                    }
                    player.sendMessage(ChatColor.GREEN + "Repaired all item(s)!");
                    return true;
                }
            }
        } else {
            if (ServerEssentials.plugin.getConfig().getString("no-permission-message").length() == 0) {
                player.sendMessage(ChatColor.RED + "You do not have the required permission (se.repair) to run this command.");
                return true;
            } else {
                String permission = ServerEssentials.getPlugin().getConfig().getString("no-permission-message");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', permission));
                return true;
            }
        }
        return false;
    }
}
