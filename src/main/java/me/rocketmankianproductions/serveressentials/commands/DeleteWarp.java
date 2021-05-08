package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class DeleteWarp implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        // Checking if the player has the se.deletewarp permission
        if (player.hasPermission("se.deletewarp")) {
            if (args.length == 1) {
                String name = player.getUniqueId().toString();
                // Averaging out the whether the file exists or not by checking for value in one of the default saving points
                if (Setwarp.file.exists() && Setwarp.fileConfig.getString("Warp." + args[0]) != null) {
                    // If the file exists then it will get deleted upon execution of command
                    if (Setwarp.fileConfig.getStringList("Warp.") != null){
                        Setwarp.fileConfig.set("Warp." + args[0], null);
                        try {
                            Setwarp.fileConfig.save(Setwarp.file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        player.sendMessage(ChatColor.GREEN + "Warp " + ChatColor.GOLD + args[0] + ChatColor.GREEN + " has been successfully deleted");
                        return true;
                    }else {
                        Setwarp.fileConfig.set("Warp", null);
                        try {
                            Setwarp.fileConfig.save(Setwarp.file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        player.sendMessage(ChatColor.GREEN + "Warp " + ChatColor.GOLD + args[0] + ChatColor.GREEN + " has been successfully deleted");
                        return true;
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "Warp Doesn't Exist");
                    return true;
                }
            }else if (args.length == 0){
                return false;
            }
        } else {
            if (ServerEssentials.plugin.getConfig().getString("no-permission-message").length() == 0){
                player.sendMessage(ChatColor.RED + "You do not have the required permission (se.deletewarp) to run this command.");
                return true;
            }else{
                String permission = ServerEssentials.getPlugin().getConfig().getString("no-permission-message");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', permission));
            }
            return true;
        }
        return true;
    }
}
