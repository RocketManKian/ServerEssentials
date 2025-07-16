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
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import static me.rocketmankianproductions.serveressentials.ServerEssentials.hex;

public class Mute implements CommandExecutor {

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
        if (!ServerEssentials.permissionChecker(sender, "se." + type)) {
            return false;
        }
        if (args.length != 1){
            String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/mute <target>");
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
            return false;
        }
        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        if (!target.hasPlayedBefore() && !target.isOnline()){
            String msg = Lang.fileConfig.getString("target-offline");
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
            return false;
        }
        if (target == sender){
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(Lang.fileConfig.getString("target-self"))));
            return false;
        }
        if (target.getPlayer().hasPermission("se.mute.bypass")){
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(Lang.fileConfig.getString("mute-bypass"))));
            return false;
        }
        UserFile.fileConfig.set(target.getUniqueId() + ".muted", mute);
        try {
            UserFile.fileConfig.save(UserFile.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String msg = Lang.fileConfig.getString(type + "-sender").replace("<target>", target.getName());
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
        String msg2 = Lang.fileConfig.getString(type + "-target").replace("<target>", sender.getName());
        target.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg2)));
        return true;
    }
}