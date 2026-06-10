package me.rocketmankianproductions.serveressentials.file;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.utils.JailManagerService;
import me.rocketmankianproductions.serveressentials.utils.JailPlayerUtil;
import me.rocketmankianproductions.serveressentials.utils.JailUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JailFile {

    public static File file;
    public static FileConfiguration config;

    public static void setup() {

        file = new File("plugins/ServerEssentials", "jail.yml");

        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        config = YamlConfiguration.loadConfiguration(file);
    }

    public static void load(JailManagerService manager) {

        // jails
        ConfigurationSection jailSection = config.getConfigurationSection("jails");

        if (jailSection != null) {

            for (String key : jailSection.getKeys(false)) {

                String path = "jails." + key;

                World world = Bukkit.getWorld(config.getString(path + ".world"));
                if (world == null) continue;

                Location loc = new Location(
                        world,
                        config.getDouble(path + ".x"),
                        config.getDouble(path + ".y"),
                        config.getDouble(path + ".z"),
                        (float) config.getDouble(path + ".yaw"),
                        (float) config.getDouble(path + ".pitch")
                );

                manager.addJail(new JailUtil(key, loc));
            }
        }

        // jailed players
        ConfigurationSection playerSection = config.getConfigurationSection("jailedPlayers");

        if (playerSection != null) {

            for (String uuidStr : playerSection.getKeys(false)) {

                String path = "jailedPlayers." + uuidStr;

                UUID uuid = UUID.fromString(uuidStr);

                String jailName = config.getString(path + ".jailName");
                Location location = config.getLocation(path + ".previousLocation");
                String reason = config.getString(path + ".reason");
                long releaseTime = config.getLong(path + ".releaseTime");

                manager.getJailedPlayers().put(
                        uuid,
                        new JailPlayerUtil(uuid, jailName, location, releaseTime, reason)
                );
            }
        }

        Bukkit.getLogger().info("Loaded " +
                manager.getAllJails().size() + " jails and " +
                manager.getJailedPlayers().size() + " jailed players.");
    }

    public static void saveAsync(JailManagerService manager) {
        // 1. Snapshot the maps on the main thread
        Map<String, JailUtil> jails = new HashMap<>(manager.getCache());
        Map<UUID, JailPlayerUtil> players = new HashMap<>(manager.getJailedPlayers());

        // 2. Pre-extract the world names on the main thread into a safe lookup map
        Map<String, String> jailWorldNames = new HashMap<>();
        for (JailUtil jail : jails.values()) {
            if (jail.getLocation() != null && jail.getLocation().getWorld() != null) {
                jailWorldNames.put(jail.getName().toLowerCase(), jail.getLocation().getWorld().getName());
            } else {
                jailWorldNames.put(jail.getName().toLowerCase(), "world"); // fallback default
            }
        }

        Bukkit.getScheduler().runTaskAsynchronously(
                ServerEssentials.getPlugin(),
                () -> {
                    FileConfiguration newConfig = new YamlConfiguration();
                    // Pass the pre-extracted world names into the data builder
                    populateConfig(newConfig, jails, jailWorldNames, players);

                    try {
                        newConfig.save(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );
    }

    public static void saveSync(JailManagerService manager) {
        Map<String, JailUtil> jails = new HashMap<>(manager.getCache());
        Map<UUID, JailPlayerUtil> players = new HashMap<>(manager.getJailedPlayers());

        // Main thread can safely fetch world names directly, but we map it for the shared method
        Map<String, String> jailWorldNames = new HashMap<>();
        for (JailUtil jail : jails.values()) {
            jailWorldNames.put(jail.getName().toLowerCase(), jail.getLocation().getWorld().getName());
        }

        FileConfiguration newConfig = new YamlConfiguration();
        populateConfig(newConfig, jails, jailWorldNames, players);

        try {
            newConfig.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Shared thread-safe helper method for handling the data structures
    private static void populateConfig(FileConfiguration config, Map<String, JailUtil> jails, Map<String, String> worldNames, Map<UUID, JailPlayerUtil> players) {
        for (JailUtil jail : jails.values()) {
            String jailKey = jail.getName().toLowerCase();
            String path = "jails." + jailKey;
            Location loc = jail.getLocation();

            // Safe: pulling the String name from our thread-safe map instead of loc.getWorld()
            config.set(path + ".world", worldNames.get(jailKey));
            config.set(path + ".x", loc.getX());
            config.set(path + ".y", loc.getY());
            config.set(path + ".z", loc.getZ());
            config.set(path + ".yaw", loc.getYaw());
            config.set(path + ".pitch", loc.getPitch());
        }

        for (JailPlayerUtil entry : players.values()) {
            String path = "jailedPlayers." + entry.getPlayerId();
            config.set(path + ".jailName", entry.getJailName());
            config.set(path + ".previousLocation", entry.getPlayerPreviousLocation());
            config.set(path + ".reason", entry.getReason());
            config.set(path + ".releaseTime", entry.getReleaseTime());
        }
    }

    public static void reload() {
        config = YamlConfiguration.loadConfiguration(file);
        ServerEssentials.getInstance.jailManager.clear();
        load(ServerEssentials.getInstance.jailManager);
    }
}