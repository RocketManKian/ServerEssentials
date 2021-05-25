package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DeleteSpawn implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        // Checking if the player has the se.deletespawn permission
        if (player.hasPermission("se.deletespawn")) {
            // Averaging out the whether the file exists or not by checking for value in one of the default saving points
            if (Setspawn.file.exists() && Setspawn.fileConfig.getString("Location.World") != null) {
                // If the file exists then it will get deleted upon execution of command
                Setspawn.file.delete();
                Setspawn.reload();
                player.sendMessage(ChatColor.GREEN + "Spawn Deleted");
                return true;
            } else {
                sender.sendMessage(ChatColor.RED + "Spawn Doesn't Exist");
                return true;
            }
        } else {
            if (ServerEssentials.plugin.getConfig().getString("no-permission-message").length() == 0){
                player.sendMessage(ChatColor.RED + "You do not have the required permission (se.deletespawn) to run this command.");
                return true;
            }else{
                String permission = ServerEssentials.getPlugin().getConfig().getString("no-permission-message");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', permission));
                return true;
            }
        }
    }
}
