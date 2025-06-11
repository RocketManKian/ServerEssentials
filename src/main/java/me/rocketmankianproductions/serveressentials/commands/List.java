package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;

import static org.bukkit.Bukkit.getServer;

public class List implements CommandExecutor {

    private static Permission perms = null;

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        Player player = (Player) commandSender;
        if (ServerEssentials.permissionChecker(player, "se.list")){
            String msg = Lang.fileConfig.getString("list-message").replace("<amount>", "" + getServer().getOnlinePlayers()).replace("<total>", "" + getServer().getMaxPlayers());
            for (Player onlinePlayers : Bukkit.getOnlinePlayers()){
                if (setupPermissions()){
                    onlinePlayers.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6" + perms.getPlayerGroups(onlinePlayers)));
                }
            }
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', ServerEssentials.hex(msg)));
        }
        return false;
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        if (rsp == null) {
            return false;
        }
        perms = rsp.getProvider();
        return perms != null;
    }
}