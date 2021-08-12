package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Speed implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        if (sender instanceof Player){
            if (player.hasPermission("se.speed")){
                if (args.length == 1){
                    int speed;
                    try {
                        speed = Integer.parseInt(args[0]);
                    } catch (NumberFormatException e){
                        String msg = Lang.fileConfig.getString("speed-invalid-number");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    }
                    if (speed <1 || speed > 10){
                        String msg = Lang.fileConfig.getString("speed-invalid-number");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    }
                    if (player.isFlying()){
                        player.setFlySpeed((float) speed / 10);
                        String msg = Lang.fileConfig.getString("speed-fly-success").replace("<speed>", String.valueOf((float)speed));
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    }else if (!player.isFlying()){
                        player.setWalkSpeed((float) speed / 10);
                        String msg = Lang.fileConfig.getString("speed-walk-success").replace("<speed>", String.valueOf((float)speed));
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    }
                }
            }else{
                String perm = Lang.fileConfig.getString("no-permission-message").replace("<permission>", "se.speed");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', perm));
                return true;
            }
        }
        return false;
    }
}
