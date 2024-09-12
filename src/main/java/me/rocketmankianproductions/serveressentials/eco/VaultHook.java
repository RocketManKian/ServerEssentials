package me.rocketmankianproductions.serveressentials.eco;

import me.rocketmankianproductions.serveressentials.LoggerMessage;
import me.rocketmankianproductions.serveressentials.ServerEssentials;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;

public class VaultHook {

    private ServerEssentials plugin = ServerEssentials.getInstance;

    private Economy provider;

    public void hook() {
        provider = plugin.economyImplementer;
        Bukkit.getServicesManager().register(Economy.class, this.provider, this.plugin, ServicePriority.Normal);
        LoggerMessage.log(LoggerMessage.LogLevel.SUCCESS, "VaultAPI has been hooked into ServerEssentials.");
    }

    public void unhook() {
        Bukkit.getServicesManager().unregister(Economy.class, this.provider);
        LoggerMessage.log(LoggerMessage.LogLevel.WARNING, "VaultAPI has been unhooked from ServerEssentials.");
    }
}
