package me.rocketmankianproductions.serveressentials.events;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.commands.Back;
import me.rocketmankianproductions.serveressentials.commands.Freeze;
import me.rocketmankianproductions.serveressentials.commands.Home;
import me.rocketmankianproductions.serveressentials.commands.Spawn;
import me.rocketmankianproductions.serveressentials.commands.TeleportRequest;
import me.rocketmankianproductions.serveressentials.commands.Warp;
import me.rocketmankianproductions.serveressentials.file.Lang;
import me.rocketmankianproductions.serveressentials.utils.AFKManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.UUID;

import static me.rocketmankianproductions.serveressentials.ServerEssentials.hex;

public class PlayerMoveListener implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        // Only proceed if the player has actually moved to a different block
        if (!hasPlayerMovedBlocks(event)) {
            return;
        }

        // Freeze Command
        if (Freeze.freeze.containsKey(player)) {
            player.teleport(event.getFrom());
            sendMessage(player, "freeze-message");
        }

        // AFK Command
        if (AFKManager.isAFK(player)){
            sendMessage(player, "afk-inactive");
            player.setSleepingIgnored(false); // Reset ignored sleeping state
            AFKManager.setAFK(player, false);
        }

        // Handle generic movement cancellations for various commands
        if (Home.cancel.contains(player.getUniqueId())){
            handleMovementCancellation(player, Home.cancel, "home-movement-cancel");
        }
        if (Warp.cancel.contains(player.getUniqueId())){
            handleMovementCancellation(player, Warp.cancel, "warp-movement-cancel");
        }
        if (Spawn.cancel.contains(player.getUniqueId())){
            handleMovementCancellation(player, Spawn.cancel, "spawn-movement-cancel");
        }
        if (Back.cancel.contains(player.getUniqueId())){
            handleMovementCancellation(player, Back.cancel, "back-movement-cancel");
        }

        // Special handling for TeleportRequest movement cancellation lists.
        // This event listener ONLY removes the player from the list.
        // The actual cancellation message is sent by the scheduled task in TeleportRequest.
        if (TeleportRequest.movementCancelTPA.contains(player.getUniqueId())) {
            TeleportRequest.movementCancelTPA.remove(player.getUniqueId());
        } else if (TeleportRequest.movementCancelTPAHere.contains(player.getUniqueId())) {
            TeleportRequest.movementCancelTPAHere.remove(player.getUniqueId());
        }
    }

    /**
     * Checks if the player has moved to a different block.
     *
     * @param event The PlayerMoveEvent.
     * @return true if the player moved to a different block, false otherwise.
     */
    private boolean hasPlayerMovedBlocks(PlayerMoveEvent event) {
        return event.getFrom().getBlockX() != event.getTo().getBlockX() ||
                event.getFrom().getBlockZ() != event.getTo().getBlockZ() ||
                event.getFrom().getBlockY() != event.getTo().getBlockY();
    }

    /**
     * Handles movement cancellation for various commands.
     * This method is only responsible for removing the player from the cancel list.
     * The actual message to the player is sent by the respective command's delayed task.
     *
     * @param player The player who moved.
     * @param cancelList The ArrayList of UUIDs to check for cancellation.
     * @param langPath The language file path for the cancellation message (not sent here, but as reference).
     */
    private void handleMovementCancellation(Player player, ArrayList<UUID> cancelList, String langPath) {
        if (cancelList.contains(player.getUniqueId())) {
            cancelList.remove(player.getUniqueId());
            // Message sending is handled by the command's scheduled task (e.g., Home/Warp/Spawn/Back classes)
        }
    }

    /**
     * Helper method to send messages from Lang.fileConfig, handling color codes and Hex.
     *
     * @param player The player to send the message to.
     * @param configPath The path to the string in Lang.fileConfig.
     * @param replacements Optional replacements for placeholders in the string (e.g., "<placeholder>", "value").
     */
    private static void sendMessage(Player player, String configPath, String... replacements) {
        // Always add a null check for the player before sending a message
        if (player == null || !player.isOnline()) {
            return; // Don't try to send a message to a null or offline player
        }

        String message = Lang.fileConfig.getString(configPath);
        if (message == null) {
            message = "Error: Message not found for path '" + configPath + "'";
            ServerEssentials.getPlugin().getLogger().warning(message);
            player.sendMessage(ChatColor.RED + message);
            return;
        }

        for (int i = 0; i < replacements.length; i += 2) {
            if (i + 1 < replacements.length) {
                message = message.replace(replacements[i], replacements[i+1]);
            }
        }
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(message)));
    }
}