package me.rocketmankianproductions.serveressentials.events;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.commands.Sethome;
import me.rocketmankianproductions.serveressentials.commands.Setwarp;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.UUID;

public class PlayerClickEvent implements Listener {
    Location loc;

    @EventHandler
    public void onClick(InventoryClickEvent e){
        Player player = (Player) e.getWhoClicked();
        //InventoryView inv = e.getView();
        if (e.getView().getTitle().equalsIgnoreCase(ChatColor.AQUA + "Server Essentials")){
            e.setCancelled(true);
        }else if (e.getView().getTitle().equalsIgnoreCase("Equipped Armor")){
            e.setCancelled(true);
        }else if (e.getView().getTitle().equalsIgnoreCase(ChatColor.AQUA + "Warp GUI")){
            e.setCancelled(true);
            ItemStack item = e.getCurrentItem();
            if (item != null){
                String warp = item.getItemMeta().getDisplayName();
                warp = ChatColor.stripColor(warp);
                if (e.getClick()== ClickType.RIGHT) {
                    if (player.hasPermission("se.deletewarp")) {
                        Setwarp.fileConfig.set("Warp." + warp, null);
                        try {
                            Setwarp.fileConfig.save(Setwarp.file);
                        } catch (IOException i) {
                            i.printStackTrace();
                        }
                        player.sendMessage(ChatColor.GREEN + "Warp " + ChatColor.GOLD + warp + ChatColor.GREEN + " has been successfully deleted");
                        player.closeInventory();
                    }
                }else if (e.getClick()==ClickType.LEFT){
                    if (player.hasPermission("se.warps." + warp) || player.hasPermission("se.warps.all")) {
                        // Gathering Location
                        float yaw = Setwarp.fileConfig.getInt("Warp." + warp + ".Yaw");
                        float pitch = Sethome.fileConfig.getInt("Home." + player.getName() + ".Pitch");
                        loc = new Location(Bukkit.getWorld(Setwarp.fileConfig.getString("Warp." + warp + ".World")),
                                Setwarp.fileConfig.getDouble("Warp." + warp + ".X"),
                                Setwarp.fileConfig.getDouble("Warp." + warp + ".Y"),
                                Setwarp.fileConfig.getDouble("Warp." + warp + ".Z"),
                                yaw, pitch);
                        player.teleport(loc);
                        Boolean subtitle = ServerEssentials.plugin.getConfig().getBoolean("enable-warp-subtitle");
                        if (subtitle) {
                            player.sendTitle("Warped to " + ChatColor.GOLD + warp, null);
                        } else {
                            player.sendMessage("Successfully warped to " + warp);
                        }
                        player.closeInventory();
                    }
                }else{
                    if (ServerEssentials.plugin.getConfig().getString("no-permission-message").length() == 0) {
                        player.sendMessage(ChatColor.RED + "You do not have the required permission (se.warps." + warp + ") to run this command.");
                    } else {
                        String permission = ServerEssentials.getPlugin().getConfig().getString("no-permission-message");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', permission));
                    }
                }
            }
        }else if (e.getView().getTitle().equalsIgnoreCase(ChatColor.AQUA + "Home GUI")) {
            e.setCancelled(true);
            String name = player.getUniqueId().toString();
            ItemStack item = e.getCurrentItem();
            if (item != null) {
                String home = item.getItemMeta().getDisplayName();
                home = ChatColor.stripColor(home);
                if (e.getClick() == ClickType.RIGHT) {
                    if (player.hasPermission("se.deletehome")) {
                        Player target = (Player) e.getInventory().getHolder();
                        if (e.getInventory().getHolder().equals(target)){
                            UUID targetname = target.getUniqueId();
                            Sethome.fileConfig.set("Home." + targetname + "." + home, null);
                            try {
                                Sethome.fileConfig.save(Sethome.file);
                            } catch (IOException i) {
                                i.printStackTrace();
                            }
                            player.sendMessage(ChatColor.GREEN + "Home " + ChatColor.GOLD + home + ChatColor.GREEN + " has been successfully deleted");
                            player.closeInventory();
                        }else{
                            Sethome.fileConfig.set("Home." + name + "." + home, null);
                            try {
                                Sethome.fileConfig.save(Sethome.file);
                            } catch (IOException i) {
                                i.printStackTrace();
                            }
                            player.sendMessage(ChatColor.GREEN + "Home " + ChatColor.GOLD + home + ChatColor.GREEN + " has been successfully deleted");
                            player.closeInventory();
                        }
                    }
                } else if (e.getClick() == ClickType.LEFT) {
                    // Gathering Location
                    float yaw = Sethome.fileConfig.getInt("Home." + name + "." + home + ".Yaw");
                    loc = new Location(Bukkit.getWorld(Sethome.fileConfig.getString("Home." + name + "." + home + ".World")),
                            Sethome.fileConfig.getDouble("Home." + name + "." + home + ".X"),
                            Sethome.fileConfig.getDouble("Home." + name + "." + home + ".Y"),
                            Sethome.fileConfig.getDouble("Home." + name + "." + home + ".Z"),
                            yaw, 0);
                    player.teleport(loc);
                    Boolean subtitle = ServerEssentials.plugin.getConfig().getBoolean("enable-home-subtitle");
                    if (subtitle) {
                        player.sendTitle("Teleported to " + ChatColor.GOLD + home, null);
                    } else {
                        player.sendMessage("Successfully teleported to " + home);
                    }
                    player.closeInventory();
                }
            }
        }
    }
}