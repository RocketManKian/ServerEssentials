package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class Rename implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            if (player.hasPermission("se.rename")){
                if (args.length >= 1) {
                    ItemStack hand = player.getItemInHand();
                    if (hand.getType().equals(Material.AIR)) {
                        player.sendMessage(ChatColor.RED + "Please hold a valid item to rename.");
                        return true;
                    } else {
                        ItemMeta im = hand.getItemMeta();
                        String myArgs = String.join(" ", args);
                        im.setDisplayName(ChatColor.translateAlternateColorCodes('&', myArgs));
                        hand.setItemMeta(im);
                        player.sendMessage(ChatColor.GREEN + "Successfully set item name as " + ChatColor.translateAlternateColorCodes('&', myArgs));
                        return true;
                    }
                }
            }else{
                if (ServerEssentials.plugin.getConfig().getString("no-permission-message").length() == 0){
                    player.sendMessage(ChatColor.RED + "You do not have the required permission (se.rename) to run this command.");
                    return true;
                }else{
                    String permission = ServerEssentials.getPlugin().getConfig().getString("no-permission-message");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', permission));
                    return true;
                }
            }
        }
        return false;
    }
}
