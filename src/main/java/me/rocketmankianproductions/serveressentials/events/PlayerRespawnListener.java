package me.rocketmankianproductions.serveressentials.events;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.commands.Setspawn;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawnListener implements Listener {

    FileConfiguration config = ServerEssentials.getPlugin().getConfig();
    Location loc;

    @EventHandler(priority = EventPriority.HIGHEST)
    void onPlayerDie(PlayerRespawnEvent re) {
        Player player = re.getPlayer();
        Location loc;
        String name = player.getUniqueId().toString();

        if (ServerEssentials.getPlugin().getConfig().getBoolean("spawn-on-death") && Setspawn.fileConfig.getString("Location.World") != null) {
            // Gathering Location
            float yaw = Setspawn.fileConfig.getInt("Location.Yaw");
            float pitch = Setspawn.fileConfig.getInt("Location.Pitch");
            //Combining location data
            loc = new Location(Bukkit.getWorld(Setspawn.fileConfig.getString("Location.World")), Setspawn.fileConfig.getDouble("Location.X"), Setspawn.fileConfig.getDouble("Location.Y"), Setspawn.fileConfig.getDouble("Location.Z"), yaw, pitch);
            // Set respawn Location
            re.setRespawnLocation(loc);
        }else;
//        if (PrimaryHome.home.get(player.getUniqueId()) != null){
//            HashMap<UUID, String> home = PrimaryHome.home;
//            player.sendMessage(String.valueOf(home));
//            // Gathering Location
//            float yaw = Sethome.fileConfig.getInt("Home." + name + "." + home + ".Yaw");
//            //float pitch = Sethome.fileConfig.getInt("Home." + player.getName() + ".Pitch");
//            loc = new Location(Bukkit.getWorld(Sethome.fileConfig.getString("Home." + name + "." + home + ".World")),
//                    Sethome.fileConfig.getDouble("Home." + name + "." + home + ".X"),
//                    Sethome.fileConfig.getDouble("Home." + name + "." + home + ".Y"),
//                    Sethome.fileConfig.getDouble("Home." + name + "." + home + ".Z"),
//                    yaw, 0);
//            player.sendMessage(home);
//            // Set respawn Location
//            re.setRespawnLocation(loc);
//        }
    }
}