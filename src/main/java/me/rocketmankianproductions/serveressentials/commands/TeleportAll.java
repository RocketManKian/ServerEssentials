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

public class TeleportAll implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (ServerEssentials.permissionChecker(player, "se.tpall")) {
                if (Bukkit.getServer().getOnlinePlayers().size() == 1) {
                    String msg = Lang.fileConfig.getString("teleport-no-players-online");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                } else if (Bukkit.getServer().getOnlinePlayers().size() > 1 && args.length == 0) {
                    int numOfPlayer = 0;
                    for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                        numOfPlayer++;
                        p.teleport(player.getLocation());
                    }
                    int players = numOfPlayer - 1;
                    String msg = Lang.fileConfig.getString("teleport-all-message").replace("<amount>", String.valueOf(players));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                }else{
                    String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/tpall");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                    return true;
                }
            }
        }else{
            String console = Lang.fileConfig.getString("console-invalid");
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', hex(console)));
            return true;
        }
        return true;
    }
}