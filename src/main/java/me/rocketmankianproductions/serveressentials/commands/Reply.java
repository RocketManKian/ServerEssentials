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
                String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/reply <message>");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
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
                    String msgsender = Lang.fileConfig.getString("reply-sender").replace("<target>", name).replace("<message>", sm);
                    String msgrecipient = Lang.fileConfig.getString("reply-recipient").replace("<sender>", player.getName()).replace("<message>", sm);
                    String msgsocialspy = Lang.fileConfig.getString("socialspy-message").replace("<sender>", player.getName()).replace("<target>", name).replace("<message>", sm);
                    if (!player.hasPermission("se.socialspy")) {
                        // Loop to check through all Online Players and get all players who are included within the HashMap
                        for (Player admin : Bukkit.getOnlinePlayers()) {
                            if (SocialSpy.socialspy.contains(admin)) {
                                if (admin.getUniqueId().equals(sender)){
                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msgsender));
                                    Bukkit.getPlayer(reply.get(player.getUniqueId())).sendMessage(ChatColor.translateAlternateColorCodes('&', msgrecipient));
                                }else{
                                    admin.sendMessage(ChatColor.translateAlternateColorCodes('&', msgsocialspy));
                                }
                            }
                        }
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msgsender));
                        Bukkit.getPlayer(reply.get(player.getUniqueId())).sendMessage(ChatColor.translateAlternateColorCodes('&', msgrecipient));
                        return true;
                    } else {
                        // Loop to check through all Online Players and get all players who are included within the HashMap
                        for (Player admin : Bukkit.getOnlinePlayers()) {
                            if (SocialSpy.socialspy.contains(admin)) {
                                if (admin.getUniqueId().equals(sender)){
                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msgsender));
                                    Bukkit.getPlayer(reply.get(player.getUniqueId())).sendMessage(ChatColor.translateAlternateColorCodes('&', msgrecipient));
                                }else{
                                    admin.sendMessage(ChatColor.translateAlternateColorCodes('&', msgsocialspy));
                                }
                            }
                        }
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msgsender));
                        Bukkit.getPlayer(reply.get(player.getUniqueId())).sendMessage(ChatColor.translateAlternateColorCodes('&', msgrecipient));
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