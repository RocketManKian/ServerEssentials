package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Enderchest implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            boolean hasPerm = ServerEssentials.permissionChecker(player, "se.enderchest");
            if (hasPerm) {
                if (args.length == 0) {
                    player.openInventory(player.getEnderChest());
                    player.playSound(((Player) sender).getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 3.0F, 2.0F);
                    return true;
                } else if (args.length == 1) {
                    boolean hasPerm2 = ServerEssentials.permissionChecker(player, "se.enderchest.others");
                    if (hasPerm2) {
                        Player target = Bukkit.getPlayer(args[0]);
                        if (target == null) {
                            String msg = Lang.fileConfig.getString("target-offline");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                            return true;
                        } else if (target == sender) {
                            String msg = Lang.fileConfig.getString("enderchest-target-is-sender");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                            return true;
                        } else {
                            player.openInventory(target.getEnderChest());
                            player.playSound(((Player) sender).getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 3.0F, 2.0F);
                            String msg = Lang.fileConfig.getString("enderchest-open-success".replace("<target>", target.getName()));
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                            return true;
                        }
                    }
                }else{
                    String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/enderchest");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    return true;
                }
            }
        }else{
            String console = Lang.fileConfig.getString("console-invalid");
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', console));
            return true;
        }
        return false;
    }
}
