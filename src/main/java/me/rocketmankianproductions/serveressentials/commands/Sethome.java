package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.LoggerMessage;
import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

import static me.rocketmankianproductions.serveressentials.ServerEssentials.hex;

public class Sethome implements CommandExecutor {

    public static ServerEssentials plugin;

    //settings
    public static String filepath = "home.yml";

    public static File file;
    public static FileConfiguration fileConfig;

    public Sethome() {
        //setup location.yml
        file = new File(ServerEssentials.getPlugin().getDataFolder(), filepath);
        if (!file.exists()) {
            try {
                //create default file
                file.createNewFile();
                LoggerMessage.log(LoggerMessage.LogLevel.INFO, "Home.yml file doesn't exist, creating now...");
                fileConfig = YamlConfiguration.loadConfiguration(file);
                LoggerMessage.log(LoggerMessage.LogLevel.SUCCESS, "Home.yml file created");

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

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 1) {
                if (ServerEssentials.permissionChecker(player, "se.sethome")) {
                    String world = player.getWorld().getName();
                    String name = player.getUniqueId().toString();
                    int maxHomesConfig = ServerEssentials.plugin.getConfig().getInt("default-home-count");
                    int maxHomes = checkMaxHomes(player);
                    Boolean blacklistenabled = ServerEssentials.getPlugin().getConfig().getBoolean("enable-home-blacklist");
                    if (blacklistenabled && checkBlacklist(player)){
                        return true;
                    }
                    if (fileConfig.contains("Home." + name)) {
                        ConfigurationSection inventorySection = fileConfig.getConfigurationSection("Home." + name);
                        int homesAmount = (inventorySection != null) ? inventorySection.getKeys(false).size() : 0;
                        if (fileConfig.getString("Home." + name + "." + args[0]) != null || player.hasPermission("se.sethome.unlimited") || homesAmount < maxHomesConfig || homesAmount < maxHomes) {
                            createHome(name, args, world, player);
                            return true;
                        }
                        // Send maximum homes message
                        int maxHomesToShow = (maxHomes != 0) ? maxHomes : maxHomesConfig;
                        String msg = Lang.fileConfig.getString("home-max-homes").replace("<max>", String.valueOf(maxHomesToShow));
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                        return true;
                    } else if (fileConfig.getString("Home." + name + "." + args[0]) == null) {
                        createHome(name, args, world, player);
                        return true;
                    }
                }
            } else {
                String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/sethome (name)");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                return true;
            }
        }else{
            String console = Lang.fileConfig.getString("console-invalid");
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', hex(console)));
            return true;
        }
        return false;
    }

    public void createHome(String name, String[] args, String world, Player player) {
        fileConfig.set("Home." + name + "." + args[0] + ".World", world);
        fileConfig.set("Home." + name + "." + args[0] + ".X", player.getLocation().getX());
        fileConfig.set("Home." + name + "." + args[0] + ".Y", player.getLocation().getY());
        fileConfig.set("Home." + name + "." + args[0] + ".Z", player.getLocation().getZ());
        fileConfig.set("Home." + name + "." + args[0] + ".Yaw", player.getLocation().getYaw());
        try {
            fileConfig.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Sethome.reload();
        String msg = Lang.fileConfig.getString("home-set-success");
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
    }

    public static int checkMaxHomes(Player player){
        int max = 0;
        for (PermissionAttachmentInfo permissions : player.getEffectivePermissions()){
            if (permissions.getPermission().contains("se.sethome.") && !permissions.getPermission().contains("se.sethome.unlimited")) {
                try {
                    max = Integer.parseInt(permissions.getPermission().split("\\.")[2]);
                } catch (NumberFormatException e){
                }
            }
        }
        return max;
    }

    public static boolean checkBlacklist(Player player){
        boolean isBlacklisted = false;
        for (String worlds : ServerEssentials.plugin.getConfig().getStringList("home-blacklist")) {
            if (player.getWorld().getName().equalsIgnoreCase(worlds)) {
                String msg = Lang.fileConfig.getString("home-blacklisted-world");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                isBlacklisted = true;
            }
        }
        return isBlacklisted;
    }
}