package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Hurt implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender Sender, Command command, String commandLabel, String[] args) {
        if (Sender instanceof Player) {
            Player player = (Player) Sender;

            //hurt <PLAYER> <AMOUNT>

            if (commandLabel.equalsIgnoreCase("hurt")) {
                // Checking if the player has the se.hurt permission
                if (player.hasPermission("se.hurt")) {
                    // Checking if the player exists
                    if (args.length>=2) {
                        try {
                            Player playerToHurt = Bukkit.getPlayer(args[0]);
                            double damageAmount = Double.parseDouble(args[1]);
                            playerToHurt.damage(damageAmount);
                            player.sendMessage(ChatColor.YELLOW + playerToHurt.getName() + ChatColor.RED + " was hurt for " + ChatColor.YELLOW + damageAmount + ChatColor.RED + " damage points");
                            return true;
                        } catch (NumberFormatException exception) {
                            player.sendMessage(ChatColor.RED + args[1] + " is not a valid number!");
                            return true;
                        }
                        // Displays name which is invalid or null
                    }
                    return false;
                }
                else {
                    if (ServerEssentials.plugin.getConfig().getString("no-permission-message").length() == 0){
                        player.sendMessage(ChatColor.RED + "You do not have the required permission (se.hurt) to run this command.");
                        return true;
                    }else{
                        String permission = ServerEssentials.getPlugin().getConfig().getString("no-permission-message");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', permission));
                    }
                    return true;
                }
            }
        }
                return true;
            }
        }