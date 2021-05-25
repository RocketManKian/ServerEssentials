package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class God implements CommandExecutor, Listener {

    public static ArrayList<String> god_toggle = new ArrayList<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        if (player.hasPermission("se.god")) {
            if (args.length == 0) {
                if (god_toggle.contains(player.getName())) {
                    player.sendMessage(ChatColor.RED + "Godmode Disabled");
                    god_toggle.remove(player.getName());
                    return true;
                } else if (!god_toggle.contains(player.getName())) {
                    player.sendMessage(ChatColor.GREEN + "Godmode Enabled");
                    god_toggle.add(player.getName());
                    return true;
                }
            } else if (args.length == 1) {
                Player target = Bukkit.getPlayer(args[0]);
                if (target == sender) {
                    sender.sendMessage(ChatColor.RED + "Use /god to set yourself into Godmode");
                    return true;
                } else if (target == null) {
                    sender.sendMessage(ChatColor.RED + "Player does not exist");
                    return true;
                }else if (god_toggle.contains(target.getName())){
                    player.sendMessage(ChatColor.RED + "Godmode Disabled for " + ChatColor.WHITE + target.getName());
                    target.sendMessage(ChatColor.RED + "Godmode Disabled");
                    god_toggle.remove(target.getName());
                    return true;
                }else if (!god_toggle.contains(target.getName())){
                    player.sendMessage(ChatColor.GREEN + "Godmode Enabled for " + ChatColor.WHITE + target.getName());
                    target.sendMessage(ChatColor.GREEN + "Godmode Enabled");
                    god_toggle.add(target.getName());
                    return true;
                }
            }
        }else{
            if (ServerEssentials.plugin.getConfig().getString("no-permission-message").length() == 0) {
                player.sendMessage(ChatColor.RED + "You do not have the required permission (se.god) to run this command.");
                return true;
            } else {
                String permission = ServerEssentials.getPlugin().getConfig().getString("no-permission-message");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', permission));
                return true;
            }
        }
        return false;
    }
    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        Entity ent = e.getEntity();
        if (ent instanceof Player) {
            Player p = (Player) ent;
            if (god_toggle.contains(p.getName())) {
                e.setCancelled(true);
            }
        }
    }
}