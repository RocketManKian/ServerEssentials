package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
import me.rocketmankianproductions.serveressentials.file.UserFile;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.logging.Level; // Import Level for logging

import static me.rocketmankianproductions.serveressentials.ServerEssentials.hex;

public class TeleportRequest implements CommandExecutor {

    // Maps to store pending teleport requests
    public static final HashMap<UUID, UUID> tpaRequests = new HashMap<>(); // target UUID -> sender UUID
    public static final HashMap<UUID, UUID> tpahereRequests = new HashMap<>(); // target UUID -> sender UUID

    // Maps to store BukkitTask IDs for scheduled tasks (timeouts, delayed teleports)
    public static final HashMap<UUID, Integer> teleportCancelTasks = new HashMap<>(); // target UUID -> Task ID for timeout

    // Maps to store cooldown expiry timestamps (System.currentTimeMillis())
    public static final HashMap<UUID, Long> teleportCooldowns = new HashMap<>(); // Player UUID -> Cooldown expiry time in milliseconds
    public static final HashMap<UUID, Long> teleportHereCooldowns = new HashMap<>(); // Player UUID -> Cooldown expiry time in milliseconds
    public static final HashMap<UUID, Integer> delayedTeleports = new HashMap<>(); // Player UUID -> Task ID for delayed teleport

    // Lists for movement cancellation
    public static final ArrayList<UUID> movementCancelTPA = new ArrayList<>(); // Player UUID who initiated TPA and needs movement cancel
    public static final ArrayList<UUID> movementCancelTPAHere = new ArrayList<>(); // Player UUID who accepted TPAHERE and needs movement cancel


    /**
     * Helper for PlayerMoveEvent to find the target (acceptor) UUID of a TPA request given the initiator (sender) UUID.
     * @param initiatorUUID The UUID of the player who sent the /tpa request.
     * @return The UUID of the target player, or null if no such pending TPA request exists.
     */
    public static UUID getTpaTargetByInitiator(UUID initiatorUUID) {
        for (Map.Entry<UUID, UUID> entry : tpaRequests.entrySet()) {
            if (entry.getValue().equals(initiatorUUID)) {
                return entry.getKey(); // The key in tpaRequests is the target's UUID
            }
        }
        return null;
    }

    /**
     * Helper for PlayerMoveEvent to find the initiator (sender) UUID of a TPAHERE request given the target (acceptor) UUID.
     * @param targetUUID The UUID of the player who accepted the /tpahere request.
     * @return The UUID of the initiator player, or null if no such pending TPAHERE request exists.
     */
    public static UUID getTpaHereInitiatorByTarget(UUID targetUUID) {
        // In tpahereRequests, the key is the target's UUID, and the value is the initiator's UUID.
        return tpahereRequests.get(targetUUID);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sendMessage(null, "console-invalid", message -> Bukkit.getLogger().info(message)); // Using lambda for Consumer
            return true;
        }

        switch (command.getName().toLowerCase()) {
            case "tpa":
                return handleTpaCommand(player, args);
            case "tpahere":
                return handleTpaHereCommand(player, args);
            case "tpacancel":
                return handleTpaCancelCommand(player);
            case "tpaccept":
                return handleTpAcceptCommand(player);
            case "tpdeny":
                return handleTpDenyCommand(player);
            default:
                return false;
        }
    }

    private boolean handleTpaCommand(Player player, String[] args) {
        if (!ServerEssentials.permissionChecker(player, "se.tpa")) {
            return true;
        }

        if (args.length < 1) {
            sendMessage(player, "incorrect-format", "<command>", "/tpa (player)");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (!isValidTarget(player, target)) {
            return true;
        }

        return handleTeleportRequest(player, target, tpaRequests, teleportCooldowns, "teleport-request-blacklisted-world",
                "teleport-request-sent", "teleport-request-target-receive", "teleport-request-timeout-warning",
                "teleport-request-cancel-warning", "teleport-request-cancel-button", "teleport-request-accept", "teleport-request-deny",
                "teleport-request-accept-button", "teleport-request-deny-button", "teleport-request-cancel-button");
    }

    private boolean handleTpaHereCommand(Player player, String[] args) {
        if (!ServerEssentials.permissionChecker(player, "se.tpahere")) {
            return true;
        }

        if (args.length < 1) {
            sendMessage(player, "incorrect-format", "<command>", "/tpahere (player)");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (!isValidTarget(player, target)) {
            return true;
        }

        return handleTeleportRequest(player, target, tpahereRequests, teleportHereCooldowns, "teleport-here-blacklisted-world",
                "teleport-here-request-sent", "teleport-here-request-target-receive", "teleport-here-request-timeout-warning",
                "teleport-here-request-cancel-warning", "teleport-request-cancel-button", "teleport-here-request-accept", "teleport-here-request-deny",
                "teleport-here-request-accept-button", "teleport-here-request-deny-button", "teleport-here-request-cancel-button");
    }

    private boolean handleTpaCancelCommand(Player player) {
        if (!ServerEssentials.permissionChecker(player, "se.tpacancel")) {
            return true;
        }

        // Find if this player is the SENDER of a TPA or TPAHERE request
        UUID tpaTargetUUID = getTpaTargetByInitiator(player.getUniqueId()); // player is the sender, so find the target
        UUID tpahereTargetUUID = null;
        for (Map.Entry<UUID, UUID> entry : tpahereRequests.entrySet()) {
            if (entry.getValue().equals(player.getUniqueId())) {
                tpahereTargetUUID = entry.getKey(); // Player is the sender, find the target (key)
                break;
            }
        }

        if (tpaTargetUUID != null || tpahereTargetUUID != null) {
            sendMessage(player, "teleport-cancel"); // Message to the player who cancelled their own request

            if (tpaTargetUUID != null) {
                cancelTimeout(tpaTargetUUID); // Cancel timeout for the target of the TPA request
                tpaRequests.remove(tpaTargetUUID); // Remove the request using the target's UUID as key
                //ServerEssentials.getPlugin().getLogger().log(Level.INFO, "[TPA] Removed Request (Cancel by Sender): Target=" + Bukkit.getPlayer(tpaTargetUUID).getName() + " (" + tpaTargetUUID + "), Sender=" + player.getName() + " (" + player.getUniqueId() + "). Map size: " + tpaRequests.size());
                // Inform the target that the request was cancelled by the sender
                Player targetOfTpa = Bukkit.getPlayer(tpaTargetUUID);
                if (targetOfTpa != null && targetOfTpa.isOnline()) {
                    sendMessage(targetOfTpa, "teleport-request-cancelled-by-sender", "<target>", player.getName()); // Add to lang.yml
                }
            }
            if (tpahereTargetUUID != null) {
                cancelTimeout(tpahereTargetUUID); // Cancel timeout for the target of the TPAHERE request
                tpahereRequests.remove(tpahereTargetUUID); // Remove the request using the target's UUID as key
                //ServerEssentials.getPlugin().getLogger().log(Level.INFO, "[TPAHERE] Removed Request (Cancel by Sender): Target=" + Bukkit.getPlayer(tpahereTargetUUID).getName() + " (" + tpahereTargetUUID + "), Sender=" + player.getName() + " (" + player.getUniqueId() + "). Map size: " + tpahereRequests.size());
                // Inform the target that the request was cancelled by the sender
                Player targetOfTpaHere = Bukkit.getPlayer(tpahereTargetUUID);
                if (targetOfTpaHere != null && targetOfTpaHere.isOnline()) {
                    sendMessage(targetOfTpaHere, "teleport-request-cancelled-by-sender", "<target>", player.getName()); // Add to lang.yml
                }
            }
            return true;
        } else {
            sendMessage(player, "teleport-no-request");
            return true;
        }
    }

    private boolean handleTpAcceptCommand(Player player) { // 'player' is the acceptor/target
        if (!ServerEssentials.permissionChecker(player, "se.tpaccept")) {
            return true;
        }

        Player initiator = null; // Renamed 'target' to 'initiator' for clarity
        String requestType = null;

        if (tpaRequests.containsKey(player.getUniqueId())) {
            initiator = Bukkit.getPlayer(tpaRequests.get(player.getUniqueId()));
            requestType = "tpa";
        } else if (tpahereRequests.containsKey(player.getUniqueId())) {
            initiator = Bukkit.getPlayer(tpahereRequests.get(player.getUniqueId()));
            requestType = "tpahere";
        }

        if (requestType == null) {
            sendMessage(player, "teleport-no-request-accept");
            return true;
        }
        if (initiator == null || !initiator.isOnline()) {
            sendMessage(player, "target-offline"); // Message for acceptor: initiator is offline
            if ("tpa".equals(requestType)) {
                tpaRequests.remove(player.getUniqueId());
                //ServerEssentials.getPlugin().getLogger().log(Level.INFO, "[TPA] Removed Stale Request (Initiator Offline): Target=" + player.getName() + " (" + player.getUniqueId() + "). Map size: " + tpaRequests.size());
            } else {
                tpahereRequests.remove(player.getUniqueId());
                //ServerEssentials.getPlugin().getLogger().log(Level.INFO, "[TPAHERE] Removed Stale Request (Initiator Offline): Target=" + player.getName() + " (" + player.getUniqueId() + "). Map size: " + tpahereRequests.size());
            }
            cancelTimeout(player.getUniqueId()); // Cancel any associated timeout task
            return true;
        }

        int tpWaitTime = ServerEssentials.plugin.getConfig().getInt("teleport-wait");
        // Check both players for bypass permission
        boolean bypassWait;
        // The player who is ACTUALLY teleporting should be checked for bypass
        if (requestType.equals("tpa")){ // Initiator teleports to acceptor
            bypassWait = initiator.hasPermission("se.teleport.bypass");
        }else{ // Acceptor teleports to initiator
            bypassWait = player.hasPermission("se.teleport.bypass");
        }
        boolean movementCancelEnabled = ServerEssentials.plugin.getConfig().getBoolean("teleport-movement-cancel");

        cancelTimeout(player.getUniqueId()); // Cancel the request timeout for the accepting player (the key in teleportCancelTasks)

        // >>> MOVE THE REQUEST REMOVAL HERE, BEFORE INSTANT OR DELAYED TELEPORTATION <<<
        if ("tpa".equals(requestType)) {
            tpaRequests.remove(player.getUniqueId()); // player is the acceptor (the key)
            //ServerEssentials.getPlugin().getLogger().log(Level.INFO, "[TPA] Removed Request (Accepted - Pre-Teleport): Target=" + player.getName() + " (" + player.getUniqueId() + "), Initiator=" + initiator.getName() + " (" + initiator.getUniqueId() + "). Map size: " + tpaRequests.size());
        } else { // tpahere
            tpahereRequests.remove(player.getUniqueId()); // player is the acceptor (the key)
            //ServerEssentials.getPlugin().getLogger().log(Level.INFO, "[TPAHERE] Removed Request (Accepted - Pre-Teleport): Target=" + player.getName() + " (" + player.getUniqueId() + "), Initiator=" + initiator.getName() + " (" + initiator.getUniqueId() + "). Map size: " + tpahereRequests.size());
        }

        if (tpWaitTime == 0 || bypassWait) {
            performInstantTeleport(player, initiator, requestType); // Pass player as acceptor, initiator as initiator
        } else {
            initiateTeleportationWithDelay(player, initiator, tpWaitTime, requestType, movementCancelEnabled); // Pass player as acceptor, initiator as initiator
        }
        return true;
    }

    private boolean handleTpDenyCommand(Player player) { // 'player' is the denier/target
        if (!ServerEssentials.permissionChecker(player, "se.tpdeny")) {
            return true;
        }

        Player initiator = null; // Renamed 'target' to 'initiator' for clarity
        String requestType = null;

        if (tpaRequests.containsKey(player.getUniqueId())) {
            initiator = Bukkit.getPlayer(tpaRequests.get(player.getUniqueId()));
            requestType = "tpa";
        } else if (tpahereRequests.containsKey(player.getUniqueId())) {
            initiator = Bukkit.getPlayer(tpahereRequests.get(player.getUniqueId()));
            requestType = "tpahere";
        }

        if (requestType == null) {
            sendMessage(player, "teleport-no-request-deny");
            return true;
        }
        if (initiator == null || !initiator.isOnline()) {
            sendMessage(player, "target-offline"); // Message for denier: initiator is offline
            // If the initiator is offline, the request is stale, so remove it
            if ("tpa".equals(requestType)) {
                tpaRequests.remove(player.getUniqueId());
                //ServerEssentials.getPlugin().getLogger().log(Level.INFO, "[TPA] Removed Stale Request (Initiator Offline): Target=" + player.getName() + " (" + player.getUniqueId() + "). Map size: " + tpaRequests.size());
            } else {
                tpahereRequests.remove(player.getUniqueId());
                //ServerEssentials.getPlugin().getLogger().log(Level.INFO, "[TPAHERE] Removed Stale Request (Initiator Offline): Target=" + player.getName() + " (" + player.getUniqueId() + "). Map size: " + tpahereRequests.size());
            }
            cancelTimeout(player.getUniqueId()); // Cancel any associated timeout task
            return true;
        }

        if (requestType.equals("tpa")) {
            sendMessage(player, "teleport-deny-request-target", "<target>", initiator.getName()); // Message for denier about initiator
            sendMessage(initiator, "teleport-deny-request", "<sender>", player.getName()); // Message for initiator about denier
            tpaRequests.remove(player.getUniqueId()); // Remove the request
            //ServerEssentials.getPlugin().getLogger().log(Level.INFO, "[TPA] Removed Request (Denied): Target=" + player.getName() + " (" + player.getUniqueId() + "), Initiator=" + initiator.getName() + " (" + initiator.getUniqueId() + "). Map size: " + tpaRequests.size());
        } else { // tpahere
            sendMessage(player, "teleport-deny-request-target", "<target>", initiator.getName());
            sendMessage(initiator, "teleport-deny-request", "<sender>", player.getName());
            tpahereRequests.remove(player.getUniqueId()); // Remove the request
            //ServerEssentials.getPlugin().getLogger().log(Level.INFO, "[TPAHERE] Removed Request (Denied): Target=" + player.getName() + " (" + player.getUniqueId() + "), Initiator=" + initiator.getName() + " (" + initiator.getUniqueId() + "). Map size: " + tpahereRequests.size());
        }
        cancelTimeout(player.getUniqueId());
        return true;
    }

    private boolean isValidTarget(Player sender, Player target) {
        if (target == null || !target.isOnline()) {
            sendMessage(sender, "target-offline");
            return false;
        }
        if (target.equals(sender)) {
            sendMessage(sender, "teleport-self");
            return false;
        }
        return true;
    }

    private boolean handleTeleportRequest(Player sender, Player target, HashMap<UUID, UUID> requestMap,
                                          HashMap<UUID, Long> cooldownMap, String blacklistLangPath,
                                          String requestSentPath, String targetReceivePath, String timeoutWarningPath,
                                          String cancelWarningPath, String cancelButtonPath, String acceptButtonPath, String denyButtonPath,
                                          String acceptButtonTextPath, String denyButtonTextPath, String cancelButtonTextPath) {

        int cooldownDuration = ServerEssentials.getPlugin().getConfig().getInt("tp-cooldown"); // in seconds

        // Cooldown check and application (moved to the top)
        if (!sender.hasPermission("se.teleport.bypass")) {
            if (cooldownMap.containsKey(sender.getUniqueId())) {
                long expiryTime = cooldownMap.get(sender.getUniqueId());
                if (System.currentTimeMillis() < expiryTime) {
                    int remainingTime = (int) Math.ceil((expiryTime - System.currentTimeMillis()) / 1000.0);
                    // Only send cooldown message if there's actual time remaining
                    if (remainingTime > 0) {
                        sendMessage(sender, "command-timeout", "<time>", String.valueOf(remainingTime));
                        return true; // Command is on cooldown
                    } else {
                        // Cooldown expired, remove it so it doesn't linger
                        cooldownMap.remove(sender.getUniqueId());
                    }
                } else {
                    // Cooldown expired, remove it
                    cooldownMap.remove(sender.getUniqueId());
                }
            }
            // Apply cooldown immediately upon successful initial validation
            // This ensures that if they try to spam, the cooldown is already set
            cooldownMap.put(sender.getUniqueId(), System.currentTimeMillis() + (long)cooldownDuration * 1000);
            //ServerEssentials.getPlugin().getLogger().log(Level.INFO, "[COOLDOWN] Applied cooldown to " + sender.getName() + ". Expires in " + cooldownDuration + "s.");
        }


        if (blacklistCheck(sender, target, blacklistLangPath)) {
            // If blacklisted, remove the cooldown that was just applied (optional, but cleaner)
            if (!sender.hasPermission("se.teleport.bypass")) {
                cooldownMap.remove(sender.getUniqueId());
            }
            return true;
        }

        if (UserFile.config.getBoolean(target.getUniqueId() + ".tptoggle")) {
            sendMessage(sender, "teleport-disabled");
            // If target has TPToggle enabled, remove the cooldown (optional, but cleaner)
            if (!sender.hasPermission("se.teleport.bypass")) {
                cooldownMap.remove(sender.getUniqueId());
            }
            return true;
        }

        // Check if there's already a pending request to the target (to prevent spamming one target)
        if (requestMap.containsKey(target.getUniqueId())) {
            Player existingSenderOfRequest = Bukkit.getPlayer(requestMap.get(target.getUniqueId())); // This is the UUID of the original sender
            if (existingSenderOfRequest != null && existingSenderOfRequest.isOnline()) {
                sendMessage(sender, "teleport-request-already-sent", "<target>", target.getName()); // Add to lang.yml
                // If a request is already pending, remove the cooldown for the current sender (optional)
                if (!sender.hasPermission("se.teleport.bypass")) {
                    cooldownMap.remove(sender.getUniqueId());
                }
                return true;
            } else {
                // Clean up stale request if original sender is offline
                requestMap.remove(target.getUniqueId());
                cancelTimeout(target.getUniqueId());
                //ServerEssentials.getPlugin().getLogger().log(Level.INFO, "[TPA/TPAHERE] Removed Stale Request (Original Sender Offline): Target=" + target.getName() + " (" + target.getUniqueId() + "). Map size: " + requestMap.size());
            }
        }


        requestMap.put(target.getUniqueId(), sender.getUniqueId());
        //ServerEssentials.getPlugin().getLogger().log(Level.INFO, "[TPA/TPAHERE] Added Request: Target=" + target.getName() + " (" + target.getUniqueId() + "), Sender=" + sender.getName() + " (" + sender.getUniqueId() + "). Map size: " + requestMap.size());

        int timeoutDelay = ServerEssentials.getPlugin().getConfig().getInt("teleport-cancel"); // in seconds
        int timeoutDelayTicks = timeoutDelay * 20;

        // Cancel any existing timeout for the target (if they have another pending request)
        if (teleportCancelTasks.containsKey(target.getUniqueId())) {
            cancelTimeout(target.getUniqueId());
        }

        // Schedule timeout for the request
        int timeoutTask = Bukkit.getScheduler().scheduleSyncDelayedTask(ServerEssentials.getPlugin(), () -> {
            // Check if the request is still pending for this specific sender/target pair
            if (requestMap.containsKey(target.getUniqueId()) && requestMap.get(target.getUniqueId()).equals(sender.getUniqueId())) {
                sendMessage(target, "teleport-request-timeout", "<target>", sender.getName()); // Message to target (they didn't respond)
                if (sender.isOnline()) {
                    sendMessage(sender, "teleport-request-sender-timeout", "<target>", target.getName()); // Message to sender (their request timed out)
                }
                requestMap.remove(target.getUniqueId());
                //ServerEssentials.getPlugin().getLogger().log(Level.INFO, "[TPA/TPAHERE] Removed Request (Timeout): Target=" + target.getName() + " (" + target.getUniqueId() + "), Sender=" + sender.getName() + " (" + sender.getUniqueId() + "). Map size: " + requestMap.size());
            }
            teleportCancelTasks.remove(target.getUniqueId()); // Remove task ID after execution, regardless of whether it was active
        }, timeoutDelayTicks);
        teleportCancelTasks.put(target.getUniqueId(), timeoutTask);

        sendTeleportRequestMessages(sender, target, requestSentPath, targetReceivePath, timeoutWarningPath,
                cancelWarningPath, cancelButtonPath, acceptButtonPath, denyButtonPath, acceptButtonTextPath, denyButtonTextPath, cancelButtonTextPath, timeoutDelay);
        return true;
    }

    private void performInstantTeleport(Player acceptor, Player initiator, String requestType) { // Renamed for clarity
        if ("tpa".equals(requestType)) {
            Teleport.teleportSave(initiator); // initiator is the one teleporting
            initiator.teleport(acceptor); // initiator teleports to acceptor
            teleportSuccessMessage(initiator, acceptor, "tpa");
        } else { // tpahere
            Teleport.teleportSave(acceptor); // acceptor is the one teleporting
            acceptor.teleport(initiator); // acceptor teleports to initiator
            teleportSuccessMessage(acceptor, initiator, "tpahere");
        }
        //ServerEssentials.getPlugin().getLogger().log(Level.INFO, "[TELEPORT] Instant Teleport Completed: Type=" + requestType + ", Initiator=" + initiator.getName() + ", Acceptor=" + acceptor.getName());
    }

    public void initiateTeleportationWithDelay(Player acceptor, Player initiator, int waitTime, String requestType, boolean movementCancellation) {
        // Clear any existing delayed teleport task for the acceptor
        if (delayedTeleports.containsKey(acceptor.getUniqueId())) {
            Bukkit.getScheduler().cancelTask(delayedTeleports.get(acceptor.getUniqueId()));
            delayedTeleports.remove(acceptor.getUniqueId());
            //ServerEssentials.getPlugin().getLogger().log(Level.INFO, "[DELAYED_TELEPORT] Cancelled existing delayed task for acceptor: " + acceptor.getName());
        }
        // Also clear any existing delayed teleport task for the initiator if they were somehow involved in another active delay
        if (delayedTeleports.containsKey(initiator.getUniqueId())) {
            Bukkit.getScheduler().cancelTask(delayedTeleports.get(initiator.getUniqueId()));
            delayedTeleports.remove(initiator.getUniqueId());
            //ServerEssentials.getPlugin().getLogger().log(Level.INFO, "[DELAYED_TELEPORT] Cancelled existing delayed task for initiator: " + initiator.getName());
        }


        // Set up movement cancellation tracking if enabled
        if (movementCancellation) {
            if ("tpa".equals(requestType)) { // initiator teleports to acceptor
                movementCancelTPA.add(initiator.getUniqueId()); // Add initiator to cancel list
                //ServerEssentials.getPlugin().getLogger().log(Level.INFO, "[MOVEMENT_CANCEL] Added " + initiator.getName() + " to movementCancelTPA.");
            } else { // tpahere: acceptor teleports to initiator
                movementCancelTPAHere.add(acceptor.getUniqueId()); // Add acceptor to cancel list
                //ServerEssentials.getPlugin().getLogger().log(Level.INFO, "[MOVEMENT_CANCEL] Added " + acceptor.getName() + " to movementCancelTPAHere.");
            }
        }

        // Send initial messages about acceptance and wait
        sendMessage(acceptor, "teleport-accept-request-target", "<target>", initiator.getName());
        sendMessage(initiator, "teleport-wait-message", "<player>", acceptor.getName(), "<time>", String.valueOf(waitTime));


        Location teleportLoc;
        if ("tpa".equals(requestType)) {
            teleportLoc = acceptor.getLocation();
        }else {
            teleportLoc = initiator.getLocation();
        }

        Location finalTeleportLoc = teleportLoc;
        int scheduledTask = Bukkit.getScheduler().scheduleSyncDelayedTask(ServerEssentials.plugin, () -> {
            boolean cancelledByMovement = false;
            UUID playerToTeleportUUID;
            Player playerWhoTeleports;
            Player otherPlayer; // The player who is NOT teleporting (the destination)
            otherPlayer = acceptor;
            playerToTeleportUUID = initiator.getUniqueId(); // Initiator is the one who will teleport
            playerWhoTeleports = initiator;

            if ("tpa".equals(requestType)) {
                if (movementCancellation && !movementCancelTPA.contains(playerToTeleportUUID)) {
                    // This means the player *was* in the list, but the PlayerMoveEvent removed them because they moved.
                    cancelledByMovement = true;
                }
                movementCancelTPA.remove(playerToTeleportUUID); // Ensure it's removed if it was still there
                //ServerEssentials.getPlugin().getLogger().log(Level.INFO, "[MOVEMENT_CANCEL] Removed " + playerWhoTeleports.getName() + " from movementCancelTPA at end of delay.");
            } else { // tpahere
                playerToTeleportUUID = acceptor.getUniqueId(); // Acceptor is the one who will teleport
                playerWhoTeleports = acceptor;
                otherPlayer = initiator;
                if (movementCancellation && !movementCancelTPAHere.contains(playerToTeleportUUID)) {
                    cancelledByMovement = true;
                }
                movementCancelTPAHere.remove(playerToTeleportUUID); // Ensure it's removed
                //ServerEssentials.getPlugin().getLogger().log(Level.INFO, "[MOVEMENT_CANCEL] Removed " + playerWhoTeleports.getName() + " from movementCancelTPAHere at end of delay.");
            }

            // Handle cancelled by movement:
            if (cancelledByMovement) {
                ServerEssentials.getPlugin().getLogger().log(Level.INFO, "[TELEPORT] Delayed " + requestType.toUpperCase() + " Cancelled by Movement for " + playerWhoTeleports.getName());
                if (playerWhoTeleports.isOnline()) {
                    sendMessage(playerWhoTeleports, "teleport-movement-cancel"); // Message for the mover (sender of TPA, target of TPAHERE)
                }
                if (otherPlayer.isOnline()) {
                    sendMessage(otherPlayer, "teleport-request-cancelled-by-target", "<target>", playerWhoTeleports.getName()); // Message for the other party
                }
                // Ensure the task ID is removed
                if (playerToTeleportUUID != null) {
                    delayedTeleports.remove(playerToTeleportUUID);
                }
                return; // Do not proceed with teleportation
            }

            // If not cancelled by movement, proceed with teleport if both players are online
            if (!playerWhoTeleports.isOnline() || !otherPlayer.isOnline()) {
                if (playerWhoTeleports.isOnline()) sendMessage(playerWhoTeleports, "teleport-failed-offline");
                if (otherPlayer.isOnline()) sendMessage(otherPlayer, "teleport-failed-offline");
                //ServerEssentials.getPlugin().getLogger().log(Level.WARNING, "[TELEPORT] Delayed " + requestType.toUpperCase() + " Failed: One or both players went offline.");
                // Ensure the task ID is removed
                if (playerToTeleportUUID != null) {
                    delayedTeleports.remove(playerToTeleportUUID);
                }
                return;
            }

            // Perform teleport
            if ("tpa".equals(requestType)) {
                Teleport.teleportSave(initiator); // initiator is teleporting
                if (!ServerEssentials.plugin.getConfig().getBoolean("teleport-after-wait-location")){
                    initiator.teleport(finalTeleportLoc); // initiator teleports to acceptor's current location
                }else{
                    initiator.teleport(acceptor.getLocation()); // initiator teleports to acceptor's current location
                }
                teleportSuccessMessage(initiator, acceptor, "tpa");
                //ServerEssentials.getPlugin().getLogger().log(Level.INFO, "[TELEPORT] Delayed TPA Completed: Initiator=" + initiator.getName() + ", Acceptor=" + acceptor.getName());
            } else { // tpahere
                Teleport.teleportSave(acceptor); // acceptor is teleporting
                if (!ServerEssentials.plugin.getConfig().getBoolean("teleport-after-wait-location")){
                    acceptor.teleport(finalTeleportLoc); // initiator teleports to acceptor's current location
                }else{
                    acceptor.teleport(initiator.getLocation()); // initiator teleports to acceptor's current location
                }
                teleportSuccessMessage(acceptor, initiator, "tpahere");
                //ServerEssentials.getPlugin().getLogger().log(Level.INFO, "[TELEPORT] Delayed TPAHERE Completed: Acceptor=" + acceptor.getName() + ", Initiator=" + initiator.getName());
            }

            // Ensure the delayed teleport task ID is removed after successful teleport
            if (playerToTeleportUUID != null) {
                delayedTeleports.remove(playerToTeleportUUID);
                //ServerEssentials.getPlugin().getLogger().log(Level.INFO, "[DELAYED_TELEPORT] Removed scheduled task for " + playerWhoTeleports.getName());
            }

        }, (long) waitTime * 20); // Convert seconds to ticks

        // Store task ID against the player who is GOING TO BE TELEPORTING
        if ("tpa".equals(requestType)) { // initiator is teleporting
            delayedTeleports.put(initiator.getUniqueId(), scheduledTask);
            //ServerEssentials.getPlugin().getLogger().log(Level.INFO, "[DELAYED_TELEPORT] Scheduled TPA for " + initiator.getName() + ". Task ID: " + scheduledTask);
        } else { // tpahere: acceptor is teleporting
            delayedTeleports.put(acceptor.getUniqueId(), scheduledTask);
            //ServerEssentials.getPlugin().getLogger().log(Level.INFO, "[DELAYED_TELEPORT] Scheduled TPAHERE for " + acceptor.getName() + ". Task ID: " + scheduledTask);
        }
    }

    private void teleportSuccessMessage(Player teleportingPlayer, Player otherPlayer, String type) {
        if (teleportingPlayer.isOnline() || otherPlayer.isOnline()) {
            if (type.equals("tpa")){
                sendMessage(teleportingPlayer, "teleport-success", "<target>", otherPlayer.getName());
                sendMessage(otherPlayer, "teleport-target-success", "<sender>", teleportingPlayer.getName());
            }else{
                sendMessage(teleportingPlayer, "teleport-success", "<target>", otherPlayer.getName());
                sendMessage(otherPlayer, "teleport-target-success", "<sender>", teleportingPlayer.getName());
            }
        }
    }

    private boolean blacklistCheck(Player sender, Player target, String langPath) {
        if (ServerEssentials.getPlugin().getConfig().getBoolean("teleport-blacklist-enabled")) {
            for (String worldName : ServerEssentials.getPlugin().getConfig().getStringList("teleport-blacklisted-worlds")) {
                if (target.getWorld().getName().equalsIgnoreCase(worldName)) {
                    sendMessage(sender, langPath, "<player>", target.getName());
                    return true;
                }
            }
        }
        return false;
    }

    private void sendTeleportRequestMessages(Player sender, Player target, String requestSentPath, String targetReceivePath, String timeoutWarningPath,
                                             String cancelWarningPath, String cancelButtonPath, String acceptButtonPath, String denyButtonPath,
                                             String acceptButtonTextPath, String denyButtonTextPath, String cancelButtonTextPath, int timeoutDelay) {
        // Message to the sender
        sendMessage(sender, requestSentPath, "<target>", target.getName());
        sendMessage(sender, timeoutWarningPath, "<time>", String.valueOf(timeoutDelay));
        sendMessage(sender, cancelWarningPath);
        String cancelText = Lang.fileConfig.getString(cancelButtonTextPath); // For /tpacancel option
        TextComponent cancelButton = new TextComponent(ChatColor.translateAlternateColorCodes('&', hex(cancelText)));
        cancelButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpacancel"));
        cancelButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.translateAlternateColorCodes('&', hex(Lang.fileConfig.getString(cancelButtonPath))))));
        sender.spigot().sendMessage(cancelButton);

        // Message to the target (with clickable buttons)
        String receiveMessage = Lang.fileConfig.getString(targetReceivePath);
        if (receiveMessage != null) {
            receiveMessage = receiveMessage.replace("<sender>", sender.getName());
            receiveMessage = ChatColor.translateAlternateColorCodes('&', hex(receiveMessage));
            target.spigot().sendMessage(new TextComponent(receiveMessage));
        }

        String acceptText = Lang.fileConfig.getString(acceptButtonTextPath);
        String denyText = Lang.fileConfig.getString(denyButtonTextPath);

        TextComponent acceptButton = new TextComponent(ChatColor.translateAlternateColorCodes('&', hex(acceptText)));
        acceptButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaccept"));
        acceptButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.translateAlternateColorCodes('&', hex(Lang.fileConfig.getString(acceptButtonPath))))));

        TextComponent denyButton = new TextComponent(ChatColor.translateAlternateColorCodes('&', hex(denyText)));
        denyButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpdeny"));
        denyButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.translateAlternateColorCodes('&', hex(Lang.fileConfig.getString(denyButtonPath))))));

        TextComponent separator = new TextComponent(ChatColor.DARK_GRAY + " | ");

        target.spigot().sendMessage(acceptButton, separator, denyButton, separator); // Send to target
    }

    private void cancelTimeout(UUID playerUUID) {
        if (teleportCancelTasks.containsKey(playerUUID)) {
            Bukkit.getScheduler().cancelTask(teleportCancelTasks.get(playerUUID));
            teleportCancelTasks.remove(playerUUID);
            //ServerEssentials.getPlugin().getLogger().log(Level.INFO, "[TIMEOUT] Cancelled timeout task for player: " + Bukkit.getPlayer(playerUUID).getName() + " (" + playerUUID + ")");
        }
    }

    private UUID getKeyByValue(HashMap<UUID, UUID> map, UUID value) {
        for (Map.Entry<UUID, UUID> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }

    private void sendMessage(Player player, String configPath, String... replacements) {
        // Consumer for logging messages for console output
        Consumer<String> consoleLogger = message -> ServerEssentials.getPlugin().getLogger().info(message);
        sendMessage(player, configPath, consoleLogger, replacements);
    }

    private void sendMessage(Player player, String configPath, Consumer<String> consoleOutput, String... replacements) {
        String message = Lang.fileConfig.getString(configPath);
        if (message == null) {
            String errorMsg = "Error: Message not found for path '" + configPath + "'";
            ServerEssentials.getPlugin().getLogger().warning(errorMsg);
            if (player != null && player.isOnline()) {
                player.sendMessage(ChatColor.RED + errorMsg);
            } else {
                consoleOutput.accept(ChatColor.stripColor(errorMsg));
            }
            return;
        }

        for (int i = 0; i < replacements.length; i += 2) {
            if (i + 1 < replacements.length) {
                message = message.replace(replacements[i], replacements[i + 1]);
            }
        }

        if (player != null && player.isOnline()) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(message)));
        } else {
            consoleOutput.accept(ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', hex(message))));
        }
    }
}