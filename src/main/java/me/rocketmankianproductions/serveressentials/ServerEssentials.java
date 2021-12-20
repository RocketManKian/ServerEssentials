package me.rocketmankianproductions.serveressentials;

import github.scarsz.discordsrv.DiscordSRV;
import me.rocketmankianproductions.serveressentials.Metrics.MetricsLite;
import me.rocketmankianproductions.serveressentials.UpdateChecker.Update;
import me.rocketmankianproductions.serveressentials.commands.*;
import me.rocketmankianproductions.serveressentials.events.*;
import me.rocketmankianproductions.serveressentials.file.Lang;
import me.rocketmankianproductions.serveressentials.tasks.Broadcast;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

public final class ServerEssentials extends JavaPlugin implements Listener {

    public static ServerEssentials plugin;
    public static BukkitTask broadcastLoop;
    File configFile = new File(this.getDataFolder(), "config.yml");
    public static boolean hasUpdate;
    public static boolean isConnectedToPlaceholderAPI = false;
    public static boolean isConnectedToDiscordSRV = false;

    public ArrayList<Player> invisible_list = new ArrayList<>();

    private DiscordMessageReceived discordsrvListener = new DiscordMessageReceived(this);

    public static boolean hasUpdate() {
        return hasUpdate;
    }

    @Override
    public void onEnable() {
        plugin = this;
        LoggerMessage.log(LoggerMessage.LogLevel.OUTLINE, "*********************");
        // Plugin startup logic
        LoggerMessage.log(LoggerMessage.LogLevel.SUCCESS, "Server Essentials has been enabled.");
        this.saveDefaultConfig();
        // Lang File
        Lang.setup();
        // Placeholder API
        registerPlaceholder();
        // DiscordSRV
        registerDiscordSRV();
        // Metrics
        MetricsLite metricsLite = new MetricsLite(this);
        // Setup Config
        setupConfig();
        // Setup Commands
        registerCommands();
        new SilentJoin(plugin);
        new TPToggle(plugin);
        new MsgToggle(plugin);
        LoggerMessage.log(LoggerMessage.LogLevel.SUCCESS, "Commands have been enabled.");
        // Register Update
        registerUpdate();
        // Setup Events
        new BukkitRunnable() {
            @Override
            public void run() {
                registerEvents();
            }
        }.runTaskLaterAsynchronously(this, 20L);
        LoggerMessage.log(LoggerMessage.LogLevel.SUCCESS, "Events have been enabled.");
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
        // Config
        LoggerMessage.log(LoggerMessage.LogLevel.WARNING, "Config.yml has been uninitialised.");
        // Disable Commands
        LoggerMessage.log(LoggerMessage.LogLevel.WARNING, "Commands have been disabled.");
        // Disable Events
        LoggerMessage.log(LoggerMessage.LogLevel.WARNING, "Events have been disabled.");
        // End
        LoggerMessage.log(LoggerMessage.LogLevel.OUTLINE, "*********************");
    }

    // Registering and Setting Up Everything

    public void setupConfig() {
        if (configFile.exists())
            LoggerMessage.log(LoggerMessage.LogLevel.SUCCESS, "Config.yml has been initialised.");
        else ;
    }

    public void registerCommands() {
        // Hurt Command
        Objects.requireNonNull(getCommand("hurt")).setExecutor(new Hurt());
        // Help Command
        getCommand("rules").setExecutor(new Rules());
        // SE Command
        getCommand("se").setExecutor(new SE());
        getCommand("se").setTabCompleter(new TabCompletion());
        // Discord Command
        getCommand("discord").setExecutor(new Discord());
        // Website Command
        getCommand("website").setExecutor(new Website());
        // Playtime Command
        getCommand("playtime").setExecutor(new Playtime());
        // SetSpawn Command
        getCommand("setspawn").setExecutor(new Setspawn());
        // Spawn Command
        getCommand("spawn").setExecutor(new Spawn());
        // Deletespawn Command
        getCommand("deletespawn").setExecutor(new DeleteSpawn());
        // Sethome Command
        getCommand("sethome").setExecutor(new Sethome());
        // Home Command
        getCommand("home").setExecutor(new Home());
        getCommand("home").setTabCompleter(new TabCompletion());
        // Deletehome Command
        getCommand("deletehome").setExecutor(new DeleteHome());
        getCommand("deletehome").setTabCompleter(new TabCompletion());
        // Teleport Command
        getCommand("teleport").setExecutor(new Teleport());
        // TeleportHere Command
        getCommand("tphere").setExecutor(new TeleportHere());
        // TeleportAll Command
        getCommand("teleportall").setExecutor(new TeleportAll());
        // Main Teleport Request Command
        getCommand("tpa").setExecutor(new TeleportRequest());
        // Teleport Accept Command
        getCommand("tpaccept").setExecutor(new TeleportRequest());
        // Teleport Deny Command
        getCommand("tpdeny").setExecutor(new TeleportRequest());
        // Teleport Here Command
        getCommand("tpahere").setExecutor(new TeleportRequest());
        // Heal Command
        getCommand("heal").setExecutor(new Heal());
        // Feed Command
        getCommand("feed").setExecutor(new Feed());
        // Announce Command
        getCommand("announce").setExecutor(new Announce());
        // Invsee Command
        getCommand("invsee").setExecutor(new Invsee());
        // Gamemode Command
        getCommand("gamemode").setExecutor(new Gamemode());
        // Creative Command
        getCommand("creative").setExecutor(new Gamemode());
        // Survival Command
        getCommand("survival").setExecutor(new Gamemode());
        // Adventure Command
        getCommand("adventure").setExecutor(new Gamemode());
        // Spectator Command
        getCommand("spectator").setExecutor(new Gamemode());
        // GMC Command
        getCommand("gmc").setExecutor(new Gamemode());
        // GMS Command
        getCommand("gms").setExecutor(new Gamemode());
        // GMSP Command
        getCommand("gmsp").setExecutor(new Gamemode());
        // GMA Command
        getCommand("gma").setExecutor(new Gamemode());
        // Fly Command
        getCommand("fly").setExecutor(new Fly());
        // Kill Command
        getCommand("kill").setExecutor(new Kill());
        // Message Command
        getCommand("msg").setExecutor(new Message());
        getCommand("msg").setTabCompleter(new TabCompletion());
        // Reply Command
        getCommand("reply").setExecutor(new Reply());
        getCommand("reply").setTabCompleter(new TabCompletion());
        // Reply Command
        getCommand("craft").setExecutor(new Craft());
        // Enderchest Command
        getCommand("enderchest").setExecutor(new Enderchest());
        // Setwarp Command
        getCommand("setwarp").setExecutor(new Setwarp());
        // Warp Command
        getCommand("warp").setExecutor(new Warp());
        getCommand("warp").setTabCompleter(new TabCompletion());
        // Warp Command
        getCommand("deletewarp").setExecutor(new DeleteWarp());
        getCommand("deletewarp").setTabCompleter(new TabCompletion());
        // Repair Command
        getCommand("repair").setExecutor(new Repair());
        // Sunrise Command
        getCommand("sunrise").setExecutor(new Time());
        // Day Command
        getCommand("day").setExecutor(new Time());
        // Sunset Command
        getCommand("sunset").setExecutor(new Time());
        // Midnight Command
        getCommand("midnight").setExecutor(new Time());
        // Midnight Command
        getCommand("sun").setExecutor(new Time());
        // Midnight Command
        getCommand("storm").setExecutor(new Time());
        // Midnight Command
        getCommand("thunder").setExecutor(new Time());
        // Vanish Command
        getCommand("vanish").setExecutor(new Vanish());
        // Test Command
        getCommand("test").setExecutor(new Test());
        getCommand("test").setTabCompleter(new TabCompletion());
        // Clear Command
        getCommand("clear").setExecutor(new Clear());
        // God Command
        getCommand("god").setExecutor(new God());
        // Hat Command
        getCommand("hat").setExecutor(new Hat());
        // Send Home Command
        getCommand("sendhome").setExecutor(new SendHome());
        getCommand("sendhome").setTabCompleter(new TabCompletion());
        // List Home Command
        getCommand("listhomes").setExecutor(new ListHomes());
        // TPA Cancel Command
        getCommand("tpacancel").setExecutor(new TeleportRequest());
        // Rename Command
        getCommand("rename").setExecutor(new Rename());
        // Set Lore Command
        getCommand("lore").setExecutor(new Lore());
        // Speed Command
        getCommand("speed").setExecutor(new Speed());
        getCommand("speed").setTabCompleter(new TabCompletion());
        // Report Command
        getCommand("report").setExecutor(new Report());
        // Report Bug Command
        getCommand("reportbug").setExecutor(new ReportBug());
        // Clear Chat Command
        getCommand("clearchat").setExecutor(new ClearChat());
        // Send Warp Command
        getCommand("sendwarp").setExecutor(new SendWarp());
        getCommand("sendwarp").setTabCompleter(new TabCompletion());
        // Social Spy Command
        getCommand("socialspy").setExecutor(new SocialSpy());
        // Staff Chat Command
        getCommand("staffchat").setExecutor(new StaffChat());
        // Trash Command
        getCommand("trash").setExecutor(new Trash());
        // Back Command
        getCommand("back").setExecutor(new Back());
        // Convert Command
        getCommand("convert").setExecutor(new Convert());
        Convert.setItems();
        // Book Command
        //getCommand("book").setExecutor(new Book());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        new TPToggle(plugin).run(sender, args, command);
        new MsgToggle(plugin).run(sender, args, command);
        return false;
    }

    public void registerEvents() {
        // Scheduler
        Long delay = plugin.getConfig().getLong("broadcast-delay");
        broadcastLoop = new Broadcast(this).runTaskTimer(this, delay, delay);
        // Join and Leave Message
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerLeaveListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerRespawnListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerClickEvent(), this);
        getServer().getPluginManager().registerEvents(new PlayerChatEvent(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathEvent(), this);
        getServer().getPluginManager().registerEvents(new PlayerMoveEvent(), this);
        getServer().getPluginManager().registerEvents(new PlayerWorldCheck(), this);
        // God Command
        getServer().getPluginManager().registerEvents(new God(), this);
    }

    public void registerPlaceholder() {
        // PlaceholderAPI
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            //Bleh
            LoggerMessage.log(LoggerMessage.LogLevel.SUCCESS, "PlaceholderAPI has been enabled.");
            isConnectedToPlaceholderAPI = true;
        } else {
            LoggerMessage.log(LoggerMessage.LogLevel.WARNING, "Placeholder API is not installed!");
            isConnectedToPlaceholderAPI = false;
        }
    }

    public void registerDiscordSRV() {
        // PlaceholderAPI
        if (Bukkit.getPluginManager().getPlugin("DiscordSRV") != null) {
            //Bleh
            LoggerMessage.log(LoggerMessage.LogLevel.SUCCESS, "DiscordSRV Integration has been enabled.");
            getServer().getPluginManager().registerEvents(new DiscordMessageReceived(this), this);
            DiscordSRV.api.subscribe(discordsrvListener);
            isConnectedToDiscordSRV = true;
        } else {
            LoggerMessage.log(LoggerMessage.LogLevel.WARNING, "DiscordSRV is not installed!");
            isConnectedToDiscordSRV = false;
        }
    }

    public void registerUpdate() {
        // UpdateChecker
        new Update(this, 86675).getLatestVersion(version -> {
            if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
                LoggerMessage.log(LoggerMessage.LogLevel.SUCCESS, "Server Essentials is up to date!");
                hasUpdate = false;
            } else {
                LoggerMessage.log(LoggerMessage.LogLevel.WARNING, "Server Essentials has an update.");
                hasUpdate = true;
            }
        });
    }

    public static ServerEssentials getPlugin() {
        return plugin;
    }
}