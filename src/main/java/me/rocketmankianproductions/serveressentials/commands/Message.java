package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static me.rocketmankianproductions.serveressentials.ServerEssentials.hex;

public class Message implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player){
            Player messager = (Player) sender;
            // Check if the Sender has the se.message permission
            if (ServerEssentials.permissionChecker(messager, "se.message")) {
                String sm = "";
                if (args.length >= 2) {
                    //recipient == target
                    Player recipient = Bukkit.getPlayer(args[0]);
                    if (recipient == sender) {
                        String msg = Lang.fileConfig.getString("message-self");
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                        return true;
                    } else if (recipient == null) {
                        String msg = Lang.fileConfig.getString("target-offline");
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
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
                        String msgsender = Lang.fileConfig.getString("message-sender").replace("<target>", targetname).replace("<message>", sm);
                        String msgrecipient = Lang.fileConfig.getString("message-recipient").replace("<sender>", sendername).replace("<message>", sm);
                        String msgsocialspy = Lang.fileConfig.getString("socialspy-message").replace("<sender>", sendername).replace("<target>", targetname).replace("<message>", sm);
                        socialSpy(messager, recipient, msgsocialspy, msgsender, msgrecipient);
                        return true;
                    } else {
                        String msg = Lang.fileConfig.getString("message-disabled");
                        messager.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                        return true;
                    }
                }else{
                    String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/msg (player) <message>");
                    messager.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                    return true;
                }
            }
        }else if (sender instanceof ConsoleCommandSender || sender instanceof BlockCommandSender){
            String sm = "";
            if (args.length <= 1) {
                Bukkit.getLogger().info(ChatColor.RED + "Usage: msg <player> <message>");
                return true;
            } else if (args.length >= 2) {
                Player recipient = Bukkit.getPlayer(args[0]);
                if (recipient == null) {
                    Bukkit.getLogger().info(ChatColor.RED + "Player does not exist");
                    return true;
                } else if (MsgToggle.fileConfig.getBoolean("msgtoggle." + recipient.getName(), false) == false) {
                    String targetname = recipient.getName();
                    for (int i = 1; i < args.length; i++) {
                        String arg = (args[i] + " ");
                        sm = (sm + arg);
                    }
                    String msgsender = Lang.fileConfig.getString("message-sender").replace("<target>", targetname).replace("<message>", sm);
                    String msgrecipientconsole = Lang.fileConfig.getString("message-recipient-console").replace("<message>", sm);
                    Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', hex(msgsender)));
                    recipient.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msgrecipientconsole)));
                    return true;
                } else {
                    String msg = Lang.fileConfig.getString("message-disabled");
                    Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                }
            }
        }
        return false;
    }

    public void socialSpy(Player messager, Player recipient, String msgsocialspy, String msgsender, String msgrecipient){
        for (Player admin : Bukkit.getOnlinePlayers()) {
            if (SocialSpy.fileConfig.getBoolean("Spy." + admin.getName())) {
                if (admin != messager && admin != recipient) {
                    admin.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msgsocialspy)));
                }
            }
        }
        messager.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msgsender)));
        recipient.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msgrecipient)));
    }
}