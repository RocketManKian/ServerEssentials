package me.rocketmankianproductions.serveressentials.file;

import me.rocketmankianproductions.serveressentials.LoggerMessage;
import me.rocketmankianproductions.serveressentials.ServerEssentials;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Lang {

    public static ServerEssentials plugin;

    //settings
    public static String filepath = "lang.yml";

    public static File file;
    public static FileConfiguration fileConfig;

    public static void setup() {
        //setup location.yml
        file = new File(ServerEssentials.getPlugin().getDataFolder(), filepath);
        if (!file.exists()) {
            try {
                //create default file
                file.createNewFile();
                LoggerMessage.log(LoggerMessage.LogLevel.INFO, "Lang.yml file doesn't exist, creating now...");
                fileConfig = YamlConfiguration.loadConfiguration(file);
                fileConfig.addDefault("no-permission-message", "You do not have the required permission (<permission>) to run this command.");
                fileConfig.addDefault("command-timeout", "You cannot use this command for another <time> Seconds");
                fileConfig.addDefault("player-offline", "Player does not exist!");
                fileConfig.addDefault("target-offline", "Target is offline");
                fileConfig.addDefault("invalid-player", "You are not a player");
                fileConfig.addDefault("target-self", "You cannot target yourself.");
                fileConfig.addDefault("back-previous-location", "Teleported to previous location");
                fileConfig.addDefault("back-no-location", "You have no location to return to");
                fileConfig.addDefault("back-blacklisted-world", "Cannot use Back Command in a Blacklisted World");
                fileConfig.addDefault("clear-success", "Inventory cleared");
                fileConfig.addDefault("clear-target-success", "<target>(s) inventory has been cleared");
                fileConfig.addDefault("home-deletion-success", "Home <home> has been successfully deleted");
                fileConfig.addDefault("home-not-found", "Home <home> doesn't exist");
                fileConfig.addDefault("spawn-deletion-success", "Spawn Deleted");
                fileConfig.addDefault("spawn-not-found", "Spawn doesn't exist");
                fileConfig.addDefault("warp-deletion-success", "Warp <warp> has been successfully deleted");
                fileConfig.addDefault("warp-not-found", "Warp <warp> doesn't exist");
                fileConfig.addDefault("enderchest-target-is-sender", "Do /ec to access your own Enderchest");
                fileConfig.addDefault("enderchest-open-success", "Opened <target>(s) Enderchest");
                fileConfig.addDefault("feed-sender-message", "You have fed <target>");
                fileConfig.addDefault("feed-target-message", "You have been fed by <sender>");
                fileConfig.addDefault("feed-self", "You have fed yourself");
                fileConfig.addDefault("fly-enabled", "Flying enabled");
                fileConfig.addDefault("fly-disabled", "Flying disabled");
                fileConfig.addDefault("fly-target-enabled", "<target> can now fly");
                fileConfig.addDefault("fly-target-disabled", "<target> can no longer fly");
                fileConfig.addDefault("gamemode-creative-self", "You are now in Creative");
                fileConfig.addDefault("gamemode-creative-target", "Set <target> into Creative");
                fileConfig.addDefault("gamemode-survival-self", "You are now in Survival");
                fileConfig.addDefault("gamemode-survival-target", "Set <target> into Survival");
                fileConfig.addDefault("gamemode-spectator-self", "You are now in Spectator");
                fileConfig.addDefault("gamemode-spectator-target", "Set <target> into Spectator");
                fileConfig.addDefault("gamemode-adventure-self", "You are now in Adventure");
                fileConfig.addDefault("gamemode-adventure-target", "Set <target> into Adventure");
                fileConfig.addDefault("god-enabled", "Godmode Enabled");
                fileConfig.addDefault("god-disabled", "Godmode Disabled");
                fileConfig.addDefault("god-enabled-target", "Godmode Enabled for <target>");
                fileConfig.addDefault("god-disabled-target", "Godmode Disabled for <target>");
                fileConfig.addDefault("god-target-is-sender", "Use /god to set yourself into Godmode");
                fileConfig.addDefault("hat-success", "You are now wearing <hat>");
                fileConfig.addDefault("hat-invalid", "You can't wear that!");
                fileConfig.addDefault("heal-self", "You have healed yourself");
                fileConfig.addDefault("heal-target", "You have healed <target>");
                fileConfig.addDefault("heal-target-message", "You have been healed by <sender>");
                fileConfig.addDefault("home-set-success", "Successfully set home location");
                fileConfig.addDefault("home-blacklisted-world", "You cannot set a Home in a Blacklisted World");
                fileConfig.addDefault("home-max-homes", "You cannot set any more homes. (Max Homes: <max>)");
                fileConfig.addDefault("home-subtitle", "Successfully teleported to <home>");
                fileConfig.addDefault("home-message", "Successfully teleported to <home>");
                fileConfig.addDefault("home-wait-message", "Teleporting to <home> in <time> Seconds");
                fileConfig.addDefault("home-invalid", "Home <home> doesn't exist!");
                fileConfig.addDefault("home-gui-name", "Home GUI");
                fileConfig.addDefault("home-file-error", "home.yml file is empty or null");
                fileConfig.addDefault("home-gui-left-click", "Click to teleport to <home>");
                fileConfig.addDefault("home-gui-right-click", "Right Click to Delete Home");
                fileConfig.addDefault("no-homes-set", "No Homes have been set");
                fileConfig.addDefault("no-homes-set-target", "<target> hasn't set any Homes");
                fileConfig.addDefault("home-gui-error", "GUI Size is too small, increase the value in Config!");
                fileConfig.addDefault("home-teleport-target", "You have been teleported to <target>(s) home");
                fileConfig.addDefault("home-deleted", "Home <home> has been successfully deleted");
                fileConfig.addDefault("sendhome-target", "You have been teleported to your home");
                fileConfig.addDefault("sendhome-player", "Teleported <target> to their home");
                fileConfig.addDefault("home-movement-cancel", "Teleportation to Home cancelled due to Movement");
                fileConfig.addDefault("hurt-target", "<target> was hurt for <damage> damage points");
                fileConfig.addDefault("hurt-invalid-number", "<damage> is not a valid number");
                fileConfig.addDefault("invsee-target-is-sender", "You cannot open your own inventory");
                fileConfig.addDefault("invsee-armor-gui", "Equipped Armor");
                fileConfig.addDefault("kill-self", "You just killed yourself");
                fileConfig.addDefault("kill-target", "You just killed <target>");
                fileConfig.addDefault("lore-invalid-item", "Please hold a valid item to set the lore");
                fileConfig.addDefault("lore-successful", "Successfully set item lore as <lore>");
                fileConfig.addDefault("message-self", "You cannot message yourself");
                fileConfig.addDefault("message-disabled", "That person has messaging disabled");
                fileConfig.addDefault("message-toggle-enabled", "Incoming Messages have been Disabled");
                fileConfig.addDefault("message-toggle-disabled", "Incoming Messages have been Enabled");
                fileConfig.addDefault("reply-no-message", "There is no message to reply to");
                fileConfig.addDefault("playtime-self", "You have played for <days> Days <hours> Hours <minutes> Minutes <seconds> Seconds");
                fileConfig.addDefault("playtime-target", "<target> has played for <days> Days <hours> Hours <minutes> Minutes <seconds> Seconds");
                fileConfig.addDefault("rename-invalid-item", "Please hold a valid item to rename");
                fileConfig.addDefault("rename-successful", "Successfully set item name as <name>");
                fileConfig.addDefault("repair-durability-max", "Durability is max");
                fileConfig.addDefault("repair-successful", "<item> has been repaired");
                fileConfig.addDefault("repair-invalid-item", "You cannot repair that item");
                fileConfig.addDefault("repair-all-items", "Repaired all item(s)");
                fileConfig.addDefault("report-self", "You cannot report yourself");
                fileConfig.addDefault("report-successful", "Report sent successfully");
                fileConfig.addDefault("spawn-set-successful", "Successfully set spawn location in <world>");
                fileConfig.addDefault("spawn-successful", "Successfully teleported to Spawn");
                fileConfig.addDefault("spawn-wait-message", "Teleporting to Spawn in <time> Seconds");
                fileConfig.addDefault("spawn-teleport-target", "You have teleported <target> to Spawn");
                fileConfig.addDefault("spawn-teleport-target-success", "You have been teleported to Spawn");
                fileConfig.addDefault("spawn-invalid", "Spawn hasn't been set");
                fileConfig.addDefault("spawn-movement-cancel", "Teleportation to Spawn cancelled due to Movement");
                fileConfig.addDefault("warp-set-successful", "Successfully set Warp location");
                fileConfig.addDefault("warp-blacklisted-world", "You cannot set a Warp in a Blacklisted World");
                fileConfig.addDefault("warp-invalid", "Warp doesn't exist");
                fileConfig.addDefault("warp-subtitle", "Warped to <warp>");
                fileConfig.addDefault("warp-message", "Successfully warped to <warp>");
                fileConfig.addDefault("warp-wait-message", "Warping to <warp> in <time> Seconds");
                fileConfig.addDefault("warp-gui-name", "Warp GUI");
                fileConfig.addDefault("warp-gui-left-click", "Click to teleport to <warp>");
                fileConfig.addDefault("warp-gui-right-click", "Right click to Delete Warp");
                fileConfig.addDefault("no-warps-set", "No Warps have been set");
                fileConfig.addDefault("warp-gui-invalid", "GUI Size is too small, increase the value in Config");
                fileConfig.addDefault("warp-file-error", "warp.yml file is empty or null");
                fileConfig.addDefault("warp-deleted", "Warp <warp> has been successfully deleted");
                fileConfig.addDefault("sendwarp-target", "Successfully warped to <warp>");
                fileConfig.addDefault("sendwarp-player", "Successfully sent <target> to <warp>");
                fileConfig.addDefault("warp-movement-cancel", "Warping cancelled due to Movement");
                fileConfig.addDefault("silentjoin-enabled", "SilentJoin has been Enabled");
                fileConfig.addDefault("silentjoin-disabled", "SilentJoin has been Disabled");
                fileConfig.addDefault("socialspy-enabled", "SocialSpy has been Enabled");
                fileConfig.addDefault("socialspy-disabled", "SocialSpy has been Disabled");
                fileConfig.addDefault("speed-invalid-number", "Please provide a speed from 1-10");
                fileConfig.addDefault("speed-fly-success", "Set flying speed to <speed>");
                fileConfig.addDefault("speed-walk-success", "Set walking speed to <speed>");
                fileConfig.addDefault("staffchat-enabled", "StaffChat has been Enabled");
                fileConfig.addDefault("staffchat-disabled", "StaffChat has been Disabled");
                fileConfig.addDefault("teleport-self", "You cannot teleport to yourself");
                fileConfig.addDefault("teleport-success", "Teleported to <target>");
                fileConfig.addDefault("teleport-target-success", "<sender> has teleported to you");
                fileConfig.addDefault("teleport-target-to-self", "You cannot teleport someone to themself");
                fileConfig.addDefault("teleport-others", "Teleported <target> to <target2>");
                fileConfig.addDefault("teleport-force-target", "You have been teleported to <target>");
                fileConfig.addDefault("teleport-no-players-online", "No other players are online right now");
                fileConfig.addDefault("teleport-all-message", "Teleported <amount> player(s) to you");
                fileConfig.addDefault("teleport-pos-success", "Teleported to chosen coordinates");
                fileConfig.addDefault("teleport-pos-invalid", "Please enter valid coordinates...");
                fileConfig.addDefault("teleport-pos-target-success", "You have been teleported");
                fileConfig.addDefault("teleport-pos-target-message", "Teleported <target> to chosen coordinates");
                fileConfig.addDefault("teleport-request-blacklisted-world", "Target is in a Blacklisted World");
                fileConfig.addDefault("teleport-request-sent", "You sent a teleport request to <target>");
                fileConfig.addDefault("teleport-request-cancel-warning", "To cancel this request, type /tpacancel");
                fileConfig.addDefault("teleport-request-target-receive", "<sender> sent a teleport request to you");
                fileConfig.addDefault("teleport-request-accept", "To accept, type /tpaccept");
                fileConfig.addDefault("teleport-request-deny","To deny, type /tpdeny");
                fileConfig.addDefault("teleport-request-timeout-warning", "This request will timeout in <time> Seconds");
                fileConfig.addDefault("teleport-request-timeout", "Teleport request timed out");
                fileConfig.addDefault("teleport-disabled", "That person has teleporting disabled");
                fileConfig.addDefault("teleport-here-blacklisted-world", "Cannot send Teleport Here Request because you are in a Blacklisted World");
                fileConfig.addDefault("teleport-here-request-sent", "You sent a teleport here request to <target>");
                fileConfig.addDefault("teleport-here-request-cancel-warning", "To cancel this request, type /tpacancel");
                fileConfig.addDefault("teleport-here-request-target-receive", "<sender> would like you to teleport to them");
                fileConfig.addDefault("teleport-here-request-accept", "To accept, type /tpaccept");
                fileConfig.addDefault("teleport-here-request-deny","To deny, type /tpdeny");
                fileConfig.addDefault("teleport-here-request-timeout-warning", "This request will timeout in <time> Seconds");
                fileConfig.addDefault("teleport-here-request-timeout", "Teleport here request timed out");
                fileConfig.addDefault("teleport-cancel", "You have cancelled all outgoing Teleport Requests");
                fileConfig.addDefault("teleport-no-request", "There's no request to cancel");
                fileConfig.addDefault("teleport-no-request-accept", "There's no request to accept");
                fileConfig.addDefault("teleport-no-request-deny", "There's no request to deny");
                fileConfig.addDefault("teleport-accept-request", "<sender> has accepted the teleport request");
                fileConfig.addDefault("teleport-deny-request", "<sender> has denied the teleport request");
                fileConfig.addDefault("teleport-accept-request-target", "Successfully accepted <target>(s) Teleport Request");
                fileConfig.addDefault("teleport-deny-request-target", "Successfully denied <target>(s) Teleport Request");
                fileConfig.addDefault("teleport-wait-message", "Teleporting to <player> in <time> Seconds");
                fileConfig.addDefault("teleport-toggle-enabled", "Teleport Requests have been Disabled");
                fileConfig.addDefault("teleport-toggle-disabled", "Teleport Requests have been Enabled");
                fileConfig.addDefault("teleport-movement-cancel", "Teleportation cancelled due to Movement");
                fileConfig.addDefault("time-sunrise", "Time set to Sunrise");
                fileConfig.addDefault("time-day", "Time set to Day");
                fileConfig.addDefault("time-sunset", "Time set to Sunset");
                fileConfig.addDefault("time-midnight", "Time set to Midnight");
                fileConfig.addDefault("weather-sun", "Weather changed to Sun");
                fileConfig.addDefault("weather-storm", "Weather changed to Storm");
                fileConfig.addDefault("trash-gui-name", "Trash Chute");
                fileConfig.addDefault("vanish-enabled", "You are now invisible!");
                fileConfig.addDefault("vanish-disabled", "You are now visible to other players on the server");
                fileConfig.addDefault("vanish-target-enabled", "<target> is now invisible");
                fileConfig.addDefault("vanish-target-disabled", "<target> is now visible");
                fileConfig.options().copyDefaults(true);
                fileConfig.save(file);
                LoggerMessage.log(LoggerMessage.LogLevel.SUCCESS, "Lang.yml file created");
            } catch (IOException e) {
                ServerEssentials.plugin.getLogger().warning(e.toString());
            }
        } else {
            fileConfig = YamlConfiguration.loadConfiguration(file);
        }
    }

    public static void reload() {
        fileConfig = YamlConfiguration.loadConfiguration(file);
    }
}