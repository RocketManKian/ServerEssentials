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
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import static me.rocketmankianproductions.serveressentials.ServerEssentials.hex;

public class Seen implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length == 1) {
            if (commandSender instanceof ConsoleCommandSender || (commandSender instanceof Player && ServerEssentials.permissionChecker((Player) commandSender, "se.seen"))) {
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
                if (target.isOnline()) {
                    long currentTime = System.currentTimeMillis();
                    long lastLogin = UserFile.fileConfig.getLong(target.getUniqueId() + ".login");
                    long timeElapsed = currentTime - lastLogin;
                    String readableTime = formatTimeElapsed(timeElapsed);

                    commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.fileConfig.getString("seen-online").replace("<player>", target.getName()).replace("<time>", readableTime).replace("<uuid>", target.getUniqueId().toString())));
                    return true;
                }

                if (UserFile.fileConfig.get(target.getUniqueId() + ".logout") != null) {
                    long currentTime = System.currentTimeMillis();
                    long lastLogout = UserFile.fileConfig.getLong(target.getUniqueId() + ".logout");
                    long timeElapsed = currentTime - lastLogout;
                    String readableTime = formatTimeElapsed(timeElapsed);

                    commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.fileConfig.getString("seen-offline").replace("<player>", target.getName()).replace("<time>", readableTime).replace("<uuid>", target.getUniqueId().toString())));
                } else {
                    commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.fileConfig.getString("seen-invalid").replace("<player>", target.getName())));
                }
                return true;
            }
        } else {
            String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/seen <player>");
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
            return true;
        }
        return false;
    }

    private String formatTimeElapsed(long millis) {
        long days = TimeUnit.MILLISECONDS.toDays(millis);
        long hours = TimeUnit.MILLISECONDS.toHours(millis) % 24;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % 60;

        StringBuilder timeString = new StringBuilder();

        if (days > 0) {
            timeString.append(days).append(" day").append(days > 1 ? "s" : "").append(", ");
        }

        if (hours > 0) {
            timeString.append(hours).append(" hour").append(hours > 1 ? "s" : "").append(", ");
        }

        if (minutes > 0) {
            timeString.append(minutes).append(" minute").append(minutes > 1 ? "s" : "").append(", ");
        }

        // Only show seconds if less than 1 minute has passed, or to complete the output.
        if (seconds > 0 || timeString.length() == 0) {  // Show seconds if no other units are shown
            timeString.append(seconds).append(" second").append(seconds > 1 ? "s" : "");
        } else if (timeString.charAt(timeString.length() - 2) == ',') {
            // Remove trailing comma and space if no seconds
            timeString.setLength(timeString.length() - 2);
        }

        return timeString.toString();
    }
}
