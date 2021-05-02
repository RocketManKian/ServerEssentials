package me.rocketmankianproductions.serveressentials.commands;

import com.sun.org.apache.bcel.internal.Const;
import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.api.EmbedBuilder;
import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;
import me.rocketmankianproductions.serveressentials.LoggerMessage;
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
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import sun.plugin2.main.server.Plugin;

public class Report implements CommandExecutor {

    public static Plugin plugin;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        if (sender instanceof Player){
            if (player.hasPermission("se.report")){
                if (args.length >= 2){
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target == null){
                        player.sendMessage(ChatColor.RED + "Player doesn't exist");
                        return true;
                    }else if (target == sender){
                        player.sendMessage(ChatColor.RED + "You cannot report yourself");
                        return true;
                    }else {
                        player.sendMessage(ChatColor.GOLD + "Report sent successfully!");
                        StringBuilder builder = new StringBuilder();
                        int startArg = 1;
                        int endArg = args.length;
                        for (int i = startArg; i < endArg; i++) {
                            builder.append(args[i] + (args.length > (i + 1) ? " " : ""));
                        }
                        String messages = builder.toString(); // your message from all args after "startArg - 1"
                        if (ServerEssentials.isConnectedToDiscordSRV == true && ServerEssentials.getPlugin().getConfig().getBoolean("enable-discord-integration") == true) {
                            String channelname = ServerEssentials.getPlugin().getConfig().getString("channel-name");
                            TextChannel textChannel = DiscordSRV.getPlugin().getDestinationTextChannelForGameChannelName(channelname);

                            // null if the channel isn't specified in the config.yml
                            if (textChannel != null) {
                                textChannel.sendMessage(":warning:" + "**" + player.getName() + "** " + "has reported user " + "**" + target.getName() + "**" + " for reason: " + "**" + messages + "**").queue();
                            } else {
                                LoggerMessage.log(LoggerMessage.LogLevel.WARNING, "Channel called " + channelname + " could not be found in the DiscordSRV configuration");
                            }
                            for (Player admin : Bukkit.getOnlinePlayers()) {
                                if (admin.hasPermission("se.reportnotification")) {
                                    admin.sendMessage(ChatColor.AQUA + "--------- " + ChatColor.RED + "NEW REPORT " + ChatColor.AQUA + "---------");
                                    admin.sendMessage(ChatColor.RED + "Reporter" + ChatColor.GRAY + " » " + ChatColor.WHITE + player.getName());
                                    admin.sendMessage(ChatColor.RED + "Reported User" + ChatColor.GRAY + " » " + ChatColor.WHITE + args[0]);
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
                                    admin.sendMessage("Reported User" + ChatColor.GRAY + " » " + ChatColor.WHITE + args[0]);
                                    admin.sendMessage(ChatColor.RED + "Reason" + ChatColor.GRAY + " » " + ChatColor.WHITE + messages);
                                    admin.sendMessage(ChatColor.AQUA + "-----------------------------");
                                }
                            }
                            return true;
                        }
                    }
                }
            }else{
                if (ServerEssentials.plugin.getConfig().getString("no-permission-message").length() == 0) {
                    player.sendMessage(ChatColor.RED + "You do not have the required permission (se.report) to run this command.");
                    return true;
                } else {
                    String permission = ServerEssentials.getPlugin().getConfig().getString("no-permission-message");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', permission));
                }
                return true;
            }
        }
        return false;
    }
}
