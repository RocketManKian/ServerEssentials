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

import static me.rocketmankianproductions.serveressentials.ServerEssentials.hex;

public class Home implements CommandExecutor {

    public static HashMap<UUID, Integer> hometeleport = new HashMap<>();
    public static ArrayList<UUID> cancel = new ArrayList<>();

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            int delay = ServerEssentials.plugin.getConfig().getInt("home-teleport");
            Boolean subtitle = ServerEssentials.plugin.getConfig().getBoolean("enable-home-subtitle");
            Player player = (Player) sender;
            String name = player.getUniqueId().toString();
            if (ServerEssentials.permissionChecker(player, "se.home")) {
                if (args.length == 0) {
                    ConfigurationSection inventorySection = Sethome.fileConfig.getConfigurationSection("Home." + name);
                    if (inventorySection != null) {
                        // If Player only has 1 Home Set
                        if (inventorySection.getKeys(false).size() == 1) {
                            // Gathering Location
                            String home = null;
                            for (String key : inventorySection.getKeys(false)) {
                                home = key;
                            }
                            float yaw = Sethome.fileConfig.getInt("Home." + name + "." + home + ".Yaw");
                            Location loc = new Location(Bukkit.getWorld(Sethome.fileConfig.getString("Home." + name + "." + home + ".World")),
                                    Sethome.fileConfig.getDouble("Home." + name + "." + home + ".X"),
                                    Sethome.fileConfig.getDouble("Home." + name + "." + home + ".Y"),
                                    Sethome.fileConfig.getDouble("Home." + name + "." + home + ".Z"),
                                    yaw, 0);
                            if (ServerEssentials.plugin.getConfig().getInt("home-teleport") == 0) {
                                homeSave(player);
                                if (loc.isWorldLoaded()) {
                                    player.teleport(loc);
                                    if (subtitle) {
                                        String msg = ChatColor.translateAlternateColorCodes('&', hex(Lang.fileConfig.getString("home-subtitle").replace("<home>", home)));
                                        player.sendTitle((msg), null);
                                        return true;
                                    } else {
                                        String msg = Lang.fileConfig.getString("home-message");
                                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg).replace("<home>", home)));
                                        return true;
                                    }
                                } else {
                                    String msg = Lang.fileConfig.getString("home-world-invalid");
                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                                }
                            } else {
                                if (ServerEssentials.plugin.getConfig().getBoolean("home-movement-cancel")) {
                                    cancel.add(player.getUniqueId());
                                    String msg = Lang.fileConfig.getString("home-wait-message").replace("<home>", home).replace("<time>", String.valueOf(delay));
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                                    delay = delay * 20;
                                    if (hometeleport.containsKey(player.getUniqueId()) && hometeleport.get(player.getUniqueId()) != null) {
                                        Bukkit.getScheduler().cancelTask(hometeleport.get(player.getUniqueId()));
                                    }
                                    String finalHome = home;
                                    hometeleport.put(player.getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((ServerEssentials.plugin), new Runnable() {
                                        public void run() {
                                            if (cancel.contains(player.getUniqueId())) {
                                                if (hometeleport.containsKey(player.getUniqueId())) {
                                                    homeSave(player);
                                                    homeTeleport(player, loc, "home-message", subtitle, finalHome);
                                                }
                                            }
                                        }
                                    }, delay));
                                    return true;
                                } else {
                                    String msg = Lang.fileConfig.getString("home-wait-message").replace("<home>", home).replace("<time>", String.valueOf(delay));
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                                    delay = delay * 20;
                                    if (hometeleport.containsKey(player.getUniqueId()) && hometeleport.get(player.getUniqueId()) != null) {
                                        Bukkit.getScheduler().cancelTask(hometeleport.get(player.getUniqueId()));
                                    }
                                    String finalHome = home;
                                    hometeleport.put(player.getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((ServerEssentials.plugin), new Runnable() {
                                        public void run() {
                                            if (hometeleport.containsKey(player.getUniqueId())) {
                                                homeSave(player);
                                                homeTeleport(player, loc, "home-message", subtitle, finalHome);
                                            }
                                        }
                                    }, delay));
                                    return true;
                                }
                            }
                        } else {
                            if (ServerEssentials.plugin.getConfig().getBoolean("enable-home-gui")) {
                                int index = 0;
                                Integer size = ServerEssentials.plugin.getConfig().getInt("home-gui-size");
                                Inventory inv = Bukkit.createInventory(player, size, ChatColor.translateAlternateColorCodes('&', Lang.fileConfig.getString("home-gui-name")));
                                if (inventorySection == null) {
                                    String msg = Lang.fileConfig.getString("home-file-error");
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                                    return true;
                                } else {
                                    assert inventorySection != null;
                                    String homeitem = ServerEssentials.plugin.getConfig().getString("home-item");
                                    ItemStack item = new ItemStack(Material.getMaterial(homeitem));
                                    ItemMeta meta = item.getItemMeta();
                                    if (!inventorySection.getKeys(true).isEmpty()) {
                                        if (!(inventorySection.getKeys(false).size() > ServerEssentials.plugin.getConfig().getInt("home-gui-size"))) {
                                            try {
                                                for (String key : inventorySection.getKeys(false)) {
                                                    String homecolour = ServerEssentials.plugin.getConfig().getString("home-name-colour");
                                                    meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', homecolour + key));
                                                    List<String> loreList = new ArrayList<String>();
                                                    String msg = Lang.fileConfig.getString("home-gui-left-click").replace("<home>", key);
                                                    loreList.add(ChatColor.translateAlternateColorCodes('&', msg));
                                                    if (player.hasPermission("se.deletehome")) {
                                                        String msg2 = Lang.fileConfig.getString("home-gui-right-click").replace("<home>", key);
                                                        loreList.add(ChatColor.translateAlternateColorCodes('&', msg2));
                                                    }
                                                    meta.setLore(loreList);
                                                    item.setItemMeta(meta);
                                                    inv.setItem(index, item);
                                                    index++;
                                                }
                                            } catch (NullPointerException e) {
                                                String msg = Lang.fileConfig.getString("no-homes-set");
                                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                                            }
                                            player.openInventory(inv);
                                            return true;
                                        } else {
                                            String msg = Lang.fileConfig.getString("home-gui-error");
                                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                                            return true;
                                        }
                                    } else {
                                        String msg = Lang.fileConfig.getString("no-homes-set");
                                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                                        return true;
                                    }
                                }
                            } else {
                                if (inventorySection == null) {
                                    String msg = Lang.fileConfig.getString("home-file-error");
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                    return true;
                                } else if (!inventorySection.getKeys(true).isEmpty()) {
                                    assert inventorySection != null;
                                    player.sendMessage(ChatColor.GREEN + "---------------------------"
                                            + "\nHome(s) List"
                                            + "\n---------------------------");
                                    try {
                                        for (String key : inventorySection.getKeys(false)) {
                                            player.sendMessage(ChatColor.GOLD + key);
                                        }
                                    } catch (NullPointerException e) {
                                        e.printStackTrace();
                                    }
                                    return true;
                                } else {
                                    assert inventorySection != null;
                                    player.sendMessage(ChatColor.GREEN + "---------------------------"
                                            + "\nHome(s) List"
                                            + "\n---------------------------");
                                    String msg = Lang.fileConfig.getString("no-homes-set");
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                                    return true;
                                }
                            }
                        }
                    } else {
                        String msg = Lang.fileConfig.getString("home-file-error");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                        return true;
                    }
                } else if (args.length == 1) {
                    // Check if the File Exists and if Location.World has data
                    if (Sethome.file.exists() && Sethome.fileConfig.getString("Home." + name + "." + args[0] + ".World") != null) {
                        Location loc = getLocation(args, player);
                        if (args.length == 1) {
                            if (ServerEssentials.plugin.getConfig().getInt("home-teleport") == 0) {
                                homeSave(player);
                                homeTeleport(player, loc, "home-message", subtitle, args[0]);
                            } else {
                                if (ServerEssentials.plugin.getConfig().getBoolean("home-movement-cancel")) {
                                    cancel.add(player.getUniqueId());
                                    String msg = Lang.fileConfig.getString("home-wait-message").replace("<home>", args[0]).replace("<time>", String.valueOf(delay));
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                                    delay = delay * 20;
                                    if (hometeleport.containsKey(player.getUniqueId()) && hometeleport.get(player.getUniqueId()) != null) {
                                        Bukkit.getScheduler().cancelTask(hometeleport.get(player.getUniqueId()));
                                    }
                                    hometeleport.put(player.getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((ServerEssentials.plugin), new Runnable() {
                                        public void run() {
                                            if (cancel.contains(player.getUniqueId())) {
                                                if (hometeleport.containsKey(player.getUniqueId())) {
                                                    homeSave(player);
                                                    homeTeleport(player, loc, "home-message", subtitle, args[0]);
                                                }
                                            }
                                        }
                                    }, delay));
                                    return true;
                                } else {
                                    String msg = Lang.fileConfig.getString("home-wait-message").replace("<home>", args[0]).replace("<time>", String.valueOf(delay));
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                                    delay = delay * 20;
                                    if (hometeleport.containsKey(player.getUniqueId()) && hometeleport.get(player.getUniqueId()) != null) {
                                        Bukkit.getScheduler().cancelTask(hometeleport.get(player.getUniqueId()));
                                    }
                                    hometeleport.put(player.getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((ServerEssentials.plugin), new Runnable() {
                                        public void run() {
                                            if (hometeleport.containsKey(player.getUniqueId())) {
                                                homeSave(player);
                                                homeTeleport(player, loc, "home-message", subtitle, args[0]);
                                            }
                                        }
                                    }, delay));
                                    return true;
                                }
                            }
                        } else {
                            String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/home (home)");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                            return true;
                        }
                    } else {
                        String msg = Lang.fileConfig.getString("home-invalid").replace("<home>", args[0]);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                        return true;
                    }
                } else if (args.length == 2) {
                    OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
                    if (ServerEssentials.permissionChecker(player, "se.home.others")) {
                        if (args.length == 2) {
                            if (target.hasPlayedBefore()) {
                                String targetname = target.getUniqueId().toString();
                                if (target != player) {
                                    if (args[0].equalsIgnoreCase(target.getName())) {
                                        // Check if the File Exists and if Location.World has data
                                        if (Sethome.file.exists() && Sethome.fileConfig.getString("Home." + targetname + "." + args[1] + ".World") != null) {
                                            if (ServerEssentials.plugin.getConfig().getInt("home-teleport") == 0) {
                                                // Gathering Location
                                                float yaw = Sethome.fileConfig.getInt("Home." + targetname + "." + args[1] + ".Yaw");
                                                Location loc = new Location(Bukkit.getWorld(Sethome.fileConfig.getString("Home." + targetname + "." + args[1] + ".World")),
                                                        Sethome.fileConfig.getDouble("Home." + targetname + "." + args[1] + ".X"),
                                                        Sethome.fileConfig.getDouble("Home." + targetname + "." + args[1] + ".Y"),
                                                        Sethome.fileConfig.getDouble("Home." + targetname + "." + args[1] + ".Z"),
                                                        yaw, 0);
                                                homeSave(player);
                                                if (loc.isWorldLoaded()) {
                                                    // Teleporting To Target's Home
                                                    player.teleport(loc);
                                                    String msg = Lang.fileConfig.getString("home-teleport-target").replace("<target>", target.getName());
                                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                                                    return true;
                                                } else {
                                                    String msg = Lang.fileConfig.getString("home-world-invalid");
                                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                                                    return true;
                                                }
                                            } else {
                                                // If Movement Cancel is Enabled in Config
                                                if (ServerEssentials.plugin.getConfig().getBoolean("home-movement-cancel")) {
                                                    cancel.add(player.getUniqueId());
                                                    String msg = Lang.fileConfig.getString("target-home-wait-message").replace("<target>", args[0]).replace("<time>", String.valueOf(delay));
                                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                                                    delay = delay * 20;
                                                    if (hometeleport.containsKey(player.getUniqueId()) && hometeleport.get(player.getUniqueId()) != null) {
                                                        Bukkit.getScheduler().cancelTask(hometeleport.get(player.getUniqueId()));
                                                    }
                                                    hometeleport.put(player.getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((ServerEssentials.plugin), new Runnable() {
                                                        public void run() {
                                                            if (cancel.contains(player.getUniqueId())) {
                                                                if (hometeleport.containsKey(player.getUniqueId())) {
                                                                    homeSave(player);
                                                                    // Gathering Location
                                                                    float yaw = Sethome.fileConfig.getInt("Home." + targetname + "." + args[1] + ".Yaw");
                                                                    Location loc = new Location(Bukkit.getWorld(Sethome.fileConfig.getString("Home." + targetname + "." + args[1] + ".World")),
                                                                            Sethome.fileConfig.getDouble("Home." + targetname + "." + args[1] + ".X"),
                                                                            Sethome.fileConfig.getDouble("Home." + targetname + "." + args[1] + ".Y"),
                                                                            Sethome.fileConfig.getDouble("Home." + targetname + "." + args[1] + ".Z"),
                                                                            yaw, 0);
                                                                    if (loc.isWorldLoaded()) {
                                                                        // Teleporting To Target's Home
                                                                        player.teleport(loc);
                                                                        String msg = Lang.fileConfig.getString("home-teleport-target").replace("<target>", target.getName());
                                                                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                                                                    } else {
                                                                        String msg = Lang.fileConfig.getString("home-world-invalid");
                                                                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                                                                    }
                                                                    cancel.remove(player.getUniqueId());
                                                                }
                                                            }
                                                        }
                                                    }, delay));
                                                    return true;
                                                } else {
                                                    String msg = Lang.fileConfig.getString("target-home-wait-message").replace("<target>", args[0]).replace("<time>", String.valueOf(delay));
                                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                                    delay = delay * 20;
                                                    if (hometeleport.containsKey(player.getUniqueId()) && hometeleport.get(player.getUniqueId()) != null) {
                                                        Bukkit.getScheduler().cancelTask(hometeleport.get(player.getUniqueId()));
                                                    }
                                                    hometeleport.put(player.getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((ServerEssentials.plugin), new Runnable() {
                                                        public void run() {
                                                            if (hometeleport.containsKey(player.getUniqueId())) {
                                                                homeSave(player);
                                                                // Gathering Location
                                                                float yaw = Sethome.fileConfig.getInt("Home." + targetname + "." + args[1] + ".Yaw");
                                                                Location loc = new Location(Bukkit.getWorld(Sethome.fileConfig.getString("Home." + targetname + "." + args[1] + ".World")),
                                                                        Sethome.fileConfig.getDouble("Home." + targetname + "." + args[1] + ".X"),
                                                                        Sethome.fileConfig.getDouble("Home." + targetname + "." + args[1] + ".Y"),
                                                                        Sethome.fileConfig.getDouble("Home." + targetname + "." + args[1] + ".Z"),
                                                                        yaw, 0);
                                                                if (loc.isWorldLoaded()) {
                                                                    // Teleporting To Target's Home
                                                                    player.teleport(loc);
                                                                    String msg = Lang.fileConfig.getString("home-teleport-target").replace("<target>", target.getName());
                                                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                                                                } else {
                                                                    String msg = Lang.fileConfig.getString("home-world-invalid");
                                                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                                                                }
                                                            }
                                                        }
                                                    }, delay));
                                                    return true;
                                                }
                                            }
                                        } else {
                                            String msg = Lang.fileConfig.getString("home-invalid");
                                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                                            return true;
                                        }
                                    } else {
                                        String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/home (home)");
                                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                                        return true;
                                    }
                                } else {
                                    String msg = Lang.fileConfig.getString("player-offline");
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                                    return true;
                                }
                            }
                        }
                    } else {
                        String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/home (home)");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                        return true;
                    }
                }
            }
        }else{
            String console = Lang.fileConfig.getString("console-invalid");
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

    public static void homeSave(Player player){
        if (ServerEssentials.plugin.getConfig().getBoolean("home-save")) {
            Back.location.put(player.getUniqueId(), player.getLocation());
        } else if (player.hasPermission("se.back.bypass")) {
            Back.location.put(player.getUniqueId(), player.getLocation());
        }
    }

    public static void homeTeleport(Player player, Location loc, String lang, Boolean subtitle, String finalHome){
        if (subtitle){
            if (loc.isWorldLoaded()) {
                // Teleporting Player
                player.teleport(loc);
                String msg = Lang.fileConfig.getString("home-subtitle").replace("<home>", finalHome);
                player.sendTitle(hex(msg), null);
            } else {
                String msg = ChatColor.translateAlternateColorCodes('&', hex(Lang.fileConfig.getString("home-world-invalid")));
                player.sendMessage(msg);
            }
        }else{
            if (loc.isWorldLoaded()) {
                // Teleporting Player
                player.teleport(loc);
                String msg = Lang.fileConfig.getString(lang);
                player.sendMessage(hex(msg).replace("<home>", finalHome));
            } else {
                String msg = hex(Lang.fileConfig.getString("home-world-invalid"));
                player.sendMessage(msg);
            }
        }
        cancel.remove(player.getUniqueId());
        hometeleport.remove(player.getUniqueId());
    }
}