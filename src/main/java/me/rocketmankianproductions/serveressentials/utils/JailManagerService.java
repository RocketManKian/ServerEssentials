package me.rocketmankianproductions.serveressentials.utils;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.api.JailEvent;
import me.rocketmankianproductions.serveressentials.api.JailReleaseEvent;
import me.rocketmankianproductions.serveressentials.commands.Setspawn;
import me.rocketmankianproductions.serveressentials.file.JailFile;
import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class JailManagerService implements Listener {

    private final Map<String, JailUtil> jailCache = new ConcurrentHashMap<>();
    private final Set<UUID> pendingRelease = ConcurrentHashMap.newKeySet();
    private final Map<UUID, JailPlayerUtil> jailedPlayers = new ConcurrentHashMap<>();

    public Map<UUID, JailPlayerUtil> getJailedPlayers() {
        return jailedPlayers;
    }

    public boolean isJailed(Player player) {

        JailPlayerUtil entry = jailedPlayers.get(player.getUniqueId());

        if (entry == null) return false;

        if (entry.isExpired()) {
            releasePlayer(player.getUniqueId());
            return false;
        }

        return true;
    }

    public void addJail(JailUtil jail) {
        jailCache.put(jail.getName().toLowerCase(), jail);
        JailFile.saveAsync(this);
    }

    public void removeJail(String name) {
        jailCache.remove(name.toLowerCase());
        JailFile.saveAsync(this);
    }

    public JailUtil getJail(String name) {
        return jailCache.get(name.toLowerCase());
    }

    // =========================
    // JAIL PLAYER
    // =========================
    public void jailPlayer(Player player, String jailName, int durationSeconds, String durationUnconverted) {

        JailUtil jail = getJail(jailName);
        if (jail == null) {
            player.sendMessage("§cJail not found.");
            return;
        }

        JailEvent event = new JailEvent(player, jailName, durationSeconds);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            player.sendMessage("§cYour jail was cancelled by a plugin.");
            return;
        }

        long releaseTime = System.currentTimeMillis() + (durationSeconds * 1000L);

        JailPlayerUtil util = new JailPlayerUtil(
                player.getUniqueId(),
                jailName,
                releaseTime
        );

        jailedPlayers.put(player.getUniqueId(), util);

        JailFile.saveAsync(this);

        player.teleport(jail.getLocation());

        player.sendMessage(
                ServerEssentials.hex(
                        Lang.fileConfig.getString("jail-target")
                                .replace("<duration>", String.valueOf(durationUnconverted))
                )
        );
    }

    // =========================
    // RELEASE PLAYER
    // =========================
    public void releasePlayer(UUID uuid) {

        JailPlayerUtil entry = jailedPlayers.remove(uuid);

        pendingRelease.remove(uuid);

        if (entry == null) return;

        Player player = Bukkit.getPlayer(uuid);

        if (player != null && player.isOnline()) {
            player.sendMessage(ServerEssentials.hex(Lang.fileConfig.getString("jail-target-release")));

            if (Setspawn.fileConfig.getString("Location.World") != null) {

                World world = Bukkit.getWorld(Setspawn.fileConfig.getString("Location.World"));

                if (world != null) {

                    Location loc = new Location(
                            world,
                            Setspawn.fileConfig.getDouble("Location.X"),
                            Setspawn.fileConfig.getDouble("Location.Y"),
                            Setspawn.fileConfig.getDouble("Location.Z"),
                            Setspawn.fileConfig.getInt("Location.Yaw"),
                            Setspawn.fileConfig.getInt("Location.Pitch")
                    );

                    player.teleport(loc);
                }
            }
            Bukkit.getPluginManager().callEvent(
                    new JailReleaseEvent(player)
            );
            JailFile.saveAsync(this);
        } else {
            // ✔ KEEP PERSISTENT UNTIL JOIN HANDLES IT
            pendingRelease.add(uuid);
        }
    }

    // =========================
    // TIME LEFT
    // =========================
    public String getTimeLeft(JailPlayerUtil util) {

        if (util == null) {
            return "0s";
        }

        long millis = util.getReleaseTime() - System.currentTimeMillis();

        if (millis <= 0) {
            return "0s";
        }

        long totalSeconds = millis / 1000;

        long weeks = totalSeconds / (60 * 60 * 24 * 7);
        totalSeconds %= (60 * 60 * 24 * 7);

        long days = totalSeconds / (60 * 60 * 24);
        totalSeconds %= (60 * 60 * 24);

        long hours = totalSeconds / (60 * 60);
        totalSeconds %= (60 * 60);

        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;

        StringBuilder builder = new StringBuilder();

        if (weeks > 0) builder.append(weeks).append("w ");
        if (days > 0) builder.append(days).append("d ");
        if (hours > 0) builder.append(hours).append("h ");
        if (minutes > 0) builder.append(minutes).append("m ");
        if (seconds > 0 || builder.isEmpty()) builder.append(seconds).append("s");

        return builder.toString().trim();
    }

    // =========================
    // JOIN HANDLER
    // =========================
    public void handlePlayerJoin(Player player) {

        UUID uuid = player.getUniqueId();

        // ✔ If they were released while offline
        if (pendingRelease.remove(uuid)) {
            player.sendMessage(ServerEssentials.hex(Lang.fileConfig.getString("jail-target-release")));

            if (Setspawn.fileConfig.getString("Location.World") != null) {

                World world = Bukkit.getWorld(Setspawn.fileConfig.getString("Location.World"));

                if (world != null) {

                    Location loc = new Location(
                            world,
                            Setspawn.fileConfig.getDouble("Location.X"),
                            Setspawn.fileConfig.getDouble("Location.Y"),
                            Setspawn.fileConfig.getDouble("Location.Z"),
                            Setspawn.fileConfig.getInt("Location.Yaw"),
                            Setspawn.fileConfig.getInt("Location.Pitch")
                    );

                    player.teleport(loc);
                }
            }

            JailFile.saveAsync(this);

            return;
        }

        // ✔ still jailed
        JailPlayerUtil entry = jailedPlayers.get(uuid);

        if (entry == null) return;

        if (entry.isExpired()) {
            releasePlayer(uuid);
            return;
        }

        JailUtil jail = getJail(entry.getJailName());
        if (jail == null) return;

        player.teleport(jail.getLocation());
        player.sendMessage(ServerEssentials.hex(Lang.fileConfig.getString("jail-target-attempt").replace("<duration>", getTimeLeft(entry))));
    }

    // =========================
    // AUTO RELEASE TASK (SAFE)
    // =========================
    public void startAutoReleaseTask() {

        Bukkit.getScheduler().runTaskTimer(
                ServerEssentials.getPlugin(),
                () -> {

                    Iterator<Map.Entry<UUID, JailPlayerUtil>> it =
                            jailedPlayers.entrySet().iterator();

                    while (it.hasNext()) {

                        Map.Entry<UUID, JailPlayerUtil> entry = it.next();

                        if (!entry.getValue().isExpired()) continue;

                        UUID uuid = entry.getKey();

                        releasePlayer(uuid); // ✔ ALL logic centralized here
                    }

                },
                20L,
                20L
        );
    }

    // =========================
// BLOCK BLOCK BREAK
// =========================
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {

        if (!isJailed(event.getPlayer())) return;

        event.setCancelled(true);
        JailPlayerUtil entry = jailedPlayers.get(event.getPlayer().getUniqueId());
        event.getPlayer().sendMessage(ServerEssentials.hex(Lang.fileConfig.getString("jail-target-attempt").replace("<duration>", getTimeLeft(entry))));
    }

    // =========================
// BLOCK BLOCK PLACE
// =========================
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {

        if (!isJailed(event.getPlayer())) return;

        event.setCancelled(true);
        JailPlayerUtil entry = jailedPlayers.get(event.getPlayer().getUniqueId());
        event.getPlayer().sendMessage(ServerEssentials.hex(Lang.fileConfig.getString("jail-target-attempt").replace("<duration>", getTimeLeft(entry))));
    }

    // =========================
// BLOCK COMMANDS
// =========================
    private final Set<String> allowedCommands = Set.of(
            "/jailtime",
            "/msg",
            "/r"
    );

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {

        Player player = event.getPlayer();

        if (!isJailed(player)) return;

        String message = event.getMessage().toLowerCase();

        for (String allowed : allowedCommands) {
            if (message.startsWith(allowed)) {
                return;
            }
        }

        event.setCancelled(true);
        JailPlayerUtil entry = jailedPlayers.get(player.getUniqueId());
        event.getPlayer().sendMessage(ServerEssentials.hex(Lang.fileConfig.getString("jail-target-attempt").replace("<duration>", getTimeLeft(entry))));
    }

    // =========================
// BLOCK ITEM DROP
// =========================
    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {

        if (!isJailed(event.getPlayer())) return;

        event.setCancelled(true);
    }

    // =========================
// BLOCK ITEM PICKUP
// =========================
    @EventHandler
    public void onPickup(EntityPickupItemEvent event) {

        if (!(event.getEntity() instanceof Player player)) return;

        if (!isJailed(player)) return;

        event.setCancelled(true);
    }

    // =========================
// BLOCK PVP
// =========================
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {

        if (!(event.getDamager() instanceof Player player)) return;

        if (!isJailed(player)) return;

        event.setCancelled(true);
        JailPlayerUtil entry = jailedPlayers.get(player.getUniqueId());
        player.sendMessage(ServerEssentials.hex(Lang.fileConfig.getString("jail-target-attempt").replace("<duration>", getTimeLeft(entry))));
    }

    // =========================
// PREVENT ESCAPING JAIL
// =========================
    @EventHandler
    public void onMove(PlayerMoveEvent event) {

        Player player = event.getPlayer();

        if (!isJailed(player)) return;

        JailPlayerUtil entry = jailedPlayers.get(player.getUniqueId());
        if (entry == null) return;

        JailUtil jail = getJail(entry.getJailName());
        if (jail == null) return;

        // radius check
        double maxDistance = 15.0;

        if (!player.getWorld().equals(jail.getLocation().getWorld())) {
            player.teleport(jail.getLocation());
            return;
        }

        if (player.getLocation().distance(jail.getLocation()) > maxDistance) {
            player.teleport(jail.getLocation());

            player.sendMessage(
                    ServerEssentials.hex(
                            Lang.fileConfig.getString("jail-target-attempt")
                                    .replace("<duration>", getTimeLeft(entry))
                    )
            );
        }
    }

    // =========================
    // CACHE ACCESS
    // =========================
    public Collection<JailUtil> getAllJails() {
        return Collections.unmodifiableCollection(jailCache.values());
    }

    public Map<String, JailUtil> getCache() {
        return jailCache;
    }

    public void clear() {
        jailCache.clear();
    }
}