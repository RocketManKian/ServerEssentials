package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Hurt implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (commandLabel.equalsIgnoreCase("hurt")) {
                // Checking if the player has the se.hurt permission
                if (ServerEssentials.permissionChecker(player, "se.hurt")) {
                    // Checking if the player exists
                    if (args.length >= 2) {
                        try {
                            Player playerToHurt = Bukkit.getPlayer(args[0]);
                            double damageAmount = Double.parseDouble(args[1]);
                            playerToHurt.damage(damageAmount);
                            String msg = Lang.fileConfig.getString("hurt-target").replace("<target>", playerToHurt.getName()).replace("<damage>", args[1]);
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                            return true;
                        } catch (NumberFormatException exception) {
                            String msg = Lang.fileConfig.getString("hurt-invalid-number").replace("<damage>", args[1]);
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                            return true;
                        }
                    }else{
                        String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/hurt (player) <amount>");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    }
                }
            }
        }else{
            String console = Lang.fileConfig.getString("console-invalid");
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', console));
            return true;
        }
        return false;
    }
}