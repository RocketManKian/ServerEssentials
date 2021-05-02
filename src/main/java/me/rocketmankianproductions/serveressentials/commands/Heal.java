package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Heal implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

        if (sender instanceof Player) {
            Player target;
            Player player = (Player) sender;
            if (player.hasPermission("se.heal")) {
                if (args.length <= 1) {
                    if (args.length == 1) {
                        target = Bukkit.getServer().getPlayer(args[0]);
                        if (target == null) {
                            sender.sendMessage(ChatColor.RED + "Cannot find player " + args[0] + ".");
                            return true;
                        } else if (target != null) {
                            if (target != player) {
                                if (ServerEssentials.getPlugin().getConfig().getBoolean("remove-effects-on-heal")) {
                                    target = Bukkit.getServer().getPlayer(args[0]);
                                    target.setHealth(target.getMaxHealth());
                                    sender.sendMessage(ChatColor.GREEN + "You have healed " + target.getName() + ".");
                                    target.sendMessage(ChatColor.GREEN + "You have been healed by " + sender.getName() + ".");
                                    for (PotionEffect effect : target.getActivePotionEffects())
                                        target.removePotionEffect(effect.getType());
                                    return true;
                                } else {
                                    target = Bukkit.getServer().getPlayer(args[0]);
                                    target.setHealth(target.getMaxHealth());
                                    sender.sendMessage(ChatColor.GREEN + "You have healed " + target.getName() + ".");
                                    target.sendMessage(ChatColor.GREEN + "You have been healed by " + sender.getName() + ".");
                                    return true;
                                }
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + "Player doesn't exist.");
                        }
                    } else if (args.length == 0) {
                        if (ServerEssentials.getPlugin().getConfig().getBoolean("remove-effects-on-heal")){
                            try{
                                sender.sendMessage(ChatColor.GREEN + "You have healed yourself");
                                target = Bukkit.getServer().getPlayer(sender.getName());
                                target.setHealth(target.getMaxHealth());
                                for (PotionEffect effect : player.getActivePotionEffects())
                                    player.removePotionEffect(effect.getType());
                                return true;
                            } catch (ArrayIndexOutOfBoundsException a){
                                return true;
                            }
                        }else {
                            try{
                                sender.sendMessage(ChatColor.GREEN + "You have healed yourself");
                                target = Bukkit.getServer().getPlayer(sender.getName());
                                target.setHealth(target.getMaxHealth());
                                return true;
                            } catch (ArrayIndexOutOfBoundsException a){
                                return true;
                            }
                        }
                    }
                } else if (args.length > 1) {
                    player.sendMessage(ChatColor.RED + "Incorrect format! Use /heal (name)");
                    return true;
                }
            } else {
                if (ServerEssentials.plugin.getConfig().getString("no-permission-message").length() == 0){
                    player.sendMessage(ChatColor.RED + "You do not have the required permission (se.heal) to run this command.");
                    return true;
                }else{
                    String permission = ServerEssentials.getPlugin().getConfig().getString("no-permission-message");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', permission));
                    return true;
                }
            }
        }else if (sender instanceof ConsoleCommandSender){
            if (args.length == 1){
                Player target = Bukkit.getPlayer(args[0]);
                if (target != null){
                    if (ServerEssentials.getPlugin().getConfig().getBoolean("remove-effects-on-heal")) {
                        target = Bukkit.getServer().getPlayer(args[0]);
                        target.setHealth(target.getMaxHealth());
                        System.out.println(ChatColor.GREEN + "You have healed " + target.getName() + ".");
                        target.sendMessage(ChatColor.GREEN + "You have been healed by " + ChatColor.WHITE + "Console.");
                        for (PotionEffect effect : target.getActivePotionEffects())
                            target.removePotionEffect(effect.getType());
                        return true;
                    } else {
                        target = Bukkit.getServer().getPlayer(args[0]);
                        target.setHealth(target.getMaxHealth());
                        System.out.println(ChatColor.GREEN + "You have healed " + target.getName() + ".");
                        target.sendMessage(ChatColor.GREEN + "You have been healed by " + ChatColor.WHITE + "Console.");
                        return true;
                    }
                }else{
                    System.out.println(ChatColor.RED + "Player doesn't exist.");
                    return true;
                }
            }
        }
        return false;
    }
}
