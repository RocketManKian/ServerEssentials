package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Clear implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            if (player.hasPermission("se.clear")){
                if (args.length == 0){
                    player.getInventory().clear();
                    sender.sendMessage(ChatColor.GREEN + "Inventory cleared");
                    return true;
                }else if (args.length == 1){
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target == null){
                        player.sendMessage(ChatColor.RED + "Player does not exist!");
                        return true;
                    }else if (sender == target){
                        player.sendMessage(ChatColor.RED + "You cannot target yourself.");
                        return true;
                    }else{
                        target.getInventory().clear();
                        sender.sendMessage(target.getName() + "(s)" + ChatColor.GREEN + " inventory has been cleared!");
                        return true;
                    }
                }
            }else{
                if (ServerEssentials.plugin.getConfig().getString("no-permission-message").length() == 0){
                    player.sendMessage(ChatColor.RED + "You do not have the required permission (se.clear) to run this command.");
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
