package me.rocketmankianproductions.serveressentials.events;

import com.palmergames.bukkit.towny.event.actions.TownyDestroyEvent;
import me.rocketmankianproductions.serveressentials.ServerEssentials;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class TownBankListener implements Listener {
    private ServerEssentials plugin = ServerEssentials.getInstance;

    @EventHandler
    public void onTownRemove(TownyDestroyEvent event) {
        String townName = event.getTownBlock().getName(); // Get the name of the deleted town
        String bankName = "town-" + townName; // Construct the full bank name as stored in BankFile

        // Call your deleteBank method
        EconomyResponse response = plugin.economyImplementer.deleteBank(bankName);

        if (response.transactionSuccess()) {
            Bukkit.getLogger().info("Successfully removed bank account for deleted town: " + townName);
        } else {
            Bukkit.getLogger().warning("Failed to remove bank account for deleted town " + townName + ": " + response.errorMessage);
        }
    }
}
