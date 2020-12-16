package me.rocketmankianproductions.serveressentials;

import com.sun.org.apache.xpath.internal.operations.String;
import me.rocketmankianproductions.serveressentials.Metrics.MetricsLite;
import me.rocketmankianproductions.serveressentials.UpdateChecker.Update;
import me.rocketmankianproductions.serveressentials.commands.*;
import me.rocketmankianproductions.serveressentials.events.PlayerJoin;
import me.rocketmankianproductions.serveressentials.events.PlayerLeave;
import me.rocketmankianproductions.serveressentials.events.WelcomeBackMessage;
import me.rocketmankianproductions.serveressentials.events.WelcomeMessage;
import me.rocketmankianproductions.serveressentials.tasks.Broadcast;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.Objects;
import java.util.logging.Logger;

public final class ServerEssentials extends JavaPlugin {

    public static ServerEssentials plugin;
    public static BukkitTask broadcastLoop;
    File configFile = new File(this.getDataFolder(), "config.yml");

    @Override
    public void onEnable() {
        plugin = this;
        LoggerMessage.log(LoggerMessage.LogLevel.OUTLINE, "*********************");
        // Plugin startup logic
        LoggerMessage.log(LoggerMessage.LogLevel.SUCCESS, "Server Essentials has been enabled.");
        this.saveDefaultConfig();
        // Placeholder API
        registerPlaceholder();
        // Metrics
        MetricsLite metricsLite = new MetricsLite(this);
        // Setup Config
        setupConfig();
        // Setup Commands
        registerCommands();
        LoggerMessage.log(LoggerMessage.LogLevel.SUCCESS, "Commands have been enabled.");
        // Setup Events
        registerEvents();
        LoggerMessage.log(LoggerMessage.LogLevel.SUCCESS, "Events have been enabled.");
        // Register Update
        registerUpdate();
        // End
        LoggerMessage.log(LoggerMessage.LogLevel.OUTLINE, "*********************");
    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
        LoggerMessage.log(LoggerMessage.LogLevel.OUTLINE, "*********************");
        LoggerMessage.log(LoggerMessage.LogLevel.WARNING, "Server Essentials has been disabled.");
        this.saveDefaultConfig();
        // Disable Placeholder API
        LoggerMessage.log(LoggerMessage.LogLevel.WARNING, "PlaceholderAPI has been disabled.");
        // Metrics
        MetricsLite metricsLite = new MetricsLite(this);
        // Disable Config
        LoggerMessage.log(LoggerMessage.LogLevel.WARNING, "Config.yml has been disabled.");
        // Disable Commands
        LoggerMessage.log(LoggerMessage.LogLevel.WARNING, "Commands have been disabled.");
        // Disable Events
        LoggerMessage.log(LoggerMessage.LogLevel.WARNING, "Events have been disabled.");
        // End
        LoggerMessage.log(LoggerMessage.LogLevel.OUTLINE, "*********************");
    }

    // Registering and Setting Up Everything

    public void setupConfig(){
        if (configFile.exists())
            LoggerMessage.log(LoggerMessage.LogLevel.SUCCESS, "Config.yml has been enabled.");
        else;
        // bleh
    }
    public void registerCommands(){
        // Health Command
        Objects.requireNonNull(getCommand("hurt")).setExecutor(new Hurt());
        // Help Command
        getCommand("rules").setExecutor(new Rules());
        // Reload Command
        getCommand("sereload").setExecutor(new Reload());
        // Discord Command
        getCommand("discord").setExecutor(new Discord());
        // Website Command
        getCommand("website").setExecutor(new Website());
        // Playtime Command
        getCommand("playtime").setExecutor(new Playtime());
    }
    public void registerEvents(){
        // Join and Leave Message
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new PlayerLeave(), this);
        // Welcome Message
        getServer().getPluginManager().registerEvents(new WelcomeMessage(), this);
        // Welcome Back Message
        getServer().getPluginManager().registerEvents(new WelcomeBackMessage(), this);
        // Scheduler
        Long delay = plugin.getConfig().getLong("broadcast-delay");
        broadcastLoop = new Broadcast(this).runTaskTimer(this, delay, delay);
    }
    public void registerPlaceholder(){
        // PlaceholderAPI
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
            //Bleh
            LoggerMessage.log(LoggerMessage.LogLevel.SUCCESS, "PlaceholderAPI has been enabled.");
        }else{
            getLogger().info("Placeholder API is not installed!");
        }
    }
    public void registerUpdate(){
        // UpdateChecker
        new Update(this, 86675).getLatestVersion(version -> {
            if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
                LoggerMessage.log(LoggerMessage.LogLevel.SUCCESS, "Server Essentials is up to date!");
            } else {
                LoggerMessage.log(LoggerMessage.LogLevel.WARNING, "Server Essentials has an update.");
            }
        });
    }
    public static ServerEssentials getPlugin() {
        return plugin;
    }
    public void debug(String message) {
        FileConfiguration config = getConfig();
        if(!config.getBoolean("debug")) return;

        Logger logger = getLogger();
        logger.info("[Debug] " + message);
    }
}