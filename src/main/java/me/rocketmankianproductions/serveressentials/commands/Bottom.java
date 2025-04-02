package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Bottom implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            if (ServerEssentials.permissionChecker(player, "se.bottom")) {
                Location location = player.getLocation();
                World world = player.getWorld();

                int x = location.getBlockX();
                int z = location.getBlockZ();

                // Find the lowest solid block starting from y = -64 upwards
                Block block = null;
                for (int y = world.getMinHeight(); y < world.getMaxHeight(); y++) { // Use world.getMinHeight() dynamically
                    Block currentBlock = world.getBlockAt(x, y, z);
                    if (!currentBlock.isEmpty() && !currentBlock.isLiquid() && !currentBlock.getType().isAir()) {
                        block = currentBlock;
                        break; // Found the first non-air, non-liquid, non-air block
                    }
                }

                // If no valid block was found, prevent teleporting to an invalid location
                if (block == null) {
                    String msg = Lang.fileConfig.getString("bottom-invalid");
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ServerEssentials.hex(msg)));
                    return false;
                }

                Location newLocation = new Location(world, x + 0.5, block.getY() + 1, z + 0.5);

                if (newLocation.getY() == player.getLocation().getY()) {
                    String msg = Lang.fileConfig.getString("bottom-unsuccessful");
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ServerEssentials.hex(msg)));
                } else {
                    player.teleport(newLocation);
                    String msg = Lang.fileConfig.getString("bottom-successful");
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ServerEssentials.hex(msg)));
                }
            }

        }
        return false;
    }
}
