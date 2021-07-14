package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
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
        int delay = ServerEssentials.plugin.getConfig().getInt("home-teleport");
        Player player = (Player) sender;
        String name = player.getUniqueId().toString();

        if (args.length == 1) {
            // Checking if the player has the correct permission
            if (player.hasPermission("se.home")) {
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
                            player.teleport(loc);
                            Boolean subtitle = ServerEssentials.plugin.getConfig().getBoolean("enable-warp-subtitle");
                            if (subtitle) {
                                player.sendTitle("Successfully teleported to " + ChatColor.GOLD + args[0], null);
                                return true;
                            } else {
                                player.sendMessage(ChatColor.GREEN + "Successfully teleported to " + ChatColor.GOLD + args[0]);
                                return true;
                            }
                        }else{
                            if (ServerEssentials.plugin.getConfig().getBoolean("home-movement-cancel")){
                                cancel.add(player.getUniqueId());
                                player.sendMessage(ChatColor.GREEN + "Teleporting to " + ChatColor.GOLD + args[0] + ChatColor.GREEN + " in " + ChatColor.GOLD + delay + " Seconds");
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
                                                // Teleporting Player
                                                player.teleport(loc);
                                                Boolean subtitle = ServerEssentials.plugin.getConfig().getBoolean("enable-home-subtitle");
                                                if (subtitle) {
                                                    player.sendTitle("Successfully teleported to " + ChatColor.GOLD + args[0], null);
                                                } else {
                                                    player.sendMessage(ChatColor.GREEN + "Successfully teleported to " + ChatColor.GOLD + args[0]);
                                                }
                                                cancel.remove(player.getUniqueId());
                                            }
                                        }
                                    }
                                }, delay));
                                return true;
                            }else{
                                player.sendMessage(ChatColor.GREEN + "Teleporting to " + ChatColor.GOLD + args[0] + ChatColor.GREEN + " in " + ChatColor.GOLD + delay + " Seconds");
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
                                                // Teleporting Player
                                                player.teleport(loc);
                                                Boolean subtitle = ServerEssentials.plugin.getConfig().getBoolean("enable-home-subtitle");
                                                if (subtitle) {
                                                    player.sendTitle("Successfully teleported to " + ChatColor.GOLD + args[0], null);
                                                } else {
                                                    player.sendMessage(ChatColor.GREEN + "Successfully teleported to " + ChatColor.GOLD + args[0]);
                                                }
                                            }
                                        }
                                    }
                                }, delay));
                                return true;
                            }
                        }
                    }
                } else {
                    player.sendMessage("Home Doesn't Exist!");
                    return true;
                }
            } else {
                if (ServerEssentials.plugin.getConfig().getString("no-permission-message").length() == 0){
                    player.sendMessage(ChatColor.RED + "You do not have the required permission (se.home) to run this command.");
                    return true;
                }else{
                    String permission = ServerEssentials.getPlugin().getConfig().getString("no-permission-message");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', permission));
                }
                return true;
            }
        } else if (args.length == 0) {
            if (player.hasPermission("se.home")){
                if (ServerEssentials.plugin.getConfig().getBoolean("enable-home-gui")){
                    int index = 0;
                    ConfigurationSection inventorySection = Sethome.fileConfig.getConfigurationSection("Home." + name);
                    Integer size = ServerEssentials.plugin.getConfig().getInt("home-gui-size");
                    Inventory inv = Bukkit.createInventory(player, size, ChatColor.AQUA + "Home GUI");
                    if (inventorySection == null){
                        player.sendMessage(ChatColor.RED + "home.yml file is empty or null");
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
                                        loreList.add(ChatColor.DARK_PURPLE + "Click to teleport to " + key);
                                        if (player.hasPermission("se.deletehome")){
                                            loreList.add(ChatColor.RED + "Right Click to Delete Home");
                                        }
                                        meta.setLore(loreList);
                                        item.setItemMeta(meta);
                                        inv.setItem(index, item);
                                        index ++;
                                    }
                                } catch (NullPointerException e) {
                                    player.sendMessage(ChatColor.RED + "No Homes have been set.");
                                }
                                player.openInventory(inv);
                                return true;
                            }else{
                                player.sendMessage(ChatColor.RED + "GUI Size is too small, increase the value in Config!");
                                return true;
                            }
                        }else{
                            player.sendMessage(ChatColor.RED + "No Homes have been set");
                            return true;
                        }
                    }
                }else{
                    ConfigurationSection inventorySection = Sethome.fileConfig.getConfigurationSection("Home." + name);
                    if (inventorySection == null) {
                        player.sendMessage(ChatColor.RED + "home.yml file is empty or null");
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
                        player.sendMessage(ChatColor.RED + "No Homes have been set.");
                        return true;
                    }
                }
            } else {
                if (ServerEssentials.plugin.getConfig().getString("no-permission-message").length() == 0){
                    player.sendMessage(ChatColor.RED + "You do not have the required permission (se.home) to run this command.");
                    return true;
                }else{
                    String permission = ServerEssentials.getPlugin().getConfig().getString("no-permission-message");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', permission));
                }
                return true;
            }
        }else if (args.length == 2){
            Player target = Bukkit.getPlayer(args[0]);
            if (player.hasPermission("se.home.others")) {
                if (args.length == 2) {
                    if (target != null) {
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
                                    // Teleporting To Target's Home
                                    player.teleport(loc);
                                    player.sendMessage("You have been teleported to " + ChatColor.GOLD + target.getName() + "'s" + " home");
                                    return true;
                                } else {
                                    player.sendMessage("Home Doesn't Exist!");
                                    return true;
                                }
                            }
                        } else {
                            player.sendMessage("Incorrect format! Please use /home (name) to teleport to your home");
                            return true;
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "Player does not exist");
                        return true;
                    }
                }
            }else{
                if (ServerEssentials.plugin.getConfig().getString("no-permission-message").length() == 0){
                    player.sendMessage(ChatColor.RED + "You do not have the required permission (se.home.others) to run this command.");
                    return true;
                }else{
                    String permission = ServerEssentials.getPlugin().getConfig().getString("no-permission-message");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', permission));
                }
                return true;
            }
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