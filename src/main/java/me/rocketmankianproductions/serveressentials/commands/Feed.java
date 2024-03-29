package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.HashMap;
import java.util.UUID;

import static me.rocketmankianproductions.serveressentials.ServerEssentials.hex;

public class Feed implements CommandExecutor {

    public static HashMap<UUID, Integer> feedcancel = new HashMap<UUID, Integer>();
    int time;
    int taskID;
    Long delay = ServerEssentials.getPlugin().getConfig().getLong("feed-cooldown");
    int delay2 = (int) (delay * 20);
    int delay3 = delay2 / 20;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (ServerEssentials.permissionChecker(player, "se.feed")) {
                if (!feedcancel.containsKey(player.getUniqueId())){
                    if (args.length == 1) {
                        Player target = Bukkit.getPlayer(args[0]);
                        if (target == null) {
                            String msg = Lang.fileConfig.getString("target-offline");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                        } else {
                            if (target != player) {
                                target = Bukkit.getServer().getPlayer(args[0]);
                                target.setFoodLevel(20);
                                target.setSaturation(5);
                                String msg = Lang.fileConfig.getString("feed-sender-message").replace("<target>", target.getName());
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                                String msg2 = Lang.fileConfig.getString("feed-target-message".replace("<sender>", sender.getName()));
                                target.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg2)));
                            }else{
                                String msg = Lang.fileConfig.getString("feed-self");
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                                player.setFoodLevel(20);
                                player.setSaturation(5);
                            }
                            // Command Cooldown
                            feedcancel.put(player.getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((ServerEssentials.getPlugin()), new Runnable() {
                                public void run() {
                                    feedcancel.remove(player.getUniqueId());
                                }
                            }, delay2));
                            setTimer(delay3);
                            startTimer();
                        }
                        return true;
                    } else if (args.length == 0) {
                        String msg = Lang.fileConfig.getString("feed-self");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                        player.setFoodLevel(20);
                        player.setSaturation(5);
                        // Command Cooldown
                        feedcancel.put(player.getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((ServerEssentials.getPlugin()), new Runnable() {
                            public void run() {
                                feedcancel.remove(player.getUniqueId());
                            }
                        }, delay2));
                        setTimer(delay3);
                        startTimer();
                    } else {
                        String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/feed (player)");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                    }
                    return true;
                }else if (feedcancel.containsKey(player.getUniqueId()) && feedcancel.get(player.getUniqueId()) != null) {
                    String msg = Lang.fileConfig.getString("command-timeout").replace("<time>", String.valueOf(time));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                    return true;
                }
            }
        }else if (sender instanceof ConsoleCommandSender){
            if (args.length == 1){
                Player target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    String msg = Lang.fileConfig.getString("player-offline");
                    Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                    return true;
                } else {
                    target = Bukkit.getServer().getPlayer(args[0]);
                    target.setFoodLevel(20);
                    target.setSaturation(5);
                    String msg = Lang.fileConfig.getString("feed-sender-message").replace("<target>", target.getName());
                    Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                    String msg2 = Lang.fileConfig.getString("feed-target-message").replace("<sender>", "Console");
                    target.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg2)));
                    return true;
                }
            }
        }else if (sender instanceof BlockCommandSender){
            if (args.length == 1){
                BlockCommandSender block = (BlockCommandSender) sender;
                Player target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    String msg = Lang.fileConfig.getString("player-offline");
                    block.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                    return true;
                } else {
                    target = Bukkit.getServer().getPlayer(args[0]);
                    target.setFoodLevel(20);
                    target.setSaturation(5);
                    block.sendMessage("Successfully fed " + target.getName());
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
