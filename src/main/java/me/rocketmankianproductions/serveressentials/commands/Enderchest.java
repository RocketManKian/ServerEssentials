package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
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
        Player player = (Player) sender;

        if (player.hasPermission("se.enderchest")){
            if (sender instanceof Player){
                if (args.length == 0){
                    player.openInventory(player.getEnderChest());
                    player.playSound(((Player) sender).getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 3.0F, 2.0F);
                    return true;
                }else if (args.length == 1){
                    Player target = Bukkit.getPlayer(args[0]);
                    player.openInventory(target.getEnderChest());
                    player.playSound(((Player) sender).getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 3.0F, 2.0F);
                    sender.sendMessage(ChatColor.GREEN + "Opened " + target.getName() + "(s) Enderchest");
                    return true;
                }else{
                    return false;
                }
            }else{
                player.sendMessage(ChatColor.RED + "You are not a player");
            }
        }else {
            if (ServerEssentials.plugin.getConfig().getString("no-permission-message").length() == 0){
                player.sendMessage(ChatColor.RED + "You do not have the required permission (se.enderchest) to run this command.");
                return true;
            }else{
                String permission = ServerEssentials.getPlugin().getConfig().getString("no-permission-message");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', permission));
            }
            return true;
        }
        return false;
    }
}
