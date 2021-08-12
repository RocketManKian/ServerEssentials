package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Message implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player){
            Player messager = (Player) sender;
            // Check if the Sender has the se.message permission
            if (messager.hasPermission("se.message")) {
                String sm = "";
                if (args.length >= 2) {
                    //recipient == target
                    Player recipient = Bukkit.getPlayer(args[0]);
                    if (recipient == sender) {
                        String msg = Lang.fileConfig.getString("message-self");
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    } else if (recipient == null) {
                        String msg = Lang.fileConfig.getString("target-offline");
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    }
                    // Check if recipient has messaging enabled
                    else if (!MsgToggle.fileConfig.getBoolean("msgtoggle." + recipient.getName(), false)) {
                        // set players to hashmap
                        Reply.reply.put(recipient.getUniqueId(), messager.getUniqueId());
                        String targetname = recipient.getName();
                        String sendername = sender.getName();
                        for (int i = 1; i < args.length; i++) {
                            String arg = (args[i] + " ");
                            sm = (sm + arg);
                        }
                        // Check if the Sender doesn't have the se.socialspy permission
                        if (!sender.hasPermission("se.socialspy")) {
                            // Loop to check through all Online Players and get all players who are included within the HashMap
                            for (Player admin : Bukkit.getOnlinePlayers()) {
                                if (SocialSpy.socialspy.contains(admin)) {
                                    if (admin.getUniqueId().equals(sender)){
                                        sender.sendMessage(ChatColor.YELLOW + "me" + ChatColor.GOLD + " >> " + ChatColor.WHITE + targetname + ChatColor.GRAY + " : " + ChatColor.translateAlternateColorCodes('&', sm));
                                        recipient.sendMessage(sendername + ChatColor.GOLD + " >> " + ChatColor.YELLOW + "me" + ChatColor.GRAY + " : " + ChatColor.translateAlternateColorCodes('&', sm));
                                        return true;
                                    }else{
                                        admin.sendMessage(ChatColor.RED + "[SocialSpy] " + ChatColor.WHITE + sendername + ChatColor.GOLD + " >> " + ChatColor.WHITE + targetname + ChatColor.GRAY + " : " + ChatColor.translateAlternateColorCodes('&', sm));
                                        return true;
                                    }
                                }
                            }
                            sender.sendMessage(ChatColor.YELLOW + "me" + ChatColor.GOLD + " >> " + ChatColor.WHITE + targetname + ChatColor.GRAY + " : " + ChatColor.translateAlternateColorCodes('&', sm));
                            recipient.sendMessage(sendername + ChatColor.GOLD + " >> " + ChatColor.YELLOW + "me" + ChatColor.GRAY + " : " + ChatColor.translateAlternateColorCodes('&', sm));
                            return true;
                        } else {
                            // Loop to check through all Online Players and get all players who are included within the HashMap
                            for (Player admin : Bukkit.getOnlinePlayers()) {
                                if (SocialSpy.socialspy.contains(admin)) {
                                    if (admin.getUniqueId().equals(sender)){
                                        sender.sendMessage(ChatColor.YELLOW + "me" + ChatColor.GOLD + " >> " + ChatColor.WHITE + targetname + ChatColor.GRAY + " : " + ChatColor.translateAlternateColorCodes('&', sm));
                                        recipient.sendMessage(sendername + ChatColor.GOLD + " >> " + ChatColor.YELLOW + "me" + ChatColor.GRAY + " : " + ChatColor.translateAlternateColorCodes('&', sm));
                                        return true;
                                    }else{
                                        admin.sendMessage(ChatColor.RED + "[SocialSpy] " + ChatColor.WHITE + sendername + ChatColor.GOLD + " >> " + ChatColor.WHITE + targetname + ChatColor.GRAY + " : " + ChatColor.translateAlternateColorCodes('&', sm));
                                        return true;
                                    }
                                }
                            }
                            sender.sendMessage(ChatColor.YELLOW + "me" + ChatColor.GOLD + " >> " + ChatColor.WHITE + targetname + ChatColor.GRAY + " : " + ChatColor.translateAlternateColorCodes('&', sm));
                            recipient.sendMessage(sendername + ChatColor.GOLD + " >> " + ChatColor.YELLOW + "me" + ChatColor.GRAY + " : " + ChatColor.translateAlternateColorCodes('&', sm));
                            return true;
                        }
                    } else {
                        String msg = Lang.fileConfig.getString("message-disabled");
                        messager.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    }
                }
            } else {
                String perm = Lang.fileConfig.getString("no-permission-message").replace("<permission>", "se.message");
                messager.sendMessage(ChatColor.translateAlternateColorCodes('&', perm));
                return true;
            }
        }else if (sender instanceof ConsoleCommandSender){
            String sm = "";
            if (args.length <= 1) {
                System.out.println(ChatColor.RED + "Usage: msg <player> <message>");
                return true;
            } else if (args.length >= 2) {
                Player recipient = Bukkit.getPlayer(args[0]);
                if (recipient == null) {
                    System.out.println(ChatColor.RED + "Player does not exist");
                    return true;
                } else if (MsgToggle.fileConfig.getBoolean("msgtoggle." + recipient.getName(), false) == false) {
                    String targetname = recipient.getName();
                    for (int i = 1; i < args.length; i++) {
                        String arg = (args[i] + " ");
                        sm = (sm + arg);
                    }
                    System.out.println(ChatColor.YELLOW + "me" + ChatColor.GOLD + " >> " + ChatColor.WHITE + targetname + ChatColor.GRAY + " : " + ChatColor.translateAlternateColorCodes('&', sm));
                    recipient.sendMessage("Console" + ChatColor.GOLD + " >> " + ChatColor.YELLOW + "me" + ChatColor.GRAY + " : " + ChatColor.translateAlternateColorCodes('&', sm));
                    return true;
                } else {
                    String msg = Lang.fileConfig.getString("message-disabled");
                    System.out.println(ChatColor.translateAlternateColorCodes('&', msg));
                }
            }
        }
        return false;
    }
}