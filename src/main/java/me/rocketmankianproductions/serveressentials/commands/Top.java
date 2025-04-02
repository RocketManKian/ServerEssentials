package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Top implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (ServerEssentials.permissionChecker(player, "se.top")) {
                Location location = player.getLocation();
                Block block = player.getWorld().getHighestBlockAt(location);

                // Ensure the block isn't air
                if (block == null || block.getType().isAir()) {
                    String msg = Lang.fileConfig.getString("top-invalid");
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ServerEssentials.hex(msg)));
                    return false;
                }

                // Get a safe teleport location (1 block above the highest solid block)
                Location newLocation = new Location(player.getWorld(), location.getX(), block.getY() + 1, location.getZ());

                // Prevent teleporting if already at the highest block
                if (newLocation.getY() == player.getLocation().getY()) {
                    String msg = Lang.fileConfig.getString("top-unsuccessful");
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ServerEssentials.hex(msg)));
                    return true;
                }

                // Teleport the player
                player.teleport(newLocation);
                String msg = Lang.fileConfig.getString("top-successful");
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ServerEssentials.hex(msg)));
                return true;
            }
        }
        return false;
    }
}
