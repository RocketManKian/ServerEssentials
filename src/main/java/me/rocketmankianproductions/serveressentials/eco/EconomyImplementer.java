package me.rocketmankianproductions.serveressentials.eco;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.UserFile;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.*;

public class EconomyImplementer implements Economy {

    private ServerEssentials plugin = ServerEssentials.getInstance;

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return "ServerEssentials";
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return 2; // Use 2 decimal places by default
    }

    @Override
    public String format(double amount) {
        String currencySymbol = plugin.getConfig().getString("currency-symbol", "$");
        return currencySymbol + String.format("%.2f", amount);
    }

    @Override
    public String currencyNamePlural() {
        return plugin.getConfig().getString("currency-name-plural", "Dollars");
    }

    @Override
    public String currencyNameSingular() {
        return plugin.getConfig().getString("currency-name-singular", "Dollar");
    }

    private double initializeBalance(UUID uuid) {
        double startingBalance = plugin.getConfig().getDouble("start-balance", 0.0);
        if (!UserFile.fileConfig.contains(uuid.toString())) {
            UserFile.fileConfig.set(uuid + ".money", startingBalance);
            saveUserFile();
        }
        return UserFile.fileConfig.getDouble(uuid + ".money");
    }

    private void saveUserFile() {
        try {
            UserFile.fileConfig.save(UserFile.file);
        } catch (IOException e) {
            Bukkit.getLogger().severe("Could not save UserFile: " + e.getMessage());
        }
    }

    private double getBalanceOrInitialize(UUID uuid) {
        if (!plugin.playerBank.containsKey(uuid)) {
            double balance = initializeBalance(uuid);
            plugin.playerBank.put(uuid, balance);
        }
        return plugin.playerBank.get(uuid);
    }

    // New method to check if UUID belongs to a real player
    private boolean isPlayer(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player != null && player.hasMetadata("NPC")) {
            return false; // NPC detected
        }
        return Bukkit.getOfflinePlayer(uuid).hasPlayedBefore() ||  Bukkit.getOfflinePlayer(uuid).isOnline(); // Ensures UUID belongs to a real player
    }

    @Override
    public boolean hasAccount(String playerName) {
        Player player = Bukkit.getPlayer(playerName);
        return player != null && isPlayer(player.getUniqueId()) && hasAccount(player.getUniqueId());
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer) {
        return isPlayer(offlinePlayer.getUniqueId()) && hasAccount(offlinePlayer.getUniqueId());
    }

    private boolean hasAccount(UUID uuid) {
        return isPlayer(uuid) && UserFile.fileConfig.contains(uuid.toString());
    }

    @Override
    public boolean hasAccount(String playerName, String worldName) {
        return hasAccount(playerName);
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer, String worldName) {
        return hasAccount(offlinePlayer);
    }

    @Override
    public double getBalance(String playerName) {
        Player player = Bukkit.getPlayer(playerName);
        return (player != null && isPlayer(player.getUniqueId())) ? getBalance(player.getUniqueId()) : 0.0;
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer) {
        return isPlayer(offlinePlayer.getUniqueId()) ? getBalance(offlinePlayer.getUniqueId()) : 0.0;
    }

    private double getBalance(UUID uuid) {
        return isPlayer(uuid) ? getBalanceOrInitialize(uuid) : 0.0;
    }

    @Override
    public double getBalance(String playerName, String worldName) {
        return getBalance(playerName);
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer, String worldName) {
        return getBalance(offlinePlayer);
    }

    @Override
    public boolean has(String playerName, double amount) {
        return getBalance(playerName) >= amount;
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, double amount) {
        return getBalance(offlinePlayer) >= amount;
    }

    @Override
    public boolean has(String playerName, String worldName, double amount) {
        return has(playerName, amount);
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, String worldName, double amount) {
        return has(offlinePlayer, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        Player player = Bukkit.getPlayer(playerName);
        return (player != null && isPlayer(player.getUniqueId())) ? withdrawPlayer(player.getUniqueId(), amount) :
                new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Player not found");
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double amount) {
        return (isPlayer(offlinePlayer.getUniqueId())) ? withdrawPlayer(offlinePlayer.getUniqueId(), amount) :
                new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Player not found or is an NPC");
    }

    private EconomyResponse withdrawPlayer(UUID uuid, double amount) {
        if (!isPlayer(uuid)) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Invalid player UUID");
        }
        double currentBalance = getBalance(uuid);
        if (currentBalance < amount) {
            return new EconomyResponse(0, currentBalance, EconomyResponse.ResponseType.FAILURE, "Insufficient funds");
        }
        double newBalance = currentBalance - amount;
        plugin.playerBank.put(uuid, newBalance);
        UserFile.fileConfig.set(uuid + ".money", newBalance);
        saveUserFile();
        return new EconomyResponse(amount, newBalance, EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
        return withdrawPlayer(playerName, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, String worldName, double amount) {
        return withdrawPlayer(offlinePlayer, amount);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, double amount) {
        Player player = Bukkit.getPlayer(playerName);
        return (player != null && isPlayer(player.getUniqueId())) ? depositPlayer(player.getUniqueId(), amount) :
                new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Player not found");
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double amount) {
        return (isPlayer(offlinePlayer.getUniqueId())) ? depositPlayer(offlinePlayer.getUniqueId(), amount) :
                new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Player not found or is an NPC");
    }

    private EconomyResponse depositPlayer(UUID uuid, double amount) {
        if (!isPlayer(uuid)) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Invalid player UUID");
        }
        double currentBalance = getBalance(uuid);
        double newBalance = currentBalance + amount;
        plugin.playerBank.put(uuid, newBalance);
        UserFile.fileConfig.set(uuid + ".money", newBalance);
        saveUserFile();
        return new EconomyResponse(amount, newBalance, EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        return depositPlayer(playerName, amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, String worldName, double amount) {
        return depositPlayer(offlinePlayer, amount);
    }

    @Override
    public boolean createPlayerAccount(String playerName) {
        Player player = Bukkit.getPlayer(playerName);
        return (player != null && isPlayer(player.getUniqueId())) && createPlayerAccount(player.getUniqueId());
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer) {
        return (isPlayer(offlinePlayer.getUniqueId())) && createPlayerAccount(offlinePlayer.getUniqueId());
    }

    private boolean createPlayerAccount(UUID uuid) {
        if (!isPlayer(uuid) || hasAccount(uuid)) {
            return false;
        }
        initializeBalance(uuid);
        return true;
    }

    @Override
    public boolean createPlayerAccount(String playerName, String worldName) {
        return createPlayerAccount(playerName);
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer, String worldName) {
        return createPlayerAccount(offlinePlayer);
    }

    @Override
    public EconomyResponse createBank(String name, String owner) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Bank support is not enabled");
    }

    @Override
    public EconomyResponse createBank(String name, OfflinePlayer owner) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Bank support is not enabled");
    }

    @Override
    public EconomyResponse deleteBank(String name) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Bank support is not enabled");
    }

    @Override
    public EconomyResponse bankBalance(String name) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Bank support is not enabled");
    }

    @Override
    public EconomyResponse bankHas(String name, double amount) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Bank support is not enabled");
    }

    @Override
    public EconomyResponse bankWithdraw(String name, double amount) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Bank support is not enabled");
    }

    @Override
    public EconomyResponse bankDeposit(String name, double amount) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Bank support is not enabled");
    }

    @Override
    public EconomyResponse isBankOwner(String name, String playerName) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Bank support is not enabled");
    }

    @Override
    public EconomyResponse isBankOwner(String name, OfflinePlayer owner) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Bank support is not enabled");
    }

    @Override
    public EconomyResponse isBankMember(String name, String playerName) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Bank support is not enabled");
    }

    @Override
    public EconomyResponse isBankMember(String name, OfflinePlayer owner) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Bank support is not enabled");
    }

    @Override
    public List<String> getBanks() {
        return Collections.emptyList(); // Bank support is not enabled
    }
}