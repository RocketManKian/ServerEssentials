package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
import me.rocketmankianproductions.serveressentials.utils.JailUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Jail implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        switch (command.getName().toLowerCase()) {

            case "jail" -> handleJail(sender, args);

            case "unjail" -> handleUnjail(sender, args);

            case "jailtime" -> handleJailTime(sender, args);

            case "createjail" -> handleCreateJail(sender, args);

            case "deletejail" -> handleDeleteJail(sender, args);

            default -> sender.sendMessage("Unknown command.");
        }

        return false;
    }

    private void handleJail(CommandSender sender, String[] args) {

        if (ServerEssentials.permissionChecker(sender, "se.jail")){
            if (args.length != 3) {
                sender.sendMessage("§cUsage: /jail <player> <seconds> <jail>");
                return;
            }

            Player target = Bukkit.getPlayer(args[0]);

            if (target == null) {
                sender.sendMessage(ServerEssentials.hex(Lang.fileConfig.getString("target-offline")));
                return;
            }

//            if (target == sender){
//                sender.sendMessage(ServerEssentials.hex(Lang.fileConfig.getString("jail-self")));
//                return;
//            }

            if (ServerEssentials.getInstance.jailManager.isJailed(target)){
                sender.sendMessage(ServerEssentials.hex(Lang.fileConfig.getString("already-jailed")
                        .replace("<player>", target.getName())));
                return;
            }

            int seconds;

            try {
                seconds = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                sender.sendMessage("§cInvalid time.");
                return;
            }

            String jailName = args[2];

            // ✅ CHECK JAIL EXISTS BEFORE JAILING
            if (ServerEssentials.getInstance.jailManager.getJail(jailName) == null) {
                sender.sendMessage(ServerEssentials.hex(Lang.fileConfig.getString("jail-invalid")
                        .replace("<jail>", jailName)));
                return;
            }

            ServerEssentials.getInstance
                    .jailManager
                    .jailPlayer(target, jailName, seconds);

            sender.sendMessage(ServerEssentials.hex(Lang.fileConfig.getString("jail-success")
                    .replace("<player>", target.getName())
                    .replace("<jail>", jailName)
                    .replace("<duration>", String.valueOf(seconds))));
        }
    }

    private void handleUnjail(CommandSender sender, String[] args) {

        if (ServerEssentials.permissionChecker(sender, "se.unjail")){
            if (args.length != 1) {
                sender.sendMessage("§cUsage: /unjail <player>");
                return;
            }

            Player target = Bukkit.getPlayer(args[0]);

            if (target == null) {
                sender.sendMessage(ServerEssentials.hex(Lang.fileConfig.getString("target-offline")));
                return;
            }

            if (!ServerEssentials.getInstance.jailManager.isJailed(target)){
                sender.sendMessage(ServerEssentials.hex(Lang.fileConfig.getString("jail-release-invalid").replace("<player>", target.getName())));
                return;
            }

            ServerEssentials.getInstance
                    .jailManager
                    .releasePlayer(target.getUniqueId());

            sender.sendMessage(ServerEssentials.hex(Lang.fileConfig.getString("jail-release")
                    .replace("<player>", target.getName())));
        }
    }

    private void handleJailTime(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cThis command must be used in-game.");
            return;
        }

        if (ServerEssentials.permissionChecker(sender, "se.jailtime")){
            if (args.length != 0) {
                player.sendMessage("§cUsage: /jailtime");
                return;
            }

            if (!ServerEssentials.getInstance.jailManager.isJailed(player)){
                player.sendMessage(ServerEssentials.hex(Lang.fileConfig.getString("jail-self-invalid")));
                return;
            }

            player.sendMessage(ServerEssentials.hex(Lang.fileConfig.getString("jail-target-attempt").replace("<duration>", ServerEssentials.getInstance.jailManager.getTimeLeft(player))));
        }
    }

    private void handleCreateJail(CommandSender sender, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cThis command must be used in-game.");
            return;
        }

        if (ServerEssentials.permissionChecker(sender, "se.createjail")){
            if (args.length != 1) {
                sender.sendMessage("§cUsage: /createjail <name>");
                return;
            }

            String name = args[0];

            JailUtil existing = ServerEssentials.getInstance
                    .jailManager
                    .getJail(name);

            JailUtil jail = new JailUtil(name, player.getLocation());

            ServerEssentials.getInstance
                    .jailManager
                    .addJail(jail);

            if (existing == null) {
                sender.sendMessage(ServerEssentials.hex(Lang.fileConfig.getString("jail-created").replace("<jail>", name)));
            } else {
                sender.sendMessage(ServerEssentials.hex(Lang.fileConfig.getString("jail-updated").replace("<jail>", name)));
            }
        }
    }

    private void handleDeleteJail(CommandSender sender, String[] args) {

        if (ServerEssentials.permissionChecker(sender, "se.deletejail")){
            if (args.length != 1) {
                sender.sendMessage("§cUsage: /deletejail <name>");
                return;
            }

            String name = args[0];

            JailUtil existing = ServerEssentials.getInstance
                    .jailManager
                    .getJail(name);

            if (existing == null) {
                sender.sendMessage(ServerEssentials.hex(Lang.fileConfig.getString("jail-invalid").replace("<jail>", name)));
                return;
            }

            ServerEssentials.getInstance
                    .jailManager
                    .removeJail(name);

            sender.sendMessage(ServerEssentials.hex(Lang.fileConfig.getString("jail-deleted").replace("<jail>", name)));
        }
    }
}
