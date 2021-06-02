package me.rocketmankianproductions.serveressentials.commands;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;
import me.rocketmankianproductions.serveressentials.ServerEssentials;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class StaffChat implements CommandExecutor {

    public static ArrayList<Player> staffchat = new ArrayList<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            if (player.hasPermission("se.staffchat")){
                if (args.length == 0) {
                    if (!staffchat.contains(player)){
                        player.sendMessage(ChatColor.GOLD + "StaffChat has been" + ChatColor.GREEN + " enabled!");
                        staffchat.add(player);
                        return true;
                    }else{
                        player.sendMessage(ChatColor.GOLD + "StaffChat has been" + ChatColor.RED + " disabled!");
                        staffchat.remove(player);
                        return true;
                    }
                }else if (args.length >= 1){
                    String myArgs = String.join(" ", args);
                    String playername = ChatColor.stripColor(player.getDisplayName());
                    TextChannel textChannel = DiscordSRV.getPlugin().getDestinationTextChannelForGameChannelName("staff-chat");
                    if (textChannel != null && ServerEssentials.isConnectedToDiscordSRV && ServerEssentials.plugin.getConfig().getBoolean("enable-discord-integration")){
                        textChannel.sendMessage("**" + playername + "** » " + myArgs).queue();
                        Bukkit.broadcast(ChatColor.translateAlternateColorCodes('&', "&d(&5&lStaff&d) ") + ChatColor.LIGHT_PURPLE + playername + ": " + ChatColor.GRAY + myArgs, "se.staffchat");
                        return true;
                    }else{
                        Bukkit.broadcast(ChatColor.translateAlternateColorCodes('&', "&d(&5&lStaff&d) ") + ChatColor.LIGHT_PURPLE + playername + ": " + ChatColor.GRAY + myArgs, "se.staffchat");
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
