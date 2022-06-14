package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ListHomes implements CommandExecutor {

    public static OfflinePlayer target;

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            if (player.hasPermission("se.listhomes") || player.hasPermission("se.all")){
                if (args.length == 1) {
                    target = Bukkit.getOfflinePlayer(args[0]);
                    if (ServerEssentials.plugin.getConfig().getBoolean("enable-home-gui")){
                        if (target.hasPlayedBefore()) {
                            String targetname = target.getUniqueId().toString();
                            if (target != player) {
                                int index = 0;
                                ConfigurationSection inventorySection = Sethome.fileConfig.getConfigurationSection("Home." + targetname);
                                Integer size = ServerEssentials.plugin.getConfig().getInt("home-gui-size");
                                Inventory inv = Bukkit.createInventory(target.getPlayer(), size, ChatColor.translateAlternateColorCodes('&', Lang.fileConfig.getString("target-home-gui-name").replace("<target>", target.getName())));
                                if (inventorySection == null) {
                                    String msg = Lang.fileConfig.getString("home-file-error");
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                    return true;
                                } else {
                                    assert inventorySection != null;
                                    String homeitem = ServerEssentials.plugin.getConfig().getString("home-item");
                                    ItemStack item = new ItemStack(Material.getMaterial(homeitem));
                                    ItemMeta meta = item.getItemMeta();
                                    if (!inventorySection.getKeys(true).isEmpty()) {
                                        if (!(inventorySection.getKeys(false).size() > ServerEssentials.plugin.getConfig().getInt("home-gui-size"))) {
                                            try {
                                                for (String key : inventorySection.getKeys(false)) {
                                                    String homecolour = ServerEssentials.plugin.getConfig().getString("home-name-colour");
                                                    meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', homecolour + key));
                                                    List<String> loreList = new ArrayList<String>();
                                                    String msg = Lang.fileConfig.getString("home-gui-left-click").replace("<home>", key);
                                                    loreList.add(ChatColor.translateAlternateColorCodes('&', msg));
                                                    if (player.hasPermission("se.deletehome.others") || player.hasPermission("se.all")) {
                                                        String msg2 = Lang.fileConfig.getString("home-gui-right-click");
                                                        loreList.add(ChatColor.translateAlternateColorCodes('&', msg2));
                                                    }
                                                    meta.setLore(loreList);
                                                    item.setItemMeta(meta);
                                                    inv.setItem(index, item);
                                                    index++;
                                                }
                                            } catch (NullPointerException e) {
                                                String msg = Lang.fileConfig.getString("no-homes-set-target").replace("<target>", target.getName());
                                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                            }
                                            player.openInventory(inv);
                                            return true;
                                        } else {
                                            String msg = Lang.fileConfig.getString("home-gui-error");
                                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                            return true;
                                        }
                                    } else {
                                        String msg = Lang.fileConfig.getString("no-homes-set-target").replace("<target>", target.getName());
                                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                        return true;
                                    }
                                }
                            } else {
                                String msg = Lang.fileConfig.getString("listhomes-self");
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            }
                        }else{
                            String msg = Lang.fileConfig.getString("player-offline");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                            return true;
                        }
                    }else{
                        if (target.hasPlayedBefore()) {
                            String targetname = target.getUniqueId().toString();
                            if (target != player) {
                                if (args[0].equalsIgnoreCase(target.getName())) {
                                    ConfigurationSection inventorySection = Sethome.fileConfig.getConfigurationSection("Home." + targetname);
                                    if (inventorySection == null) {
                                        String msg = Lang.fileConfig.getString("home-file-error");
                                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                        return true;
                                    } else if (!inventorySection.getKeys(true).isEmpty()) {
                                        assert inventorySection != null;
                                        player.sendMessage(ChatColor.GREEN + "---------------------------"
                                                + "\n" + target.getName() + "'s Home(s) List"
                                                + "\n---------------------------");
                                        try {
                                            for (String key : inventorySection.getKeys(false)) {
                                                player.sendMessage(ChatColor.GOLD + key);
                                            }
                                        } catch (NullPointerException e) {
                                            //Bleh
                                        }
                                        return true;
                                    } else {
                                        assert inventorySection != null;
                                        player.sendMessage(ChatColor.GREEN + "---------------------------"
                                                + "\n" + target.getName() + "'s Home(s) List"
                                                + "\n---------------------------");
                                        String msg = Lang.fileConfig.getString("no-homes-set-target").replace("<target>", target.getName());
                                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                        return true;
                                    }
                                }
                            } else {
                                String msg = Lang.fileConfig.getString("listhomes-self");
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            }
                        }else{
                            String msg = Lang.fileConfig.getString("player-offline");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                            return true;
                        }
                    }
                }else{
                    String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/listhomes (player)");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    return true;
                }
            }else{
                String perm = Lang.fileConfig.getString("no-permission-message").replace("<permission>", "se.listhomes");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', perm));
                return true;
            }
        }else{
            String console = Lang.fileConfig.getString("console-invalid");
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', console));
            return true;
        }
        return false;
    }
}