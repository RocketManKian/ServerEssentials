package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
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
            String msg = Lang.fileConfig.getString("invalid-player");
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
            return true;
        }
        Player player = (Player) sender;
        if (command.getName().equalsIgnoreCase("tpa")) {
            if (player.hasPermission("se.tpa") || player.hasPermission("se.all")) {
                if (args.length == 1) {
                    Player target = Bukkit.getPlayer(args[0]);
                    Boolean blacklistedworld = ServerEssentials.plugin.getConfig().getBoolean("enable-teleport-blacklist");
                    if (target != null) {
                        if (target != player) {
                            if (blacklistedworld){
                                for (String worlds : ServerEssentials.plugin.getConfig().getStringList("teleport-blacklist")){
                                    if (target.getWorld().getName().equalsIgnoreCase(worlds)){
                                        String msg = Lang.fileConfig.getString("teleport-request-blacklisted-world");
                                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                        return true;
                                    }
                                }
                                if (TPToggle.fileConfig.getBoolean("tptoggle." + target.getName(), false) == false) {
                                    tpa.put(target.getUniqueId(), player.getUniqueId());
                                    String msg = Lang.fileConfig.getString("teleport-request-sent").replace("<target>", target.getName());
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                    String msg2 = Lang.fileConfig.getString("teleport-request-cancel-warning");
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg2));
                                    String msg3 = Lang.fileConfig.getString("teleport-request-target-receive").replace("<sender>", player.getName());
                                    target.sendMessage(ChatColor.translateAlternateColorCodes('&', msg3));
                                    String msg4 = Lang.fileConfig.getString("teleport-request-accept");
                                    target.sendMessage(ChatColor.translateAlternateColorCodes('&', msg4));
                                    String msg5 = Lang.fileConfig.getString("teleport-request-deny");
                                    target.sendMessage(ChatColor.translateAlternateColorCodes('&', msg5));
                                    String msg6 = Lang.fileConfig.getString("teleport-request-timeout-warning").replace("<time>", String.valueOf(delay3));
                                    target.sendMessage(ChatColor.translateAlternateColorCodes('&', msg6));
                                    if (teleportcancel.containsKey(target.getUniqueId()) && teleportcancel.get(target.getUniqueId()) != null) {
                                        cancelTimeout(target);
                                    }
                                    teleportcancel.put(target.getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((ServerEssentials.getPlugin()), new Runnable() {
                                        public void run() {
                                            if (tpa.containsKey(target.getUniqueId())) {
                                                String msg = Lang.fileConfig.getString("teleport-request-timeout");
                                                target.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                                tpa.remove(target.getUniqueId());
                                            }
                                        }
                                    }, delay2));
                                    return true;
                                } else {
                                    String msg = Lang.fileConfig.getString("teleport-disabled");
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                    return true;
                                }
                            }else{
                                if (TPToggle.fileConfig.getBoolean("tptoggle." + target.getName(), false) == false) {
                                    tpa.put(target.getUniqueId(), player.getUniqueId());
                                    String msg = Lang.fileConfig.getString("teleport-request-sent").replace("<target>", target.getName());
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                    String msg2 = Lang.fileConfig.getString("teleport-request-cancel-warning");
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg2));
                                    String msg3 = Lang.fileConfig.getString("teleport-request-target-receive").replace("<sender>", player.getName());
                                    target.sendMessage(ChatColor.translateAlternateColorCodes('&', msg3));
                                    String msg4 = Lang.fileConfig.getString("teleport-request-accept");
                                    target.sendMessage(ChatColor.translateAlternateColorCodes('&', msg4));
                                    String msg5 = Lang.fileConfig.getString("teleport-request-deny");
                                    target.sendMessage(ChatColor.translateAlternateColorCodes('&', msg5));
                                    String msg6 = Lang.fileConfig.getString("teleport-request-timeout-warning").replace("<time>", String.valueOf(delay3));
                                    target.sendMessage(ChatColor.translateAlternateColorCodes('&', msg6));
                                    if (teleportcancel.containsKey(target.getUniqueId()) && teleportcancel.get(target.getUniqueId()) != null) {
                                        cancelTimeout(target);
                                    }
                                    teleportcancel.put(target.getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((ServerEssentials.getPlugin()), new Runnable() {
                                        public void run() {
                                            if (tpa.containsKey(target.getUniqueId())) {
                                                String msg = Lang.fileConfig.getString("teleport-request-timeout");
                                                target.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                                tpa.remove(target.getUniqueId());
                                            }
                                        }
                                    }, delay2));
                                    return true;
                                } else {
                                    String msg = Lang.fileConfig.getString("teleport-disabled");
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                    return true;
                                }
                            }
                        } else {
                            String msg = Lang.fileConfig.getString("teleport-self");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                            return true;
                        }
                    } else {
                        String msg = Lang.fileConfig.getString("target-offline");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    }
                }
            }else{
                String perm = Lang.fileConfig.getString("no-permission-message").replace("<permission>", "se.tpa");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', perm));
                return true;
            }
        }
        if (command.getName().equalsIgnoreCase("tpahere")) {
            if (!player.hasPermission("se.tpahere") || !player.hasPermission("se.all")) {
                String perm = Lang.fileConfig.getString("no-permission-message").replace("<permission>", "se.tpahere");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', perm));
                return true;
            }
            if (args.length == 1) {
                Player target = Bukkit.getPlayer(args[0]);
                Boolean blacklistedworld = ServerEssentials.plugin.getConfig().getBoolean("enable-teleport-blacklist");
                if (target != null) {
                    if (target != player) {
                        if (blacklistedworld){
                            for (String worlds : ServerEssentials.plugin.getConfig().getStringList("teleport-blacklist")){
                                if (player.getWorld().getName().equalsIgnoreCase(worlds)){
                                    String msg = Lang.fileConfig.getString("teleport-here-blacklisted-world");
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                    return true;
                                }
                            }
                            if (TPToggle.fileConfig.getBoolean("tptoggle." + target.getName(), false) == false) {
                                tpahere.put(target.getUniqueId(), player.getUniqueId());
                                String msg = Lang.fileConfig.getString("teleport-here-request-sent").replace("<target>", target.getName());
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                String msg2 = Lang.fileConfig.getString("teleport-here-request-cancel-warning");
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg2));
                                String msg3 = Lang.fileConfig.getString("teleport-here-request-target-receive").replace("<sender>", player.getName());
                                target.sendMessage(ChatColor.translateAlternateColorCodes('&', msg3));
                                String msg4 = Lang.fileConfig.getString("teleport-here-request-accept");
                                target.sendMessage(ChatColor.translateAlternateColorCodes('&', msg4));
                                String msg5 = Lang.fileConfig.getString("teleport-here-request-deny");
                                target.sendMessage(ChatColor.translateAlternateColorCodes('&', msg5));
                                String msg6 = Lang.fileConfig.getString("teleport-here-request-timeout-warning").replace("<time>", String.valueOf(delay3));
                                target.sendMessage(ChatColor.translateAlternateColorCodes('&', msg6));
                                if (teleportcancel.containsKey(target.getUniqueId()) && teleportcancel.get(target.getUniqueId()) != null) {
                                    cancelTimeout(target);
                                }
                                teleportcancel.put(target.getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((ServerEssentials.getPlugin()), new Runnable() {
                                    public void run() {
                                        if (tpahere.containsKey(target.getUniqueId())) {
                                            String msg = Lang.fileConfig.getString("teleport-here-request-timeout");
                                            target.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                            tpahere.remove(target.getUniqueId());
                                        }
                                    }
                                }, delay2));
                                return true;
                            } else {
                                String msg = Lang.fileConfig.getString("teleport-disabled");
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            }
                        }else{
                            if (TPToggle.fileConfig.getBoolean("tptoggle." + target.getName(), false) == false) {
                                tpahere.put(target.getUniqueId(), player.getUniqueId());
                                String msg = Lang.fileConfig.getString("teleport-here-request-sent").replace("<target>", target.getName());
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                String msg2 = Lang.fileConfig.getString("teleport-here-request-cancel-warning");
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg2));
                                String msg3 = Lang.fileConfig.getString("teleport-here-request-target-receive").replace("<sender>", player.getName());
                                target.sendMessage(ChatColor.translateAlternateColorCodes('&', msg3));
                                String msg4 = Lang.fileConfig.getString("teleport-here-request-accept");
                                target.sendMessage(ChatColor.translateAlternateColorCodes('&', msg4));
                                String msg5 = Lang.fileConfig.getString("teleport-here-request-deny");
                                target.sendMessage(ChatColor.translateAlternateColorCodes('&', msg5));
                                String msg6 = Lang.fileConfig.getString("teleport-here-request-timeout-warning").replace("<time>", String.valueOf(delay3));
                                target.sendMessage(ChatColor.translateAlternateColorCodes('&', msg6));
                                if (teleportcancel.containsKey(target.getUniqueId()) && teleportcancel.get(target.getUniqueId()) != null) {
                                    cancelTimeout(target);
                                }
                                teleportcancel.put(target.getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((ServerEssentials.getPlugin()), new Runnable() {
                                    public void run() {
                                        if (tpahere.containsKey(target.getUniqueId())) {
                                            String msg = Lang.fileConfig.getString("teleport-here-request-timeout");
                                            target.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                            tpahere.remove(target.getUniqueId());
                                        }
                                    }
                                }, delay2));
                                return true;
                            } else {
                                String msg = Lang.fileConfig.getString("teleport-disabled");
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            }
                        }
                    } else {
                        String msg = Lang.fileConfig.getString("teleport-self");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    }
                } else {
                    String msg = Lang.fileConfig.getString("target-offline");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    return true;
                }
            }
        }
        if (command.getName().equalsIgnoreCase("tpacancel")) {
            if (player.hasPermission("se.tpacancel") || player.hasPermission("se.all")) {
                if (tpa.containsKey(getKey(tpa, player.getUniqueId())) || tpahere.containsKey(getKey(tpahere, player.getUniqueId()))) {
                    String msg = Lang.fileConfig.getString("teleport-cancel");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    if (teleportcancel.containsKey(player.getUniqueId()) && teleportcancel.get(player.getUniqueId()) != null){
                        Bukkit.getScheduler().cancelTask(teleportcancel.get(getKey(tpa, player.getUniqueId())));
                        Bukkit.getScheduler().cancelTask(teleportcancel.get(getKey(tpahere, player.getUniqueId())));
                    }
                    tpa.remove(getKey(tpa, player.getUniqueId()));
                    tpahere.remove(getKey(tpahere, player.getUniqueId()));
                    return true;
                } else if (!tpahere.containsKey(player.getUniqueId()) || !tpa.containsKey(player.getUniqueId())) {
                    String msg = Lang.fileConfig.getString("teleport-no-request");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    return true;
                }
            } else {
                String perm = Lang.fileConfig.getString("no-permission-message").replace("<permission>", "se.tpacancel");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', perm));
                return true;
            }
        }
        if (command.getName().equalsIgnoreCase("tpaccept")) {
            if (player.hasPermission("se.tpaccept") || player.hasPermission("se.all")) {
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
                        String msg = Lang.fileConfig.getString("teleport-target-success").replace("<sender>", target.getName());
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        String msg2 = Lang.fileConfig.getString("teleport-accept-request").replace("<sender>", player.getName());
                        target.sendMessage(ChatColor.translateAlternateColorCodes('&', msg2));
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
                        String msg = Lang.fileConfig.getString("teleport-target-success").replace("<sender>", player.getName());
                        target.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        String msg2 = Lang.fileConfig.getString("teleport-success").replace("<target>", target.getName());
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg2));
                        player.teleport(Bukkit.getPlayer(tpahere.get(player.getUniqueId())));
                        cancelTimeout(player);
                        tpahere.remove(player.getUniqueId());
                        return true;
                    } else if (tpahere.get(player.getUniqueId()) == null || tpa.get(player.getUniqueId()) == null) {
                        String msg = Lang.fileConfig.getString("teleport-no-request-accept");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    }
                }else{
                    if (ServerEssentials.plugin.getConfig().getBoolean("teleport-movement-cancel")){
                        if (tpa.containsKey(player.getUniqueId())) {
                            Player target = Bukkit.getPlayer(tpa.get(player.getUniqueId()));
                            cancelTimeout(player);
                            cancel.add(target.getUniqueId());
                            String msg = Lang.fileConfig.getString("teleport-accept-request-target").replace("<target>", target.getName());
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                            String msg2 = Lang.fileConfig.getString("teleport-wait-message").replace("<player>", player.getName()).replace("<time>", String.valueOf(tpwait));
                            target.sendMessage(ChatColor.translateAlternateColorCodes('&', msg2));
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
                                            String msg = Lang.fileConfig.getString("teleport-success").replace("<target>", player.getName());
                                            target.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                            String msg2 = Lang.fileConfig.getString("teleport-target-success").replace("<sender>", target.getName());
                                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg2));
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
                            String msg = Lang.fileConfig.getString("teleport-accept-request").replace("<sender>", player.getName());
                            Bukkit.getPlayer(tpahere.get(player.getUniqueId())).sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                            String msg2 = Lang.fileConfig.getString("teleport-wait-message").replace("<player>", target.getName()).replace("<time>", String.valueOf(tpwait));
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg2));
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
                                            String msg = Lang.fileConfig.getString("teleport-target-success").replace("<sender>", player.getName());
                                            target.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                            String msg2 = Lang.fileConfig.getString("teleport-success").replace("<target>", target.getName());
                                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg2));
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
                            String msg = Lang.fileConfig.getString("teleport-no-request-accept");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                            return true;
                        }
                    }else{
                        if (tpa.containsKey(player.getUniqueId())) {
                            Player target = Bukkit.getPlayer(tpa.get(player.getUniqueId()));
                            cancelTimeout(player);
                            String msg = Lang.fileConfig.getString("teleport-accept-request-target").replace("<target>", target.getName());
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                            String msg2 = Lang.fileConfig.getString("teleport-wait-message").replace("<player>", player.getName()).replace("<time>", String.valueOf(tpwait));
                            target.sendMessage(ChatColor.translateAlternateColorCodes('&', msg2));
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
                                        String msg = Lang.fileConfig.getString("teleport-target-success").replace("<sender>", target.getName());
                                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                        String msg2 = Lang.fileConfig.getString("teleport-success").replace("<target>", player.getName());
                                        target.sendMessage(ChatColor.translateAlternateColorCodes('&', msg2));
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
                            String msg = Lang.fileConfig.getString("teleport-wait-message").replace("<player>", target.getName()).replace("<time>", String.valueOf(tpwait));
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                            String msg2 = Lang.fileConfig.getString("teleport-accept-request").replace("<sender>", player.getName());
                            Bukkit.getPlayer(tpahere.get(player.getUniqueId())).sendMessage(ChatColor.translateAlternateColorCodes('&', msg2));
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
                                        String msg = Lang.fileConfig.getString("teleport-target-success").replace("<sender>", player.getName());
                                        target.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                        String msg2 = Lang.fileConfig.getString("teleport-success").replace("<target>", target.getName());
                                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg2));
                                        player.teleport(Bukkit.getPlayer(tpahere.get(player.getUniqueId())));
                                        tpahere.remove(player.getUniqueId());
                                        teleport.remove(player.getUniqueId());
                                        tpahere.remove(target.getUniqueId());
                                    }
                                }
                            }, tpwait));
                            return true;
                        } else if (tpahere.get(player.getUniqueId()) == null || tpa.get(player.getUniqueId()) == null) {
                            String msg = Lang.fileConfig.getString("teleport-no-request-accept");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                            return true;
                        }
                    }
                }
            } else {
                String perm = Lang.fileConfig.getString("no-permission-message").replace("<permission>", "se.tpaccept");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', perm));
                return true;
            }
        }
        if (command.getName().equalsIgnoreCase("tpdeny")) {
            if (player.hasPermission("se.tpdeny") || player.hasPermission("se.all")) {
                if (tpa.containsKey(player.getUniqueId())) {
                    Player target = Bukkit.getPlayer(tpa.get(player.getUniqueId()));
                    String msg = Lang.fileConfig.getString("teleport-deny-request-target").replace("<target>", target.getName());
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    String msg2 = Lang.fileConfig.getString("teleport-deny-request").replace("<sender>", player.getName());
                    Bukkit.getPlayer(tpa.get(player.getUniqueId())).sendMessage(ChatColor.translateAlternateColorCodes('&', msg2));
                    if (teleportcancel.containsKey(player.getUniqueId()) && teleportcancel.get(player.getUniqueId()) != null){
                        Bukkit.getScheduler().cancelTask(teleportcancel.get(player.getUniqueId()));
                    }
                    tpa.remove(player.getUniqueId());
                    return true;
                } else if (tpahere.containsKey(player.getUniqueId())) {
                    Player target = Bukkit.getPlayer(tpahere.get(player.getUniqueId()));
                    String msg = Lang.fileConfig.getString("teleport-deny-request-target").replace("<target>", target.getName());
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    String msg2 = Lang.fileConfig.getString("teleport-deny-request").replace("<sender>", player.getName());
                    Bukkit.getPlayer(tpahere.get(player.getUniqueId())).sendMessage(ChatColor.translateAlternateColorCodes('&', msg2));
                    if (teleportcancel.containsKey(player.getUniqueId()) && teleportcancel.get(player.getUniqueId()) != null){
                        Bukkit.getScheduler().cancelTask(teleportcancel.get(player.getUniqueId()));
                    }
                    tpahere.remove(player.getUniqueId());
                    return true;
                } else if (!tpahere.containsKey(player.getUniqueId()) || !tpa.containsKey(player.getUniqueId())) {
                    String msg = Lang.fileConfig.getString("teleport-no-request-deny");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    return true;
                }
            } else {
                String perm = Lang.fileConfig.getString("no-permission-message").replace("<permission>", "se.tpdeny");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', perm));
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