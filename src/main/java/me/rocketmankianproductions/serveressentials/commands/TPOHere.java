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

import static me.rocketmankianproductions.serveressentials.ServerEssentials.hex;
import static me.rocketmankianproductions.serveressentials.commands.Teleport.teleportSave;

public class TPOHere implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (sender instanceof Player){
            if (args.length == 1){
                Player player = (Player) sender;
                if (ServerEssentials.permissionChecker(player, "se.tpohere")){
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target == null) {
                        String msg = Lang.fileConfig.getString("target-offline");
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                        return true;
                    }
                    if (target == player) {
                        String msg = Lang.fileConfig.getString("teleport-self");
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                        return true;
                    }
                    String msgSuccess = Lang.fileConfig.getString("teleport-target-success").replace("<sender>", target.getName());
                    if (player.hasPermission("se.silenttp")) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msgSuccess)));
                    } else {
                        String msg = Lang.fileConfig.getString("teleport-force-target").replace("<target>", sender.getName());
                        target.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msgSuccess)));
                    }
                    teleportSave(target);
                    target.teleport(player.getLocation());
                    return true;
                }
            }
        }
        return false;
    }
}