package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.api.PlayerUnAFKEvent;
import me.rocketmankianproductions.serveressentials.file.Lang;
import me.rocketmankianproductions.serveressentials.utils.AFKManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.jetbrains.annotations.NotNull;

public class AFK implements CommandExecutor, Listener {

    public AFK() {
        // AFK inactivity checker
        Bukkit.getScheduler().runTaskTimer(
                ServerEssentials.getPlugin(),
                this::checkAFKPlayers,
                20L,
                20L * 30 // every 30 seconds
        );
    }

    /* ===============================
       Command
       =============================== */
    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                             @NotNull Command command,
                             @NotNull String label,
                             @NotNull String[] args) {

        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;

        if (!ServerEssentials.permissionChecker(player, "se.afk")) return true;

        AFKManager.updateActivity(player);

        if (!AFKManager.isAFK(player)) {
            setAFK(player);
        } else {
            removeAFK(player);
        }

        return true;
    }

    /* ===============================
       AFK State Changes
       =============================== */
    private void setAFK(Player player) {
        player.sendMessage(ChatColor.translateAlternateColorCodes(
                '&', Lang.fileConfig.getString("afk-active")
        ));
        player.setSleepingIgnored(true);
        AFKManager.setAFK(player, true);
    }

    private void removeAFK(Player player) {
        player.sendMessage(ChatColor.translateAlternateColorCodes(
                '&', Lang.fileConfig.getString("afk-inactive")
        ));
        player.setSleepingIgnored(false);
        AFKManager.setAFK(player, false);

        // Fire the event safely
        Bukkit.getScheduler().runTask(ServerEssentials.getPlugin(),
                () -> Bukkit.getPluginManager().callEvent(new PlayerUnAFKEvent(player))
        );
    }

    /* ===============================
       AFK Timeout Check
       =============================== */
    private void checkAFKPlayers() {
        long now = System.currentTimeMillis();

        long afkTimeoutSeconds = ServerEssentials.plugin.getConfig().getLong("afk-timer", 300); // default 300 seconds
        long AFK_TIMEOUT = afkTimeoutSeconds * 1000L; // milliseconds

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (AFKManager.isAFK(player)) continue;

            long last = AFKManager.getLastActivity(player);
            if (now - last >= AFK_TIMEOUT) {
                setAFK(player);
            }
        }
    }

    /* ===============================
       Activity Listeners
       =============================== */
    private void handleActivity(Player player, boolean asyncSafe) {
        AFKManager.updateActivity(player);

        if (AFKManager.isAFK(player)) {
            if (asyncSafe) {
                Bukkit.getScheduler().runTask(ServerEssentials.getPlugin(), () -> removeAFK(player));
            } else {
                removeAFK(player);
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        handleActivity(event.getPlayer(), false);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        handleActivity(event.getPlayer(), true); // async event
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        handleActivity((Player) event.getWhoClicked(), false);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        handleActivity(event.getPlayer(), false);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!event.getFrom().getBlock().equals(event.getTo().getBlock())) {
            handleActivity(event.getPlayer(), false);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        handleActivity(event.getEntity(), false);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        AFKManager.updateActivity(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        AFKManager.remove(event.getPlayer());
    }
}