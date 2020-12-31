package me.rocketmankianproductions.serveressentials.commands;

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
                if (player.hasPermission("se.hurt")) {
                    if (Bukkit.getPlayer(args[0]) != null) {
                        try {
                            Player playerToHurt = Bukkit.getPlayer(args[0]);
                            double damageAmount = Double.parseDouble(args[1]);
                            playerToHurt.damage(damageAmount);
                            player.sendMessage(ChatColor.YELLOW + playerToHurt.getName() + ChatColor.RED + " was hurt for " + ChatColor.YELLOW + damageAmount + ChatColor.RED + " damage points");
                        } catch (NumberFormatException exception) {
                            player.sendMessage(ChatColor.RED + args[1] + " is not a valid number!");
                        }
                    } else player.sendMessage("Can't find player " + args[0]);
                }
                else {
                    player.sendMessage(ChatColor.RED + "You do not have the required permission for (se.hurt) to run this command.");
                }
            }
        }
                return true;
            }
        }