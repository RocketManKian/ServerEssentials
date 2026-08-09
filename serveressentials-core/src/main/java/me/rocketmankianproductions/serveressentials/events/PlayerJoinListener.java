package me.rocketmankianproductions.serveressentials.events;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;
import me.clip.placeholderapi.PlaceholderAPI;
import me.rocketmankianproductions.serveressentials.LoggerMessage;
import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.UpdateChecker.Update;
import me.rocketmankianproductions.serveressentials.commands.*;
import me.rocketmankianproductions.serveressentials.file.UserFile;
import me.rocketmankianproductions.serveressentials.file.Lang;
import me.rocketmankianproductions.serveressentials.utils.AFKManager;
import me.rocketmankianproductions.serveressentials.utils.JailManagerService;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.IOException;

import static me.rocketmankianproductions.serveressentials.ServerEssentials.hex;

public class PlayerJoinListener implements Listener {
    Location loc;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent pj) {
        Player player = pj.getPlayer();

        String uuidPath = player.getUniqueId() + ".nickname";

        // 1. Check if the player has a saved nickname in their file
        if (UserFile.config.contains(uuidPath)) {
            String savedNickname = UserFile.config.getString(uuidPath);
            if (savedNickname != null && !savedNickname.isEmpty()) {
                Nickname.applyNickname(player, savedNickname);
            }else{
                UserFile.config.set(player.getUniqueId() + ".nickname", player.getName());
                try {
                    UserFile.config.save(UserFile.file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //Jail
        ServerEssentials.getInstance.jailManager.handlePlayerJoin(player);

        // Fly
        if (player.hasPermission("se.fly.login")) {
            player.setAllowFlight(true);
            player.setFlying(true);
        }

        // Seen Command
        long currentTime = System.currentTimeMillis();  // Capture current timestamp
        UserFile.config.set(player.getUniqueId() + ".login", currentTime);
        try {
            UserFile.config.save(UserFile.file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Check to see if Update Checker is enabled in Config
        if (ServerEssentials.getPlugin().getConfig().getBoolean("update-checker")){
            // Checking if the player is op and if the plugin has an update
            if ((player.isOp() || player.hasPermission("se.alert")) && ServerEssentials.getPlugin().hasUpdate()) {
                new Update(ServerEssentials.getPlugin(), 86675).getLatestVersion(version -> {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&5--------------------------------"));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7There is a new version of &6ServerEssentials &7available."));
                    TextComponent textComponent = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&6&lDownload"));
                    textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                            new ComponentBuilder("Click to Download").create()));
                    textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.spigotmc.org/resources/server-essentials.86675/"));
                    player.spigot().sendMessage(textComponent);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bLatest version: " + "&a" + version + " &8| &bInstalled version: &c" + ServerEssentials.getPlugin().getDescription().getVersion()));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&5--------------------------------"));
                });
            }
        }

        // Sets default value if player has the permission.
        if (player.hasPermission("se.hidebalance")) {
            if (UserFile.config.getString(player.getUniqueId() + ".balancehidden") == null){
                boolean b = UserFile.config.getBoolean((player.getUniqueId() + ".balancehidden"), false);
                UserFile.config.set((player.getUniqueId() + ".balancehidden"), b);
                try {
                    UserFile.config.save(UserFile.file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // Sets default value if player has the permission.
        if (player.hasPermission("se.silentjoin")) {
            if (UserFile.config.getString(player.getUniqueId() + ".silent") == null){
                boolean b = UserFile.config.getBoolean((player.getUniqueId() + ".silent"), false);
                UserFile.config.set((player.getUniqueId() + ".silent"), b);
                try {
                    UserFile.config.save(UserFile.file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // Decide if Join Message gets posted or not.
        if (player.hasPlayedBefore()) {
            if (!UserFile.config.getBoolean(player.getUniqueId() + ".silent") && !UserFile.config.getBoolean(player.getUniqueId() + ".vanish")) {

                String rawJoinSymbol = Lang.fileConfig.getString("join-symbol");

                // 2. Safely check if the string is null or empty before doing any manipulation
                if (rawJoinSymbol == null || rawJoinSymbol.isEmpty()) {
                    pj.setJoinMessage("");
                } else {
                    // 3. Process placeholders and hex colors
                    String msg = ServerEssentials.hex(rawJoinSymbol).replace("<player>", player.getName());

                    if (ServerEssentials.isConnectedToPlaceholderAPI) {
                        msg = PlaceholderAPI.setPlaceholders(player, msg);
                    }
                    pj.setJoinMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                }
            } else {
                pj.setJoinMessage("");
            }
        } else if (!player.hasPlayedBefore()) {
            if (ServerEssentials.getPlugin().getConfig().getBoolean("enable-first-time-join-message")) {
                String msg = Lang.fileConfig.getString("first-time-join").replace("<player>", player.getName());
                if (ServerEssentials.isConnectedToPlaceholderAPI) {
                    msg = PlaceholderAPI.setPlaceholders(player, msg);
                }
                pj.setJoinMessage(hex(msg));
            }
        }
        if (!player.hasPlayedBefore() && ServerEssentials.getPlugin().getConfig().getBoolean("spawn-on-first-join")){
            // Prioritise the Newbie Spawn
            if (Setspawn.fileConfig.getString("Newbies.Location.World")!= null){
                if (Bukkit.getWorld(Setspawn.fileConfig.getString("Newbies.Location.World")) != null){
                    // Gathering Location
                    float yaw = Setspawn.fileConfig.getInt("Newbies.Location.Yaw");
                    float pitch = Setspawn.fileConfig.getInt("Newbies.Location.Pitch");
                    // Combining location data
                    loc = new Location(Bukkit.getWorld(Setspawn.fileConfig.getString("Newbies.Location.World")), Setspawn.fileConfig.getDouble("Newbies.Location.X"), Setspawn.fileConfig.getDouble("Newbies.Location.Y"), Setspawn.fileConfig.getDouble("Newbies.Location.Z"), yaw, pitch);
                    // Teleporting player to location
                    player.teleport(loc);
                }else{
                    LoggerMessage.log(LoggerMessage.LogLevel.WARNING, "Spawn World isn't loaded");
                }
            }else if (Setspawn.fileConfig.getString("Location.World") != null){
                if (Bukkit.getWorld(Setspawn.fileConfig.getString("Location.World")) != null){
                    // Gathering Location
                    float yaw = Setspawn.fileConfig.getInt("Location.Yaw");
                    float pitch = Setspawn.fileConfig.getInt("Location.Pitch");
                    // Combining location data
                    loc = new Location(Bukkit.getWorld(Setspawn.fileConfig.getString("Location.World")), Setspawn.fileConfig.getDouble("Location.X"), Setspawn.fileConfig.getDouble("Location.Y"), Setspawn.fileConfig.getDouble("Location.Z"), yaw, pitch);
                    // Teleporting player to location
                    player.teleport(loc);
                }else{
                    LoggerMessage.log(LoggerMessage.LogLevel.WARNING, "Spawn World isn't loaded");
                }
            }else{
                LoggerMessage.log(LoggerMessage.LogLevel.WARNING, "World Spawn isn't set");
            }
        }
        // Handles spawn-on-join
        if (ServerEssentials.getPlugin().getConfig().getBoolean("spawn-on-join") && Setspawn.fileConfig.getString("Location.World") != null) {
            // Gathering Location
            float yaw = Setspawn.fileConfig.getInt("Location.Yaw");
            float pitch = Setspawn.fileConfig.getInt("Location.Pitch");
            // Combining location data
            loc = new Location(Bukkit.getWorld(Setspawn.fileConfig.getString("Location.World")), Setspawn.fileConfig.getDouble("Location.X"), Setspawn.fileConfig.getDouble("Location.Y"), Setspawn.fileConfig.getDouble("Location.Z"), yaw, pitch);
            // Teleporting player to location
            player.teleport(loc);
        }

        // Staff
        if (ServerEssentials.getPlugin().getConfig().getBoolean("enable-staff-join-message")){
            if (player.hasPermission("se.staffchat")){
                String channel = ServerEssentials.getPlugin().getConfig().getString("staff-chat-channel-name");
                String servername = ServerEssentials.plugin.getConfig().getString("server-name");
                String server = (servername == null || servername.isEmpty()) ? " has joined the game" : " has joined the " + servername + " Server";
                if (ServerEssentials.isConnectedToDiscordSRV){
                    TextChannel textChannel = DiscordSRV.getPlugin().getDestinationTextChannelForGameChannelName(channel);
                    if (textChannel != null && ServerEssentials.plugin.getConfig().getBoolean("enable-staff-discord-integration")){
                        textChannel.sendMessage("**" + player.getName() + "**" + server).queue();
                    }
                    Bukkit.broadcast(hex(Lang.fileConfig.getString("staff-join-message").replace("<player>", player.getName())), "se.staffchat");
                }else{
                    Bukkit.broadcast(hex(Lang.fileConfig.getString("staff-join-message").replace("<player>", player.getName())), "se.staffchat");
                }
            }
        }

        Long delay = ServerEssentials.getPlugin().getConfig().getLong("motd-delay");
        Long delay2 = delay * 20;
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((ServerEssentials.getPlugin()), new Runnable() {
            public void run() {
                if (!ServerEssentials.isConnectedToPlaceholderAPI && ServerEssentials.plugin.getConfig().getBoolean("enable-motd")) {
                    for (String msg : ServerEssentials.plugin.getConfig().getStringList("motd-message")) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                    }
                } else if (ServerEssentials.isConnectedToPlaceholderAPI && ServerEssentials.plugin.getConfig().getBoolean("enable-motd")){
                    if (ServerEssentials.plugin.getConfig().getBoolean("enable-motd")) {
                        for (String msg : ServerEssentials.plugin.getConfig().getStringList("motd-message")) {
                            String placeholder = PlaceholderAPI.setPlaceholders(player, msg);
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(placeholder)));
                        }
                    }
                }
            }
        }, delay2);

        // Vanish
        if (UserFile.config.getBoolean(player.getUniqueId() + ".vanish")) {
            for (Player people : Bukkit.getOnlinePlayers()){
                if (!people.hasPermission("se.vanish.see")){
                    people.hidePlayer(ServerEssentials.getPlugin(), player);
                }
            }
            String msg = Lang.fileConfig.getString("vanish-enabled");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
        }
    }
}