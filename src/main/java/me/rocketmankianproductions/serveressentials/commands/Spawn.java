package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Spawn implements CommandExecutor {

    private static HashMap<UUID, Integer> spawnteleport = new HashMap<>();
    public static ArrayList<UUID> cancel = new ArrayList<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        int delay = ServerEssentials.plugin.getConfig().getInt("spawn-teleport");
        if (sender instanceof Player){
            Player player = (Player) sender;
            // Checking if the player has the correct permission
            if (player.hasPermission("se.spawn")) {
                // Check if the File Exists and if Location.World has data
                if (Setspawn.file.exists() && Setspawn.fileConfig.getString("Location.World") != null) {
                    Location loc = getLocation();
                    if (args.length == 0) {
                        if (ServerEssentials.plugin.getConfig().getInt("spawn-teleport") == 0){
                            if (ServerEssentials.getPlugin().getConfig().getBoolean("spawn-save")){
                                if (Back.location.containsKey(player.getUniqueId())){
                                    Back.location.remove(player.getUniqueId());
                                    Back.location.put(player.getUniqueId(), player.getLocation());
                                }else{
                                    Back.location.put(player.getUniqueId(), player.getLocation());
                                }
                            }else if (player.hasPermission("se.back.bypass")){
                                if (Back.location.containsKey(player.getUniqueId())){
                                    Back.location.remove(player.getUniqueId());
                                    Back.location.put(player.getUniqueId(), player.getLocation());
                                }else{
                                    Back.location.put(player.getUniqueId(), player.getLocation());
                                }
                            }
                            // Teleporting Player
                            player.teleport(loc);
                            player.sendMessage("Successfully teleported to spawn.");
                            return true;
                        }else{
                            if (ServerEssentials.plugin.getConfig().getBoolean("spawn-movement-cancel")){
                                cancel.add(player.getUniqueId());
                                player.sendMessage(ChatColor.GREEN + "Teleporting to Spawn in " + ChatColor.GOLD + delay + " Seconds");
                                delay = delay * 20;
                                if (spawnteleport.containsKey(player.getUniqueId()) && spawnteleport.get(player.getUniqueId()) != null) {
                                    Bukkit.getScheduler().cancelTask(spawnteleport.get(player.getUniqueId()));
                                }
                                spawnteleport.put(player.getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((ServerEssentials.plugin), new Runnable() {
                                    public void run() {
                                        if (cancel.contains(player.getUniqueId())){
                                            if (spawnteleport.containsKey(player.getUniqueId())) {
                                                if (ServerEssentials.getPlugin().getConfig().getBoolean("spawn-save")){
                                                    if (Back.location.containsKey(player.getUniqueId())){
                                                        Back.location.remove(player.getUniqueId());
                                                        Back.location.put(player.getUniqueId(), player.getLocation());
                                                    }else{
                                                        Back.location.put(player.getUniqueId(), player.getLocation());
                                                    }
                                                }else if (player.hasPermission("se.back.bypass")){
                                                    if (Back.location.containsKey(player.getUniqueId())){
                                                        Back.location.remove(player.getUniqueId());
                                                        Back.location.put(player.getUniqueId(), player.getLocation());
                                                    }else{
                                                        Back.location.put(player.getUniqueId(), player.getLocation());
                                                    }
                                                }
                                                // Teleporting Player
                                                player.teleport(loc);
                                                player.sendMessage("Successfully teleported to spawn.");
                                                cancel.remove(player.getUniqueId());
                                                spawnteleport.remove(player.getUniqueId());
                                            }
                                        }
                                    }
                                }, delay));
                                return true;
                            }else{
                                player.sendMessage(ChatColor.GREEN + "Teleporting to Spawn in " + ChatColor.GOLD + delay + " Seconds");
                                delay = delay * 20;
                                if (spawnteleport.containsKey(player.getUniqueId()) && spawnteleport.get(player.getUniqueId()) != null) {
                                    Bukkit.getScheduler().cancelTask(spawnteleport.get(player.getUniqueId()));
                                }
                                spawnteleport.put(player.getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((ServerEssentials.plugin), new Runnable() {
                                    public void run() {
                                        if (spawnteleport.containsKey(player.getUniqueId())) {
                                            if (ServerEssentials.getPlugin().getConfig().getBoolean("spawn-save")){
                                                if (Back.location.containsKey(player.getUniqueId())){
                                                    Back.location.remove(player.getUniqueId());
                                                    Back.location.put(player.getUniqueId(), player.getLocation());
                                                }else{
                                                    Back.location.put(player.getUniqueId(), player.getLocation());
                                                }
                                            }else if (player.hasPermission("se.back.bypass")){
                                                if (Back.location.containsKey(player.getUniqueId())){
                                                    Back.location.remove(player.getUniqueId());
                                                    Back.location.put(player.getUniqueId(), player.getLocation());
                                                }else{
                                                    Back.location.put(player.getUniqueId(), player.getLocation());
                                                }
                                            }
                                            // Teleporting Player
                                            player.teleport(loc);
                                            player.sendMessage("Successfully teleported to spawn.");
                                        }
                                    }
                                }, delay));
                                return true;
                            }
                        }
                    } else if (args.length >= 1) {
                        if (player.hasPermission("se.spawn.others")) {
                            Player target = Bukkit.getPlayerExact(args[0]);
                            // Checking if the player exists
                            if (target != null) {
                                if (ServerEssentials.getPlugin().getConfig().getBoolean("spawn-save")) {
                                    if (Back.location.containsKey(target.getUniqueId())) {
                                        Back.location.remove(target.getUniqueId());
                                        Back.location.put(target.getUniqueId(), target.getLocation());
                                    } else {
                                        Back.location.put(target.getUniqueId(), target.getLocation());
                                    }
                                } else if (target.hasPermission("se.back.bypass")) {
                                    if (Back.location.containsKey(target.getUniqueId())) {
                                        Back.location.remove(target.getUniqueId());
                                        Back.location.put(target.getUniqueId(), target.getLocation());
                                    } else {
                                        Back.location.put(target.getUniqueId(), target.getLocation());
                                    }
                                }
                                // Teleporting player to Location
                                target.teleport(loc);
                                // Sending the Sender and Target a message
                                sender.sendMessage(ChatColor.GREEN + ("You have teleported " + ChatColor.WHITE + target.getName() + ChatColor.GREEN + " to spawn."));
                                target.sendMessage(ChatColor.GREEN + ("You have been teleported to spawn."));
                                return true;
                            } else {
                                sender.sendMessage(ChatColor.RED + ("Player not found."));
                                return true;
                            }
                        }else{
                            if (ServerEssentials.plugin.getConfig().getString("no-permission-message").length() == 0){
                                player.sendMessage(ChatColor.RED + "You do not have the required permission (se.spawn.others) to run this command.");
                                return true;
                            }else{
                                String permission = ServerEssentials.getPlugin().getConfig().getString("no-permission-message");
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', permission));
                                return true;
                            }
                        }
                    }
                } else {
                    // Sends Message if Spawn Doesn't Exist
                    sender.sendMessage("Spawn doesn't exist.");
                }
            } else {
                if (ServerEssentials.plugin.getConfig().getString("no-permission-message").length() == 0){
                    player.sendMessage(ChatColor.RED + "You do not have the required permission (se.spawn) to run this command.");
                    return true;
                }else{
                    String permission = ServerEssentials.getPlugin().getConfig().getString("no-permission-message");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', permission));
                    return true;
                }
            }
        }else if (sender instanceof ConsoleCommandSender){
            if (args.length == 1){
                Player target = Bukkit.getPlayerExact(args[0]);
                if (target != null){
                    // Check if the File Exists and if Location.World has data
                    if (Setspawn.file.exists() && Setspawn.fileConfig.getString("Location.World") != null) {
                        Location loc = getLocation();
                        if (ServerEssentials.plugin.getConfig().getBoolean("spawn-save")){
                            if (Back.location.containsKey(target.getUniqueId())){
                                Back.location.remove(target.getUniqueId());
                                Back.location.put(target.getUniqueId(), target.getLocation());
                            }else{
                                Back.location.put(target.getUniqueId(), target.getLocation());
                            }
                        }else if (target.hasPermission("se.back.bypass")){
                            if (Back.location.containsKey(target.getUniqueId())){
                                Back.location.remove(target.getUniqueId());
                                Back.location.put(target.getUniqueId(), target.getLocation());
                            }else{
                                Back.location.put(target.getUniqueId(), target.getLocation());
                            }
                        }
                        // Teleporting player to Location
                        target.teleport(loc);
                        // Sending the Sender and Target a message
                        System.out.println(ChatColor.GREEN + ("You have teleported " + ChatColor.WHITE + target.getName() + ChatColor.GREEN + " to spawn."));
                        target.sendMessage(ChatColor.GREEN + ("You have been teleported to spawn."));
                        return true;
                    } else {
                        // Sends Message if Spawn Doesn't Exist
                        System.out.println(ChatColor.RED + "Spawn doesn't exist.");
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + ("Player not found."));
                    return true;
                }
            }
        }
        return false;
    }
    public static Location getLocation() {
        // Gathering Location
        float yaw = Setspawn.fileConfig.getInt("Location.Yaw");
        float pitch = Setspawn.fileConfig.getInt("Location.Pitch");
        Location loc = new Location(Bukkit.getWorld(Setspawn.fileConfig.getString("Location.World")), Setspawn.fileConfig.getDouble("Location.X"), Setspawn.fileConfig.getDouble("Location.Y"), Setspawn.fileConfig.getDouble("Location.Z"), yaw, pitch);
        return loc;
    }
}
