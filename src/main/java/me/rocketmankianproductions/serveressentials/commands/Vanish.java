package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Vanish implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player){
            Player player = (Player) sender;
            if (player.hasPermission("se.vanish") || player.hasPermission("se.all")){
                if (args.length == 0){
                    if (ServerEssentials.getPlugin().invisible_list.contains(player)){
                        for (Player people : Bukkit.getOnlinePlayers()){
                            people.showPlayer(ServerEssentials.getPlugin(), player);
                        }
                        ServerEssentials.getPlugin().invisible_list.remove(player);
                        String msg = Lang.fileConfig.getString("vanish-disabled");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    }else if(!ServerEssentials.getPlugin().invisible_list.contains(player)){
                        for (Player people : Bukkit.getOnlinePlayers()){
                            if (!people.hasPermission("se.vanish")){
                                people.hidePlayer(ServerEssentials.getPlugin(), player);
                            }
                        }
                        ServerEssentials.getPlugin().invisible_list.add(player);
                        String msg = Lang.fileConfig.getString("vanish-enabled");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    }
                }else if (args.length == 1){
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target == sender) {
                        String msg = Lang.fileConfig.getString("target-self");
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    } else if (target == null) {
                        String msg = Lang.fileConfig.getString("target-offline");
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    } else if (ServerEssentials.getPlugin().invisible_list.contains(target)){
                        for (Player people : Bukkit.getOnlinePlayers()){
                            people.showPlayer(ServerEssentials.getPlugin(), target);
                        }
                        ServerEssentials.getPlugin().invisible_list.remove(target);
                        String msg = Lang.fileConfig.getString("vanish-disabled");
                        target.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        String msg2 = Lang.fileConfig.getString("vanish-target-disabled").replace("<target>", target.getName());
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg2));
                    } else if (!ServerEssentials.getPlugin().invisible_list.contains(target)){
                        for (Player people : Bukkit.getOnlinePlayers()){
                            if (!people.hasPermission("se.vanish")){
                                people.hidePlayer(ServerEssentials.getPlugin(), target);
                            }
                        }
                        ServerEssentials.getPlugin().invisible_list.add(target);
                        String msg = Lang.fileConfig.getString("vanish-enabled");
                        target.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        String msg2 = Lang.fileConfig.getString("vanish-target-enabled").replace("<target>", target.getName());
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg2));
                    }
                }else{
                    return false;
                }
            }else{
                String perm = Lang.fileConfig.getString("no-permission-message").replace("<permission>", "se.vanish");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', perm));
                return true;
            }
        }
        return true;
    }
}
