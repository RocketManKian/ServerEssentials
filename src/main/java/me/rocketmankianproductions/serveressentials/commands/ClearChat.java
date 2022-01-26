package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ClearChat implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            if (player.hasPermission("se.clearchat") || player.hasPermission("se.all")){
                if (command.getName().equalsIgnoreCase("clearchat") || command.getName().equalsIgnoreCase("cc")){
                    for (int x = 0; x < 150; x++){
                        Bukkit.broadcastMessage("");
                    }
                    Bukkit.broadcastMessage(ChatColor.GOLD + "|-------------------+====+-------------------|");
                    Bukkit.broadcastMessage(ChatColor.RED + " The chat has been cleared by a staff member.");
                    Bukkit.broadcastMessage(ChatColor.GOLD + "|-------------------+====+-------------------|");
                    return true;
                }else{
                    String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/clearchat");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    return true;
                }
            }else{
                String perm = Lang.fileConfig.getString("no-permission-message").replace("<permission>", "se.clearchat");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', perm));
                return true;
            }
        }else{
            String console = Lang.fileConfig.getString("console-invalid");
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', console));
            return true;
        }
    }
}
