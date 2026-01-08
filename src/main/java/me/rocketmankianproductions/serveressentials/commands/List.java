package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class List implements CommandExecutor {

    private Permission perms;
    private LuckPerms luckPerms;

    private static final java.util.List<String> RANK_PRIORITY = java.util.List.of(
            "admin",
            "mod",
            "helper",
            "default"
    );

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player player &&
                !ServerEssentials.permissionChecker(player, "se.list")) {
            return true;
        }

        if (!setupPermissions()) {
            sender.sendMessage(ChatColor.RED + "Permissions system not found.");
            return true;
        }

        setupLuckPerms();

        String header = Lang.fileConfig.getString("list-message")
                .replace("<amount>", String.valueOf(Bukkit.getOnlinePlayers().size()))
                .replace("<total>", String.valueOf(Bukkit.getMaxPlayers()));

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                ServerEssentials.hex(header)));

        java.util.List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());

        players.sort((p1, p2) ->
                Integer.compare(getPriority(p2), getPriority(p1))
        );

        StringBuilder list = new StringBuilder();

        for (Player online : players) {
            String group = perms.getPrimaryGroup(online);
            if (group == null || group.isEmpty()) group = "default";

            list.append(ChatColor.GOLD)
                    .append("[")
                    .append(capitalize(group))
                    .append("] ")
                    .append(ChatColor.YELLOW)
                    .append(online.getName())
                    .append(ChatColor.GRAY)
                    .append(", ");
        }

        if (list.length() >= 2) {
            list.setLength(list.length() - 2);
        }

        sender.sendMessage(list.toString());
        return true;
    }

    private int getPriority(Player player) {
        return luckPerms != null
                ? getLuckPermsWeight(player)
                : getManualPriority(player);
    }

    private int getLuckPermsWeight(Player player) {
        User user = luckPerms.getUserManager().getUser(player.getUniqueId());
        if (user == null) return 0;

        Group group = luckPerms.getGroupManager().getGroup(user.getPrimaryGroup());
        if (group == null) return 0;

        return group.getWeight().orElse(0);
    }

    private int getManualPriority(Player player) {
        String group = perms.getPrimaryGroup(player);
        if (group == null) return 0;

        int index = RANK_PRIORITY.indexOf(group.toLowerCase());
        return index == -1 ? 0 : (RANK_PRIORITY.size() - index);
    }

    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return "";
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }

    private boolean setupPermissions() {
        if (perms != null) return true;

        RegisteredServiceProvider<Permission> rsp =
                Bukkit.getServicesManager().getRegistration(Permission.class);

        if (rsp == null) return false;

        perms = rsp.getProvider();
        return perms != null;
    }

    private void setupLuckPerms() {
        if (luckPerms != null) return;

        if (Bukkit.getPluginManager().isPluginEnabled("LuckPerms")) {
            try {
                luckPerms = LuckPermsProvider.get();
            } catch (IllegalStateException ignored) {
            }
        }
    }
}