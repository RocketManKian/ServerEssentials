package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
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
            if (messager.hasPermission("se.message")) {
                String sm = "";
                if (args.length <= 1) {
                    sender.sendMessage(ChatColor.RED + "Usage: /msg <player> <message>");
                    return true;
                } else {
                    Player recipient = Bukkit.getPlayer(args[0]);
                    if (recipient == sender) {
                        sender.sendMessage(ChatColor.RED + "You cannot message yourself.");
                        return true;
                    } else if (recipient == null) {
                        sender.sendMessage(ChatColor.RED + "Player does not exist");
                        return true;
                    }
                    // check if recipient has messaging enabled
                    else if (!MsgToggle.fileConfig.getBoolean("msgtoggle." + recipient.getName(), false)) {
                        Reply.reply.put(recipient.getUniqueId(), messager.getUniqueId()); // set players to hashmap
                        String targetname = recipient.getName();
                        String sendername = sender.getName();
                        for (int i = 1; i < args.length; i++) {
                            String arg = (args[i] + " ");
                            sm = (sm + arg);
                        }
                        if (!sender.hasPermission("se.socialspy")){
                            sender.sendMessage(ChatColor.YELLOW + "me" + ChatColor.GOLD + " >> " + ChatColor.WHITE + targetname + ChatColor.GRAY + " : " + ChatColor.translateAlternateColorCodes('&', sm));
                            recipient.sendMessage(sendername + ChatColor.GOLD + " >> " + ChatColor.YELLOW + "me" + ChatColor.GRAY + " : " + ChatColor.translateAlternateColorCodes('&', sm));
                            Bukkit.broadcast(ChatColor.RED + "[SocialSpy] " + ChatColor.WHITE + sendername + ChatColor.GOLD + " >> " + ChatColor.WHITE + targetname + ChatColor.GRAY + " : " + ChatColor.translateAlternateColorCodes('&', sm), "se.socialspy");
                            return true;
                        }else {
                            if (Reply.reply.containsKey(recipient.getUniqueId())){
                                sender.sendMessage(ChatColor.YELLOW + "me" + ChatColor.GOLD + " >> " + ChatColor.WHITE + targetname + ChatColor.GRAY + " : " + ChatColor.translateAlternateColorCodes('&', sm));
                                recipient.sendMessage(sendername + ChatColor.GOLD + " >> " + ChatColor.YELLOW + "me" + ChatColor.GRAY + " : " + ChatColor.translateAlternateColorCodes('&', sm));
                            }else{
                                Bukkit.broadcast(ChatColor.RED + "[SocialSpy] " + ChatColor.WHITE + sendername + ChatColor.GOLD + " >> " + ChatColor.WHITE + targetname + ChatColor.GRAY + " : " + ChatColor.translateAlternateColorCodes('&', sm), "se.socialspy");
                                recipient.sendMessage(sendername + ChatColor.GOLD + " >> " + ChatColor.YELLOW + "me" + ChatColor.GRAY + " : " + ChatColor.translateAlternateColorCodes('&', sm));
                            }
                        }
                        return true;
                    } else {
                        messager.sendMessage(ChatColor.RED + "That person has messaging disabled!");
                    }
                }
            } else {
                if (ServerEssentials.plugin.getConfig().getString("no-permission-message").length() == 0){
                    messager.sendMessage(ChatColor.RED + "You do not have the required permission (se.message) to run this command.");
                    return true;
                }else{
                    String permission = ServerEssentials.getPlugin().getConfig().getString("no-permission-message");
                    messager.sendMessage(ChatColor.translateAlternateColorCodes('&', permission));
                }
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
                    System.out.println(ChatColor.RED + "That person has messaging disabled!");
                }
            }
        }
        return false;
    }
}
