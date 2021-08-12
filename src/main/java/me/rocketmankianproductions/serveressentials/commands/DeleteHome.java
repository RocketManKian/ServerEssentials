package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class DeleteHome implements CommandExecutor {

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
                    String msg = Lang.fileConfig.getString("home-deletion-success").replace("<home>", args[0]);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    return true;
                } else {
                    String msg = Lang.fileConfig.getString("home-deletion-success").replace("<home>", args[0]);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    return true;
                }
            }
        } else {
            String perm = Lang.fileConfig.getString("no-permission-message").replace("<permission>", "se.deletehome");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', perm));
            return true;
        }
        return false;
    }
}