package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Eco implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                             @NotNull Command command,
                             @NotNull String label,
                             @NotNull String[] args) {

        if (!ServerEssentials.permissionChecker(sender, "se.eco")) {
            return true;
        }

        if (args.length < 2) {
            incorrectFormat(sender);
            return true;
        }

        String sub = args[0].toLowerCase();

        switch (sub) {

            case "give" -> handleGive(sender, args);
            case "take" -> handleTake(sender, args);
            case "set" -> handleSet(sender, args);
            case "reset" -> handleReset(sender, args);

            default -> incorrectFormat(sender);
        }

        return true;
    }

    /* =========================
     * GIVE
     * ========================= */
    private void handleGive(CommandSender sender, String[] args) {

        if (args.length != 3) {
            incorrectFormat(sender);
            return;
        }

        double amount = parseAmount(sender, args[2]);
        if (amount <= 0) return;

        if (isAll(args[1])) {
            Bukkit.getOnlinePlayers().forEach(p ->
                    ServerEssentials.getPlugin().economyImplementer.depositPlayer(p, amount)
            );

            broadcast(sender, "eco-give-all", amount, null);
            return;
        }

        EconomyResponse r = ServerEssentials.getPlugin().economyImplementer.depositPlayer(args[1], amount);
        sendResult(sender, args[1], amount, r, "eco-give", "eco-receive");
    }

    /* =========================
     * TAKE
     * ========================= */
    private void handleTake(CommandSender sender, String[] args) {

        if (args.length != 3) {
            incorrectFormat(sender);
            return;
        }

        double amount = parseAmount(sender, args[2]);
        if (amount <= 0) return;

        if (isAll(args[1])) {
            Bukkit.getOnlinePlayers().forEach(p ->
                    ServerEssentials.getPlugin().economyImplementer.withdrawPlayer(p, amount)
            );

            broadcast(sender, "eco-take-all", amount, null);
            return;
        }

        EconomyResponse r = ServerEssentials.getPlugin().economyImplementer.withdrawPlayer(args[1], amount);
        sendResult(sender, args[1], amount, r, "eco-take", "eco-take-target");
    }

    /* =========================
     * SET
     * ========================= */
    private void handleSet(CommandSender sender, String[] args) {

        if (args.length != 3) {
            incorrectFormat(sender);
            return;
        }

        double target = parseAmount(sender, args[2]);
        if (target < 0) return;

        if (isAll(args[1])) {
            Bukkit.getOnlinePlayers().forEach(p -> {
                ServerEssentials.getPlugin().economyImplementer.withdrawPlayer(p, ServerEssentials.getPlugin().economyImplementer.getBalance(p));
                ServerEssentials.getPlugin().economyImplementer.depositPlayer(p, target);
            });

            broadcast(sender, "eco-set-all", target, null);
            return;
        }

        double current = ServerEssentials.getPlugin().economyImplementer.getBalance(args[1]);

        if (current > 0) {
            ServerEssentials.getPlugin().economyImplementer.withdrawPlayer(args[1], current);
        }

        EconomyResponse r = ServerEssentials.getPlugin().economyImplementer.depositPlayer(args[1], target);
        sendResult(sender, args[1], target, r, "eco-set", "eco-set-target");
    }

    /* =========================
     * RESET
     * ========================= */
    private void handleReset(CommandSender sender, String[] args) {

        if (args.length != 2) {
            incorrectFormat(sender);
            return;
        }

        double start = ServerEssentials.getPlugin().getConfig().getDouble("start-balance", 0.0);

        if (isAll(args[1])) {

            Bukkit.getOnlinePlayers().forEach(p -> {
                ServerEssentials.getPlugin().economyImplementer.withdrawPlayer(p, ServerEssentials.getPlugin().economyImplementer.getBalance(p));
                ServerEssentials.getPlugin().economyImplementer.depositPlayer(p, start);
            });

            broadcast(sender, "eco-reset-all", start, null);
            return;
        }

        String target = args[1];
        double balance = ServerEssentials.getPlugin().economyImplementer.getBalance(target);

        if (balance > 0) {
            ServerEssentials.getPlugin().economyImplementer.withdrawPlayer(target, balance);
        }

        EconomyResponse r = ServerEssentials.getPlugin().economyImplementer.depositPlayer(target, start);
        sendResult(sender, target, start, r, "eco-reset", "eco-reset-target");
    }

    /* =========================
     * HELPERS
     * ========================= */

    private boolean isAll(String input) {
        return input.equalsIgnoreCase("*") || input.equalsIgnoreCase("all");
    }

    private double parseAmount(CommandSender sender, String input) {
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Invalid number.");
            return -1;
        }
    }

    private void sendResult(CommandSender sender, String target, double amount,
                            EconomyResponse r, String msgSelf, String msgTarget) {

        if (!r.transactionSuccess()) {
            sender.sendMessage(ChatColor.RED + "Error: " + r.errorMessage);
            return;
        }

        String msg = Lang.fileConfig.getString(msgSelf)
                .replace("<player>", target)
                .replace("<amount>", ServerEssentials.getPlugin().economyImplementer.format(amount))
                .replace("<balance>", ServerEssentials.getPlugin().economyImplementer.format(r.balance));

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ServerEssentials.hex(msg)));

        Player p = Bukkit.getPlayer(target);
        if (p != null && p != sender) {

            String msg2 = Lang.fileConfig.getString(msgTarget)
                    .replace("<player>", sender.getName())
                    .replace("<amount>", ServerEssentials.getPlugin().economyImplementer.format(amount))
                    .replace("<balance>", ServerEssentials.getPlugin().economyImplementer.format(r.balance));

            p.sendMessage(ChatColor.translateAlternateColorCodes('&', ServerEssentials.hex(msg2)));
        }
    }

    private void broadcast(CommandSender sender, String key, double amount, String extra) {

        String msg = Lang.fileConfig.getString(key)
                .replace("<amount>", ServerEssentials.getPlugin().economyImplementer.format(amount));

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', ServerEssentials.hex(msg)));
        }

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ServerEssentials.hex(msg)));
    }

    public void incorrectFormat(CommandSender sender) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&6Usage:\n" +
                        "&f/eco give <player/all/*> <amount>\n" +
                        "&f/eco take <player/all/*> <amount>\n" +
                        "&f/eco set <player/all/*> <amount>\n" +
                        "&f/eco reset <player/all/*>"
        ));
    }
}