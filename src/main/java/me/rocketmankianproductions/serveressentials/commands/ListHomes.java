package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ListHomes implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;

        if (sender instanceof Player){
            if (player.hasPermission("se.listhomes")){
                if (args.length == 1) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target != null) {
                        String targetname = target.getUniqueId().toString();
                        if (target != player) {
                            if (args[0].equalsIgnoreCase(target.getName())) {
                                ConfigurationSection inventorySection = Sethome.fileConfig.getConfigurationSection("Home." + targetname);
                                assert inventorySection != null;
                                player.sendMessage(ChatColor.GREEN + "---------------------------"
                                        + "\n" + target.getName() + "'s Home(s) List"
                                        + "\n---------------------------");
                                try {
                                    for (String key : inventorySection.getKeys(false)) {
                                        player.sendMessage(ChatColor.GOLD + key);
                                    }
                                } catch (NullPointerException e) {
                                    player.sendMessage(ChatColor.RED + target.getName() + " hasn't set any homes");
                                }
                                return true;
                            }
                        }else{
                            player.sendMessage("Incorrect format! Please use /home to view your homes");
                            return true;
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "Player does not exist");
                        return true;
                    }
                }
            }else{
                if (ServerEssentials.plugin.getConfig().getString("no-permission-message").length() == 0){
                    player.sendMessage(ChatColor.RED + "You do not have the required permission (se.listhomes) to run this command.");
                    return true;
                }else{
                    String permission = ServerEssentials.getPlugin().getConfig().getString("no-permission-message");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', permission));
                }
                return true;
            }
        }
        return false;
    }
}
