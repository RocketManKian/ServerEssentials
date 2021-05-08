package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class DeleteHome implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        // Checking if the player has the se.deletehome permission
        if (player.hasPermission("se.deletehome")) {
            if (args.length == 1) {
                String name = player.getUniqueId().toString();
                // Averaging out the whether the file exists or not by checking for value in one of the default saving points
                if (Sethome.file.exists() && Sethome.fileConfig.getString("Home." + name + "." + args[0]) != null) {
                    Sethome.fileConfig.set("Home." + name + "." + args[0], null);
                    try {
                        Sethome.fileConfig.save(Sethome.file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    player.sendMessage(ChatColor.GREEN + "Home " + ChatColor.GOLD + args[0] + ChatColor.GREEN + " has been successfully deleted");
                    return true;
                } else {
                    sender.sendMessage(ChatColor.RED + "Home Doesn't Exist");
                }
            }
        } else {
            if (ServerEssentials.plugin.getConfig().getString("no-permission-message").length() == 0){
                player.sendMessage(ChatColor.RED + "You do not have the required permission (se.deletehome) to run this command.");
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