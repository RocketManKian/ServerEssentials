package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.events.PlaceholderExpansion;
import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class AFK implements CommandExecutor, Listener {

    public static HashMap<Player, Boolean> afk = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            if (!afk.containsKey(player)){
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.fileConfig.getString("afk-active")));
                player.setSleepingIgnored(true);
                PlaceholderExpansion.isafk = true;
                afk.put(player, true);
            }else{
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.fileConfig.getString("afk-inactive")));
                player.setSleepingIgnored(false);
                PlaceholderExpansion.isafk = false;
                afk.remove(player);
            }
        }
        return false;
    }
}
