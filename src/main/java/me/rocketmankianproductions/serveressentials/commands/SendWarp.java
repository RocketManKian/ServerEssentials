package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.file.Lang;
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
                                    String msg = Lang.fileConfig.getString("sendwarp-player").replace("<target>", target.getName()).replace("<warp>", args[1]);
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                    String msg2 = Lang.fileConfig.getString("sendwarp-target").replace("<warp>", args[1]);
                                    target.sendMessage(ChatColor.translateAlternateColorCodes('&', msg2));
                                    return true;
                                } else {
                                    String perm = Lang.fileConfig.getString("no-permission-message").replace("<permission>", "se.warps." + args[1]);
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', perm));
                                    return true;
                                }
                            }
                        } else {
                            String msg = Lang.fileConfig.getString("target-offline");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                            return true;
                        }
                    } else {
                        String msg = Lang.fileConfig.getString("warp-invalid");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    }
                }
            } else {
                String perm = Lang.fileConfig.getString("no-permission-message").replace("<permission>", "se.sendwarp");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', perm));
                return true;
            }
        }
        return false;
    }
}
