package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

import static me.rocketmankianproductions.serveressentials.ServerEssentials.hex;

public class Invsee implements CommandExecutor, Listener {

    public static HashMap<Player, String> targetName = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 1) {
                if (ServerEssentials.permissionChecker(player, "se.invsee")) {
                    Player targetPlayer = Bukkit.getServer().getPlayer(args[0]);
                    if (targetPlayer == sender) {
                        String msg = Lang.fileConfig.getString("invsee-target-is-sender");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                        return true;
                    } else if (Bukkit.getPlayer(args[0]) == null) {
                        String msg = Lang.fileConfig.getString("target-offline");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                        return true;
                    } else if (Bukkit.getPlayer(args[0]) != null) {
                        if (player.hasPermission("se.invsee.others")){
                            player.openInventory(targetPlayer.getInventory());
                        }else{
                            Inventory myInventory = Bukkit.createInventory(player, InventoryType.PLAYER, ChatColor.translateAlternateColorCodes('&', "&b&l" + targetPlayer.getName() + "'s Inventory"));
                            myInventory.setContents(targetPlayer.getInventory().getContents());
                            player.openInventory(myInventory);
                            targetName.put(player, targetPlayer.getName());
                        }
                        return true;
                    }
                }
            } else if (args.length == 2) {
                if (ServerEssentials.permissionChecker(player, "se.invsee")) {
                    Inventory myInventory = Bukkit.createInventory(player, 9, ChatColor.translateAlternateColorCodes('&', Lang.fileConfig.getString("invsee-armor-gui")));
                    Player targetPlayer = Bukkit.getServer().getPlayer(args[0]);
                    if (targetPlayer == sender) {
                        String msg = Lang.fileConfig.getString("invsee-target-is-sender");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                        return true;
                    } else if (Bukkit.getPlayer(args[0]) == null) {
                        String msg = Lang.fileConfig.getString("target-offline");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                        return true;
                    } else if (!(Bukkit.getPlayer(args[0]) == null)) {
                        if (targetPlayer.getInventory().getHelmet() != null){
                            myInventory.setItem(0, targetPlayer.getInventory().getHelmet());
                        }if (targetPlayer.getInventory().getChestplate() != null){
                            myInventory.setItem(1, targetPlayer.getInventory().getChestplate());
                        }if (targetPlayer.getInventory().getLeggings() != null){
                            myInventory.setItem(2, targetPlayer.getInventory().getLeggings());
                        }if (targetPlayer.getInventory().getBoots() != null){
                            myInventory.setItem(3, targetPlayer.getInventory().getBoots());
                        }
                        player.openInventory(myInventory);
                        return true;
                    }
                }
            }else{
                String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/invsee (player)");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                return true;
            }
        }else{
            String console = Lang.fileConfig.getString("console-invalid");
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', hex(console)));
            return true;
        }
        return false;
    }
}