package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.events.PlayerClickEvent;
import me.rocketmankianproductions.serveressentials.file.Lang;
import me.rocketmankianproductions.serveressentials.utils.GUIPaginationHelper;
import me.rocketmankianproductions.serveressentials.utils.JailPlayerUtil;
import me.rocketmankianproductions.serveressentials.utils.JailUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.List;

public class Jail implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        switch (command.getName().toLowerCase()) {

            case "jail" -> handleJail(sender, args);

            case "unjail" -> handleUnjail(sender, args);

            case "jailtime" -> handleJailTime(sender, args);

            case "createjail" -> handleCreateJail(sender, args);

            case "deletejail" -> handleDeleteJail(sender, args);

            case "jaillist" -> handleJailList(sender, args);

            default -> sender.sendMessage("Unknown command.");
        }

        return false;
    }

    private void handleJail(CommandSender sender, String[] args) {

        if (ServerEssentials.permissionChecker(sender, "se.jail")){
            if (!(args.length >= 4)) {
                sender.sendMessage("§cUsage: /jail <player> <seconds> <jail> <reason>");
                return;
            }

            Player target = Bukkit.getPlayer(args[0]);

            if (target == null) {
                sender.sendMessage(ServerEssentials.hex(Lang.fileConfig.getString("target-offline")));
                return;
            }

            if (target == sender){
                sender.sendMessage(ServerEssentials.hex(Lang.fileConfig.getString("jail-self")));
                return;
            }

            if (target.hasPermission("se.jail.bypass")){
                sender.sendMessage(ServerEssentials.hex(Lang.fileConfig.getString("jail-target-bypass")
                        .replace("<player>", target.getName())));
                return;
            }

            if (ServerEssentials.getInstance.jailManager.isJailed(target)){
                sender.sendMessage(ServerEssentials.hex(Lang.fileConfig.getString("already-jailed")
                        .replace("<player>", target.getName())));
                return;
            }

            long time;

            try {
                time = parseDuration(args[1]);
            } catch (IllegalArgumentException e) {
                sender.sendMessage("§cInvalid duration. Example: 10s, 5m, 2h30m, 1d12h");
                return;
            }

            String jailName = args[2];

            // ✅ CHECK JAIL EXISTS BEFORE JAILING
            if (ServerEssentials.getInstance.jailManager.getJail(jailName) == null) {
                sender.sendMessage(ServerEssentials.hex(Lang.fileConfig.getString("jail-invalid")
                        .replace("<jail>", jailName)));
                return;
            }

            String reason = String.join(" ", Arrays.copyOfRange(args, 3, args.length));
            ServerEssentials.getInstance
                    .jailManager
                    .jailPlayer(target, jailName, (int) time, args[1], reason);

            sender.sendMessage(ServerEssentials.hex(Lang.fileConfig.getString("jail-success")
                    .replace("<player>", target.getName())
                    .replace("<jail>", jailName)
                    .replace("<duration>", args[1])
                    .replace("<reason>", reason)));
        }
    }

    private void handleUnjail(CommandSender sender, String[] args) {

        if (ServerEssentials.permissionChecker(sender, "se.unjail")){
            if (args.length != 1) {
                sender.sendMessage("§cUsage: /unjail <player>");
                return;
            }

            Player target = Bukkit.getPlayer(args[0]);

            if (target == null) {
                sender.sendMessage(ServerEssentials.hex(Lang.fileConfig.getString("target-offline")));
                return;
            }

            if (!ServerEssentials.getInstance.jailManager.isJailed(target)){
                sender.sendMessage(ServerEssentials.hex(Lang.fileConfig.getString("jail-release-invalid").replace("<player>", target.getName())));
                return;
            }

            ServerEssentials.getInstance
                    .jailManager
                    .releasePlayer(target.getUniqueId());

            sender.sendMessage(ServerEssentials.hex(Lang.fileConfig.getString("jail-release")
                    .replace("<player>", target.getName())));
        }
    }

    private void handleJailTime(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cThis command must be used in-game.");
            return;
        }

        if (ServerEssentials.permissionChecker(sender, "se.jailtime")){
            if (args.length != 0) {
                player.sendMessage("§cUsage: /jailtime");
                return;
            }

            if (!ServerEssentials.getInstance.jailManager.isJailed(player)){
                player.sendMessage(ServerEssentials.hex(Lang.fileConfig.getString("jail-self-invalid")));
                return;
            }

            JailPlayerUtil entry = ServerEssentials.getInstance.jailManager.getJailedPlayers().get(player.getUniqueId());
            player.sendMessage(ServerEssentials.hex(Lang.fileConfig.getString("jail-target-attempt").replace("<reason>", entry.getReason())
                    .replace("<duration>", ServerEssentials.getInstance.jailManager.getTimeLeft(entry))));
        }
    }

    private void handleCreateJail(CommandSender sender, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cThis command must be used in-game.");
            return;
        }

        if (ServerEssentials.permissionChecker(sender, "se.createjail")){
            if (args.length != 1) {
                sender.sendMessage("§cUsage: /createjail <name>");
                return;
            }

            String name = args[0];

            JailUtil existing = ServerEssentials.getInstance
                    .jailManager
                    .getJail(name);

            JailUtil jail = new JailUtil(name, player.getLocation());

            ServerEssentials.getInstance
                    .jailManager
                    .addJail(jail);

            if (existing == null) {
                sender.sendMessage(ServerEssentials.hex(Lang.fileConfig.getString("jail-created").replace("<jail>", name)));
            } else {
                sender.sendMessage(ServerEssentials.hex(Lang.fileConfig.getString("jail-updated").replace("<jail>", name)));
            }
        }
    }

    private void handleDeleteJail(CommandSender sender, String[] args) {

        if (ServerEssentials.permissionChecker(sender, "se.deletejail")){
            if (args.length != 1) {
                sender.sendMessage("§cUsage: /deletejail <name>");
                return;
            }

            String name = args[0];

            JailUtil existing = ServerEssentials.getInstance
                    .jailManager
                    .getJail(name);

            if (existing == null) {
                sender.sendMessage(ServerEssentials.hex(Lang.fileConfig.getString("jail-invalid").replace("<jail>", name)));
                return;
            }

            ServerEssentials.getInstance
                    .jailManager
                    .removeJail(name);

            sender.sendMessage(ServerEssentials.hex(Lang.fileConfig.getString("jail-deleted").replace("<jail>", name)));
        }
    }

    private void handleJailList(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cThis command must be used in-game.");
            return;
        }

        if (ServerEssentials.permissionChecker(sender, "se.jaillist")){
            if (args.length != 0) {
                sender.sendMessage("§cUsage: /jaillist");
                return;
            }
            openJailGUI(player, 1);
        }
    }

    public static void openJailGUI(Player player, int page) {

        List<ItemStack> allJailItems = new ArrayList<>();

        Collection<JailUtil> allJails =
                ServerEssentials.getInstance.jailManager.getAllJails();

        // =========================
        // BUILD ITEMS
        // =========================
        if (!allJails.isEmpty()) {

            allJailItems = allJails.stream()

                    .sorted(Comparator.comparing(
                            JailUtil::getName,
                            String.CASE_INSENSITIVE_ORDER
                    ))

                    .map(jail -> {

                        ItemStack item = new ItemStack(Material.IRON_BARS);

                        ItemMeta meta = item.getItemMeta();

                        if (meta != null) {

                            meta.setDisplayName(
                                    ChatColor.translateAlternateColorCodes(
                                            '&',
                                            "&c" + jail.getName()
                                    )
                            );

                            List<String> lore = new ArrayList<>();

                            lore.add("§7Click to teleport to jail.");
                            lore.add("");

                            Location loc = jail.getLocation();

                            lore.add("§fWorld: §7" + loc.getWorld().getName());
                            lore.add("§fX: §7" + loc.getBlockX());
                            lore.add("§fY: §7" + loc.getBlockY());
                            lore.add("§fZ: §7" + loc.getBlockZ());

                            meta.setLore(lore);

                            item.setItemMeta(meta);
                        }

                        return item;
                    })

                    .toList();
        }else{
            player.sendMessage(ServerEssentials.hex(Lang.fileConfig.getString("no-jails")));
            return;
        }

        // =========================
        // PAGINATION
        // =========================
        int itemsPerPage = 7;

        int totalItems = allJailItems.size();

        int totalPages =
                (int) Math.ceil((double) totalItems / itemsPerPage);

        if (totalPages <= 0) {
            totalPages = 1;
        }

        if (page < 1) {
            page = 1;
        }

        if (page > totalPages) {
            page = totalPages;
        }

        int startIndex = (page - 1) * itemsPerPage;

        int endIndex =
                Math.min(startIndex + itemsPerPage, totalItems);

        List<ItemStack> itemsOnPage =
                allJailItems.subList(startIndex, endIndex);

        // =========================
        // CREATE INVENTORY
        // =========================
        Inventory gui = Bukkit.createInventory(
                null,
                9,
                "§c§lJails"
        );
        // 54

        // =========================
        // PLACE ITEMS
        // =========================
        for (int i = 0; i < itemsOnPage.size(); i++) {
            gui.setItem(i, itemsOnPage.get(i));
        }

        // =========================
        // PAGINATION BUTTONS
        // =========================
        GUIPaginationHelper.updatePaginationButtons(
                gui,
                page,
                totalPages,
                itemsOnPage
        );

        // =========================
        // OPEN GUI
        // =========================
        player.openInventory(gui);

        PlayerClickEvent.jailPages.put(
                player.getUniqueId(),
                page
        );
    }

    private long parseDuration(String input) {

        input = input.toLowerCase();

        long totalSeconds = 0;

        StringBuilder number = new StringBuilder();

        for (char c : input.toCharArray()) {

            if (Character.isDigit(c)) {
                number.append(c);
                continue;
            }

            if (number.isEmpty()) {
                throw new IllegalArgumentException("Invalid duration format.");
            }

            long value = Long.parseLong(number.toString());

            switch (c) {
                case 's' -> totalSeconds += value;
                case 'm' -> totalSeconds += value * 60;
                case 'h' -> totalSeconds += value * 60 * 60;
                case 'd' -> totalSeconds += value * 60 * 60 * 24;
                case 'w' -> totalSeconds += value * 60 * 60 * 24 * 7;

                default -> throw new IllegalArgumentException("Invalid time unit: " + c);
            }

            number.setLength(0);
        }

        // catches "10" with no unit
        if (!number.isEmpty()) {
            throw new IllegalArgumentException("Missing time unit.");
        }

        return totalSeconds;
    }
}