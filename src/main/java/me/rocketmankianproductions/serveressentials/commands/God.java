package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static me.rocketmankianproductions.serveressentials.ServerEssentials.hex;

public class God implements CommandExecutor, Listener {

    public static ArrayList<String> god_toggle = new ArrayList<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (ServerEssentials.permissionChecker(player, "se.god")) {
                if (args.length == 0) {
                    if (god_toggle.contains(player.getName())) {
                        String msg = Lang.fileConfig.getString("god-disabled");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                        god_toggle.remove(player.getName());
                        return true;
                    } else if (!god_toggle.contains(player.getName())) {
                        String msg = Lang.fileConfig.getString("god-enabled");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                        god_toggle.add(player.getName());
                        return true;
                    }
                } else if (args.length == 1) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target == sender) {
                        String msg = Lang.fileConfig.getString("god-target-is-sender");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                        return true;
                    } else if (target == null) {
                        String msg = Lang.fileConfig.getString("player-offline");
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                        return true;
                    } else if (god_toggle.contains(target.getName())) {
                        String msg = Lang.fileConfig.getString("god-disabled-target").replace("<target>", target.getName());
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        String msg2 = Lang.fileConfig.getString("god-disabled");
                        target.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg2)));
                        god_toggle.remove(target.getName());
                        return true;
                    } else if (!god_toggle.contains(target.getName())) {
                        String msg = Lang.fileConfig.getString("god-enabled-target").replace("<target>", target.getName());
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        String msg2 = Lang.fileConfig.getString("god-enabled");
                        target.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg2)));
                        god_toggle.add(target.getName());
                        return true;
                    }
                }else{
                    String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/god (player)");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                    return true;
                }
            }
        } else if (sender instanceof ConsoleCommandSender) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                String msg = Lang.fileConfig.getString("player-offline");
                Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                return true;
            } else if (god_toggle.contains(target.getName())) {
                String msg = Lang.fileConfig.getString("god-disabled-target").replace("<target>", target.getName());
                Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', msg));
                String msg2 = Lang.fileConfig.getString("god-disabled");
                target.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg2)));
                god_toggle.remove(target.getName());
                return true;
            } else if (!god_toggle.contains(target.getName())) {
                String msg = Lang.fileConfig.getString("god-enabled-target").replace("<target>", target.getName());
                Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', msg));
                String msg2 = Lang.fileConfig.getString("god-enabled");
                target.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg2)));
                god_toggle.add(target.getName());
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

    @EventHandler
    public void onHunger(FoodLevelChangeEvent e) {
        Entity ent = e.getEntity();
        if (ent instanceof Player) {
            Player p = (Player) ent;
            if (god_toggle.contains(p.getName())) {
                e.setFoodLevel(20);
                e.setCancelled(true);
            }
        }
    }
}
