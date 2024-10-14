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

import java.util.List;
import java.util.ArrayList;

import static me.rocketmankianproductions.serveressentials.ServerEssentials.hex;

public class Near implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (commandSender instanceof Player){
            Player player = (Player) commandSender;
            double max_radius = ServerEssentials.getPlugin().getConfig().getDouble("near-range");
            if (ServerEssentials.permissionChecker(player, "se.near")){
                List<String> nearbyPlayers = new ArrayList<>();
                boolean foundPlayers = false;
                for (Player target : Bukkit.getOnlinePlayers()) {
                    if (target != player){
                        int distance = (int) player.getLocation().distance(target.getLocation());
                        if (distance <= max_radius){
                            nearbyPlayers.add(ChatColor.WHITE + target.getDisplayName() +
                                    "(" + ChatColor.RED + distance + "m" + ChatColor.WHITE + ")");
                            foundPlayers = true;
                        }
                    }
                }
                if (foundPlayers) {
                    String message = String.join(", ", nearbyPlayers);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.fileConfig.getString("near-message")) + " " + message);
                } else {
                    // Send the accumulated message
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.fileConfig.getString("near-invalid")).replace("<range>", String.valueOf(max_radius)));
                }
                return true;
            }
        }else{
            String console = Lang.fileConfig.getString("console-invalid");
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', hex(console)));
            return true;
        }
        return false;
    }
}
