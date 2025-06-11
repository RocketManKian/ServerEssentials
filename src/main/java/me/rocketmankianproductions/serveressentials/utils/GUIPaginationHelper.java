package me.rocketmankianproductions.serveressentials.utils;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin; // Used for NamespacedKey, essential for custom item data

import java.util.ArrayList;
import java.util.List;
import java.util.Objects; // For Objects.requireNonNull

public class GUIPaginationHelper {

    // IMPORTANT: This plugin instance MUST be set by your main plugin class.
    // Example: In your main plugin's onEnable() method, add:
    // GUIPaginationHelper.setPlugin(this);
    private static JavaPlugin plugin;

    /**
     * Sets the plugin instance. This is crucial for creating NamespacedKeys,
     * which are used to store custom data on your pagination buttons (like the target page).
     * You should call this method once in your main plugin class's onEnable() method.
     *
     * @param p Your plugin's instance (e.g., 'this' from your main plugin class).
     */
    public static void setPlugin(JavaPlugin p) {
        plugin = Objects.requireNonNull(p, "Plugin instance cannot be null for GUIPaginationHelper.");
    }

    /**
     * Updates a Minecraft GUI inventory with pagination buttons (previous and next).
     * This method dynamically populates the inventory with provided content items
     * and places the navigation buttons in designated slots.
     *
     * Assumptions:
     * - The last slot ({@code inventorySize - 1}) is reserved for the "Next Page" button.
     * - The second-to-last slot ({@code inventorySize - 2}) is reserved for the "Previous Page" button.
     * - "Full" in the context of this helper means that if there are more pages ({@code totalPages > 1}),
     * then the GUI is effectively "full enough" to require pagination, and the buttons
     * will occupy their reserved slots, allowing access to overflowing items.
     *
     * @param inventory          The Minecraft Inventory object representing the GUI.
     * @param currentPage        The current page number (1-indexed).
     * @param totalPages         The total number of available pages.
     * @param itemsOnCurrentPage A list of all ItemStack objects that *should* be displayed on
     * the current page. This list should NOT include the pagination buttons themselves.
     */
    public static void updatePaginationButtons(Inventory inventory, int currentPage, int totalPages, List<ItemStack> itemsOnCurrentPage) {
        if (plugin == null) {
            System.err.println("GUIPaginationHelper plugin instance is not set! Call GUIPaginationHelper.setPlugin(this) in your main plugin class's onEnable().");
            return;
        }

        int inventorySize = inventory.getSize();
        int nextPageButtonSlot = inventorySize - 1;
        int previousPageButtonSlot = inventorySize - 2;

        // Clear the entire inventory first to ensure a clean slate before populating
        inventory.clear();

        // Calculate the number of slots available for actual content items,
        // excluding the two slots reserved for pagination buttons.
        int availableContentSlots = inventorySize - 2;
        if (availableContentSlots < 0) {
            System.err.println("Warning: Inventory size " + inventorySize + " is too small to place pagination buttons. Minimum size 2 required.");
            return; // Cannot place buttons if inventory is too small
        }

        // Populate the inventory with the content items for the current page.
        // We only place items up to the calculated availableContentSlots.
        for (int i = 0; i < itemsOnCurrentPage.size() && i < availableContentSlots; i++) {
            inventory.setItem(i, itemsOnCurrentPage.get(i));
        }

        // Place the "Next Page" button if there are more pages to navigate to.
        // It always goes in the last slot (inventorySize - 1).
        if (currentPage < totalPages) {
            inventory.setItem(nextPageButtonSlot, createNextPageButton(currentPage));
        }

        // Place the "Previous Page" button if the current page is not the first page.
        // It always goes in the second-to-last slot (inventorySize - 2), provided the slot exists.
        if (currentPage > 1) {
            if (previousPageButtonSlot >= 0) { // Ensure the slot is valid (e.g., for very small inventories)
                inventory.setItem(previousPageButtonSlot, createPreviousPageButton(currentPage));
            } else {
                System.err.println("Warning: Inventory size " + inventorySize + " is too small to place a previous page button at slot " + previousPageButtonSlot + ".");
            }
        }
    }

    /**
     * Creates an ItemStack representing the "Next Page" button.
     * This button uses an arrow icon, a display name, lore, and a
     * Persistent Data Tag to store the target page number for easy retrieval
     * when a player interacts with it.
     *
     * @param currentPage The current page number. The button will link to (currentPage + 1).
     * @return An ItemStack configured as the "Next Page" button.
     */
    public static ItemStack createNextPageButton(int currentPage) {
        ItemStack item = new ItemStack(Material.ARROW); // Common icon for navigation
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            // Display name with color codes for better visibility
            meta.setDisplayName("§aNext Page (§7Page " + (currentPage + 1) + "§a)");

            // Add lore for player guidance
            List<String> lore = new ArrayList<>();
            lore.add("§7Click to go to the next page.");
            meta.setLore(lore);

            // Store the target page number using a Persistent Data Container (PDC).
            // This is the recommended way to attach custom, persistent data to items.
            NamespacedKey key = new NamespacedKey(plugin, "gui_next_page");
            meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, currentPage + 1);

            item.setItemMeta(meta);
        }
        return item;
    }

    /**
     * Creates an ItemStack representing the "Previous Page" button.
     * Similar to the next page button, it uses an arrow icon, display name,
     * lore, and a Persistent Data Tag for the target page.
     *
     * @param currentPage The current page number. The button will link to (currentPage - 1).
     * @return An ItemStack configured as the "Previous Page" button.
     */
    public static ItemStack createPreviousPageButton(int currentPage) {
        ItemStack item = new ItemStack(Material.ARROW); // Common icon for navigation
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            // Display name with color codes
            meta.setDisplayName("§cPrevious Page (§7Page " + (currentPage - 1) + "§c)");

            // Add lore for player guidance
            List<String> lore = new ArrayList<>();
            lore.add("§7Click to go to the previous page.");
            meta.setLore(lore);

            // Store the target page number using PDC
            NamespacedKey key = new NamespacedKey(plugin, "gui_previous_page");
            meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, currentPage - 1);

            item.setItemMeta(meta);
        }
        return item;
    }

    // Helper methods to read the page number from a button (useful in InventoryClickEvent)
    /**
     * Retrieves the target page number from a "Next Page" button.
     * @param item The ItemStack to check.
     * @return The target page number, or -1 if the item is not a valid "Next Page" button.
     */
    public static int getNextPageFromButton(ItemStack item) {
        if (plugin == null || item == null || !item.hasItemMeta()) {
            return -1;
        }
        ItemMeta meta = item.getItemMeta();
        NamespacedKey key = new NamespacedKey(plugin, "gui_next_page");
        if (meta.getPersistentDataContainer().has(key, PersistentDataType.INTEGER)) {
            return meta.getPersistentDataContainer().get(key, PersistentDataType.INTEGER);
        }
        return -1;
    }

    /**
     * Retrieves the target page number from a "Previous Page" button.
     * @param item The ItemStack to check.
     * @return The target page number, or -1 if the item is not a valid "Previous Page" button.
     */
    public static int getPreviousPageFromButton(ItemStack item) {
        if (plugin == null || item == null || !item.hasItemMeta()) {
            return -1;
        }
        ItemMeta meta = item.getItemMeta();
        NamespacedKey key = new NamespacedKey(plugin, "gui_previous_page");
        if (meta.getPersistentDataContainer().has(key, PersistentDataType.INTEGER)) {
            return meta.getPersistentDataContainer().get(key, PersistentDataType.INTEGER);
        }
        return -1;
    }
}