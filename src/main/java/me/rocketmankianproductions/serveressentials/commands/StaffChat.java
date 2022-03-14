package me.rocketmankianproductions.serveressentials.commands;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;
import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class StaffChat implements CommandExecutor {

    public static ArrayList<Player> staffchat = new ArrayList<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            if (player.hasPermission("se.staffchat") || player.hasPermission("se.all")){
                if (args.length == 0) {
                    if (!staffchat.contains(player)){
                        String msg = Lang.fileConfig.getString("staffchat-enabled");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        staffchat.add(player);
                        return true;
                    }else{
                        String msg = Lang.fileConfig.getString("staffchat-disabled");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        staffchat.remove(player);
                        return true;
                    }
                }else if (args.length >= 1){
                    String myArgs = String.join(" ", args);
                    String playername = ChatColor.stripColor(player.getDisplayName());
                    String channel = ServerEssentials.getPlugin().getConfig().getString("staff-chat-channel-name");
                    String msg = Lang.fileConfig.getString("staffchat-message").replace("<player>", player.getName()).replace("<message>", ChatColor.GRAY + myArgs);
                    if (ServerEssentials.isConnectedToDiscordSRV && ServerEssentials.plugin.getConfig().getBoolean("enable-discord-integration")){
                        TextChannel textChannel = DiscordSRV.getPlugin().getDestinationTextChannelForGameChannelName(channel);
                        if (textChannel != null){
                            textChannel.sendMessage("**" + playername + "** » " + myArgs).queue();
                            Bukkit.broadcast(ChatColor.translateAlternateColorCodes('&', msg), "se.staffchat");
                            return true;
                        }else{
                            Bukkit.broadcast(ChatColor.translateAlternateColorCodes('&', msg), "se.staffchat");
                            return true;
                        }
                    }else{
                        Bukkit.broadcast(ChatColor.translateAlternateColorCodes('&', msg), "se.staffchat");
                        return true;
                    }
                }
            }else{
                String perm = Lang.fileConfig.getString("no-permission-message").replace("<permission>", "se.staffchat");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', perm));
                return true;
            }
        }else if (sender instanceof ConsoleCommandSender){
            if (args.length >= 1){
                String myArgs = String.join(" ", args);
                String channel = ServerEssentials.getPlugin().getConfig().getString("staff-chat-channel-name");
                TextChannel textChannel = DiscordSRV.getPlugin().getDestinationTextChannelForGameChannelName(channel);
                if (textChannel != null && ServerEssentials.isConnectedToDiscordSRV && ServerEssentials.plugin.getConfig().getBoolean("enable-discord-integration")){
                    textChannel.sendMessage("**" + "Console" + "** » " + myArgs).queue();
                    Bukkit.broadcast(ChatColor.translateAlternateColorCodes('&', "&d(&5&lStaff&d) ") + ChatColor.LIGHT_PURPLE + "Console" + ": " + ChatColor.GRAY + myArgs, "se.staffchat");
                    return true;
                }else{
                    Bukkit.broadcast(ChatColor.translateAlternateColorCodes('&', "&d(&5&lStaff&d) ") + ChatColor.LIGHT_PURPLE + "Console" + ": " + ChatColor.GRAY + myArgs, "se.staffchat");
                    return true;
                }
            }
        }
        return false;
    }
}