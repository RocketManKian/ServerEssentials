package me.rocketmankianproductions.serveressentials.events;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.commands.*;
import me.rocketmankianproductions.serveressentials.file.Lang;
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
import java.util.HashMap;
import java.util.UUID;

public class PlayerClickEvent implements Listener {

    private static HashMap<UUID, Integer> hometeleport = new HashMap<>();
    private static HashMap<UUID, Integer> warpteleport = new HashMap<>();
    String home2 = null;
    String targethome2 = null;
    String warp2 = null;

    @EventHandler
    public void onClick(InventoryClickEvent e){
        Player player = (Player) e.getWhoClicked();
        if (e.getView().getTitle().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', Lang.fileConfig.getString("invsee-armor-gui")))){
            e.setCancelled(true);
        }else if (e.getView().getTitle().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', Lang.fileConfig.getString("warp-gui-name")))){
            e.setCancelled(true);
            ItemStack item = e.getCurrentItem();
            if (item != null){
                String warp = item.getItemMeta().getDisplayName();
                warp = ChatColor.stripColor(warp);
                if (e.getClick()== ClickType.RIGHT) {
                    String deletewarpguiname = Lang.fileConfig.getString("delete-warp-gui-name").replace("<warp>", warp);
                    Inventory confirm = Bukkit.createInventory(player.getPlayer(), 27, ChatColor.translateAlternateColorCodes('&', deletewarpguiname));
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
                    warp2 = warp;
                }else if (e.getClick()==ClickType.LEFT) {
                    if (player.hasPermission("se.warps." + warp) || player.hasPermission("se.warps.all")) {
                        if (ServerEssentials.plugin.getConfig().getInt("warp-teleport") == 0){
                            Location loc = getWarpLocation(warp, player);
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
                                    String msg = Lang.fileConfig.getString("warp-subtitle").replace("<warp>", warp);
                                    player.sendTitle(ChatColor.translateAlternateColorCodes('&', msg), null);
                                } else {
                                    String msg = Lang.fileConfig.getString("warp-message").replace("<warp>", warp);
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                }
                            }else{
                                String msg = Lang.fileConfig.getString("warp-world-invalid");
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                            }
                            player.closeInventory();
                        }else{
                            if (ServerEssentials.plugin.getConfig().getBoolean("warp-movement-cancel")){
                                Warp.cancel.add(player.getUniqueId());
                                Location loc = getWarpLocation(warp, player);
                                String finalWarp = warp;
                                int seconds = ServerEssentials.plugin.getConfig().getInt("warp-teleport");
                                String msg = Lang.fileConfig.getString("warp-wait-message").replace("<warp>", warp).replace("<time>", String.valueOf(seconds));
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                seconds = seconds * 20;
                                if (warpteleport.containsKey(player.getUniqueId()) && warpteleport.get(player.getUniqueId()) != null) {
                                    Bukkit.getScheduler().cancelTask(warpteleport.get(player.getUniqueId()));
                                }
                                warpteleport.put(player.getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((ServerEssentials.plugin), new Runnable() {
                                    public void run() {
                                        if (Warp.cancel.contains(player.getUniqueId())){
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
                                                        String msg = Lang.fileConfig.getString("warp-subtitle").replace("<warp>", finalWarp);
                                                        player.sendTitle(ChatColor.translateAlternateColorCodes('&', msg), null);
                                                    } else {
                                                        String msg = Lang.fileConfig.getString("warp-message").replace("<warp>", finalWarp);
                                                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                                    }
                                                }else{
                                                    String msg = Lang.fileConfig.getString("warp-world-invalid");
                                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
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
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                seconds = seconds * 20;
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
                                                    String msg = Lang.fileConfig.getString("warp-subtitle").replace("<warp>", finalWarp);
                                                    player.sendTitle(ChatColor.translateAlternateColorCodes('&', msg), null);
                                                } else {
                                                    String msg = Lang.fileConfig.getString("warp-message").replace("<warp>", finalWarp);
                                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                                }
                                            }else{
                                                String msg = Lang.fileConfig.getString("warp-world-invalid");
                                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                            }
                                        }
                                    }
                                }, seconds));
                                player.closeInventory();
                            }
                        }
                    } else {
                        String perm = Lang.fileConfig.getString("no-permission-message").replace("<permission>", "se.warps." + warp);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', perm));
                    }
                }
            }
        }else if (e.getView().getTitle().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', Lang.fileConfig.getString("home-gui-name")))) {
            e.setCancelled(true);
            ItemStack item = e.getCurrentItem();
            if (item != null) {
                String home = item.getItemMeta().getDisplayName();
                home = ChatColor.stripColor(home);
                if (e.getClick() == ClickType.RIGHT) {
                    String deletehomeguiname = Lang.fileConfig.getString("delete-home-gui-name").replace("<home>", home);
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
                    home2 = home;
                } else if (e.getClick() == ClickType.LEFT) {
                    if (ServerEssentials.plugin.getConfig().getInt("home-teleport") == 0) {
                        Location loc = getHomeLocation(home, player);
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
                            // Teleporting Player
                            player.teleport(loc);
                            Boolean subtitle = ServerEssentials.plugin.getConfig().getBoolean("enable-home-subtitle");
                            if (subtitle) {
                                String msg = Lang.fileConfig.getString("home-subtitle").replace("<home>", home);
                                player.sendTitle(ChatColor.translateAlternateColorCodes('&', msg), null);
                            } else {
                                String msg = Lang.fileConfig.getString("home-message").replace("<home>", home);
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                            }
                        }else{
                            String msg = Lang.fileConfig.getString("home-world-invalid");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        }
                        player.closeInventory();
                    } else {
                        if (ServerEssentials.plugin.getConfig().getBoolean("home-movement-cancel")){
                            Home.cancel.add(player.getUniqueId());
                            int seconds = ServerEssentials.plugin.getConfig().getInt("home-teleport");
                            String msg = Lang.fileConfig.getString("home-wait-message").replace("<home>", home).replace("<time>", String.valueOf(seconds));
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                            seconds = seconds * 20;
                            Location loc = getHomeLocation(home, player);
                            if (hometeleport.containsKey(player.getUniqueId()) && hometeleport.get(player.getUniqueId()) != null) {
                                Bukkit.getScheduler().cancelTask(hometeleport.get(player.getUniqueId()));
                            }
                            String finalHome = home;
                            hometeleport.put(player.getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((ServerEssentials.plugin), new Runnable() {
                                public void run() {
                                    if (Home.cancel.contains(player.getUniqueId())){
                                        if (hometeleport.containsKey(player.getUniqueId())) {
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
                                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                            }
                                            Home.cancel.remove(player.getUniqueId());
                                        }
                                    }
                                }
                            }, seconds));
                            player.closeInventory();
                        }else{
                            int seconds = ServerEssentials.plugin.getConfig().getInt("home-teleport");
                            String msg = Lang.fileConfig.getString("home-wait-message").replace("<home>", home).replace("<time>", String.valueOf(seconds));
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                            seconds = seconds * 20;
                            Location loc = getHomeLocation(home, player);
                            if (hometeleport.containsKey(player.getUniqueId()) && hometeleport.get(player.getUniqueId()) != null) {
                                Bukkit.getScheduler().cancelTask(hometeleport.get(player.getUniqueId()));
                            }
                            String finalHome = home;
                            hometeleport.put(player.getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((ServerEssentials.plugin), new Runnable() {
                                public void run() {
                                    if (hometeleport.containsKey(player.getUniqueId())) {
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
                                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                        }
                                    }
                                }
                            }, seconds));
                            player.closeInventory();
                        }
                    }
                }
            }
        }else if (ListHomes.target != null && e.getView().getTitle().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', Lang.fileConfig.getString("target-home-gui-name").replace("<target>", ListHomes.target.getName())))) {
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
                            // Teleporting Player
                            player.teleport(loc);
                            Boolean subtitle = ServerEssentials.plugin.getConfig().getBoolean("enable-home-subtitle");
                            if (subtitle) {
                                String msg = Lang.fileConfig.getString("home-subtitle").replace("<home>", home);
                                player.sendTitle(ChatColor.translateAlternateColorCodes('&', msg), null);
                            } else {
                                String msg = Lang.fileConfig.getString("home-message").replace("<home>", home);
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                            }
                        }else{
                            String msg = Lang.fileConfig.getString("home-world-invalid");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        }
                        player.closeInventory();
                    } else {
                        if (ServerEssentials.plugin.getConfig().getBoolean("home-movement-cancel")){
                            Home.cancel.add(player.getUniqueId());
                            int seconds = ServerEssentials.plugin.getConfig().getInt("home-teleport");
                            String msg = Lang.fileConfig.getString("target-home-wait-message").replace("<target>", ListHomes.target.getName()).replace("<time>", String.valueOf(seconds));
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                            seconds = seconds * 20;
                            Location loc = getHomeLocation(home, ListHomes.target);
                            if (hometeleport.containsKey(player.getUniqueId()) && hometeleport.get(player.getUniqueId()) != null) {
                                Bukkit.getScheduler().cancelTask(hometeleport.get(player.getUniqueId()));
                            }
                            String finalHome = home;
                            hometeleport.put(player.getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((ServerEssentials.plugin), new Runnable() {
                                public void run() {
                                    if (Home.cancel.contains(player.getUniqueId())){
                                        if (hometeleport.containsKey(player.getUniqueId())) {
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
                                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                            }
                                            Home.cancel.remove(player.getUniqueId());
                                        }
                                    }
                                }
                            }, seconds));
                            player.closeInventory();
                        }else{
                            int seconds = ServerEssentials.plugin.getConfig().getInt("home-teleport");
                            String msg = Lang.fileConfig.getString("target-home-wait-message").replace("<target>", ListHomes.target.getName()).replace("<time>", String.valueOf(seconds));
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                            seconds = seconds * 20;
                            Location loc = getHomeLocation(home, ListHomes.target);
                            if (hometeleport.containsKey(player.getUniqueId()) && hometeleport.get(player.getUniqueId()) != null) {
                                Bukkit.getScheduler().cancelTask(hometeleport.get(player.getUniqueId()));
                            }
                            String finalHome = home;
                            hometeleport.put(player.getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((ServerEssentials.plugin), new Runnable() {
                                public void run() {
                                    if (hometeleport.containsKey(player.getUniqueId())) {
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
                                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
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
        // Delete Home Confirm GUI
        String home3 = home2;
        if (home3 != null){
            if (e.getView().getTitle().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', Lang.fileConfig.getString("delete-home-gui-name").replace("<home>", home3)))) {
                if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR || e.getCurrentItem().getType() == Material.GRAY_STAINED_GLASS_PANE){
                    e.setCancelled(true);
                    return;
                }
                if (e.getCurrentItem().getType() == Material.LIME_STAINED_GLASS_PANE) {
                    e.setCancelled(true);
                    if (player.hasPermission("se.deletehome")) {
                        Player target = (Player) e.getInventory().getHolder();
                        UUID targetname = target.getUniqueId();
                        Sethome.fileConfig.set("Home." + targetname + "." + home3, null);
                        try {
                            Sethome.fileConfig.save(Sethome.file);
                        } catch (IOException i) {
                            i.printStackTrace();
                        }
                        String msg = Lang.fileConfig.getString("home-deletion-success").replace("<home>", home3);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        player.closeInventory();
                    }else{
                        e.setCancelled(true);
                        player.closeInventory();
                        String perm = Lang.fileConfig.getString("no-permission-message").replace("<permission>", "se.deletehome");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', perm));
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
            if (e.getView().getTitle().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', Lang.fileConfig.getString("target-delete-home-gui-name").replace("<target>", ListHomes.target.getName()).replace("<home>", targethome3)))) {
                if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR || e.getCurrentItem().getType() == Material.GRAY_STAINED_GLASS_PANE){
                    e.setCancelled(true);
                    return;
                }
                if (e.getCurrentItem().getType() == Material.LIME_STAINED_GLASS_PANE) {
                    e.setCancelled(true);
                    if (player.hasPermission("se.deletehome.others")) {
                        OfflinePlayer target = ListHomes.target;
                        UUID targetname = target.getUniqueId();
                        Sethome.fileConfig.set("Home." + targetname + "." + targethome3, null);
                        try {
                            Sethome.fileConfig.save(Sethome.file);
                        } catch (IOException i) {
                            i.printStackTrace();
                        }
                        String msg = Lang.fileConfig.getString("target-home-deletion-success").replace("<target>", target.getName());
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        player.closeInventory();
                    }else{
                        e.setCancelled(true);
                        player.closeInventory();
                        String perm = Lang.fileConfig.getString("no-permission-message").replace("<permission>", "se.deletehome.others");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', perm));
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
            if (e.getView().getTitle().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', Lang.fileConfig.getString("delete-warp-gui-name").replace("<warp>", warp3)))) {
                if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR || e.getCurrentItem().getType() == Material.GRAY_STAINED_GLASS_PANE){
                    e.setCancelled(true);
                    return;
                }
                if (e.getCurrentItem().getType() == Material.LIME_STAINED_GLASS_PANE) {
                    e.setCancelled(true);
                    if (player.hasPermission("se.deletewarp")) {
                        Setwarp.fileConfig.set("Warp." + warp3, null);
                        try {
                            Setwarp.fileConfig.save(Setwarp.file);
                        } catch (IOException i) {
                            i.printStackTrace();
                        }
                        String msg = Lang.fileConfig.getString("warp-deletion-success").replace("<warp>", warp3);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        player.closeInventory();
                    }else{
                        e.setCancelled(true);
                        player.closeInventory();
                        String perm = Lang.fileConfig.getString("no-permission-message").replace("<permission>", "se.deletewarp");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', perm));
                    }
                } else if (e.getCurrentItem().getType() == Material.RED_STAINED_GLASS_PANE) {
                    e.setCancelled(true);
                    player.closeInventory();
                }
            }
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
    public static Location getHomeLocation(String home, OfflinePlayer player){
        UUID name = player.getUniqueId();
        // Gathering Location
        float yaw = Sethome.fileConfig.getInt("Home." + name + "." + home + ".Yaw");
        Location loc = new Location(Bukkit.getWorld(Sethome.fileConfig.getString("Home." + name + "." + home + ".World")),
                Sethome.fileConfig.getDouble("Home." + name + "." + home + ".X"),
                Sethome.fileConfig.getDouble("Home." + name + "." + home + ".Y"),
                Sethome.fileConfig.getDouble("Home." + name + "." + home + ".Z"),
                yaw, 0);
        return loc;
    }
}