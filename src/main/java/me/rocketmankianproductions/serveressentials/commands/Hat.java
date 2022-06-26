package me.rocketmankianproductions.serveressentials.commands;

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
import org.jetbrains.annotations.NotNull;

public class Hat implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                boolean hasPerm = ServerEssentials.permissionChecker(player, "se.hat");
                if (hasPerm) {
                    if (!player.getItemInHand().getType().equals(Material.AIR)) {
                        String msg = Lang.fileConfig.getString("hat-success").replace("<hat>", player.getItemInHand().getType().name());
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        hatCommand(player);
                        return true;
                    } else {
                        String msg = Lang.fileConfig.getString("hat-invalid");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    }
                }
            }else{
                String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/hat");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                return true;
            }
        }else{
            String console = Lang.fileConfig.getString("console-invalid");
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', console));
            return true;
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
