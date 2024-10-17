package me.rocketmankianproductions.serveressentials;

import github.scarsz.discordsrv.DiscordSRV;
import me.rocketmankianproductions.serveressentials.Metrics.MetricsLite;
import me.rocketmankianproductions.serveressentials.UpdateChecker.Update;
import me.rocketmankianproductions.serveressentials.commands.*;
import me.rocketmankianproductions.serveressentials.eco.EconomyImplementer;
import me.rocketmankianproductions.serveressentials.eco.VaultHook;
import me.rocketmankianproductions.serveressentials.events.*;
import me.rocketmankianproductions.serveressentials.file.UserFile;
import me.rocketmankianproductions.serveressentials.file.Lang;
import me.rocketmankianproductions.serveressentials.tasks.Broadcast;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ServerEssentials extends JavaPlugin implements Listener {

    public static ServerEssentials plugin;
    public static BukkitTask broadcastLoop;
    File configFile = new File(this.getDataFolder(), "config.yml");
    public static boolean hasUpdate;
    public static boolean isConnectedToPlaceholderAPI = false;
    public static boolean isConnectedToDiscordSRV = false;
    public static String prefix;
    public static ServerEssentials getInstance;
    public EconomyImplementer economyImplementer;
    private VaultHook vaultHook;
    public final HashMap<UUID,Double> playerBank = new HashMap<>();

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
        // Metrics
        MetricsLite metricsLite = new MetricsLite(this);
        // Setup Config
        setupConfig();
        // Placeholder API
        registerPlaceholder();
        // DiscordSRV
        registerDiscordSRV();
        // Setup Economy
        if (Bukkit.getPluginManager().getPlugin("Vault") != null){
            instanceClasses();
            vaultHook.hook();
        }else{
            LoggerMessage.log(LoggerMessage.LogLevel.WARNING, "Vault is not installed!");
        }
        // Setup Commands
        registerCommands();
        new UserFile(plugin);
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
        // Placeholder API Event
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
            new PlaceholderExpansion(this).register();
        }
        LoggerMessage.log(LoggerMessage.LogLevel.SUCCESS, "Events have been enabled.");
        // End
        LoggerMessage.log(LoggerMessage.LogLevel.OUTLINE, "*********************");
        prefix = ServerEssentials.getPlugin().getConfig().getString("prefix");
    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
        LoggerMessage.log(LoggerMessage.LogLevel.OUTLINE, "*********************");
        LoggerMessage.log(LoggerMessage.LogLevel.WARNING, "Server Essentials has been disabled.");
        this.saveDefaultConfig();
        // Config
        LoggerMessage.log(LoggerMessage.LogLevel.WARNING, "Config.yml has been uninitialised.");
        // Disable Placeholder API
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
            LoggerMessage.log(LoggerMessage.LogLevel.WARNING, "PlaceholderAPI has been disabled.");
        }
        // Disable Economy
        if (Bukkit.getPluginManager().getPlugin("Vault") != null){
            LoggerMessage.log(LoggerMessage.LogLevel.WARNING, "Vault has been disabled.");
        }
        // Metrics
        MetricsLite metricsLite = new MetricsLite(this);
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
        // YouTube Command
        getCommand("youtube").setExecutor(new YouTube());
        // Twitch Command
        getCommand("twitch").setExecutor(new Twitch());
        // Playtime Command
        getCommand("playtime").setExecutor(new Playtime());
        // SetSpawn Command
        getCommand("setspawn").setExecutor(new Setspawn());
        getCommand("setspawn").setTabCompleter(new TabCompletion());
        // Spawn Command
        getCommand("spawn").setExecutor(new Spawn());
        getCommand("spawn").setTabCompleter(new TabCompletion());
        getCommand("tutorial").setExecutor(new Spawn());
        // Deletespawn Command
        getCommand("deletespawn").setExecutor(new DeleteSpawn());
        getCommand("deletespawn").setTabCompleter(new TabCompletion());
        // Sethome Command
        getCommand("sethome").setExecutor(new Sethome());
        // Home Command
        getCommand("home").setExecutor(new Home());
        getCommand("home").setTabCompleter(new TabCompletion());
        // Deletehome Command
        getCommand("deletehome").setExecutor(new DeleteHome());
        getCommand("deletehome").setTabCompleter(new TabCompletion());
        // Teleport Toggle Command
        getCommand("tptoggle").setExecutor(new TPToggle());
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
        getCommand("gamemode").setTabCompleter(new TabCompletion());
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
        // Message Toggle Command
        getCommand("msgtoggle").setExecutor(new MsgToggle());
        // Reply Command
        getCommand("reply").setExecutor(new Reply());
        getCommand("reply").setTabCompleter(new TabCompletion());
        // Craft Command
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
        // Weather Command
        getCommand("weather").setExecutor(new Weather());
        getCommand("weather").setTabCompleter(new TabCompletion());
        // Sun Weather Command
        getCommand("sun").setExecutor(new Weather());
        // Storm Weather Command
        getCommand("storm").setExecutor(new Weather());
        // Time Command
        getCommand("time").setExecutor(new Time());
        getCommand("time").setTabCompleter(new TabCompletion());
        // Sunrise Command
        getCommand("sunrise").setExecutor(new Time());
        // Day Command
        getCommand("day").setExecutor(new Time());
        // Sunset Command
        getCommand("sunset").setExecutor(new Time());
        // Night Command
        getCommand("night").setExecutor(new Time());
        // Midnight Command
        getCommand("midnight").setExecutor(new Time());
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
        getCommand("reportbug").setTabCompleter(new TabCompletion());
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
        // Sudo Command
        getCommand("sudo").setExecutor(new Sudo());
        getCommand("sudo").setTabCompleter(new TabCompletion());
        // Whois Command
        getCommand("whois").setExecutor(new Whois());
        // Ping Command
        getCommand("ping").setExecutor(new Ping());
        // Near Command
        getCommand("near").setExecutor(new Near());
        // Anvil, Cartography and Loom Command
        if (isVersionOrLater("1.21.1")) {
            getCommand("anvil").setExecutor(new Anvil());
            getCommand("cartographytable").setExecutor(new CartographyTable());
            getCommand("loom").setExecutor(new Loom());
        }
        // Seen Command
        getCommand("seen").setExecutor(new Seen());
        // Economy
        if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
            getCommand("pay").setExecutor(new Pay());
            getCommand("paytoggle").setExecutor(new PayToggle());
            getCommand("balance").setExecutor(new Balance());
            getCommand("baltop").setExecutor(new Baltop());
            getCommand("eco").setExecutor(new Eco());
            getCommand("eco").setTabCompleter(new TabCompletion());
        }
        // AFK Command
        //getCommand("afk").setExecutor(new AFK());
    }

    public void registerEvents() {
        PluginManager pm = getServer().getPluginManager();
        // Scheduler
        Long delay = plugin.getConfig().getLong("broadcast-delay");
        broadcastLoop = new Broadcast(this).runTaskTimer(this, delay, delay);
        pm.registerEvents(new PlayerJoinListener(), this);
        pm.registerEvents(new PlayerLeaveListener(), this);
        pm.registerEvents(new PlayerRespawnListener(), this);
        pm.registerEvents(new PlayerClickEvent(), this);
        pm.registerEvents(new PlayerChatEvent(), this);
        pm.registerEvents(new PlayerDeathEvent(), this);
        pm.registerEvents(new PlayerMoveEvent(), this);
        pm.registerEvents(new PlayerWorldCheck(), this);
        pm.registerEvents(new Plugins(), this);
        pm.registerEvents(new God(), this);
        //pm.registerEvents(new AFK(), this);
    }

    public void registerPlaceholder() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            LoggerMessage.log(LoggerMessage.LogLevel.SUCCESS, "PlaceholderAPI has been enabled.");
            isConnectedToPlaceholderAPI = true;
        } else {
            LoggerMessage.log(LoggerMessage.LogLevel.WARNING, "Placeholder API is not installed!");
            isConnectedToPlaceholderAPI = false;
        }
    }

    public void registerDiscordSRV() {
        if (Bukkit.getPluginManager().getPlugin("DiscordSRV") != null) {
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

    public static boolean permissionChecker(Player player, String perm) {
        boolean hasPerm = false;
        if (player.hasPermission(perm) || player.hasPermission("se.all")){
            hasPerm = true;
        }else{
            String permmsg = Lang.fileConfig.getString("no-permission-message").replace("<permission>", perm);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', permmsg));
        }
        return hasPerm;
    }

    public static String hex(String message) {
        Pattern pattern = Pattern.compile("(#[a-fA-F0-9]{6})");
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            String hexCode = message.substring(matcher.start(), matcher.end());
            String replaceSharp = hexCode.replace('#', 'x');

            char[] ch = replaceSharp.toCharArray();
            StringBuilder builder = new StringBuilder("");
            for (char c : ch) {
                builder.append("&" + c);
            }

            message = message.replace(hexCode, builder.toString());
            matcher = pattern.matcher(message);
        }
        return ChatColor.translateAlternateColorCodes('&', message).replace('&', '§');
    }

    private void instanceClasses() {
        LoggerMessage.log(LoggerMessage.LogLevel.SUCCESS, "Vault has been enabled.");
        getInstance = this;
        economyImplementer = new EconomyImplementer();
        vaultHook = new VaultHook();
    }

    public static ServerEssentials getPlugin() {
        return plugin;
    }

    public boolean isVersionOrLater(String targetVersion) {
        // Get the server's version string from Bukkit
        String serverVersion = Bukkit.getServer().getVersion();

        // Extract version number from the server string
        String versionPattern = "([0-9]+)\\.([0-9]+)\\.([0-9]+)";
        Pattern pattern = Pattern.compile(versionPattern);
        Matcher matcher = pattern.matcher(serverVersion);

        // If server version matches the version pattern, extract version numbers
        if (matcher.find()) {
            int major = Integer.parseInt(matcher.group(1));
            int minor = Integer.parseInt(matcher.group(2));
            int patch = Integer.parseInt(matcher.group(3));

            // Parse the target version for comparison
            String[] targetParts = targetVersion.split("\\.");
            int targetMajor = Integer.parseInt(targetParts[0]);
            int targetMinor = Integer.parseInt(targetParts[1]);
            int targetPatch = Integer.parseInt(targetParts[2]);

            // Compare versions: first major, then minor, then patch
            if (major > targetMajor) {
                return true; // Major version is higher, so it's later
            } else if (major == targetMajor) {
                if (minor > targetMinor) {
                    return true; // Minor version is higher
                } else if (minor == targetMinor && patch >= targetPatch) {
                    return true; // Patch version is the same or higher
                }
            }
        }
        return false;
    }
}