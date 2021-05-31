package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TeleportRequest implements CommandExecutor {

    private static HashMap<UUID, UUID> tpa = new HashMap<>();
    private static HashMap<UUID, UUID> tpahere = new HashMap<>();
    private static HashMap<UUID, Integer> teleportcancel = new HashMap<>();

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
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
                if (target != null) {
                    if (target != player) {
                        if (TPToggle.fileConfig.getBoolean("tptoggle." + target.getName(), false) == false) {
                            tpa.put(target.getUniqueId(), player.getUniqueId());
                            player.sendMessage(ChatColor.GOLD + "You sent a teleport request to " + ChatColor.WHITE + target.getName() + ".");
                            player.sendMessage(ChatColor.GOLD + "To cancel this request, type " + ChatColor.RED + "/tpacancel");
                            target.sendMessage(ChatColor.WHITE + player.getName() + ChatColor.GOLD + " sent a teleport request to you.");
                            target.sendMessage(ChatColor.GOLD + "To accept, type" + ChatColor.RED + " /tpaccept.");
                            target.sendMessage(ChatColor.GOLD + "To deny, type " + ChatColor.RED + "/tpdeny.");
                            target.sendMessage(ChatColor.GOLD + "This request will timeout in " + ChatColor.RED + delay3 + " seconds");
                            if (teleportcancel.containsKey(target.getUniqueId()) && teleportcancel.get(target.getUniqueId()) != null) {
                                Bukkit.getScheduler().cancelTask(teleportcancel.get(target.getUniqueId()));
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
                if (target != null) {
                    if (target != player) {
                        if (TPToggle.fileConfig.getBoolean("tptoggle." + target.getName(), false) == false) {
                            tpahere.put(target.getUniqueId(), player.getUniqueId());
                            player.sendMessage(ChatColor.GOLD + "You sent a teleport here request to " + ChatColor.WHITE + target.getName() + ".");
                            player.sendMessage(ChatColor.GOLD + "To cancel this request, type " + ChatColor.RED + "/tpacancel");
                            target.sendMessage(ChatColor.WHITE + player.getName() + ChatColor.GOLD + " would like you to teleport to them");
                            target.sendMessage(ChatColor.GOLD + "To accept, type" + ChatColor.RED + " /tpaccept.");
                            target.sendMessage(ChatColor.GOLD + "To deny, type " + ChatColor.RED + "/tpdeny.");
                            target.sendMessage(ChatColor.GOLD + "This request will timeout in " + ChatColor.RED + delay3 + " seconds");
                            if (teleportcancel.containsKey(target.getUniqueId()) && teleportcancel.get(target.getUniqueId()) != null) {
                                Bukkit.getScheduler().cancelTask(teleportcancel.get(target.getUniqueId()));
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
                if (tpa.containsKey(player.getUniqueId())) {
                    player.sendMessage(ChatColor.GREEN + "You accepted the teleport request.");
                    Bukkit.getPlayer(tpa.get(player.getUniqueId())).sendMessage(ChatColor.WHITE + player.getName() + ChatColor.GREEN + " accepted the teleport request.");
                    Bukkit.getPlayer(tpa.get(player.getUniqueId())).teleport(player);
                    tpa.remove(player.getUniqueId());
                    return true;
                } else if (tpahere.containsKey(player.getUniqueId())) {
                    player.sendMessage(ChatColor.GREEN + "You accepted the teleport request.");
                    Bukkit.getPlayer(tpahere.get(player.getUniqueId())).sendMessage(ChatColor.WHITE + player.getName() + ChatColor.GREEN + " accepted the teleport request.");
                    player.teleport(Bukkit.getPlayer(tpahere.get(player.getUniqueId())));
                    tpahere.remove(player.getUniqueId());
                    return true;
                } else if (tpahere.get(player.getUniqueId()) == null || tpa.get(player.getUniqueId()) == null) {
                    player.sendMessage(ChatColor.RED + "There's no request to accept.");
                    return true;
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
}