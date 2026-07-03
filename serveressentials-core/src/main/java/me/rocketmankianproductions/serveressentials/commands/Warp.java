package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.events.PlayerClickEvent; // Import PlayerClickEvent to access pagination maps and constants
import me.rocketmankianproductions.serveressentials.file.Lang;
import me.rocketmankianproductions.serveressentials.utils.GUIPaginationHelper; // Import the pagination helper
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.*;
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

public class Warp implements CommandExecutor {

    public static HashMap<UUID, Integer> warpteleport = new HashMap<>();
    public static ArrayList<UUID> cancel = new ArrayList<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            String console = Lang.fileConfig.getString("console-invalid");
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', hex(console)));
            return true;
        }

        Player player = (Player) sender;
        if (!ServerEssentials.permissionChecker(player, "se.warp")) {
            return false;
        }

        if (args.length == 1) {
            String warpName = args[0];
            if (!Setwarp.file.exists() || Setwarp.fileConfig.getString("Warp." + warpName + ".World") == null) {
                String msg = Lang.fileConfig.getString("warp-not-found").replace("<warp>", warpName);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                return false;
            }

            if (!player.hasPermission("se.warps.all") && !ServerEssentials.permissionChecker(player, "se.warps." + warpName)) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(Lang.fileConfig.getString("no-permission"))));
                return false;
            }

            Location loc = getLocation(args);
            if (loc == null || !loc.isWorldLoaded()) {
                String msg = Lang.fileConfig.getString("warp-world-invalid");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                return false;
            }

            int delay = ServerEssentials.plugin.getConfig().getInt("warp-teleport");
            boolean movementCancel = ServerEssentials.plugin.getConfig().getBoolean("warp-movement-cancel");

            if (delay == 0 || player.hasPermission("se.warp.bypass")) {
                warpSave(player); // Save player's location for /back
                warpTeleport(player, loc, "warp-message", ServerEssentials.plugin.getConfig().getBoolean("enable-warp-subtitle"), warpName);
            } else {
                String msg = Lang.fileConfig.getString("warp-wait-message").replace("<warp>", warpName).replace("<time>", String.valueOf(delay));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                int finalDelay = delay * 20;

                if (warpteleport.containsKey(player.getUniqueId()) && warpteleport.get(player.getUniqueId()) != null) {
                    Bukkit.getScheduler().cancelTask(warpteleport.get(player.getUniqueId()));
                }

                if (movementCancel) {
                    cancel.add(player.getUniqueId());
                }

                warpteleport.put(player.getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(ServerEssentials.plugin, () -> {
                    if (movementCancel && !cancel.contains(player.getUniqueId())) {
                        warpteleport.remove(player.getUniqueId());
                        return;
                    }
                    warpSave(player);
                    warpTeleport(player, loc, "warp-message", ServerEssentials.plugin.getConfig().getBoolean("enable-warp-subtitle"), warpName);
                    cancel.remove(player.getUniqueId());
                }, finalDelay));
            }
            return true;

        } else if (args.length == 0) {
            if (ServerEssentials.plugin.getConfig().getBoolean("enable-warp-gui")) {
                openWarpGUI(player, 1); // Open the first page of the warp GUI
                return true;
            } else {
                ConfigurationSection warpSection = Setwarp.fileConfig.getConfigurationSection("Warp");
                if (warpSection == null || warpSection.getKeys(false).isEmpty()) {
                    String msg = Lang.fileConfig.getString("no-warps-set");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                    return false;
                }

                player.sendMessage(ChatColor.GREEN + "---------------------------"
                        + "\nWarp(s) List"
                        + "\n---------------------------");
                for (String key : warpSection.getKeys(false)) {
                    player.sendMessage(ChatColor.GOLD + key);
                }
                return true;
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("setblock")) {
                if (!ServerEssentials.permissionChecker(player, "se.setwarp.block")) {
                    return false;
                }

                String warpName = args[1];
                if (!Setwarp.fileConfig.contains("Warp." + warpName)) {
                    String msg = Lang.fileConfig.getString("warp-not-found").replace("<warp>", warpName);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                    return false;
                }

                Material material = player.getInventory().getItemInMainHand().getType();
                if (material == Material.AIR) {
                    String perm = Lang.fileConfig.getString("warp-block-invalid").replace("<item>", "AIR");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(perm)));
                    return false;
                } else {
                    Setwarp.fileConfig.set("Warp." + warpName + ".Block", material.toString());
                    try {
                        Setwarp.fileConfig.save(Setwarp.file);
                    } catch (IOException e) {
                        e.printStackTrace();
                        player.sendMessage(ChatColor.RED + "Error: Could not save warp block. Check console.");
                    }
                    Setwarp.reload();
                    String blockMsg = Lang.fileConfig.getString("warp-set-block-successful").replace("<warp>", warpName).replace("<block>", material.toString());
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(blockMsg)));
                    return true;
                }
            } else {
                String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/warp setblock (warp)");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                return false;
            }
        } else {
            String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/warp (warp)");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
            return false;
        }
    }

    /**
     * Opens the paginated Warp GUI for the specified player and page.
     * This method retrieves all warps, processes them into ItemStacks,
     * extracts the items for the current page, and then uses GUIPaginationHelper
     * to display them along with navigation buttons.
     *
     * @param player The player to open the GUI for.
     * @param page   The page number to display (1-indexed).
     */
    public static void openWarpGUI(Player player, int page) {
        List<ItemStack> allWarpItems = new ArrayList<>();
        ConfigurationSection warpSection = Setwarp.fileConfig.getConfigurationSection("Warp");

        if (warpSection != null && !warpSection.getKeys(false).isEmpty()) {
            allWarpItems = warpSection.getKeys(false).stream()
                    .sorted(String.CASE_INSENSITIVE_ORDER)
                    .map(warpName -> {
                        String warpBlock = Setwarp.fileConfig.getString("Warp." + warpName + ".Block", "COMPASS");
                        Material material = Material.getMaterial(warpBlock);
                        if (material == null) {
                            material = Material.COMPASS;
                        }

                        ItemStack item = new ItemStack(material);
                        ItemMeta meta = item.getItemMeta();
                        if (meta != null) {
                            String warpColour = ServerEssentials.plugin.getConfig().getString("warp-name-colour", "&e");
                            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', warpColour + warpName));

                            List<String> loreList = new ArrayList<>();
                            String leftClickMsg = Lang.fileConfig.getString("warp-gui-left-click", "&7Left-Click to teleport to <warp>.")
                                    .replace("<warp>", warpName);
                            loreList.add(ChatColor.translateAlternateColorCodes('&', hex(leftClickMsg)));

                            if (player.hasPermission("se.deletewarp")) {
                                String rightClickMsg = Lang.fileConfig.getString("warp-gui-right-click", "&cRight-Click to delete.");
                                loreList.add(ChatColor.translateAlternateColorCodes('&', hex(rightClickMsg)));
                            }
                            meta.setLore(loreList);
                            item.setItemMeta(meta);
                        }
                        return item;
                    })
                    .collect(Collectors.toList());
        }

        int totalItems = allWarpItems.size();
        int totalPages = (int) Math.ceil((double) totalItems / PlayerClickEvent.WARP_ITEMS_PER_PAGE);
        if (totalPages == 0) totalPages = 1;

        if (page < 1) page = 1;
        if (page > totalPages) page = totalPages;

        int startIndex = (page - 1) * PlayerClickEvent.WARP_ITEMS_PER_PAGE;
        int endIndex = Math.min(startIndex + PlayerClickEvent.WARP_ITEMS_PER_PAGE, totalItems);
        List<ItemStack> itemsOnCurrentPage = allWarpItems.subList(startIndex, endIndex);

        String guiTitle = ChatColor.translateAlternateColorCodes('&', Lang.fileConfig.getString("warp-gui-name", "&b&lWarps"));
        int inventorySize = plugin.getConfig().getInt("warp-gui-size");

        Inventory gui = Bukkit.createInventory(null, inventorySize, guiTitle);

        GUIPaginationHelper.updatePaginationButtons(gui, page, totalPages, itemsOnCurrentPage);

        player.openInventory(gui);

        PlayerClickEvent.playerWarpPages.put(player.getUniqueId(), page);

        if (totalItems == 0) {
            String msg = Lang.fileConfig.getString("no-warps-set");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
        }
    }


    /**
     * Retrieves the Location for a given warp from the configuration.
     *
     * @param args An array containing the warp name at index 0.
     * @return The Location of the warp, or null if any part of the location data is invalid or missing.
     */
    public static Location getLocation(String[] args) {
        String warpName = args[0];
        String worldName = Setwarp.fileConfig.getString("Warp." + warpName + ".World");
        if (worldName == null) {
            return null;
        }
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            return null;
        }

        double x = Setwarp.fileConfig.getDouble("Warp." + warpName + ".X");
        double y = Setwarp.fileConfig.getDouble("Warp." + warpName + ".Y");
        double z = Setwarp.fileConfig.getDouble("Warp." + warpName + ".Z");
        float yaw = (float) Setwarp.fileConfig.getDouble("Warp." + warpName + ".Yaw");
        float pitch = (float) Setwarp.fileConfig.getDouble("Warp." + warpName + ".Pitch"); // Corrected to use Setwarp.fileConfig

        return new Location(world, x, y, z, yaw, pitch);
    }

    /**
     * Saves the player's current location to the Back command's history.
     *
     * @param player The player whose location is to be saved.
     */
    public static void warpSave(Player player) {
        if (ServerEssentials.plugin.getConfig().getBoolean("warp-save") || player.hasPermission("se.back.bypass")) {
            Back.location.put(player.getUniqueId(), player.getLocation());
        }
    }

    /**
     * Teleports the player to a specified location and sends a corresponding message.
     * Also handles cleanup of pending teleport tasks and movement cancellation flags.
     *
     * @param player   The player to teleport.
     * @param loc      The target Location.
     * @param langKey  The language file key for the chat message.
     * @param subtitle If true, sends a title message; otherwise, a chat message.
     * @param warpName The name of the warp for message placeholders.
     */
    public static void warpTeleport(Player player, Location loc, String langKey, Boolean subtitle, String warpName) {
        if (!loc.isWorldLoaded()) {
            String msg = Lang.fileConfig.getString("warp-world-invalid");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
            return;
        }

        player.teleport(loc);

        if (subtitle) {
            String msg = Lang.fileConfig.getString("warp-subtitle").replace("<warp>", warpName);
            player.sendTitle(ChatColor.translateAlternateColorCodes('&', hex(msg)), null);
        } else {
            String msg = Lang.fileConfig.getString(langKey).replace("<warp>", warpName);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
        }

        cancel.remove(player.getUniqueId());
        warpteleport.remove(player.getUniqueId());
    }
}
