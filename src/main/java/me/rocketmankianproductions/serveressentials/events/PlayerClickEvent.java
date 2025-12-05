package me.rocketmankianproductions.serveressentials.events;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.commands.*; // Assuming Home, Warp, ListHomes, Sethome, Setwarp, Invsee, AFK are here
import me.rocketmankianproductions.serveressentials.file.Lang;
import me.rocketmankianproductions.serveressentials.utils.AFKManager;
import me.rocketmankianproductions.serveressentials.utils.CompatibilityUtil;
import me.rocketmankianproductions.serveressentials.utils.GUIPaginationHelper; // Import the pagination helper
import org.bukkit.*;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.List; // Import List
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap; // Use ConcurrentHashMap for thread safety if accessed from other threads

import static me.rocketmankianproductions.serveressentials.ServerEssentials.hex;

public class PlayerClickEvent implements Listener {

    // --- Pagination related maps ---
    // These maps store the current page a player is viewing for a specific GUI type.
    public static final Map<UUID, Integer> playerWarpPages = new ConcurrentHashMap<>();
    public static final Map<UUID, Integer> playerHomePages = new ConcurrentHashMap<>();
    public static final Map<UUID, Integer> playerTargetHomePages = new ConcurrentHashMap<>(); // For other players' homes

    // Define how many items can be displayed per page (excluding pagination buttons)
    // For a 27-slot inventory (3 rows), with 2 slots for previous/next buttons.
    public static final int WARP_ITEMS_PER_PAGE = ServerEssentials.plugin.getConfig().getInt("warp-gui-size") - 2; // 27 - 2
    public static final int HOME_ITEMS_PER_PAGE = ServerEssentials.plugin.getConfig().getInt("home-gui-size") - 2; // 27 - 2

    // Map to store pending home/warp deletions per player to avoid race conditions.
    // Key: Player UUID, Value: String (the home/warp name to be deleted)
    private static final Map<UUID, String> pendingDeletions = new ConcurrentHashMap<>();

    // Map to store the type of deletion (home or warp) for confirmation GUIs
    private static final Map<UUID, String> pendingDeletionType = new ConcurrentHashMap<>();


    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        String inventoryTitle = CompatibilityUtil.getTitle(event);

        // Cancel clicks in specific GUIs where interaction is not allowed
        if (inventoryTitle.equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', Lang.fileConfig.getString("invsee-armor-gui"))) ||
                (Invsee.targetName.containsKey(player) && inventoryTitle.equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', "&b&l" + Invsee.targetName.get(player) + "'s Inventory")))) {
            event.setCancelled(true);
            return; // No further processing needed for these GUIs
        }

        ItemStack clickedItem = event.getCurrentItem();
        // If clicked item is null or air, cancel event and return for non-confirmation GUIs
        if (clickedItem == null || clickedItem.getType() == Material.AIR) {
            // Only cancel if it's not a confirmation GUI, which might have empty slots
            if (isConfirmationGUI(inventoryTitle)) {
                event.setCancelled(true);
            }
            return;
        }

        // --- Handle different GUI types ---
        if (inventoryTitle.equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', Lang.fileConfig.getString("warp-gui-name")))) {
            handleWarpClick(player, clickedItem, event);
        } else if (inventoryTitle.equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', Lang.fileConfig.getString("home-gui-name")))) {
            handleHomeClick(player, clickedItem, event);
        } else if (ListHomes.target != null && inventoryTitle.equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', Lang.fileConfig.getString("target-home-gui-name").replace("<target>", ListHomes.target.getName())))) {
            handleTargetHomeClick(player, clickedItem, event);
        }
        // --- Handle Confirmation GUIs ---
        else if (isConfirmationGUI(inventoryTitle)) {
            handleConfirmDeletion(player, inventoryTitle, clickedItem, event);
        }

        // AFK Command: This part of the code seems to be a general click handler
        // rather than specifically tied to GUI interaction. If a player clicks anywhere
        // while AFK, it sets them as active. Consider moving this to a more general
        // PlayerInteractEvent or PlayerMoveEvent for better logic separation if desired.
        if (AFKManager.isAFK(player)){
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.fileConfig.getString("afk-inactive")));
            player.setSleepingIgnored(false); // Assuming this is for AFK status
            AFKManager.setAFK(player, false);
        }
    }

    /**
     * Checks if the given inventory title corresponds to any of the confirmation GUIs.
     * @param title The title of the inventory.
     * @return true if it's a confirmation GUI, false otherwise.
     */
    private boolean isConfirmationGUI(String title) {
        // Use regex for robust matching of dynamic titles
        String homeDeleteTitlePattern = ChatColor.translateAlternateColorCodes('&', Lang.fileConfig.getString("delete-home-gui-name")).replace("<home>", ".*");
        String targetHomeDeleteTitlePattern = ChatColor.translateAlternateColorCodes('&', Lang.fileConfig.getString("target-delete-home-gui-name")).replace("<target>", ".*").replace("<home>", ".*");
        String warpDeleteTitlePattern = ChatColor.translateAlternateColorCodes('&', Lang.fileConfig.getString("delete-warp-gui-name")).replace("<warp>", ".*");

        // Strip color codes from the inventory title before matching with regex
        String strippedTitle = ChatColor.stripColor(title);

        return strippedTitle.matches(ChatColor.stripColor(homeDeleteTitlePattern)) ||
                strippedTitle.matches(ChatColor.stripColor(targetHomeDeleteTitlePattern)) ||
                strippedTitle.matches(ChatColor.stripColor(warpDeleteTitlePattern));
    }


    /**
     * Handles clicks within the Warp GUI, including pagination.
     */
    private void handleWarpClick(Player player, ItemStack clickedItem, InventoryClickEvent event) {
        event.setCancelled(true);

        int currentPage = playerWarpPages.getOrDefault(player.getUniqueId(), 1); // Get current page, default to 1

        // Check for pagination buttons first
        int targetPage = GUIPaginationHelper.getNextPageFromButton(clickedItem);
        if (targetPage == -1) { // Not a next page button, check for previous
            targetPage = GUIPaginationHelper.getPreviousPageFromButton(clickedItem);
        }

        if (targetPage != -1) { // It's a pagination button
            playerWarpPages.put(player.getUniqueId(), targetPage); // Update current page
            Warp.openWarpGUI(player, targetPage);
            return;
        }

        // If not a pagination button, proceed with original warp item logic
        String warpName = ChatColor.stripColor(Objects.requireNonNull(clickedItem.getItemMeta()).getDisplayName());

        if (event.getClick() == ClickType.RIGHT) {
            showConfirmDenyGUI(player, "warp", warpName);
            pendingDeletions.put(player.getUniqueId(), warpName);
            pendingDeletionType.put(player.getUniqueId(), "warp");
        } else if (event.getClick() == ClickType.LEFT) {
            if (player.hasPermission("se.warps.all") || ServerEssentials.permissionChecker(player, "se.warps." + warpName)) {
                Location loc = getWarpLocation(warpName, player);
                if (loc == null || !loc.isWorldLoaded()) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(Lang.fileConfig.getString("warp-world-invalid"))));
                    player.closeInventory();
                    return;
                }
                // Using the overloaded handleTeleportation method (without target)
                PlayerClickEvent.handleTeleportation(player, loc, "warp-message", "warp-subtitle", warpName,
                        ServerEssentials.plugin.getConfig().getInt("warp-teleport"),
                        ServerEssentials.plugin.getConfig().getBoolean("warp-movement-cancel"),
                        Warp.cancel, Warp.warpteleport);
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(Lang.fileConfig.getString("no-permission"))));
            }
            player.closeInventory();
        }
    }

    /**
     * Handles clicks within the Home GUI, including pagination.
     */
    private void handleHomeClick(Player player, ItemStack clickedItem, InventoryClickEvent event) {
        event.setCancelled(true);

        int currentPage = playerHomePages.getOrDefault(player.getUniqueId(), 1); // Get current page, default to 1

        // Check for pagination buttons first
        int targetPage = GUIPaginationHelper.getNextPageFromButton(clickedItem);
        if (targetPage == -1) {
            targetPage = GUIPaginationHelper.getPreviousPageFromButton(clickedItem);
        }

        if (targetPage != -1) { // It's a pagination button
            playerHomePages.put(player.getUniqueId(), targetPage); // Update current page
            Home.openHomeGUI(player, targetPage);
            player.closeInventory();
            return;
        }

        // If not a pagination button, proceed with original home item logic
        String homeName = ChatColor.stripColor(Objects.requireNonNull(clickedItem.getItemMeta()).getDisplayName());

        if (event.getClick() == ClickType.RIGHT) {
            showConfirmDenyGUI(player, "home", homeName);
            pendingDeletions.put(player.getUniqueId(), homeName);
            pendingDeletionType.put(player.getUniqueId(), "home");
        } else if (event.getClick() == ClickType.LEFT) {
            Location loc = getHomeLocation(homeName, player);
            if (loc == null) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(Lang.fileConfig.getString("home-invalid").replace("<home>", homeName))));
                player.closeInventory();
                return;
            }
            // Using the overloaded handleTeleportation method (without target)
            PlayerClickEvent.handleTeleportation(player, loc, "home-message", "enable-home-subtitle", homeName,
                    ServerEssentials.plugin.getConfig().getInt("home-teleport"),
                    ServerEssentials.plugin.getConfig().getBoolean("home-movement-cancel"),
                    Home.cancel, Home.hometeleport);
            player.closeInventory();
        }
    }

    /**
     * Handles clicks within the Target Home GUI, including pagination.
     */
    private void handleTargetHomeClick(Player player, ItemStack clickedItem, InventoryClickEvent event) {
        event.setCancelled(true);

        int currentPage = playerTargetHomePages.getOrDefault(player.getUniqueId(), 1); // Get current page, default to 1

        // Check for pagination buttons first
        int targetPage = GUIPaginationHelper.getNextPageFromButton(clickedItem);
        if (targetPage == -1) {
            targetPage = GUIPaginationHelper.getPreviousPageFromButton(clickedItem);
        }

        if (targetPage != -1) { // It's a pagination button
            playerTargetHomePages.put(player.getUniqueId(), targetPage); // Update current page
            // IMPORTANT: Call your ListHomes command's openTargetHomeGUI method here
            ListHomes.openTargetHomeGUI(player, targetPage, ListHomes.target); // UNCOMMENTED
            player.closeInventory();
            return;
        }

        // If not a pagination button, proceed with original item logic
        String homeName = ChatColor.stripColor(Objects.requireNonNull(clickedItem.getItemMeta()).getDisplayName());
        OfflinePlayer targetPlayer = ListHomes.target;

        if (event.getClick() == ClickType.RIGHT) {
            String deleteHomeGuiName = Lang.fileConfig.getString("target-delete-home-gui-name")
                    .replace("<target>", targetPlayer.getName())
                    .replace("<home>", homeName);
            showConfirmDenyGUI(player, "targethome", homeName, deleteHomeGuiName);
            pendingDeletions.put(player.getUniqueId(), homeName);
            pendingDeletionType.put(player.getUniqueId(), "targethome");
        } else if (event.getClick() == ClickType.LEFT) {
            Location loc = getHomeLocation(homeName, targetPlayer);
            if (loc == null) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(Lang.fileConfig.getString("home-invalid").replace("<home>", homeName))));
                player.closeInventory();
                return;
            }
            PlayerClickEvent.handleTeleportation(player, loc, "home-message", "enable-home-subtitle", homeName,
                    ServerEssentials.plugin.getConfig().getInt("home-teleport"),
                    ServerEssentials.plugin.getConfig().getBoolean("home-movement-cancel"),
                    Home.cancel, Home.hometeleport, targetPlayer);
            player.closeInventory();
        }
    }

    /**
     * Handles clicks within confirmation (delete) GUIs.
     */
    private void handleConfirmDeletion(Player player, String inventoryTitle, ItemStack clickedItem, InventoryClickEvent event) {
        event.setCancelled(true);

        if (clickedItem.getType() == Material.GRAY_STAINED_GLASS_PANE) {
            return; // Ignore clicks on filler panes
        }

        String deletionTarget = pendingDeletions.get(player.getUniqueId());
        String deletionType = pendingDeletionType.get(player.getUniqueId());

        if (deletionTarget == null || deletionType == null) {
            player.closeInventory(); // Close if no pending deletion data is found
            return;
        }

        if (clickedItem.getType() == Material.LIME_STAINED_GLASS_PANE) { // Confirm
            boolean success = false;
            String successMessageKey = "";
            boolean hasPermission = false;

            if (deletionType.equals("home")) {
                hasPermission = ServerEssentials.permissionChecker(player, "se.deletehome");
                if (hasPermission) {
                    Sethome.fileConfig.set("Home." + player.getUniqueId() + "." + deletionTarget, null);
                    successMessageKey = "home-deletion-success";
                    success = true;
                }
            } else if (deletionType.equals("warp")) {
                hasPermission = ServerEssentials.permissionChecker(player, "se.deletewarp");
                if (hasPermission) {
                    Setwarp.fileConfig.set("Warp." + deletionTarget, null);
                    successMessageKey = "warp-deletion-success";
                    success = true;
                }
            } else if (deletionType.equals("targethome")) {
                hasPermission = ServerEssentials.permissionChecker(player, "se.deletehome.others");
                if (hasPermission) {
                    OfflinePlayer target = ListHomes.target;
                    if (target != null) {
                        Sethome.fileConfig.set("Home." + target.getUniqueId() + "." + deletionTarget, null);
                        successMessageKey = "target-home-deletion-success";
                        // Replace <target> in message
                        String msg = Lang.fileConfig.getString(successMessageKey).replace("<target>", target.getName());
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                        success = true;
                    }
                }
            }

            if (success) {
                try {
                    if (deletionType.equals("home") || deletionType.equals("targethome")) {
                        Sethome.fileConfig.save(Sethome.file);
                    } else if (deletionType.equals("warp")) {
                        Setwarp.fileConfig.save(Setwarp.file);
                    }
                    if (!deletionType.equals("targethome")) { // Target home success message handled inline
                        String msg = Lang.fileConfig.getString(successMessageKey).replace("<" + deletionType + ">", deletionTarget);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                    }
                } catch (IOException i) {
                    i.printStackTrace(); // Log the error
                    player.sendMessage(ChatColor.RED + "Error: Could not save data. See console for details.");
                }
            } else if (!hasPermission) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(Lang.fileConfig.getString("no-permission"))));
            }
            player.closeInventory();
            pendingDeletions.remove(player.getUniqueId()); // Clean up pending deletion
            pendingDeletionType.remove(player.getUniqueId());

        } else if (clickedItem.getType() == Material.RED_STAINED_GLASS_PANE) { // Deny
            player.closeInventory();
            pendingDeletions.remove(player.getUniqueId()); // Clean up pending deletion
            pendingDeletionType.remove(player.getUniqueId());
        }
    }

    /**
     * Overloaded method for handleTeleportation when no specific target player is involved (e.g., for self-teleport).
     * Delegates to the main handleTeleportation method with a null target.
     */
    public static void handleTeleportation(Player player, Location location, String messageKey,
                                           String subtitleEnableKey, String name, int teleportDelaySeconds,
                                           boolean movementCancel, List<UUID> cancelList, Map<UUID, Integer> teleportTaskMap) {
        handleTeleportation(player, location, messageKey, subtitleEnableKey, name,
                teleportDelaySeconds, movementCancel, cancelList, teleportTaskMap, null);
    }


    /**
     * Handles the teleportation logic, including instant and delayed teleports.
     *
     * @param player             The player to teleport.
     * @param location           The target location.
     * @param messageKey         The language file key for the success message.
     * @param subtitleEnableKey  The language file key for enabling subtitle.
     * @param name               The name of the home/warp (for message placeholders).
     * @param teleportDelaySeconds The delay in seconds before teleporting.
     * @param movementCancel     Whether movement cancels the teleport.
     * @param cancelList         List for tracking movement cancellation (e.g., Home.cancel, Warp.cancel).
     * A player's UUID being present in this list indicates movement cancellation is active.
     * @param teleportTaskMap    Map for tracking scheduled teleport tasks (e.g., Home.hometeleport, Warp.warpteleport).
     * @param target             Optional: The target player for target homes. Can be null.
     */
    public static void handleTeleportation(Player player, Location location, String messageKey,
                                           String subtitleEnableKey, String name, int teleportDelaySeconds,
                                           boolean movementCancel, List<UUID> cancelList, Map<UUID, Integer> teleportTaskMap,
                                           OfflinePlayer target) {

        // Save player's current location (e.g., for /back command)
        // Home.homeSave and Warp.warpSave should probably be merged or called conditionally
        // based on the context (home or warp). For now, keeping as is.
        Home.homeSave(player);
        Warp.warpSave(player);


        // Instant teleport
        if (teleportDelaySeconds == 0 || player.hasPermission("se.home.bypass") || player.hasPermission("se.warp.bypass")) {
            player.teleport(location);
            sendTeleportMessage(player, messageKey, subtitleEnableKey, name, target);
            return;
        }

        // Delayed teleport
        String waitMessageKey = "";
        if (messageKey.contains("home")) { // Determine correct wait message based on context
            waitMessageKey = (target != null) ? "target-home-wait-message" : "home-wait-message";
        } else if (messageKey.contains("warp")) {
            waitMessageKey = "warp-wait-message";
        }

        String msg = Lang.fileConfig.getString(waitMessageKey)
                .replace("<home>", name)
                .replace("<warp>", name)
                .replace("<time>", String.valueOf(teleportDelaySeconds));
        if (target != null) {
            msg = msg.replace("<target>", target.getName());
        }
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));

        if (movementCancel) {
            // Add player's UUID to the list to indicate movement cancellation is active
            if (!cancelList.contains(player.getUniqueId())) {
                cancelList.add(player.getUniqueId());
            }
        }

        // Cancel any existing teleport task for this player
        if (teleportTaskMap.containsKey(player.getUniqueId()) && teleportTaskMap.get(player.getUniqueId()) != null) {
            Bukkit.getScheduler().cancelTask(teleportTaskMap.get(player.getUniqueId()));
        }

        int taskId = new BukkitRunnable() {
            @Override
            public void run() {
                // Check if the player moved and was removed from the list.
                // If movementCancel is true and the player is no longer in the cancelList,
                // it means they moved and the teleport should be cancelled.
                if (movementCancel && cancelList.contains(player.getUniqueId())) { // This checks if player is still in the list when task runs
                    // If still in the list, means they did not move (or movement not detected by the cancelling event)
                    // So, proceed with teleport and then remove.
                } else if (movementCancel && !cancelList.contains(player.getUniqueId())) {
                    // Player moved and was removed from the list, so the teleport should be cancelled.
                    teleportTaskMap.remove(player.getUniqueId()); // Clean up task ID
                    return; // Stop execution if movement cancelled
                }

                player.teleport(location);
                sendTeleportMessage(player, messageKey, subtitleEnableKey, name, target);
                cancelList.remove(player.getUniqueId()); // Clean up cancellation flag
                teleportTaskMap.remove(player.getUniqueId()); // Clean up task ID
            }
        }.runTaskLater(ServerEssentials.plugin, teleportDelaySeconds * 20L).getTaskId(); // 20 ticks per second

        teleportTaskMap.put(player.getUniqueId(), taskId);
    }

    /**
     * Sends the appropriate teleport message (title or chat) to the player.
     */
    private static void sendTeleportMessage(Player player, String messageKey, String subtitleEnableKey, String name, OfflinePlayer target) {
        boolean useSubtitle = ServerEssentials.plugin.getConfig().getBoolean(subtitleEnableKey);
        String messagePath = useSubtitle ? (messageKey.replace("-message", "-subtitle")) : messageKey;
        String msg = Lang.fileConfig.getString(messagePath)
                .replace("<home>", name)
                .replace("<warp>", name);

        if (target != null) {
            msg = msg.replace("<target>", target.getName());
        }

        if (useSubtitle) {
            player.sendTitle(ChatColor.translateAlternateColorCodes('&', hex(msg)), null);
        } else {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
        }
    }


    /**
     * Retrieves the Location for a given warp.
     * @param warp The name of the warp.
     * @param player The player (for context, though not directly used for warp data).
     * @return The Location of the warp, or null if invalid.
     */
    public static Location getWarpLocation(String warp, OfflinePlayer player) { // Player param is unused but kept to match original signature
        String worldName = Setwarp.fileConfig.getString("Warp." + warp + ".World");
        if (worldName == null) {
            return null;
        }
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            return null;
        }

        double x = Setwarp.fileConfig.getDouble("Warp." + warp + ".X");
        double y = Setwarp.fileConfig.getDouble("Warp." + warp + ".Y");
        double z = Setwarp.fileConfig.getDouble("Warp." + warp + ".Z");
        float yaw = (float) Setwarp.fileConfig.getDouble("Warp." + warp + ".Yaw");
        float pitch = (float) Setwarp.fileConfig.getDouble("Warp." + warp + ".Pitch");

        return new Location(world, x, y, z, yaw, pitch);
    }

    /**
     * Retrieves the Location for a given home of a specific player.
     * @param home The name of the home.
     * @param player The OfflinePlayer who owns the home.
     * @return The Location of the home, or null if invalid.
     */
    public static Location getHomeLocation(String home, OfflinePlayer player) {
        UUID name = player.getUniqueId();
        String worldName = Sethome.fileConfig.getString("Home." + name + "." + home + ".World");
        if (worldName == null) {
            return null;
        }
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            return null;
        }
        double x = Sethome.fileConfig.getDouble("Home." + name + "." + home + ".X");
        double y = Sethome.fileConfig.getDouble("Home." + name + "." + home + ".Y");
        double z = Sethome.fileConfig.getDouble("Home." + name + "." + home + ".Z");
        float yaw = (float) Sethome.fileConfig.getDouble("Home." + name + "." + home + ".Yaw");
        float pitch = (float) Sethome.fileConfig.getDouble("Home." + name + "." + home + ".Pitch");

        return new Location(world, x, y, z, yaw, pitch);
    }

    /**
     * Displays a generic confirmation/denial GUI to the player.
     * Overloaded to allow custom titles for target home deletions.
     *
     * @param player The player to show the GUI to.
     * @param type   The type of action ("home", "warp", "targethome").
     * @param value  The home/warp name.
     * @param customTitle Optional custom title for the GUI.
     */
    public static void showConfirmDenyGUI(Player player, String type, String value, String customTitle) {
        String guiTitle = null;
        if (customTitle != null) {
            guiTitle = customTitle;
        } else if (type.equalsIgnoreCase("home")) {
            guiTitle = Lang.fileConfig.getString("delete-home-gui-name").replace("<home>", value);
        } else if (type.equalsIgnoreCase("warp")) {
            guiTitle = Lang.fileConfig.getString("delete-warp-gui-name").replace("<warp>", value);
        }

        if (guiTitle == null) {
            player.sendMessage(ChatColor.RED + "Error: Could not generate GUI title.");
            return;
        }

        Inventory confirm = Bukkit.createInventory(player, 27, ChatColor.translateAlternateColorCodes('&', guiTitle));

        ItemStack confirmItem = createConfirmationGUIItem(Material.LIME_STAINED_GLASS_PANE, Lang.fileConfig.getString("gui-confirm-name"));
        ItemStack cancelItem = createConfirmationGUIItem(Material.RED_STAINED_GLASS_PANE, Lang.fileConfig.getString("gui-deny-name"));
        ItemStack idleItem = createConfirmationGUIItem(Material.GRAY_STAINED_GLASS_PANE, ChatColor.DARK_GRAY + "");

        // Fill inventory with idle items
        for (int counter = 0; counter < confirm.getSize(); counter++) {
            confirm.setItem(counter, idleItem);
        }

        confirm.setItem(11, confirmItem); // Yes button
        confirm.setItem(15, cancelItem);  // No button

        player.openInventory(confirm);
    }

    /**
     * Overload for showConfirmDenyGUI without a custom title.
     */
    public static void showConfirmDenyGUI(Player player, String type, String value) {
        showConfirmDenyGUI(player, type, value, null);
    }

    /**
     * Helper to create the stained glass pane items for the confirmation GUI.
     */
    private static ItemStack createConfirmationGUIItem(Material material, String displayNameKey) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayNameKey));
            item.setItemMeta(meta);
        }
        return item;
    }
}