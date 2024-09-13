package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.UserFile;
import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Balance implements CommandExecutor {
    private ServerEssentials plugin = ServerEssentials.getInstance;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        Player player = (Player)sender;
        if (ServerEssentials.permissionChecker(player, "se.balance")) {
            if (args.length == 0 || args[0].equalsIgnoreCase(player.getName())){
                if (UserFile.fileConfig.getString(String.valueOf(player.getUniqueId())) == null){
                    UserFile.fileConfig.set(player.getUniqueId() + ".money", plugin.getConfig().getDouble("start-balance"));
                    Eco.saveBalance();
                }
                plugin.playerBank.put(player.getUniqueId(), UserFile.fileConfig.getDouble(player.getUniqueId() + ".money"));
                String msg = Lang.fileConfig.getString("eco-balance").replace("<balance>", plugin.economyImplementer.format((plugin.economyImplementer.getBalance(player))));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ServerEssentials.hex(msg)));
                return true;
            }else if (args.length == 1){
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
                if (target.hasPlayedBefore()){
                    if (UserFile.fileConfig.getString(String.valueOf(target.getUniqueId())) == null){
                        UserFile.fileConfig.set(target.getUniqueId() + ".money", plugin.getConfig().getDouble("start-balance"));
                        Eco.saveBalance();
                    }
                    plugin.playerBank.put(target.getUniqueId(), UserFile.fileConfig.getDouble(target.getUniqueId() + ".money"));
                    String msg = Lang.fileConfig.getString("eco-balance-target").replace("<balance>", plugin.economyImplementer.format((plugin.economyImplementer.getBalance(target)))).replace("<player>", target.getName());
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ServerEssentials.hex(msg)));
                    return true;
                }else{
                    String msg = Lang.fileConfig.getString("player-offline");
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ServerEssentials.hex(msg)));
                    return true;
                }
            }
        }
        return false;
    }
}