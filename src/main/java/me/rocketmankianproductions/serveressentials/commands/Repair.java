package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class Repair implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;

        if (player.hasPermission("se.repair") || player.hasPermission("se.all")) {
            if (args.length == 0) {
                if (sender instanceof Player) {
                    if (!player.getItemInHand().getType().equals(Material.AIR)) {
                        ItemStack item = player.getItemInHand();
                        short durability = item.getDurability();
                        if (durability == 0) {
                            String msg = Lang.fileConfig.getString("repair-durability-max");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                            return true;
                        } else {
                            player.getItemInHand().setDurability((short) 0);
                            String msg = Lang.fileConfig.getString("repair-successful").replace("<item>", player.getItemInHand().getType().toString());
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                            return true;
                        }
                    } else {
                        String msg = Lang.fileConfig.getString("repair-invalid-item");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    }
                } else {
                    String msg = Lang.fileConfig.getString("invalid-player");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
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
                    String msg = Lang.fileConfig.getString("repair-all-items");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    return true;
                }
            }
        } else {
            String perm = Lang.fileConfig.getString("no-permission-message").replace("<permission>", "se.repair");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', perm));
            return true;
        }
        return false;
    }
}