package me.rocketmankianproductions.serveressentials.commands;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.api.EmbedBuilder;
import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;
import me.rocketmankianproductions.serveressentials.LoggerMessage;
import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class ReportBug implements CommandExecutor {

    public static ServerEssentials plugin;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            boolean hasPerm = ServerEssentials.permissionChecker(player, "se.reportbug");
            if (hasPerm) {
                if (args.length >= 1) {
                    String msg = Lang.fileConfig.getString("report-successful");
                    String server = ServerEssentials.getPlugin().getConfig().getString("server-name");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    StringBuilder builder = new StringBuilder();
                    int startArg = 0;
                    int endArg = args.length;
                    for (int i = startArg; i < endArg; i++) {
                        builder.append(args[i] + (args.length > (i + 1) ? " " : ""));
                    }
                    String messages = builder.toString(); // your message from all args after "startArg - 1"
                    String msg1 = Lang.fileConfig.getString("report-bug-line-one");
                    String msg2 = Lang.fileConfig.getString("report-bug-line-two").replace("<player>", player.getName());
                    String msg3 = Lang.fileConfig.getString("report-bug-line-three").replace("<message>", messages);
                    String msg4 = Lang.fileConfig.getString("report-bug-line-four");
                    if (ServerEssentials.isConnectedToDiscordSRV && ServerEssentials.getPlugin().getConfig().getBoolean("enable-discord-integration") == true) {
                        String channelname = ServerEssentials.getPlugin().getConfig().getString("report-bug-channel-name");
                        TextChannel textChannel = DiscordSRV.getPlugin().getDestinationTextChannelForGameChannelName(channelname);
                        EmbedBuilder report = new EmbedBuilder();
                        if (ServerEssentials.plugin.getConfig().getString("group-id").length() != 0) {
                            String prefix = ServerEssentials.getPlugin().getConfig().getString("group-id");
                            String finaltext = ("<@&" + prefix + ">");
                            textChannel.sendMessage(finaltext)
                                    .queue();
                            report.setTitle("Bug Report")
                                    .setColor(Color.RED)
                                    .addField("Reporter » ", player.getName(), true)
                                    .addField("Bug » ", messages, false);
                            if (!server.isEmpty()){
                                report.addField("Server » ", server, false);
                            }
                            // null if the channel isn't specified in the config.yml
                            if (textChannel != null) {
                                textChannel.sendMessageEmbeds(report.build()).queue();
                            } else {
                                LoggerMessage.log(LoggerMessage.LogLevel.WARNING, "Channel called " + channelname + " could not be found in the DiscordSRV configuration");
                            }
                            for (Player admin : Bukkit.getOnlinePlayers()) {
                                if (admin.hasPermission("se.reportnotification")) {
                                    admin.sendMessage(ChatColor.translateAlternateColorCodes('&', msg1));
                                    admin.sendMessage(ChatColor.translateAlternateColorCodes('&', msg2));
                                    admin.sendMessage(ChatColor.translateAlternateColorCodes('&', msg3));
                                    admin.sendMessage(ChatColor.translateAlternateColorCodes('&', msg4));
                                }
                            }
                            return true;
                        } else {
                            report.setTitle("Bug Report")
                                    .setColor(Color.RED)
                                    .addField("Reporter » ", player.getName(), true)
                                    .addField("Bug » ", messages, false);
                            if (!server.isEmpty()){
                                report.addField("Server » ", server, false);
                            }
                            // null if the channel isn't specified in the config.yml
                            if (textChannel != null) {
                                textChannel.sendMessageEmbeds(report.build()).queue();
                            } else {
                                LoggerMessage.log(LoggerMessage.LogLevel.WARNING, "Channel called " + channelname + " could not be found in the DiscordSRV configuration");
                            }
                            for (Player admin : Bukkit.getOnlinePlayers()) {
                                if (admin.hasPermission("se.reportnotification")) {
                                    if (admin.hasPermission("se.reportnotification")) {
                                        admin.sendMessage(ChatColor.translateAlternateColorCodes('&', msg1));
                                        admin.sendMessage(ChatColor.translateAlternateColorCodes('&', msg2));
                                        admin.sendMessage(ChatColor.translateAlternateColorCodes('&', msg3));
                                        admin.sendMessage(ChatColor.translateAlternateColorCodes('&', msg4));
                                    }
                                }
                            }
                            return true;
                        }
                    } else {
                        for (Player admin : Bukkit.getOnlinePlayers()) {
                            if (admin.hasPermission("se.reportnotification")) {
                                if (admin.hasPermission("se.reportnotification")) {
                                    admin.sendMessage(ChatColor.translateAlternateColorCodes('&', msg1));
                                    admin.sendMessage(ChatColor.translateAlternateColorCodes('&', msg2));
                                    admin.sendMessage(ChatColor.translateAlternateColorCodes('&', msg3));
                                    admin.sendMessage(ChatColor.translateAlternateColorCodes('&', msg4));
                                }
                            }
                        }
                        return true;
                    }
                }else{
                    String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/reportbug <bug>");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    return true;
                }
            }
        }else{
            String console = Lang.fileConfig.getString("console-invalid");
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', console));
            return true;
        }
        return false;
    }
}