package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Jump implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            if (ServerEssentials.permissionChecker(player, "se.jump")){
                Block targetBlock = player.getTargetBlockExact(100); // Max distance to look at

                if (targetBlock != null && targetBlock.getType() != Material.AIR) {
                    player.teleport(targetBlock.getLocation());
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', ServerEssentials.hex(Lang.fileConfig.getString("jump-remove-message"))));
                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', ServerEssentials.hex(Lang.fileConfig.getString("jump-error-message"))));
                }
                return true;
            }
        }
        return false;
    }
}