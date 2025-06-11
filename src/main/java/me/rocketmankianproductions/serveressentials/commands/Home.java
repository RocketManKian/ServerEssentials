package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.events.PlayerClickEvent; // Import PlayerClickEvent for pagination maps and constants
import me.rocketmankianproductions.serveressentials.file.Lang;
import me.rocketmankianproductions.serveressentials.utils.GUIPaginationHelper; // Import the pagination helper
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static me.rocketmankianproductions.serveressentials.ServerEssentials.hex;
import static me.rocketmankianproductions.serveressentials.ServerEssentials.plugin;

public class Home implements CommandExecutor {

    public static HashMap<UUID, Integer> hometeleport = new HashMap<>();
    public static ArrayList<UUID> cancel = new ArrayList<>(); // Used for movement cancellation

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            String console = Lang.fileConfig.getString("console-invalid");
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', hex(console)));
            return true;
        }

        Player player = (Player) sender;
        if (!ServerEssentials.permissionChecker(player, "se.home")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(Lang.fileConfig.getString("no-permission"))));
            return true;
        }

        UUID playerUUID = player.getUniqueId();
        int delay = ServerEssentials.plugin.getConfig().getInt("home-teleport");
        boolean enableSubtitle = ServerEssentials.plugin.getConfig().getBoolean("enable-home-subtitle");
        boolean movementCancel = ServerEssentials.plugin.getConfig().getBoolean("home-movement-cancel");

        if (args.length == 0) {
            // Display paginated home GUI
            if (ServerEssentials.plugin.getConfig().getBoolean("enable-home-gui")) {
                openHomeGUI(player, 1); // Open the first page of the home GUI
            } else {
                // Fallback to text list if GUI is disabled
                ConfigurationSection inventorySection = Sethome.fileConfig.getConfigurationSection("Home." + playerUUID);
                if (inventorySection == null || inventorySection.getKeys(false).isEmpty()) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(Lang.fileConfig.getString("no-homes-set"))));
                    return true;
                }

                player.sendMessage(ChatColor.GREEN + "---------------------------"
                        + "\nHome(s) List"
                        + "\n---------------------------");
                for (String key : inventorySection.getKeys(false)) {
                    player.sendMessage(ChatColor.GOLD + key);
                }
            }
            return true;
        } else if (args.length == 1) {
            // Teleport to a specific home
            String homeName = args[0];
            Location loc = getLocation(player, homeName);

            if (loc == null || !loc.isWorldLoaded()) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(Lang.fileConfig.getString("home-invalid").replace("<home>", homeName))));
                return true;
            }

            // Use PlayerClickEvent's centralized teleportation method
            // Pass null for target since it's a self-teleport
            PlayerClickEvent.handleTeleportation(player, loc, "home-message", "enable-home-subtitle", homeName,
                    delay, movementCancel, Home.cancel, Home.hometeleport);
            return true;
        } else if (args.length == 2) {
            // Teleport to another player's home (requires 'se.home.others' permission)
            if (!ServerEssentials.permissionChecker(player, "se.home.others")) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(Lang.fileConfig.getString("no-permission"))));
                return true;
            }

            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
            String homeName = args[1];

            if (!target.hasPlayedBefore() || target == null) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(Lang.fileConfig.getString("player-offline"))));
                return true;
            }

            // Get location for the target player's home
            Location loc = getLocation(target, homeName); // Use getLocation with OfflinePlayer

            if (loc == null || !loc.isWorldLoaded()) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(Lang.fileConfig.getString("home-invalid").replace("<home>", homeName))));
                return true;
            }

            // Use PlayerClickEvent's centralized teleportation method with target
            PlayerClickEvent.handleTeleportation(player, loc, "home-teleport-target", "enable-home-subtitle", homeName,
                    delay, movementCancel, Home.cancel, Home.hometeleport, target);
            return true;
        } else {
            String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/home (home) OR /home (player) (home)");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
            return true;
        }
    }

    /**
     * Opens the paginated Home GUI for the specified player and page.
     * This method retrieves all homes for the player, processes them into ItemStacks,
     * extracts the items for the current page, and then uses GUIPaginationHelper
     * to display them along with navigation buttons.
     *
     * @param player The player to open the GUI for.
     * @param page   The page number to display (1-indexed).
     */
    public static void openHomeGUI(Player player, int page) {
        List<ItemStack> allHomeItems = new ArrayList<>();
        UUID playerUUID = player.getUniqueId();
        ConfigurationSection homeSection = Sethome.fileConfig.getConfigurationSection("Home." + playerUUID);

        if (homeSection != null && !homeSection.getKeys(false).isEmpty()) {
            allHomeItems = homeSection.getKeys(false).stream()
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

                            if (player.hasPermission("se.deletehome")) {
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

        int totalItems = allHomeItems.size();
        int totalPages = (int) Math.ceil((double) totalItems / PlayerClickEvent.HOME_ITEMS_PER_PAGE);
        if (totalPages == 0) totalPages = 1; // Always at least one page

        // Ensure current page is within valid bounds
        if (page < 1) page = 1;
        if (page > totalPages) page = totalPages;

        int startIndex = (page - 1) * PlayerClickEvent.HOME_ITEMS_PER_PAGE;
        int endIndex = Math.min(startIndex + PlayerClickEvent.HOME_ITEMS_PER_PAGE, totalItems);
        List<ItemStack> itemsOnCurrentPage = allHomeItems.subList(startIndex, endIndex);

        String guiTitle = ChatColor.translateAlternateColorCodes('&', Lang.fileConfig.getString("home-gui-name", "&a&lHomes"));
        // Force GUI size to 27 for pagination (25 items + 2 buttons)
        int inventorySize = plugin.getConfig().getInt("home-gui-size");

        Inventory gui = Bukkit.createInventory(player, inventorySize, guiTitle);

        GUIPaginationHelper.updatePaginationButtons(gui, page, totalPages, itemsOnCurrentPage);

        player.openInventory(gui);

        PlayerClickEvent.playerHomePages.put(player.getUniqueId(), page);

        if (totalItems == 0) {
            String msg = Lang.fileConfig.getString("no-homes-set");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
        }
    }


    /**
     * Retrieves the Location for a given home of a specific player.
     * This method is overloaded to accept an OfflinePlayer to fetch homes for other players.
     *
     * @param targetPlayer The OfflinePlayer who owns the home.
     * @param home         The name of the home.
     * @return The Location of the home, or null if invalid or not found.
     */
    public static Location getLocation(OfflinePlayer targetPlayer, String home) {
        UUID name = targetPlayer.getUniqueId();
        String worldName = Sethome.fileConfig.getString("Home." + name + "." + home + ".World");
        if (worldName == null) {
            return null; // World name is missing
        }
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            return null; // World does not exist or is not loaded
        }
        double x = Sethome.fileConfig.getDouble("Home." + name + "." + home + ".X");
        double y = Sethome.fileConfig.getDouble("Home." + name + "." + home + ".Y");
        double z = Sethome.fileConfig.getDouble("Home." + name + "." + home + ".Z");
        float yaw = (float) Sethome.fileConfig.getDouble("Home." + name + "." + home + ".Yaw");
        float pitch = (float) Sethome.fileConfig.getDouble("Home." + name + "." + home + ".Pitch"); // Ensure pitch is retrieved

        return new Location(world, x, y, z, yaw, pitch);
    }

    // Overload for player's own homes
    public static Location getLocation(Player player, String home) {
        return getLocation((OfflinePlayer) player, home);
    }


    /**
     * Saves the player's current location to the Back command's history.
     *
     * @param player The player whose location is to be saved.
     */
    public static void homeSave(Player player) {
        if (ServerEssentials.plugin.getConfig().getBoolean("home-save") || player.hasPermission("se.back.bypass")) {
            Back.location.put(player.getUniqueId(), player.getLocation());
        }
    }

    /**
     * Teleports the player to a specified location and sends a corresponding message.
     * Also handles cleanup of pending teleport tasks and movement cancellation flags.
     *
     * @param player     The player to teleport.
     * @param loc        The target Location.
     * @param langKey    The language file key for the chat message (e.g., "home-message").
     * @param subtitle   If true, sends a title message; otherwise, a chat message.
     * @param homeName   The name of the home for message placeholders.
     */
    public static void homeTeleport(Player player, Location loc, String langKey, Boolean subtitle, String homeName){
        if (!loc.isWorldLoaded()) {
            String msg = ChatColor.translateAlternateColorCodes('&', hex(Lang.fileConfig.getString("home-world-invalid")));
            player.sendMessage(msg);
            return;
        }

        player.teleport(loc);

        if (subtitle){
            String msg = Lang.fileConfig.getString("home-subtitle").replace("<home>", homeName);
            player.sendTitle(hex(msg), null);
        } else {
            String msg = Lang.fileConfig.getString(langKey).replace("<home>", homeName);
            player.sendMessage(hex(msg));
        }

        cancel.remove(player.getUniqueId());
        hometeleport.remove(player.getUniqueId());
    }

    // This method was private in your original Home class but public in the PlayerClickEvent.
    // Making it private again as it should primarily be an internal helper,
    // and PlayerClickEvent already uses hex() directly.
    /*
    private void sendErrorMessage(Player player, String key) {
        String msg = Lang.fileConfig.getString(key);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
    }
    */
}