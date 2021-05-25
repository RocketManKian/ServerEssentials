package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SendWarp implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        Location loc;
        Player player = (Player) sender;

        if (sender instanceof Player) {
            if (player.hasPermission("se.sendwarp")) {
                if (args.length == 2) {
                    if (Setwarp.file.exists() && Setwarp.fileConfig.getString("Warp." + args[1] + ".World") != null) {
                        Player target = Bukkit.getServer().getPlayer(args[0]);
                        if (target != null) {
                            if (target != player) {
                                // Check if the File Exists and if Location.World has data
                                if (player.hasPermission("se.warps." + args[1]) || player.hasPermission("se.warps.all")) {
                                    // Gathering Location
                                    float yaw = Setwarp.fileConfig.getInt("Warp." + args[1] + ".Yaw");
                                    float pitch = Sethome.fileConfig.getInt("Warp." + args[1] + ".Pitch");
                                    loc = new Location(Bukkit.getWorld(Setwarp.fileConfig.getString("Warp." + args[1] + ".World")),
                                            Setwarp.fileConfig.getDouble("Warp." + args[1] + ".X"),
                                            Setwarp.fileConfig.getDouble("Warp." + args[1] + ".Y"),
                                            Setwarp.fileConfig.getDouble("Warp." + args[1] + ".Z"),
                                            yaw, pitch);
                                    // Teleporting Target
                                    target.teleport(loc);
                                    target.sendMessage("Successfully warped to " + args[1]);
                                    return true;
                                } else {
                                    if (ServerEssentials.plugin.getConfig().getString("no-permission-message").length() == 0) {
                                        player.sendMessage(ChatColor.RED + "You do not have the required permission (se.warps." + args[1] + ") to run this command.");
                                        return true;
                                    } else {
                                        String permission = ServerEssentials.getPlugin().getConfig().getString("no-permission-message");
                                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', permission));
                                    }
                                    return true;
                                }
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + "Cannot find player " + ChatColor.WHITE + args[0]);
                            return true;
                        }
                    } else {
                        player.sendMessage("Warp Doesn't Exist!");
                        return true;
                    }
                }
            } else {
                if (ServerEssentials.plugin.getConfig().getString("no-permission-message").length() == 0) {
                    player.sendMessage(ChatColor.RED + "You do not have the required permission (se.sendhome) to run this command.");
                    return true;
                } else {
                    String permission = ServerEssentials.getPlugin().getConfig().getString("no-permission-message");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', permission));
                }
                return true;
            }
        }
        return false;
    }
}
