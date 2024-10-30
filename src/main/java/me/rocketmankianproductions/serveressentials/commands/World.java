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

import java.util.Objects;

import static me.rocketmankianproductions.serveressentials.ServerEssentials.hex;

public class World implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (commandSender instanceof Player){
            Player player = (Player) commandSender;
            if (ServerEssentials.permissionChecker(player, "se.world")){
                if (args.length == 1){
                    String world = args[0];
                    if (Bukkit.getWorld(world) != null){
                        player.teleport(Objects.requireNonNull(Bukkit.getWorld(world)).getSpawnLocation());
                        String msg = Lang.fileConfig.getString("world-success").replace("<world>", world);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                        return true;
                    }else{
                        String msg = Lang.fileConfig.getString("world-invalid").replace("<world>", world);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                        return true;
                    }
                }else{
                    String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/world <world>");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                    return true;
                }
            }
        }
        return false;
    }
}
