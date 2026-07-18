package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static me.rocketmankianproductions.serveressentials.ServerEssentials.hex;

public class Gamemode implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        String cmdName = command.getName().toLowerCase();

        // Handle base gamemode / gm commands
        if (cmdName.equals("gamemode")) {
            if (args.length == 0) {
                sendFormattedMessage(sender, "incorrect-format", "/gamemode <survival/creative/spectator/adventure> [player]");
                return true;
            }

            GameMode targetGm = matchGameMode(args[0]);
            if (targetGm == null) {
                sendFormattedMessage(sender, "incorrect-format", "/gamemode <survival/creative/spectator/adventure>");
                return true;
            }

            // If a player is specified as the second argument
            if (args.length >= 2) {
                executeGameModeChange(sender, targetGm, args[1]);
            } else {
                // No player specified -> process for self (only if sender is a player)
                if (sender instanceof Player) {
                    executeGameModeChange(sender, targetGm, sender.getName());
                } else {
                    sendFormattedMessage(sender, "incorrect-format", "/gamemode <mode> (player)");
                }
            }
            return true;
        }

        // Handle all other shortcut commands mapped from your plugin.yml
        GameMode shortcutGm = getShortcutGameMode(cmdName);
        if (shortcutGm != null) {
            if (args.length == 0) {
                if (sender instanceof Player) {
                    executeGameModeChange(sender, shortcutGm, sender.getName());
                } else {
                    sendFormattedMessage(sender, "incorrect-format", "/" + cmdName + " (player)");
                }
            } else if (args.length == 1) {
                executeGameModeChange(sender, shortcutGm, args[0]);
            } else {
                sendFormattedMessage(sender, "incorrect-format", "/" + cmdName + " (player)");
            }
            return true;
        }

        return false;
    }

    /**
     * Executes the gamemode change, processing permissions, target evaluation, and messaging.
     */
    private void executeGameModeChange(CommandSender sender, GameMode gameMode, String targetName) {
        String permission = "se.gamemode." + gameMode.name().toLowerCase();

        // Check permissions if sender is a player
        if (sender instanceof Player && !ServerEssentials.permissionChecker(sender, permission)) {
            return;
        }

        Player targetPlayer = Bukkit.getPlayer(targetName);
        if (targetPlayer == null) {
            sendFormattedMessage(sender, "target-offline", null);
            return;
        }

        // Apply game mode
        targetPlayer.setGameMode(gameMode);
        String modeName = gameMode.name().toLowerCase();

        // Send confirmation messages
        if (sender instanceof Player && sender.getName().equalsIgnoreCase(targetName)) {
            // Target is self
            sendFormattedMessage(sender, "gamemode-" + modeName + "-self", null);
        } else {
            if (!ServerEssentials.permissionChecker(sender, "se.gamemode.others")) return;
            // Target is someone else (or action executed by Console)
            sendFormattedMessage(targetPlayer, "gamemode-" + modeName + "-self", null);

            String targetMsg = Lang.fileConfig.getString("gamemode-" + modeName + "-target");
            if (targetMsg != null) {
                targetMsg = targetMsg.replace("<target>", targetPlayer.getName());
                sendMessage(sender, targetMsg);
            }
        }
    }

    /**
     * Matches string input arguments to Bukkit GameMode types (for /gamemode <arg>).
     */
    private GameMode matchGameMode(String input) {
        switch (input.toLowerCase()) {
            case "creative": case "c": case "1": return GameMode.CREATIVE;
            case "survival": case "s": case "0": return GameMode.SURVIVAL;
            case "spectator": case "sp": case "3": return GameMode.SPECTATOR;
            case "adventure": case "a": case "2": return GameMode.ADVENTURE;
            default: return null;
        }
    }

    /**
     * Maps your plugin.yml root commands directly to their GameMode enum.
     * (Aliases like /c, /s, /gm, etc. point here automatically by Spigot)
     */
    private GameMode getShortcutGameMode(String commandName) {
        switch (commandName) {
            case "gmc": case "creative": return GameMode.CREATIVE;
            case "gms": case "survival": return GameMode.SURVIVAL;
            case "gmsp": case "spectator": return GameMode.SPECTATOR;
            case "gma": case "adventure": return GameMode.ADVENTURE;
            default: return null;
        }
    }

    /**
     * Fetches configuration paths, parses hex/legacy color formatting, and handles variable replacements.
     */
    private void sendFormattedMessage(CommandSender recipient, String configPath, String commandReplacement) {
        String msg = Lang.fileConfig.getString(configPath);
        if (msg == null) return;

        if (commandReplacement != null) {
            msg = msg.replace("<command>", commandReplacement);
        }
        sendMessage(recipient, msg);
    }

    /**
     * Internal utility to route output text to either player chat or console streams safely.
     */
    private void sendMessage(CommandSender recipient, String message) {
        String formatted = ChatColor.translateAlternateColorCodes('&', hex(message));
        if (recipient instanceof ConsoleCommandSender) {
            Bukkit.getLogger().info(formatted);
        } else {
            recipient.sendMessage(formatted);
        }
    }
}