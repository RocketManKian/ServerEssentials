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
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static me.rocketmankianproductions.serveressentials.ServerEssentials.hex;

public class Mute implements CommandExecutor {

    public static final Map<UUID, Long> playerMuteTime = new ConcurrentHashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        switch (command.getName().toLowerCase()) {
            case "mute":
                return handleMuteCommand(sender, args, true);
            case "unmute":
                return handleMuteCommand(sender, args, false);
            default:
                return false;
        }
    }

    private boolean handleMuteCommand(CommandSender sender, String[] args, boolean mute) {
        String type = mute ? "mute" : "unmute";

        // Permission check
        if (!ServerEssentials.permissionChecker(sender, "se." + type)) {
            return false;
        }

        // No args -> show usage
        if (args.length == 0) {
            String msg = Lang.fileConfig.getString("incorrect-format")
                    .replace("<command>", mute ? "/mute <target> <time>" : "/unmute <target>");
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
            return false;
        }

        // Get target
        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

        if (!target.hasPlayedBefore() && !target.isOnline()) {
            String msg = Lang.fileConfig.getString("target-offline");
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
            return false;
        }

        if (target == sender){
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(Lang.fileConfig.getString("target-self"))));
            return false;
        }

        // Bypass check (only for mute)
        if (mute && target.isOnline() && target.getPlayer().hasPermission("se.mute.bypass")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(Lang.fileConfig.getString("mute-bypass"))));
            return false;
        }

        // ------------------
        //     MUTE LOGIC
        // ------------------
        if (mute) {
            int timeSeconds;

            // Permanent mute keyword
            if (args[1].equalsIgnoreCase("perma") || args[1].equalsIgnoreCase("permanent") || args[1].equalsIgnoreCase("-1")) {
                timeSeconds = -1;

            } else {
                // Numeric mute time
                try {
                    timeSeconds = Integer.parseInt(args[1]);
                    if (timeSeconds <= 0) timeSeconds = 0;
                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "This duration is invalid."));
                    return false;
                }
            }

            addMute(target, timeSeconds);

        } else {
            // ------------------
            //    UNMUTE LOGIC
            // ------------------
            removeMute(target); // you should have this method; I can write it if needed
        }

        // Sender feedback
        String msg = Lang.fileConfig.getString(type + "-sender")
                .replace("<target>", target.getName());
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));

        // Target feedback (only if online)
        if (target.isOnline()) {
            Player online = target.getPlayer();
            String msg2 = Lang.fileConfig.getString(type + "-target")
                    .replace("<target>", sender.getName());
            online.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg2)));
        }

        return true;
    }

    public void addMute(OfflinePlayer target, int timeSeconds) {
        UUID uuid = target.getUniqueId();

        UserFile.fileConfig.set(uuid + ".muted", true);

        if (timeSeconds == -1) {
            // permanent mute
            UserFile.fileConfig.set(uuid + ".muteDuration", -1L);
            playerMuteTime.put(uuid, -1L);

        } else if (timeSeconds > 0) {
            // timed mute
            long expire = System.currentTimeMillis() + (timeSeconds * 1000L);
            UserFile.fileConfig.set(uuid + ".muteDuration", expire);
            playerMuteTime.put(uuid, expire);
        }

        try {
            UserFile.fileConfig.save(UserFile.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getPlayerMuteTime(Player player) {
        UUID uuid = player.getUniqueId();
        Long expire = playerMuteTime.get(uuid);

        if (expire == null) return null;

        if (expire == -1L) {
            return "Permanent";
        }

        long now = System.currentTimeMillis();

        if (expire > now) {
            long remaining = (expire - now) / 1000;

            long days = remaining / 86400;
            long hours = (remaining % 86400) / 3600;
            long minutes = (remaining % 3600) / 60;
            long seconds = remaining % 60;

            StringBuilder sb = new StringBuilder();
            if (days > 0) sb.append(days).append("d ");
            if (hours > 0) sb.append(hours).append("h ");
            if (minutes > 0) sb.append(minutes).append("m ");
            if (seconds > 0 || sb.length() == 0) sb.append(seconds).append("s");

            return sb.toString();
        }

        // expired
        playerMuteTime.remove(uuid);
        UserFile.fileConfig.set(uuid + ".muteDuration", null);

        try {
            UserFile.fileConfig.save(UserFile.file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void loadAllMutedPlayers() {
        FileConfiguration config = UserFile.fileConfig;

        for (String key : config.getKeys(false)) {
            try {
                UUID uuid = UUID.fromString(key);
                long expire = config.getLong(key + ".muteDuration", 0L);

                if (expire == -1L) {
                    playerMuteTime.put(uuid, -1L);
                    continue;
                }

                if (expire > System.currentTimeMillis()) {
                    playerMuteTime.put(uuid, expire);
                } else {
                    config.set(key + ".muteDuration", null);
                }

            } catch (IllegalArgumentException ignored) {}
        }

        try {
            config.save(UserFile.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeMute(OfflinePlayer target) {
        UUID uuid = target.getUniqueId();

        // Remove from memory
        playerMuteTime.remove(uuid);

        // Remove from config
        UserFile.fileConfig.set(uuid + ".muted", null);
        UserFile.fileConfig.set(uuid + ".muteDuration", null);

        try {
            UserFile.fileConfig.save(UserFile.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int parseDuration(String input) {

        if (input.equalsIgnoreCase("perma") || input.equalsIgnoreCase("permanent"))
            return -1;

        // Pure number -> seconds
        if (input.matches("\\d+"))
            return Integer.parseInt(input);

        int total = 0;

        // Regex supports: 2d, 10h, 30m, 45s
        Matcher matcher = Pattern.compile("(\\d+)([smhd])").matcher(input.toLowerCase());

        while (matcher.find()) {
            int value = Integer.parseInt(matcher.group(1));
            char unit = matcher.group(2).charAt(0);

            switch (unit) {
                case 's': total += value; break;
                case 'm': total += value * 60; break;
                case 'h': total += value * 3600; break;
                case 'd': total += value * 86400; break;
            }
        }

        return total;
    }
}
