package me.rocketmankianproductions.serveressentials.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class StaffChat implements CommandExecutor {

    public static ArrayList<Player> staffchat = new ArrayList<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            if (player.hasPermission("se.staffchat")){
                if (args.length == 0) {
                    if (!staffchat.contains(player)){
                        player.sendMessage(ChatColor.GOLD + "StaffChat has been" + ChatColor.GREEN + " enabled!");
                        staffchat.add(player);
                        return true;
                    }else{
                        player.sendMessage(ChatColor.GOLD + "StaffChat has been" + ChatColor.RED + " disabled!");
                        staffchat.remove(player);
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
