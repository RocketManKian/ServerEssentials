package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class Hat implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        if (sender instanceof Player) {
            if (args.length == 0) {
                if (player.hasPermission("se.hat")) {
                    if (!player.getItemInHand().getType().equals(Material.AIR)) {
                        ItemStack activeitem = player.getItemInHand();
                        String msg = Lang.fileConfig.getString("hat-success");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        hatCommand(player);
                        return true;
                    } else {
                        String msg = Lang.fileConfig.getString("hat-invalid");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    }
                } else {
                    String perm = Lang.fileConfig.getString("no-permission-message").replace("<permission>", "se.hat");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', perm));
                    return true;
                }
            }
        }
        return false;
    }
    public void hatCommand(Player player) {
        ItemStack activeitem = player.getItemInHand();
        ItemStack helmet = player.getInventory().getHelmet();
        int itemslot = player.getInventory().getHeldItemSlot();
        player.getInventory().setItem(itemslot, helmet);
        player.getInventory().setHelmet(activeitem);
        int activeitem3 = activeitem.getAmount();
        activeitem.setAmount(activeitem3 - 1);
    }
}
