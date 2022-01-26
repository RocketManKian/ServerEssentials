package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public class Heal implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (sender instanceof Player) {
            Player target;
            Player player = (Player) sender;
            if (player.hasPermission("se.heal") || player.hasPermission("se.all")) {
                if (args.length <= 1) {
                    if (args.length == 1) {
                        target = Bukkit.getServer().getPlayer(args[0]);
                        if (target == null) {
                            String msg = Lang.fileConfig.getString("target-offline");
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                            return true;
                        } else if (target != null) {
                            if (target != player) {
                                if (ServerEssentials.getPlugin().getConfig().getBoolean("remove-effects-on-heal")) {
                                    target = Bukkit.getServer().getPlayer(args[0]);
                                    target.setHealth(target.getMaxHealth());
                                    String msg = Lang.fileConfig.getString("heal-target").replace("<target>", target.getName());
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                    String msg2 = Lang.fileConfig.getString("heal-target-message").replace("<sender>", player.getName());
                                    target.sendMessage(ChatColor.translateAlternateColorCodes('&', msg2));
                                    for (PotionEffect effect : target.getActivePotionEffects())
                                        target.removePotionEffect(effect.getType());
                                    return true;
                                } else {
                                    target = Bukkit.getServer().getPlayer(args[0]);
                                    target.setHealth(target.getMaxHealth());
                                    String msg = Lang.fileConfig.getString("heal-target").replace("<target>", target.getName());
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                    String msg2 = Lang.fileConfig.getString("heal-target-message").replace("<sender>", player.getName());
                                    target.sendMessage(ChatColor.translateAlternateColorCodes('&', msg2));
                                    return true;
                                }
                            }
                        } else {
                            String msg = Lang.fileConfig.getString("player-offline");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                            return true;
                        }
                    } else if (args.length == 0) {
                        if (ServerEssentials.getPlugin().getConfig().getBoolean("remove-effects-on-heal")){
                            try{
                                String msg = Lang.fileConfig.getString("heal-target");
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
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
                                String msg = Lang.fileConfig.getString("heal-self");
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                target = Bukkit.getServer().getPlayer(sender.getName());
                                target.setHealth(target.getMaxHealth());
                                return true;
                            } catch (ArrayIndexOutOfBoundsException a){
                                return true;
                            }
                        }
                    }
                } else if (args.length > 1) {
                    String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/heal (player)");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    return true;
                }
            } else {
                String perm = Lang.fileConfig.getString("no-permission-message").replace("<permission>", "se.heal");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', perm));
                return true;
            }
        }else if (sender instanceof ConsoleCommandSender){
            if (args.length == 1){
                Player target = Bukkit.getPlayer(args[0]);
                if (target != null){
                    if (ServerEssentials.getPlugin().getConfig().getBoolean("remove-effects-on-heal")) {
                        target = Bukkit.getServer().getPlayer(args[0]);
                        target.setHealth(target.getMaxHealth());
                        String msg = Lang.fileConfig.getString("heal-target").replace("<target>", target.getName());
                        Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', msg));
                        String msg2 = Lang.fileConfig.getString("heal-target-message").replace("<sender>", "Console");
                        target.sendMessage(ChatColor.translateAlternateColorCodes('&', msg2));
                        for (PotionEffect effect : target.getActivePotionEffects())
                            target.removePotionEffect(effect.getType());
                        return true;
                    } else {
                        target = Bukkit.getServer().getPlayer(args[0]);
                        target.setHealth(target.getMaxHealth());
                        String msg = Lang.fileConfig.getString("heal-target").replace("<target>", target.getName());
                        Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', msg));
                        String msg2 = Lang.fileConfig.getString("heal-target-message").replace("<sender>", "Console");
                        target.sendMessage(ChatColor.translateAlternateColorCodes('&', msg2));
                        return true;
                    }
                }else{
                    String msg = Lang.fileConfig.getString("target-offline");
                    Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', msg));
                    return true;
                }
            }
        }else if (sender instanceof BlockCommandSender){
            if (args.length == 1){
                BlockCommandSender block = (BlockCommandSender) sender;
                Player target = Bukkit.getPlayer(args[0]);
                if (target != null){
                    if (ServerEssentials.getPlugin().getConfig().getBoolean("remove-effects-on-heal")) {
                        target = Bukkit.getServer().getPlayer(args[0]);
                        target.setHealth(target.getMaxHealth());
                        for (PotionEffect effect : target.getActivePotionEffects())
                            target.removePotionEffect(effect.getType());
                        block.sendMessage("Successfully healed " + target.getName());
                        return true;
                    } else {
                        target = Bukkit.getServer().getPlayer(args[0]);
                        target.setHealth(target.getMaxHealth());
                        block.sendMessage("Successfully healed " + target.getName());
                        return true;
                    }
                }else{
                    String msg = Lang.fileConfig.getString("target-offline");
                    block.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    return true;
                }
            }
        }
        return false;
    }
}