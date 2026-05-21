package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static me.rocketmankianproductions.serveressentials.ServerEssentials.hex;

public class Pay implements CommandExecutor {

    private final ServerEssentials plugin = ServerEssentials.getInstance;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return true;
        }

        if (!ServerEssentials.permissionChecker(player, "se.pay")) {
            return true;
        }

        if (args.length != 2) {
            String msg = Lang.fileConfig.getString("incorrect-format")
                    .replace("<command>", "/pay <player> <amount>");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

        if (!target.hasPlayedBefore() && !target.isOnline()) {
            String msg = Lang.fileConfig.getString("target-offline");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
            return true;
        }

        if (target.getUniqueId().equals(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "You cannot pay yourself.");
            return true;
        }

        double amount;

        try {
            amount = Double.parseDouble(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage(ChatColor.RED + "Invalid amount.");
            return true;
        }

        if (amount <= 0) {
            String msg = Lang.fileConfig.getString("eco-invalid");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
            return true;
        }

        if (!plugin.economyImplementer.has(player, amount)) {
            String msg = Lang.fileConfig.getString("eco-insufficient");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
            return true;
        }

        EconomyResponse withdraw = plugin.economyImplementer.withdrawPlayer(player, amount);

        if (!withdraw.transactionSuccess()) {
            player.sendMessage(ChatColor.RED + "Transaction failed: " + withdraw.errorMessage);
            return true;
        }

        EconomyResponse deposit = plugin.economyImplementer.depositPlayer(target, amount);

        if (!deposit.transactionSuccess()) {
            // rollback (important for safety)
            plugin.economyImplementer.depositPlayer(player, amount);
            player.sendMessage(ChatColor.RED + "Transaction failed: " + deposit.errorMessage);
            return true;
        }

        // Messages
        if (target.isOnline()) {
            Player t = target.getPlayer();

            String msg = Lang.fileConfig.getString("eco-receive")
                    .replace("<amount>", plugin.economyImplementer.format(amount))
                    .replace("<player>", player.getName())
                    .replace("<balance>", plugin.economyImplementer.format(plugin.economyImplementer.getBalance(target)));

            t.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
        }

        String msg2 = Lang.fileConfig.getString("eco-pay")
                .replace("<amount>", plugin.economyImplementer.format(amount))
                .replace("<player>", target.getName())
                .replace("<balance>", plugin.economyImplementer.format(plugin.economyImplementer.getBalance(player)));

        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg2)));

        return true;
    }
}