package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
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

import static me.rocketmankianproductions.serveressentials.ServerEssentials.hex;
import static me.rocketmankianproductions.serveressentials.ServerEssentials.permissionChecker;

public class Spawn implements CommandExecutor {

    private static HashMap<UUID, Integer> spawnteleport = new HashMap<>();
    public static ArrayList<UUID> cancel = new ArrayList<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        int delay = ServerEssentials.plugin.getConfig().getInt("spawn-teleport");
        if (sender instanceof Player){
            Player player = (Player) sender;
            // Checking if the player has the correct permission
            if (ServerEssentials.permissionChecker(player, "se.spawn")){
                // Check if the File Exists
                if (Setspawn.file.exists()) {
                    if (args.length == 0) {
                        if (command.getName().equalsIgnoreCase("tutorial")){
                            if (!checkNewbiesSpawn(sender)) {
                                Location loc = getNewbiesLocation();
                                if (ServerEssentials.plugin.getConfig().getInt("spawn-teleport") == 0){
                                    spawnSave(player);
                                    spawnTeleport(player, loc, "newbies-spawn-successful");
                                }else {
                                    if (ServerEssentials.plugin.getConfig().getBoolean("spawn-movement-cancel")) {
                                        cancel.add(player.getUniqueId());
                                        String msg = Lang.fileConfig.getString("newbies-spawn-wait-message").replace("<time>", String.valueOf(delay));
                                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                                        delay = delay * 20;
                                        if (spawnteleport.containsKey(player.getUniqueId()) && spawnteleport.get(player.getUniqueId()) != null) {
                                            Bukkit.getScheduler().cancelTask(spawnteleport.get(player.getUniqueId()));
                                        }
                                        spawnteleport.put(player.getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((ServerEssentials.plugin), new Runnable() {
                                            public void run() {
                                                if (cancel.contains(player.getUniqueId())) {
                                                    if (spawnteleport.containsKey(player.getUniqueId())) {
                                                        spawnSave(player);
                                                        spawnTeleport(player, loc, "newbies-spawn-successful");
                                                    }
                                                }
                                            }
                                        }, delay));
                                        return true;
                                    } else {
                                        String msg = Lang.fileConfig.getString("newbies-spawn-wait-message").replace("<time>", String.valueOf(delay));
                                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                                        delay = delay * 20;
                                        if (spawnteleport.containsKey(player.getUniqueId()) && spawnteleport.get(player.getUniqueId()) != null) {
                                            Bukkit.getScheduler().cancelTask(spawnteleport.get(player.getUniqueId()));
                                        }
                                        spawnteleport.put(player.getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((ServerEssentials.plugin), new Runnable() {
                                            public void run() {
                                                if (spawnteleport.containsKey(player.getUniqueId())) {
                                                    spawnSave(player);
                                                    spawnTeleport(player, loc, "newbies-spawn-successful");
                                                }
                                            }
                                        }, delay));
                                        return true;
                                    }
                                }
                            }
                        }else if (command.getName().equalsIgnoreCase("spawn")) {
                            if (!checkSpawn(sender)){
                                Location loc = getLocation();
                                if (ServerEssentials.plugin.getConfig().getInt("spawn-teleport") == 0) {
                                    spawnSave(player);
                                    spawnTeleport(player, loc, "spawn-successful");
                                } else {
                                    if (ServerEssentials.plugin.getConfig().getBoolean("spawn-movement-cancel")) {
                                        cancel.add(player.getUniqueId());
                                        String msg = Lang.fileConfig.getString("spawn-wait-message").replace("<time>", String.valueOf(delay));
                                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                                        delay = delay * 20;
                                        if (spawnteleport.containsKey(player.getUniqueId()) && spawnteleport.get(player.getUniqueId()) != null) {
                                            Bukkit.getScheduler().cancelTask(spawnteleport.get(player.getUniqueId()));
                                        }
                                        spawnteleport.put(player.getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((ServerEssentials.plugin), new Runnable() {
                                            public void run() {
                                                if (cancel.contains(player.getUniqueId())) {
                                                    if (spawnteleport.containsKey(player.getUniqueId())) {
                                                        spawnSave(player);
                                                        spawnTeleport(player, loc, "spawn-successful");
                                                    }
                                                }
                                            }
                                        }, delay));
                                        return true;
                                    } else {
                                        String msg = Lang.fileConfig.getString("spawn-wait-message").replace("<time>", String.valueOf(delay));
                                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                                        delay = delay * 20;
                                        if (spawnteleport.containsKey(player.getUniqueId()) && spawnteleport.get(player.getUniqueId()) != null) {
                                            Bukkit.getScheduler().cancelTask(spawnteleport.get(player.getUniqueId()));
                                        }
                                        spawnteleport.put(player.getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((ServerEssentials.plugin), new Runnable() {
                                            public void run() {
                                                if (spawnteleport.containsKey(player.getUniqueId())) {
                                                    spawnSave(player);
                                                    spawnTeleport(player, loc, "spawn-successful");
                                                }
                                            }
                                        }, delay));
                                        return true;
                                    }
                                }
                            }
                        }
                    } else if (args.length == 1) {
                        // Teleport to Newbies Spawn
                        if (args[0].equalsIgnoreCase("newbies")) {
                            if (!checkNewbiesSpawn(sender)){
                                Location loc = getNewbiesLocation();
                                if (ServerEssentials.plugin.getConfig().getInt("spawn-teleport") == 0) {
                                    spawnSave(player);
                                    spawnTeleport(player, loc, "newbies-spawn-successful");
                                } else {
                                    if (ServerEssentials.plugin.getConfig().getBoolean("spawn-movement-cancel")) {
                                        cancel.add(player.getUniqueId());
                                        String msg = Lang.fileConfig.getString("newbies-spawn-wait-message").replace("<time>", String.valueOf(delay));
                                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                                        delay = delay * 20;
                                        if (spawnteleport.containsKey(player.getUniqueId()) && spawnteleport.get(player.getUniqueId()) != null) {
                                            Bukkit.getScheduler().cancelTask(spawnteleport.get(player.getUniqueId()));
                                        }
                                        spawnteleport.put(player.getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((ServerEssentials.plugin), new Runnable() {
                                            public void run() {
                                                if (cancel.contains(player.getUniqueId())) {
                                                    if (spawnteleport.containsKey(player.getUniqueId())) {
                                                        spawnSave(player);
                                                        spawnTeleport(player, loc, "newbies-spawn-successful");
                                                    }
                                                }
                                            }
                                        }, delay));
                                        return true;
                                    } else {
                                        String msg = Lang.fileConfig.getString("newbies-spawn-wait-message").replace("<time>", String.valueOf(delay));
                                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                                        delay = delay * 20;
                                        if (spawnteleport.containsKey(player.getUniqueId()) && spawnteleport.get(player.getUniqueId()) != null) {
                                            Bukkit.getScheduler().cancelTask(spawnteleport.get(player.getUniqueId()));
                                        }
                                        spawnteleport.put(player.getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((ServerEssentials.plugin), new Runnable() {
                                            public void run() {
                                                if (spawnteleport.containsKey(player.getUniqueId())) {
                                                    spawnSave(player);
                                                    spawnTeleport(player, loc, "newbies-spawn-successful");
                                                }
                                            }
                                        }, delay));
                                        return true;
                                    }
                                }
                            }
                        } else {
                            if (permissionChecker(player, "se.spawn.others")){
                                if (!checkSpawn(sender)){
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
                                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                                            String msg2 = Lang.fileConfig.getString("spawn-teleport-target-success");
                                            target.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg2)));
                                            return true;
                                        } else {
                                            String msg = Lang.fileConfig.getString("spawn-world-invalid");
                                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                                            return true;
                                        }
                                    } else {
                                        String msg = Lang.fileConfig.getString("target-offline");
                                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                                        return true;
                                    }
                                }
                            }
                        }
                    }else if (args.length == 2 && args[1].equalsIgnoreCase("newbies")){
                        if (permissionChecker(player, "se.spawn.others")) {
                            if (!checkNewbiesSpawn(sender)) {
                                Location loc = getNewbiesLocation();
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
                                        String msg = Lang.fileConfig.getString("newbies-spawn-teleport-target").replace("<target>", target.getName());
                                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                                        String msg2 = Lang.fileConfig.getString("newbies-spawn-teleport-target-success");
                                        target.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg2)));
                                        return true;
                                    } else {
                                        String msg = Lang.fileConfig.getString("spawn-world-invalid");
                                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                                        return true;
                                    }
                                } else {
                                    String msg = Lang.fileConfig.getString("target-offline");
                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                                    return true;
                                }
                            }
                        }
                    }else{
                        String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/spawn");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                        return true;
                    }
                } else {
                    // Sends Message if Spawn Doesn't Exist
                    String msg = Lang.fileConfig.getString("spawn-invalid");
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                }
            }
        }else if (sender instanceof ConsoleCommandSender){
            if (args.length == 1){
                Player target = Bukkit.getPlayerExact(args[0]);
                if (target != null){
                    // Check if the File Exists and if Location isn't null
                    if (!checkSpawn(sender)){
                        Location loc = getLocation();
                        spawnSave(target);
                        if (loc.isWorldLoaded()){
                            // Teleporting player to Location
                            target.teleport(loc);
                            // Sending the Sender and Target a message
                            String msg = Lang.fileConfig.getString("spawn-teleport-target").replace("<target>", target.getName());
                            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                            String msg2 = Lang.fileConfig.getString("spawn-teleport-target-success");
                            target.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg2)));
                            return true;
                        }else{
                            String msg = Lang.fileConfig.getString("spawn-world-invalid");
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                            return true;
                        }
                    }
                } else {
                    String msg = Lang.fileConfig.getString("target-offline");
                    Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                    return true;
                }
            }else if (args.length == 2 && args[1].equalsIgnoreCase("newbies")){
                Player target = Bukkit.getPlayerExact(args[0]);
                if (target != null){
                    // Check if the File Exists and if Location isn't null
                    if (!checkNewbiesSpawn(sender)){
                        Location loc = getNewbiesLocation();
                        spawnSave(target);
                        if (loc.isWorldLoaded()){
                            // Teleporting player to Location
                            target.teleport(loc);
                            // Sending the Sender and Target a message
                            String msg = Lang.fileConfig.getString("newbies-spawn-teleport-target").replace("<target>", target.getName());
                            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                            String msg2 = Lang.fileConfig.getString("newbies-spawn-teleport-target-success");
                            target.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg2)));
                            return true;
                        }else{
                            String msg = Lang.fileConfig.getString("spawn-world-invalid");
                            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                            return true;
                        }
                    }
                } else {
                    String msg = Lang.fileConfig.getString("target-offline");
                    Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                    return true;
                }
            }else{
                String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/spawn");
                Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                return true;
            }
        }
        return false;
    }
    public static Location getLocation() {
        // Gathering Location
        if (Setspawn.file.exists() && Setspawn.fileConfig.getString("Location.World") != null){
            float yaw = Setspawn.fileConfig.getInt("Location.Yaw");
            float pitch = Setspawn.fileConfig.getInt("Location.Pitch");
            Location loc = new Location(Bukkit.getWorld(Setspawn.fileConfig.getString("Location.World")), Setspawn.fileConfig.getDouble("Location.X"), Setspawn.fileConfig.getDouble("Location.Y"), Setspawn.fileConfig.getDouble("Location.Z"), yaw, pitch);
            return loc;
        }
        return null;
    }
    public static Location getNewbiesLocation() {
        // Gathering Location
        if (Setspawn.file.exists() && Setspawn.fileConfig.getString("Newbies.Location.World") != null){
            float yaw = Setspawn.fileConfig.getInt("Newbies.Location.Yaw");
            float pitch = Setspawn.fileConfig.getInt("Newbies.Location.Pitch");
            Location loc = new Location(Bukkit.getWorld(Setspawn.fileConfig.getString("Newbies.Location.World")), Setspawn.fileConfig.getDouble("Newbies.Location.X"), Setspawn.fileConfig.getDouble("Newbies.Location.Y"), Setspawn.fileConfig.getDouble("Newbies.Location.Z"), yaw, pitch);
            return loc;
        }
        return null;
    }

    public boolean checkSpawn(CommandSender sender){
        boolean spawnInvalid;
        if (Setspawn.file.exists() && getLocation() != null) {
            spawnInvalid = false;
        }else{
            spawnInvalid = true;
            String msg = Lang.fileConfig.getString("spawn-invalid");
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
        }
        return spawnInvalid;
    }

    public boolean checkNewbiesSpawn(CommandSender sender){
        boolean spawnInvalid;
        if (Setspawn.file.exists() && getNewbiesLocation() != null) {
            spawnInvalid = false;
        }else{
            spawnInvalid = true;
            String msg = Lang.fileConfig.getString("newbies-spawn-invalid");
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
        }
        return spawnInvalid;
    }

    public static void spawnSave(Player player){
        if (ServerEssentials.plugin.getConfig().getBoolean("spawn-save")) {
            if (Back.location.containsKey(player.getUniqueId())) {
                Back.location.remove(player.getUniqueId());
                Back.location.put(player.getUniqueId(), player.getLocation());
            } else {
                Back.location.put(player.getUniqueId(), player.getLocation());
            }
        } else if (player.hasPermission("se.back.bypass")) {
            if (Back.location.containsKey(player.getUniqueId())) {
                Back.location.remove(player.getUniqueId());
                Back.location.put(player.getUniqueId(), player.getLocation());
            } else {
                Back.location.put(player.getUniqueId(), player.getLocation());
            }
        }
    }

    public static void spawnTeleport(Player player, Location loc, String lang){
        if (loc.isWorldLoaded()) {
            // Teleporting Player
            player.teleport(loc);
            String msg = Lang.fileConfig.getString(lang);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
        } else {
            String msg = Lang.fileConfig.getString("spawn-world-invalid");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
        }
        cancel.remove(player.getUniqueId());
        spawnteleport.remove(player.getUniqueId());
    }
}