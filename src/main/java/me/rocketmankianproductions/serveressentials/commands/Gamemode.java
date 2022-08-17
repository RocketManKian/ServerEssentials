package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

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
                }else{
                    String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/gm <survival/creative/spectator/adventure>");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    return true;
                }
            }else if ((command.getName().equalsIgnoreCase("gmc"))){
                if (ServerEssentials.permissionChecker(player, "se.gamemode.creative")) {
                    if (args.length == 1) {
                        Player targetPlayer = Bukkit.getServer().getPlayer(args[0]);
                        targetPlayer.setGameMode(GameMode.CREATIVE);
                        String msg = Lang.fileConfig.getString("gamemode-creative-target").replace("<target>", targetPlayer.getName());
                        targetPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        String msg2 = Lang.fileConfig.getString("gamemode-creative-self");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg2));
                        return true;
                    } else if (args.length == 0) {
                        player.setGameMode(GameMode.CREATIVE);
                        String msg = Lang.fileConfig.getString("gamemode-creative-self");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    }else{
                        String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/gmc (player)");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    }
                }
            }else if ((command.getName().equalsIgnoreCase("gms"))){
                if (ServerEssentials.permissionChecker(player, "se.gamemode.survival")) {
                    if (args.length == 1) {
                        Player targetPlayer = Bukkit.getServer().getPlayer(args[0]);
                        targetPlayer.setGameMode(GameMode.SURVIVAL);
                        String msg = Lang.fileConfig.getString("gamemode-survival-target").replace("<target>", targetPlayer.getName());
                        targetPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        String msg2 = Lang.fileConfig.getString("gamemode-survival-self");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg2));
                        return true;
                    } else if (args.length == 0) {
                        player.setGameMode(GameMode.SURVIVAL);
                        String msg = Lang.fileConfig.getString("gamemode-survival-self");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    }else{
                        String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/gms (player)");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    }
                }
            }else if ((command.getName().equalsIgnoreCase("gmsp"))){
                if (ServerEssentials.permissionChecker(player, "se.gamemode.spectator")) {
                    if (args.length == 1) {
                        Player targetPlayer = Bukkit.getServer().getPlayer(args[0]);
                        targetPlayer.setGameMode(GameMode.SPECTATOR);
                        String msg = Lang.fileConfig.getString("gamemode-spectator-target").replace("<target>", targetPlayer.getName());
                        targetPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        String msg2 = Lang.fileConfig.getString("gamemode-spectator-self");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg2));
                        return true;
                    } else if (args.length == 0) {
                        player.setGameMode(GameMode.SPECTATOR);
                        String msg = Lang.fileConfig.getString("gamemode-spectator-self");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    }else{
                        String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/gmsp (player)");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    }
                }
            }else if ((command.getName().equalsIgnoreCase("gma"))){
                if (ServerEssentials.permissionChecker(player, "se.gamemode.adventure")) {
                    if (args.length == 1) {
                        Player targetPlayer = Bukkit.getServer().getPlayer(args[0]);
                        targetPlayer.setGameMode(GameMode.ADVENTURE);
                        String msg = Lang.fileConfig.getString("gamemode-adventure-target").replace("<target>", targetPlayer.getName());
                        targetPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        String msg2 = Lang.fileConfig.getString("gamemode-adventure-self");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg2));
                        return true;
                    } else if (args.length == 0) {
                        player.setGameMode(GameMode.ADVENTURE);
                        String msg = Lang.fileConfig.getString("gamemode-adventure-self");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    }else{
                        String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/gma (player)");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    }
                }
            }else{
                String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/gm <survival/creative/spectator/adventure>");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                return true;
            }
        }else if (sender instanceof ConsoleCommandSender) {
            if ((command.getName().equalsIgnoreCase("gamemode") || command.getName().equalsIgnoreCase("gm"))) {
                if (args[0].equalsIgnoreCase("creative") || args[0].equalsIgnoreCase("c") || args[0].equalsIgnoreCase("1")) {
                    if (args.length == 2) {
                        Player targetPlayer = Bukkit.getServer().getPlayer(args[1]);
                        targetPlayer.setGameMode(GameMode.CREATIVE);
                        String msg = Lang.fileConfig.getString("gamemode-creative-self");
                        targetPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        String msg2 = Lang.fileConfig.getString("gamemode-creative-target").replace("<target>", targetPlayer.getName());
                        Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', msg2));
                        return true;
                    }else{
                        String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/gamemode creative (player)");
                        Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("survival") || args[0].equalsIgnoreCase("s") || args[0].equalsIgnoreCase("0")) {
                    if (args.length == 2) {
                        Player targetPlayer = Bukkit.getServer().getPlayer(args[1]);
                        targetPlayer.setGameMode(GameMode.SURVIVAL);
                        String msg = Lang.fileConfig.getString("gamemode-survival-self");
                        targetPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        String msg2 = Lang.fileConfig.getString("gamemode-survival-target").replace("<target>", targetPlayer.getName());
                        Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', msg2));
                        return true;
                    }else{
                        String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/gamemode survival (player)");
                        Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("spectator") || args[0].equalsIgnoreCase("3")) {
                    if (args.length == 2) {
                        Player targetPlayer = Bukkit.getServer().getPlayer(args[1]);
                        targetPlayer.setGameMode(GameMode.SPECTATOR);
                        String msg = Lang.fileConfig.getString("gamemode-spectator-self");
                        targetPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        String msg2 = Lang.fileConfig.getString("gamemode-spectator-target").replace("<target>", targetPlayer.getName());
                        Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', msg2));
                        return true;
                    }else{
                        String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/gamemode spectator (player)");
                        Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("adventure") || args[0].equalsIgnoreCase("a") || args[0].equalsIgnoreCase("2")) {
                    if (args.length == 2) {
                        Player targetPlayer = Bukkit.getServer().getPlayer(args[1]);
                        targetPlayer.setGameMode(GameMode.ADVENTURE);
                        String msg = Lang.fileConfig.getString("gamemode-adventure-self");
                        targetPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        String msg2 = Lang.fileConfig.getString("gamemode-adventure-target").replace("<target>", targetPlayer.getName());
                        Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', msg2));
                        return true;
                    }else{
                        String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/gamemode adventure (player)");
                        Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    }
                }else{
                    String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/gm <survival/creative/spectator/adventure>");
                    Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', msg));
                    return true;
                }
            }else if ((command.getName().equalsIgnoreCase("gmc"))){
                if (args.length == 1) {
                    Player targetPlayer = Bukkit.getServer().getPlayer(args[0]);
                    targetPlayer.setGameMode(GameMode.CREATIVE);
                    String msg = Lang.fileConfig.getString("gamemode-creative-self");
                    targetPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    String msg2 = Lang.fileConfig.getString("gamemode-creative-target").replace("<target>", targetPlayer.getName());
                    Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', msg2));
                    return true;
                }else{
                    String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/gmc (player)");
                    Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', msg));
                    return true;
                }
            }else if ((command.getName().equalsIgnoreCase("gms"))) {
                if (args.length == 1) {
                    Player targetPlayer = Bukkit.getServer().getPlayer(args[0]);
                    targetPlayer.setGameMode(GameMode.SURVIVAL);
                    String msg = Lang.fileConfig.getString("gamemode-survival-self");
                    targetPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    String msg2 = Lang.fileConfig.getString("gamemode-survival-target").replace("<target>", targetPlayer.getName());
                    Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', msg2));
                    return true;
                }else{
                    String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/gms (player)");
                    Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', msg));
                    return true;
                }
            }else if ((command.getName().equalsIgnoreCase("gmsp"))) {
                if (args.length == 1) {
                    Player targetPlayer = Bukkit.getServer().getPlayer(args[0]);
                    targetPlayer.setGameMode(GameMode.SPECTATOR);
                    String msg = Lang.fileConfig.getString("gamemode-spectator-self");
                    targetPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    String msg2 = Lang.fileConfig.getString("gamemode-spectator-target").replace("<target>", targetPlayer.getName());
                    Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', msg2));
                    return true;
                }else{
                    String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/gmsp (player)");
                    Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', msg));
                    return true;
                }
            }else if ((command.getName().equalsIgnoreCase("gma"))) {
                if (args.length == 1) {
                    Player targetPlayer = Bukkit.getServer().getPlayer(args[0]);
                    targetPlayer.setGameMode(GameMode.ADVENTURE);
                    String msg = Lang.fileConfig.getString("gamemode-adventure-self");
                    targetPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    String msg2 = Lang.fileConfig.getString("gamemode-adventure-target").replace("<target>", targetPlayer.getName());
                    Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', msg2));
                    return true;
                }else{
                    String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/gma (player)");
                    Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', msg));
                    return true;
                }
            } else{
                String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/gm <survival/creative/spectator/adventure>");
                Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', msg));
                return true;
            }
        }
        return false;
    }
    public void gmc(String[] args, Player player) {
        if (ServerEssentials.permissionChecker(player, "se.gamemode.creative")) {
            if (args.length == 2) {
                Player targetPlayer = Bukkit.getServer().getPlayer(args[1]);
                if (targetPlayer != null){
                    targetPlayer.setGameMode(GameMode.CREATIVE);
                    String msg = Lang.fileConfig.getString("gamemode-creative-self");
                    targetPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    String msg2 = Lang.fileConfig.getString("gamemode-creative-target").replace("<target>", targetPlayer.getName());
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg2));
                }else{
                    String msg = Lang.fileConfig.getString("target-offline");
                    player.sendMessage(msg);
                }
            } else if (args.length == 1) {
                player.setGameMode(GameMode.CREATIVE);
                String msg = Lang.fileConfig.getString("gamemode-creative-self");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
            }else{
                String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/gamemode creative (player)");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
            }
        }
    }
    public void gms(String[] args, Player player) {
        if (ServerEssentials.permissionChecker(player, "se.gamemode.survival")) {
            if (args.length == 2) {
                Player targetPlayer = Bukkit.getServer().getPlayer(args[1]);
                if (targetPlayer != null){
                    targetPlayer.setGameMode(GameMode.SURVIVAL);
                    String msg = Lang.fileConfig.getString("gamemode-survival-self");
                    targetPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    String msg2 = Lang.fileConfig.getString("gamemode-survival-target").replace("<target>", targetPlayer.getName());
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg2));
                }else{
                    String msg = Lang.fileConfig.getString("target-offline");
                    player.sendMessage(msg);
                }
            } else if (args.length == 1) {
                player.setGameMode(GameMode.SURVIVAL);
                String msg = Lang.fileConfig.getString("gamemode-survival-self");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
            }else{
                String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/gamemode survival (player)");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
            }
        }
    }
    public void gmsp(String[] args, Player player) {
        if (ServerEssentials.permissionChecker(player, "se.gamemode.spectator")) {
            if (args.length == 2) {
                Player targetPlayer = Bukkit.getServer().getPlayer(args[1]);
                if (targetPlayer != null){
                    targetPlayer.setGameMode(GameMode.SPECTATOR);
                    String msg = Lang.fileConfig.getString("gamemode-spectator-self");
                    targetPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    String msg2 = Lang.fileConfig.getString("gamemode-spectator-target").replace("<target>", targetPlayer.getName());
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg2));
                }else{
                    String msg = Lang.fileConfig.getString("target-offline");
                    player.sendMessage(msg);
                }
            } else if (args.length == 1) {
                player.setGameMode(GameMode.SPECTATOR);
                String msg = Lang.fileConfig.getString("gamemode-spectator-self");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
            }else{
                String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/gamemode spectator (player)");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
            }
        }
    }
    public void gma(String[] args, Player player) {
        if (ServerEssentials.permissionChecker(player, "se.gamemode.adventure")) {
            if (args.length == 2) {
                Player targetPlayer = Bukkit.getServer().getPlayer(args[1]);
                if (targetPlayer != null){
                    targetPlayer.setGameMode(GameMode.ADVENTURE);
                    String msg = Lang.fileConfig.getString("gamemode-adventure-self");
                    targetPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    String msg2 = Lang.fileConfig.getString("gamemode-adventure-target").replace("<target>", targetPlayer.getName());
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg2));
                }else{
                    String msg = Lang.fileConfig.getString("target-offline");
                    player.sendMessage(msg);
                }
            } else if (args.length == 1) {
                player.setGameMode(GameMode.ADVENTURE);
                String msg = Lang.fileConfig.getString("gamemode-adventure-self");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
            }else{
                String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/gamemode adventure (player)");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
            }
        }
    }
}