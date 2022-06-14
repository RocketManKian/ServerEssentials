package me.rocketmankianproductions.serveressentials.commands;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.api.EmbedBuilder;
import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;
import me.rocketmankianproductions.serveressentials.LoggerMessage;
import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class Report implements CommandExecutor {

    public static ServerEssentials plugin;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            if (player.hasPermission("se.report") || player.hasPermission("se.all")){
                if (args.length >= 2) {
                    OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
                    if (target == sender) {
                        String msg = Lang.fileConfig.getString("report-self");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    } else {
                        String msg = Lang.fileConfig.getString("report-successful");
                        String server = ServerEssentials.getPlugin().getConfig().getString("server-name");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        StringBuilder builder = new StringBuilder();
                        int startArg = 1;
                        int endArg = args.length;
                        for (int i = startArg; i < endArg; i++) {
                            builder.append(args[i] + (args.length > (i + 1) ? " " : ""));
                        }
                        String messages = builder.toString(); // your message from all args after "startArg - 1"
                        String msg1 = Lang.fileConfig.getString("report-user-line-one");
                        String msg2 = Lang.fileConfig.getString("report-user-line-two").replace("<player>", player.getName());
                        String msg3 = Lang.fileConfig.getString("report-user-line-three").replace("<target>", target.getName());
                        String msg4 = Lang.fileConfig.getString("report-user-line-four").replace("<message>", messages);
                        String msg5 = Lang.fileConfig.getString("report-user-line-five");
                        if (ServerEssentials.isConnectedToDiscordSRV && ServerEssentials.getPlugin().getConfig().getBoolean("enable-discord-integration") == true) {
                            String channelname = ServerEssentials.getPlugin().getConfig().getString("report-user-channel-name");
                            TextChannel textChannel = DiscordSRV.getPlugin().getDestinationTextChannelForGameChannelName(channelname);
                            EmbedBuilder report = new EmbedBuilder();
                            if (ServerEssentials.plugin.getConfig().getString("group-id").length() != 0) {
                                String prefix = ServerEssentials.getPlugin().getConfig().getString("group-id");
                                String finaltext = ("<@&" + prefix + ">");
                                textChannel.sendMessage(finaltext)
                                        .queue();
                                report.setTitle("New Report")
                                        .setColor(Color.RED)
                                        .addField("Reporter » ", player.getName(), true)
                                        .addField("Reported User » ", target.getName(), true)
                                        .addField("Reason » ", messages, false);
                                if (!server.isEmpty()){
                                    report.addField("Server » ", server, true);
                                }
                                // null if the channel isn't specified in the config.yml
                                if (textChannel != null) {
                                    textChannel.sendMessageEmbeds(report.build()).queue();
                                } else {
                                    LoggerMessage.log(LoggerMessage.LogLevel.WARNING, "Channel called " + channelname + " could not be found in the DiscordSRV configuration");
                                }
                                if (!target.isOnline()){
                                    for (Player admin : Bukkit.getOnlinePlayers()) {
                                        if (admin.hasPermission("se.reportnotification")) {
                                            admin.sendMessage(ChatColor.translateAlternateColorCodes('&', msg1));
                                            admin.sendMessage(ChatColor.translateAlternateColorCodes('&', msg2));
                                            admin.sendMessage(ChatColor.translateAlternateColorCodes('&', msg3));
                                            admin.sendMessage(ChatColor.translateAlternateColorCodes('&', msg4));
                                            admin.sendMessage(ChatColor.translateAlternateColorCodes('&', msg5));
                                        }
                                    }
                                }else{
                                    for (Player admin : Bukkit.getOnlinePlayers()) {
                                        if (admin.hasPermission("se.reportnotification")) {
                                            admin.sendMessage(ChatColor.translateAlternateColorCodes('&', msg1));
                                            admin.sendMessage(ChatColor.translateAlternateColorCodes('&', msg2));
                                            admin.sendMessage(ChatColor.translateAlternateColorCodes('&', msg3));
                                            admin.sendMessage(ChatColor.translateAlternateColorCodes('&', msg4));
                                            admin.sendMessage(ChatColor.translateAlternateColorCodes('&', msg5));
                                        }
                                    }
                                }
                                return true;
                            }else{
                                report.setTitle("New Report")
                                        .setColor(Color.RED)
                                        .addField("Reporter » ", player.getName(), true)
                                        .addField("Reported User » ", target.getName(), true)
                                        .addField("Reason » ", messages, false);
                                if (!server.isEmpty()){
                                    report.addField("Server » ", server, true);
                                }
                                // null if the channel isn't specified in the config.yml
                                if (textChannel != null) {
                                    textChannel.sendMessageEmbeds(report.build()).queue();
                                } else {
                                    LoggerMessage.log(LoggerMessage.LogLevel.WARNING, "Channel called " + channelname + " could not be found in the DiscordSRV configuration");
                                }
                                if (!target.isOnline()){
                                    for (Player admin : Bukkit.getOnlinePlayers()) {
                                        if (admin.hasPermission("se.reportnotification")) {
                                            admin.sendMessage(ChatColor.translateAlternateColorCodes('&', msg1));
                                            admin.sendMessage(ChatColor.translateAlternateColorCodes('&', msg2));
                                            admin.sendMessage(ChatColor.translateAlternateColorCodes('&', msg3));
                                            admin.sendMessage(ChatColor.translateAlternateColorCodes('&', msg4));
                                            admin.sendMessage(ChatColor.translateAlternateColorCodes('&', msg5));
                                        }
                                    }
                                }else{
                                    for (Player admin : Bukkit.getOnlinePlayers()) {
                                        if (admin.hasPermission("se.reportnotification")) {
                                            admin.sendMessage(ChatColor.translateAlternateColorCodes('&', msg1));
                                            admin.sendMessage(ChatColor.translateAlternateColorCodes('&', msg2));
                                            admin.sendMessage(ChatColor.translateAlternateColorCodes('&', msg3));
                                            admin.sendMessage(ChatColor.translateAlternateColorCodes('&', msg4));
                                            admin.sendMessage(ChatColor.translateAlternateColorCodes('&', msg5));
                                        }
                                    }
                                }
                                return true;
                            }
                        } else {
                            if (!target.isOnline()){
                                for (Player admin : Bukkit.getOnlinePlayers()) {
                                    if (admin.hasPermission("se.reportnotification")) {
                                        admin.sendMessage(ChatColor.translateAlternateColorCodes('&', msg1));
                                        admin.sendMessage(ChatColor.translateAlternateColorCodes('&', msg2));
                                        admin.sendMessage(ChatColor.translateAlternateColorCodes('&', msg3));
                                        admin.sendMessage(ChatColor.translateAlternateColorCodes('&', msg4));
                                        admin.sendMessage(ChatColor.translateAlternateColorCodes('&', msg5));
                                    }
                                }
                            }else{
                                for (Player admin : Bukkit.getOnlinePlayers()) {
                                    if (admin.hasPermission("se.reportnotification")) {
                                        admin.sendMessage(ChatColor.translateAlternateColorCodes('&', msg1));
                                        admin.sendMessage(ChatColor.translateAlternateColorCodes('&', msg2));
                                        admin.sendMessage(ChatColor.translateAlternateColorCodes('&', msg3));
                                        admin.sendMessage(ChatColor.translateAlternateColorCodes('&', msg4));
                                        admin.sendMessage(ChatColor.translateAlternateColorCodes('&', msg5));
                                    }
                                }
                            }
                            return true;
                        }
                    }
                }else{
                    String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/report (player) <reason>");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    return true;
                }
            }else{
                String perm = Lang.fileConfig.getString("no-permission-message").replace("<permission>", "se.report");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', perm));
                return true;
            }
        }else{
            String console = Lang.fileConfig.getString("console-invalid");
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', console));
            return true;
        }
    }
}