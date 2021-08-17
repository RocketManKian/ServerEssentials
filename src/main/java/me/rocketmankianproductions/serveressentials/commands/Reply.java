package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

public class Reply implements CommandExecutor {

    public static HashMap<UUID, UUID> reply = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        Player player = (Player) sender;
        if (player.hasPermission("se.reply") || player.hasPermission("se.all")) {
            if (args.length <= 0) {
                sender.sendMessage(ChatColor.RED + "Usage: /reply <message>");
                return true;
            } else {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < args.length; i++) {
                    sb.append(args[i]).append(" ");
                }
                String sm = sb.toString();

                // check if theres something in the hashmap / something to reply to
                if (reply.get(player.getUniqueId()) == null) {
                    String msg = Lang.fileConfig.getString("reply-no-message");
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    return true;
                } else if (Bukkit.getServer().getOnlinePlayers().contains(Bukkit.getPlayer(reply.get(player.getUniqueId())))) {
                    Reply.reply.put(reply.get(player.getUniqueId()), player.getUniqueId()); // put again to hashmap
                    String name = Bukkit.getPlayer(reply.get(player.getUniqueId())).getName();
                    if (!player.hasPermission("se.socialspy")) {
                        // Loop to check through all Online Players and get all players who are included within the HashMap
                        for (Player admin : Bukkit.getOnlinePlayers()) {
                            if (SocialSpy.socialspy.contains(admin)) {
                                if (admin.getUniqueId().equals(sender)){
                                    sender.sendMessage(ChatColor.YELLOW + "me" + ChatColor.GOLD + " >> " + ChatColor.WHITE + name + ChatColor.GRAY + " : " + ChatColor.translateAlternateColorCodes('&', sm));
                                    Bukkit.getPlayer(reply.get(player.getUniqueId())).sendMessage(player.getName() + ChatColor.GOLD + " >> " + ChatColor.YELLOW + "me" + ChatColor.GRAY + " : " + ChatColor.translateAlternateColorCodes('&', sm));
                                }else{
                                    admin.sendMessage(ChatColor.RED + "[SocialSpy] " + ChatColor.WHITE + player.getName()  + ChatColor.GOLD + " >> " + ChatColor.WHITE + name + ChatColor.GRAY + " : " + ChatColor.translateAlternateColorCodes('&', sm));
                                }
                            }
                        }
                        sender.sendMessage(ChatColor.YELLOW + "me" + ChatColor.GOLD + " >> " + ChatColor.WHITE + name + ChatColor.GRAY + " : " + ChatColor.translateAlternateColorCodes('&', sm));
                        Bukkit.getPlayer(reply.get(player.getUniqueId())).sendMessage(player.getName() + ChatColor.GOLD + " >> " + ChatColor.YELLOW + "me" + ChatColor.GRAY + " : " + ChatColor.translateAlternateColorCodes('&', sm));
                        return true;
                    } else {
                        // Loop to check through all Online Players and get all players who are included within the HashMap
                        for (Player admin : Bukkit.getOnlinePlayers()) {
                            if (SocialSpy.socialspy.contains(admin)) {
                                if (admin.getUniqueId().equals(sender)){
                                    sender.sendMessage(ChatColor.YELLOW + "me" + ChatColor.GOLD + " >> " + ChatColor.WHITE + name + ChatColor.GRAY + " : " + ChatColor.translateAlternateColorCodes('&', sm));
                                    Bukkit.getPlayer(reply.get(player.getUniqueId())).sendMessage(player.getName() + ChatColor.GOLD + " >> " + ChatColor.YELLOW + "me" + ChatColor.GRAY + " : " + ChatColor.translateAlternateColorCodes('&', sm));
                                }else{
                                    admin.sendMessage(ChatColor.RED + "[SocialSpy] " + ChatColor.WHITE + player.getName()  + ChatColor.GOLD + " >> " + ChatColor.WHITE + name + ChatColor.GRAY + " : " + ChatColor.translateAlternateColorCodes('&', sm));
                                }
                            }
                        }
                        sender.sendMessage(ChatColor.YELLOW + "me" + ChatColor.GOLD + " >> " + ChatColor.WHITE + name + ChatColor.GRAY + " : " + ChatColor.translateAlternateColorCodes('&', sm));
                        Bukkit.getPlayer(reply.get(player.getUniqueId())).sendMessage(player.getName() + ChatColor.GOLD + " >> " + ChatColor.YELLOW + "me" + ChatColor.GRAY + " : " + ChatColor.translateAlternateColorCodes('&', sm));
                        return true;
                    }
                } else {
                    String msg = Lang.fileConfig.getString("target-offline");
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    return true;
                }
            }
        } else {
            String perm = Lang.fileConfig.getString("no-permission-message").replace("<permission>", "se.reply");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', perm));
            return true;
        }
    }
}