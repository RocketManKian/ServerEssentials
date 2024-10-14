package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
import me.rocketmankianproductions.serveressentials.file.UserFile;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static me.rocketmankianproductions.serveressentials.ServerEssentials.hex;

public class Pay implements CommandExecutor {
    private ServerEssentials plugin = ServerEssentials.getInstance;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        Player player = (Player)sender;
        if (ServerEssentials.permissionChecker(player, "se.pay")) {
            if (UserFile.fileConfig.getString(String.valueOf(player.getUniqueId())) == null){
                UserFile.fileConfig.set(player.getUniqueId() + ".money", plugin.getConfig().getDouble("start-balance"));
                Eco.saveBalance();
            }
            plugin.playerBank.put(player.getUniqueId(), UserFile.fileConfig.getDouble(player.getUniqueId() + ".money"));

            if (args.length == 2){
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
                if (target.hasPlayedBefore() && target != player){
                    if (!UserFile.fileConfig.getBoolean(target.getUniqueId() + ".paytoggle")) {
                        // Make sure Target has money
                        if (UserFile.fileConfig.getString(String.valueOf(target.getUniqueId())) == null){
                            UserFile.fileConfig.set(player.getUniqueId() + ".money", plugin.getConfig().getDouble("start-balance"));
                            Eco.saveBalance();
                        }
                        plugin.playerBank.put(target.getUniqueId(), UserFile.fileConfig.getDouble(target.getUniqueId() + ".money"));
                        // Pay Target
                        double amount = Double.parseDouble(args[1]);
                        try {
                            if (amount > 0){
                                if (plugin.economyImplementer.has(player, amount)){
                                    EconomyResponse r = plugin.economyImplementer.depositPlayer(args[0], amount);
                                    plugin.economyImplementer.withdrawPlayer(player, amount);
                                    if (r.transactionSuccess()) {
                                        if (target.isOnline()){
                                            String msg = Lang.fileConfig.getString("eco-receive").replace("<amount>", plugin.economyImplementer.format(amount)).replace("<player>", args[0]).replace("<balance>", plugin.economyImplementer.format(plugin.economyImplementer.getBalance(target)));
                                            target.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', ServerEssentials.hex(msg)));
                                        }
                                        String msg2 = Lang.fileConfig.getString("eco-pay").replace("<amount>", plugin.economyImplementer.format(amount)).replace("<player>", args[0]).replace("<balance>", plugin.economyImplementer.format(plugin.economyImplementer.getBalance(target)));
                                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ServerEssentials.hex(msg2)));
                                        UserFile.fileConfig.set(player.getUniqueId() + ".money", plugin.economyImplementer.getBalance(player));
                                        UserFile.fileConfig.set(target.getUniqueId() + ".money", plugin.economyImplementer.getBalance(target));
                                        Eco.saveBalance();
                                        return true;
                                    } else {
                                        sender.sendMessage(String.format(ChatColor.RED + "An error occurred: %s", r.errorMessage));
                                    }
                                }else {
                                    String msg = Lang.fileConfig.getString("eco-insufficient");
                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ServerEssentials.hex(msg)));
                                    return false;
                                }
                            }else{
                                String msg = Lang.fileConfig.getString("eco-invalid");
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ServerEssentials.hex(msg)));
                                return false;
                            }
                        } catch (NumberFormatException var12) {
                            sender.sendMessage(ChatColor.RED + "Invalid balance amount. Please enter a valid number.");
                        }
                    }else{
                        String msg = Lang.fileConfig.getString("pay-disabled");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                        return true;
                    }
                    return true;
                }else{
                    String msg = Lang.fileConfig.getString("target-offline");
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                    return false;
                }
            }else{
                String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/pay (player) <amount>");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                return false;
            }
        }
        return false;
    }
}