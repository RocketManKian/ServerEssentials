package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Craft implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;

        if (player.hasPermission("se.craft")){
            if (args.length == 0) {
                if (sender instanceof Player) {
                    player.playSound(((Player) sender).getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 3.0F, 2.0F);
                    player.openWorkbench((Location) null, true);
                    return true;
                } else {
                    String msg = Lang.fileConfig.getString("invalid-player");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    return true;
                }
            }
        }else{
            String perm = Lang.fileConfig.getString("no-permission-message").replace("<permission>", "se.craft");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', perm));
            return true;
        }
        return false;
    }
}
