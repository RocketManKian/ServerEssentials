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
import sun.plugin2.main.server.Plugin;

import java.awt.*;

public class Report implements CommandExecutor {

    public static Plugin plugin;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        if (sender instanceof Player){
            if (player.hasPermission("se.report") || player.hasPermission("se.all")){
                if (args.length >= 2) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target == null) {
                        String msg = Lang.fileConfig.getString("target-offline");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    } else if (target == sender) {
                        String msg = Lang.fileConfig.getString("report-self");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    } else {
                        String msg = Lang.fileConfig.getString("report-successful");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        StringBuilder builder = new StringBuilder();
                        int startArg = 1;
                        int endArg = args.length;
                        for (int i = startArg; i < endArg; i++) {
                            builder.append(args[i] + (args.length > (i + 1) ? " " : ""));
                        }
                        String messages = builder.toString(); // your message from all args after "startArg - 1"
                        if (ServerEssentials.isConnectedToDiscordSRV == true && ServerEssentials.getPlugin().getConfig().getBoolean("enable-discord-integration") == true) {
                            String channelname = ServerEssentials.getPlugin().getConfig().getString("report-user-channel-name");
                            TextChannel textChannel = DiscordSRV.getPlugin().getDestinationTextChannelForGameChannelName(channelname);
                            EmbedBuilder report = new EmbedBuilder();
                            report.setTitle("New Report")
                                    .setColor(Color.RED)
                                    .addField("Reporter » ", player.getName(), true)
                                    .addField("Reported User » ", target.getName(), true)
                                    .addField("Reason » ", messages, false);

                            // null if the channel isn't specified in the config.yml
                            if (textChannel != null) {
                                textChannel.sendMessage(report.build()).queue();
                            } else {
                                LoggerMessage.log(LoggerMessage.LogLevel.WARNING, "Channel called " + channelname + " could not be found in the DiscordSRV configuration");
                            }
                            for (Player admin : Bukkit.getOnlinePlayers()) {
                                if (admin.hasPermission("se.reportnotification")) {
                                    admin.sendMessage(ChatColor.AQUA + "--------- " + ChatColor.RED + "NEW REPORT " + ChatColor.AQUA + "---------");
                                    admin.sendMessage(ChatColor.RED + "Reporter" + ChatColor.GRAY + " » " + ChatColor.WHITE + player.getName());
                                    admin.sendMessage(ChatColor.RED + "Reported User" + ChatColor.GRAY + " » " + ChatColor.WHITE + target.getName());
                                    admin.sendMessage(ChatColor.RED + "Reason" + ChatColor.GRAY + " » " + ChatColor.WHITE + messages);
                                    admin.sendMessage(ChatColor.AQUA + "-----------------------------");
                                }
                            }
                            return true;
                        } else {
                            for (Player admin : Bukkit.getOnlinePlayers()) {
                                if (admin.hasPermission("se.reportnotification")) {
                                    admin.sendMessage(ChatColor.AQUA + "--------- " + ChatColor.RED + "NEW REPORT " + ChatColor.AQUA + "---------");
                                    admin.sendMessage(ChatColor.RED + "Reporter" + ChatColor.GRAY + " » " + ChatColor.WHITE + player.getName());
                                    admin.sendMessage(ChatColor.RED + "Reported User" + ChatColor.GRAY + " » " + ChatColor.WHITE + target.getName());
                                    admin.sendMessage(ChatColor.RED + "Reason" + ChatColor.GRAY + " » " + ChatColor.WHITE + messages);
                                    admin.sendMessage(ChatColor.AQUA + "-----------------------------");
                                }
                            }
                            return true;
                        }
                    }
                }
            }else{
                String perm = Lang.fileConfig.getString("no-permission-message").replace("<permission>", "se.report");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', perm));
                return true;
            }
        }
        return false;
    }
}