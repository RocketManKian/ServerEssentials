package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
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

public class Home implements CommandExecutor {

    public static HashMap<UUID, Integer> hometeleport = new HashMap<>();
    public static ArrayList<UUID> cancel = new ArrayList<>();

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player){
            int delay = ServerEssentials.plugin.getConfig().getInt("home-teleport");
            Player player = (Player) sender;
            String name = player.getUniqueId().toString();
            if (args.length == 1) {
                // Checking if the player has the correct permission
                if (player.hasPermission("se.home") || player.hasPermission("se.all")) {
                    // Check if the File Exists and if Location.World has data
                    if (Sethome.file.exists() && Sethome.fileConfig.getString("Home." + name + "." + args[0] + ".World") != null) {
                        Location loc = getLocation(args, player);
                        if (args.length == 1) {
                            if (ServerEssentials.plugin.getConfig().getInt("home-teleport") == 0) {
                                if (ServerEssentials.plugin.getConfig().getBoolean("home-save")){
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
                                        String msg = Lang.fileConfig.getString("home-subtitle").replace("<home>", args[0]);
                                        player.sendTitle(ChatColor.translateAlternateColorCodes('&', msg), null);
                                        return true;
                                    } else {
                                        String msg = Lang.fileConfig.getString("home-message").replace("<home>", args[0]);
                                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                        return true;
                                    }
                                }else{
                                    String msg = Lang.fileConfig.getString("home-world-invalid");
                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                }
                            }else{
                                if (ServerEssentials.plugin.getConfig().getBoolean("home-movement-cancel")){
                                    cancel.add(player.getUniqueId());
                                    String msg = Lang.fileConfig.getString("home-wait-message").replace("<home>", args[0]).replace("<time>", String.valueOf(delay));
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                    delay = delay * 20;
                                    if (hometeleport.containsKey(player.getUniqueId()) && hometeleport.get(player.getUniqueId()) != null) {
                                        Bukkit.getScheduler().cancelTask(hometeleport.get(player.getUniqueId()));
                                    }
                                    hometeleport.put(player.getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((ServerEssentials.plugin), new Runnable() {
                                        public void run() {
                                            if (cancel.contains(player.getUniqueId())){
                                                if (hometeleport.containsKey(player.getUniqueId())) {
                                                    if (ServerEssentials.plugin.getConfig().getBoolean("home-save")) {
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
                                                    if (loc.isWorldLoaded()){
                                                        // Teleporting Player
                                                        player.teleport(loc);
                                                        Boolean subtitle = ServerEssentials.plugin.getConfig().getBoolean("enable-home-subtitle");
                                                        if (subtitle) {
                                                            String msg = Lang.fileConfig.getString("home-subtitle").replace("<home>", args[0]);
                                                            player.sendTitle(ChatColor.translateAlternateColorCodes('&', msg), null);
                                                        } else {
                                                            String msg = Lang.fileConfig.getString("home-message").replace("<home>", args[0]);
                                                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                                        }
                                                    }else{
                                                        String msg = Lang.fileConfig.getString("home-world-invalid");
                                                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                                    }
                                                    cancel.remove(player.getUniqueId());
                                                }
                                            }
                                        }
                                    }, delay));
                                    return true;
                                }else{
                                    String msg = Lang.fileConfig.getString("home-wait-message").replace("<home>", args[0]).replace("<time>", String.valueOf(delay));
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                    delay = delay * 20;
                                    if (hometeleport.containsKey(player.getUniqueId()) && hometeleport.get(player.getUniqueId()) != null) {
                                        Bukkit.getScheduler().cancelTask(hometeleport.get(player.getUniqueId()));
                                    }
                                    hometeleport.put(player.getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((ServerEssentials.plugin), new Runnable() {
                                        public void run() {
                                            if (hometeleport.containsKey(player.getUniqueId())) {
                                                if (ServerEssentials.plugin.getConfig().getBoolean("home-save")) {
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
                                                if (loc.isWorldLoaded()) {
                                                    // Teleporting Player
                                                    player.teleport(loc);
                                                    Boolean subtitle = ServerEssentials.plugin.getConfig().getBoolean("enable-home-subtitle");
                                                    if (subtitle) {
                                                        String msg = Lang.fileConfig.getString("home-subtitle").replace("<home>", args[0]);
                                                        player.sendTitle(ChatColor.translateAlternateColorCodes('&', msg), null);
                                                    } else {
                                                        String msg = Lang.fileConfig.getString("home-message").replace("<home>", args[0]);
                                                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                                    }
                                                } else {
                                                    String msg = Lang.fileConfig.getString("home-world-invalid");
                                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                                }
                                            }
                                        }
                                    }, delay));
                                    return true;
                                }
                            }
                        }else{
                            String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/home (home)");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                            return true;
                        }
                    } else {
                        String msg = Lang.fileConfig.getString("home-invalid").replace("<home>", args[0]);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    }
                } else {
                    String perm = Lang.fileConfig.getString("no-permission-message").replace("<permission>", "se.home");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', perm));
                    return true;
                }
            } else if (args.length == 0) {
                if (player.hasPermission("se.home") || player.hasPermission("se.all")){
                    ConfigurationSection inventorySection = Sethome.fileConfig.getConfigurationSection("Home." + name);
                    if (inventorySection != null){
                        // If Player only has 1 Home Set
                        if (inventorySection.getKeys(false).size() == 1){
                            // Gathering Location
                            String home = null;
                            for (String key : inventorySection.getKeys(false)){
                                home = key;
                            }
                            float yaw = Sethome.fileConfig.getInt("Home." + name + "." + home + ".Yaw");
                            Location loc = new Location(Bukkit.getWorld(Sethome.fileConfig.getString("Home." + name + "." + home + ".World")),
                                    Sethome.fileConfig.getDouble("Home." + name + "." + home + ".X"),
                                    Sethome.fileConfig.getDouble("Home." + name + "." + home + ".Y"),
                                    Sethome.fileConfig.getDouble("Home." + name + "." + home + ".Z"),
                                    yaw, 0);
                            if (ServerEssentials.plugin.getConfig().getInt("home-teleport") == 0) {
                                if (ServerEssentials.plugin.getConfig().getBoolean("home-save")){
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
                                        String msg = Lang.fileConfig.getString("home-subtitle").replace("<home>", home);
                                        player.sendTitle(ChatColor.translateAlternateColorCodes('&', msg), null);
                                        return true;
                                    } else {
                                        String msg = Lang.fileConfig.getString("home-message").replace("<home>", home);
                                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                        return true;
                                    }
                                }else{
                                    String msg = Lang.fileConfig.getString("home-world-invalid");
                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                }
                            }else{
                                if (ServerEssentials.plugin.getConfig().getBoolean("home-movement-cancel")){
                                    cancel.add(player.getUniqueId());
                                    String msg = Lang.fileConfig.getString("home-wait-message").replace("<home>", home).replace("<time>", String.valueOf(delay));
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                    delay = delay * 20;
                                    if (hometeleport.containsKey(player.getUniqueId()) && hometeleport.get(player.getUniqueId()) != null) {
                                        Bukkit.getScheduler().cancelTask(hometeleport.get(player.getUniqueId()));
                                    }
                                    String finalHome = home;
                                    hometeleport.put(player.getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((ServerEssentials.plugin), new Runnable() {
                                        public void run() {
                                            if (cancel.contains(player.getUniqueId())){
                                                if (hometeleport.containsKey(player.getUniqueId())) {
                                                    if (ServerEssentials.plugin.getConfig().getBoolean("home-save")) {
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
                                                    if (loc.isWorldLoaded()){
                                                        // Teleporting Player
                                                        player.teleport(loc);
                                                        Boolean subtitle = ServerEssentials.plugin.getConfig().getBoolean("enable-home-subtitle");
                                                        if (subtitle) {
                                                            String msg = Lang.fileConfig.getString("home-subtitle").replace("<home>", finalHome);
                                                            player.sendTitle(ChatColor.translateAlternateColorCodes('&', msg), null);
                                                        } else {
                                                            String msg = Lang.fileConfig.getString("home-message").replace("<home>", finalHome);
                                                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                                        }
                                                    }else{
                                                        String msg = Lang.fileConfig.getString("home-world-invalid");
                                                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                                    }
                                                    cancel.remove(player.getUniqueId());
                                                }
                                            }
                                        }
                                    }, delay));
                                    return true;
                                }else{
                                    String msg = Lang.fileConfig.getString("home-wait-message").replace("<home>", home).replace("<time>", String.valueOf(delay));
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                    delay = delay * 20;
                                    if (hometeleport.containsKey(player.getUniqueId()) && hometeleport.get(player.getUniqueId()) != null) {
                                        Bukkit.getScheduler().cancelTask(hometeleport.get(player.getUniqueId()));
                                    }
                                    String finalHome1 = home;
                                    hometeleport.put(player.getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((ServerEssentials.plugin), new Runnable() {
                                        public void run() {
                                            if (hometeleport.containsKey(player.getUniqueId())) {
                                                if (ServerEssentials.plugin.getConfig().getBoolean("home-save")) {
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
                                                if (loc.isWorldLoaded()) {
                                                    // Teleporting Player
                                                    player.teleport(loc);
                                                    Boolean subtitle = ServerEssentials.plugin.getConfig().getBoolean("enable-home-subtitle");
                                                    if (subtitle) {
                                                        String msg = Lang.fileConfig.getString("home-subtitle").replace("<home>", finalHome1);
                                                        player.sendTitle(ChatColor.translateAlternateColorCodes('&', msg), null);
                                                    } else {
                                                        String msg = Lang.fileConfig.getString("home-message").replace("<home>", finalHome1);
                                                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                                    }
                                                } else {
                                                    String msg = Lang.fileConfig.getString("home-world-invalid");
                                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                                }
                                            }
                                        }
                                    }, delay));
                                    return true;
                                }
                            }
                        }else{
                            if (ServerEssentials.plugin.getConfig().getBoolean("enable-home-gui")){
                                int index = 0;
                                Integer size = ServerEssentials.plugin.getConfig().getInt("home-gui-size");
                                Inventory inv = Bukkit.createInventory(player, size, ChatColor.translateAlternateColorCodes('&', Lang.fileConfig.getString("home-gui-name")));
                                if (inventorySection == null){
                                    String msg = Lang.fileConfig.getString("home-file-error");
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                    return true;
                                }else{
                                    assert inventorySection != null;
                                    String homeitem = ServerEssentials.plugin.getConfig().getString("home-item");
                                    ItemStack item = new ItemStack(Material.getMaterial(homeitem));
                                    ItemMeta meta = item.getItemMeta();
                                    if (!inventorySection.getKeys(true).isEmpty()){
                                        if (!(inventorySection.getKeys(false).size() > ServerEssentials.plugin.getConfig().getInt("home-gui-size"))){
                                            try {
                                                for (String key : inventorySection.getKeys(false)) {
                                                    String homecolour = ServerEssentials.plugin.getConfig().getString("home-name-colour");
                                                    meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', homecolour + key));
                                                    List<String> loreList = new ArrayList<String>();
                                                    String msg = Lang.fileConfig.getString("home-gui-left-click").replace("<home>", key);
                                                    loreList.add(ChatColor.translateAlternateColorCodes('&', msg));
                                                    if (player.hasPermission("se.deletehome")){
                                                        String msg2 = Lang.fileConfig.getString("home-gui-right-click").replace("<home>", key);
                                                        loreList.add(ChatColor.translateAlternateColorCodes('&', msg2));
                                                    }
                                                    meta.setLore(loreList);
                                                    item.setItemMeta(meta);
                                                    inv.setItem(index, item);
                                                    index ++;
                                                }
                                            } catch (NullPointerException e) {
                                                String msg = Lang.fileConfig.getString("no-homes-set");
                                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                            }
                                            player.openInventory(inv);
                                            return true;
                                        }else{
                                            String msg = Lang.fileConfig.getString("home-gui-error");
                                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                            return true;
                                        }
                                    }else{
                                        String msg = Lang.fileConfig.getString("no-homes-set");
                                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                        return true;
                                    }
                                }
                            }else{
                                if (inventorySection == null) {
                                    String msg = Lang.fileConfig.getString("home-file-error");
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                    return true;
                                } else if (!inventorySection.getKeys(true).isEmpty()){
                                    assert inventorySection != null;
                                    player.sendMessage(ChatColor.GREEN + "---------------------------"
                                            + "\nHome(s) List"
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
                                    player.sendMessage(ChatColor.GREEN + "---------------------------"
                                            + "\nHome(s) List"
                                            + "\n---------------------------");
                                    String msg = Lang.fileConfig.getString("no-homes-set");
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                    return true;
                                }
                            }
                        }
                    }else{
                        String msg = Lang.fileConfig.getString("home-file-error");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    }
                } else {
                    String perm = Lang.fileConfig.getString("no-permission-message").replace("<permission>", "se.home");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', perm));
                    return true;
                }
            }else if (args.length == 2){
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
                if (player.hasPermission("se.home.others") || player.hasPermission("se.all")) {
                    if (args.length == 2) {
                        if (target != null && target.isOnline()) {
                            String targetname = target.getUniqueId().toString();
                            if (target != player) {
                                if (args[0].equalsIgnoreCase(target.getName())) {
                                    // Check if the File Exists and if Location.World has data
                                    if (Sethome.file.exists() && Sethome.fileConfig.getString("Home." + targetname + "." + args[1] + ".World") != null) {
                                        // Gathering Location
                                        float yaw = Sethome.fileConfig.getInt("Home." + targetname + "." + args[1] + ".Yaw");
                                        Location loc = new Location(Bukkit.getWorld(Sethome.fileConfig.getString("Home." + targetname + "." + args[1] + ".World")),
                                                Sethome.fileConfig.getDouble("Home." + targetname + "." + args[1] + ".X"),
                                                Sethome.fileConfig.getDouble("Home." + targetname + "." + args[1] + ".Y"),
                                                Sethome.fileConfig.getDouble("Home." + targetname + "." + args[1] + ".Z"),
                                                yaw, 0);
                                        if (ServerEssentials.plugin.getConfig().getBoolean("home-save")) {
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
                                        if (loc.isWorldLoaded()) {
                                            // Teleporting To Target's Home
                                            player.teleport(loc);
                                            String msg = Lang.fileConfig.getString("home-teleport-target").replace("<target>", target.getName());
                                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                            return true;
                                        } else {
                                            String msg = Lang.fileConfig.getString("home-world-invalid");
                                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                            return true;
                                        }
                                    } else {
                                        String msg = Lang.fileConfig.getString("home-invalid");
                                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                        return true;
                                    }
                                }
                            } else {
                                String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/home (home)");
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            }
                        } else {
                            if (target.hasPlayedBefore()){
                                String targetname = target.getUniqueId().toString();
                                if (target != player) {
                                    if (args[0].equalsIgnoreCase(target.getName())) {
                                        // Check if the File Exists and if Location.World has data
                                        if (Sethome.file.exists() && Sethome.fileConfig.getString("Home." + targetname + "." + args[1] + ".World") != null) {
                                            // Gathering Location
                                            float yaw = Sethome.fileConfig.getInt("Home." + targetname + "." + args[1] + ".Yaw");
                                            Location loc = new Location(Bukkit.getWorld(Sethome.fileConfig.getString("Home." + targetname + "." + args[1] + ".World")),
                                                    Sethome.fileConfig.getDouble("Home." + targetname + "." + args[1] + ".X"),
                                                    Sethome.fileConfig.getDouble("Home." + targetname + "." + args[1] + ".Y"),
                                                    Sethome.fileConfig.getDouble("Home." + targetname + "." + args[1] + ".Z"),
                                                    yaw, 0);
                                            if (ServerEssentials.plugin.getConfig().getBoolean("home-save")) {
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
                                            if (loc.isWorldLoaded()) {
                                                // Teleporting To Target's Home
                                                player.teleport(loc);
                                                String msg = Lang.fileConfig.getString("home-teleport-target").replace("<target>", target.getName());
                                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                                return true;
                                            } else {
                                                String msg = Lang.fileConfig.getString("home-world-invalid");
                                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                                return true;
                                            }
                                        } else {
                                            String msg = Lang.fileConfig.getString("home-invalid");
                                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                            return true;
                                        }
                                    }
                                } else {
                                    String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/home (home)");
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                    return true;
                                }
                            }else{
                                String msg = Lang.fileConfig.getString("player-offline");
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            }
                        }
                    }
                }else{
                    String perm = Lang.fileConfig.getString("no-permission-message").replace("<permission>", "se.home.others");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', perm));
                    return true;
                }
            }else{
                String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/home (home)");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                return true;
            }
        }else{
            String console = Lang.fileConfig.getString("console-invalid");
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', console));
            return true;
        }
        return false;
    }
    public static Location getLocation(String[] args, Player player) {
        UUID name = player.getUniqueId();
        // Gathering Location
        float yaw = Sethome.fileConfig.getInt("Home." + name + "." + args[0] + ".Yaw");
        Location loc = new Location(Bukkit.getWorld(Sethome.fileConfig.getString("Home." + name + "." + args[0] + ".World")),
                Sethome.fileConfig.getDouble("Home." + name + "." + args[0] + ".X"),
                Sethome.fileConfig.getDouble("Home." + name + "." + args[0] + ".Y"),
                Sethome.fileConfig.getDouble("Home." + name + "." + args[0] + ".Z"),
                yaw, 0);
        return loc;
    }
}