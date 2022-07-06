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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Back implements CommandExecutor {

    public static HashMap<UUID, Location> location = new HashMap<>();
    public static HashMap<UUID, Location> location2 = new HashMap<>();
    public static HashMap<UUID, Integer> backcancel = new HashMap<>();
    public static HashMap<UUID, Boolean> backconfirm = new HashMap<>();
    public static HashMap<UUID, Integer> back = new HashMap<>();
    public static ArrayList<UUID> cancel = new ArrayList<>();
    int time;
    int taskID;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            Long cooldown = ServerEssentials.getPlugin().getConfig().getLong("back-cooldown");
            int delay2 = (int) (cooldown * 20);
            int delay3 = delay2 / 20;
            int backwait = ServerEssentials.plugin.getConfig().getInt("back-teleport");
            if (ServerEssentials.permissionChecker(player, "se.back")) {
                if (player.hasPermission("se.back.bypass")) {
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
                    if (backcancel.containsKey(player.getUniqueId()) && backcancel.get(player.getUniqueId()) != null) {
                        String msg = Lang.fileConfig.getString("command-timeout").replace("<time>", String.valueOf(time));
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    }else{
                        if (ServerEssentials.plugin.getConfig().getInt("back-teleport") == 0){
                            if (location.containsKey(player.getUniqueId())){
                                blacklistCheck(player);
                                if (!blacklistCheck(player)){
                                    backCancel(player, location2, location, delay2, delay3);
                                }
                            }else if (location2.containsKey(player.getUniqueId())) {
                                blacklistCheck(player);
                                if (!blacklistCheck(player)){
                                    backCancel(player, location2, location, delay2, delay3);
                                }
                            }else{
                                String msg = Lang.fileConfig.getString("back-no-location");
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                            }
                        }else{
                            backwait = backwait * 20;
                            if (ServerEssentials.plugin.getConfig().getBoolean("back-movement-cancel")){
                                if (location.containsKey(player.getUniqueId())){
                                    cancel.add(player.getUniqueId());
                                    blacklistCheck(player);
                                    if (!blacklistCheck(player)){
                                        backWait(player, location, location2, backwait, delay2, delay3);
                                    }
                                }else if (location2.containsKey(player.getUniqueId())) {
                                    cancel.add(player.getUniqueId());
                                    blacklistCheck(player);
                                    if (!blacklistCheck(player)){
                                        backWait(player, location2, location, backwait, delay2, delay3);
                                    }
                                }else{
                                    String msg = Lang.fileConfig.getString("back-no-location");
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                }
                            }else{
                                if (location.containsKey(player.getUniqueId())){
                                    blacklistCheck(player);
                                    if (!blacklistCheck(player)){
                                        player.sendMessage("hi");
                                        backWait(player, location, location2, backwait, delay2, delay3);
                                    }
                                }else if (location2.containsKey(player.getUniqueId())) {
                                    blacklistCheck(player);
                                    if (!blacklistCheck(player)){
                                        backWait(player, location2, location, backwait, delay2, delay3);
                                    }
                                }else{
                                    String msg = Lang.fileConfig.getString("back-no-location");
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                }
                            }
                        }
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

    public void backCancel(Player player, HashMap<UUID, Location> location, HashMap<UUID, Location> location2, int delay2, int delay3) {
        location.put(player.getUniqueId(), player.getLocation());
        player.teleport(location2.get(player.getUniqueId()));
        location2.remove(player.getUniqueId());
        String msg = Lang.fileConfig.getString("back-previous-location");
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
        if (ServerEssentials.plugin.getConfig().getInt("back-cooldown") != 0){
            backcancel.put(player.getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((ServerEssentials.getPlugin()), new Runnable() {
                public void run() {
                    backconfirm.put(player.getUniqueId(), true);
                    backcancel.remove(player.getUniqueId());
                }
            }, delay2));
            setTimer(delay3);
            startTimer();
        }
        cancel.remove(player.getUniqueId());
        back.remove(player.getUniqueId());
    }

    public void backWait(Player player, HashMap<UUID, Location> location, HashMap<UUID, Location> location2, int backwait, int delay2, int delay3){
        String msg = Lang.fileConfig.getString("back-wait-message").replace("<time>", String.valueOf(backwait / 20));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
        if (back.containsKey(player.getUniqueId()) && back.get(player.getUniqueId()) != null) {
            Bukkit.getScheduler().cancelTask(back.get(player.getUniqueId()));
        }
        back.put(player.getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((ServerEssentials.plugin), new Runnable() {
            public void run() {
                if (ServerEssentials.plugin.getConfig().getBoolean("back-movement-cancel")){
                    if (cancel.contains(player.getUniqueId())){
                        backCancel(player, location2, location, delay2, delay3);
                    }
                }else{
                    backCancel(player, location2, location, delay2, delay3);
                }
            }
        }, backwait));
    }

    public static Boolean blacklistCheck(Player player){
        boolean blacklistedworld = false;
        if (ServerEssentials.plugin.getConfig().getBoolean("enable-back-blacklist")){
            for (String worlds : ServerEssentials.plugin.getConfig().getStringList("back-blacklist")) {
                if (player.getWorld().getName().equalsIgnoreCase(worlds)) {
                    String msg = Lang.fileConfig.getString("back-blacklisted-world");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    blacklistedworld = true;
                }
            }
        }
        return blacklistedworld;
    }
}
