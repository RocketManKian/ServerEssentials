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

public class TPO implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (sender instanceof Player){
            if (args.length == 1){
                Player player = (Player) sender;
                if (ServerEssentials.permissionChecker(player, "se.tpo")){
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
                    if (player.hasPermission("se.silenttp") || sender.hasPermission("se.all")) {
                        String msg = Lang.fileConfig.getString("teleport-success").replace("<target>", target.getName());
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                        teleportSave(player);
                        player.teleport(target.getLocation());
                        return true;
                    } else if (!player.hasPermission("se.silenttp")) {
                        teleportSave(player);
                        String msg = Lang.fileConfig.getString("teleport-success").replace("<target>", target.getName());
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                        String msg2 = Lang.fileConfig.getString("teleport-target-success").replace("<sender>", player.getName());
                        target.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg2)));
                        player.teleport(target.getLocation());
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
