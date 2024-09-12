package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
import me.rocketmankianproductions.serveressentials.file.UserFile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

import static me.rocketmankianproductions.serveressentials.ServerEssentials.hex;

public class Reply implements CommandExecutor {

    public static HashMap<UUID, UUID> reply = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            boolean hasPerm = ServerEssentials.permissionChecker(player, "se.reply");
            if (hasPerm) {
                if (args.length <= 0) {
                    String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/reply <message>");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                    return true;
                } else {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < args.length; i++) {
                        sb.append(args[i]).append(" ");
                    }
                    String sm = sb.toString();
                    // check if there's something in the hashmap / something to reply to
                    if (reply.get(player.getUniqueId()) == null) {
                        String msg = Lang.fileConfig.getString("reply-no-message");
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                        return true;
                    } else if (Bukkit.getServer().getOnlinePlayers().contains(Bukkit.getPlayer(reply.get(player.getUniqueId())))) {
                        Reply.reply.put(reply.get(player.getUniqueId()), player.getUniqueId()); // put again to hashmap
                        String name = Bukkit.getPlayer(reply.get(player.getUniqueId())).getName();
                        String msgsender = Lang.fileConfig.getString("reply-sender").replace("<target>", name).replace("<message>", sm);
                        String msgrecipient = Lang.fileConfig.getString("reply-recipient").replace("<sender>", player.getName()).replace("<message>", sm);
                        String msgsocialspy = Lang.fileConfig.getString("socialspy-message").replace("<sender>", player.getName()).replace("<target>", name).replace("<message>", sm);
                        socialSpy(player, Bukkit.getPlayer(reply.get(player.getUniqueId())), msgsocialspy, msgsender, msgrecipient);
                        return true;
                    } else {
                        String msg = Lang.fileConfig.getString("target-offline");
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                        return true;
                    }
                }
            }
        }else{
            String console = Lang.fileConfig.getString("console-invalid");
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', hex(console)));
            return true;
        }
        return false;
    }

    public void socialSpy(Player messager, Player recipient, String msgsocialspy, String msgsender, String msgrecipient){
        for (Player admin : Bukkit.getOnlinePlayers()) {
            if (UserFile.fileConfig.getBoolean(admin.getUniqueId() + ".spy")) {
                if (admin != messager && admin != recipient) {
                    admin.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msgsocialspy)));
                }
            }
        }
        messager.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msgsender)));
        recipient.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msgrecipient)));
    }
}
