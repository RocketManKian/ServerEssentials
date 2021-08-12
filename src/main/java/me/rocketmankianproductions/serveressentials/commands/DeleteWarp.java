package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.file.Lang;
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
                // Averaging out the whether the file exists or not by checking for value in one of the default saving points
                if (Setwarp.file.exists() && Setwarp.fileConfig.getString("Warp." + args[0]) != null) {
                    // If the file exists then it will get deleted upon execution of command
                    if (Setwarp.fileConfig.getStringList("Warp.") != null) {
                        Setwarp.fileConfig.set("Warp." + args[0], null);
                        try {
                            Setwarp.fileConfig.save(Setwarp.file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        String msg = Lang.fileConfig.getString("warp-deletion-success").replace("<warp>", args[0]);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    } else {
                        Setwarp.fileConfig.set("Warp", null);
                        try {
                            Setwarp.fileConfig.save(Setwarp.file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        String msg = Lang.fileConfig.getString("warp-deletion-success").replace("<warp>", args[0]);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    }
                } else {
                    String msg = Lang.fileConfig.getString("warp-not-found").replace("<warp>", args[0]);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    return true;
                }
            }
        } else {
            String perm = Lang.fileConfig.getString("no-permission-message").replace("<permission>", "se.deletewarp");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', perm));
            return true;
        }
        return false;
    }
}