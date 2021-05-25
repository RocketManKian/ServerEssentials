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

public class SendHome implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        Location loc;
        Player player = (Player) sender;

        if (sender instanceof Player){
            if (player.hasPermission("se.sendhome")){
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
                                    target.sendMessage("You have been teleported to your home");
                                    player.sendMessage(ChatColor.GREEN + "Teleported " + ChatColor.WHITE + target.getName() + ChatColor.GREEN + " to their home");
                                    return true;
                                } else {
                                    player.sendMessage("Home Doesn't Exist!");
                                    return true;
                                }
                            }
                        }else{
                            player.sendMessage("Incorrect format! Please use /home (name) to teleport to your home");
                            return true;
                        }
                    }else{
                        player.sendMessage(ChatColor.RED + "Player does not exist");
                        return true;
                    }
                }
            }else{
                if (ServerEssentials.plugin.getConfig().getString("no-permission-message").length() == 0){
                    player.sendMessage(ChatColor.RED + "You do not have the required permission (se.sendhome) to run this command.");
                    return true;
                }else{
                    String permission = ServerEssentials.getPlugin().getConfig().getString("no-permission-message");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', permission));
                    return true;
                }
            }
        }
        return false;
    }
}