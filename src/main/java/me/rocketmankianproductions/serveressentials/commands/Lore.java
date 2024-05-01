package me.rocketmankianproductions.serveressentials.commands;

import github.scarsz.discordsrv.dependencies.commons.lang3.ArrayUtils;
import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.Bukkit;
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

import static me.rocketmankianproductions.serveressentials.ServerEssentials.hex;

public class Lore implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (ServerEssentials.permissionChecker(player, "se.lore")) {
                if (args.length >= 1){
                    if (args[0].equalsIgnoreCase("set")){
                        ItemStack hand = player.getItemInHand();
                        if (hand.getType().equals(Material.AIR)) {
                            String msg = Lang.fileConfig.getString("lore-invalid-item");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                            return true;
                        } else {
                            ItemMeta im = hand.getItemMeta();
                            String myArgs = String.join(" ", ArrayUtils.remove(args, 0));
                            im.setLore(Collections.singletonList(ChatColor.translateAlternateColorCodes('&', myArgs)));
                            hand.setItemMeta(im);
                            String msg = Lang.fileConfig.getString("lore-successful").replace("<lore>", myArgs);
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                            return true;
                        }
                    }else if (args[0].equalsIgnoreCase("reset") && args.length == 1) {
                        ItemStack hand = player.getItemInHand();
                        if (hand.getType().equals(Material.AIR)) {
                            String msg = Lang.fileConfig.getString("lore-reset-invalid-item");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                            return true;
                        } else {
                            ItemMeta im = hand.getItemMeta();
                            im.setLore(null);
                            hand.setItemMeta(im);
                            String msg = Lang.fileConfig.getString("lore-reset-successful");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                            return true;
                        }
                    } else {
                        String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/lore <set/reset>");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                        return true;
                    }
                } else {
                    String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/lore <set/reset>");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                    return true;
                }
            }
        } else {
            String console = Lang.fileConfig.getString("console-invalid");
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', hex(console)));
            return true;
        }
        return false;
    }
}
