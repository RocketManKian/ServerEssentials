package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
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
                        player.sendMessage(ChatColor.GREEN + "You are now wearing " + ChatColor.WHITE + activeitem.getType());
                        hatCommand(player);
                        return true;
                    } else {
                        player.sendMessage(ChatColor.RED + "You can't wear that!");
                        return true;
                    }
                } else {
                    if (ServerEssentials.plugin.getConfig().getString("no-permission-message").length() == 0) {
                        player.sendMessage(ChatColor.RED + "You do not have the required permission (se.hat) to run this command.");
                        return true;
                    } else {
                        String permission = ServerEssentials.getPlugin().getConfig().getString("no-permission-message");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', permission));
                    }
                    return true;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
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
