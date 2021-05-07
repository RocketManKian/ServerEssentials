package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public class Warp implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Location loc;
        // Checking if the player has the correct permission
        if (sender instanceof Player){
            Player player = (Player) sender;
            if (player.hasPermission("se.warp")) {
                if (args.length == 1) {
                    if (Setwarp.file.exists() && Setwarp.fileConfig.getString("Warp." + args[0] + ".World") != null) {
                        // Check if the File Exists and if Location.World has data
                        if (sender.hasPermission("se.warps." + args[0]) || sender.hasPermission("se.warps.all")) {
                            // Gathering Location
                            float yaw = Setwarp.fileConfig.getInt("Warp." + args[0] + ".Yaw");
                            float pitch = Sethome.fileConfig.getInt("Home." + player.getName() + ".Pitch");
                            loc = new Location(Bukkit.getWorld(Setwarp.fileConfig.getString("Warp." + args[0] + ".World")),
                                    Setwarp.fileConfig.getDouble("Warp." + args[0] + ".X"),
                                    Setwarp.fileConfig.getDouble("Warp." + args[0] + ".Y"),
                                    Setwarp.fileConfig.getDouble("Warp." + args[0] + ".Z"),
                                    yaw, pitch);
                            if (args.length == 1) {
                                // Teleporting Player
                                player.teleport(loc);
                                player.sendMessage("Successfully warped to " + args[0]);
                                return true;
                            } else
                                return false;
                        } else {
                            if (ServerEssentials.plugin.getConfig().getString("no-permission-message").length() == 0) {
                                player.sendMessage(ChatColor.RED + "You do not have the required permission (se.warps." + args[0] + ") to run this command.");
                                return true;
                            } else {
                                String permission = ServerEssentials.getPlugin().getConfig().getString("no-permission-message");
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', permission));
                            }
                            return true;
                        }
                    } else {
                        player.sendMessage("Warp Doesn't Exist!");
                        return true;
                    }
                } else if (args.length == 0) {
                    if (player.hasPermission("se.warp")) {
                        if (ServerEssentials.plugin.getConfig().getBoolean("enable-warp-gui")){
                            int index = 0;
                            Integer size = ServerEssentials.plugin.getConfig().getInt("warp-gui-size");
                            Inventory inv = Bukkit.createInventory(null, size, ChatColor.AQUA + "Warp GUI");
                            ConfigurationSection inventorySection = Setwarp.fileConfig.getConfigurationSection("Warp.");
                            ConfigurationSection inventorySection2 = Setwarp.fileConfig.getConfigurationSection("Warp");
                            if (inventorySection == null && inventorySection2 == null){
                                player.sendMessage(ChatColor.RED + "warp.yml file is empty or null");
                                return true;
                            }else{
                                assert inventorySection2 != null;
                                assert inventorySection != null;
                                String warpitem = ServerEssentials.plugin.getConfig().getString("warp-item");
                                ItemStack item = new ItemStack(Material.getMaterial(warpitem));
                                ItemMeta meta = item.getItemMeta();
                                if (!inventorySection.getKeys(true).isEmpty()){
                                    if (!(inventorySection.getKeys(false).size() > ServerEssentials.plugin.getConfig().getInt("warp-gui-size"))){
                                        try {
                                            for (String key : inventorySection.getKeys(false)) {
                                                String warpcolour = ServerEssentials.plugin.getConfig().getString("warp-name-colour");
                                                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', warpcolour + key));
                                                meta.setLore(Collections.singletonList((ChatColor.DARK_PURPLE + "Click to teleport to " + key)));
                                                item.setItemMeta(meta);
                                                inv.setItem(index, item);
                                                index ++;
                                            }
                                        } catch (NullPointerException e) {
                                            player.sendMessage(ChatColor.RED + "No Warps have been set.");
                                        }
                                        player.openInventory(inv);
                                        return true;
                                    }else{
                                        player.sendMessage(ChatColor.RED + "GUI Size is too small, increase the value in Config!");
                                        return true;
                                    }
                                }else{
                                    player.sendMessage(ChatColor.RED + "No Warps are available");
                                    return true;
                                }
                            }
                        }else{
                            ConfigurationSection inventorySection = Setwarp.fileConfig.getConfigurationSection("Warp.");
                            ConfigurationSection inventorySection2 = Setwarp.fileConfig.getConfigurationSection("Warp");
                            assert inventorySection2 != null;
                            player.sendMessage(ChatColor.GREEN + "---------------------------"
                                    + "\nWarp(s) List"
                                    + "\n---------------------------");
                            try {
                                for (String key : inventorySection.getKeys(false)) {
                                    player.sendMessage(ChatColor.GOLD + key);
                                }
                            } catch (NullPointerException e) {
                                player.sendMessage(ChatColor.RED + "No Warps have been set.");
                            }
                            return true;
                        }
                    } else {
                        if (ServerEssentials.plugin.getConfig().getString("no-permission-message").length() == 0) {
                            player.sendMessage(ChatColor.RED + "You do not have the required permission (se.warp) to run this command.");
                            return true;
                        } else {
                            String permission = ServerEssentials.getPlugin().getConfig().getString("no-permission-message");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', permission));
                        }
                        return true;
                    }
                }
            } else {
                if (ServerEssentials.plugin.getConfig().getString("no-permission-message").length() == 0) {
                    player.sendMessage(ChatColor.RED + "You do not have the required permission (se.warp) to run this command.");
                    return true;
                } else {
                    String permission = ServerEssentials.getPlugin().getConfig().getString("no-permission-message");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', permission));
                }
                return true;
            }
        }else if (sender instanceof ConsoleCommandSender || sender instanceof BlockCommandSender) {
            if (args.length == 2) {
                if (Setwarp.file.exists() && Setwarp.fileConfig.getString("Warp." + args[1] + ".World") != null) {
                    Player target = Bukkit.getServer().getPlayer(args[0]);
                    if (target != null) {
                        if (target != sender) {
                            // Gathering Location
                            float yaw = Setwarp.fileConfig.getInt("Warp." + args[1] + ".Yaw");
                            loc = new Location(Bukkit.getWorld(Setwarp.fileConfig.getString("Warp." + args[1] + ".World")),
                                    Setwarp.fileConfig.getDouble("Warp." + args[1] + ".X"),
                                    Setwarp.fileConfig.getDouble("Warp." + args[1] + ".Y"),
                                    Setwarp.fileConfig.getDouble("Warp." + args[1] + ".Z"),
                                    yaw, 0);
                            // Teleporting Target
                            target.teleport(loc);
                            target.sendMessage("Successfully warped to " + args[1]);
                            return true;
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "Cannot find player " + ChatColor.WHITE + args[0]);
                        return true;
                    }
                } else {
                    sender.sendMessage("Warp Doesn't Exist!");
                    return true;
                }
            }
        }
        return false;
    }
}