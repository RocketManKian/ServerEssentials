package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;

        if (sender instanceof Player){
            if (player.hasPermission("se.listhomes")){
                if (args.length == 1) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (ServerEssentials.plugin.getConfig().getBoolean("enable-home-gui")){
                        if (target != null) {
                            String targetname = target.getUniqueId().toString();
                            int index = 0;
                            ConfigurationSection inventorySection = Sethome.fileConfig.getConfigurationSection("Home." + targetname);
                            Integer size = ServerEssentials.plugin.getConfig().getInt("home-gui-size");
                            Inventory inv = Bukkit.createInventory(target, size, ChatColor.AQUA + "Home GUI");
                            if (inventorySection == null){
                                player.sendMessage(ChatColor.RED + "home.yml file is empty or null");
                                return true;
                            }else{
                                assert inventorySection != null;
                                String homeitem = ServerEssentials.plugin.getConfig().getString("home-item");
                                ItemStack item = new ItemStack(Material.getMaterial(homeitem));
                                ItemMeta meta = item.getItemMeta();
                                if (!inventorySection.getKeys(true).isEmpty()){
                                    if (!(inventorySection.getKeys(false).size() > ServerEssentials.plugin.getConfig().getInt("home-gui-size"))){
                                        try {
                                            for (String key : inventorySection.getKeys(false)) {
                                                String homecolour = ServerEssentials.plugin.getConfig().getString("home-name-colour");
                                                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', homecolour + key));
                                                List<String> loreList = new ArrayList<String>();
                                                loreList.add(ChatColor.DARK_PURPLE + "Click to teleport to " + key);
                                                if (player.hasPermission("se.deletehome")){
                                                    loreList.add(ChatColor.RED + "Right Click to Delete Home");
                                                }
                                                meta.setLore(loreList);
                                                item.setItemMeta(meta);
                                                inv.setItem(index, item);
                                                index ++;
                                            }
                                        } catch (NullPointerException e) {
                                            player.sendMessage(ChatColor.GOLD + target.getName() + ChatColor.RED + "hasn't set any Homes.");
                                        }
                                        player.openInventory(inv);
                                        return true;
                                    }else{
                                        player.sendMessage(ChatColor.RED + "GUI Size is too small, increase the value in Config!");
                                        return true;
                                    }
                                }else{
                                    player.sendMessage(ChatColor.GOLD + target.getName() + ChatColor.RED + " hasn't set any Homes.");
                                    return true;
                                }
                            }
                        }
                    }else{
                        if (target != null) {
                            String targetname = target.getUniqueId().toString();
                            if (target != player) {
                                if (args[0].equalsIgnoreCase(target.getName())) {
                                    ConfigurationSection inventorySection = Sethome.fileConfig.getConfigurationSection("Home." + targetname);
                                    if (inventorySection == null) {
                                        player.sendMessage(ChatColor.RED + "home.yml file is empty or null");
                                        return true;
                                    }else if (!inventorySection.getKeys(true).isEmpty()){
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
                                    }else{
                                        assert inventorySection != null;
                                        player.sendMessage(ChatColor.GREEN + "---------------------------"
                                                + "\n" + target.getName() + "'s Home(s) List"
                                                + "\n---------------------------");
                                        player.sendMessage(ChatColor.RED + target.getName() + " hasn't set any homes");
                                        return true;
                                    }
                                }
                            }else{
                                player.sendMessage("Incorrect format! Please use /home to view your homes");
                                return true;
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + "Player does not exist");
                            return true;
                        }
                    }
                }
            }else{
                if (ServerEssentials.plugin.getConfig().getString("no-permission-message").length() == 0){
                    player.sendMessage(ChatColor.RED + "You do not have the required permission (se.listhomes) to run this command.");
                    return true;
                }else{
                    String permission = ServerEssentials.getPlugin().getConfig().getString("no-permission-message");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', permission));
                }
                return true;
            }
        }
        return false;
    }
}
