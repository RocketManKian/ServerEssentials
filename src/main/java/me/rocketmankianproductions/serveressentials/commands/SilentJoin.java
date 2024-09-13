package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
import me.rocketmankianproductions.serveressentials.file.UserFile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

import static me.rocketmankianproductions.serveressentials.ServerEssentials.hex;

public class SilentJoin {

    public static ServerEssentials plugin;

    public SilentJoin(ServerEssentials plugin) {
        this.plugin = plugin;
    }

    public void run(CommandSender sender, String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            if (ServerEssentials.permissionChecker(player, "se.silentjoin")) {
                if (!UserFile.fileConfig.getBoolean(player.getUniqueId() + ".silent")) {
                    UserFile.fileConfig.set(player.getUniqueId() + ".silent", true);
                    String msg = Lang.fileConfig.getString("silentjoin-enabled");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                } else {
                    UserFile.fileConfig.set(player.getUniqueId() + ".silent", false);
                    String msg = Lang.fileConfig.getString("silentjoin-disabled");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                }
                try {
                    UserFile.fileConfig.save(UserFile.file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else{
            String console = Lang.fileConfig.getString("console-invalid");
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', hex(console)));
        }
    }
}