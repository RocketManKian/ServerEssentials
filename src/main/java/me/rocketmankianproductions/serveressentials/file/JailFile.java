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
                long releaseTime = config.getLong(path + ".releaseTime");

                manager.getJailedPlayers().put(
                        uuid,
                        new JailPlayerUtil(uuid, jailName, releaseTime)
                );
            }
        }

        Bukkit.getLogger().info("Loaded " +
                manager.getAllJails().size() + " jails and " +
                manager.getJailedPlayers().size() + " jailed players.");
    }

    public static void saveAsync(JailManagerService manager) {

        Map<String, JailUtil> jails = new HashMap<>(manager.getCache());
        Map<UUID, JailPlayerUtil> players = new HashMap<>(manager.getJailedPlayers());

        Bukkit.getScheduler().runTaskAsynchronously(
                ServerEssentials.getPlugin(),
                () -> {

                    FileConfiguration newConfig = new YamlConfiguration();

                    for (JailUtil jail : jails.values()) {

                        String path = "jails." + jail.getName().toLowerCase();
                        Location loc = jail.getLocation();

                        newConfig.set(path + ".world", loc.getWorld().getName());
                        newConfig.set(path + ".x", loc.getX());
                        newConfig.set(path + ".y", loc.getY());
                        newConfig.set(path + ".z", loc.getZ());
                        newConfig.set(path + ".yaw", loc.getYaw());
                        newConfig.set(path + ".pitch", loc.getPitch());
                    }

                    for (JailPlayerUtil entry : players.values()) {

                        String path = "jailedPlayers." + entry.getPlayerId();

                        newConfig.set(path + ".jailName", entry.getJailName());
                        newConfig.set(path + ".releaseTime", entry.getReleaseTime());
                    }

                    try {
                        newConfig.save(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );
    }

    public static void reload() {
        config = YamlConfiguration.loadConfiguration(file);
        ServerEssentials.getInstance.jailManager.clear();
        load(ServerEssentials.getInstance.jailManager);
    }
}