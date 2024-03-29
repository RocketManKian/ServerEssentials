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

import static me.rocketmankianproductions.serveressentials.ServerEssentials.hex;

public class Warp implements CommandExecutor {

    public static HashMap<UUID, Integer> warpteleport = new HashMap<>();
    public static ArrayList<UUID> cancel = new ArrayList<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        int delay = ServerEssentials.plugin.getConfig().getInt("warp-teleport");
        Boolean subtitle = ServerEssentials.plugin.getConfig().getBoolean("enable-warp-subtitle");
        if (sender instanceof Player){
            Player player = (Player) sender;
            if (ServerEssentials.permissionChecker(player, "se.warp")) {
                if (args.length == 1) {
                    if (Setwarp.file.exists() && Setwarp.fileConfig.getString("Warp." + args[0] + ".World") != null) {
                        if (player.hasPermission("se.warps.all") || ServerEssentials.permissionChecker(player, "se.warps." + args[0])) {
                            Location loc = getLocation(args);
                            if (ServerEssentials.plugin.getConfig().getInt("warp-teleport") == 0) {
                                warpSave(player);
                                warpTeleport(player, loc, "warp-message", subtitle, args[0]);
                            } else {
                                if (ServerEssentials.plugin.getConfig().getBoolean("warp-movement-cancel")) {
                                    cancel.add(player.getUniqueId());
                                    String msg = Lang.fileConfig.getString("warp-wait-message").replace("<warp>", args[0]).replace("<time>", String.valueOf(delay));
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                                    delay = delay * 20;
                                    if (warpteleport.containsKey(player.getUniqueId()) && warpteleport.get(player.getUniqueId()) != null) {
                                        Bukkit.getScheduler().cancelTask(warpteleport.get(player.getUniqueId()));
                                    }
                                    warpteleport.put(player.getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((ServerEssentials.plugin), new Runnable() {
                                        public void run() {
                                            if (cancel.contains(player.getUniqueId())) {
                                                if (warpteleport.containsKey(player.getUniqueId())) {
                                                    warpSave(player);
                                                    warpTeleport(player, loc, "warp-message", subtitle, args[0]);
                                                }
                                            }
                                        }
                                    }, delay));
                                    return true;
                                } else {
                                    String msg = Lang.fileConfig.getString("warp-wait-message").replace("<warp>", args[0]).replace("<time>", String.valueOf(delay));
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                                    delay = delay * 20;
                                    if (warpteleport.containsKey(player.getUniqueId()) && warpteleport.get(player.getUniqueId()) != null) {
                                        Bukkit.getScheduler().cancelTask(warpteleport.get(player.getUniqueId()));
                                    }
                                    warpteleport.put(player.getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((ServerEssentials.plugin), new Runnable() {
                                        public void run() {
                                            if (warpteleport.containsKey(player.getUniqueId())) {
                                                warpSave(player);
                                                warpTeleport(player, loc, "warp-message", subtitle, args[0]);
                                            }
                                        }
                                    }, delay));
                                    return true;
                                }
                            }
                        }
                    } else {
                        String msg = Lang.fileConfig.getString("warp-not-found").replace("<warp>", args[0]);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                        return true;
                    }
                } else if (args.length == 0) {
                    if (ServerEssentials.permissionChecker(player, "se.warp")) {
                        if (ServerEssentials.plugin.getConfig().getBoolean("enable-warp-gui")){
                            int index = 0;
                            Integer size = ServerEssentials.plugin.getConfig().getInt("warp-gui-size");
                            Inventory inv = Bukkit.createInventory(null, size, ChatColor.translateAlternateColorCodes('&', Lang.fileConfig.getString("warp-gui-name")));
                            ConfigurationSection inventorySection = Setwarp.fileConfig.getConfigurationSection("Warp.");
                            ConfigurationSection inventorySection2 = Setwarp.fileConfig.getConfigurationSection("Warp");
                            if (inventorySection == null && inventorySection2 == null){
                                String msg = Lang.fileConfig.getString("warp-file-error");
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
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
                                                loreList.add(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                                                if (player.hasPermission("se.deletewarp")){
                                                    String msg2 = Lang.fileConfig.getString("warp-gui-right-click");
                                                    loreList.add(ChatColor.translateAlternateColorCodes('&', hex(msg2)));
                                                }
                                                meta.setLore(loreList);
                                                item.setItemMeta(meta);
                                                inv.setItem(index, item);
                                                index ++;
                                            }
                                        } catch (NullPointerException e) {
                                            String msg = Lang.fileConfig.getString("no-warps-set");
                                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                                        }
                                        player.openInventory(inv);
                                        return true;
                                    }else{
                                        String msg = Lang.fileConfig.getString("warp-gui-invalid");
                                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                                        return true;
                                    }
                                }else{
                                    String msg = Lang.fileConfig.getString("no-warps-set");
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                                    return true;
                                }
                            }
                        }else{
                            ConfigurationSection inventorySection = Setwarp.fileConfig.getConfigurationSection("Warp.");
                            ConfigurationSection inventorySection2 = Setwarp.fileConfig.getConfigurationSection("Warp");
                            if (inventorySection == null && inventorySection2 == null) {
                                String msg = Lang.fileConfig.getString("warp-file-error");
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
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
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                                return true;
                            }
                        }
                    }
                }else if (args.length == 2){
                    if (ServerEssentials.permissionChecker(player, "se.setwarp.block")){
                        if (args[0].equalsIgnoreCase("setblock")){
                            if (Setwarp.fileConfig.getString("Warp." + args[1] + ".World") != null){
                                Material material = Material.valueOf(player.getInventory().getItemInMainHand().getType().toString());
                                if (player.getInventory().getItemInMainHand().getType() == Material.AIR){
                                    String perm = Lang.fileConfig.getString("warp-block-invalid").replace("<item>", String.valueOf(material));
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(perm)));
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
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(block)));
                                    return true;
                                }
                            }else{
                                String msg = Lang.fileConfig.getString("warp-not-found").replace("<warp>", args[1]);
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                                return true;
                            }
                        }else{
                            String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/warp setblock (warp)");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                            return true;
                        }
                    }
                }else{
                    String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/warp (warp)");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                    return true;
                }
            }
        }else if (sender instanceof ConsoleCommandSender || sender instanceof BlockCommandSender) {
            String console = Lang.fileConfig.getString("console-invalid");
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', hex(console)));
            return true;
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

    public static void warpSave(Player player){
        if (ServerEssentials.plugin.getConfig().getBoolean("warp-save")) {
            if (Back.location.containsKey(player.getUniqueId())) {
                Back.location.remove(player.getUniqueId());
                Back.location.put(player.getUniqueId(), player.getLocation());
            } else {
                Back.location.put(player.getUniqueId(), player.getLocation());
            }
        } else if (player.hasPermission("se.back.bypass")) {
            if (Back.location.containsKey(player.getUniqueId())) {
                Back.location.remove(player.getUniqueId());
                Back.location.put(player.getUniqueId(), player.getLocation());
            } else {
                Back.location.put(player.getUniqueId(), player.getLocation());
            }
        }
    }

    public static void warpTeleport(Player player, Location loc, String lang, Boolean subtitle, String warp){
        if (subtitle){
            if (loc.isWorldLoaded()) {
                // Teleporting Player
                player.teleport(loc);
                String msg = Lang.fileConfig.getString("warp-subtitle").replace("<warp>", warp);
                player.sendTitle(ChatColor.translateAlternateColorCodes('&', hex(msg)), null);
            } else {
                String msg = Lang.fileConfig.getString("warp-world-invalid");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
            }
        }else{
            if (loc.isWorldLoaded()) {
                // Teleporting Player
                player.teleport(loc);
                String msg = Lang.fileConfig.getString(lang).replace("<warp>", warp);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
            } else {
                String msg = Lang.fileConfig.getString("warp-world-invalid");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
            }
        }
        cancel.remove(player.getUniqueId());
        warpteleport.remove(player.getUniqueId());
    }
}