package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class DeleteSpawn implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            // Checking if the player has the se.deletespawn permission
            if (ServerEssentials.permissionChecker(player, "se.deletespawn")) {
                // Averaging out the whether the file exists or not by checking for value in one of the default saving points
                if (args.length == 0) {
                    if (Setspawn.file.exists() && Setspawn.fileConfig.getString("Location.World") != null) {
                        // If the file exists then it will get deleted upon execution of command
                        Setspawn.fileConfig.set("Location", null);
                        try {
                            Setspawn.fileConfig.save(Setspawn.file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Setspawn.reload();
                        String msg = Lang.fileConfig.getString("spawn-deletion-success");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    } else {
                        String msg = Lang.fileConfig.getString("spawn-not-found");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    }
                } else if (args.length == 1 && args[0].equalsIgnoreCase("newbies")) {
                    if (Setspawn.file.exists() && Setspawn.fileConfig.getString("Newbies.Location.World") != null) {
                        // If the file exists then it will get deleted upon execution of command
                        Setspawn.fileConfig.set("Newbies", null);
                        try {
                            Setspawn.fileConfig.save(Setspawn.file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Setspawn.reload();
                        // If the file exists then it will get deleted upon execution of command
                        String msg = Lang.fileConfig.getString("newbies-spawn-deletion-success");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    } else {
                        String msg = Lang.fileConfig.getString("spawn-not-found");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    }
                }
            }
        } else {
            String console = Lang.fileConfig.getString("console-invalid");
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', console));
            return true;
        }
        return false;
    }
}
