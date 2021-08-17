package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.file.Lang;
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
        if (player.hasPermission("se.deletespawn") || player.hasPermission("se.all")) {
            // Averaging out the whether the file exists or not by checking for value in one of the default saving points
            if (Setspawn.file.exists() && Setspawn.fileConfig.getString("Location.World") != null) {
                // If the file exists then it will get deleted upon execution of command
                Setspawn.file.delete();
                Setspawn.reload();
                String msg = Lang.fileConfig.getString("spawn-deletion-success");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                return true;
            } else {
                String msg = Lang.fileConfig.getString("spawn-not-found");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                return true;
            }
        } else {
            String perm = Lang.fileConfig.getString("no-permission-message").replace("<permission>", "se.deletespawn");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', perm));
            return true;
        }
    }
}