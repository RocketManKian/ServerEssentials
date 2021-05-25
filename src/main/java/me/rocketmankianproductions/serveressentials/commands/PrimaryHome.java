package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class PrimaryHome implements CommandExecutor {

    public static HashMap<UUID, String> home = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        Player player = (Player) sender;

        if (sender instanceof Player){
            if (player.hasPermission("se.sethome")){
                if (args.length == 1) {
                    String name = player.getUniqueId().toString();
                    if (Sethome.fileConfig.getString("Home." + name + "." + args[0]) != null) {
                        home.remove(player.getUniqueId());
                        home.put(player.getUniqueId(), args[0]);
                        System.out.println(home);
                        try {
                            Sethome.fileConfig.save(Sethome.file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Sethome.reload();
                        player.sendMessage(ChatColor.GOLD + args[0] + ChatColor.GREEN + " set as Primary Home");
                        return true;
                    }
                }
            }else{
                if (ServerEssentials.plugin.getConfig().getString("no-permission-message").length() == 0) {
                    player.sendMessage(ChatColor.RED + "You do not have the required permission (se.sethome) to run this command.");
                    return true;
                } else {
                    String permission = ServerEssentials.getPlugin().getConfig().getString("no-permission-message");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', permission));
                    return true;
                }
            }
        }else{
            sender.sendMessage(ChatColor.RED + "You are not a player");
        }
        return false;
    }
}
