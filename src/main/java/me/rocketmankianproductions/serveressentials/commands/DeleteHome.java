package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class DeleteHome implements CommandExecutor {

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            // Checking if the player has the se.deletehome permission
            if (ServerEssentials.permissionChecker(player, "se.deletehome")) {
                if (args.length == 1) {
                    String name = player.getUniqueId().toString();
                    // Averaging out the whether the file exists or not by checking for value in one of the default saving points
                    if (Sethome.file.exists() && Sethome.fileConfig.getString("Home." + name + "." + args[0]) != null && !Sethome.fileConfig.getString("Home." + name).isEmpty()) {
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
                        String msg = Lang.fileConfig.getString("home-invalid").replace("<home>", args[0]);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    }
                }else if (args.length == 2){
                    boolean hasPerm2 = ServerEssentials.permissionChecker(player, "se.deletehome.others");
                    if (hasPerm2) {
                        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
                        // Averaging out the whether the file exists or not by checking for value in one of the default saving points
                        if (Sethome.file.exists() && Sethome.fileConfig.getString("Home." + target.getUniqueId() + "." + args[1]) != null && !Sethome.fileConfig.getString("Home." + target.getUniqueId()).isEmpty()) {
                            Sethome.fileConfig.set("Home." + target.getUniqueId() + "." + args[1], null);
                            try {
                                Sethome.fileConfig.save(Sethome.file);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            String msg = Lang.fileConfig.getString("target-home-deletion-success").replace("<target>", args[0]);
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                            return true;
                        } else {
                            String msg = Lang.fileConfig.getString("home-invalid").replace("<home>", args[1]);
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                            return true;
                        }
                    }
                }else{
                    String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/deletehome (home)");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    return true;
                }
            }
        }else{
            String console = Lang.fileConfig.getString("console-invalid");
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', console));
            return true;
        }
        return false;
    }
}