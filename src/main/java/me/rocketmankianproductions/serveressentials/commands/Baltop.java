package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.UserFile;
import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Baltop implements CommandExecutor {
    private ServerEssentials plugin = ServerEssentials.getInstance;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (sender instanceof Player) {
            if (ServerEssentials.permissionChecker(((Player) sender).getPlayer(), "se.baltop")) {
                String msg = Lang.fileConfig.getString("eco-baltop");
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ServerEssentials.hex(msg)));
                if (getSortedBalances().isEmpty()){
                    String msg2 = Lang.fileConfig.getString("eco-baltop-empty");
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ServerEssentials.hex(msg2)));
                }
                int rank = 1;
                for (Map.Entry<String, Double> entry : getSortedBalances().entrySet()) {
                    if (rank == 11) {
                        break;
                    }
                    String playername = Bukkit.getOfflinePlayer(UUID.fromString(entry.getKey())).getName();
                    String name = playername != null ? playername : entry.getKey();
                    String msg3 = Lang.fileConfig.getString("eco-baltop-player").replace("<number>", "" + rank).replace("<player>", name).replace("<balance>", "" + plugin.economyImplementer.format(entry.getValue()));
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ServerEssentials.hex(msg3)));
                    rank ++;
                }
            }
        }
        return false;
    }

    private Map<String, Double> getSortedBalances() {
        Map<String, Double> balances = new HashMap<>();

        // Iterate over the configuration section to extract UUIDs and money values
        for (String uuid : UserFile.fileConfig.getKeys(false)) {
            double money = UserFile.fileConfig.getDouble(uuid + ".money", 0.0);
            balances.put(uuid, money);
        }

        // Sort the balances by money in descending order
        List<Map.Entry<String, Double>> entries = new ArrayList<>(balances.entrySet());
        entries.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        // Convert sorted entries back to a LinkedHashMap to preserve the sorted order
        Map<String, Double> sortedBalances = new LinkedHashMap<>();
        for (Map.Entry<String, Double> entry : entries) {
            sortedBalances.put(entry.getKey(), entry.getValue());
        }

        return sortedBalances;
    }
}
