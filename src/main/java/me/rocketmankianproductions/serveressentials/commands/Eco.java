package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.UserFile;
import me.rocketmankianproductions.serveressentials.file.Lang;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class Eco implements CommandExecutor {
    private ServerEssentials plugin = ServerEssentials.getInstance;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        Player player = (Player) sender;
        if (ServerEssentials.permissionChecker(player, "se.eco")) {
            if (UserFile.fileConfig.getString(String.valueOf(player.getUniqueId())) == null){
                UserFile.fileConfig.set(player.getUniqueId() + ".money", plugin.getConfig().getDouble("start-balance"));
                saveBalance();
            }
            plugin.playerBank.put(player.getUniqueId(), UserFile.fileConfig.getDouble(player.getUniqueId() + ".money"));
            if (args.length > 1) {
                if (Bukkit.getPlayer(args[1]) != null) {
                    if (args.length == 2){
                        if (args[0].equalsIgnoreCase("reset")) {
                            double newBalance = plugin.economyImplementer.getBalance(args[1]);
                            if (newBalance != 0.0) {
                                if (newBalance > 0.0) {
                                    plugin.economyImplementer.withdrawPlayer(args[1], newBalance);
                                } else {
                                    plugin.economyImplementer.depositPlayer(args[1], -newBalance);
                                }
                                double currentBalance = ServerEssentials.plugin.getConfig().getInt("start-balance");
                                EconomyResponse r = plugin.economyImplementer.depositPlayer(args[1], currentBalance);
                                if (r.transactionSuccess()) {
                                    String msg = Lang.fileConfig.getString("eco-reset").replace("<player>", args[1]).replace("<balance>", plugin.economyImplementer.format(plugin.economyImplementer.getBalance(args[1])));
                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ServerEssentials.hex(msg)));
                                    if (Bukkit.getPlayer(args[1]) != player){
                                        String msg2 = Lang.fileConfig.getString("eco-reset-target").replace("<player>", sender.getName()).replace("<balance>", plugin.economyImplementer.format(plugin.economyImplementer.getBalance(args[1])));
                                        Bukkit.getPlayer(args[1]).sendMessage(ChatColor.translateAlternateColorCodes('&', ServerEssentials.hex(msg2)));
                                    }
                                    UserFile.fileConfig.set(Bukkit.getPlayer(args[1]).getUniqueId() + ".money", plugin.economyImplementer.getBalance(args[1]));
                                    saveBalance();
                                } else {
                                    sender.sendMessage(String.format(ChatColor.RED + "An error occurred: %s", r.errorMessage));
                                }
                            } else {
                                double currentBalance = ServerEssentials.plugin.getConfig().getInt("start-balance");
                                EconomyResponse r = plugin.economyImplementer.depositPlayer(args[1], currentBalance);
                                if (r.transactionSuccess()) {
                                    String msg = Lang.fileConfig.getString("eco-reset").replace("<player>", args[1]).replace("<balance>", plugin.economyImplementer.format(plugin.economyImplementer.getBalance(args[1])));
                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ServerEssentials.hex(msg)));
                                    if (Bukkit.getPlayer(args[1]) != player){
                                        String msg2 = Lang.fileConfig.getString("eco-reset-target").replace("<player>", sender.getName()).replace("<balance>", plugin.economyImplementer.format(plugin.economyImplementer.getBalance(args[1])));
                                        Bukkit.getPlayer(args[1]).sendMessage(ChatColor.translateAlternateColorCodes('&', ServerEssentials.hex(msg2)));
                                    }
                                    UserFile.fileConfig.set(Bukkit.getPlayer(args[1]).getUniqueId() + ".money", plugin.economyImplementer.getBalance(args[1]));
                                    saveBalance();
                                } else {
                                    sender.sendMessage(String.format(ChatColor.RED + "An error occurred: %s", r.errorMessage));
                                }
                            }
                            return true;
                        }
                    }else if (args.length == 3){
                        if (args[0].equalsIgnoreCase("give")) {
                            try {
                                EconomyResponse r = plugin.economyImplementer.depositPlayer(args[1], Integer.parseInt(args[2]));
                                if (r.transactionSuccess()) {
                                    String msg = Lang.fileConfig.getString("eco-give").replace("<amount>", plugin.economyImplementer.format(Double.parseDouble(args[2]))).replace("<player>", args[1]).replace("<balance>", plugin.economyImplementer.format(plugin.economyImplementer.getBalance(args[1])));
                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ServerEssentials.hex(msg)));
                                    if (Bukkit.getPlayer(args[1]) != player){
                                        String msg2 = Lang.fileConfig.getString("eco-receive").replace("<amount>", plugin.economyImplementer.format(Double.parseDouble(args[2]))).replace("<player>", sender.getName()).replace("<balance>", plugin.economyImplementer.format(plugin.economyImplementer.getBalance(args[1])));
                                        Bukkit.getPlayer(args[1]).sendMessage(ChatColor.translateAlternateColorCodes('&', ServerEssentials.hex(msg2)));
                                    }
                                    UserFile.fileConfig.set(Bukkit.getPlayer(args[1]).getUniqueId() + ".money", plugin.economyImplementer.getBalance(args[1]));
                                    saveBalance();
                                } else {
                                    sender.sendMessage(String.format(ChatColor.RED + "An error occurred: %s", r.errorMessage));
                                }
                            } catch (NumberFormatException var12) {
                                sender.sendMessage(ChatColor.RED + "Invalid balance amount. Please enter a valid number.");
                            }
                            return true;
                        }else if (args[0].equalsIgnoreCase("take")) {
                            try {
                                EconomyResponse r = plugin.economyImplementer.withdrawPlayer(args[1], Integer.parseInt(args[2]));
                                if (r.transactionSuccess()) {
                                    String msg = Lang.fileConfig.getString("eco-take").replace("<amount>", plugin.economyImplementer.format(Double.parseDouble(args[2]))).replace("<player>", args[1]).replace("<balance>", plugin.economyImplementer.format(plugin.economyImplementer.getBalance(args[1])));
                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ServerEssentials.hex(msg)));
                                    if (Bukkit.getPlayer(args[1]) != player){
                                        String msg2 = Lang.fileConfig.getString("eco-take-target").replace("<amount>", plugin.economyImplementer.format(Double.parseDouble(args[2]))).replace("<player>", sender.getName()).replace("<balance>", plugin.economyImplementer.format(plugin.economyImplementer.getBalance(args[1])));
                                        Bukkit.getPlayer(args[1]).sendMessage(ChatColor.translateAlternateColorCodes('&', ServerEssentials.hex(msg2)));
                                    }
                                    UserFile.fileConfig.set(Bukkit.getPlayer(args[1]).getUniqueId() + ".money", plugin.economyImplementer.getBalance(args[1]));
                                    saveBalance();
                                } else {
                                    sender.sendMessage(String.format(ChatColor.RED + "An error occurred: %s", r.errorMessage));
                                }
                                return true;
                            } catch (NumberFormatException var14) {
                                sender.sendMessage(ChatColor.RED + "Invalid balance amount. Please enter a valid number.");
                                return false;
                            }
                        }else if (args[0].equalsIgnoreCase("set")){
                            try {
                                int newBalance = Integer.parseInt(args[2]);
                                double currentBalance = plugin.economyImplementer.getBalance(args[1]);
                                if (currentBalance > 0.0) {
                                    plugin.economyImplementer.withdrawPlayer(args[1], currentBalance);
                                } else if (currentBalance < 0.0) {
                                    plugin.economyImplementer.depositPlayer(args[1], -currentBalance);
                                }
                                EconomyResponse r = plugin.economyImplementer.depositPlayer(args[1], newBalance);
                                if (r.transactionSuccess()) {
                                    String msg = Lang.fileConfig.getString("eco-set").replace("<player>", args[1]).replace("<balance>", plugin.economyImplementer.format(plugin.economyImplementer.getBalance(args[1])));
                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ServerEssentials.hex(msg)));
                                    if (Bukkit.getPlayer(args[1]) != player){
                                        String msg2 = Lang.fileConfig.getString("eco-set-target").replace("<balance>", plugin.economyImplementer.format(plugin.economyImplementer.getBalance(args[1])));
                                        Bukkit.getPlayer(args[1]).sendMessage(ChatColor.translateAlternateColorCodes('&', ServerEssentials.hex(msg2)));
                                    }
                                    UserFile.fileConfig.set(Bukkit.getPlayer(args[1]).getUniqueId() + ".money", plugin.economyImplementer.getBalance(args[1]));
                                    saveBalance();
                                } else {
                                    sender.sendMessage(String.format(ChatColor.RED + "An error occurred: %s", r.errorMessage));
                                }
                                return true;
                            } catch (NumberFormatException var13) {
                                sender.sendMessage(ChatColor.RED + "Invalid balance amount. Please enter a valid number.");
                                return false;
                            }
                        }else{
                            incorrectFormat(player);
                            return false;
                        }
                    }else{
                        incorrectFormat(player);
                        return false;
                    }
                }else{
                    String msg = Lang.fileConfig.getString("target-offline");
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ServerEssentials.hex(msg)));
                    return true;
                }
            }else{
                incorrectFormat(player);
                return false;
            }
        }
        return false;
    }

    public static void saveBalance(){
        try {
            UserFile.fileConfig.save(UserFile.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void incorrectFormat(Player sender) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Usage:\n&f/eco give &e<player> <amount> &6- Gives the specified player the specified amount of money.\n&f/eco take &e<player> <amount> &6- Takes the specified amount of money from the specified player.\n&f/eco set &e<player> <amount> &6- Sets the specified player's balance to the specified amount of money.\n&f/eco reset &e<player> &6- Resets the specified player's balance to the server's starting balance."));
    }
}