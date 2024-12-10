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

import java.io.IOException;

import static me.rocketmankianproductions.serveressentials.ServerEssentials.hex;

public class Vanish implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            if (ServerEssentials.permissionChecker(player, "se.vanish")) {
                if (args.length == 0){
                    if (UserFile.fileConfig.getBoolean(player.getUniqueId() + ".vanish")) {
                        for (Player people : Bukkit.getOnlinePlayers()){
                            people.showPlayer(ServerEssentials.getPlugin(), player);
                        }
                        String msg = Lang.fileConfig.getString("vanish-disabled");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                        UserFile.fileConfig.set(player.getUniqueId() + ".vanish", false);
                        try {
                            UserFile.fileConfig.save(UserFile.file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else if (!UserFile.fileConfig.getBoolean(player.getUniqueId() + ".vanish")) {
                        for (Player people : Bukkit.getOnlinePlayers()){
                            if (!people.hasPermission("se.vanish.see")){
                                people.hidePlayer(ServerEssentials.getPlugin(), player);
                            }
                        }
                        String msg = Lang.fileConfig.getString("vanish-enabled");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                        UserFile.fileConfig.set(player.getUniqueId() + ".vanish", true);
                        try {
                            UserFile.fileConfig.save(UserFile.file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }else if (args.length == 1){
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target == sender) {
                        String msg = Lang.fileConfig.getString("target-self");
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                        return true;
                    } else if (target == null) {
                        String msg = Lang.fileConfig.getString("target-offline");
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                        return true;
                    } else if (UserFile.fileConfig.getBoolean(target.getUniqueId() + ".vanish")) {
                        for (Player people : Bukkit.getOnlinePlayers()){
                            people.showPlayer(ServerEssentials.getPlugin(), target);
                        }
                        String msg = Lang.fileConfig.getString("vanish-disabled");
                        target.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                        String msg2 = Lang.fileConfig.getString("vanish-target-disabled").replace("<target>", target.getName());
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg2)));
                        UserFile.fileConfig.set(target.getUniqueId() + ".vanish", false);
                        try {
                            UserFile.fileConfig.save(UserFile.file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else if (!UserFile.fileConfig.getBoolean(target.getUniqueId() + ".vanish")) {
                        for (Player people : Bukkit.getOnlinePlayers()){
                            if (!people.hasPermission("se.vanish.see")){
                                people.hidePlayer(ServerEssentials.getPlugin(), target);
                            }
                        }
                        String msg = Lang.fileConfig.getString("vanish-enabled");
                        target.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                        String msg2 = Lang.fileConfig.getString("vanish-target-enabled").replace("<target>", target.getName());
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg2)));
                        UserFile.fileConfig.set(target.getUniqueId() + ".vanish", true);
                        try {
                            UserFile.fileConfig.save(UserFile.file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }else{
                    String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/vanish");
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