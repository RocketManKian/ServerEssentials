package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class Gamemode implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player){
            Player player = (Player) sender;
            if ((command.getName().equalsIgnoreCase("gamemode") || command.getName().equalsIgnoreCase("gm")) && args.length > 0) {
                if (args[0].equalsIgnoreCase("creative") || args[0].equalsIgnoreCase("c") || args[0].equalsIgnoreCase("1")) {
                    gmc(args, player);
                    return true;
                } else if (args[0].equalsIgnoreCase("survival") || args[0].equalsIgnoreCase("s") || args[0].equalsIgnoreCase("0")) {
                    gms(args, player);
                    return true;
                } else if (args[0].equalsIgnoreCase("spectator") || args[0].equalsIgnoreCase("3")) {
                    gmsp(args, player);
                    return true;
                } else if (args[0].equalsIgnoreCase("adventure") || args[0].equalsIgnoreCase("a") || args[0].equalsIgnoreCase("2")) {
                    gma(args, player);
                    return true;
                }
            }else if ((command.getName().equalsIgnoreCase("gmc")) && args.length == 0){
                if (player.hasPermission("se.gamemode.creative")) {
                    if (args.length == 1) {
                        Player targetPlayer = Bukkit.getServer().getPlayer(args[0]);
                        targetPlayer.setGameMode(GameMode.CREATIVE);
                        targetPlayer.sendMessage(ChatColor.GREEN + "You are now in Creative");
                        player.sendMessage(ChatColor.GREEN + "Set " + ChatColor.GOLD + targetPlayer.getName() + ChatColor.GREEN + " into Creative");
                        return true;
                    } else if (args.length == 0) {
                        player.setGameMode(GameMode.CREATIVE);
                        player.sendMessage(ChatColor.GREEN + "You are now in Creative");
                        return true;
                    }
                } else {
                    if (ServerEssentials.plugin.getConfig().getString("no-permission-message").length() == 0) {
                        player.sendMessage(ChatColor.RED + "You do not have the required permission (se.gamemode.creative) to run this command.");
                        return true;
                    } else {
                        String permission = ServerEssentials.getPlugin().getConfig().getString("no-permission-message");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', permission));
                        return true;
                    }
                }
            }else if ((command.getName().equalsIgnoreCase("gms")) && args.length == 0){
                if (player.hasPermission("se.gamemode.survival")) {
                    if (args.length == 1) {
                        Player targetPlayer = Bukkit.getServer().getPlayer(args[0]);
                        targetPlayer.setGameMode(GameMode.SURVIVAL);
                        targetPlayer.sendMessage(ChatColor.GREEN + "You are now in Survival");
                        player.sendMessage(ChatColor.GREEN + "Set " + ChatColor.GOLD + targetPlayer.getName() + ChatColor.GREEN + " into Survival");
                        return true;
                    } else if (args.length == 0) {
                        player.setGameMode(GameMode.SURVIVAL);
                        player.sendMessage(ChatColor.GREEN + "You are now in Survival");
                        return true;
                    }
                } else {
                    if (ServerEssentials.plugin.getConfig().getString("no-permission-message").length() == 0) {
                        player.sendMessage(ChatColor.RED + "You do not have the required permission (se.gamemode.survival) to run this command.");
                        return true;
                    } else {
                        String permission = ServerEssentials.getPlugin().getConfig().getString("no-permission-message");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', permission));
                        return true;
                    }
                }
            }else if ((command.getName().equalsIgnoreCase("gmsp")) && args.length == 0){
                if (player.hasPermission("se.gamemode.spectator")) {
                    if (args.length == 1) {
                        Player targetPlayer = Bukkit.getServer().getPlayer(args[0]);
                        targetPlayer.setGameMode(GameMode.SPECTATOR);
                        targetPlayer.sendMessage(ChatColor.GREEN + "You are now in Spectator");
                        player.sendMessage(ChatColor.GREEN + "Set " + ChatColor.GOLD + targetPlayer.getName() + ChatColor.GREEN + " into Spectator");
                        return true;
                    } else if (args.length == 0) {
                        player.setGameMode(GameMode.SPECTATOR);
                        player.sendMessage(ChatColor.GREEN + "You are now in Spectator");
                        return true;
                    }
                } else {
                    if (ServerEssentials.plugin.getConfig().getString("no-permission-message").length() == 0) {
                        player.sendMessage(ChatColor.RED + "You do not have the required permission (se.gamemode.spectator) to run this command.");
                        return true;
                    } else {
                        String permission = ServerEssentials.getPlugin().getConfig().getString("no-permission-message");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', permission));
                        return true;
                    }
                }
            }else if ((command.getName().equalsIgnoreCase("gma")) && args.length == 0){
                if (player.hasPermission("se.gamemode.adventure")) {
                    if (args.length == 1) {
                        Player targetPlayer = Bukkit.getServer().getPlayer(args[0]);
                        targetPlayer.setGameMode(GameMode.ADVENTURE);
                        targetPlayer.sendMessage(ChatColor.GREEN + "You are now in Adventure");
                        player.sendMessage(ChatColor.GREEN + "Set " + ChatColor.GOLD + targetPlayer.getName() + ChatColor.GREEN + " into Adventure");
                        return true;
                    } else if (args.length == 0) {
                        player.setGameMode(GameMode.ADVENTURE);
                        player.sendMessage(ChatColor.GREEN + "You are now in Adventure");
                        return true;
                    }
                } else {
                    if (ServerEssentials.plugin.getConfig().getString("no-permission-message").length() == 0) {
                        player.sendMessage(ChatColor.RED + "You do not have the required permission (se.gamemode.adventure) to run this command.");
                        return true;
                    } else {
                        String permission = ServerEssentials.getPlugin().getConfig().getString("no-permission-message");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', permission));
                        return true;
                    }
                }
            }
        }else if (sender instanceof ConsoleCommandSender){
            if ((command.getName().equalsIgnoreCase("gamemode") || command.getName().equalsIgnoreCase("gm")) && args.length == 2) {
                if (args[0].equalsIgnoreCase("creative") || args[0].equalsIgnoreCase("c") || args[0].equalsIgnoreCase("1")) {
                    if (args.length == 2) {
                        Player targetPlayer = Bukkit.getServer().getPlayer(args[1]);
                        targetPlayer.setGameMode(GameMode.CREATIVE);
                        targetPlayer.sendMessage(ChatColor.GREEN + "You are now in Creative");
                        System.out.println(ChatColor.GREEN + "Set " + ChatColor.WHITE + targetPlayer.getDisplayName() + ChatColor.GREEN + " into Creative");
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("survival") || args[0].equalsIgnoreCase("s") || args[0].equalsIgnoreCase("0")) {
                    if (args.length == 2) {
                        Player targetPlayer = Bukkit.getServer().getPlayer(args[1]);
                        targetPlayer.setGameMode(GameMode.SURVIVAL);
                        targetPlayer.sendMessage(ChatColor.GREEN + "You are now in Survival");
                        System.out.println(ChatColor.GREEN + "Set " + ChatColor.WHITE + targetPlayer.getDisplayName() + ChatColor.GREEN + " into Survival");
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("spectator") || args[0].equalsIgnoreCase("3")) {
                    if (args.length == 2) {
                        Player targetPlayer = Bukkit.getServer().getPlayer(args[1]);
                        targetPlayer.setGameMode(GameMode.SPECTATOR);
                        targetPlayer.sendMessage(ChatColor.GREEN + "You are now in Spectator");
                        System.out.println(ChatColor.GREEN + "Set " + ChatColor.WHITE + targetPlayer.getDisplayName() + ChatColor.GREEN + " into Spectator");
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("adventure") || args[0].equalsIgnoreCase("a") || args[0].equalsIgnoreCase("2")) {
                    if (args.length == 2) {
                        Player targetPlayer = Bukkit.getServer().getPlayer(args[1]);
                        targetPlayer.setGameMode(GameMode.ADVENTURE);
                        targetPlayer.sendMessage(ChatColor.GREEN + "You are now in Adventure");
                        System.out.println(ChatColor.GREEN + "Set " + ChatColor.WHITE + targetPlayer.getDisplayName() + ChatColor.GREEN + " into Adventure");
                        return true;
                    }
                }
            }else{
                System.out.println(ChatColor.RED + "Incorrect format! Please use /gamemode <mode> <player>");
                return true;
            }
        }
        return false;
    }
    public void gmc(String[] args, Player player) {
        if (player.hasPermission("se.gamemode.creative")) {
            if (args.length == 2) {
                Player targetPlayer = Bukkit.getServer().getPlayer(args[1]);
                targetPlayer.setGameMode(GameMode.CREATIVE);
                targetPlayer.sendMessage(ChatColor.GREEN + "You are now in Creative");
                player.sendMessage(ChatColor.GREEN + "Set " + ChatColor.GOLD + targetPlayer.getName() + ChatColor.GREEN + " into Creative");
            } else if (args.length == 1) {
                player.setGameMode(GameMode.CREATIVE);
                player.sendMessage(ChatColor.GREEN + "You are now in Creative");
            }
        } else {
            if (ServerEssentials.plugin.getConfig().getString("no-permission-message").length() == 0) {
                player.sendMessage(ChatColor.RED + "You do not have the required permission (se.gamemode.creative) to run this command.");
            } else {
                String permission = ServerEssentials.getPlugin().getConfig().getString("no-permission-message");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', permission));
            }
        }
    }
    public void gms(String[] args, Player player) {
        if (player.hasPermission("se.gamemode.survival")) {
            if (args.length == 2) {
                Player targetPlayer = Bukkit.getServer().getPlayer(args[1]);
                targetPlayer.setGameMode(GameMode.SURVIVAL);
                targetPlayer.sendMessage(ChatColor.GREEN + "You are now in Survival");
                player.sendMessage(ChatColor.GREEN + "Set " + ChatColor.GOLD + targetPlayer.getName() + ChatColor.GREEN + " into Survival");
            } else if (args.length == 1) {
                player.setGameMode(GameMode.SURVIVAL);
                player.sendMessage(ChatColor.GREEN + "You are now in Survival");
            }
        } else {
            if (ServerEssentials.plugin.getConfig().getString("no-permission-message").length() == 0) {
                player.sendMessage(ChatColor.RED + "You do not have the required permission (se.gamemode.survival) to run this command.");
            } else {
                String permission = ServerEssentials.getPlugin().getConfig().getString("no-permission-message");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', permission));
            }
        }
    }
    public void gmsp(String[] args, Player player) {
        if (player.hasPermission("se.gamemode.spectator")) {
            if (args.length == 2) {
                Player targetPlayer = Bukkit.getServer().getPlayer(args[1]);
                targetPlayer.setGameMode(GameMode.SPECTATOR);
                targetPlayer.sendMessage(ChatColor.GREEN + "You are now in Spectator");
                player.sendMessage(ChatColor.GREEN + "Set " + ChatColor.GOLD + targetPlayer.getName() + ChatColor.GREEN + " into Spectator");
            } else if (args.length == 1) {
                player.setGameMode(GameMode.SPECTATOR);
                player.sendMessage(ChatColor.GREEN + "You are now in Spectator");
            }
        } else {
            if (ServerEssentials.plugin.getConfig().getString("no-permission-message").length() == 0) {
                player.sendMessage(ChatColor.RED + "You do not have the required permission (se.gamemode.spectator) to run this command.");
            } else {
                String permission = ServerEssentials.getPlugin().getConfig().getString("no-permission-message");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', permission));
            }
        }
    }
    public void gma(String[] args, Player player) {
        if (player.hasPermission("se.gamemode.adventure")) {
            if (args.length == 2) {
                Player targetPlayer = Bukkit.getServer().getPlayer(args[1]);
                targetPlayer.setGameMode(GameMode.ADVENTURE);
                targetPlayer.sendMessage(ChatColor.GREEN + "You are now in Adventure");
                player.sendMessage(ChatColor.GREEN + "Set " + ChatColor.GOLD + targetPlayer.getName() + ChatColor.GREEN + " into Adventure");
            } else if (args.length == 1) {
                player.setGameMode(GameMode.ADVENTURE);
                player.sendMessage(ChatColor.GREEN + "You are now in Adventure");
            }
        } else {
            if (ServerEssentials.plugin.getConfig().getString("no-permission-message").length() == 0) {
                player.sendMessage(ChatColor.RED + "You do not have the required permission (se.gamemode.adventure) to run this command.");
            } else {
                String permission = ServerEssentials.getPlugin().getConfig().getString("no-permission-message");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', permission));
            }
        }
    }
}