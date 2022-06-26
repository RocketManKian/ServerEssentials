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

public class SendHome implements CommandExecutor {

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player){
            Location loc;
            Player player = (Player) sender;
            boolean hasPerm = ServerEssentials.permissionChecker(player, "se.sendhome");
            if (hasPerm) {
                if (args.length == 2){
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target != null) {
                        String targetname = target.getUniqueId().toString();
                        if (target != player) {
                            if (args[0].equalsIgnoreCase(target.getName())){
                                // Check if the File Exists and if Location.World has data
                                if (Sethome.file.exists() && Sethome.fileConfig.getString("Home." + targetname + "." + args[1] + ".World") != null) {
                                    // Gathering Location
                                    float yaw = Sethome.fileConfig.getInt("Home." + targetname + "." + args[1] + ".Yaw");
                                    loc = new Location(Bukkit.getWorld(Sethome.fileConfig.getString("Home." + targetname + "." + args[1] + ".World")),
                                            Sethome.fileConfig.getDouble("Home." + targetname + "." + args[1] + ".X"),
                                            Sethome.fileConfig.getDouble("Home." + targetname + "." + args[1] + ".Y"),
                                            Sethome.fileConfig.getDouble("Home." + targetname + "." + args[1] + ".Z"),
                                            yaw, 0);
                                    // Teleporting Target
                                    target.teleport(loc);
                                    String msg = Lang.fileConfig.getString("sendhome-target");
                                    target.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                    String msg2 = Lang.fileConfig.getString("sendhome-player").replace("<target>", target.getName());
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg2));
                                    return true;
                                } else {
                                    String msg = Lang.fileConfig.getString("home-invalid");
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                    return true;
                                }
                            }
                        }else{
                            player.sendMessage("Incorrect format! Please use /home (name) to teleport to your home");
                            return true;
                        }
                    }else{
                        String msg = Lang.fileConfig.getString("target-offline");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    }
                }else{
                    String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/sendhome (player) (home)");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    return true;
                }
            }
        }else{
            String console = Lang.fileConfig.getString("console-invalid");
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', console));
            return true;
        }
        return false;
    }
}
