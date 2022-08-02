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
                fileConfig.addDefault("plugins-command", "Hah! Nice try...");
                fileConfig.addDefault("command-timeout", "You cannot use this command for another <time> Seconds");
                fileConfig.addDefault("player-offline", "Player does not exist!");
                fileConfig.addDefault("target-offline", "Target is offline");
                fileConfig.addDefault("console-invalid", "You cannot execute this command in Console");
                fileConfig.addDefault("invalid-player", "You are not a player");
                fileConfig.addDefault("incorrect-format", "Incorrect format! Please use &6<command>");
                fileConfig.addDefault("target-self", "You cannot target yourself.");
                fileConfig.addDefault("first-time-join", "Welcome <player> to the Server!");
                fileConfig.addDefault("join-symbol", "&6[&a+&6]");
                fileConfig.addDefault("leave-symbol", "&6[&c-&6]");
                fileConfig.addDefault("staff-join-message", "&d(&5&lStaff&d) <player> &7has joined the game");
                fileConfig.addDefault("staff-leave-message", "&d(&5&lStaff&d) <player> &7has quit the game");
                fileConfig.addDefault("website-command", "Website Command");
                fileConfig.addDefault("discord-command", "Discord Command");
                fileConfig.addDefault("youtube-command", "YouTube Command");
                fileConfig.addDefault("twitch-command", "Twitch Command");
                fileConfig.addDefault("gui-confirm-name", "&aYes");
                fileConfig.addDefault("gui-deny-name", "&cNo");
                fileConfig.addDefault("back-previous-location", "Teleported to previous location");
                fileConfig.addDefault("back-no-location", "You have no location to return to");
                fileConfig.addDefault("back-blacklisted-world", "Cannot use Back Command in a Blacklisted World");
                fileConfig.addDefault("back-movement-cancel", "Teleportation to previous location cancelled due to Movement");
                fileConfig.addDefault("back-wait-message", "Teleporting to previous location in <time> Seconds");
                fileConfig.addDefault("clear-success", "Inventory cleared");
                fileConfig.addDefault("clear-target-success", "<target>(s) inventory has been cleared");
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
                fileConfig.addDefault("home-deletion-success", "Home <home> has been successfully deleted");
                fileConfig.addDefault("target-home-deletion-success", "<target>(s) Home has been successfully deleted");
                fileConfig.addDefault("home-set-success", "Successfully set home location");
                fileConfig.addDefault("home-blacklisted-world", "You cannot set a Home in a Blacklisted World");
                fileConfig.addDefault("home-world-invalid", "World isn't loaded!");
                fileConfig.addDefault("home-max-homes", "You cannot set any more homes (Max Homes: <max>)");
                fileConfig.addDefault("home-subtitle", "Successfully teleported to <home>");
                fileConfig.addDefault("home-message", "Successfully teleported to <home>");
                fileConfig.addDefault("home-wait-message", "Teleporting to <home> in <time> Seconds");
                fileConfig.addDefault("target-home-wait-message", "Teleporting to <target>(s) Home in <time> Seconds");
                fileConfig.addDefault("home-invalid", "Home <home> doesn't exist!");
                fileConfig.addDefault("home-file-error", "home.yml file is empty or null");
                fileConfig.addDefault("home-gui-name", "Home GUI");
                fileConfig.addDefault("target-home-gui-name", "<target>(s) Home List");
                fileConfig.addDefault("delete-home-gui-name", "Delete Home &6<home>?");
                fileConfig.addDefault("target-delete-home-gui-name", "Delete <target>(s) Home &6<home>?");
                fileConfig.addDefault("home-gui-left-click", "Click to teleport to <home>");
                fileConfig.addDefault("home-gui-right-click", "Right Click to Delete Home");
                fileConfig.addDefault("no-homes-set", "No Homes have been set");
                fileConfig.addDefault("no-homes-set-target", "<target> hasn't set any Homes");
                fileConfig.addDefault("home-gui-error", "GUI Size is too small, increase the value in Config!");
                fileConfig.addDefault("home-teleport-target", "You have teleported to <target>(s) home");
                fileConfig.addDefault("home-movement-cancel", "Teleportation to Home cancelled due to Movement");
                fileConfig.addDefault("listhomes-self", "To check your Homes, type /home");
                fileConfig.addDefault("sendhome-target", "You have been teleported to your home");
                fileConfig.addDefault("sendhome-player", "Teleported <target> to their home");
                fileConfig.addDefault("hurt-target", "<target> was hurt for <damage> damage points");
                fileConfig.addDefault("hurt-invalid-number", "<damage> is not a valid number");
                fileConfig.addDefault("invsee-target-is-sender", "You cannot open your own inventory");
                fileConfig.addDefault("invsee-armor-gui", "Equipped Armor");
                fileConfig.addDefault("kill-self", "You just killed yourself");
                fileConfig.addDefault("kill-target", "You just killed <target>");
                fileConfig.addDefault("lore-invalid-item", "Please hold a valid item to set the lore");
                fileConfig.addDefault("lore-reset-invalid-item", "Please hold a valid item to reset the lore");
                fileConfig.addDefault("lore-successful", "Successfully set item lore as <lore>");
                fileConfig.addDefault("lore-reset-successful", "Successfully reset item lore");
                fileConfig.addDefault("message-self", "You cannot message yourself");
                fileConfig.addDefault("message-disabled", "That person has messaging disabled");
                fileConfig.addDefault("message-toggle-enabled", "Incoming Messages have been Disabled");
                fileConfig.addDefault("message-toggle-disabled", "Incoming Messages have been Enabled");
                fileConfig.addDefault("message-sender", "&eme &6>> &f<target> &7: <message>");
                fileConfig.addDefault("message-recipient", "&f<sender> &6>> &eme &7: <message>");
                fileConfig.addDefault("reply-sender", "&eme &6>> &f<target> &7: <message>");
                fileConfig.addDefault("reply-recipient", "&f<sender> &6>> &eme &7: <message>");
                fileConfig.addDefault("reply-no-message", "There is no message to reply to");
                fileConfig.addDefault("playtime-self", "You have played for <days> Days <hours> Hours <minutes> Minutes <seconds> Seconds");
                fileConfig.addDefault("playtime-target", "<target> has played for <days> Days <hours> Hours <minutes> Minutes <seconds> Seconds");
                fileConfig.addDefault("rename-invalid-item", "Please hold a valid item to rename");
                fileConfig.addDefault("rename-reset-invalid-item", "Please hold a valid item to clear the name");
                fileConfig.addDefault("rename-successful", "Successfully set item name as <name>");
                fileConfig.addDefault("rename-reset-successful", "Successfully reset item name");
                fileConfig.addDefault("repair-durability-max", "Durability is max");
                fileConfig.addDefault("repair-successful", "<item> has been repaired");
                fileConfig.addDefault("repair-invalid-item", "You cannot repair that item");
                fileConfig.addDefault("repair-all-items", "Repaired all item(s)");
                fileConfig.addDefault("report-self", "You cannot report yourself");
                fileConfig.addDefault("report-successful", "Report sent successfully");
                fileConfig.addDefault("report-user-line-one", "&b--------- &cNEW REPORT &b---------");
                fileConfig.addDefault("report-user-line-two", "&cReporter &7>> &f<player>");
                fileConfig.addDefault("report-user-line-three", "&cReported User &7>> &f<target>");
                fileConfig.addDefault("report-user-line-four", "&cReason &7>> &f<message>");
                fileConfig.addDefault("report-user-line-five", "&b-----------------------------");
                fileConfig.addDefault("report-bug-line-one", "&b--------- &cBUG REPORT &b---------");
                fileConfig.addDefault("report-bug-line-two", "&cReporter &7>> &f<player>");
                fileConfig.addDefault("report-bug-line-three", "&cBug &7>> &f<message>");
                fileConfig.addDefault("report-bug-line-four", "&b-----------------------------");
                fileConfig.addDefault("newbies-spawn-deletion-success", "Newbie Spawn Deleted");
                fileConfig.addDefault("spawn-deletion-success", "Spawn Deleted");
                fileConfig.addDefault("spawn-not-found", "Spawn doesn't exist");
                fileConfig.addDefault("spawn-world-invalid", "World isn't loaded!");
                fileConfig.addDefault("newbies-spawn-set-successful", "Successfully set newbie spawn location in <world>");
                fileConfig.addDefault("spawn-set-successful", "Successfully set spawn location in <world>");
                fileConfig.addDefault("newbies-spawn-successful", "Successfully teleported to Newbies Spawn");
                fileConfig.addDefault("spawn-successful", "Successfully teleported to Spawn");
                fileConfig.addDefault("newbies-spawn-wait-message", "Teleporting to Newbies Spawn in <time> Seconds");
                fileConfig.addDefault("spawn-wait-message", "Teleporting to Spawn in <time> Seconds");
                fileConfig.addDefault("newbies-spawn-teleport-target", "You have teleported <target> to Newbies Spawn");
                fileConfig.addDefault("newbies-spawn-teleport-target-success", "You have been teleported to Newbies Spawn");
                fileConfig.addDefault("spawn-teleport-target", "You have teleported <target> to Spawn");
                fileConfig.addDefault("spawn-teleport-target-success", "You have been teleported to Spawn");
                fileConfig.addDefault("newbies-spawn-invalid", "Newbies Spawn hasn't been set");
                fileConfig.addDefault("spawn-invalid", "Spawn hasn't been set");
                fileConfig.addDefault("spawn-movement-cancel", "Teleportation to Spawn cancelled due to Movement");
                fileConfig.addDefault("warp-deletion-success", "Warp <warp> has been successfully deleted");
                fileConfig.addDefault("warp-not-found", "Warp <warp> doesn't exist");
                fileConfig.addDefault("warp-set-successful", "Successfully set Warp location");
                fileConfig.addDefault("warp-set-block-successful", "Successfully set <warp> Warp to <block>");
                fileConfig.addDefault("warp-block-invalid", "You cannot set Warp Block as <item>");
                fileConfig.addDefault("warp-blacklisted-world", "You cannot set a Warp in a Blacklisted World");
                fileConfig.addDefault("warp-world-invalid", "World isn't loaded!");
                fileConfig.addDefault("warp-subtitle", "Warped to <warp>");
                fileConfig.addDefault("warp-message", "Successfully warped to <warp>");
                fileConfig.addDefault("warp-wait-message", "Warping to <warp> in <time> Seconds");
                fileConfig.addDefault("warp-gui-name", "Warp GUI");
                fileConfig.addDefault("delete-warp-gui-name", "Delete Warp &6<warp>?");
                fileConfig.addDefault("warp-gui-left-click", "Click to teleport to <warp>");
                fileConfig.addDefault("warp-gui-right-click", "Right click to Delete Warp");
                fileConfig.addDefault("warp-gui-invalid", "GUI Size is too small, increase the value in Config");
                fileConfig.addDefault("warp-file-error", "warp.yml file is empty or null");
                fileConfig.addDefault("warp-movement-cancel", "Warping cancelled due to Movement");
                fileConfig.addDefault("no-warps-set", "No Warps have been set");
                fileConfig.addDefault("sendwarp-target", "Successfully warped to <warp>");
                fileConfig.addDefault("sendwarp-player", "Successfully sent <target> to <warp>");
                fileConfig.addDefault("silentjoin-enabled", "SilentJoin has been Enabled");
                fileConfig.addDefault("silentjoin-disabled", "SilentJoin has been Disabled");
                fileConfig.addDefault("socialspy-enabled", "SocialSpy has been Enabled");
                fileConfig.addDefault("socialspy-disabled", "SocialSpy has been Disabled");
                fileConfig.addDefault("socialspy-message", "&c[SocialSpy] &f<sender> &6>> &f<target> &7: <message>");
                fileConfig.addDefault("speed-invalid-number", "Please provide a speed from 1-10");
                fileConfig.addDefault("speed-fly-success", "Flying speed is now <speed>");
                fileConfig.addDefault("speed-walk-success", "Walking speed is now <speed>");
                fileConfig.addDefault("speed-reset-success", "Fly and Walk Speed is now reset");
                fileConfig.addDefault("speed-reset-fly-success", "Fly Speed is now reset");
                fileConfig.addDefault("speed-reset-walk-success", "Walk Speed is now reset");
                fileConfig.addDefault("speed-fly-success-target", "Set flying speed to <speed> for <target>");
                fileConfig.addDefault("speed-walk-success-target", "Set walking speed to <speed> for <target>");
                fileConfig.addDefault("speed-reset-success-target", "Successfully reset Fly and Walk Speed for <target>");
                fileConfig.addDefault("staffchat-enabled", "StaffChat has been Enabled");
                fileConfig.addDefault("staffchat-disabled", "StaffChat has been Disabled");
                fileConfig.addDefault("staffchat-message", "&d(&5&lStaff&d) <player>: &7<message>");
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
                fileConfig.addDefault("convert-successful", "Successfully converted <amount> <item> into <total> <block>");
                fileConfig.addDefault("convert-unsuccessful", "You do not have enough <item> to convert");
                fileConfig.addDefault("convert-invalid", "You cannot convert <item>");
                fileConfig.addDefault("sudo-successful", "Successfully ran command <command> for <target>");
                fileConfig.addDefault("sudo-command-invalid", "Command <command> doesn't exist.");
                fileConfig.addDefault("ping-self", "Ping: <ping>");
                fileConfig.addDefault("ping-target", "<target>'s Ping: <ping>");
                fileConfig.options().copyDefaults(true);
                fileConfig.save(file);
                LoggerMessage.log(LoggerMessage.LogLevel.SUCCESS, "Lang.yml file created");
            } catch (IOException e) {
                ServerEssentials.plugin.getLogger().warning(e.toString());
            }
        } else {
            try {
                fileConfig = YamlConfiguration.loadConfiguration(file);
                if (fileConfig.getString("no-permission-message") == null){
                    fileConfig.set("no-permission-message", "You do not have the required permission (<permission>) to run this command.");
                }
                if (fileConfig.getString("plugins-command") == null){
                    fileConfig.set("plugins-command", "Hah! Nice try...");
                }
                if (fileConfig.getString("command-timeout") == null){
                    fileConfig.set("command-timeout", "You cannot use this command for another <time> Seconds");
                }
                if (fileConfig.getString("player-offline") == null){
                    fileConfig.set("player-offline", "Player does not exist!");
                }
                if (fileConfig.getString("target-offline") == null){
                    fileConfig.set("target-offline", "Target is offline");
                }
                if (fileConfig.getString("console-invalid") == null){
                    fileConfig.set("console-invalid", "You cannot execute this command in Console");
                }
                if (fileConfig.getString("invalid-player") == null){
                    fileConfig.set("invalid-player", "You are not a player");
                }
                if (fileConfig.getString("incorrect-format") == null){
                    fileConfig.set("incorrect-format", "Incorrect format! Please use &6<command>");
                }
                if (fileConfig.getString("target-self") == null){
                    fileConfig.set("target-self", "You cannot target yourself.");
                }
                if (fileConfig.getString("first-time-join") == null){
                    fileConfig.set("first-time-join", "Welcome <player> to the Server!");
                }
                if (fileConfig.getString("join-symbol") == null){
                    fileConfig.set("join-symbol", "&6[&a+&6]");
                }
                if (fileConfig.getString("leave-symbol") == null){
                    fileConfig.set("leave-symbol", "&6[&c-&6]");
                }
                if (fileConfig.getString("staff-join-message") == null){
                    fileConfig.set("staff-join-message", "&d(&5&lStaff&d) <player> &7has joined the game");
                }
                if (fileConfig.getString("staff-leave-message") == null){
                    fileConfig.set("staff-leave-message", "&d(&5&lStaff&d) <player> &7has quit the game");
                }
                if (fileConfig.getString("website-command") == null){
                    fileConfig.set("website-command", "Website Command");
                }
                if (fileConfig.getString("discord-command") == null){
                    fileConfig.set("discord-command", "Discord Command");
                }
                if (fileConfig.getString("youtube-command") == null){
                    fileConfig.set("youtube-command", "YouTube Command");
                }
                if (fileConfig.getString("twitch-command") == null){
                    fileConfig.set("twitch-command", "Twitch Command");
                }
                if (fileConfig.getString("gui-confirm-name") == null){
                    fileConfig.set("gui-confirm-name", "&aYes");
                }
                if (fileConfig.getString("gui-deny-name") == null){
                    fileConfig.set("gui-deny-name", "&cNo");
                }
                if (fileConfig.getString("back-previous-location") == null){
                    fileConfig.set("back-previous-location", "Teleported to previous location");
                }
                if (fileConfig.getString("back-no-location") == null){
                    fileConfig.set("back-no-location", "You have no location to return to");
                }
                if (fileConfig.getString("back-blacklisted-world") == null){
                    fileConfig.set("back-blacklisted-world", "Cannot use Back Command in a Blacklisted World");
                }
                if (fileConfig.getString("back-movement-cancel") == null){
                    fileConfig.set("back-movement-cancel", "Teleportation to previous location cancelled due to Movement");
                }
                if (fileConfig.getString("back-wait-message") == null){
                    fileConfig.set("back-wait-message", "Teleporting to previous location in <time> Seconds");
                }
                if (fileConfig.getString("clear-success") == null){
                    fileConfig.set("clear-success", "Inventory cleared");
                }
                if (fileConfig.getString("clear-target-success") == null){
                    fileConfig.set("clear-target-success", "<target>(s) inventory has been cleared");
                }
                if (fileConfig.getString("enderchest-target-is-sender") == null){
                    fileConfig.set("enderchest-target-is-sender", "Do /ec to access your own Enderchest");
                }
                if (fileConfig.getString("enderchest-open-success") == null){
                    fileConfig.set("enderchest-open-success", "Opened <target>(s) Enderchest");
                }
                if (fileConfig.getString("feed-sender-message") == null){
                    fileConfig.set("feed-sender-message", "You have fed <target>");
                }
                if (fileConfig.getString("feed-target-message") == null){
                    fileConfig.set("feed-target-message", "You have been fed by <sender>");
                }
                if (fileConfig.getString("feed-self") == null){
                    fileConfig.set("feed-self", "You have fed yourself");
                }
                if (fileConfig.getString("fly-enabled") == null){
                    fileConfig.set("fly-enabled", "Flying enabled");
                }
                if (fileConfig.getString("fly-disabled") == null){
                    fileConfig.set("fly-disabled", "Flying disabled");
                }
                if (fileConfig.getString("fly-target-enabled") == null){
                    fileConfig.set("fly-target-enabled", "<target> can now fly");
                }
                if (fileConfig.getString("fly-target-disabled") == null){
                    fileConfig.set("fly-target-disabled", "<target> can no longer fly");
                }
                if (fileConfig.getString("gamemode-creative-self") == null){
                    fileConfig.set("gamemode-creative-self", "You are now in Creative");
                }
                if (fileConfig.getString("gamemode-creative-target") == null){
                    fileConfig.set("gamemode-creative-target", "Set <target> into Creative");
                }
                if (fileConfig.getString("gamemode-survival-self") == null){
                    fileConfig.set("gamemode-survival-self", "You are now in Survival");
                }
                if (fileConfig.getString("gamemode-survival-target") == null){
                    fileConfig.set("gamemode-survival-target", "Set <target> into Survival");
                }
                if (fileConfig.getString("gamemode-spectator-self") == null){
                    fileConfig.set("gamemode-spectator-self", "You are now in Spectator");
                }
                if (fileConfig.getString("gamemode-spectator-target") == null){
                    fileConfig.set("gamemode-spectator-target", "Set <target> into Spectator");
                }
                if (fileConfig.getString("gamemode-adventure-self") == null){
                    fileConfig.set("gamemode-adventure-self", "You are now in Adventure");
                }
                if (fileConfig.getString("gamemode-adventure-target") == null){
                    fileConfig.set("gamemode-adventure-target", "Set <target> into Adventure");
                }
                if (fileConfig.getString("god-enabled") == null){
                    fileConfig.set("god-enabled", "Godmode Enabled");
                }
                if (fileConfig.getString("god-disabled") == null){
                    fileConfig.set("god-disabled", "Godmode Disabled");
                }
                if (fileConfig.getString("god-enabled-target") == null){
                    fileConfig.set("god-enabled-target", "Godmode Enabled for <target>");
                }
                if (fileConfig.getString("god-disabled-target") == null){
                    fileConfig.set("god-disabled-target", "Godmode Disabled for <target>");
                }
                if (fileConfig.getString("god-target-is-sender") == null){
                    fileConfig.set("god-target-is-sender", "Use /god to set yourself into Godmode");
                }
                if (fileConfig.getString("hat-success") == null){
                    fileConfig.set("hat-success", "You are now wearing <hat>");
                }
                if (fileConfig.getString("hat-invalid") == null){
                    fileConfig.set("hat-invalid", "You can't wear that!");
                }
                if (fileConfig.getString("hat-self") == null){
                    fileConfig.set("heal-self", "You have healed yourself");
                }
                if (fileConfig.getString("heal-target") == null){
                    fileConfig.set("heal-target", "You have healed <target>");
                }
                if (fileConfig.getString("heal-target-message") == null){
                    fileConfig.set("heal-target-message", "You have been healed by <sender>");
                }
                if (fileConfig.getString("home-deletion-success") == null){
                    fileConfig.set("home-deletion-success", "Home <home> has been successfully deleted");
                }
                if (fileConfig.getString("target-home-deletion-success") == null){
                    fileConfig.set("target-home-deletion-success", "<target>(s) Home has been successfully deleted");
                }
                if (fileConfig.getString("home-set-success") == null){
                    fileConfig.set("home-set-success", "Successfully set home location");
                }
                if (fileConfig.getString("home-blacklisted-world") == null){
                    fileConfig.set("home-blacklisted-world", "You cannot set a Home in a Blacklisted World");
                }
                if (fileConfig.getString("home-world-invalid") == null){
                    fileConfig.set("home-world-invalid", "World isn't loaded!");
                }
                if (fileConfig.getString("home-max-homes") == null){
                    fileConfig.set("home-max-homes", "You cannot set any more homes (Max Homes: <max>)");
                }
                if (fileConfig.getString("home-subtitle") == null){
                    fileConfig.set("home-subtitle", "Successfully teleported to <home>");
                }
                if (fileConfig.getString("home-message") == null){
                    fileConfig.set("home-message", "Successfully teleported to <home>");
                }
                if (fileConfig.getString("home-wait-message") == null){
                    fileConfig.set("home-wait-message", "Teleporting to <home> in <time> Seconds");
                }
                if (fileConfig.getString("target-home-wait-message") == null){
                    fileConfig.set("target-home-wait-message", "Teleporting to <target>(s) Home in <time> Seconds");
                }
                if (fileConfig.getString("home-invalid") == null) {
                    fileConfig.set("home-invalid", "Home <home> doesn't exist!");
                }
                if (fileConfig.getString("home-file-error") == null){
                    fileConfig.set("home-file-error", "home.yml file is empty or null");
                }
                if (fileConfig.getString("home-gui-name") == null){
                    fileConfig.set("home-gui-name", "Home GUI");
                }
                if (fileConfig.getString("target-home-gui-name") == null){
                    fileConfig.set("target-home-gui-name", "<target>(s) Home List");
                }
                if (fileConfig.getString("delete-home-gui-name") == null){
                    fileConfig.set("delete-home-gui-name", "Delete Home &6<home>?");
                }
                if (fileConfig.getString("target-delete-home-gui-name") == null){
                    fileConfig.set("target-delete-home-gui-name", "Delete <target>(s) Home &6<home>?");
                }
                if (fileConfig.getString("home-gui-left-click") == null){
                    fileConfig.set("home-gui-left-click", "Click to teleport to <home>");
                }
                if (fileConfig.getString("home-gui-right-click") == null){
                    fileConfig.set("home-gui-right-click", "Right Click to Delete Home");
                }
                if (fileConfig.getString("no-homes-set") == null){
                    fileConfig.set("no-homes-set", "No Homes have been set");
                }
                if (fileConfig.getString("no-homes-set-target") == null){
                    fileConfig.set("no-homes-set-target", "<target> hasn't set any Homes");
                }
                if (fileConfig.getString("home-gui-error") == null){
                    fileConfig.set("home-gui-error", "GUI Size is too small, increase the value in Config!");
                }
                if (fileConfig.getString("home-teleport-target") == null){
                    fileConfig.set("home-teleport-target", "You have teleported to <target>(s) home");
                }
                if (fileConfig.getString("home-movement-cancel") == null){
                    fileConfig.set("home-movement-cancel", "Teleportation to Home cancelled due to Movement");
                }
                if (fileConfig.getString("listhomes-self") == null){
                    fileConfig.set("listhomes-self", "To check your Homes, type /home");
                }
                if (fileConfig.getString("sendhome-target") == null){
                    fileConfig.set("sendhome-target", "You have been teleported to your home");
                }
                if (fileConfig.getString("sendhome-player") == null){
                    fileConfig.set("sendhome-player", "Teleported <target> to their home");
                }
                if (fileConfig.getString("hurt-target") == null){
                    fileConfig.set("hurt-target", "<target> was hurt for <damage> damage points");
                }
                if (fileConfig.getString("hurt-invalid-number") == null){
                    fileConfig.set("hurt-invalid-number", "<damage> is not a valid number");
                }
                if (fileConfig.getString("invsee-target-is-sender") == null){
                    fileConfig.set("invsee-target-is-sender", "You cannot open your own inventory");
                }
                if (fileConfig.getString("invsee-armor-gui") == null){
                    fileConfig.set("invsee-armor-gui", "Equipped Armor");
                }
                if (fileConfig.getString("kill-self") == null){
                    fileConfig.set("kill-self", "You just killed yourself");
                }
                if (fileConfig.getString("kill-target") == null){
                    fileConfig.set("kill-target", "You just killed <target>");
                }
                if (fileConfig.getString("lore-invalid-item") == null){
                    fileConfig.set("lore-invalid-item", "Please hold a valid item to set the lore");
                }
                if (fileConfig.getString("lore-reset-invalid-item") == null){
                    fileConfig.set("lore-reset-invalid-item", "Please hold a valid item to reset the lore");
                }
                if (fileConfig.getString("lore-successful") == null){
                    fileConfig.set("lore-successful", "Successfully set item lore as <lore>");
                }
                if (fileConfig.getString("lore-reset-successful") == null){
                    fileConfig.set("lore-reset-successful", "Successfully reset item lore");
                }
                if (fileConfig.getString("message-self") == null){
                    fileConfig.set("message-self", "You cannot message yourself");
                }
                if (fileConfig.getString("message-disabled") == null){
                    fileConfig.set("message-disabled", "That person has messaging disabled");
                }
                if (fileConfig.getString("message-toggle-enabled") == null){
                    fileConfig.set("message-toggle-enabled", "Incoming Messages have been Disabled");
                }
                if (fileConfig.getString("message-toggle-disabled") == null){
                    fileConfig.set("message-toggle-disabled", "Incoming Messages have been Enabled");
                }
                if (fileConfig.getString("reply-no-message") == null){
                    fileConfig.set("reply-no-message", "There is no message to reply to");
                }
                if (fileConfig.getString("message-sender") == null){
                    fileConfig.set("message-sender", "&eme &6>> &f<target> &7: <message>");
                }
                if (fileConfig.getString("message-recipient") == null){
                    fileConfig.set("message-recipient", "&f<sender> &6>> &eme &7: <message>");
                }
                if (fileConfig.getString("reply-sender") == null){
                    fileConfig.set("reply-sender", "&eme &6>> &f<target> &7: <message>");
                }
                if (fileConfig.getString("reply-recipient") == null){
                    fileConfig.set("reply-recipient", "&f<sender> &6>> &eme &7: <message>");
                }
                if (fileConfig.getString("playtime-self") == null){
                    fileConfig.set("playtime-self", "You have played for <days> Days <hours> Hours <minutes> Minutes <seconds> Seconds");
                }
                if (fileConfig.getString("playtime-target") == null){
                    fileConfig.set("playtime-target", "<target> has played for <days> Days <hours> Hours <minutes> Minutes <seconds> Seconds");
                }
                if (fileConfig.getString("rename-invalid-item") == null){
                    fileConfig.set("rename-invalid-item", "Please hold a valid item to rename");
                }
                if (fileConfig.getString("rename-reset-invalid-item") == null){
                    fileConfig.set("rename-reset-invalid-item", "Please hold a valid item to clear the name");
                }
                if (fileConfig.getString("rename-successful") == null){
                    fileConfig.set("rename-successful", "Successfully set item name as <name>");
                }
                if (fileConfig.getString("rename-reset-successful") == null){
                    fileConfig.set("rename-reset-successful", "Successfully reset item name");
                }
                if (fileConfig.getString("repair-durability-max") == null){
                    fileConfig.set("repair-durability-max", "Durability is max");
                }
                if (fileConfig.getString("repair-successful") == null){
                    fileConfig.set("repair-successful", "<item> has been repaired");
                }
                if (fileConfig.getString("repair-invalid-item") == null){
                    fileConfig.set("repair-invalid-item", "You cannot repair that item");
                }
                if (fileConfig.getString("repair-all-items") == null){
                    fileConfig.set("repair-all-items", "Repaired all item(s)");
                }
                if (fileConfig.getString("report-self") == null){
                    fileConfig.set("report-self", "You cannot report yourself");
                }
                if (fileConfig.getString("report-successful") == null){
                    fileConfig.set("report-successful", "Report sent successfully");
                }
                if (fileConfig.getString("report-user-line-one") == null){
                    fileConfig.set("report-user-line-one", "&b--------- &cNEW REPORT &b---------");
                }
                if (fileConfig.getString("report-user-line-two") == null){
                    fileConfig.set("report-user-line-two", "&cReporter &7>> &f<player>");
                }
                if (fileConfig.getString("report-user-line-three") == null){
                    fileConfig.set("report-user-line-three", "&cReported User &7>> &f<target>");
                }
                if (fileConfig.getString("report-user-line-four") == null){
                    fileConfig.set("report-user-line-four", "&cReason &7>> &f<message>");
                }
                if (fileConfig.getString("report-user-line-five") == null){
                    fileConfig.set("report-user-line-five", "&b-----------------------------");
                }
                if (fileConfig.getString("report-bug-line-one") == null){
                    fileConfig.set("report-bug-line-one", "&b--------- &cBUG REPORT &b---------");
                }
                if (fileConfig.getString("report-bug-line-two") == null){
                    fileConfig.set("report-bug-line-two", "&cReporter &7>> &f<player>");
                }
                if (fileConfig.getString("report-bug-line-three") == null){
                    fileConfig.set("report-bug-line-three", "&cBug &7>> &f<message>");
                }
                if (fileConfig.getString("report-bug-line-four") == null){
                    fileConfig.set("report-bug-line-four", "&b-----------------------------");
                }
                if (fileConfig.getString("newbies-spawn-deletion-success") == null){
                    fileConfig.set("newbies-spawn-deletion-success", "Newbie Spawn Deleted");
                }
                if (fileConfig.getString("spawn-deletion-success") == null){
                    fileConfig.set("spawn-deletion-success", "Spawn Deleted");
                }
                if (fileConfig.getString("spawn-not-found") == null){
                    fileConfig.set("spawn-not-found", "Spawn doesn't exist");
                }
                if (fileConfig.getString("spawn-world-invalid") == null){
                    fileConfig.set("spawn-world-invalid", "World isn't loaded!");
                }
                if (fileConfig.getString("newbies-spawn-set-successful") == null){
                    fileConfig.set("newbies-spawn-set-successful", "Successfully set newbie spawn location in <world>");
                }
                if (fileConfig.getString("spawn-set-successful") == null){
                    fileConfig.set("spawn-set-successful", "Successfully set spawn location in <world>");
                }
                if (fileConfig.getString("newbies-spawn-successful") == null){
                    fileConfig.set("newbies-spawn-successful", "Successfully teleported to Newbies Spawn");
                }
                if (fileConfig.getString("spawn-successful") == null){
                    fileConfig.set("spawn-successful", "Successfully teleported to Spawn");
                }
                if (fileConfig.getString("newbies-spawn-wait-message") == null){
                    fileConfig.set("newbies-spawn-wait-message", "Teleporting to Newbies Spawn in <time> Seconds");
                }
                if (fileConfig.getString("spawn-wait-message") == null){
                    fileConfig.set("spawn-wait-message", "Teleporting to Spawn in <time> Seconds");
                }
                if (fileConfig.getString("newbies-spawn-teleport-target") == null){
                    fileConfig.set("newbies-spawn-teleport-target", "You have teleported <target> to Newbies Spawn");
                }
                if (fileConfig.getString("newbies-spawn-teleport-target-success") == null){
                    fileConfig.set("newbies-spawn-teleport-target-success", "You have been teleported to Newbies Spawn");
                }
                if (fileConfig.getString("spawn-teleport-target") == null){
                    fileConfig.set("spawn-teleport-target", "You have teleported <target> to Spawn");
                }
                if (fileConfig.getString("spawn-teleport-target-success") == null){
                    fileConfig.set("spawn-teleport-target-success", "You have been teleported to Spawn");
                }
                if (fileConfig.getString("newbies-spawn-invalid") == null){
                    fileConfig.set("newbies-spawn-invalid", "Newbies Spawn hasn't been set");
                }
                if (fileConfig.getString("spawn-invalid") == null){
                    fileConfig.set("spawn-invalid", "Spawn hasn't been set");
                }
                if (fileConfig.getString("spawn-movement-cancel") == null){
                    fileConfig.set("spawn-movement-cancel", "Teleportation to Spawn cancelled due to Movement");
                }
                if (fileConfig.getString("warp-deletion-success") == null){
                    fileConfig.set("warp-deletion-success", "Warp <warp> has been successfully deleted");
                }
                if (fileConfig.getString("warp-not-found") == null){
                    fileConfig.set("warp-not-found", "Warp <warp> doesn't exist");
                }
                if (fileConfig.getString("warp-set-successful") == null){
                    fileConfig.set("warp-set-successful", "Successfully set Warp location");
                }
                if (fileConfig.getString("warp-set-block-successful") == null){
                    fileConfig.set("warp-set-block-successful", "Successfully set <warp> Warp to <block>");
                }
                if (fileConfig.getString("warp-block-invalid") == null){
                    fileConfig.set("warp-block-invalid", "You cannot set Warp Block as <item>");
                }
                if (fileConfig.getString("warp-blacklisted-world") == null){
                    fileConfig.set("warp-blacklisted-world", "You cannot set a Warp in a Blacklisted World");
                }
                if (fileConfig.getString("warp-world-invalid") == null){
                    fileConfig.set("warp-world-invalid", "World isn't loaded!");
                }
                if (fileConfig.getString("warp-subtitle") == null){
                    fileConfig.set("warp-subtitle", "Warped to <warp>");
                }
                if (fileConfig.getString("warp-message") == null){
                    fileConfig.set("warp-message", "Successfully warped to <warp>");
                }
                if (fileConfig.getString("warp-wait-message") == null){
                    fileConfig.set("warp-wait-message", "Warping to <warp> in <time> Seconds");
                }
                if (fileConfig.getString("warp-gui-name") == null){
                    fileConfig.set("warp-gui-name", "Warp GUI");
                }
                if (fileConfig.getString("delete-warp-gui-name") == null){
                    fileConfig.addDefault("delete-warp-gui-name", "Delete Warp &6<warp>?");
                }
                if (fileConfig.getString("warp-gui-left-click") == null){
                    fileConfig.set("warp-gui-left-click", "Click to teleport to <warp>");
                }
                if (fileConfig.getString("warp-gui-right-click") == null){
                    fileConfig.set("warp-gui-right-click", "Right click to Delete Warp");
                }
                if (fileConfig.getString("no-warps-set") == null){
                    fileConfig.set("no-warps-set", "No Warps have been set");
                }
                if (fileConfig.getString("warp-gui-invalid") == null){
                    fileConfig.set("warp-gui-invalid", "GUI Size is too small, increase the value in Config");
                }
                if (fileConfig.getString("warp-movement-cancel") == null){
                    fileConfig.set("warp-movement-cancel", "Warping cancelled due to Movement");
                }
                if (fileConfig.getString("sendwarp-target") == null){
                    fileConfig.set("sendwarp-target", "Successfully warped to <warp>");
                }
                if (fileConfig.getString("sendwarp-player") == null){
                    fileConfig.set("sendwarp-player", "Successfully sent <target> to <warp>");
                }
                if (fileConfig.getString("warp-file-error") == null){
                    fileConfig.set("warp-file-error", "warp.yml file is empty or null");
                }
                if (fileConfig.getString("silentjoin-enabled") == null){
                    fileConfig.set("silentjoin-enabled", "SilentJoin has been Enabled");
                }
                if (fileConfig.getString("silentjoin-disabled") == null){
                    fileConfig.set("silentjoin-disabled", "SilentJoin has been Disabled");
                }
                if (fileConfig.getString("socialspy-enabled") == null){
                    fileConfig.set("socialspy-enabled", "SocialSpy has been Enabled");
                }
                if (fileConfig.getString("socialspy-disabled") == null){
                    fileConfig.set("socialspy-disabled", "SocialSpy has been Disabled");
                }
                if (fileConfig.getString("socialspy-message") == null){
                    fileConfig.set("socialspy-message", "&c[SocialSpy] &f<sender> &6>> &f<target> &7: <message>");
                }
                if (fileConfig.getString("speed-invalid-number") == null){
                    fileConfig.set("speed-invalid-number", "Please provide a speed from 1-10");
                }
                if (fileConfig.getString("speed-fly-success") == null){
                    fileConfig.set("speed-fly-success", "Flying speed is now <speed>");
                }
                if (fileConfig.getString("speed-walk-success") == null){
                    fileConfig.set("speed-walk-success", "Walking speed is now <speed>");
                }
                if (fileConfig.getString("speed-reset-success") == null){
                    fileConfig.set("speed-reset-success", "Fly and Walk Speed is now reset");
                }
                if (fileConfig.getString("speed-reset-fly-success") == null){
                    fileConfig.set("speed-reset-fly-success", "Fly Speed is now reset");
                }
                if (fileConfig.getString("speed-reset-walk-success") == null){
                    fileConfig.set("speed-reset-walk-success", "Walk Speed is now reset");
                }
                if (fileConfig.getString("speed-fly-success-target") == null){
                    fileConfig.set("speed-fly-success-target", "Set flying speed to <speed> for <target>");
                }
                if (fileConfig.getString("speed-walk-success-target") == null){
                    fileConfig.set("speed-walk-success-target", "Set walking speed to <speed> for <target>");
                }
                if (fileConfig.getString("speed-reset-success-target") == null){
                    fileConfig.set("speed-reset-success-target", "Successfully reset Fly and Walk Speed for <target>");
                }
                if (fileConfig.getString("staffchat-enabled") == null){
                    fileConfig.set("staffchat-enabled", "StaffChat has been Enabled");
                }
                if (fileConfig.getString("staffchat-disabled") == null){
                    fileConfig.set("staffchat-disabled", "StaffChat has been Disabled");
                }
                if (fileConfig.getString("staffchat-message") == null){
                    fileConfig.set("staffchat-message", "&d(&5&lStaff&d) <player>: &7<message>");
                }
                if (fileConfig.getString("teleport-self") == null){
                    fileConfig.set("teleport-self", "You cannot teleport to yourself");
                }
                if (fileConfig.getString("teleport-success") == null){
                    fileConfig.set("teleport-success", "Teleported to <target>");
                }
                if (fileConfig.getString("teleport-target-success") == null){
                    fileConfig.set("teleport-target-success", "<sender> has teleported to you");
                }
                if (fileConfig.getString("teleport-target-to-self") == null){
                    fileConfig.set("teleport-target-to-self", "You cannot teleport someone to themself");
                }
                if (fileConfig.getString("teleport-others") == null){
                    fileConfig.set("teleport-others", "Teleported <target> to <target2>");
                }
                if (fileConfig.getString("teleport-force-target") == null){
                    fileConfig.set("teleport-force-target", "You have been teleported to <target>");
                }
                if (fileConfig.getString("teleport-no-players-online") == null){
                    fileConfig.set("teleport-no-players-online", "No other players are online right now");
                }
                if (fileConfig.getString("teleport-all-message") == null){
                    fileConfig.set("teleport-all-message", "Teleported <amount> player(s) to you");
                }
                if (fileConfig.getString("teleport-pos-success") == null){
                    fileConfig.set("teleport-pos-success", "Teleported to chosen coordinates");
                }
                if (fileConfig.getString("teleport-pos-invalid") == null){
                    fileConfig.set("teleport-pos-invalid", "Please enter valid coordinates...");
                }
                if (fileConfig.getString("teleport-pos-target-success") == null){
                    fileConfig.set("teleport-pos-target-success", "You have been teleported");
                }
                if (fileConfig.getString("teleport-pos-target-message") == null){
                    fileConfig.set("teleport-pos-target-message", "Teleported <target> to chosen coordinates");
                }
                if (fileConfig.getString("teleport-request-blacklisted-world") == null){
                    fileConfig.set("teleport-request-blacklisted-world", "Target is in a Blacklisted World");
                }
                if (fileConfig.getString("teleport-request-sent") == null){
                    fileConfig.set("teleport-request-sent", "You sent a teleport request to <target>");
                }
                if (fileConfig.getString("teleport-request-cancel-warning") == null){
                    fileConfig.set("teleport-request-cancel-warning", "To cancel this request, type /tpacancel");
                }
                if (fileConfig.getString("teleport-request-target-receive") == null){
                    fileConfig.set("teleport-request-target-receive", "<sender> sent a teleport request to you");
                }
                if (fileConfig.getString("teleport-request-accept") == null){
                    fileConfig.set("teleport-request-accept", "To accept, type /tpaccept");
                }
                if (fileConfig.getString("teleport-request-deny") == null){
                    fileConfig.set("teleport-request-deny","To deny, type /tpdeny");
                }
                if (fileConfig.getString("teleport-request-timeout-warning") == null){
                    fileConfig.set("teleport-request-timeout-warning", "This request will timeout in <time> Seconds");
                }
                if (fileConfig.getString("teleport-request-timeout") == null){
                    fileConfig.set("teleport-request-timeout", "Teleport request timed out");
                }
                if (fileConfig.getString("teleport-disabled") == null){
                    fileConfig.set("teleport-disabled", "That person has teleporting disabled");
                }
                if (fileConfig.getString("teleport-here-blacklisted-world") == null){
                    fileConfig.set("teleport-here-blacklisted-world", "Cannot send Teleport Here Request because you are in a Blacklisted World");
                }
                if (fileConfig.getString("teleport-here-request-sent") == null){
                    fileConfig.set("teleport-here-request-sent", "You sent a teleport here request to <target>");
                }
                if (fileConfig.getString("teleport-here-request-cancel-warning") == null){
                    fileConfig.set("teleport-here-request-cancel-warning", "To cancel this request, type /tpacancel");
                }
                if (fileConfig.getString("teleport-here-request-target-receive") == null){
                    fileConfig.set("teleport-here-request-target-receive", "<sender> would like you to teleport to them");
                }
                if (fileConfig.getString("teleport-here-request-accept") == null){
                    fileConfig.set("teleport-here-request-accept", "To accept, type /tpaccept");
                }
                if (fileConfig.getString("teleport-here-request-deny") == null){
                    fileConfig.set("teleport-here-request-deny","To deny, type /tpdeny");
                }
                if (fileConfig.getString("teleport-here-request-timeout-warning") == null){
                    fileConfig.set("teleport-here-request-timeout-warning", "This request will timeout in <time> Seconds");
                }
                if (fileConfig.getString("teleport-here-request-timeout") == null){
                    fileConfig.set("teleport-here-request-timeout", "Teleport here request timed out");
                }
                if (fileConfig.getString("teleport-cancel") == null){
                    fileConfig.set("teleport-cancel", "You have cancelled all outgoing Teleport Requests");
                }
                if (fileConfig.getString("teleport-no-request") == null){
                    fileConfig.set("teleport-no-request", "There's no request to cancel");
                }
                if (fileConfig.getString("teleport-no-request-accept") == null){
                    fileConfig.set("teleport-no-request-accept", "There's no request to accept");
                }
                if (fileConfig.getString("teleport-no-request-deny") == null){
                    fileConfig.set("teleport-no-request-deny", "There's no request to deny");
                }
                if (fileConfig.getString("teleport-accept-request") == null){
                    fileConfig.set("teleport-accept-request", "<sender> has accepted the teleport request");
                }
                if (fileConfig.getString("teleport-deny-request") == null){
                    fileConfig.set("teleport-deny-request", "<sender> has denied the teleport request");
                }
                if (fileConfig.getString("teleport-accept-request-target") == null){
                    fileConfig.set("teleport-accept-request-target", "Successfully accepted <target>(s) Teleport Request");
                }
                if (fileConfig.getString("teleport-deny-request-target") == null){
                    fileConfig.set("teleport-deny-request-target", "Successfully denied <target>(s) Teleport Request");
                }
                if (fileConfig.getString("teleport-wait-message") == null){
                    fileConfig.set("teleport-wait-message", "Teleporting to <player> in <time> Seconds");
                }
                if (fileConfig.getString("teleport-toggle-enabled") == null){
                    fileConfig.set("teleport-toggle-enabled", "Teleport Requests have been Disabled");
                }
                if (fileConfig.getString("teleport-toggle-disabled") == null){
                    fileConfig.set("teleport-toggle-disabled", "Teleport Requests have been Enabled");
                }
                if (fileConfig.getString("teleport-movement-cancel") == null){
                    fileConfig.set("teleport-movement-cancel", "Teleportation cancelled due to Movement");
                }
                if (fileConfig.getString("time-sunrise") == null){
                    fileConfig.set("time-sunrise", "Time set to Sunrise");
                }
                if (fileConfig.getString("time-day") == null){
                    fileConfig.set("time-day", "Time set to Day");
                }
                if (fileConfig.getString("time-sunset") == null){
                    fileConfig.set("time-sunset", "Time set to Sunset");
                }
                if (fileConfig.getString("time-midnight") == null){
                    fileConfig.set("time-midnight", "Time set to Midnight");
                }
                if (fileConfig.getString("weather-sun") == null){
                    fileConfig.set("weather-sun", "Weather changed to Sun");
                }
                if (fileConfig.getString("weather-storm") == null){
                    fileConfig.set("weather-storm", "Weather changed to Storm");
                }
                if (fileConfig.getString("trash-gui-name") == null){
                    fileConfig.set("trash-gui-name", "Trash Chute");
                }
                if (fileConfig.getString("vanish-enabled") == null){
                    fileConfig.set("vanish-enabled", "You are now invisible!");
                }
                if (fileConfig.getString("vanish-disabled") == null){
                    fileConfig.set("vanish-disabled", "You are now visible to other players on the server");
                }
                if (fileConfig.getString("vanish-target-enabled") == null){
                    fileConfig.set("vanish-target-enabled", "<target> is now invisible");
                }
                if (fileConfig.getString("vanish-target-disabled") == null){
                    fileConfig.set("vanish-target-disabled", "<target> is now visible");
                }
                if (fileConfig.getString("convert-successful") == null){
                    fileConfig.set("convert-successful", "Successfully converted <amount> <item> into <total> <block>");
                }
                if (fileConfig.getString("convert-unsuccessful") == null){
                    fileConfig.set("convert-unsuccessful", "You do not have enough <item> to convert");
                }
                if (fileConfig.getString("convert-invalid") == null){
                    fileConfig.set("convert-invalid", "You cannot convert <item>");
                }
                if (fileConfig.getString("sudo-successful") == null){
                    fileConfig.set("sudo-successful", "Successfully ran command [<command>] for <target>");
                }
                if (fileConfig.getString("sudo-command-invalid") == null){
                    fileConfig.set("sudo-command-invalid", "Command [<command>] doesn't exist.");
                }
                if (fileConfig.getString("ping-self") == null){
                    fileConfig.set("ping-self", "Ping: <ping>");
                }
                if (fileConfig.getString("ping-target") == null){
                    fileConfig.set("ping-target", "<target>'s Ping: <ping>");
                }
                fileConfig.options().copyDefaults(true);
                fileConfig.save(file);
            } catch (IOException e) {
                ServerEssentials.plugin.getLogger().warning(e.toString());
            }
        }
    }

    public static void reload() {
        fileConfig = YamlConfiguration.loadConfiguration(file);
    }
}