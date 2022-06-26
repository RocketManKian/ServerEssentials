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
import org.jetbrains.annotations.NotNull;

public class SendSpawn implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            boolean hasPerm = ServerEssentials.permissionChecker(player, "se.spawn.others");
            if (hasPerm) {
                if (args.length == 1){
                    if (Setspawn.fileConfig.getString("Location.World") != null) {
                        Location loc = getLocation();
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
                            if (loc.isWorldLoaded()) {
                                // Teleporting player to Location
                                target.teleport(loc);
                                // Sending the Sender and Target a message
                                String msg = Lang.fileConfig.getString("spawn-teleport-target").replace("<target>", target.getName());
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                String msg2 = Lang.fileConfig.getString("spawn-teleport-target-success");
                                target.sendMessage(ChatColor.translateAlternateColorCodes('&', msg2));
                                return true;
                            } else {
                                String msg = Lang.fileConfig.getString("spawn-world-invalid");
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            }
                        } else {
                            String msg = Lang.fileConfig.getString("target-offline");
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                            return true;
                        }
                    }else{
                        // Sends Message Newbies Spawn Doesn't Exist
                        String msg = Lang.fileConfig.getString("spawn-invalid");
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    }
                }else if (args.length == 2){
                    if (args[0].equalsIgnoreCase("newbies")) {
                        if (Setspawn.fileConfig.getString("Newbies.Location.World") != null) {
                            Location loc = getNewbiesLocation();
                            Player target = Bukkit.getPlayerExact(args[1]);
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
                                if (loc.isWorldLoaded()) {
                                    // Teleporting player to Location
                                    target.teleport(loc);
                                    // Sending the Sender and Target a message
                                    String msg = Lang.fileConfig.getString("newbies-spawn-teleport-target").replace("<target>", target.getName());
                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                    String msg2 = Lang.fileConfig.getString("newbies-spawn-teleport-target-success");
                                    target.sendMessage(ChatColor.translateAlternateColorCodes('&', msg2));
                                    return true;
                                } else {
                                    String msg = Lang.fileConfig.getString("spawn-world-invalid");
                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                    return true;
                                }
                            } else {
                                String msg = Lang.fileConfig.getString("target-offline");
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            }
                        } else {
                            // Sends Message Newbies Spawn Doesn't Exist
                            String msg = Lang.fileConfig.getString("newbies-spawn-invalid");
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        }
                    }
                }
            }
        }else{
            String console = Lang.fileConfig.getString("console-invalid");
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', console));
            return true;
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
    public static Location getNewbiesLocation() {
        // Gathering Location
        float yaw = Setspawn.fileConfig.getInt("Newbies.Location.Yaw");
        float pitch = Setspawn.fileConfig.getInt("Newbies.Location.Pitch");
        Location loc = new Location(Bukkit.getWorld(Setspawn.fileConfig.getString("Newbies.Location.World")), Setspawn.fileConfig.getDouble("Newbies.Location.X"), Setspawn.fileConfig.getDouble("Newbies.Location.Y"), Setspawn.fileConfig.getDouble("Newbies.Location.Z"), yaw, pitch);
        return loc;
    }
}
