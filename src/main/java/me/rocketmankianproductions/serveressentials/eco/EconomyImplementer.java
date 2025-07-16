package me.rocketmankianproductions.serveressentials.eco;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.BankFile;
import me.rocketmankianproductions.serveressentials.file.UserFile;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.*;

public class EconomyImplementer implements Economy {

    private static final String TOWN_PREFIX = "town-";
    private static final String NATION_PREFIX = "nation-";

    private final ServerEssentials plugin = ServerEssentials.getInstance;

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
        return true;
    }

    @Override
    public int fractionalDigits() {
        return 2;
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
        double balance = UserFile.fileConfig.getDouble(uuid + ".money", initializeBalance(uuid));
        plugin.playerBank.put(uuid, balance); // sync memory cache
        return balance;
    }

    // This method should strictly check if the UUID belongs to a real player.
    // Towny's "players" for banks are typically just names.
    private boolean isPlayer(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player != null && player.hasMetadata("NPC")) {
            return false; // Exclude NPCs
        }
        OfflinePlayer off = Bukkit.getOfflinePlayer(uuid);

        // A valid OfflinePlayer for a player account must have played before or be online,
        // and its name shouldn't be null.
        return off != null && (off.hasPlayedBefore() || off.isOnline()) && off.getName() != null;
    }

    // Helper to check if a name refers to a town or nation bank
    private boolean isBankName(String name) {
        return name != null && (name.startsWith(TOWN_PREFIX) || name.startsWith(NATION_PREFIX));
    }

    @Override
    public boolean hasAccount(String playerName) {
        // First, check if it's a town/nation bank account
        if (isBankName(playerName)) {
            return BankFile.config.contains(normalizeBankName(playerName));
        }

        // If not a bank, assume it's a player name
        Player player = Bukkit.getPlayer(playerName);
        if (player != null) { // Player is online
            return isPlayer(player.getUniqueId()) && UserFile.fileConfig.contains(player.getUniqueId().toString());
        } else { // Player is offline, try to get their UUID
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);
            if (offlinePlayer.getUniqueId() != null) { // Check if UUID is available for offline player
                return isPlayer(offlinePlayer.getUniqueId()) && UserFile.fileConfig.contains(offlinePlayer.getUniqueId().toString());
            }
        }
        return false;
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer) {
        // If the OfflinePlayer is actually representing a town (e.g., from Towny),
        // we should treat it as a bank. This is a common pattern for Vault.
        if (offlinePlayer.getName() != null && isBankName(offlinePlayer.getName())) {
            return BankFile.config.contains(normalizeBankName(offlinePlayer.getName()));
        }

        // Otherwise, assume it's a regular player
        return isPlayer(offlinePlayer.getUniqueId()) && UserFile.fileConfig.contains(offlinePlayer.getUniqueId().toString());
    }

    // This private method is for internal UUID-based player account checks only
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
        // If it's a bank name, get the bank balance
        if (isBankName(playerName)) {
            return bankBalance(playerName).amount; // Using the existing bankBalance method
        }

        // Otherwise, treat as a player
        Player player = Bukkit.getPlayer(playerName);
        if (player != null && isPlayer(player.getUniqueId())) {
            return getBalance(player.getUniqueId());
        } else {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);
            if (offlinePlayer.getUniqueId() != null && isPlayer(offlinePlayer.getUniqueId())) {
                return getBalance(offlinePlayer.getUniqueId());
            }
        }
        return 0.0;
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer) {
        // Check if the OfflinePlayer represents a bank (e.g., via name)
        if (offlinePlayer.getName() != null && isBankName(offlinePlayer.getName())) {
            return bankBalance(offlinePlayer.getName()).amount;
        }
        // Otherwise, treat as a player
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
        // If it's a bank name, use bankWithdraw
        if (isBankName(playerName)) {
            return bankWithdraw(playerName, amount);
        }

        // Otherwise, treat as a player
        Player player = Bukkit.getPlayer(playerName);
        if (player != null && isPlayer(player.getUniqueId())) {
            return withdrawPlayer(player.getUniqueId(), amount);
        } else {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);
            if (offlinePlayer.getUniqueId() != null && isPlayer(offlinePlayer.getUniqueId())) {
                return withdrawPlayer(offlinePlayer.getUniqueId(), amount);
            }
        }
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Player or bank not found");
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double amount) {
        // If the OfflinePlayer represents a bank, use bankWithdraw
        if (offlinePlayer.getName() != null && isBankName(offlinePlayer.getName())) {
            return bankWithdraw(offlinePlayer.getName(), amount);
        }

        // Otherwise, treat as a player
        initializeBalance(offlinePlayer.getUniqueId()); // Ensure balance is initialized for players
        return (isPlayer(offlinePlayer.getUniqueId()))
                ? withdrawPlayer(offlinePlayer.getUniqueId(), amount)
                : new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Player not found or is an NPC");
    }

    private EconomyResponse withdrawPlayer(UUID uuid, double amount) {
        if (!isPlayer(uuid)) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Invalid player UUID or not a player");
        }

        double currentBalance = getBalanceOrInitialize(uuid);

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
        // If it's a bank name, use bankDeposit
        if (isBankName(playerName)) {
            return bankDeposit(playerName, amount);
        }

        // Otherwise, treat as a player
        Player player = Bukkit.getPlayer(playerName);
        if (player != null && isPlayer(player.getUniqueId())) {
            return depositPlayer(player.getUniqueId(), amount);
        } else {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);
            if (offlinePlayer.getUniqueId() != null && isPlayer(offlinePlayer.getUniqueId())) {
                return depositPlayer(offlinePlayer.getUniqueId(), amount);
            }
        }
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Player or bank not found");
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double amount) {
        // If the OfflinePlayer represents a bank, use bankDeposit
        if (offlinePlayer.getName() != null && isBankName(offlinePlayer.getName())) {
            return bankDeposit(offlinePlayer.getName(), amount);
        }

        // Otherwise, treat as a player
        initializeBalance(offlinePlayer.getUniqueId()); // Ensure balance is initialized for players
        return (isPlayer(offlinePlayer.getUniqueId()))
                ? depositPlayer(offlinePlayer.getUniqueId(), amount)
                : new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Player not found or is an NPC");
    }

    private EconomyResponse depositPlayer(UUID uuid, double amount) {
        if (!isPlayer(uuid)) { // Ensure it's a valid player UUID for player accounts
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Invalid player UUID or not a player");
        }

        double currentBalance = getBalanceOrInitialize(uuid);
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
        // If it's a bank name, create a bank account instead
        if (isBankName(playerName)) {
            return createBank(playerName, playerName).type == EconomyResponse.ResponseType.SUCCESS;
        }

        // Otherwise, try to create a player account
        Player player = Bukkit.getPlayer(playerName);
        return (player != null && isPlayer(player.getUniqueId())) && createPlayerAccount(player.getUniqueId());
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer) {
        // If the OfflinePlayer represents a bank, create a bank account
        if (offlinePlayer.getName() != null && isBankName(offlinePlayer.getName())) {
            return createBank(offlinePlayer.getName(), offlinePlayer.getName()).type == EconomyResponse.ResponseType.SUCCESS;
        }
        // Otherwise, create a player account
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
    public EconomyResponse createBank(String name, String playerName) {
        String bankName = normalizeBankName(name);

        if (BankFile.config.contains(bankName)) {
            return new EconomyResponse(0, BankFile.config.getDouble(bankName + ".money"), EconomyResponse.ResponseType.SUCCESS, "Bank already exists");
        }

        BankFile.config.set(bankName + ".money", 0.0);
        BankFile.config.set(bankName + ".owner", playerName);
        BankFile.config.set(bankName + ".members", new ArrayList<>());
        BankFile.save();

        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse createBank(String s, OfflinePlayer offlinePlayer) {
        return createBank(s, offlinePlayer.getName());
    }

    @Override
    public EconomyResponse bankBalance(String name) {
        String bankName = normalizeBankName(name);

        // If the bank doesn't exist, we don't necessarily want to create it here
        // as bankBalance is for querying. Let's just return 0.0 if not found.
        if (!BankFile.config.contains(bankName)) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Bank does not exist");
        }
        double balance = BankFile.config.getDouble(bankName + ".money");
        return new EconomyResponse(balance, balance, EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse bankDeposit(String name, double amount) {
        String bankName = normalizeBankName(name);

        if (!BankFile.config.contains(bankName)) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Bank does not exist");
        }

        double currentBalance = BankFile.config.getDouble(bankName + ".money");
        double newBalance = currentBalance + amount;
        BankFile.config.set(bankName + ".money", newBalance); // Use bankName here, not 'name'
        BankFile.save();

        return new EconomyResponse(amount, newBalance, EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse bankWithdraw(String name, double amount) {
        String bankName = normalizeBankName(name);

        if (!BankFile.config.contains(bankName)) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Bank does not exist");
        }
        double currentBalance = BankFile.config.getDouble(bankName + ".money");
        if (currentBalance < amount) {
            return new EconomyResponse(0, currentBalance, EconomyResponse.ResponseType.FAILURE, "Insufficient funds");
        }
        double newBalance = currentBalance - amount;
        BankFile.config.set(bankName + ".money", newBalance);
        BankFile.save();
        return new EconomyResponse(amount, newBalance, EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse bankHas(String name, double amount) {
        String bankName = normalizeBankName(name);

        if (!BankFile.config.contains(bankName)) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Bank does not exist");
        }
        double balance = BankFile.config.getDouble(bankName + ".money");
        boolean hasEnough = balance >= amount;
        return new EconomyResponse(balance, balance, hasEnough ? EconomyResponse.ResponseType.SUCCESS : EconomyResponse.ResponseType.FAILURE, null);
    }

    @Override
    public EconomyResponse deleteBank(String name) {
        String bankName = normalizeBankName(name);

        if (!BankFile.config.contains(bankName)) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Bank does not exist");
        }
        BankFile.config.set(bankName, null);  // Remove the bank from the config
        BankFile.save();  // Save the updated config
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.SUCCESS, "Bank deleted successfully");
    }

    @Override
    public EconomyResponse isBankOwner(String name, String playerName) {
        String bankName = normalizeBankName(name);

        if (!BankFile.config.contains(bankName)) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Bank does not exist");
        }

        String bankOwner = BankFile.config.getString(bankName + ".owner");

        if (bankOwner != null && bankOwner.equalsIgnoreCase(playerName)) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.SUCCESS, "Player is the owner of the bank");
        }

        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Player is not the owner of the bank");
    }

    @Override
    public EconomyResponse isBankOwner(String name, OfflinePlayer owner) {
        // Ensure owner.getName() is not null before passing
        return owner.getName() != null ? isBankOwner(name, owner.getName()) : new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Owner name is null");
    }

    @Override
    public EconomyResponse isBankMember(String name, String playerName) {
        String bankName = normalizeBankName(name);

        if (!BankFile.config.contains(bankName)) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Bank does not exist");
        }

        List<String> members = BankFile.config.getStringList(bankName + ".members");

        if (members != null && members.contains(playerName)) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.SUCCESS, "Player is a member of the bank");
        }

        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Player is not a member of the bank");
    }

    @Override
    public EconomyResponse isBankMember(String name, OfflinePlayer owner) {
        // Ensure owner.getName() is not null before passing
        return owner.getName() != null ? isBankMember(name, owner.getName()) : new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Owner name is null");
    }

    @Override
    public List<String> getBanks() {
        if (BankFile.config == null) {
            return Collections.emptyList();
        }

        Set<String> bankKeys = BankFile.config.getKeys(false);
        if (bankKeys == null || bankKeys.isEmpty()) {
            return Collections.emptyList();
        }

        // Filter for actual banks (those with .money, .owner, .members keys)
        // This prevents other top-level keys from being returned as "banks"
        List<String> actualBanks = new ArrayList<>();
        for (String key : bankKeys) {
            if (BankFile.config.contains(key + ".money") &&
                    BankFile.config.contains(key + ".owner") &&
                    BankFile.config.contains(key + ".members")) {
                actualBanks.add(key);
            }
        }
        return actualBanks;
    }

    // This method is now used for display or internal checks, but normalizeBankName is for storage/lookup
    private String stripPrefix(String bankName) {
        if (bankName != null) {
            if (bankName.startsWith(TOWN_PREFIX)) {
                return bankName.substring(TOWN_PREFIX.length());
            } else if (bankName.startsWith(NATION_PREFIX)) {
                return bankName.substring(NATION_PREFIX.length());
            }
        }
        return bankName;
    }

    // This method ensures the bank name is consistent for storage and retrieval.
    // Towny typically sends names with prefixes already, so it's often a direct pass-through.
    private String normalizeBankName(String bankName) {
        // You could add logic here if you expect names without prefixes and want to add them.
        // For now, assuming Towny always sends prefixed names.
        return bankName;
    }
}