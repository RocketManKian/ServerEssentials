package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

import static me.rocketmankianproductions.serveressentials.ServerEssentials.hex;

public class Repair implements CommandExecutor {

    public static HashMap<UUID, Integer> repaircancel = new HashMap<UUID, Integer>();
    int time;
    int taskID;
    Long delay = ServerEssentials.getPlugin().getConfig().getLong("repair-cooldown");
    int delay2 = (int) (delay * 20);
    int delay3 = delay2 / 20;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (ServerEssentials.permissionChecker(player, "se.repair")) {
                if (args.length == 0) {
                    // Check to see if Player has command cooldown active
                    if (!repaircancel.containsKey(player.getUniqueId())) {
                        if (!player.getItemInHand().getType().equals(Material.AIR)) {
                            ItemStack item = player.getItemInHand();
                            short durability = item.getDurability();
                            if (item.getType().isBlock()) {
                                String msg = Lang.fileConfig.getString("repair-invalid-item");
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                            } else if (item.getType().isEdible()) {
                                String msg = Lang.fileConfig.getString("repair-invalid-item");
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                            } else if (durability == 0) {
                                String msg = Lang.fileConfig.getString("repair-durability-max");
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                            } else {
                                player.getItemInHand().setDurability((short) 0);
                                String msg = Lang.fileConfig.getString("repair-successful").replace("<item>", player.getItemInHand().getType().toString());
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                                // Command Cooldown
                                repaircancel.put(player.getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((ServerEssentials.getPlugin()), new Runnable() {
                                    public void run() {
                                        repaircancel.remove(player.getUniqueId());
                                    }
                                }, delay2));
                                setTimer(delay3);
                                startTimer();
                            }
                            return true;
                        } else {
                            String msg = Lang.fileConfig.getString("repair-invalid-item");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                            return true;
                        }
                    } else if (repaircancel.containsKey(player.getUniqueId()) && repaircancel.get(player.getUniqueId()) != null) {
                        String msg = Lang.fileConfig.getString("command-timeout").replace("<time>", String.valueOf(time));
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                        return true;
                    }
                } else if (args.length == 1) {
                    // Check to see if Player has command cooldown active
                    if (!repaircancel.containsKey(player.getUniqueId())) {
                        if (args[0].equalsIgnoreCase("all")) {
                            for (int i = 0; i <= 36; i++) {
                                try {
                                    if (!player.getInventory().getItem(i).getType().isBlock() && !player.getInventory().getItem(i).getType().isEdible()) {
                                        short durability = player.getInventory().getItem(i).getDurability();
                                        if (durability > 0) {
                                            player.getInventory().getItem(i).setDurability((short) 0);
                                        }
                                    }
                                } catch (Exception e) {}
                            }
                            player.getInventory().getItemInOffHand().setDurability((short) 0);
                            if (player.getInventory().getBoots() != null) {
                                player.getInventory().getBoots().setDurability((short) 0);
                            }
                            if (player.getInventory().getLeggings() != null) {
                                player.getInventory().getLeggings().setDurability((short) 0);
                            }
                            if (player.getInventory().getChestplate() != null) {
                                player.getInventory().getChestplate().setDurability((short) 0);
                            }
                            if (player.getInventory().getHelmet() != null) {
                                player.getInventory().getHelmet().setDurability((short) 0);
                            }
                            String msg = Lang.fileConfig.getString("repair-all-items");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                            // Command Cooldown
                            repaircancel.put(player.getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((ServerEssentials.getPlugin()), new Runnable() {
                                public void run() {
                                    repaircancel.remove(player.getUniqueId());
                                }
                            }, delay2));
                            setTimer(delay3);
                            startTimer();
                            return true;
                        } else {
                            String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/repair all");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                            return true;
                        }
                    } else if (repaircancel.containsKey(player.getUniqueId()) && repaircancel.get(player.getUniqueId()) != null) {
                        String msg = Lang.fileConfig.getString("command-timeout").replace("<time>", String.valueOf(time));
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                        return true;
                    }
                } else {
                    String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/repair");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                    return true;
                }
            }
        } else {
            String console = Lang.fileConfig.getString("console-invalid");
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', hex(console)));
            return true;
        }
        return false;
    }
    public void setTimer ( int amount){
        time = amount;
    }

    public void startTimer () {
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        taskID = scheduler.scheduleSyncRepeatingTask(ServerEssentials.plugin, new Runnable() {
            @Override
            public void run() {
                if (time == 0) {
                    stopTimer();
                    return;
                }
                time = time - 1;
            }
        }, 0L, 20L);
    }

    public void stopTimer () {
        Bukkit.getScheduler().cancelTask(taskID);
    }
}
