package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

public class Back implements CommandExecutor {

    public static HashMap<UUID, Location> location = new HashMap<UUID, Location>();
    public static HashMap<UUID, Integer> backcancel = new HashMap<UUID, Integer>();
    public static HashMap<UUID, Boolean> backconfirm = new HashMap<UUID, Boolean>();
    int time;
    int taskID;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            if (player.hasPermission("se.back")){
                if (player.hasPermission("se.back.bypass")){
                    if (location.containsKey(player.getUniqueId())){
                        player.teleport(location.get(player.getUniqueId()));
                        player.sendMessage(ChatColor.GOLD + "Teleported to previous location");
                        return true;
                    }else{
                        player.sendMessage(ChatColor.RED + "You have no location to return to");
                        return true;
                    }
                }else{
                    Long delay = ServerEssentials.getPlugin().getConfig().getLong("back-cooldown");
                    int delay2 = (int) (delay * 20);
                    int delay3 = delay2 / 20;
                    if (location.containsKey(player.getUniqueId())) {
                        Boolean blacklistedworld = ServerEssentials.plugin.getConfig().getBoolean("enable-back-blacklist");
                        if (player.hasPermission("se.back.bypass")) {
                            if (!backcancel.containsKey(player.getUniqueId())) {
                                player.teleport(location.get(player.getUniqueId()));
                                player.sendMessage(ChatColor.GOLD + "Teleported to previous location");
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
                                player.sendMessage(ChatColor.RED + "You cannot use this command for another " + ChatColor.GOLD + time + " Seconds");
                                return true;
                            }
                        }
                        if (blacklistedworld) {
                            for (String worlds : ServerEssentials.plugin.getConfig().getStringList("back-blacklist")) {
                                if (player.getWorld().getName().equalsIgnoreCase(worlds)) {
                                    player.sendMessage(ChatColor.RED + "Cannot use Back Command in a Blacklisted World");
                                    return true;
                                }
                            }
                            if (!backcancel.containsKey(player.getUniqueId())) {
                                player.teleport(location.get(player.getUniqueId()));
                                player.sendMessage(ChatColor.GOLD + "Teleported to previous location");
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
                                player.sendMessage(ChatColor.RED + "You cannot use this command for another " + ChatColor.GOLD + time + " Seconds");
                                return true;
                            }
                        }
                        if (!blacklistedworld){
                            if (!backcancel.containsKey(player.getUniqueId())) {
                                player.teleport(location.get(player.getUniqueId()));
                                player.sendMessage(ChatColor.GOLD + "Teleported to previous location");
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
                                player.sendMessage(ChatColor.RED + "You cannot use this command for another " + ChatColor.GOLD + time + " Seconds");
                                return true;
                            }
                        }
                    }else{
                        player.sendMessage(ChatColor.RED + "You have no location to return to");
                        return true;
                    }
                }
            }else{
                if (ServerEssentials.plugin.getConfig().getString("no-permission-message").length() == 0){
                    player.sendMessage(ChatColor.RED + "You do not have the required permission (se.back) to run this command.");
                    return true;
                }else{
                    String permission = ServerEssentials.getPlugin().getConfig().getString("no-permission-message");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', permission));
                    return true;
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