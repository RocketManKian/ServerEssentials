package me.rocketmankianproductions.serveressentials.eco;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.commands.Eco;
import me.rocketmankianproductions.serveressentials.file.UserFile;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

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
        return 0;
    }

    @Override
    public String format(double v) {
        String currencySymbol = ServerEssentials.plugin.getConfig().getString("currency-symbol", "$");
        String formattedBalance = String.format("%.2f", v);
        return currencySymbol + formattedBalance;
    }

    @Override
    public String currencyNamePlural() {
        return null;
    }

    @Override
    public String currencyNameSingular() {
        return null;
    }

    @Override
    public boolean hasAccount(String s) {
        return false;
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer) {
        return false;
    }

    @Override
    public boolean hasAccount(String s, String s1) {
        return false;
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer, String s) {
        return false;
    }

    @Override
    public double getBalance(String s) {
        Player player = Bukkit.getPlayer(s);
        UUID uuid = player.getUniqueId();
        return plugin.playerBank.get(uuid);
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer) {
        UUID uuid = offlinePlayer.getUniqueId();
        return plugin.playerBank.get(uuid);
    }

    @Override
    public double getBalance(String s, String s1) {
        Player player = Bukkit.getPlayer(s);
        UUID uuid = player.getUniqueId();
        return plugin.playerBank.get(uuid);
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer, String s) {
        UUID uuid = offlinePlayer.getUniqueId();
        return plugin.playerBank.get(uuid);
    }

    @Override
    public boolean has(String s, double v) {
        boolean hasMoney = false;
        Player player = Bukkit.getPlayer(s);
        UUID uuid = player.getUniqueId();
        Double balance = plugin.playerBank.get(uuid);
        if (balance.doubleValue() >= v){
            hasMoney = true;
        }
        return hasMoney;
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, double v) {
        boolean hasMoney = false;
        OfflinePlayer player = offlinePlayer;
        UUID uuid = player.getUniqueId();
        Double balance = plugin.playerBank.get(uuid);
        if (balance.doubleValue() >= v){
            hasMoney = true;
        }
        return hasMoney;
    }

    @Override
    public boolean has(String s, String s1, double v) {
        boolean hasMoney = false;
        Player player = Bukkit.getPlayer(s);
        UUID uuid = player.getUniqueId();
        Double balance = plugin.playerBank.get(uuid);
        if (balance.doubleValue() >= v){
            hasMoney = true;
        }
        return hasMoney;
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, String s, double v) {
        boolean hasMoney = false;
        OfflinePlayer player = offlinePlayer;
        UUID uuid = player.getUniqueId();
        Double balance = plugin.playerBank.get(uuid);
        if (balance.doubleValue() >= v){
            hasMoney = true;
        }
        return hasMoney;
    }

    @Override
    public EconomyResponse withdrawPlayer(String s, double v) {
        Player player = Bukkit.getPlayer(s);
        if (player == null) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Player not found");
        }

        UUID uuid = player.getUniqueId();
        Double oldBalance = plugin.playerBank.get(uuid);

        if (oldBalance == null) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "No balance found for player");
        }

        plugin.playerBank.put(uuid, oldBalance - v);
        UserFile.fileConfig.set(uuid + ".money", plugin.economyImplementer.getBalance(Bukkit.getPlayer(uuid)));
        Eco.saveBalance();
        return new EconomyResponse(v, oldBalance - v, EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double v) {
        UUID uuid = offlinePlayer.getUniqueId();
        Double oldBalance = plugin.playerBank.get(uuid);

        if (oldBalance == null) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "No balance found for player");
        }

        plugin.playerBank.put(uuid, oldBalance - v);
        UserFile.fileConfig.set(uuid + ".money", plugin.economyImplementer.getBalance(Bukkit.getOfflinePlayer(uuid)));
        Eco.saveBalance();
        return new EconomyResponse(v, oldBalance - v, EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse withdrawPlayer(String s, String s1, double v) {
        Player player = Bukkit.getPlayer(s);
        if (player == null) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Player not found");
        }

        UUID uuid = player.getUniqueId();
        Double oldBalance = plugin.playerBank.get(uuid);

        if (oldBalance == null) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "No balance found for player");
        }

        plugin.playerBank.put(uuid, oldBalance - v);
        UserFile.fileConfig.set(uuid + ".money", plugin.economyImplementer.getBalance(Bukkit.getPlayer(uuid)));
        Eco.saveBalance();
        return new EconomyResponse(v, oldBalance - v, EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, String s, double v) {
        UUID uuid = offlinePlayer.getUniqueId();
        Double oldBalance = plugin.playerBank.get(uuid);

        if (oldBalance == null) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "No balance found for player");
        }

        plugin.playerBank.put(uuid, oldBalance - v);
        UserFile.fileConfig.set(uuid + ".money", plugin.economyImplementer.getBalance(Bukkit.getOfflinePlayer(uuid)));
        Eco.saveBalance();
        return new EconomyResponse(v, oldBalance - v, EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse depositPlayer(String s, double v) {
        Player player = Bukkit.getPlayer(s);
        if (player == null) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Player not found");
        }

        UUID uuid = player.getUniqueId();
        Double oldBalance = plugin.playerBank.get(uuid);

        if (oldBalance == null) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "No balance found for player");
        }

        plugin.playerBank.put(uuid, oldBalance + v);
        UserFile.fileConfig.set(uuid + ".money", plugin.economyImplementer.getBalance(Bukkit.getPlayer(uuid)));
        Eco.saveBalance();
        return new EconomyResponse(v, oldBalance + v, EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double v) {
        UUID uuid = offlinePlayer.getUniqueId();
        Double oldBalance = plugin.playerBank.get(uuid);

        if (oldBalance == null) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "No balance found for player");
        }

        plugin.playerBank.put(uuid, oldBalance + v);
        UserFile.fileConfig.set(uuid + ".money", plugin.economyImplementer.getBalance(Bukkit.getOfflinePlayer(uuid)));
        Eco.saveBalance();
        return new EconomyResponse(v, oldBalance + v, EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse depositPlayer(String s, String s1, double v) {
        Player player = Bukkit.getPlayer(s);
        if (player == null) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Player not found");
        }

        UUID uuid = player.getUniqueId();
        Double oldBalance = plugin.playerBank.get(uuid);

        if (oldBalance == null) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "No balance found for player");
        }

        plugin.playerBank.put(uuid, oldBalance + v);
        UserFile.fileConfig.set(uuid + ".money", plugin.economyImplementer.getBalance(Bukkit.getPlayer(uuid)));
        Eco.saveBalance();
        return new EconomyResponse(v, oldBalance + v, EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, String s, double v) {
        UUID uuid = offlinePlayer.getUniqueId();
        Double oldBalance = plugin.playerBank.get(uuid);

        if (oldBalance == null) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "No balance found for player");
        }

        plugin.playerBank.put(uuid, oldBalance + v);
        UserFile.fileConfig.set(uuid + ".money", plugin.economyImplementer.getBalance(Bukkit.getOfflinePlayer(uuid)));
        Eco.saveBalance();
        return new EconomyResponse(v, oldBalance + v, EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse createBank(String s, String s1) {
        return null;
    }

    @Override
    public EconomyResponse createBank(String s, OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public EconomyResponse deleteBank(String s) {
        return null;
    }

    @Override
    public EconomyResponse bankBalance(String s) {
        return null;
    }

    @Override
    public EconomyResponse bankHas(String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse bankWithdraw(String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse bankDeposit(String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String s, String s1) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String s, OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String s, String s1) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String s, OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public List<String> getBanks() {
        return null;
    }

    @Override
    public boolean createPlayerAccount(String s) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(String s, String s1) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer, String s) {
        return false;
    }
}