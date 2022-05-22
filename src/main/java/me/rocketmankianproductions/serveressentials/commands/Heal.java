package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.HashMap;
import java.util.UUID;

public class Heal implements CommandExecutor {

    public static HashMap<UUID, Integer> healcancel = new HashMap<UUID, Integer>();
    int time;
    int taskID;
    Long delay = ServerEssentials.getPlugin().getConfig().getLong("heal-cooldown");
    int delay2 = (int) (delay * 20);
    int delay3 = delay2 / 20;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (sender instanceof Player) {
            Player target;
            Player player = (Player) sender;
            if (player.hasPermission("se.heal") || player.hasPermission("se.all")) {
                if (args.length <= 1) {
                    // Check to see if Player has command cooldown active
                    if (!healcancel.containsKey(player.getUniqueId())) {
                        if (args.length == 1) {
                            target = Bukkit.getServer().getPlayer(args[0]);
                            if (target == null) {
                                String msg = Lang.fileConfig.getString("target-offline");
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            } else {
                                if (ServerEssentials.getPlugin().getConfig().getBoolean("remove-effects-on-heal")) {
                                    target = Bukkit.getServer().getPlayer(args[0]);
                                    target.setHealth(target.getMaxHealth());
                                    for (PotionEffect effect : target.getActivePotionEffects())
                                        target.removePotionEffect(effect.getType());
                                    if (target != player) {
                                        String msg = Lang.fileConfig.getString("heal-target").replace("<target>", target.getName());
                                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                        String msg2 = Lang.fileConfig.getString("heal-target-message").replace("<sender>", player.getName());
                                        target.sendMessage(ChatColor.translateAlternateColorCodes('&', msg2));
                                    }else{
                                        String msg = Lang.fileConfig.getString("heal-self");
                                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                    }
                                } else {
                                    target = Bukkit.getServer().getPlayer(args[0]);
                                    target.setHealth(target.getMaxHealth());
                                    if (target != player) {
                                        String msg = Lang.fileConfig.getString("heal-target").replace("<target>", target.getName());
                                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                        String msg2 = Lang.fileConfig.getString("heal-target-message").replace("<sender>", player.getName());
                                        target.sendMessage(ChatColor.translateAlternateColorCodes('&', msg2));
                                    }else{
                                        String msg = Lang.fileConfig.getString("heal-self");
                                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                    }
                                }
                                // Command Cooldown
                                healcancel.put(player.getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((ServerEssentials.getPlugin()), new Runnable() {
                                    public void run() {
                                        healcancel.remove(player.getUniqueId());
                                    }
                                }, delay2));
                                setTimer(delay3);
                                startTimer();
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
                                } catch (ArrayIndexOutOfBoundsException a){
                                }
                            }
                            // Command Cooldown
                            healcancel.put(player.getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((ServerEssentials.getPlugin()), new Runnable() {
                                public void run() {
                                    healcancel.remove(player.getUniqueId());
                                }
                            }, delay2));
                            setTimer(delay3);
                            startTimer();
                            return true;
                        }
                    }else if (healcancel.containsKey(player.getUniqueId()) && healcancel.get(player.getUniqueId()) != null) {
                        String msg = Lang.fileConfig.getString("command-timeout").replace("<time>", String.valueOf(time));
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
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
    public void setTimer ( int amount){
        time = amount;
    }

    public void startTimer () {
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        taskID = scheduler.scheduleSyncRepeatingTask(ServerEssentials.plugin, new Runnable() {
            @Override
            public void run() {
                if (time == 0) {
                    stopTimer();
                    return;
                }
                time = time - 1;
            }
        }, 0L, 20L);
    }

    public void stopTimer () {
        Bukkit.getScheduler().cancelTask(taskID);
    }
}