package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Teleport implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("se.teleport")) {
                // Checking if the player has the correct permission
                if (args.length == 0) {
                    player.sendMessage(ChatColor.RED + "You need to enter some arguments." + ChatColor.YELLOW
                            + "\nTo teleport yourself: /teleport <otherplayer>" + ChatColor.YELLOW
                            + "\nTo teleport others: /teleport <player> <otherplayer>");
                    return true;
                } else if (args.length == 1) {
                    Player target = Bukkit.getPlayer(args[0]);
                    String sender2 = player.getDisplayName();
                    if (target == null){
                        sender.sendMessage(ChatColor.RED + "Player doesn't exist");
                        return true;
                    }else if (target == player) {
                        sender.sendMessage(ChatColor.RED + "You cannot teleport to yourself!");
                        return true;
                    } else {
                        String target2 = target.getName();
                        try {
                            if (sender.hasPermission("se.silenttp")) {
                                sender.sendMessage(ChatColor.GREEN + "Teleported to " + ChatColor.GOLD + target2);
                                if (ServerEssentials.plugin.getConfig().getBoolean("teleport-save")){
                                    if (Back.location.containsKey(player.getUniqueId())){
                                        Back.location.remove(player.getUniqueId());
                                        Back.location.put(player.getUniqueId(), player.getLocation());
                                    }else{
                                        Back.location.put(player.getUniqueId(), player.getLocation());
                                    }
                                }
                                player.teleport(target.getLocation());
                                return true;
                            } else if (!sender.hasPermission("se.silenttp")) {
                                if (ServerEssentials.plugin.getConfig().getBoolean("teleport-save")){
                                    if (Back.location.containsKey(player.getUniqueId())){
                                        Back.location.remove(player.getUniqueId());
                                        Back.location.put(player.getUniqueId(), player.getLocation());
                                    }else{
                                        Back.location.put(player.getUniqueId(), player.getLocation());
                                    }
                                }
                                sender.sendMessage(ChatColor.GREEN + "Teleported to " + ChatColor.GOLD + target2);
                                target.sendMessage(ChatColor.GOLD + sender2 + ChatColor.GREEN + " has teleported to you!");
                                player.teleport(target.getLocation());
                                return true;
                            }
                        } catch (NullPointerException e) {
                            player.sendMessage(ChatColor.RED + "Player does not exist.");
                            return true;
                        }
                    }
                } else if (args.length == 2) {
                    Player playerToSend = Bukkit.getPlayer(args[0]);
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target == null){
                        sender.sendMessage(ChatColor.RED + "Player doesn't exist");
                        return true;
                    }else if (playerToSend == target) {
                        sender.sendMessage(ChatColor.RED + "You cannot teleport someone to themself!");
                        return true;
                    } else {
                        try {
                            String target2 = target.getName();
                            String sender2 = playerToSend.getName();
                            if (sender.hasPermission("se.silenttp")) {
                                if (target == sender){
                                    target.sendMessage(ChatColor.GOLD + sender2 + ChatColor.GREEN + " has teleported to you!");
                                    if (ServerEssentials.plugin.getConfig().getBoolean("teleport-save")){
                                        if (Back.location.containsKey(playerToSend.getUniqueId())){
                                            Back.location.remove(playerToSend.getUniqueId());
                                            Back.location.put(playerToSend.getUniqueId(), playerToSend.getLocation());
                                        }else{
                                            Back.location.put(playerToSend.getUniqueId(), playerToSend.getLocation());
                                        }
                                    }
                                    playerToSend.teleport(target.getLocation());
                                    return true;
                                }else if (playerToSend == sender){
                                    playerToSend.sendMessage(ChatColor.GREEN + "You have teleported to " + ChatColor.GOLD + target2);
                                    if (ServerEssentials.plugin.getConfig().getBoolean("teleport-save")){
                                        if (Back.location.containsKey(playerToSend.getUniqueId())){
                                            Back.location.remove(playerToSend.getUniqueId());
                                            Back.location.put(playerToSend.getUniqueId(), playerToSend.getLocation());
                                        }else{
                                            Back.location.put(playerToSend.getUniqueId(), playerToSend.getLocation());
                                        }
                                    }
                                    playerToSend.teleport(target.getLocation());
                                    return true;
                                }else{
                                    sender.sendMessage(ChatColor.GREEN + "Teleported " + ChatColor.GOLD + sender2 + ChatColor.GREEN + " to " + ChatColor.GOLD + target2);
                                    playerToSend.sendMessage(ChatColor.GREEN + "You have teleported to " + ChatColor.GOLD + target2);
                                    target.sendMessage(ChatColor.GOLD + sender2 + ChatColor.GREEN + " has teleported to you!");
                                    if (ServerEssentials.plugin.getConfig().getBoolean("teleport-save")){
                                        if (Back.location.containsKey(playerToSend.getUniqueId())){
                                            Back.location.remove(playerToSend.getUniqueId());
                                            Back.location.put(playerToSend.getUniqueId(), playerToSend.getLocation());
                                        }else{
                                            Back.location.put(playerToSend.getUniqueId(), playerToSend.getLocation());
                                        }
                                    }
                                    playerToSend.teleport(target.getLocation());
                                    return true;
                                }
                            } else if (!sender.hasPermission("se.silenttp")) {
                                if (target == sender) {
                                    playerToSend.sendMessage(ChatColor.GREEN + "You have teleported to " + ChatColor.GOLD + target2);
                                    target.sendMessage(ChatColor.GOLD + sender2 + ChatColor.GREEN + " has teleported to you!");
                                    if (ServerEssentials.plugin.getConfig().getBoolean("teleport-save")){
                                        if (Back.location.containsKey(playerToSend.getUniqueId())){
                                            Back.location.remove(playerToSend.getUniqueId());
                                            Back.location.put(playerToSend.getUniqueId(), playerToSend.getLocation());
                                        }else{
                                            Back.location.put(playerToSend.getUniqueId(), playerToSend.getLocation());
                                        }
                                    }
                                    playerToSend.teleport(target.getLocation());
                                    return true;
                                } else if (playerToSend == sender){
                                    target.sendMessage(ChatColor.GOLD + sender2 + ChatColor.GREEN + " has teleported to you!");
                                    playerToSend.sendMessage(ChatColor.GREEN + "You have teleported to " + ChatColor.GOLD + target2);
                                    if (ServerEssentials.plugin.getConfig().getBoolean("teleport-save")){
                                        if (Back.location.containsKey(playerToSend.getUniqueId())){
                                            Back.location.remove(playerToSend.getUniqueId());
                                            Back.location.put(playerToSend.getUniqueId(), playerToSend.getLocation());
                                        }else{
                                            Back.location.put(playerToSend.getUniqueId(), playerToSend.getLocation());
                                        }
                                    }
                                    playerToSend.teleport(target.getLocation());
                                    return true;
                                }else{
                                    sender.sendMessage(ChatColor.GREEN + "Teleported " + ChatColor.GOLD + sender2 + ChatColor.GREEN + " to " + ChatColor.GOLD + target2);
                                    target.sendMessage(ChatColor.GOLD + sender2 + ChatColor.GREEN + " has teleported to you!");
                                    playerToSend.sendMessage(ChatColor.GREEN + "You have teleported to " + ChatColor.GOLD + target2);
                                    if (ServerEssentials.plugin.getConfig().getBoolean("teleport-save")){
                                        if (Back.location.containsKey(playerToSend.getUniqueId())){
                                            Back.location.remove(playerToSend.getUniqueId());
                                            Back.location.put(playerToSend.getUniqueId(), playerToSend.getLocation());
                                        }else{
                                            Back.location.put(playerToSend.getUniqueId(), playerToSend.getLocation());
                                        }
                                    }
                                    playerToSend.teleport(target.getLocation());
                                    return true;
                                }
                            }
                        } catch (NullPointerException e) {
                            player.sendMessage(ChatColor.RED + "Player does not exist.");
                            return true;
                        }
                    }
                } else if (args.length >= 2) {
                    return false;
                }
            } else {
                // If it doesn't succeed with either then it'll send the player a required permission message
                if (ServerEssentials.plugin.getConfig().getString("no-permission-message").length() == 0) {
                    player.sendMessage(ChatColor.RED + "You do not have the required permission (se.teleport) to run this command.");
                    return true;
                } else {
                    String permission = ServerEssentials.getPlugin().getConfig().getString("no-permission-message");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', permission));
                    return true;
                }
            }
        } else if (sender instanceof ConsoleCommandSender) {
            if (args.length == 2){
                Player playerToSend = Bukkit.getPlayer(args[0]);
                Player target = Bukkit.getPlayer(args[1]);
                if (playerToSend != target) {
                    try {
                        String target2 = target.getName();
                        String sender2 = playerToSend.getName();
                        if (ServerEssentials.plugin.getConfig().getBoolean("teleport-save")){
                            if (Back.location.containsKey(playerToSend.getUniqueId())){
                                Back.location.remove(playerToSend.getUniqueId());
                                Back.location.put(playerToSend.getUniqueId(), playerToSend.getLocation());
                            }else{
                                Back.location.put(playerToSend.getUniqueId(), playerToSend.getLocation());
                            }
                        }
                        playerToSend.teleport(target.getLocation());
                        sender.sendMessage(ChatColor.GREEN + "Teleported " + ChatColor.GOLD + sender2 + ChatColor.GREEN + " to " + ChatColor.GOLD + target2);
                        return true;
                    } catch (NullPointerException e) {
                        sender.sendMessage(ChatColor.RED + "Player does not exist.");
                        return true;
                    }
                }
            }
        } else if (sender instanceof BlockCommandSender) {
            if (args.length == 2){
                Player playerToSend = Bukkit.getPlayer(args[0]);
                Player target = Bukkit.getPlayer(args[1]);
                if (playerToSend != target) {
                    try {
                        String target2 = target.getName();
                        String sender2 = playerToSend.getName();
                        if (ServerEssentials.plugin.getConfig().getBoolean("teleport-save")){
                            if (Back.location.containsKey(playerToSend.getUniqueId())){
                                Back.location.remove(playerToSend.getUniqueId());
                                Back.location.put(playerToSend.getUniqueId(), playerToSend.getLocation());
                            }else{
                                Back.location.put(playerToSend.getUniqueId(), playerToSend.getLocation());
                            }
                        }
                        playerToSend.teleport(target.getLocation());
                        playerToSend.sendMessage(ChatColor.GREEN + "You have been teleported to " + ChatColor.GOLD + target2);
                        target.sendMessage(ChatColor.GOLD + sender2 + ChatColor.GREEN +" has been teleported to you");
                        sender.sendMessage(ChatColor.GREEN + "Teleported " + ChatColor.GOLD + sender2 + ChatColor.GREEN + " to " + ChatColor.GOLD + target2);
                        return true;
                    } catch (NullPointerException e) {
                        sender.sendMessage(ChatColor.RED + "Player does not exist.");
                        return true;
                    }
                }
            }
        }
        return false;
    }
}