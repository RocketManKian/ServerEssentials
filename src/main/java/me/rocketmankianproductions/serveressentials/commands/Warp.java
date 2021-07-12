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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Warp implements CommandExecutor {

    private static HashMap<UUID, Integer> warpteleport = new HashMap<>();
    int delay = ServerEssentials.plugin.getConfig().getInt("warp-teleport");

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        // Checking if the player has the correct permission
        if (sender instanceof Player){
            Player player = (Player) sender;
            if (player.hasPermission("se.warp")) {
                if (args.length == 1) {
                    if (Setwarp.file.exists() && Setwarp.fileConfig.getString("Warp." + args[0] + ".World") != null) {
                        // Check if the File Exists and if Location.World has data
                        if (sender.hasPermission("se.warps." + args[0]) || sender.hasPermission("se.warps.all")) {
                            Location loc = getLocation(args);
                            if (args.length == 1) {
                                if (ServerEssentials.plugin.getConfig().getInt("warp-teleport") == 0){
                                    if (ServerEssentials.plugin.getConfig().getBoolean("warp-save")){
                                        if (Back.location.containsKey(player.getUniqueId())){
                                            Back.location.remove(player.getUniqueId());
                                            Back.location.put(player.getUniqueId(), player.getLocation());
                                        }else{
                                            Back.location.put(player.getUniqueId(), player.getLocation());
                                        }
                                    }else if (player.hasPermission("se.back.bypass")){
                                        if (Back.location.containsKey(player.getUniqueId())){
                                            Back.location.remove(player.getUniqueId());
                                            Back.location.put(player.getUniqueId(), player.getLocation());
                                        }else{
                                            Back.location.put(player.getUniqueId(), player.getLocation());
                                        }
                                    }
                                    player.teleport(loc);
                                    Boolean subtitle = ServerEssentials.plugin.getConfig().getBoolean("enable-warp-subtitle");
                                    if (subtitle) {
                                        player.sendTitle("Warped to " + ChatColor.GOLD + args[0], null);
                                        return true;
                                    } else {
                                        player.sendMessage(ChatColor.GREEN + "Successfully warped to " + ChatColor.GOLD + args[0]);
                                        return true;
                                    }
                                }else{
                                    player.sendMessage(ChatColor.GREEN + "Warping to " + ChatColor.GOLD + args[0] + ChatColor.GREEN + " in " + ChatColor.GOLD + delay + " Seconds");
                                    delay = delay * 20;
                                    if (warpteleport.containsKey(player.getUniqueId()) && warpteleport.get(player.getUniqueId()) != null) {
                                        Bukkit.getScheduler().cancelTask(warpteleport.get(player.getUniqueId()));
                                    }
                                    warpteleport.put(player.getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((ServerEssentials.plugin), new Runnable() {
                                        public void run() {
                                            if (warpteleport.containsKey(player.getUniqueId())) {
                                                if (ServerEssentials.plugin.getConfig().getBoolean("warp-save")){
                                                    if (Back.location.containsKey(player.getUniqueId())){
                                                        Back.location.remove(player.getUniqueId());
                                                        Back.location.put(player.getUniqueId(), player.getLocation());
                                                    }else{
                                                        Back.location.put(player.getUniqueId(), player.getLocation());
                                                    }
                                                }else if (player.hasPermission("se.back.bypass")){
                                                    if (Back.location.containsKey(player.getUniqueId())){
                                                        Back.location.remove(player.getUniqueId());
                                                        Back.location.put(player.getUniqueId(), player.getLocation());
                                                    }else{
                                                        Back.location.put(player.getUniqueId(), player.getLocation());
                                                    }
                                                }
                                                // Teleporting Player
                                                player.teleport(loc);
                                                Boolean subtitle = ServerEssentials.plugin.getConfig().getBoolean("enable-warp-subtitle");
                                                if (subtitle) {
                                                    player.sendTitle("Warped to " + ChatColor.GOLD + args[0], null);
                                                } else {
                                                    player.sendMessage(ChatColor.GREEN + "Successfully warped to " + ChatColor.GOLD + args[0]);
                                                }
                                            }
                                        }
                                    }, delay));
                                    return true;
                                }
                            }
                        } else {
                            if (ServerEssentials.plugin.getConfig().getString("no-permission-message").length() == 0) {
                                player.sendMessage(ChatColor.RED + "You do not have the required permission (se.warps." + args[0] + ") to run this command.");
                                return true;
                            } else {
                                String permission = ServerEssentials.getPlugin().getConfig().getString("no-permission-message");
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', permission));
                                return true;
                            }
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
                                                List<String> loreList = new ArrayList<String>();
                                                loreList.add(ChatColor.DARK_PURPLE + "Click to teleport to " + key);
                                                if (player.hasPermission("se.deletewarp")){
                                                    loreList.add(ChatColor.RED + "Right click to Delete Warp");
                                                }
                                                meta.setLore(loreList);
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
                            if (inventorySection == null && inventorySection2 == null) {
                                player.sendMessage(ChatColor.RED + "warp.yml file is empty or null");
                                return true;
                            }else if (!inventorySection.getKeys(true).isEmpty()){
                                assert inventorySection != null;
                                assert inventorySection2 != null;
                                player.sendMessage(ChatColor.GREEN + "---------------------------"
                                        + "\nWarp(s) List"
                                        + "\n---------------------------");
                                try {
                                    for (String key : inventorySection.getKeys(false)) {
                                        player.sendMessage(ChatColor.GOLD + key);
                                    }
                                } catch (NullPointerException e) {
                                    //Bleh
                                }
                                return true;
                            }else{
                                assert inventorySection != null;
                                assert inventorySection2 != null;
                                player.sendMessage(ChatColor.GREEN + "---------------------------"
                                        + "\nWarp(s) List"
                                        + "\n---------------------------");
                                player.sendMessage(ChatColor.RED + "No Warps have been set.");
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
                }
            } else {
                if (ServerEssentials.plugin.getConfig().getString("no-permission-message").length() == 0) {
                    player.sendMessage(ChatColor.RED + "You do not have the required permission (se.warp) to run this command.");
                    return true;
                } else {
                    String permission = ServerEssentials.getPlugin().getConfig().getString("no-permission-message");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', permission));
                    return true;
                }
            }
        }else if (sender instanceof ConsoleCommandSender || sender instanceof BlockCommandSender) {
            if (args.length == 2) {
                if (Setwarp.file.exists() && Setwarp.fileConfig.getString("Warp." + args[1] + ".World") != null) {
                    Player target = Bukkit.getServer().getPlayer(args[0]);
                    if (target != null) {
                        if (target != sender) {
                            Boolean subtitle = ServerEssentials.plugin.getConfig().getBoolean("enable-warp-subtitle");
                            // Gathering Location
                            float yaw = Setwarp.fileConfig.getInt("Warp." + args[1] + ".Yaw");
                            Location loc = new Location(Bukkit.getWorld(Setwarp.fileConfig.getString("Warp." + args[1] + ".World")),
                                    Setwarp.fileConfig.getDouble("Warp." + args[1] + ".X"),
                                    Setwarp.fileConfig.getDouble("Warp." + args[1] + ".Y"),
                                    Setwarp.fileConfig.getDouble("Warp." + args[1] + ".Z"),
                                    yaw, 0);
                            if (ServerEssentials.plugin.getConfig().getBoolean("warp-save")){
                                if (Back.location.containsKey(target.getUniqueId())){
                                    Back.location.remove(target.getUniqueId());
                                    Back.location.put(target.getUniqueId(), target.getLocation());
                                }else{
                                    Back.location.put(target.getUniqueId(), target.getLocation());
                                }
                            }else if (target.hasPermission("se.back.bypass")){
                                if (Back.location.containsKey(target.getUniqueId())){
                                    Back.location.remove(target.getUniqueId());
                                    Back.location.put(target.getUniqueId(), target.getLocation());
                                }else{
                                    Back.location.put(target.getUniqueId(), target.getLocation());
                                }
                            }
                            // Teleporting Target
                            target.teleport(loc);
                            if (subtitle){
                                target.sendTitle("Warped to " + ChatColor.GOLD + args[1], null);
                                return true;
                            }else{
                                target.sendMessage("Successfully warped to " + ChatColor.GOLD + args[1]);
                                return true;
                            }
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

    public static Location getLocation(String[] args) {
        // Gathering Location
        float yaw = Setwarp.fileConfig.getInt("Warp." + args[0] + ".Yaw");
        Location loc = new Location(Bukkit.getWorld(Setwarp.fileConfig.getString("Warp." + args[0] + ".World")),
                Setwarp.fileConfig.getDouble("Warp." + args[0] + ".X"),
                Setwarp.fileConfig.getDouble("Warp." + args[0] + ".Y"),
                Setwarp.fileConfig.getDouble("Warp." + args[0] + ".Z"),
                yaw, 0);
        return loc;
    }
}