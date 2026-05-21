package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
import me.rocketmankianproductions.serveressentials.file.UserFile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static me.rocketmankianproductions.serveressentials.ServerEssentials.hex;

public class Balance implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                             @NotNull Command command,
                             @NotNull String label,
                             @NotNull String[] args) {

        if (!ServerEssentials.permissionChecker(sender, "se.balance")) {
            return true;
        }

        // =========================
        // /balance (self)
        // =========================
        if (args.length == 0) {

            if (!(sender instanceof Player player)) {
                sender.sendMessage(ChatColor.RED + "Only players can use this.");
                return true;
            }

            double balance = ServerEssentials.getPlugin().economyImplementer.getBalance(player);

            String msg = Lang.fileConfig.getString("eco-balance")
                    .replace("<balance>",  ServerEssentials.getPlugin().economyImplementer.format(balance));

            player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
            return true;
        }

        // =========================
        // /balance <player>
        // =========================
        if (args.length == 1) {

            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

            // Player doesn't exist / never joined
            if (target.getName() == null) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        hex(Lang.fileConfig.getString("player-offline"))
                ));
                return true;
            }

            // hidden balance check (only if your system uses it)
            if (isHidden(target)) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        hex(Lang.fileConfig.getString("hidebalance-view"))
                ));
                return true;
            }

            double balance =  ServerEssentials.getPlugin().economyImplementer.getBalance(target);

            String msg = Lang.fileConfig.getString("eco-balance-target")
                    .replace("<player>", target.getName())
                    .replace("<balance>",  ServerEssentials.getPlugin().economyImplementer.format(balance));

            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
            return true;
        }

        return false;
    }

    /**
     * Keeps compatibility with your old "balance hidden" system.
     * You can later move this into EconomyImplementer or a PlayerData manager.
     */
    private boolean isHidden(OfflinePlayer player) {
        return UserFile.config.getBoolean(
                player.getUniqueId() + ".balancehidden",
                false
        );
    }
}