package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static me.rocketmankianproductions.serveressentials.ServerEssentials.hex;

public class TeleportHere implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            if (ServerEssentials.permissionChecker(player, "se.teleport")) {
                if (args.length == 1) {
                    Player target = Bukkit.getPlayer(args[0]);
                    String sender2 = sender.getName();
                    if (target == sender) {
                        String msg = Lang.fileConfig.getString("teleport-self");
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                    } else if (target != sender) {
                        try {
                            String target2 = target.getName();
                            if (sender.hasPermission("se.silenttp")) {
                                String msg = Lang.fileConfig.getString("teleport-target-success").replace("<sender>", target2);
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                            } else if (!sender.hasPermission("se.silenttp")) {
                                String msg = Lang.fileConfig.getString("teleport-force-target").replace("<target>", sender2);
                                target.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                                String msg2 = Lang.fileConfig.getString("teleport-target-success").replace("<sender>", target2);
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg2)));
                            }
                            target.teleport(player.getLocation());
                        } catch (NullPointerException e) {
                            String msg = Lang.fileConfig.getString("target-offline");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                        }
                        return true;
                    }
                }else{
                    String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/tphere (player)");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                    return true;
                }
            }
        }else{
            String console = Lang.fileConfig.getString("console-invalid");
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', hex(console)));
            return true;
        }
        return false;
    }
}
