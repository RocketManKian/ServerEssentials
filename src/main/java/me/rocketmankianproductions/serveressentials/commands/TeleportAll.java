package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TeleportAll implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("se.tpall") || player.hasPermission("se.all")) {
                if (Bukkit.getServer().getOnlinePlayers().size() == 1) {
                    String msg = Lang.fileConfig.getString("teleport-no-players-online");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                } else if (Bukkit.getServer().getOnlinePlayers().size() > 1 && args.length == 0) {
                    int numOfPlayer = 0;
                    for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                        numOfPlayer++;
                        p.teleport(player.getLocation());
                    }
                    int players = numOfPlayer - 1;
                    String msg = Lang.fileConfig.getString("teleport-all-message").replace("<amount>", String.valueOf(players));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                }
            } else {
                String perm = Lang.fileConfig.getString("no-permission-message").replace("<permission>", "se.tpall");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', perm));
                return true;
            }
        }
        return true;
    }
}