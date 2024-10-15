package me.rocketmankianproductions.serveressentials.events;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.commands.*;
import me.rocketmankianproductions.serveressentials.file.Lang;
import me.rocketmankianproductions.serveressentials.utils.CompatibilityUtil;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.util.UUID;

import static me.rocketmankianproductions.serveressentials.ServerEssentials.hex;

public class PlayerClickEvent implements Listener {

    String home2 = null;
    String targethome2 = null;
    String warp2 = null;

    @EventHandler
    public void onClick(InventoryClickEvent e){
        Player player = (Player) e.getWhoClicked();
        String inventoryTitle = CompatibilityUtil.getTitle(e);
        if (inventoryTitle.equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', Lang.fileConfig.getString("invsee-armor-gui")))){
            e.setCancelled(true);
        }else if (inventoryTitle.equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', Lang.fileConfig.getString("warp-gui-name")))){
            e.setCancelled(true);
            ItemStack item = e.getCurrentItem();
            if (item != null){
                String warp = item.getItemMeta().getDisplayName();
                warp = ChatColor.stripColor(warp);
                if (e.getClick()== ClickType.RIGHT) {
                    confirmDenyGUI(player, "warp", warp);
                    warp2 = warp;
                }else if (e.getClick()==ClickType.LEFT) {
                    if (player.hasPermission("se.warps.all") || ServerEssentials.permissionChecker(player, "se.warps." + warp)) {
                        if (ServerEssentials.plugin.getConfig().getInt("warp-teleport") == 0){
                            Location loc = getWarpLocation(warp, player);
                            Warp.warpSave(player);
                            if (loc.isWorldLoaded()){
                                // Teleporting Player
                                player.teleport(loc);
                                Boolean subtitle = ServerEssentials.plugin.getConfig().getBoolean("enable-warp-subtitle");
                                if (subtitle) {
                                    String msg = Lang.fileConfig.getString("warp-subtitle").replace("<warp>", warp);
                                    player.sendTitle(ChatColor.translateAlternateColorCodes('&', hex(msg)), null);
                                } else {
                                    String msg = Lang.fileConfig.getString("warp-message").replace("<warp>", warp);
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                                }
                            }else{
                                String msg = Lang.fileConfig.getString("warp-world-invalid");
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                            }
                            player.closeInventory();
                        }else{
                            if (ServerEssentials.plugin.getConfig().getBoolean("warp-movement-cancel")){
                                Warp.cancel.add(player.getUniqueId());
                                Location loc = getWarpLocation(warp, player);
                                String finalWarp = warp;
                                int seconds = ServerEssentials.plugin.getConfig().getInt("warp-teleport");
                                String msg = Lang.fileConfig.getString("warp-wait-message").replace("<warp>", warp).replace("<time>", String.valueOf(seconds));
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                                seconds = seconds * 20;
                                if (Warp.warpteleport.containsKey(player.getUniqueId()) && Warp.warpteleport.get(player.getUniqueId()) != null) {
                                    Bukkit.getScheduler().cancelTask(Warp.warpteleport.get(player.getUniqueId()));
                                }
                                Warp.warpteleport.put(player.getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((ServerEssentials.plugin), new Runnable() {
                                    public void run() {
                                        if (Warp.cancel.contains(player.getUniqueId())){
                                            if (Warp.warpteleport.containsKey(player.getUniqueId())) {
                                                Warp.warpSave(player);
                                                if (loc.isWorldLoaded()){
                                                    // Teleporting Player
                                                    player.teleport(loc);
                                                    Boolean subtitle = ServerEssentials.plugin.getConfig().getBoolean("enable-warp-subtitle");
                                                    if (subtitle) {
                                                        String msg = Lang.fileConfig.getString("warp-subtitle").replace("<warp>", finalWarp);
                                                        player.sendTitle(ChatColor.translateAlternateColorCodes('&', hex(msg)), null);
                                                    } else {
                                                        String msg = Lang.fileConfig.getString("warp-message").replace("<warp>", finalWarp);
                                                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                                                    }
                                                }else{
                                                    String msg = Lang.fileConfig.getString("warp-world-invalid");
                                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                                                }
                                                Warp.cancel.remove(player.getUniqueId());
                                            }
                                        }
                                    }
                                }, seconds));
                                player.closeInventory();
                            }else{
                                Location loc = getWarpLocation(warp, player);
                                String finalWarp = warp;
                                int seconds = ServerEssentials.plugin.getConfig().getInt("warp-teleport");
                                String msg = Lang.fileConfig.getString("warp-wait-message").replace("<warp>", warp).replace("<time>", String.valueOf(seconds));
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                                seconds = seconds * 20;
                                if (Warp.warpteleport.containsKey(player.getUniqueId()) && Warp.warpteleport.get(player.getUniqueId()) != null) {
                                    Bukkit.getScheduler().cancelTask(Warp.warpteleport.get(player.getUniqueId()));
                                }
                                Warp.warpteleport.put(player.getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((ServerEssentials.plugin), new Runnable() {
                                    public void run() {
                                        if (Warp.warpteleport.containsKey(player.getUniqueId())) {
                                            Warp.warpSave(player);
                                            if (loc.isWorldLoaded()){
                                                // Teleporting Player
                                                player.teleport(loc);
                                                Boolean subtitle = ServerEssentials.plugin.getConfig().getBoolean("enable-warp-subtitle");
                                                if (subtitle) {
                                                    String msg = Lang.fileConfig.getString("warp-subtitle").replace("<warp>", finalWarp);
                                                    player.sendTitle(ChatColor.translateAlternateColorCodes('&', hex(msg)), null);
                                                } else {
                                                    String msg = Lang.fileConfig.getString("warp-message").replace("<warp>", finalWarp);
                                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                                                }
                                            }else{
                                                String msg = Lang.fileConfig.getString("warp-world-invalid");
                                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                                            }
                                        }
                                    }
                                }, seconds));
                                player.closeInventory();
                            }
                        }
                    }
                }
            }
        }else if (inventoryTitle.equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', Lang.fileConfig.getString("home-gui-name")))) {
            Boolean subtitle = ServerEssentials.plugin.getConfig().getBoolean("enable-home-subtitle");
            e.setCancelled(true);
            ItemStack item = e.getCurrentItem();
            if (item != null) {
                String home = item.getItemMeta().getDisplayName();
                home = ChatColor.stripColor(home);
                if (e.getClick() == ClickType.RIGHT) {
                    confirmDenyGUI(player, "home", home);
                    home2 = home;
                } else if (e.getClick() == ClickType.LEFT) {
                    if (ServerEssentials.plugin.getConfig().getInt("home-teleport") == 0) {
                        Location loc = getHomeLocation(home, player);
                        Home.homeSave(player);
                        if (loc != null){
                            Home.homeTeleport(player, loc, "home-message", subtitle, home);
                            player.closeInventory();
                        }else{
                            player.sendMessage(Lang.fileConfig.getString("home-invalid").replace("<home>", home));
                            player.closeInventory();
                        }
                    } else {
                        if (ServerEssentials.plugin.getConfig().getBoolean("home-movement-cancel")){
                            Location loc = getHomeLocation(home, player);
                            if (loc != null){
                                Home.cancel.add(player.getUniqueId());
                                int seconds = ServerEssentials.plugin.getConfig().getInt("home-teleport");
                                String msg = Lang.fileConfig.getString("home-wait-message").replace("<home>", home).replace("<time>", String.valueOf(seconds));
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                                seconds = seconds * 20;
                                if (Home.hometeleport.containsKey(player.getUniqueId()) && Home.hometeleport.get(player.getUniqueId()) != null) {
                                    Bukkit.getScheduler().cancelTask(Home.hometeleport.get(player.getUniqueId()));
                                }
                                String finalHome = home;
                                Home.hometeleport.put(player.getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((ServerEssentials.plugin), new Runnable() {
                                    public void run() {
                                        if (Home.cancel.contains(player.getUniqueId())){
                                            if (Home.hometeleport.containsKey(player.getUniqueId())) {
                                                Home.homeSave(player);
                                                Home.homeTeleport(player, loc, "home-message", subtitle, finalHome);
                                            }
                                        }
                                    }
                                }, seconds));
                                player.closeInventory();
                            }else{
                                player.sendMessage(Lang.fileConfig.getString("home-invalid").replace("<home>", home));
                                player.closeInventory();
                            }
                        }else{
                            int seconds = ServerEssentials.plugin.getConfig().getInt("home-teleport");
                            String msg = Lang.fileConfig.getString("home-wait-message").replace("<home>", home).replace("<time>", String.valueOf(seconds));
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                            seconds = seconds * 20;
                            Location loc = getHomeLocation(home, player);
                            if (Home.hometeleport.containsKey(player.getUniqueId()) && Home.hometeleport.get(player.getUniqueId()) != null) {
                                Bukkit.getScheduler().cancelTask(Home.hometeleport.get(player.getUniqueId()));
                            }
                            String finalHome = home;
                            Home.hometeleport.put(player.getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((ServerEssentials.plugin), new Runnable() {
                                public void run() {
                                    if (Home.hometeleport.containsKey(player.getUniqueId())) {
                                        Home.homeSave(player);
                                        Home.homeTeleport(player, loc, "home-message", subtitle, finalHome);
                                    }
                                }
                            }, seconds));
                            player.closeInventory();
                        }
                    }
                }
            }
        }else if (ListHomes.target != null && inventoryTitle.equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', Lang.fileConfig.getString("target-home-gui-name").replace("<target>", ListHomes.target.getName())))) {
            Boolean subtitle = ServerEssentials.plugin.getConfig().getBoolean("enable-home-subtitle");
            e.setCancelled(true);
            ItemStack item = e.getCurrentItem();
            if (item != null) {
                String home = item.getItemMeta().getDisplayName();
                home = ChatColor.stripColor(home);
                if (e.getClick() == ClickType.RIGHT) {
                    String deletehomeguiname = Lang.fileConfig.getString("target-delete-home-gui-name").replace("<target>", ListHomes.target.getName()).replace("<home>", home);
                    Inventory confirm = Bukkit.createInventory(player.getPlayer(), 27, ChatColor.translateAlternateColorCodes('&', deletehomeguiname));
                    ItemStack confirmitem = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
                    ItemStack cancelitem = new ItemStack(Material.RED_STAINED_GLASS_PANE);
                    ItemStack idleitem = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
                    ItemMeta idleitemmeta = idleitem.getItemMeta();
                    ItemMeta confirmitemmeta = confirmitem.getItemMeta();
                    ItemMeta cancelitemmeta = cancelitem.getItemMeta();
                    String confirmname = Lang.fileConfig.getString("gui-confirm-name");
                    String cancelname = Lang.fileConfig.getString("gui-deny-name");
                    confirmitemmeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', confirmname));
                    cancelitemmeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', cancelname));
                    idleitemmeta.setDisplayName(ChatColor.DARK_GRAY + "");
                    confirmitem.setItemMeta(confirmitemmeta);
                    cancelitem.setItemMeta(cancelitemmeta);
                    idleitem.setItemMeta(idleitemmeta);
                    for (int counter = 0; counter <= 26; counter++) {
                        confirm.setItem(counter, idleitem);
                    }
                    confirm.setItem(11, confirmitem);
                    confirm.setItem(15, cancelitem);
                    player.openInventory(confirm);
                    targethome2 = home;
                } else if (e.getClick() == ClickType.LEFT) {
                    if (ServerEssentials.plugin.getConfig().getInt("home-teleport") == 0) {
                        Location loc = getHomeLocation(home, ListHomes.target);
                        Home.homeSave(player);
                        Home.homeTeleport(player, loc, "home-message", subtitle, home);
                        player.closeInventory();
                    } else {
                        if (ServerEssentials.plugin.getConfig().getBoolean("home-movement-cancel")){
                            Home.cancel.add(player.getUniqueId());
                            int seconds = ServerEssentials.plugin.getConfig().getInt("home-teleport");
                            String msg = Lang.fileConfig.getString("target-home-wait-message").replace("<target>", ListHomes.target.getName()).replace("<time>", String.valueOf(seconds));
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                            seconds = seconds * 20;
                            Location loc = getHomeLocation(home, ListHomes.target);
                            if (Home.hometeleport.containsKey(player.getUniqueId()) && Home.hometeleport.get(player.getUniqueId()) != null) {
                                Bukkit.getScheduler().cancelTask(Home.hometeleport.get(player.getUniqueId()));
                            }
                            String finalHome = home;
                            Home.hometeleport.put(player.getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((ServerEssentials.plugin), new Runnable() {
                                public void run() {
                                    if (Home.cancel.contains(player.getUniqueId())){
                                        if (Home.hometeleport.containsKey(player.getUniqueId())) {
                                            Home.homeSave(player);
                                            Home.homeTeleport(player, loc, "home-message", subtitle, finalHome);
                                        }
                                    }
                                }
                            }, seconds));
                            player.closeInventory();
                        }else{
                            int seconds = ServerEssentials.plugin.getConfig().getInt("home-teleport");
                            String msg = Lang.fileConfig.getString("target-home-wait-message").replace("<target>", ListHomes.target.getName()).replace("<time>", String.valueOf(seconds));
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                            seconds = seconds * 20;
                            Location loc = getHomeLocation(home, ListHomes.target);
                            if (Home.hometeleport.containsKey(player.getUniqueId()) && Home.hometeleport.get(player.getUniqueId()) != null) {
                                Bukkit.getScheduler().cancelTask(Home.hometeleport.get(player.getUniqueId()));
                            }
                            String finalHome = home;
                            Home.hometeleport.put(player.getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((ServerEssentials.plugin), new Runnable() {
                                public void run() {
                                    if (Home.hometeleport.containsKey(player.getUniqueId())) {
                                        Home.homeSave(player);
                                        Home.homeTeleport(player, loc, "home-message", subtitle, finalHome);
                                    }
                                }
                            }, seconds));
                            player.closeInventory();
                        }
                    }
                }
            }
        }
        // Delete Home Confirm GUI
        String home3 = home2;
        if (home3 != null){
            if (inventoryTitle.equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', Lang.fileConfig.getString("delete-home-gui-name").replace("<home>", home3)))) {
                if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR || e.getCurrentItem().getType() == Material.GRAY_STAINED_GLASS_PANE){
                    e.setCancelled(true);
                    return;
                }
                if (e.getCurrentItem().getType() == Material.LIME_STAINED_GLASS_PANE) {
                    e.setCancelled(true);
                    if (ServerEssentials.permissionChecker(player, "se.deletehome")) {
                        Player target = (Player) e.getInventory().getHolder();
                        UUID targetname = target.getUniqueId();
                        Sethome.fileConfig.set("Home." + targetname + "." + home3, null);
                        try {
                            Sethome.fileConfig.save(Sethome.file);
                        } catch (IOException i) {
                            i.printStackTrace();
                        }
                        String msg = Lang.fileConfig.getString("home-deletion-success").replace("<home>", home3);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                        player.closeInventory();
                    }else{
                        e.setCancelled(true);
                        player.closeInventory();
                    }
                } else if (e.getCurrentItem().getType() == Material.RED_STAINED_GLASS_PANE) {
                    e.setCancelled(true);
                    player.closeInventory();
                }
            }
        }
        // Delete Target Home Confirm GUI
        String targethome3 = targethome2;
        if (targethome3 != null){
            if (inventoryTitle.equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', Lang.fileConfig.getString("target-delete-home-gui-name").replace("<target>", ListHomes.target.getName()).replace("<home>", targethome3)))) {
                if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR || e.getCurrentItem().getType() == Material.GRAY_STAINED_GLASS_PANE){
                    e.setCancelled(true);
                    return;
                }
                if (e.getCurrentItem().getType() == Material.LIME_STAINED_GLASS_PANE) {
                    e.setCancelled(true);
                    if (ServerEssentials.permissionChecker(player, "se.deletehome.others")) {
                        OfflinePlayer target = ListHomes.target;
                        UUID targetname = target.getUniqueId();
                        Sethome.fileConfig.set("Home." + targetname + "." + targethome3, null);
                        try {
                            Sethome.fileConfig.save(Sethome.file);
                        } catch (IOException i) {
                            i.printStackTrace();
                        }
                        String msg = Lang.fileConfig.getString("target-home-deletion-success").replace("<target>", target.getName());
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                        player.closeInventory();
                    }else{
                        e.setCancelled(true);
                        player.closeInventory();
                    }
                } else if (e.getCurrentItem().getType() == Material.RED_STAINED_GLASS_PANE) {
                    e.setCancelled(true);
                    player.closeInventory();
                }
            }
        }
        // Delete Warp Confirm GUI
        String warp3 = warp2;
        if (warp3 != null){
            if (inventoryTitle.equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', Lang.fileConfig.getString("delete-warp-gui-name").replace("<warp>", warp3)))) {
                if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR || e.getCurrentItem().getType() == Material.GRAY_STAINED_GLASS_PANE){
                    e.setCancelled(true);
                    return;
                }
                if (e.getCurrentItem().getType() == Material.LIME_STAINED_GLASS_PANE) {
                    e.setCancelled(true);
                    if (ServerEssentials.permissionChecker(player, "se.deletewarp")) {
                        Setwarp.fileConfig.set("Warp." + warp3, null);
                        try {
                            Setwarp.fileConfig.save(Setwarp.file);
                        } catch (IOException i) {
                            i.printStackTrace();
                        }
                        String msg = Lang.fileConfig.getString("warp-deletion-success").replace("<warp>", warp3);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                        player.closeInventory();
                    }else{
                        e.setCancelled(true);
                        player.closeInventory();
                    }
                } else if (e.getCurrentItem().getType() == Material.RED_STAINED_GLASS_PANE) {
                    e.setCancelled(true);
                    player.closeInventory();
                }
            }
        }
        if (inventoryTitle.equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', "&b&l" + Invsee.targetName.get(player) + "'s Inventory"))){
            e.setCancelled(true);
        }
    }
    public static Location getWarpLocation(String warp, OfflinePlayer player){
        // Gathering Location
        float yaw = Setwarp.fileConfig.getInt("Warp." + warp + ".Yaw");
        float pitch = Sethome.fileConfig.getInt("Home." + player.getName() + ".Pitch");
        Location loc = new Location(Bukkit.getWorld(Setwarp.fileConfig.getString("Warp." + warp + ".World")),
                Setwarp.fileConfig.getDouble("Warp." + warp + ".X"),
                Setwarp.fileConfig.getDouble("Warp." + warp + ".Y"),
                Setwarp.fileConfig.getDouble("Warp." + warp + ".Z"),
                yaw, pitch);
        return loc;
    }
    public static Location getHomeLocation(String home, OfflinePlayer player) {
        UUID name = player.getUniqueId();
        // Gathering Location
        float yaw = Sethome.fileConfig.getInt("Home." + name + "." + home + ".Yaw");
        if (Sethome.fileConfig.getString("Home." + name + "." + home + ".World") == null){
            return null;
        }else{
            Location loc = new Location(Bukkit.getWorld(Sethome.fileConfig.getString("Home." + name + "." + home + ".World")),
                    Sethome.fileConfig.getDouble("Home." + name + "." + home + ".X"),
                    Sethome.fileConfig.getDouble("Home." + name + "." + home + ".Y"),
                    Sethome.fileConfig.getDouble("Home." + name + "." + home + ".Z"),
                    yaw, 0);
            return loc;
        }
    }

    public static void confirmDenyGUI (Player player, String homewarp, String value){
        String deletewarphomeguiname = null;
        if (homewarp.equalsIgnoreCase("home")){
            deletewarphomeguiname = Lang.fileConfig.getString("delete-home-gui-name").replace("<home>", value);
        }else if (homewarp.equalsIgnoreCase("warp")){
            deletewarphomeguiname = Lang.fileConfig.getString("delete-warp-gui-name").replace("<warp>", value);
        }
        Inventory confirm = Bukkit.createInventory(player.getPlayer(), 27, ChatColor.translateAlternateColorCodes('&', deletewarphomeguiname));
        ItemStack confirmitem = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
        ItemStack cancelitem = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemStack idleitem = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta idleitemmeta = idleitem.getItemMeta();
        ItemMeta confirmitemmeta = confirmitem.getItemMeta();
        ItemMeta cancelitemmeta = cancelitem.getItemMeta();
        String confirmname = Lang.fileConfig.getString("gui-confirm-name");
        String cancelname = Lang.fileConfig.getString("gui-deny-name");
        confirmitemmeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', confirmname));
        cancelitemmeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', cancelname));
        idleitemmeta.setDisplayName(ChatColor.DARK_GRAY + "");
        confirmitem.setItemMeta(confirmitemmeta);
        cancelitem.setItemMeta(cancelitemmeta);
        idleitem.setItemMeta(idleitemmeta);
        for (int counter = 0; counter <= 26; counter++) {
            confirm.setItem(counter, idleitem);
        }
        confirm.setItem(11, confirmitem);
        confirm.setItem(15, cancelitem);
        player.openInventory(confirm);
    }
}