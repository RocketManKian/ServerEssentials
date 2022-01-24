package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Warp implements CommandExecutor {

    private static HashMap<UUID, Integer> warpteleport = new HashMap<>();
    public static ArrayList<UUID> cancel = new ArrayList<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        int delay = ServerEssentials.plugin.getConfig().getInt("warp-teleport");
        // Checking if the player has the correct permission
        if (sender instanceof Player){
            Player player = (Player) sender;
            if (player.hasPermission("se.warp") || player.hasPermission("se.all")) {
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
                                    if (loc.isWorldLoaded()){
                                        player.teleport(loc);
                                        Boolean subtitle = ServerEssentials.plugin.getConfig().getBoolean("enable-warp-subtitle");
                                        if (subtitle) {
                                            String msg = Lang.fileConfig.getString("warp-subtitle").replace("<warp>", args[0]);
                                            player.sendTitle(ChatColor.translateAlternateColorCodes('&', msg), null);
                                            return true;
                                        } else {
                                            String msg = Lang.fileConfig.getString("warp-message").replace("<warp>", args[0]);
                                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                            return true;
                                        }
                                    }else{
                                        String msg = Lang.fileConfig.getString("warp-world-invalid");
                                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                        return true;
                                    }
                                }else{
                                    if (ServerEssentials.plugin.getConfig().getBoolean("warp-movement-cancel")){
                                        cancel.add(player.getUniqueId());
                                        String msg = Lang.fileConfig.getString("warp-wait-message").replace("<warp>", args[0]).replace("<time>", String.valueOf(delay));
                                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                        delay = delay * 20;
                                        if (warpteleport.containsKey(player.getUniqueId()) && warpteleport.get(player.getUniqueId()) != null) {
                                            Bukkit.getScheduler().cancelTask(warpteleport.get(player.getUniqueId()));
                                        }
                                        warpteleport.put(player.getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((ServerEssentials.plugin), new Runnable() {
                                            public void run() {
                                                if (cancel.contains(player.getUniqueId())){
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
                                                        if (loc.isWorldLoaded()){
                                                            // Teleporting Player
                                                            player.teleport(loc);
                                                            Boolean subtitle = ServerEssentials.plugin.getConfig().getBoolean("enable-warp-subtitle");
                                                            if (subtitle) {
                                                                String msg = Lang.fileConfig.getString("warp-subtitle").replace("<warp>", args[0]);
                                                                player.sendTitle(ChatColor.translateAlternateColorCodes('&', msg), null);
                                                            } else {
                                                                String msg = Lang.fileConfig.getString("warp-message").replace("<warp>", args[0]);
                                                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                                            }
                                                        }else{
                                                            String msg = Lang.fileConfig.getString("warp-world-invalid");
                                                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                                        }
                                                        cancel.remove(player.getUniqueId());
                                                    }
                                                }
                                            }
                                        }, delay));
                                        return true;
                                    }else{
                                        String msg = Lang.fileConfig.getString("warp-wait-message").replace("<warp>", args[0]).replace("<time>", String.valueOf(delay));
                                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
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
                                                    if (loc.isWorldLoaded()){
                                                        // Teleporting Player
                                                        player.teleport(loc);
                                                        Boolean subtitle = ServerEssentials.plugin.getConfig().getBoolean("enable-warp-subtitle");
                                                        if (subtitle) {
                                                            String msg = Lang.fileConfig.getString("warp-subtitle").replace("<warp>", args[0]);
                                                            player.sendTitle(ChatColor.translateAlternateColorCodes('&', msg), null);
                                                        } else {
                                                            String msg = Lang.fileConfig.getString("warp-message").replace("<warp>", args[0]);
                                                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                                        }
                                                    }else{
                                                        String msg = Lang.fileConfig.getString("warp-world-invalid");
                                                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                                    }
                                                }
                                            }
                                        }, delay));
                                        return true;
                                    }
                                }
                            }else{
                                String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/warp (warp)");
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            }
                        } else {
                            String perm = Lang.fileConfig.getString("no-permission-message").replace("<permission>", "se.warps." + args[0]);
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', perm));
                            return true;
                        }
                    } else {
                        String msg = Lang.fileConfig.getString("warp-not-found").replace("<warp>", args[0]);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    }
                } else if (args.length == 0) {
                    if (player.hasPermission("se.warp") || player.hasPermission("se.all")) {
                        if (ServerEssentials.plugin.getConfig().getBoolean("enable-warp-gui")){
                            int index = 0;
                            Integer size = ServerEssentials.plugin.getConfig().getInt("warp-gui-size");
                            Inventory inv = Bukkit.createInventory(null, size, ChatColor.translateAlternateColorCodes('&', Lang.fileConfig.getString("warp-gui-name")));
                            ConfigurationSection inventorySection = Setwarp.fileConfig.getConfigurationSection("Warp.");
                            ConfigurationSection inventorySection2 = Setwarp.fileConfig.getConfigurationSection("Warp");
                            if (inventorySection == null && inventorySection2 == null){
                                String msg = Lang.fileConfig.getString("warp-file-error");
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            }else{
                                assert inventorySection2 != null;
                                assert inventorySection != null;
                                if (!inventorySection.getKeys(true).isEmpty()){
                                    if (!(inventorySection.getKeys(false).size() > ServerEssentials.plugin.getConfig().getInt("warp-gui-size"))){
                                        try {
                                            for (String key : inventorySection.getKeys(false)) {
                                                // Getting Block
                                                String warpitem = Setwarp.fileConfig.getString("Warp." + key + ".Block");
                                                ItemStack item = new ItemStack(Material.getMaterial(warpitem));
                                                ItemMeta meta = item.getItemMeta();
                                                // Other
                                                String warpcolour = ServerEssentials.plugin.getConfig().getString("warp-name-colour");
                                                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', warpcolour + key));
                                                List<String> loreList = new ArrayList<String>();
                                                String msg = Lang.fileConfig.getString("warp-gui-left-click").replace("<warp>", key);
                                                loreList.add(ChatColor.translateAlternateColorCodes('&', msg));
                                                if (player.hasPermission("se.deletewarp")){
                                                    String msg2 = Lang.fileConfig.getString("warp-gui-right-click");
                                                    loreList.add(ChatColor.translateAlternateColorCodes('&', msg2));
                                                }
                                                meta.setLore(loreList);
                                                item.setItemMeta(meta);
                                                inv.setItem(index, item);
                                                index ++;
                                            }
                                        } catch (NullPointerException e) {
                                            String msg = Lang.fileConfig.getString("no-warps-set");
                                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                        }
                                        player.openInventory(inv);
                                        return true;
                                    }else{
                                        String msg = Lang.fileConfig.getString("warp-gui-invalid");
                                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                        return true;
                                    }
                                }else{
                                    String msg = Lang.fileConfig.getString("no-warps-set");
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                    return true;
                                }
                            }
                        }else{
                            ConfigurationSection inventorySection = Setwarp.fileConfig.getConfigurationSection("Warp.");
                            ConfigurationSection inventorySection2 = Setwarp.fileConfig.getConfigurationSection("Warp");
                            if (inventorySection == null && inventorySection2 == null) {
                                String msg = Lang.fileConfig.getString("warp-file-error");
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
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
                                String msg = Lang.fileConfig.getString("no-warps-set");
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            }
                        }
                    } else {
                        String perm = Lang.fileConfig.getString("no-permission-message").replace("<permission>", "se.warp");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', perm));
                        return true;
                    }
                }else if (args.length == 2){
                    if (player.hasPermission("se.setwarp.block")){
                        if (args[0].equalsIgnoreCase("setblock")){
                            if (Setwarp.fileConfig.getString("Warp." + args[1] + ".World") != null){
                                Material material = Material.valueOf(player.getInventory().getItemInMainHand().getType().toString());
                                if (player.getInventory().getItemInMainHand().getType() == Material.AIR){
                                    String perm = Lang.fileConfig.getString("warp-block-invalid").replace("<item>", String.valueOf(material));
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', perm));
                                    return true;
                                }else{
                                    Setwarp.fileConfig.set("Warp." + args[1] + ".Block", String.valueOf(material));
                                    try {
                                        Setwarp.fileConfig.save(Setwarp.file);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    Setwarp.reload();
                                    String block = Lang.fileConfig.getString("warp-set-block-successful").replace("<warp>", args[1]).replace("<block>", String.valueOf(material));
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', block));
                                    return true;
                                }
                            }else{
                                String msg = Lang.fileConfig.getString("warp-not-found").replace("<warp>", args[1]);
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            }
                        }else{
                            String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/warp setblock (warp)");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                            return true;
                        }
                    }else{
                        String perm = Lang.fileConfig.getString("no-permission-message").replace("<permission>", "se.setwarp.block");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', perm));
                        return true;
                    }
                }else{
                    String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/warp (warp)");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    return true;
                }
            } else {
                String perm = Lang.fileConfig.getString("no-permission-message").replace("<permission>", "se.warp");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', perm));
                return true;
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
                            if (loc.isWorldLoaded()){
                                // Teleporting Target
                                target.teleport(loc);
                                if (subtitle){
                                    String msg = Lang.fileConfig.getString("warp-subtitle").replace("<warp>", args[1]);
                                    target.sendTitle(ChatColor.translateAlternateColorCodes('&', msg), null);
                                    return true;
                                }else{
                                    String msg = Lang.fileConfig.getString("warp-message").replace("<warp>", args[1]);
                                    target.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                    return true;
                                }
                            }else{
                                String msg = Lang.fileConfig.getString("warp-world-invalid");
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                            }
                        }
                    } else {
                        String msg = Lang.fileConfig.getString("target-offline");
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    }
                } else {
                    String msg = Lang.fileConfig.getString("warp-not-found").replace("<warp>", args[0]);
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
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