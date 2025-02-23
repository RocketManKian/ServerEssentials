package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class EntityRemover implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player){
            Player player = (Player) commandSender;
            if (ServerEssentials.permissionChecker(player, "se.clearentity")){
                clearEntities(player);
            }
        }else{
            clearEntities(commandSender);
        }
        return false;
    }

    private void clearEntities(CommandSender sender) {
        int count = 0;
        for (Entity entity : Bukkit.getWorlds().get(0).getEntities()) { // Only clearing in first world
            if (!(entity instanceof Player)) { // Don't remove players
                entity.remove();
                count++;
            }
        }
        String msg = Lang.fileConfig.getString("clear-entity").replace("<amount>", String.valueOf(count));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
    }
}
