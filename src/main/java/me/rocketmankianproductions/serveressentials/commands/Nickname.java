package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
import me.rocketmankianproductions.serveressentials.file.UserFile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import static me.rocketmankianproductions.serveressentials.ServerEssentials.hex;

public class Nickname implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        handleNicknameCommand(sender, args);
        return true;
    }

    private void handleNicknameCommand(CommandSender sender, String[] args) {
        // --- 1 ARGUMENT CHECKS ---
        if (args.length == 1) {
            if (!(sender instanceof Player player)) {
                sendMessage(sender, "&cOnly players can set their own nickname. Use /nickname <player> <nick>");
                return;
            }

            // Syntax: /nickname reset
            if (args[0].equalsIgnoreCase("reset")) {
                applyNickname(player, player.getName());
                String msg = Lang.fileConfig.getString("nickname-reset").replace("<nickname>", player.getName());
                player.sendMessage(hex(msg));
                return;
            }

            // Syntax: /nickname <nick>
            if (!ServerEssentials.permissionChecker(player, "se.nickname")) return;

            applyNickname(player, args[0]);
            String msg = Lang.fileConfig.getString("nickname-set").replace("<nickname>", args[0]);
            player.sendMessage(hex(msg));
            return;
        }

        // --- 2 ARGUMENTS CHECKS ---
        if (args.length == 2) {
            if (sender instanceof Player player && !ServerEssentials.permissionChecker(player, "se.nickname.target")) {
                return;
            }

            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sendMessage(sender, Lang.fileConfig.getString("target-offline"));
                return;
            }

            // Syntax: /nickname <player> reset
            if (args[1].equalsIgnoreCase("reset")) {
                applyNickname(target, target.getName());
                String msg = Lang.fileConfig.getString("nickname-reset-target")
                        .replace("<nickname>", target.getName())
                        .replace("<player>", target.getName());
                sendMessage(sender, msg);
                return;
            }

            // Syntax: /nickname <player> <nick>
            applyNickname(target, args[1]);
            String msg = Lang.fileConfig.getString("nickname-set-target")
                    .replace("<nickname>", args[1])
                    .replace("<player>", target.getName());
            sendMessage(sender, msg);
            return;
        }

        // Incorrect arguments fallback
        sendMessage(sender, "&cUsage: /nickname <nick|reset> OR /nickname <player> <nick|reset>");
    }

    // Helper method to apply changes and save to YML file safely
    private void applyNickname(Player target, String nickname) {
        nickname = ChatColor.translateAlternateColorCodes('&', nickname);
        target.setDisplayName(nickname);
        UserFile.config.set(target.getUniqueId() + ".nickname", nickname);
        try {
            UserFile.config.save(UserFile.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Helper to format and send messages cleanly to both Players and Console loggers
    private void sendMessage(CommandSender sender, String message) {
        if (message == null) return;
        if (sender instanceof Player) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(message)));
        } else {
            // Strip colors for console readability
            Bukkit.getLogger().info(ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', hex(message))));
        }
    }
}