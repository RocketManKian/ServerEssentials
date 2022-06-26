package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

public class Back implements CommandExecutor {

    public static HashMap<UUID, Location> location = new HashMap<UUID, Location>();
    public static HashMap<UUID, Location> location2 = new HashMap<UUID, Location>();
    public static HashMap<UUID, Integer> backcancel = new HashMap<UUID, Integer>();
    public static HashMap<UUID, Boolean> backconfirm = new HashMap<UUID, Boolean>();
    int time;
    int taskID;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            boolean hasPerm = ServerEssentials.permissionChecker(player, "se.back");
            if (hasPerm){
                boolean hasPerm2 = ServerEssentials.permissionChecker(player, "se.back.bypass");
                if (hasPerm2){
                    if (location.containsKey(player.getUniqueId())) {
                        location2.put(player.getUniqueId(), player.getLocation());
                        player.teleport(location.get(player.getUniqueId()));
                        location.remove(player.getUniqueId());
                        String msg = Lang.fileConfig.getString("back-previous-location");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    }else if (location2.containsKey(player.getUniqueId())){
                        location.put(player.getUniqueId(), player.getLocation());
                        player.teleport(location2.get(player.getUniqueId()));
                        location2.remove(player.getUniqueId());
                        String msg = Lang.fileConfig.getString("back-previous-location");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    } else{
                        String msg = Lang.fileConfig.getString("back-no-location");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    }
                } else{
                    Long delay = ServerEssentials.getPlugin().getConfig().getLong("back-cooldown");
                    int delay2 = (int) (delay * 20);
                    int delay3 = delay2 / 20;
                    if (location.containsKey(player.getUniqueId())) {
                        Boolean blacklistedworld = ServerEssentials.plugin.getConfig().getBoolean("enable-back-blacklist");
                        if (hasPerm2){
                            if (!backcancel.containsKey(player.getUniqueId())) {
                                location2.put(player.getUniqueId(), player.getLocation());
                                player.teleport(location.get(player.getUniqueId()));
                                location.remove(player.getUniqueId());
                                String msg = Lang.fileConfig.getString("back-previous-location");
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                backcancel.put(player.getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((ServerEssentials.getPlugin()), new Runnable() {
                                    public void run() {
                                        backconfirm.put(player.getUniqueId(), true);
                                        backcancel.remove(player.getUniqueId());
                                    }
                                }, delay2));
                                setTimer(delay3);
                                startTimer();
                                return true;
                            }
                            if (backcancel.containsKey(player.getUniqueId()) && backcancel.get(player.getUniqueId()) != null) {
                                String msg = Lang.fileConfig.getString("command-timeout").replace("<time>", String.valueOf(time));
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            }
                        }
                        if (blacklistedworld) {
                            for (String worlds : ServerEssentials.plugin.getConfig().getStringList("back-blacklist")) {
                                if (player.getWorld().getName().equalsIgnoreCase(worlds)) {
                                    String msg = Lang.fileConfig.getString("back-blacklisted-world");
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                    return true;
                                }
                            }
                            if (!backcancel.containsKey(player.getUniqueId())) {
                                location2.put(player.getUniqueId(), player.getLocation());
                                player.teleport(location.get(player.getUniqueId()));
                                location.remove(player.getUniqueId());
                                String msg = Lang.fileConfig.getString("back-previous-location");
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                backcancel.put(player.getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((ServerEssentials.getPlugin()), new Runnable() {
                                    public void run() {
                                        backconfirm.put(player.getUniqueId(), true);
                                        backcancel.remove(player.getUniqueId());
                                    }
                                }, delay2));
                                setTimer(delay3);
                                startTimer();
                                return true;
                            }
                            if (backcancel.containsKey(player.getUniqueId()) && backcancel.get(player.getUniqueId()) != null) {
                                String msg = Lang.fileConfig.getString("command-timeout").replace("<time>", String.valueOf(time));
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            }
                        }
                        if (!blacklistedworld){
                            if (!backcancel.containsKey(player.getUniqueId())) {
                                location2.put(player.getUniqueId(), player.getLocation());
                                player.teleport(location.get(player.getUniqueId()));
                                location.remove(player.getUniqueId());
                                String msg = Lang.fileConfig.getString("back-previous-location");
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                backcancel.put(player.getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((ServerEssentials.getPlugin()), new Runnable() {
                                    public void run() {
                                        backconfirm.put(player.getUniqueId(), true);
                                        backcancel.remove(player.getUniqueId());
                                    }
                                }, delay2));
                                setTimer(delay3);
                                startTimer();
                                return true;
                            }
                            if (backcancel.containsKey(player.getUniqueId()) && backcancel.get(player.getUniqueId()) != null) {
                                String msg = Lang.fileConfig.getString("command-timeout").replace("<time>", String.valueOf(time));
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            }
                        }
                    } else if (location2.containsKey(player.getUniqueId())) {
                        Boolean blacklistedworld = ServerEssentials.plugin.getConfig().getBoolean("enable-back-blacklist");
                        if (hasPerm2){
                            if (!backcancel.containsKey(player.getUniqueId())) {
                                location.put(player.getUniqueId(), player.getLocation());
                                player.teleport(location2.get(player.getUniqueId()));
                                location2.remove(player.getUniqueId());
                                String msg = Lang.fileConfig.getString("back-previous-location");
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                backcancel.put(player.getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((ServerEssentials.getPlugin()), new Runnable() {
                                    public void run() {
                                        backconfirm.put(player.getUniqueId(), true);
                                        backcancel.remove(player.getUniqueId());
                                    }
                                }, delay2));
                                setTimer(delay3);
                                startTimer();
                                return true;
                            }
                            if (backcancel.containsKey(player.getUniqueId()) && backcancel.get(player.getUniqueId()) != null) {
                                String msg = Lang.fileConfig.getString("command-timeout").replace("<time>", String.valueOf(time));
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            }
                        }
                        if (blacklistedworld) {
                            for (String worlds : ServerEssentials.plugin.getConfig().getStringList("back-blacklist")) {
                                if (player.getWorld().getName().equalsIgnoreCase(worlds)) {
                                    String msg = Lang.fileConfig.getString("back-blacklisted-world");
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                    return true;
                                }
                            }
                            if (!backcancel.containsKey(player.getUniqueId())) {
                                location.put(player.getUniqueId(), player.getLocation());
                                player.teleport(location2.get(player.getUniqueId()));
                                location2.remove(player.getUniqueId());
                                String msg = Lang.fileConfig.getString("back-previous-location");
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                backcancel.put(player.getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((ServerEssentials.getPlugin()), new Runnable() {
                                    public void run() {
                                        backconfirm.put(player.getUniqueId(), true);
                                        backcancel.remove(player.getUniqueId());
                                    }
                                }, delay2));
                                setTimer(delay3);
                                startTimer();
                                return true;
                            }
                            if (backcancel.containsKey(player.getUniqueId()) && backcancel.get(player.getUniqueId()) != null) {
                                String msg = Lang.fileConfig.getString("command-timeout").replace("<time>", String.valueOf(time));
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            }
                        }
                        if (!blacklistedworld){
                            if (!backcancel.containsKey(player.getUniqueId())) {
                                location.put(player.getUniqueId(), player.getLocation());
                                player.teleport(location2.get(player.getUniqueId()));
                                location2.remove(player.getUniqueId());
                                String msg = Lang.fileConfig.getString("back-previous-location");
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                backcancel.put(player.getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((ServerEssentials.getPlugin()), new Runnable() {
                                    public void run() {
                                        backconfirm.put(player.getUniqueId(), true);
                                        backcancel.remove(player.getUniqueId());
                                    }
                                }, delay2));
                                setTimer(delay3);
                                startTimer();
                                return true;
                            }
                            if (backcancel.containsKey(player.getUniqueId()) && backcancel.get(player.getUniqueId()) != null) {
                                String msg = Lang.fileConfig.getString("command-timeout").replace("<time>", String.valueOf(time));
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            }
                        }
                    }else{
                        String msg = Lang.fileConfig.getString("back-no-location");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public void setTimer(int amount) {
        time = amount;
    }
    public void startTimer() {
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        taskID = scheduler.scheduleSyncRepeatingTask(ServerEssentials.plugin, new Runnable() {
            @Override
            public void run() {
                if(time == 0) {
                    stopTimer();
                    return;
                }
                time = time - 1;
            }
        }, 0L, 20L);
    }
    public void stopTimer() {
        Bukkit.getScheduler().cancelTask(taskID);
    }
}