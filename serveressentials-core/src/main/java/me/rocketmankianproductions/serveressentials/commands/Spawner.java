package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.jetbrains.annotations.NotNull;

import static me.rocketmankianproductions.serveressentials.ServerEssentials.hex;

public class Spawner implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (sender instanceof Player){
            Player player  = (Player) sender;
            if (ServerEssentials.permissionChecker(player, "se.spawner")){
                if (args.length >= 1){
                    String mobName = args[0].toUpperCase();
                    EntityType entityType = EntityType.fromName(mobName);

                    // Validate if it's a valid mob
                    if (entityType == null || !entityType.isAlive()) {
                        String msg = Lang.fileConfig.getString("spawner-invalid").replace("<mob>", mobName);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                        return true;
                    }

                    // Create a spawner item
                    ItemStack spawnerItem = new ItemStack(Material.SPAWNER, 1);
                    BlockStateMeta meta = (BlockStateMeta) spawnerItem.getItemMeta();
                    if (meta != null) {
                        CreatureSpawner spawner = (CreatureSpawner) meta.getBlockState();
                        spawner.setSpawnedType(entityType);
                        meta.setBlockState(spawner);
                        spawnerItem.setItemMeta(meta);
                    }

                    // Give the spawner to the player
                    player.getInventory().addItem(spawnerItem);
                    String msg = Lang.fileConfig.getString("spawner-successful").replace("<mob>", mobName);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                    return true;
                }else{
                    String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/spawner <mob>");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                    return true;
                }
            }
        }
        return false;
    }
}
