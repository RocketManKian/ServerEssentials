package me.rocketmankianproductions.serveressentials.events;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class PlayerClickEvent implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e){
        //InventoryView inv = e.getView();
        if (e.getView().getTitle().equalsIgnoreCase(ChatColor.AQUA + "Server Essentials")){
            e.setCancelled(true);
        }else if (e.getView().getTitle().equalsIgnoreCase("Equipped Armor")){
            e.setCancelled(true);
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
