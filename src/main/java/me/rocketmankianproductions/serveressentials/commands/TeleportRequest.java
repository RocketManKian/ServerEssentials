package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TeleportRequest implements CommandExecutor {

    public static HashMap<UUID, UUID> tpa = new HashMap<>();
    public static HashMap<UUID, UUID> tpahere = new HashMap<>();
    public static HashMap<UUID, Integer> teleportcancel = new HashMap<>();
    public static HashMap<UUID, Integer> teleport = new HashMap<>();
    public static ArrayList<UUID> cancel = new ArrayList<>();

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        int tpwait = ServerEssentials.plugin.getConfig().getInt("teleport-wait");
        Long delay = ServerEssentials.getPlugin().getConfig().getLong("teleport-cancel");
        int delay2 = (int) (delay * 20);
        int delay3 = delay2 / 20;
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You must be a player.");
            return true;
        }
        Player player = (Player) sender;
        if (command.getName().equalsIgnoreCase("tpa")) {
            if (!player.hasPermission("se.tpa")) {
                if (ServerEssentials.plugin.getConfig().getString("no-permission-message").length() == 0) {
                    player.sendMessage(ChatColor.RED + "You do not have the required permission (se.tpa) to run this command.");
                    return true;
                } else {
                    String permission = ServerEssentials.getPlugin().getConfig().getString("no-permission-message");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', permission));
                }
                return true;
            }
            if (args.length == 1) {
                Player target = Bukkit.getPlayer(args[0]);
                Boolean blacklistedworld = ServerEssentials.plugin.getConfig().getBoolean("enable-teleport-blacklist");
                if (target != null) {
                    if (target != player) {
                        if (blacklistedworld){
                            for (String worlds : ServerEssentials.plugin.getConfig().getStringList("teleport-blacklist")){
                                if (target.getWorld().getName().equalsIgnoreCase(worlds)){
                                    player.sendMessage(ChatColor.RED + "Target is in a Blacklisted World");
                                    return true;
                                }
                            }
                            if (TPToggle.fileConfig.getBoolean("tptoggle." + target.getName(), false) == false) {
                                tpa.put(target.getUniqueId(), player.getUniqueId());
                                player.sendMessage(ChatColor.GOLD + "You sent a teleport request to " + ChatColor.WHITE + target.getName() + ".");
                                player.sendMessage(ChatColor.GOLD + "To cancel this request, type " + ChatColor.RED + "/tpacancel");
                                target.sendMessage(ChatColor.WHITE + player.getName() + ChatColor.GOLD + " sent a teleport request to you.");
                                target.sendMessage(ChatColor.GOLD + "To accept, type" + ChatColor.RED + " /tpaccept.");
                                target.sendMessage(ChatColor.GOLD + "To deny, type " + ChatColor.RED + "/tpdeny.");
                                target.sendMessage(ChatColor.GOLD + "This request will timeout in " + ChatColor.RED + delay3 + " seconds");
                                if (teleportcancel.containsKey(target.getUniqueId()) && teleportcancel.get(target.getUniqueId()) != null) {
                                    cancelTimeout(target);
                                }
                                teleportcancel.put(target.getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((ServerEssentials.getPlugin()), new Runnable() {
                                    public void run() {
                                        if (tpa.containsKey(target.getUniqueId())) {
                                            target.sendMessage(ChatColor.RED + "Teleport request timed out");
                                            tpa.remove(target.getUniqueId());
                                        }
                                    }
                                }, delay2));
                                return true;
                            } else {
                                player.sendMessage(ChatColor.RED + "That person has Teleporting disabled.");
                                return true;
                            }
                        }else{
                            if (TPToggle.fileConfig.getBoolean("tptoggle." + target.getName(), false) == false) {
                                tpa.put(target.getUniqueId(), player.getUniqueId());
                                player.sendMessage(ChatColor.GOLD + "You sent a teleport request to " + ChatColor.WHITE + target.getName() + ".");
                                player.sendMessage(ChatColor.GOLD + "To cancel this request, type " + ChatColor.RED + "/tpacancel");
                                target.sendMessage(ChatColor.WHITE + player.getName() + ChatColor.GOLD + " sent a teleport request to you.");
                                target.sendMessage(ChatColor.GOLD + "To accept, type" + ChatColor.RED + " /tpaccept.");
                                target.sendMessage(ChatColor.GOLD + "To deny, type " + ChatColor.RED + "/tpdeny.");
                                target.sendMessage(ChatColor.GOLD + "This request will timeout in " + ChatColor.RED + delay3 + " seconds");
                                if (teleportcancel.containsKey(target.getUniqueId()) && teleportcancel.get(target.getUniqueId()) != null) {
                                    cancelTimeout(target);
                                }
                                teleportcancel.put(target.getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((ServerEssentials.getPlugin()), new Runnable() {
                                    public void run() {
                                        if (tpa.containsKey(target.getUniqueId())) {
                                            target.sendMessage(ChatColor.RED + "Teleport request timed out");
                                            tpa.remove(target.getUniqueId());
                                        }
                                    }
                                }, delay2));
                                return true;
                            } else {
                                player.sendMessage(ChatColor.RED + "That person has Teleporting disabled.");
                                return true;
                            }
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "You cannot teleport to yourself!");
                        return true;
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "§cThat player is offline.");
                    return true;
                }
            }
        }
        if (command.getName().equalsIgnoreCase("tpahere")) {
            if (!player.hasPermission("se.tpahere")) {
                if (ServerEssentials.plugin.getConfig().getString("no-permission-message").length() == 0) {
                    player.sendMessage(ChatColor.RED + "You do not have the required permission (se.tpahere) to run this command.");
                    return true;
                } else {
                    String permission = ServerEssentials.getPlugin().getConfig().getString("no-permission-message");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', permission));
                    return true;
                }
            }
            if (args.length == 1) {
                Player target = Bukkit.getPlayer(args[0]);
                Boolean blacklistedworld = ServerEssentials.plugin.getConfig().getBoolean("enable-teleport-blacklist");
                if (target != null) {
                    if (target != player) {
                        if (blacklistedworld){
                            for (String worlds : ServerEssentials.plugin.getConfig().getStringList("teleport-blacklist")){
                                if (player.getWorld().getName().equalsIgnoreCase(worlds)){
                                    player.sendMessage(ChatColor.RED + "Cannot send Teleport Here Request because you are in a Blacklisted World");
                                    return true;
                                }
                            }
                            if (TPToggle.fileConfig.getBoolean("tptoggle." + target.getName(), false) == false) {
                                tpahere.put(target.getUniqueId(), player.getUniqueId());
                                player.sendMessage(ChatColor.GOLD + "You sent a teleport here request to " + ChatColor.WHITE + target.getName() + ".");
                                player.sendMessage(ChatColor.GOLD + "To cancel this request, type " + ChatColor.RED + "/tpacancel");
                                target.sendMessage(ChatColor.WHITE + player.getName() + ChatColor.GOLD + " would like you to teleport to them");
                                target.sendMessage(ChatColor.GOLD + "To accept, type" + ChatColor.RED + " /tpaccept.");
                                target.sendMessage(ChatColor.GOLD + "To deny, type " + ChatColor.RED + "/tpdeny.");
                                target.sendMessage(ChatColor.GOLD + "This request will timeout in " + ChatColor.RED + delay3 + " seconds");
                                if (teleportcancel.containsKey(target.getUniqueId()) && teleportcancel.get(target.getUniqueId()) != null) {
                                    cancelTimeout(target);
                                }
                                teleportcancel.put(target.getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((ServerEssentials.getPlugin()), new Runnable() {
                                    public void run() {
                                        if (tpahere.containsKey(target.getUniqueId())) {
                                            target.sendMessage(ChatColor.RED + "Teleport request timed out");
                                            tpahere.remove(target.getUniqueId());
                                        }
                                    }
                                }, delay2));
                                return true;
                            } else {
                                player.sendMessage(ChatColor.RED + "That person has Teleporting disabled.");
                                return true;
                            }
                        }else{
                            if (TPToggle.fileConfig.getBoolean("tptoggle." + target.getName(), false) == false) {
                                tpahere.put(target.getUniqueId(), player.getUniqueId());
                                player.sendMessage(ChatColor.GOLD + "You sent a teleport here request to " + ChatColor.WHITE + target.getName() + ".");
                                player.sendMessage(ChatColor.GOLD + "To cancel this request, type " + ChatColor.RED + "/tpacancel");
                                target.sendMessage(ChatColor.WHITE + player.getName() + ChatColor.GOLD + " would like you to teleport to them");
                                target.sendMessage(ChatColor.GOLD + "To accept, type" + ChatColor.RED + " /tpaccept.");
                                target.sendMessage(ChatColor.GOLD + "To deny, type " + ChatColor.RED + "/tpdeny.");
                                target.sendMessage(ChatColor.GOLD + "This request will timeout in " + ChatColor.RED + delay3 + " seconds");
                                if (teleportcancel.containsKey(target.getUniqueId()) && teleportcancel.get(target.getUniqueId()) != null) {
                                    cancelTimeout(target);
                                }
                                teleportcancel.put(target.getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((ServerEssentials.getPlugin()), new Runnable() {
                                    public void run() {
                                        if (tpahere.containsKey(target.getUniqueId())) {
                                            target.sendMessage(ChatColor.RED + "Teleport request timed out");
                                            tpahere.remove(target.getUniqueId());
                                        }
                                    }
                                }, delay2));
                                return true;
                            } else {
                                player.sendMessage(ChatColor.RED + "That person has Teleporting disabled.");
                                return true;
                            }
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "You cannot teleport to yourself!");
                        return true;
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "§cThat player is offline.");
                    return true;
                }
            }
        }
        if (command.getName().equalsIgnoreCase("tpacancel")) {
            if (player.hasPermission("se.tpacancel")) {
                if (tpa.containsKey(getKey(tpa, player.getUniqueId())) || tpahere.containsKey(getKey(tpahere, player.getUniqueId()))) {
                    player.sendMessage(ChatColor.GOLD + "You have cancelled all outgoing Teleport Requests");
                    if (teleportcancel.containsKey(player.getUniqueId()) && teleportcancel.get(player.getUniqueId()) != null){
                        Bukkit.getScheduler().cancelTask(teleportcancel.get(getKey(tpa, player.getUniqueId())));
                        Bukkit.getScheduler().cancelTask(teleportcancel.get(getKey(tpahere, player.getUniqueId())));
                    }
                    tpa.remove(getKey(tpa, player.getUniqueId()));
                    tpahere.remove(getKey(tpahere, player.getUniqueId()));
                    return true;
                } else if (!tpahere.containsKey(player.getUniqueId()) || !tpa.containsKey(player.getUniqueId())) {
                    player.sendMessage(ChatColor.RED + "There's no request to cancel.");
                    return true;
                }
            } else {
                if (ServerEssentials.plugin.getConfig().getString("no-permission-message").length() == 0) {
                    player.sendMessage(ChatColor.RED + "You do not have the required permission (se.tpacancel) to run this command.");
                    return true;
                } else {
                    String permission = ServerEssentials.getPlugin().getConfig().getString("no-permission-message");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', permission));
                }
                return true;
            }
        }
        if (command.getName().equalsIgnoreCase("tpaccept")) {
            if (player.hasPermission("se.tpaccept")) {
                if (ServerEssentials.plugin.getConfig().getInt("teleport-wait") == 0){
                    if (tpa.containsKey(player.getUniqueId())) {
                        if (ServerEssentials.plugin.getConfig().getBoolean("teleport-save")){
                            if (Back.location.containsKey(Bukkit.getPlayer(tpa.get(player.getUniqueId())))){
                                Back.location.remove((Bukkit.getPlayer(tpa.get(player.getUniqueId()))));
                                Back.location.put((Bukkit.getPlayer(tpa.get(player.getUniqueId()))).getUniqueId(), (Bukkit.getPlayer(tpa.get(player.getUniqueId()))).getLocation());
                            }else{
                                Back.location.put((Bukkit.getPlayer(tpa.get(player.getUniqueId()))).getUniqueId(), (Bukkit.getPlayer(tpa.get(player.getUniqueId()))).getLocation());
                            }
                        }else if (player.hasPermission("se.back.bypass")){
                            if (Back.location.containsKey(Bukkit.getPlayer(tpa.get(player.getUniqueId())))){
                                Back.location.remove((Bukkit.getPlayer(tpa.get(player.getUniqueId()))));
                                Back.location.put((Bukkit.getPlayer(tpa.get(player.getUniqueId()))).getUniqueId(), (Bukkit.getPlayer(tpa.get(player.getUniqueId()))).getLocation());
                            }else{
                                Back.location.put((Bukkit.getPlayer(tpa.get(player.getUniqueId()))).getUniqueId(), (Bukkit.getPlayer(tpa.get(player.getUniqueId()))).getLocation());
                            }
                        }
                        Player target = Bukkit.getPlayer(tpa.get(player.getUniqueId()));
                        // Teleporting Player
                        Bukkit.getPlayer(tpa.get(player.getUniqueId())).teleport(player);
                        tpa.remove(player.getUniqueId());
                        cancelTimeout(player);
                        player.sendMessage(ChatColor.WHITE + target.getName() + ChatColor.GREEN + " has teleported to you");
                        target.sendMessage(ChatColor.WHITE + player.getName() + ChatColor.GREEN + " has accepted the teleport request.");
                        return true;
                    } else if (tpahere.containsKey(player.getUniqueId())) {
                        if (ServerEssentials.plugin.getConfig().getBoolean("teleport-save")){
                            if (Back.location.containsKey(player.getUniqueId())){
                                Back.location.remove(player.getUniqueId());
                                Back.location.put(player.getUniqueId(), player.getLocation());
                            }else{
                                Back.location.put(player.getUniqueId(), player.getLocation());
                            }
                        }else if (player.hasPermission("se.back.bypass")){
                            if (Back.location.containsKey(Bukkit.getPlayer(tpahere.get(player.getUniqueId())))){
                                Back.location.remove((Bukkit.getPlayer(tpahere.get(player.getUniqueId()))));
                                Back.location.put((Bukkit.getPlayer(tpahere.get(player.getUniqueId()))).getUniqueId(), (Bukkit.getPlayer(tpahere.get(player.getUniqueId()))).getLocation());
                            }else{
                                Back.location.put((Bukkit.getPlayer(tpahere.get(player.getUniqueId()))).getUniqueId(), (Bukkit.getPlayer(tpahere.get(player.getUniqueId()))).getLocation());
                            }
                        }
                        Player target = Bukkit.getPlayer(tpahere.get(player.getUniqueId()));
                        target.sendMessage(ChatColor.WHITE + player.getName() + ChatColor.GREEN + " has teleported to you");
                        player.sendMessage(ChatColor.GREEN + "Successfully teleported to " + ChatColor.WHITE + target.getName());
                        player.teleport(Bukkit.getPlayer(tpahere.get(player.getUniqueId())));
                        cancelTimeout(player);
                        tpahere.remove(player.getUniqueId());
                        return true;
                    } else if (tpahere.get(player.getUniqueId()) == null || tpa.get(player.getUniqueId()) == null) {
                        player.sendMessage(ChatColor.RED + "There's no request to accept.");
                        return true;
                    }
                }else{
                    if (ServerEssentials.plugin.getConfig().getBoolean("teleport-movement-cancel")){
                        if (tpa.containsKey(player.getUniqueId())) {
                            Player target = Bukkit.getPlayer(tpa.get(player.getUniqueId()));
                            cancelTimeout(player);
                            cancel.add(target.getUniqueId());
                            player.sendMessage(ChatColor.GREEN + "Successfully accepted " + ChatColor.WHITE + target.getName() + "'s " + ChatColor.GREEN + "Teleport Request");
                            target.sendMessage(ChatColor.GREEN + "Teleporting to " + ChatColor.WHITE + player.getName() + ChatColor.GREEN + " in " + ChatColor.GOLD + tpwait + " Seconds");
                            tpwait = tpwait * 20;
                            if (teleport.containsKey(player.getUniqueId()) && teleport.get(player.getUniqueId()) != null) {
                                Bukkit.getScheduler().cancelTask(teleport.get(player.getUniqueId()));
                            }
                            teleport.put(player.getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((ServerEssentials.plugin), new Runnable() {
                                public void run() {
                                    if (cancel.contains(target.getUniqueId())){
                                        if (teleport.containsKey(player.getUniqueId())) {
                                            if (ServerEssentials.plugin.getConfig().getBoolean("teleport-save")){
                                                if (Back.location.containsKey(Bukkit.getPlayer(tpa.get(player.getUniqueId())))){
                                                    Back.location.remove((Bukkit.getPlayer(tpa.get(player.getUniqueId()))));
                                                    Back.location.put((Bukkit.getPlayer(tpa.get(player.getUniqueId()))).getUniqueId(), (Bukkit.getPlayer(tpa.get(player.getUniqueId()))).getLocation());
                                                }else{
                                                    Back.location.put((Bukkit.getPlayer(tpa.get(player.getUniqueId()))).getUniqueId(), (Bukkit.getPlayer(tpa.get(player.getUniqueId()))).getLocation());
                                                }
                                            }else if (player.hasPermission("se.back.bypass")){
                                                if (Back.location.containsKey(Bukkit.getPlayer(tpa.get(player.getUniqueId())))){
                                                    Back.location.remove((Bukkit.getPlayer(tpa.get(player.getUniqueId()))));
                                                    Back.location.put((Bukkit.getPlayer(tpa.get(player.getUniqueId()))).getUniqueId(), (Bukkit.getPlayer(tpa.get(player.getUniqueId()))).getLocation());
                                                }else{
                                                    Back.location.put((Bukkit.getPlayer(tpa.get(player.getUniqueId()))).getUniqueId(), (Bukkit.getPlayer(tpa.get(player.getUniqueId()))).getLocation());
                                                }
                                            }
                                            // Teleporting Player
                                            target.sendMessage(ChatColor.GREEN + "Successfully teleported to " + ChatColor.WHITE + player.getName());
                                            player.sendMessage(ChatColor.GOLD + target.getName() + ChatColor.WHITE + " has teleported to you");
                                            Bukkit.getPlayer(tpa.get(player.getUniqueId())).teleport(player);
                                            teleport.remove(player.getUniqueId());
                                            cancel.remove(target.getUniqueId());
                                            tpa.remove(player.getUniqueId());
                                        }
                                    }
                                }
                            }, tpwait));
                            return true;
                        } else if (tpahere.containsKey(player.getUniqueId())) {
                            Player target = Bukkit.getPlayer(tpahere.get(player.getUniqueId()));
                            cancelTimeout(player);
                            cancel.add(player.getUniqueId());
                            Bukkit.getPlayer(tpahere.get(player.getUniqueId())).sendMessage(ChatColor.WHITE + player.getName() + ChatColor.GREEN + " accepted the teleport request.");
                            player.sendMessage(ChatColor.GREEN + "Teleporting to " + ChatColor.WHITE + target.getName() + ChatColor.GREEN + " in " + ChatColor.GOLD + tpwait + " Seconds");
                            tpwait = tpwait * 20;
                            if (teleport.containsKey(player.getUniqueId()) && teleport.get(player.getUniqueId()) != null) {
                                Bukkit.getScheduler().cancelTask(teleport.get(player.getUniqueId()));
                            }
                            teleport.put(player.getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((ServerEssentials.plugin), new Runnable() {
                                public void run() {
                                    if (cancel.contains(player.getUniqueId())){
                                        if (teleport.containsKey(player.getUniqueId())) {
                                            if (ServerEssentials.plugin.getConfig().getBoolean("teleport-save")){
                                                if (Back.location.containsKey(player.getUniqueId())){
                                                    Back.location.remove(player.getUniqueId());
                                                    Back.location.put(player.getUniqueId(), player.getLocation());
                                                }else{
                                                    Back.location.put(player.getUniqueId(), player.getLocation());
                                                }
                                            }else if (player.hasPermission("se.back.bypass")){
                                                if (Back.location.containsKey(Bukkit.getPlayer(tpahere.get(player.getUniqueId())))){
                                                    Back.location.remove((Bukkit.getPlayer(tpahere.get(player.getUniqueId()))));
                                                    Back.location.put((Bukkit.getPlayer(tpahere.get(player.getUniqueId()))).getUniqueId(), (Bukkit.getPlayer(tpahere.get(player.getUniqueId()))).getLocation());
                                                }else{
                                                    Back.location.put((Bukkit.getPlayer(tpahere.get(player.getUniqueId()))).getUniqueId(), (Bukkit.getPlayer(tpahere.get(player.getUniqueId()))).getLocation());
                                                }
                                            }
                                            target.sendMessage(ChatColor.WHITE + player.getName() + ChatColor.GREEN + " has teleported to you");
                                            player.sendMessage(ChatColor.GREEN + "Successfully teleported to " + ChatColor.WHITE + target.getName());
                                            player.teleport(Bukkit.getPlayer(tpahere.get(player.getUniqueId())));
                                            cancel.remove(player.getUniqueId());
                                            teleport.remove(player.getUniqueId());
                                            tpahere.remove(target.getUniqueId());
                                        }
                                    }
                                }
                            }, tpwait));
                            return true;
                        } else if (tpahere.get(player.getUniqueId()) == null || tpa.get(player.getUniqueId()) == null) {
                            player.sendMessage(ChatColor.RED + "There's no request to accept.");
                            return true;
                        }
                    }else{
                        if (tpa.containsKey(player.getUniqueId())) {
                            Player target = Bukkit.getPlayer(tpa.get(player.getUniqueId()));
                            cancelTimeout(player);
                            player.sendMessage(ChatColor.GREEN + "Successfully accepted " + ChatColor.WHITE + target.getName() + "'s " + ChatColor.GREEN + "Teleport Request");
                            target.sendMessage(ChatColor.GREEN + "Teleporting to " + ChatColor.WHITE + player.getName() + ChatColor.GREEN + " in " + ChatColor.GOLD + tpwait + " Seconds");
                            tpwait = tpwait * 20;
                            if (teleport.containsKey(player.getUniqueId()) && teleport.get(player.getUniqueId()) != null) {
                                Bukkit.getScheduler().cancelTask(teleport.get(player.getUniqueId()));
                            }
                            teleport.put(player.getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((ServerEssentials.plugin), new Runnable() {
                                public void run() {
                                    if (teleport.containsKey(player.getUniqueId())) {
                                        if (ServerEssentials.plugin.getConfig().getBoolean("teleport-save")) {
                                            if (Back.location.containsKey(Bukkit.getPlayer(tpa.get(player.getUniqueId())))) {
                                                Back.location.remove((Bukkit.getPlayer(tpa.get(player.getUniqueId()))));
                                                Back.location.put((Bukkit.getPlayer(tpa.get(player.getUniqueId()))).getUniqueId(), (Bukkit.getPlayer(tpa.get(player.getUniqueId()))).getLocation());
                                            } else {
                                                Back.location.put((Bukkit.getPlayer(tpa.get(player.getUniqueId()))).getUniqueId(), (Bukkit.getPlayer(tpa.get(player.getUniqueId()))).getLocation());
                                            }
                                        } else if (player.hasPermission("se.back.bypass")) {
                                            if (Back.location.containsKey(Bukkit.getPlayer(tpa.get(player.getUniqueId())))) {
                                                Back.location.remove((Bukkit.getPlayer(tpa.get(player.getUniqueId()))));
                                                Back.location.put((Bukkit.getPlayer(tpa.get(player.getUniqueId()))).getUniqueId(), (Bukkit.getPlayer(tpa.get(player.getUniqueId()))).getLocation());
                                            } else {
                                                Back.location.put((Bukkit.getPlayer(tpa.get(player.getUniqueId()))).getUniqueId(), (Bukkit.getPlayer(tpa.get(player.getUniqueId()))).getLocation());
                                            }
                                        }
                                        // Teleporting Player
                                        player.sendMessage(ChatColor.WHITE + target.getName() + ChatColor.GREEN + " has teleported to you");
                                        target.sendMessage(ChatColor.GREEN + "Successfully teleported to " + ChatColor.WHITE + player.getName());
                                        Bukkit.getPlayer(tpa.get(player.getUniqueId())).teleport(player);
                                        teleport.remove(player.getUniqueId());
                                        tpa.remove(player.getUniqueId());
                                    }
                                }
                            }, tpwait));
                            return true;
                        } else if (tpahere.containsKey(player.getUniqueId())) {
                            Player target = Bukkit.getPlayer(tpahere.get(player.getUniqueId()));
                            cancelTimeout(player);
                            player.sendMessage(ChatColor.GREEN + "Teleporting to " + ChatColor.WHITE + target.getName() + ChatColor.GREEN + " in " + ChatColor.GOLD + tpwait + " Seconds");
                            Bukkit.getPlayer(tpahere.get(player.getUniqueId())).sendMessage(ChatColor.WHITE + player.getName() + ChatColor.GREEN + " accepted the teleport request.");
                            tpwait = tpwait * 20;
                            if (teleport.containsKey(player.getUniqueId()) && teleport.get(player.getUniqueId()) != null) {
                                Bukkit.getScheduler().cancelTask(teleport.get(player.getUniqueId()));
                            }
                            teleport.put(player.getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((ServerEssentials.plugin), new Runnable() {
                                public void run() {
                                    if (teleport.containsKey(player.getUniqueId())) {
                                        if (ServerEssentials.plugin.getConfig().getBoolean("teleport-save")) {
                                            if (Back.location.containsKey(player.getUniqueId())) {
                                                Back.location.remove(player.getUniqueId());
                                                Back.location.put(player.getUniqueId(), player.getLocation());
                                            } else {
                                                Back.location.put(player.getUniqueId(), player.getLocation());
                                            }
                                        } else if (player.hasPermission("se.back.bypass")) {
                                            if (Back.location.containsKey(Bukkit.getPlayer(tpahere.get(player.getUniqueId())))) {
                                                Back.location.remove((Bukkit.getPlayer(tpahere.get(player.getUniqueId()))));
                                                Back.location.put((Bukkit.getPlayer(tpahere.get(player.getUniqueId()))).getUniqueId(), (Bukkit.getPlayer(tpahere.get(player.getUniqueId()))).getLocation());
                                            } else {
                                                Back.location.put((Bukkit.getPlayer(tpahere.get(player.getUniqueId()))).getUniqueId(), (Bukkit.getPlayer(tpahere.get(player.getUniqueId()))).getLocation());
                                            }
                                        }
                                        target.sendMessage(ChatColor.WHITE + player.getName() + ChatColor.GREEN + " has teleported to you");
                                        player.sendMessage(ChatColor.GREEN + "Successfully teleported to " + ChatColor.WHITE + target.getName());
                                        player.teleport(Bukkit.getPlayer(tpahere.get(player.getUniqueId())));
                                        tpahere.remove(player.getUniqueId());
                                        teleport.remove(player.getUniqueId());
                                        tpahere.remove(target.getUniqueId());
                                    }
                                }
                            }, tpwait));
                            return true;
                        } else if (tpahere.get(player.getUniqueId()) == null || tpa.get(player.getUniqueId()) == null) {
                            player.sendMessage(ChatColor.RED + "There's no request to accept.");
                            return true;
                        }
                    }
                }
            } else {
                if (ServerEssentials.plugin.getConfig().getString("no-permission-message").length() == 0) {
                    player.sendMessage(ChatColor.RED + "You do not have the required permission (se.tpaccept) to run this command.");
                    return true;
                } else {
                    String permission = ServerEssentials.getPlugin().getConfig().getString("no-permission-message");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', permission));
                }
                return true;
            }
        }
        if (command.getName().equalsIgnoreCase("tpdeny")) {
            if (player.hasPermission("se.tpdeny")) {
                if (tpa.containsKey(player.getUniqueId())) {
                    player.sendMessage(ChatColor.RED + "You denied the teleport request.");
                    Bukkit.getPlayer(tpa.get(player.getUniqueId())).sendMessage(ChatColor.WHITE + player.getName() + ChatColor.RED + " denied the teleport request.");
                    if (teleportcancel.containsKey(player.getUniqueId()) && teleportcancel.get(player.getUniqueId()) != null){
                        Bukkit.getScheduler().cancelTask(teleportcancel.get(player.getUniqueId()));
                    }
                    tpa.remove(player.getUniqueId());
                    return true;
                } else if (tpahere.containsKey(player.getUniqueId())) {
                    player.sendMessage(ChatColor.RED + "You denied the teleport request.");
                    Bukkit.getPlayer(tpahere.get(player.getUniqueId())).sendMessage(ChatColor.WHITE + player.getName() + ChatColor.RED + " denied the teleport request.");
                    if (teleportcancel.containsKey(player.getUniqueId()) && teleportcancel.get(player.getUniqueId()) != null){
                        Bukkit.getScheduler().cancelTask(teleportcancel.get(player.getUniqueId()));
                    }
                    tpahere.remove(player.getUniqueId());
                    return true;
                } else if (!tpahere.containsKey(player.getUniqueId()) || !tpa.containsKey(player.getUniqueId())) {
                    player.sendMessage(ChatColor.YELLOW + "§cThere's no request to deny.");
                    return true;
                }
            } else {
                if (ServerEssentials.plugin.getConfig().getString("no-permission-message").length() == 0) {
                    player.sendMessage(ChatColor.RED + "You do not have the required permission (se.tpdeny) to run this command.");
                    return true;
                } else {
                    String permission = ServerEssentials.getPlugin().getConfig().getString("no-permission-message");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', permission));
                }
                return true;
            }
        }
        return true;
    }

    public <K, V> K getKey(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static void cancelTimeout (Player target){
        Bukkit.getScheduler().cancelTask(teleportcancel.get(target.getUniqueId()));
    }
}