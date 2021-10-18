package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Teleport implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("se.teleport") || player.hasPermission("se.all")) {
                // Checking if the player has the correct permission
                if (args.length == 0) {
                    player.sendMessage(ChatColor.RED + "You need to enter some arguments." + ChatColor.YELLOW
                            + "\nTo teleport yourself: /teleport <otherplayer>" + ChatColor.YELLOW
                            + "\nTo teleport others: /teleport <player> <otherplayer>");
                    return true;
                } else if (args.length == 1) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target == null){
                        String msg = Lang.fileConfig.getString("target-offline");
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    }else if (target == player) {
                        String msg = Lang.fileConfig.getString("teleport-self");
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    } else {
                        String target2 = target.getName();
                        if (sender.hasPermission("se.silenttp") || sender.hasPermission("se.all")) {
                            if (target == null){
                                String msg = Lang.fileConfig.getString("target-offline");
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            }
                            String msg = Lang.fileConfig.getString("teleport-success").replace("<target>", target2);
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                            if (ServerEssentials.plugin.getConfig().getBoolean("teleport-save")){
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
                            player.teleport(target.getLocation());
                            return true;
                        } else if (!sender.hasPermission("se.silenttp")) {
                            if (target == null){
                                String msg = Lang.fileConfig.getString("target-offline");
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            }
                            if (ServerEssentials.plugin.getConfig().getBoolean("teleport-save")){
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
                            String msg = Lang.fileConfig.getString("teleport-success").replace("<target>", target2);
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                            String msg2 = Lang.fileConfig.getString("teleport-target-success").replace("<sender>", sender.getName());
                            target.sendMessage(ChatColor.translateAlternateColorCodes('&', msg2));
                            player.teleport(target.getLocation());
                            return true;
                        }
                    }
                } else if (args.length == 2) {
                    Player playerToSend = Bukkit.getPlayer(args[0]);
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target == null){
                        String msg = Lang.fileConfig.getString("target-offline");
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    }else if (playerToSend == target) {
                        String msg = Lang.fileConfig.getString("teleport-target-to-self");
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    } else {
                        String target2 = target.getName();
                        if (sender.hasPermission("se.silenttp") || sender.hasPermission("se.all")) {
                            if (playerToSend == null){
                                String msg = Lang.fileConfig.getString("target-offline");
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            }
                            if (target == sender) {
                                String msg = Lang.fileConfig.getString("teleport-target-success").replace("<sender>", playerToSend.getName());
                                target.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                if (ServerEssentials.plugin.getConfig().getBoolean("teleport-save")) {
                                    if (Back.location.containsKey(playerToSend.getUniqueId())) {
                                        Back.location.remove(playerToSend.getUniqueId());
                                        Back.location.put(playerToSend.getUniqueId(), playerToSend.getLocation());
                                    } else {
                                        Back.location.put(playerToSend.getUniqueId(), playerToSend.getLocation());
                                    }
                                } else if (player.hasPermission("se.back.bypass")) {
                                    if (Back.location.containsKey(playerToSend.getUniqueId())) {
                                        Back.location.remove(playerToSend.getUniqueId());
                                        Back.location.put(playerToSend.getUniqueId(), playerToSend.getLocation());
                                    } else {
                                        Back.location.put(playerToSend.getUniqueId(), playerToSend.getLocation());
                                    }
                                }
                                playerToSend.teleport(target.getLocation());
                                return true;
                            } else if (playerToSend == sender) {
                                String msg = Lang.fileConfig.getString("teleport-success").replace("<target>", target2);
                                playerToSend.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                if (ServerEssentials.plugin.getConfig().getBoolean("teleport-save")) {
                                    if (Back.location.containsKey(playerToSend.getUniqueId())) {
                                        Back.location.remove(playerToSend.getUniqueId());
                                        Back.location.put(playerToSend.getUniqueId(), playerToSend.getLocation());
                                    } else {
                                        Back.location.put(playerToSend.getUniqueId(), playerToSend.getLocation());
                                    }
                                } else if (player.hasPermission("se.back.bypass")) {
                                    if (Back.location.containsKey(playerToSend.getUniqueId())) {
                                        Back.location.remove(playerToSend.getUniqueId());
                                        Back.location.put(playerToSend.getUniqueId(), playerToSend.getLocation());
                                    } else {
                                        Back.location.put(playerToSend.getUniqueId(), playerToSend.getLocation());
                                    }
                                }
                                playerToSend.teleport(target.getLocation());
                                return true;
                            } else {
                                String msg = Lang.fileConfig.getString("teleport-others").replace("<target>", playerToSend.getName()).replace("<target2>", target2);
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                String msg2 = Lang.fileConfig.getString("teleport-force-target").replace("<target>", target.getName());
                                playerToSend.sendMessage(ChatColor.translateAlternateColorCodes('&', msg2));
                                String msg3 = Lang.fileConfig.getString("teleport-target-success").replace("<sender>", playerToSend.getName());
                                target.sendMessage(ChatColor.translateAlternateColorCodes('&', msg3));
                                if (ServerEssentials.plugin.getConfig().getBoolean("teleport-save")) {
                                    if (Back.location.containsKey(playerToSend.getUniqueId())) {
                                        Back.location.remove(playerToSend.getUniqueId());
                                        Back.location.put(playerToSend.getUniqueId(), playerToSend.getLocation());
                                    } else {
                                        Back.location.put(playerToSend.getUniqueId(), playerToSend.getLocation());
                                    }
                                } else if (player.hasPermission("se.back.bypass")) {
                                    if (Back.location.containsKey(playerToSend.getUniqueId())) {
                                        Back.location.remove(playerToSend.getUniqueId());
                                        Back.location.put(playerToSend.getUniqueId(), playerToSend.getLocation());
                                    } else {
                                        Back.location.put(playerToSend.getUniqueId(), playerToSend.getLocation());
                                    }
                                }
                                playerToSend.teleport(target.getLocation());
                                return true;
                            }
                        } else if (!sender.hasPermission("se.silenttp")) {
                            if (playerToSend == null){
                                String msg = Lang.fileConfig.getString("target-offline");
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            }
                            if (target == sender) {
                                String msg = Lang.fileConfig.getString("teleport-force-target").replace("<target>", target.getName());
                                playerToSend.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                String msg2 = Lang.fileConfig.getString("teleport-target-success").replace("<sender>", playerToSend.getName());
                                target.sendMessage(ChatColor.translateAlternateColorCodes('&', msg2));
                                if (ServerEssentials.plugin.getConfig().getBoolean("teleport-save")) {
                                    if (Back.location.containsKey(playerToSend.getUniqueId())) {
                                        Back.location.remove(playerToSend.getUniqueId());
                                        Back.location.put(playerToSend.getUniqueId(), playerToSend.getLocation());
                                    } else {
                                        Back.location.put(playerToSend.getUniqueId(), playerToSend.getLocation());
                                    }
                                } else if (player.hasPermission("se.back.bypass")) {
                                    if (Back.location.containsKey(playerToSend.getUniqueId())) {
                                        Back.location.remove(playerToSend.getUniqueId());
                                        Back.location.put(playerToSend.getUniqueId(), playerToSend.getLocation());
                                    } else {
                                        Back.location.put(playerToSend.getUniqueId(), playerToSend.getLocation());
                                    }
                                }
                                playerToSend.teleport(target.getLocation());
                                return true;
                            } else if (playerToSend == sender) {
                                String msg = Lang.fileConfig.getString("teleport-target-success").replace("<sender>", sender.getName());
                                target.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                String msg2 = Lang.fileConfig.getString("teleport-success").replace("<target>", target.getName());
                                playerToSend.sendMessage(ChatColor.translateAlternateColorCodes('&', msg2));
                                if (ServerEssentials.plugin.getConfig().getBoolean("teleport-save")) {
                                    if (Back.location.containsKey(playerToSend.getUniqueId())) {
                                        Back.location.remove(playerToSend.getUniqueId());
                                        Back.location.put(playerToSend.getUniqueId(), playerToSend.getLocation());
                                    } else {
                                        Back.location.put(playerToSend.getUniqueId(), playerToSend.getLocation());
                                    }
                                } else if (player.hasPermission("se.back.bypass")) {
                                    if (Back.location.containsKey(playerToSend.getUniqueId())) {
                                        Back.location.remove(playerToSend.getUniqueId());
                                        Back.location.put(playerToSend.getUniqueId(), playerToSend.getLocation());
                                    } else {
                                        Back.location.put(playerToSend.getUniqueId(), playerToSend.getLocation());
                                    }
                                }
                                playerToSend.teleport(target.getLocation());
                                return true;
                            } else {
                                String msg = Lang.fileConfig.getString("teleport-others").replace("<target>", sender.getName()).replace("<target2>", target.getName());
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                String msg2 = Lang.fileConfig.getString("teleport-target-success").replace("<sender>", sender.getName());
                                target.sendMessage(ChatColor.translateAlternateColorCodes('&', msg2));
                                String msg3 = Lang.fileConfig.getString("teleport-force-target").replace("<target>", target.getName());
                                playerToSend.sendMessage(ChatColor.translateAlternateColorCodes('&', msg3));
                                if (ServerEssentials.plugin.getConfig().getBoolean("teleport-save")) {
                                    if (Back.location.containsKey(playerToSend.getUniqueId())) {
                                        Back.location.remove(playerToSend.getUniqueId());
                                        Back.location.put(playerToSend.getUniqueId(), playerToSend.getLocation());
                                    } else {
                                        Back.location.put(playerToSend.getUniqueId(), playerToSend.getLocation());
                                    }
                                } else if (player.hasPermission("se.back.bypass")) {
                                    if (Back.location.containsKey(playerToSend.getUniqueId())) {
                                        Back.location.remove(playerToSend.getUniqueId());
                                        Back.location.put(playerToSend.getUniqueId(), playerToSend.getLocation());
                                    } else {
                                        Back.location.put(playerToSend.getUniqueId(), playerToSend.getLocation());
                                    }
                                }
                                playerToSend.teleport(target.getLocation());
                                return true;
                            }
                        }
                    }
                } else if (args.length == 3) {
                    if (ServerEssentials.plugin.getConfig().getBoolean("teleport-save")) {
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
                    try {
                        double x = Double.parseDouble(args[0]);
                        double y = Double.parseDouble(args[1]);
                        double z = Double.parseDouble(args[2]);
                        Location location = new Location(((Player) sender).getWorld(), x, y, z);
                        String msg = Lang.fileConfig.getString("teleport-pos-success");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        player.teleport(location);
                    } catch (NumberFormatException n){
                        String msg = Lang.fileConfig.getString("teleport-pos-invalid");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    }
                    return true;
                } else if (args.length == 4){
                    Player target = Bukkit.getPlayerExact(args[0]);
                    if (target == null){
                        String msg = Lang.fileConfig.getString("target-offline");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    }else if (args[0].equalsIgnoreCase(target.getName())){
                        if (args[0].equalsIgnoreCase(player.getName())){
                            try {
                                World myworld = player.getWorld();
                                Location yourlocation = new Location(myworld, Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3]));
                                player.teleport(yourlocation);
                                String msg = Lang.fileConfig.getString("teleport-pos-success");
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                            } catch (NumberFormatException e) {
                                String msg = Lang.fileConfig.getString("teleport-pos-invalid");
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                            }
                        }else{
                            try {
                                World myworld = target.getWorld();
                                Location yourlocation = new Location(myworld, Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3]));
                                target.teleport(yourlocation);
                                String msg = Lang.fileConfig.getString("teleport-pos-target-success");
                                target.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                String msg2 = Lang.fileConfig.getString("teleport-pos-target-message").replace("<target>", target.getName());
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg2));
                            } catch (NumberFormatException e) {
                                String msg = Lang.fileConfig.getString("teleport-pos-invalid");
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                            }
                        }
                        return true;
                    }
                }
            } else {
                String perm = Lang.fileConfig.getString("no-permission-message").replace("<permission>", "se.teleport");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', perm));
                return true;
            }
        } else if (sender instanceof ConsoleCommandSender) {
            if (args.length == 2) {
                Player playerToSend = Bukkit.getPlayer(args[0]);
                Player target = Bukkit.getPlayer(args[1]);
                if (playerToSend != target && target != null) {
                    if (ServerEssentials.plugin.getConfig().getBoolean("teleport-save")) {
                        if (Back.location.containsKey(playerToSend.getUniqueId())) {
                            Back.location.remove(playerToSend.getUniqueId());
                            Back.location.put(playerToSend.getUniqueId(), playerToSend.getLocation());
                        } else {
                            Back.location.put(playerToSend.getUniqueId(), playerToSend.getLocation());
                        }
                    } else if (playerToSend.hasPermission("se.back.bypass")) {
                        if (Back.location.containsKey(playerToSend.getUniqueId())) {
                            Back.location.remove(playerToSend.getUniqueId());
                            Back.location.put(playerToSend.getUniqueId(), playerToSend.getLocation());
                        } else {
                            Back.location.put(playerToSend.getUniqueId(), playerToSend.getLocation());
                        }
                    }
                    playerToSend.teleport(target.getLocation());
                    String msg = Lang.fileConfig.getString("teleport-others").replace("<target>", playerToSend.getName()).replace("<target2>", target.getName());
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    String msg2 = Lang.fileConfig.getString("teleport-force-target").replace("<target>", target.getName());
                    playerToSend.sendMessage(ChatColor.translateAlternateColorCodes('&', msg2));
                    String msg3 = Lang.fileConfig.getString("teleport-target-success").replace("<sender>", playerToSend.getName());
                    target.sendMessage(ChatColor.translateAlternateColorCodes('&', msg3));
                    return true;
                } else {
                    String msg = Lang.fileConfig.getString("target-offline");
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    return true;
                }
            } else if (args.length == 4){
                Player target = Bukkit.getPlayerExact(args[0]);
                if (target == null){
                    String msg = Lang.fileConfig.getString("target-offline");
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    return true;
                }else if (args[0].equalsIgnoreCase(target.getName())) {
                    try {
                        World myworld = target.getWorld();
                        Location yourlocation = new Location(myworld, Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3]));
                        target.teleport(yourlocation);
                        String msg = Lang.fileConfig.getString("teleport-pos-target-success");
                        target.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        String msg2 = Lang.fileConfig.getString("teleport-pos-target-message").replace("<target>", target.getName());
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg2));
                    } catch (NumberFormatException e) {
                        String msg = Lang.fileConfig.getString("teleport-pos-invalid");
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    }
                    return true;
                }
            }
        } else if (sender instanceof BlockCommandSender) {
            if (args.length == 2){
                Player playerToSend = Bukkit.getPlayer(args[0]);
                Player target = Bukkit.getPlayer(args[1]);
                if (playerToSend != target) {
                    try {
                        if (ServerEssentials.plugin.getConfig().getBoolean("teleport-save")){
                            if (Back.location.containsKey(playerToSend.getUniqueId())){
                                Back.location.remove(playerToSend.getUniqueId());
                                Back.location.put(playerToSend.getUniqueId(), playerToSend.getLocation());
                            }else{
                                Back.location.put(playerToSend.getUniqueId(), playerToSend.getLocation());
                            }
                        }else if (playerToSend.hasPermission("se.back.bypass")){
                            if (Back.location.containsKey(playerToSend.getUniqueId())){
                                Back.location.remove(playerToSend.getUniqueId());
                                Back.location.put(playerToSend.getUniqueId(), playerToSend.getLocation());
                            }else{
                                Back.location.put(playerToSend.getUniqueId(), playerToSend.getLocation());
                            }
                        }
                        playerToSend.teleport(target.getLocation());
                        String msg = Lang.fileConfig.getString("teleport-force-target").replace("<target>", target.getName());
                        playerToSend.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        String msg2 = Lang.fileConfig.getString("teleport-target-success").replace("<sender>", playerToSend.getName());
                        target.sendMessage(ChatColor.translateAlternateColorCodes('&', msg2));
                        String msg3 = Lang.fileConfig.getString("teleport-others").replace("<target>", playerToSend.getName()).replace("<target2>", target.getName());
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg3));
                        return true;
                    } catch (NullPointerException e) {
                        String msg = Lang.fileConfig.getString("target-offline");
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    }
                }
            } else if (args.length == 4){
                Player target = Bukkit.getPlayerExact(args[0]);
                if (target == null){
                    String msg = Lang.fileConfig.getString("target-offline");
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    return true;
                }else if (args[0].equalsIgnoreCase(target.getName())) {
                    try {
                        World myworld = target.getWorld();
                        Location yourlocation = new Location(myworld, Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3]));
                        target.teleport(yourlocation);
                        String msg = Lang.fileConfig.getString("teleport-pos-target-success");
                        target.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        String msg2 = Lang.fileConfig.getString("teleport-pos-target-message").replace("<target>", target.getName());
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg2));
                    } catch (NumberFormatException e) {
                        String msg = Lang.fileConfig.getString("teleport-pos-invalid");
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    }
                    return true;
                }
            }
        }
        return false;
    }
}