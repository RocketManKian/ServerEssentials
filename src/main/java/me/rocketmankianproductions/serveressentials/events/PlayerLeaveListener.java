package me.rocketmankianproductions.serveressentials.events;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.commands.SilentJoin;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeaveListener implements Listener {

    @EventHandler
    void onPlayerLeave(PlayerQuitEvent pj) {
        Player player = pj.getPlayer();
        if (ServerEssentials.getPlugin().getConfig().getBoolean("enable-leave-message")) {
            if (SilentJoin.fileConfig.getBoolean("silent." + player.getName(), false) == false) {
                String lm = ServerEssentials.getPlugin().getConfig().getString("leave-symbol");
                pj.setQuitMessage(ChatColor.translateAlternateColorCodes('&', lm + " " + player.getPlayerListName()));
            } else {
                pj.setQuitMessage("");
            }
        }else{
            if (SilentJoin.fileConfig.getBoolean("silent." + player.getName(), true) == true){
                pj.setQuitMessage("");
            }else{
                // Default Leave Message
            }
        }
        // Statistic
        // Converting the playtime stored as 20 ticks per second into Days, Hours, Minutes and Seconds.
        int ticks = player.getStatistic(Statistic.PLAY_ONE_MINUTE);
        int rest = 0;
        // Ticks divided by 20 = seconds. Seconds x 60 = Minute. Minute x 60 = Hour. Hour x 24 = Day.
        int days = ticks / (20 * 3600 * 24);
        rest = ticks % (20 * 3600 * 24);
        int hours = rest / (20 * 3600);
        rest = rest % (20 * 3600);
        int minutes = rest / (20 * 60);
        rest = rest % (20 * 60);
        int seconds = rest / 20;
        // Vanish
        ServerEssentials.getPlugin().invisible_list.remove(player);
    }
}