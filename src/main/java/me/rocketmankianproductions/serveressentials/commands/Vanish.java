package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Vanish implements CommandExecutor {

    ServerEssentials plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player){
            Player player = (Player) sender;
            if (player.hasPermission("se.vanish")){
                if (args.length == 0){
                    if (ServerEssentials.getPlugin().invisible_list.contains(player)){
                        for (Player people : Bukkit.getOnlinePlayers()){
                            people.showPlayer(ServerEssentials.getPlugin(), player);
                        }
                        ServerEssentials.getPlugin().invisible_list.remove(player);
                        player.sendMessage(ChatColor.GREEN + "You are now visible to other players on the server.");
                    }else if(!ServerEssentials.getPlugin().invisible_list.contains(player)){
                        for (Player people : Bukkit.getOnlinePlayers()){
                            people.hidePlayer(ServerEssentials.getPlugin(), player);
                        }
                        ServerEssentials.getPlugin().invisible_list.add(player);
                        player.sendMessage(ChatColor.GREEN + "You are now invisible!");
                    }
                }else if (args.length == 1){
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target == sender) {
                        sender.sendMessage(ChatColor.RED + "You cannot message yourself.");
                        return true;
                    } else if (target == null) {
                        sender.sendMessage(ChatColor.RED + "Player does not exist");
                        return true;
                    } else if (ServerEssentials.getPlugin().invisible_list.contains(target)){
                        for (Player people : Bukkit.getOnlinePlayers()){
                            people.showPlayer(ServerEssentials.getPlugin(), target);
                        }
                        ServerEssentials.getPlugin().invisible_list.remove(target);
                        target.sendMessage(ChatColor.GREEN + "You are now visible to other players on the server.");
                        player.sendMessage(target.getName() + " is now visible");
                    } else if (!ServerEssentials.getPlugin().invisible_list.contains(target)){
                        for (Player people : Bukkit.getOnlinePlayers()){
                            people.hidePlayer(ServerEssentials.getPlugin(), target);
                        }
                        ServerEssentials.getPlugin().invisible_list.add(target);
                        target.sendMessage(ChatColor.GREEN + "You are now invisible!");
                        player.sendMessage(target.getName() + " is now invisible");
                    }
                }else{
                    return false;
                }
            }else{
                if (ServerEssentials.plugin.getConfig().getString("no-permission-message").length() == 0){
                    player.sendMessage(ChatColor.RED + "You do not have the required permission (se.vanish) to run this command.");
                    return true;
                }else{
                    String permission = ServerEssentials.getPlugin().getConfig().getString("no-permission-message");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', permission));
                    return true;
                }
            }
        }
        return true;
    }
}
