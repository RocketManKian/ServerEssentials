package me.rocketmankianproductions.serveressentials.commands;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;
import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class StaffChatBungee implements CommandExecutor, PluginMessageListener {

    public static ArrayList<Player> staffchat = new ArrayList<>();
    public static String msg = null;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("se.staffchat") || player.hasPermission("se.all")) {
                if (args.length == 0) {
                    if (!staffchat.contains(player)) {
                        String msg = Lang.fileConfig.getString("staffchat-enabled");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        staffchat.add(player);
                        return true;
                    } else {
                        String msg = Lang.fileConfig.getString("staffchat-disabled");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        staffchat.remove(player);
                        return true;
                    }
                } else if (args.length >= 1) {
                    String myArgs = String.join(" ", args);
                    String playername = ChatColor.stripColor(player.getDisplayName());
                    String channel = ServerEssentials.getPlugin().getConfig().getString("staff-chat-channel-name");
                    TextChannel textChannel = DiscordSRV.getPlugin().getDestinationTextChannelForGameChannelName(channel);
                    if (ServerEssentials.isBungeeCordEnabled) {
                        if (textChannel != null && ServerEssentials.isConnectedToDiscordSRV && ServerEssentials.plugin.getConfig().getBoolean("enable-discord-integration")) {
                            textChannel.sendMessage("**" + playername + "** » " + myArgs).queue();
                            msg = myArgs;
                            getServer();
                            return true;
                        } else {
                            getServer();
                            return true;
                        }
                    } else {
                        if (textChannel != null && ServerEssentials.isConnectedToDiscordSRV && ServerEssentials.plugin.getConfig().getBoolean("enable-discord-integration")) {
                            textChannel.sendMessage("**" + playername + "** » " + myArgs).queue();
                            Bukkit.broadcast(ChatColor.translateAlternateColorCodes('&', "&d(&5&lStaff&d) ") + ChatColor.LIGHT_PURPLE + player.getName() + ": " + ChatColor.GRAY + myArgs, "se.staffchat");
                            return true;
                        } else {
                            Bukkit.broadcast(ChatColor.translateAlternateColorCodes('&', "&d(&5&lStaff&d) ") + ChatColor.LIGHT_PURPLE + player.getName() + ": " + ChatColor.GRAY + myArgs, "se.staffchat");
                            return true;
                        }
                    }
                }
            } else {
                String perm = Lang.fileConfig.getString("no-permission-message").replace("<permission>", "se.staffchat");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', perm));
                return true;
            }
        } else if (sender instanceof ConsoleCommandSender) {
            if (args.length >= 1) {
                String myArgs = String.join(" ", args);
                String channel = ServerEssentials.getPlugin().getConfig().getString("staff-chat-channel-name");
                TextChannel textChannel = DiscordSRV.getPlugin().getDestinationTextChannelForGameChannelName(channel);
                if (textChannel != null && ServerEssentials.isConnectedToDiscordSRV && ServerEssentials.plugin.getConfig().getBoolean("enable-discord-integration")) {
                    textChannel.sendMessage("**" + "Console" + "** » " + myArgs).queue();
                    Bukkit.broadcast(ChatColor.translateAlternateColorCodes('&', "&d(&5&lStaff&d) ") + ChatColor.LIGHT_PURPLE + "Console" + ": " + ChatColor.GRAY + myArgs, "se.staffchat");
                    return true;
                } else {
                    Bukkit.broadcast(ChatColor.translateAlternateColorCodes('&', "&d(&5&lStaff&d) ") + ChatColor.LIGHT_PURPLE + "Console" + ": " + ChatColor.GRAY + myArgs, "se.staffchat");
                    return true;
                }
            }
        }
        return false;
    }

    public static void getServer() {
        if (ServerEssentials.isBungeeCordEnabled) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(out);
            try {
                dataOutputStream.writeUTF("PlayerList");
                dataOutputStream.writeUTF("ALL");
                //dataOutputStream.writeUTF("GetServer");
                //dataOutputStream.writeUTF("Message");
            } catch (IOException e) {
                e.printStackTrace();
            }
            Bukkit.getServer().sendPluginMessage(ServerEssentials.plugin, "BungeeCord", out.toByteArray());
        }
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        String permission = "se.staffchat";
        if (!channel.equals("BungeeCord")) {
            return;
        }
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
        try {
            String subchannel = in.readUTF();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(out);
            String servername = null;
            if (subchannel.equals("PlayerList")) {
                String[] playerList = in.readUTF().split(", ");
                for (String playername : playerList) {
                    OfflinePlayer player1 = Bukkit.getOfflinePlayer(playername);
                    User player2 = giveMeADamnUser(player1.getUniqueId());
                    Bukkit.getLogger().info(String.valueOf(player1.getName()));
                    if (hasPermission(player2, permission)) {

                    }
                }
            }
            if (subchannel.equals("GetServer")) {
                servername = in.readUTF();
                servername = servername.toUpperCase();
            }
            if (subchannel.equals("Message")) {
                dataOutputStream.writeUTF("ALL");
                String lol = in.readUTF();
                Bukkit.broadcastMessage(lol);
                dataOutputStream.writeUTF(ChatColor.translateAlternateColorCodes('&', "&c(&4&l" + servername + "&c)&d - [&5&lStaff&d] " + player.getName() + ": " + ChatColor.GRAY + msg));
                Bukkit.getServer().sendPluginMessage(ServerEssentials.plugin, "BungeeCord", out.toByteArray());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public User giveMeADamnUser(UUID uniqueId) {
        UserManager userManager = LuckPermsProvider.get().getUserManager();
        CompletableFuture<User> userFuture = userManager.loadUser(uniqueId);

        return userFuture.join(); // ouch! (block until the User is loaded)
    }

    public boolean hasPermission(User user, String permission) {
        return user.getCachedData().getPermissionData().checkPermission(permission).asBoolean();
    }
}