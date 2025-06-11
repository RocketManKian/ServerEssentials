package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.events.PlayerClickEvent; // Import PlayerClickEvent for pagination maps and constants
import me.rocketmankianproductions.serveressentials.file.Lang;
import me.rocketmankianproductions.serveressentials.utils.GUIPaginationHelper; // Import the pagination helper
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
import java.util.UUID;
import java.util.stream.Collectors;

import static me.rocketmankianproductions.serveressentials.ServerEssentials.hex;
import static me.rocketmankianproductions.serveressentials.ServerEssentials.plugin;

public class ListHomes implements CommandExecutor {

    public static OfflinePlayer target; // This variable is used in PlayerClickEvent.handleTargetHomeClick

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            String console = Lang.fileConfig.getString("console-invalid");
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', hex(console)));
            return true;
        }

        Player player = (Player) sender;
        if (!ServerEssentials.permissionChecker(player, "se.listhomes")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(Lang.fileConfig.getString("no-permission"))));
            return true;
        }

        if (args.length == 1) {
            target = Bukkit.getOfflinePlayer(args[0]); // Set the target player for potential use in PlayerClickEvent

            if (!target.hasPlayedBefore()) {
                String msg = Lang.fileConfig.getString("player-offline");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                return true;
            }

            if (target.equals(player)) { // Check if target is the player themselves
                String msg = Lang.fileConfig.getString("listhomes-self");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                return true;
            }

            if (ServerEssentials.plugin.getConfig().getBoolean("enable-home-gui")) {
                // Open the paginated GUI for the target player's homes
                openTargetHomeGUI(player, 1, target); // Open the first page
                return true;
            } else {
                // Fallback to text list if GUI is disabled
                String targetname = target.getUniqueId().toString();
                ConfigurationSection inventorySection = Sethome.fileConfig.getConfigurationSection("Home." + targetname);

                if (inventorySection == null || inventorySection.getKeys(false).isEmpty()) {
                    String msg = Lang.fileConfig.getString("no-homes-set-target").replace("<target>", target.getName());
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                    return true;
                }

                player.sendMessage(ChatColor.GREEN + "---------------------------"
                        + "\n" + target.getName() + "'s Home(s) List"
                        + "\n---------------------------");
                for (String key : inventorySection.getKeys(false)) {
                    player.sendMessage(ChatColor.GOLD + key);
                }
                return true;
            }
        } else {
            String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/listhomes (player)");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
            return true;
        }
    }

    /**
     * Opens the paginated Home GUI for a specified target player and page.
     * This method retrieves all homes for the target, processes them into ItemStacks,
     * extracts the items for the current page, and then uses GUIPaginationHelper
     * to display them along with navigation buttons.
     *
     * @param viewer      The player viewing the GUI.
     * @param page        The page number to display (1-indexed).
     * @param targetPlayer The OfflinePlayer whose homes are being listed.
     */
    public static void openTargetHomeGUI(Player viewer, int page, OfflinePlayer targetPlayer) {
        List<ItemStack> allTargetHomeItems = new ArrayList<>();
        UUID targetUUID = targetPlayer.getUniqueId();
        ConfigurationSection homeSection = Sethome.fileConfig.getConfigurationSection("Home." + targetUUID);

        if (homeSection != null && !homeSection.getKeys(false).isEmpty()) {
            allTargetHomeItems = homeSection.getKeys(false).stream()
                    .sorted(String.CASE_INSENSITIVE_ORDER) // Sort homes by name
                    .map(homeName -> {
                        // Assuming home item is configurable in config.yml
                        String homeItemMaterialName = ServerEssentials.plugin.getConfig().getString("home-item", "BED"); // Default to BED
                        Material material = Material.getMaterial(homeItemMaterialName);
                        if (material == null) {
                            material = Material.RED_BED; // Fallback if material name is invalid
                        }

                        ItemStack item = new ItemStack(material);
                        ItemMeta meta = item.getItemMeta();
                        if (meta != null) {
                            String homeColour = ServerEssentials.plugin.getConfig().getString("home-name-colour", "&a"); // Default to green
                            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', homeColour + homeName));

                            List<String> loreList = new ArrayList<>();
                            String leftClickMsg = Lang.fileConfig.getString("home-gui-left-click", "&7Left-Click to teleport to <home>.")
                                    .replace("<home>", homeName);
                            loreList.add(ChatColor.translateAlternateColorCodes('&', hex(leftClickMsg)));

                            if (viewer.hasPermission("se.deletehome.others")) { // Check viewer's permission for deleting others' homes
                                String rightClickMsg = Lang.fileConfig.getString("home-gui-right-click", "&cRight-Click to delete.");
                                loreList.add(ChatColor.translateAlternateColorCodes('&', hex(rightClickMsg)));
                            }
                            meta.setLore(loreList);
                            item.setItemMeta(meta);
                        }
                        return item;
                    })
                    .collect(Collectors.toList());
        }

        int totalItems = allTargetHomeItems.size();
        int totalPages = (int) Math.ceil((double) totalItems / PlayerClickEvent.HOME_ITEMS_PER_PAGE);
        if (totalPages == 0) totalPages = 1; // Always at least one page

        // Ensure current page is within valid bounds
        if (page < 1) page = 1;
        if (page > totalPages) page = totalPages;

        int startIndex = (page - 1) * PlayerClickEvent.HOME_ITEMS_PER_PAGE;
        int endIndex = Math.min(startIndex + PlayerClickEvent.HOME_ITEMS_PER_PAGE, totalItems);
        List<ItemStack> itemsOnCurrentPage = allTargetHomeItems.subList(startIndex, endIndex);

        String guiTitle = ChatColor.translateAlternateColorCodes('&', Lang.fileConfig.getString("target-home-gui-name", "&a&l<target>'s Homes"))
                .replace("<target>", targetPlayer.getName());
        // Force GUI size to 27 for pagination (25 items + 2 buttons)
        int inventorySize = plugin.getConfig().getInt("home-gui-size");

        Inventory gui = Bukkit.createInventory(viewer, inventorySize, guiTitle);

        GUIPaginationHelper.updatePaginationButtons(gui, page, totalPages, itemsOnCurrentPage);

        viewer.openInventory(gui);

        // Store the current page for this viewer
        PlayerClickEvent.playerTargetHomePages.put(viewer.getUniqueId(), page);

        if (totalItems == 0) {
            String msg = Lang.fileConfig.getString("no-homes-set-target").replace("<target>", targetPlayer.getName());
            viewer.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
        }
    }
}