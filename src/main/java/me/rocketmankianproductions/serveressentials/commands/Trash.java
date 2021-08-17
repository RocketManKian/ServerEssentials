package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public class Trash implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            if (player.hasPermission("se.trash") || player.hasPermission("se.all")){
                Inventory trash = Bukkit.createInventory(player, 27, ChatColor.translateAlternateColorCodes('&', Lang.fileConfig.getString("trash-gui-name")));
                player.openInventory(trash);
                return true;
            }else{
                String perm = Lang.fileConfig.getString("no-permission-message").replace("<permission>", "se.trash");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', perm));
                return true;
            }
        }
        return false;
    }
}
