package me.rocketmankianproductions.serveressentials.events;

import me.rocketmankianproductions.serveressentials.commands.Sethome;
import me.rocketmankianproductions.serveressentials.commands.Setwarp;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

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
                // Gathering Location
                float yaw = Setwarp.fileConfig.getInt("Warp." + warp + ".Yaw");
                float pitch = Sethome.fileConfig.getInt("Home." + player.getName() + ".Pitch");
                loc = new Location(Bukkit.getWorld(Setwarp.fileConfig.getString("Warp." + warp + ".World")),
                        Setwarp.fileConfig.getDouble("Warp." + warp + ".X"),
                        Setwarp.fileConfig.getDouble("Warp." + warp + ".Y"),
                        Setwarp.fileConfig.getDouble("Warp." + warp + ".Z"),
                        yaw, pitch);
                player.teleport(loc);
                player.sendMessage("Successfully warped to " + warp);
                player.closeInventory();
            }
        }else if (e.getView().getTitle().equalsIgnoreCase(ChatColor.AQUA + "Home GUI")){
            e.setCancelled(true);
            String name = player.getUniqueId().toString();
            ItemStack item = e.getCurrentItem();
            if (item != null){
                String home = item.getItemMeta().getDisplayName();
                home = ChatColor.stripColor(home);
                // Gathering Location
                float yaw = Sethome.fileConfig.getInt("Home." + name + "." + home + ".Yaw");
                loc = new Location(Bukkit.getWorld(Sethome.fileConfig.getString("Home." + name + "." + home + ".World")),
                        Sethome.fileConfig.getDouble("Home." + name + "." + home + ".X"),
                        Sethome.fileConfig.getDouble("Home." + name + "." + home + ".Y"),
                        Sethome.fileConfig.getDouble("Home." + name + "." + home + ".Z"),
                        yaw, 0);
                player.teleport(loc);
                player.sendMessage("Successfully teleported to " + home);
                player.closeInventory();
            }
        }
    }
    @EventHandler
    public void onClose(InventoryCloseEvent c) {
        //InventoryView inv = e.getView();
        if (c.getView().getTitle().equalsIgnoreCase("Equipped Armor")) {
            c.getInventory().clear();
        }
    }
}