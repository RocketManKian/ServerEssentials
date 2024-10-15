package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static me.rocketmankianproductions.serveressentials.ServerEssentials.hex;

public class Weather implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (commandSender instanceof Player){
            Player player = (Player) commandSender;
            if (ServerEssentials.permissionChecker(player, "se.weather")) {
                if (command.getName().equalsIgnoreCase("weather") && args.length == 1){
                    if (args[0].equalsIgnoreCase("sun")){
                        setWeather(player, player.getWorld(),false, "weather-sun");
                        return true;
                    }else if (args[0].equalsIgnoreCase("storm") || args[0].equalsIgnoreCase("thunder")){
                        setWeather(player, player.getWorld(),true, "weather-storm");
                        return true;
                    }
                }else if (command.getName().equalsIgnoreCase("sun")){
                    setWeather(player, player.getWorld(),false, "weather-sun");
                    return true;
                }else if (command.getName().equalsIgnoreCase("storm")){
                    setWeather(player, player.getWorld(),true, "weather-storm");
                    return true;
                }else{
                    String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/weather <sun/storm>");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                    return true;
                }
            }
        }else if (commandSender instanceof ConsoleCommandSender){
            if (command.getName().equalsIgnoreCase("weather") && args.length == 1){
                if (args[0].equalsIgnoreCase("sun")){
                    setWeather(commandSender, null,false, "weather-sun");
                    return true;
                }else if (args[0].equalsIgnoreCase("storm") || args[0].equalsIgnoreCase("thunder")){
                    setWeather(commandSender, null,true, "weather-storm");
                    return true;
                }
            }else if (command.getName().equalsIgnoreCase("sun")){
                setWeather(commandSender, null,false, "weather-sun");
                return true;
            }else if (command.getName().equalsIgnoreCase("storm")){
                setWeather(commandSender, null,true, "weather-storm");
                return true;
            }else{
                String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/weather <sun/storm>");
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                return true;
            }
        }else if (commandSender instanceof BlockCommandSender){
            BlockCommandSender block = (BlockCommandSender) commandSender;
            if (command.getName().equalsIgnoreCase("weather") && args.length == 1){
                if (args[0].equalsIgnoreCase("sun")){
                    setWeather(block, block.getBlock().getWorld(),false, "weather-sun");
                    return true;
                }else if (args[0].equalsIgnoreCase("storm") || args[0].equalsIgnoreCase("thunder")){
                    setWeather(block, block.getBlock().getWorld(),true, "weather-storm");
                    return true;
                }
            }else if (command.getName().equalsIgnoreCase("sun")){
                setWeather(block, block.getBlock().getWorld(),false, "weather-sun");
                return true;
            }else if (command.getName().equalsIgnoreCase("storm")){
                setWeather(block, block.getBlock().getWorld(),true, "weather-storm");
                return true;
            }else{
                String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/weather <sun/storm>");
                block.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                return true;
            }
        }
        return false;
    }

    public void setWeather(CommandSender sender, World world, boolean weather, String msg){
        String message;
        if (world == null){
            StringBuilder worldString = new StringBuilder();
            for (World allworld : Bukkit.getServer().getWorlds()) {
                if (worldString.length() > 0){
                    worldString.append(", ");
                }
                worldString.append(allworld.getName());
                allworld.setStorm(weather);
            }
            message = Lang.fileConfig.getString(msg).replace("<world>", worldString.toString());
        }else{
            sender.getServer().getWorld(world.getUID()).setStorm(weather);
            message = Lang.fileConfig.getString(msg).replace("<world>", world.getName());
        }
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(message)));
    }
}
