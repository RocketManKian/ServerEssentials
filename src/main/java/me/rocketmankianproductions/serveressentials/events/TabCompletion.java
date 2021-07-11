package me.rocketmankianproductions.serveressentials.events;

import me.rocketmankianproductions.serveressentials.commands.Sethome;
import me.rocketmankianproductions.serveressentials.commands.Setwarp;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TabCompletion implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("warp")) { // checking if my command is the one i'm after

            List<String> autoCompletes = new ArrayList<>(); //create a new string list for tab completion

            if (args.length == 1) { //only interested in the first sub command, if you wanted to cover more deeper sub commands, you could have multiple if statements or a switch statement
                Player player = (Player) sender;
                if (Setwarp.fileConfig.getStringList("Warp.") != null){
                    ConfigurationSection warps = Setwarp.fileConfig.getConfigurationSection("Warp.");
                    for (String warp: warps.getKeys(false)){
                        if (player.hasPermission("se.warps." + warp)){
                            autoCompletes.add(warp);
                        }
                    }
                }
                return autoCompletes; // then return the list
            }
        }
        if (command.getName().equalsIgnoreCase("home")){
            List<String> autoCompletes = new ArrayList<>();

            if (args.length == 1) {
                Player player = (Player) sender;
                if (Sethome.fileConfig.getStringList("Home." + player.getUniqueId()) != null){
                    ConfigurationSection home = Sethome.fileConfig.getConfigurationSection("Home." + player.getUniqueId());
                    for (String playerhomes: home.getKeys(false)){
                        if (player.hasPermission("se.warps." + playerhomes)){
                            autoCompletes.add(playerhomes);
                        }
                    }
                }
                return autoCompletes; // then return the list
            }
        }
        return null; // this will return nothing if it wasn't the disguise command I have
    }
}
