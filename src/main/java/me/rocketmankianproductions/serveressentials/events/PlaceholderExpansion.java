package me.rocketmankianproductions.serveressentials.events;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.commands.AFK;
import me.rocketmankianproductions.serveressentials.commands.Freeze;
import me.rocketmankianproductions.serveressentials.commands.Sethome;
import me.rocketmankianproductions.serveressentials.file.Lang;
import me.rocketmankianproductions.serveressentials.file.UserFile;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public class PlaceholderExpansion extends me.clip.placeholderapi.expansion.PlaceholderExpansion {

    private final ServerEssentials plugin;

    public PlaceholderExpansion(ServerEssentials plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "se";
    }

    @Override
    public @NotNull String getAuthor() {
        return "RocketManKian";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public boolean persist() {
        return true; // This is required or else PlaceholderAPI will unregister the Expansion on reload
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if(params.equalsIgnoreCase("afk")) {
            if (AFK.afk.containsKey(player)) {
                return Lang.fileConfig.getString("placeholder_afk_isenabled_yes");
            } else {
                return Lang.fileConfig.getString("placeholder_afk_isenabled_no");
            }
        }else if (params.equalsIgnoreCase("freeze")) {
            if (Freeze.freeze.containsKey(player)) {
                return Lang.fileConfig.getString("placeholder_freeze_isenabled_yes");
            } else {
                return Lang.fileConfig.getString("placeholder_freeze_isenabled_no");
            }
        }else if (params.equalsIgnoreCase("godmode_isenabled")){
            if (UserFile.fileConfig.getBoolean(player.getUniqueId() + ".godmode")) {
                return Lang.fileConfig.getString("placeholder_godmode_isenabled_yes");
            }else{
                return Lang.fileConfig.getString("placeholder_godmode_isenabled_no");
            }
        }else if (params.equalsIgnoreCase("vanish_isenabled")){
            if (UserFile.fileConfig.getBoolean(player.getUniqueId() + ".vanish")) {
                return Lang.fileConfig.getString("placeholder_vanish_isenabled_yes");
            }else{
                return Lang.fileConfig.getString("placeholder_vanish_isenabled_no");
            }
        }else if (params.equalsIgnoreCase("fly_isenabled")){
            if (UserFile.fileConfig.getBoolean(player.getUniqueId() + ".fly")) {
                return Lang.fileConfig.getString("placeholder_fly_isenabled_yes");
            }else{
                return Lang.fileConfig.getString("placeholder_fly_isenabled_no");
            }
        }else if (params.equalsIgnoreCase("msgtoggle_isenabled")){
            if (UserFile.fileConfig.getBoolean(player.getUniqueId() + ".msgtoggle")) {
                return Lang.fileConfig.getString("placeholder_msgtoggle_isenabled_yes");
            }else{
                return Lang.fileConfig.getString("placeholder_msgtoggle_isenabled_no");
            }
        }else if (params.equalsIgnoreCase("tptoggle_isenabled")){
            if (UserFile.fileConfig.getBoolean(player.getUniqueId() + ".tptoggle")) {
                return Lang.fileConfig.getString("placeholder_tptoggle_isenabled_yes");
            }else{
                return Lang.fileConfig.getString("placeholder_tptoggle_isenabled_no");
            }
        }else if (params.equalsIgnoreCase("home_amount")){
            ConfigurationSection inventorySection = Sethome.fileConfig.getConfigurationSection("Home." + player.getUniqueId());
            if (inventorySection == null || inventorySection.getKeys(true).isEmpty()) {
                return "0";
            }else{
                return String.valueOf(inventorySection.getKeys(false).size());
            }
        }else if (params.equalsIgnoreCase("home_limit")) {
            if (plugin.getConfig().getInt("default-home-count") > Sethome.checkMaxHomes(player.getPlayer())){
                return String.valueOf(plugin.getConfig().getInt("default-home-count"));
            }else{
                return String.valueOf(Sethome.checkMaxHomes(player.getPlayer()));
            }
        }
        return null; // Placeholder is unknown by the Expansion
    }
}