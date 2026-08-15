package me.rocketmankianproductions.serveressentials;

import github.scarsz.discordsrv.DiscordSRV;
import me.rocketmankianproductions.serveressentials.Metrics.MetricsLite;
import me.rocketmankianproductions.serveressentials.UpdateChecker.Update;
import me.rocketmankianproductions.serveressentials.commands.*;
import me.rocketmankianproductions.serveressentials.eco.EconomyImplementer;
import me.rocketmankianproductions.serveressentials.events.*;
import me.rocketmankianproductions.serveressentials.file.BankFile;
import me.rocketmankianproductions.serveressentials.file.JailFile;
import me.rocketmankianproductions.serveressentials.file.UserFile;
import me.rocketmankianproductions.serveressentials.file.Lang;
import me.rocketmankianproductions.serveressentials.tasks.Broadcast;
import me.rocketmankianproductions.serveressentials.utils.AFKManager;
import me.rocketmankianproductions.serveressentials.utils.GUIPaginationHelper;
import me.rocketmankianproductions.serveressentials.utils.JailManagerService;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.*;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ServerEssentials extends JavaPlugin implements Listener, ServerEssentialsPlatform {

    public static ServerEssentials plugin;
    public static BukkitTask broadcastLoop;
    File configFile = new File(this.getDataFolder(), "config.yml");
    public static boolean hasUpdate;
    public static boolean isConnectedToPlaceholderAPI = false;
    public static boolean isConnectedToDiscordSRV = false;
    public static String prefix;
    public static ServerEssentials getInstance;
    public EconomyImplementer economyImplementer;
    public JailManagerService jailManager;
    public final HashMap<UUID,Double> playerBank = new HashMap<>();

    private DiscordMessageReceived discordsrvListener = new DiscordMessageReceived(this);

    public static boolean hasUpdate() {
        return hasUpdate;
    }

    @Override
    public void onEnable() {
        getInstance = this;
        plugin = this;
        ServerEssentialsAPI.setImplementation(this);
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
        // Setup Commands
        UserFile.setup();
        Mute.loadAllMutedPlayers();
        BankFile.setup();
        registerCommands();
        // Setup Economy
        if (Bukkit.getPluginManager().getPlugin("Vault") != null && ServerEssentials.getPlugin().getConfig().getBoolean("enable-eco")){
            instanceClasses();
        }else{
            LoggerMessage.log(LoggerMessage.LogLevel.WARNING, "Vault is not installed!");
        }
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
        ServerEssentialsAPI.setImplementation(null);
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
        // Disable Vault
        if (Bukkit.getPluginManager().getPlugin("Vault") != null){
            LoggerMessage.log(LoggerMessage.LogLevel.WARNING, "Vault has been disabled.");
        }
        economyImplementer.shutdown();
        JailFile.saveSync(jailManager);
        LoggerMessage.log(LoggerMessage.LogLevel.WARNING, "Economy has been disabled.");
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
        // AFK Command
        getCommand("afk").setExecutor(new AFK());
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
        // Teleport Override Command
        getCommand("tpo").setExecutor(new TPO());
        getCommand("tpohere").setExecutor(new TPOHere());
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
        // TPA Cancel Command
        getCommand("tpacancel").setExecutor(new TeleportRequest());
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
        getCommand("creative").setTabCompleter(new TabCompletion());
        // Survival Command
        getCommand("survival").setExecutor(new Gamemode());
        getCommand("survival").setTabCompleter(new TabCompletion());
        // Adventure Command
        getCommand("adventure").setExecutor(new Gamemode());
        getCommand("adventure").setTabCompleter(new TabCompletion());
        // Spectator Command
        getCommand("spectator").setExecutor(new Gamemode());
        getCommand("spectator").setTabCompleter(new TabCompletion());
        // GMC Command
        getCommand("gmc").setExecutor(new Gamemode());
        getCommand("gmc").setTabCompleter(new TabCompletion());
        // GMS Command
        getCommand("gms").setExecutor(new Gamemode());
        getCommand("gms").setTabCompleter(new TabCompletion());
        // GMSP Command
        getCommand("gmsp").setExecutor(new Gamemode());
        getCommand("gmsp").setTabCompleter(new TabCompletion());
        // GMA Command
        getCommand("gma").setExecutor(new Gamemode());
        getCommand("gma").setTabCompleter(new TabCompletion());
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
        // DeleteWarp Command
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
        // Thunder Weather Command
        getCommand("thunder").setExecutor(new Weather());
        // Rain Weather Command
        getCommand("rain").setExecutor(new Weather());
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
            getCommand("smithingtable").setExecutor(new SmithingTable());
            getCommand("stonecutter").setExecutor(new Stonecutter());
            getCommand("grindstone").setExecutor(new Grindstone());
        }
        // Seen Command
        getCommand("seen").setExecutor(new Seen());
        // Thor Command
        getCommand("thor").setExecutor(new Thor());
        // Freeze Command
        getCommand("freeze").setExecutor(new Freeze());
        getCommand("unfreeze").setExecutor(new Freeze());
        // Jump Command
        getCommand("jump").setExecutor(new Jump());
        // Jump Boost Command
        getCommand("jumpboost").setExecutor(new JumpBoost());
        // Fireball Command
        getCommand("fireball").setExecutor(new Fireball());
        // Burn Command
        getCommand("burn").setExecutor(new Burn());
        // World Command
        getCommand("world").setExecutor(new World());
        getCommand("world").setTabCompleter(new TabCompletion());
        // Clear Entity Command
        getCommand("clearentity").setExecutor(new EntityRemover());
        // Top Command
        getCommand("top").setExecutor(new Top());
        // Bottom Command
        getCommand("bottom").setExecutor(new Bottom());
        // Spawner Command
        getCommand("spawner").setExecutor(new Spawner());
        // Mute Commands
        getCommand("mute").setExecutor(new Mute());
        getCommand("unmute").setExecutor(new Mute());
        // List Command
        getCommand("list").setExecutor(new List());
        // Jail Commands
        JailFile.setup();
        jailManager = new JailManagerService();
        JailFile.load(jailManager);
        jailManager.startAutoReleaseTask();
        Bukkit.getServer().getPluginManager().registerEvents(jailManager, this);
        getCommand("jail").setExecutor(new Jail());
        getCommand("jail").setTabCompleter(new TabCompletion());
        getCommand("unjail").setExecutor(new Jail());
        getCommand("jailtime").setExecutor(new Jail());
        getCommand("createjail").setExecutor(new Jail());
        getCommand("deletejail").setExecutor(new Jail());
        getCommand("jaillist").setExecutor(new Jail());
        getCommand("nickname").setExecutor(new Nickname());
        getCommand("nickname").setTabCompleter(new TabCompletion());
        // Economy
        if (Bukkit.getPluginManager().getPlugin("Vault") != null && ServerEssentials.getPlugin().getConfig().getBoolean("enable-eco")) {
            getCommand("pay").setExecutor(new Pay());
            getCommand("paytoggle").setExecutor(new PayToggle());
            getCommand("balance").setExecutor(new Balance());
            getCommand("hidebalance").setExecutor(new HideBalance());
            getCommand("baltop").setExecutor(new Baltop());
            getCommand("eco").setExecutor(new Eco());
            getCommand("eco").setTabCompleter(new TabCompletion());
        }
    }

    public void registerEvents() {
        PluginManager pm = getServer().getPluginManager();
        // Scheduler
        Long delay = plugin.getConfig().getLong("broadcast-delay");
        broadcastLoop = new Broadcast(this).runTaskTimer(this, delay, delay);
        GUIPaginationHelper.setPlugin(this);
        pm.registerEvents(new AFK(), this);
        pm.registerEvents(new PlayerJoinListener(), this);
        pm.registerEvents(new PlayerLeaveListener(), this);
        pm.registerEvents(new PlayerRespawnListener(), this);
        pm.registerEvents(new PlayerClickEvent(), this);
        pm.registerEvents(new PlayerChatEvent(), this);
        pm.registerEvents(new PlayerDeathEvent(), this);
        pm.registerEvents(new PlayerMoveListener(), this);
        pm.registerEvents(new PlayerWorldCheck(), this);
        pm.registerEvents(new SignChange(), this);
        pm.registerEvents(new Plugins(), this);
        pm.registerEvents(new God(), this);
        pm.registerEvents(new SocialSpy(), this);
        pm.registerEvents(new StaffChat(), this);
        if (Bukkit.getPluginManager().getPlugin("Towny") != null) {
            pm.registerEvents(new TownBankListener(), this);
        }
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

    public static boolean permissionChecker(CommandSender player, String perm) {
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

        economyImplementer = new EconomyImplementer();
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        getServer().getServicesManager().register(Economy.class, economyImplementer, this, ServicePriority.High);

        LoggerMessage.log(LoggerMessage.LogLevel.SUCCESS,
                "Registered ServerEssentials as Vault Economy provider.");
        Economy econ = Bukkit.getServicesManager().load(Economy.class);
        Bukkit.getLogger().info("[ServerEssentials] Economy provider loaded: " + econ.getName());
        if (rsp != null) {
            Bukkit.getLogger().info("Current Vault Economy provider: " + rsp.getProvider().getClass().getName());
        }
    }

    public static ServerEssentials getPlugin() {
        return plugin;
    }

    public boolean isVersionOrLater(String targetVersion) {
        // getBukkitVersion() returns something like "1.21.1-R0.1-SNAPSHOT"
        String serverVersion = Bukkit.getBukkitVersion().split("-")[0];

        String[] serverParts = serverVersion.split("\\.");
        String[] targetParts = targetVersion.split("\\.");

        int length = Math.max(serverParts.length, targetParts.length);
        for (int i = 0; i < length; i++) {
            int serverPiece = i < serverParts.length ? Integer.parseInt(serverParts[i]) : 0;
            int targetPiece = i < targetParts.length ? Integer.parseInt(targetParts[i]) : 0;

            if (serverPiece > targetPiece) {
                return true;
            }
            if (serverPiece < targetPiece) {
                return false;
            }
        }
        return true; // Versions are exactly equal
    }

    @Override
    public boolean isAFK(Player player) {
        return AFKManager.isAFK(player);
    }

    @Override
    public void setAFK(Player player, boolean afk) {
        AFKManager.setAFK(player, afk);
    }

    @Override
    public String getNickname(Player player) {
        return UserFile.config.getString(player.getUniqueId() + ".nickname");
    }

    @Override
    public long getLastActivity(Player player) {
        return AFKManager.getLastActivity(player);
    }

    @Override
    public boolean isJailed(Player player) {
        // Assuming your 'getInstance' logic looks something like this internally
        return jailManager.isJailed(player);
    }

    @Override
    public boolean setJailed(Player player, String jailName, int durationSeconds, String durationUnconverted, String reason) {
        if (jailManager.getJail(jailName) == null){
            return false;
        }
        jailManager.jailPlayer(player, jailName, durationSeconds, durationUnconverted, reason);
        return true;
    }

    @Override
    public boolean releasePlayer(Player player) {
        if (!jailManager.isJailed(player)){
            return false;
        }
        jailManager.releasePlayer(player.getUniqueId());
        return true;
    }
}