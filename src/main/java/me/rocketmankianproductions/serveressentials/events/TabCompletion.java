package me.rocketmankianproductions.serveressentials.events;

import me.rocketmankianproductions.serveressentials.commands.Sethome;
import me.rocketmankianproductions.serveressentials.commands.Setwarp;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TabCompletion implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        // SE Commands
        if (command.getName().equalsIgnoreCase("se")) { // checking if my command is the one i'm after
            List<String> autoCompletes = new ArrayList<>(); //create a new string list for tab completion
            if (args.length == 1) { //only interested in the first sub command, if you wanted to cover more deeper sub commands, you could have multiple if statements or a switch statement
                Player player = (Player) sender;
                if (player.hasPermission("se.reload")){
                    autoCompletes.add("reload");
                }
                if (player.hasPermission("se.version")){
                    autoCompletes.add("version");
                }
                if (player.hasPermission("se.silentjoin")){
                    autoCompletes.add("silentjoin");
                }
                return autoCompletes; // then return the list
            }
        }
        // Test Commands
        if (command.getName().equalsIgnoreCase("test")) { // checking if my command is the one i'm after
            List<String> autoCompletes = new ArrayList<>(); //create a new string list for tab completion
            if (args.length == 1) { //only interested in the first sub command, if you wanted to cover more deeper sub commands, you could have multiple if statements or a switch statement
                Player player = (Player) sender;
                if (player.hasPermission("se.test")){
                    autoCompletes.add("join");
                    autoCompletes.add("leave");
                    autoCompletes.add("welcome");
                    autoCompletes.add("permission");
                    autoCompletes.add("motd");
                }
                return autoCompletes; // then return the list
            }
        }
        // Speed Command
        if (command.getName().equalsIgnoreCase("speed")) { // checking if my command is the one i'm after
            List<String> autoCompletes = new ArrayList<>(); //create a new string list for tab completion
            if (args.length == 1) { //only interested in the first sub command, if you wanted to cover more deeper sub commands, you could have multiple if statements or a switch statement
                Player player = (Player) sender;
                if (player.hasPermission("se.speed")){
                    autoCompletes.add("walk");
                    autoCompletes.add("fly");
                    autoCompletes.add("reset");
                }
                return autoCompletes; // then return the list
            }else if (args.length == 2){
                Player player = (Player) sender;
                if (player.hasPermission("se.speed")){
                    autoCompletes.add("walk");
                    autoCompletes.add("fly");
                }
                return autoCompletes; // then return the list
            }
        }
        // Spawn Command
        if (command.getName().equalsIgnoreCase("spawn")){
            List<String> autoCompletes = new ArrayList<>();
            if (args.length == 1){
                autoCompletes.add("newbies");
            }
            return autoCompletes;
        }
        // Set Spawn Command
        if (command.getName().equalsIgnoreCase("setspawn")){
            List<String> autoCompletes = new ArrayList<>();
            if (args.length == 1){
                autoCompletes.add("newbies");
            }
            return autoCompletes;
        }
        // Delete Spawn Command
        if (command.getName().equalsIgnoreCase("deletespawn")){
            List<String> autoCompletes = new ArrayList<>();
            if (args.length == 1){
                autoCompletes.add("newbies");
            }
            return autoCompletes;
        }
        // Warp Commands
        if (command.getName().equalsIgnoreCase("warp")) {
            List<String> autoCompletes = new ArrayList<>();
            Player player = (Player) sender;
            if (args.length == 1) {
                if (Setwarp.fileConfig.getStringList("Warp.") != null){
                    ConfigurationSection warps = Setwarp.fileConfig.getConfigurationSection("Warp.");
                    if (warps != null){
                        for (String warp: warps.getKeys(false)){
                            if (player.hasPermission("se.warps." + warp)){
                                autoCompletes.add(warp);
                            }
                            if (player.hasPermission("se.setwarp.block")){
                                autoCompletes.add("setblock");
                            }
                        }
                    }
                }
                return autoCompletes; // then return the list
            }else if (args.length == 2){
                ConfigurationSection warps = Setwarp.fileConfig.getConfigurationSection("Warp.");
                for (String warp: warps.getKeys(false)){
                    if (player.hasPermission("se.setwarp.block")){
                        autoCompletes.add(warp);
                    }
                }
                return autoCompletes; // then return the list
            }
        }
        // Send Warp Commands
        if (command.getName().equalsIgnoreCase("sendwarp")) {
            List<String> autoCompletes = new ArrayList<>();
            if (sender instanceof Player){
                if (args.length == 2) {
                    Player player = (Player) sender;
                    if (Setwarp.fileConfig.getStringList("Warp.") != null){
                        ConfigurationSection warps = Setwarp.fileConfig.getConfigurationSection("Warp.");
                        if (warps != null){
                            for (String warp: warps.getKeys(false)){
                                if (player.hasPermission("se.warps." + warp) && player.hasPermission("se.sendwarp")){
                                    autoCompletes.add(warp);
                                }
                            }
                        }
                    }
                    return autoCompletes; // then return the list
                }
            }else{
                if (args.length == 2) {
                    if (Setwarp.fileConfig.getStringList("Warp.") != null){
                        ConfigurationSection warps = Setwarp.fileConfig.getConfigurationSection("Warp.");
                        if (warps != null){
                            for (String warp: warps.getKeys(false)){
                                autoCompletes.add(warp);
                            }
                        }
                    }
                    return autoCompletes; // then return the list
                }
            }
        }
        if (command.getName().equalsIgnoreCase("deletewarp") || command.getName().equalsIgnoreCase("delwarp")) { // checking if my command is the one i'm after
            List<String> autoCompletes = new ArrayList<>(); //create a new string list for tab completion
            if (sender instanceof Player){
                if (args.length == 1) { //only interested in the first sub command, if you wanted to cover more deeper sub commands, you could have multiple if statements or a switch statement
                    Player player = (Player) sender;
                    if (Setwarp.fileConfig.getStringList("Warp.") != null){
                        ConfigurationSection warps = Setwarp.fileConfig.getConfigurationSection("Warp.");
                        if (warps != null){
                            for (String warp: warps.getKeys(false)){
                                if (player.hasPermission("se.deletewarp")){
                                    autoCompletes.add(warp);
                                }
                            }
                        }
                    }
                    return autoCompletes; // then return the list
                }
            }else{
                if (args.length == 1) { //only interested in the first sub command, if you wanted to cover more deeper sub commands, you could have multiple if statements or a switch statement
                    if (Setwarp.fileConfig.getStringList("Warp.") != null){
                        ConfigurationSection warps = Setwarp.fileConfig.getConfigurationSection("Warp.");
                        if (warps != null){
                            for (String warp: warps.getKeys(false)){
                                autoCompletes.add(warp);
                            }
                        }
                    }
                    return autoCompletes; // then return the list
                }
            }
        }
        // Home Commands
        if (command.getName().equalsIgnoreCase("home")){
            List<String> autoCompletes = new ArrayList<>();
            if (args.length == 1) {
                Player player = (Player) sender;
                if (Sethome.fileConfig.getStringList("Home." + player.getUniqueId()) != null){
                    ConfigurationSection home = Sethome.fileConfig.getConfigurationSection("Home." + player.getUniqueId());
                    if (home != null){
                        for (String playerhomes: home.getKeys(false)){
                            if (player.hasPermission("se.home") && !playerhomes.isEmpty()){
                                autoCompletes.add(playerhomes);
                                return autoCompletes;
                            }else{
                                return null;
                            }
                        }
                    }
                }
            }
            if (args.length == 2){
                Player player = (Player) sender;
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
                if (Sethome.fileConfig.getStringList("Home." + target.getUniqueId()) != null){
                    ConfigurationSection home = Sethome.fileConfig.getConfigurationSection("Home." + target.getUniqueId());
                    if (home != null){
                        for (String playerhomes: home.getKeys(false)){
                            if (player.hasPermission("se.home") && !playerhomes.isEmpty()){
                                autoCompletes.add(playerhomes);
                                return autoCompletes;
                            }else{
                                return null;
                            }
                        }
                    }
                }
            }
        }
        // Send Home Command
        if (command.getName().equalsIgnoreCase("sendhome")){
            List<String> autoCompletes = new ArrayList<>();
            if (sender instanceof Player){
                if (args.length == 2) {
                    Player player = (Player) sender;
                    Player target = Bukkit.getPlayer(args[1]);
                    UUID target2 = target.getUniqueId();
                    if (Sethome.fileConfig.getStringList("Home." + target2) != null){
                        ConfigurationSection home = Sethome.fileConfig.getConfigurationSection("Home." + target2);
                        if (home != null){
                            for (String playerhomes: home.getKeys(false)){
                                if (playerhomes != null){
                                    if (player.hasPermission("se.sendhome")){
                                        autoCompletes.add(playerhomes);
                                    }
                                }
                            }
                        }
                    }
                    return autoCompletes; // then return the list
                }
            }else{
                if (args.length == 2) {
                    Player target = Bukkit.getPlayer(args[1]);
                    UUID target2 = target.getUniqueId();
                    if (Sethome.fileConfig.getStringList("Home." + target2) != null){
                        ConfigurationSection home = Sethome.fileConfig.getConfigurationSection("Home." + target2);
                        if (home != null){
                            for (String playerhomes: home.getKeys(false)){
                                if (playerhomes != null){
                                    autoCompletes.add(playerhomes);
                                }
                            }
                        }
                    }
                    return autoCompletes; // then return the list
                }
            }
        }
        if (command.getName().equalsIgnoreCase("deletehome") || command.getName().equalsIgnoreCase("delhome")){
            List<String> autoCompletes = new ArrayList<>();
            if (args.length == 1) {
                Player player = (Player) sender;
                if (Sethome.fileConfig.getString("Home." + player.getUniqueId()) != null){
                    if (!Sethome.fileConfig.getString("Home." + player.getUniqueId()).isEmpty()){
                        ConfigurationSection home = Sethome.fileConfig.getConfigurationSection("Home." + player.getUniqueId());
                        if (home != null){
                            for (String playerhomes: home.getKeys(false)){
                                if (player.hasPermission("se.deletehome") && !playerhomes.isEmpty()){
                                    autoCompletes.add(playerhomes);
                                    return autoCompletes;
                                }else{
                                    return null;
                                }
                            }
                        }
                    }
                }
            }else if (args.length == 2){
                Player player = (Player) sender;
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
                if (Sethome.fileConfig.getString("Home." + target.getUniqueId()) != null){
                    if (!Sethome.fileConfig.getString("Home." + target.getUniqueId()).isEmpty()){
                        ConfigurationSection home = Sethome.fileConfig.getConfigurationSection("Home." + target.getUniqueId());
                        if (home != null){
                            for (String playerhomes: home.getKeys(false)){
                                if (player.hasPermission("se.deletehome") && !playerhomes.isEmpty()){
                                    autoCompletes.add(playerhomes);
                                    return autoCompletes;
                                }else{
                                    return null;
                                }
                            }
                        }
                    }
                }
            }
        }
        if (command.getName().equalsIgnoreCase("message") || command.getName().equalsIgnoreCase("msg")){
            List<String> autoCompletes = new ArrayList<>();
            if (args.length == 1){
                return null;
            }
            if (args.length == 2){
                autoCompletes.add("<message>");
            }
            return autoCompletes;
        }
        if (command.getName().equalsIgnoreCase("reply") || command.getName().equalsIgnoreCase("r")){
            List<String> autoCompletes = new ArrayList<>();
            if (args.length == 1){
                autoCompletes.add("<message>");
            }
            return autoCompletes;
        }
        if (command.getName().equalsIgnoreCase("reportbug")){
            List<String> autoCompletes = new ArrayList<>();
            if (args.length == 1){
                autoCompletes.add("<bug>");
            }
            return autoCompletes;
        }
        return null;
    }
}