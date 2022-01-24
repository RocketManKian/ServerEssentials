package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public class Lore implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            if (player.hasPermission("se.lore") || player.hasPermission("se.all")) {
                if (args.length >= 1) {
                    ItemStack hand = player.getItemInHand();
                    if (hand.getType().equals(Material.AIR)) {
                        String msg = Lang.fileConfig.getString("lore-invalid-item");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    } else {
                        ItemMeta im = hand.getItemMeta();
                        String myArgs = String.join(" ", args);
                        im.setLore(Collections.singletonList(ChatColor.translateAlternateColorCodes('&', myArgs)));
                        hand.setItemMeta(im);
                        String msg = Lang.fileConfig.getString("lore-successful").replace("<lore>", myArgs);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    }
                }else{
                    String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/lore (message)");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    return true;
                }
            }else{
                String perm = Lang.fileConfig.getString("no-permission-message").replace("<permission>", "se.lore");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', perm));
                return true;
            }
        }
        return false;
    }
}