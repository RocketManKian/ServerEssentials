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

public class TPToggle implements CommandExecutor {

    public static ServerEssentials plugin;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            if (ServerEssentials.permissionChecker(player, "se.tptoggle")) {
                if (args.length == 0) {
                    if (!UserFile.fileConfig.getBoolean(player.getUniqueId() + ".tptoggle")) {
                        UserFile.fileConfig.set(player.getUniqueId() + ".tptoggle", true);
                        try {
                            UserFile.fileConfig.save(UserFile.file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        String msg = Lang.fileConfig.getString("teleport-toggle-enabled");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                        return true;
                    } else {
                        UserFile.fileConfig.set(player.getUniqueId() + ".tptoggle", false);
                        try {
                            UserFile.fileConfig.save(UserFile.file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        String msg = Lang.fileConfig.getString("teleport-toggle-disabled");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                        return true;
                    }
                } else {
                    String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/tptoggle");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                    return true;
                }
            }
        }else{
            String console = Lang.fileConfig.getString("console-invalid");
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', hex(console)));
            return true;
        }
        return true;
    }
}
