package me.rocketmankianproductions.serveressentials.eco;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.BankFile;
import me.rocketmankianproductions.serveressentials.file.UserFile;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class EconomyImplementer implements Economy {

    private final ServerEssentials plugin = ServerEssentials.getInstance;

    private static final String TOWN_PREFIX = "town-";
    private static final String NATION_PREFIX = "nation-";

    private final Map<UUID, Double> balances = new ConcurrentHashMap<>();
    private final Map<String, Double> bankBalances = new ConcurrentHashMap<>();

    private final Set<UUID> dirtyAccounts = ConcurrentHashMap.newKeySet();
    private final Set<String> dirtyBanks = ConcurrentHashMap.newKeySet();

    public EconomyImplementer() {
        loadBalances();
        loadBanks();

        Bukkit.getScheduler().runTaskTimerAsynchronously(
                plugin,
                this::saveDirtyBalances,
                20L * 300L,
                20L * 300L
        );

        Bukkit.getScheduler().runTaskTimerAsynchronously(
                plugin,
                this::saveDirtyBanks,
                20L * 300L,
                20L * 300L
        );
    }

    /* ================= LOAD ================= */

    private void loadBalances() {
        if (UserFile.config == null) return;

        for (String key : UserFile.config.getKeys(false)) {
            try {
                UUID uuid = UUID.fromString(key);

                double bal = UserFile.config.getDouble(key + ".money", getStartingBalance());

                balances.put(uuid, bal);
            } catch (Exception ignored) {}
        }
    }

    private void loadBanks() {
        if (BankFile.config == null) return;

        for (String key : BankFile.config.getKeys(false)) {
            bankBalances.put(key,
                    BankFile.config.getDouble(key + ".money", 0.0)
            );
        }
    }

    /* ================= SAVE ================= */

    public void shutdown() {
        saveAllSync();
    }

    private void saveAllSync() {

        for (Map.Entry<UUID, Double> entry : balances.entrySet()) {
            UserFile.config.set(entry.getKey().toString() + ".money", entry.getValue());
        }

        for (Map.Entry<String, Double> entry : bankBalances.entrySet()) {
            BankFile.config.set(entry.getKey() + ".money", entry.getValue());
        }

        UserFile.save();
        BankFile.save();
    }

    private void saveDirtyBalances() {

        if (dirtyAccounts.isEmpty()) return;

        Set<UUID> snapshot = new HashSet<>(dirtyAccounts);

        for (UUID uuid : snapshot) {
            Double bal = balances.get(uuid);
            if (bal != null) {
                UserFile.config.set(uuid.toString() + ".money", bal);
            }
        }

        UserFile.save();
        dirtyAccounts.removeAll(snapshot);
    }

    private void saveDirtyBanks() {

        if (dirtyBanks.isEmpty()) return;

        Set<String> snapshot = new HashSet<>(dirtyBanks);

        for (String bank : snapshot) {
            Double bal = bankBalances.get(bank);
            if (bal != null) {
                BankFile.config.set(bank + ".money", bal);
            }
        }

        BankFile.save();
        dirtyBanks.removeAll(snapshot);
    }

    /* ================= CORE ================= */

    private double getStartingBalance() {
        return plugin.getConfig().getDouble("start-balance", 0.0);
    }

    private boolean isBank(String name) {
        return name != null &&
                (name.startsWith(TOWN_PREFIX) || name.startsWith(NATION_PREFIX));
    }

    private double getBalanceInternal(UUID uuid) {
        return balances.computeIfAbsent(uuid,
                id -> UserFile.config.getDouble(id.toString() + ".money", getStartingBalance())
        );
    }

    private void setBalance(UUID uuid, double amount) {
        balances.put(uuid, amount);
        dirtyAccounts.add(uuid);
    }

    /* ================= ECONOMY INFO ================= */

    @Override public boolean isEnabled() { return true; }
    @Override public String getName() { return "ServerEssentials"; }
    @Override public boolean hasBankSupport() { return true; }
    @Override public int fractionalDigits() { return 2; }

    @Override
    public String format(double amount) {
        return plugin.getConfig().getString("currency-symbol", "$")
                + String.format("%.2f", amount);
    }

    @Override
    public String currencyNamePlural() {
        return plugin.getConfig().getString("currency-name-plural", "Dollars");
    }

    @Override
    public String currencyNameSingular() {
        return plugin.getConfig().getString("currency-name-singular", "Dollar");
    }

    /* ================= ACCOUNT CHECK ================= */

    @Override
    public boolean hasAccount(String playerName) {
        OfflinePlayer p = Bukkit.getOfflinePlayer(playerName);
        return balances.containsKey(p.getUniqueId());
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer) {
        return balances.containsKey(offlinePlayer.getUniqueId());
    }

    @Override
    public boolean hasAccount(String playerName, String worldName) {
        return hasAccount(playerName);
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer, String worldName) {
        return hasAccount(offlinePlayer);
    }

    /* ================= BALANCE ================= */

    @Override
    public double getBalance(String playerName) {
        return getBalance(Bukkit.getOfflinePlayer(playerName).getUniqueId());
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer) {
        return getBalance(offlinePlayer.getUniqueId());
    }

    private double getBalance(UUID uuid) {
        return getBalanceInternal(uuid);
    }

    @Override
    public double getBalance(String playerName, String worldName) {
        return getBalance(playerName);
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer, String worldName) {
        return getBalance(offlinePlayer);
    }

    /* ================= HAS ================= */

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

    /* ================= WITHDRAW ================= */

    @Override
    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        return withdraw(Bukkit.getOfflinePlayer(playerName).getUniqueId(), amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double amount) {
        return withdraw(offlinePlayer.getUniqueId(), amount);
    }

    private EconomyResponse withdraw(UUID uuid, double amount) {

        if (amount < 0)
            return fail(0, getBalance(uuid), "Invalid amount");

        double bal = getBalance(uuid);

        if (bal < amount)
            return fail(0, bal, "Insufficient funds");

        double newBal = bal - amount;

        setBalance(uuid, newBal);

        return success(amount, newBal);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
        return withdrawPlayer(playerName, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, String worldName, double amount) {
        return withdrawPlayer(offlinePlayer, amount);
    }

    /* ================= DEPOSIT ================= */

    @Override
    public EconomyResponse depositPlayer(String playerName, double amount) {
        return deposit(Bukkit.getOfflinePlayer(playerName).getUniqueId(), amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double amount) {
        return deposit(offlinePlayer.getUniqueId(), amount);
    }

    private EconomyResponse deposit(UUID uuid, double amount) {

        if (amount < 0)
            return fail(0, getBalance(uuid), "Invalid amount");

        double newBal = getBalance(uuid) + amount;

        setBalance(uuid, newBal);

        return success(amount, newBal);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        return depositPlayer(playerName, amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, String worldName, double amount) {
        return depositPlayer(offlinePlayer, amount);
    }

    /* ================= CREATE ACCOUNT ================= */

    @Override
    public boolean createPlayerAccount(String playerName) {
        return createPlayerAccount(Bukkit.getOfflinePlayer(playerName));
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer) {
        UUID uuid = offlinePlayer.getUniqueId();

        if (balances.containsKey(uuid)) return false;

        setBalance(uuid, getStartingBalance());
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

    /* ================= BANKS ================= */

    @Override
    public EconomyResponse createBank(String name, String playerName) {
        bankBalances.putIfAbsent(name, 0.0);
        return success(0, 0);
    }

    @Override
    public EconomyResponse createBank(String name, OfflinePlayer player) {
        return createBank(name, player.getName());
    }

    @Override
    public EconomyResponse bankBalance(String name) {
        double bal = bankBalances.getOrDefault(name, 0.0);
        return success(bal, bal);
    }

    @Override
    public EconomyResponse bankDeposit(String name, double amount) {

        if (!bankBalances.containsKey(name))
            return fail(0, 0, "Bank does not exist");

        double bal = bankBalances.get(name);

        if (amount < 0)
            return fail(0, bal, "Invalid amount");

        double newBal = bal + amount;

        bankBalances.put(name, newBal);
        dirtyBanks.add(name);

        return success(amount, newBal);
    }

    @Override
    public EconomyResponse bankWithdraw(String name, double amount) {

        if (!bankBalances.containsKey(name))
            return fail(0, 0, "Bank does not exist");

        double bal = bankBalances.get(name);

        if (amount < 0)
            return fail(0, bal, "Invalid amount");

        if (bal < amount)
            return fail(0, bal, "Insufficient funds");

        double newBal = bal - amount;

        bankBalances.put(name, newBal);
        dirtyBanks.add(name);

        return success(amount, newBal);
    }

    @Override
    public EconomyResponse bankHas(String name, double amount) {
        double bal = bankBalances.getOrDefault(name, 0.0);
        return bal >= amount ? success(bal, bal) : fail(bal, bal, "Not enough");
    }

    @Override
    public EconomyResponse deleteBank(String name) {
        bankBalances.remove(name);
        return success(0, 0);
    }

    @Override
    public EconomyResponse isBankOwner(String name, String playerName) {
        return success(0, 0);
    }

    @Override
    public EconomyResponse isBankOwner(String name, OfflinePlayer player) {
        return success(0, 0);
    }

    @Override
    public EconomyResponse isBankMember(String name, String playerName) {
        return success(0, 0);
    }

    @Override
    public EconomyResponse isBankMember(String name, OfflinePlayer player) {
        return success(0, 0);
    }

    @Override
    public List<String> getBanks() {
        return new ArrayList<>(bankBalances.keySet());
    }

    /* ================= HELPERS ================= */

    private EconomyResponse success(double amount, double balance) {
        return new EconomyResponse(amount, balance, EconomyResponse.ResponseType.SUCCESS, null);
    }

    private EconomyResponse fail(double amount, double balance, String msg) {
        return new EconomyResponse(amount, balance, EconomyResponse.ResponseType.FAILURE, msg);
    }
}