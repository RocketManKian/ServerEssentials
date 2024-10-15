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

public class Time implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (ServerEssentials.permissionChecker(player, "se.time")) {
                if (command.getName().equalsIgnoreCase("time") && args.length >= 2) {
                    if (args[0].equalsIgnoreCase("set")) {
                        if (args[1].equalsIgnoreCase("sunrise") || args[1].equalsIgnoreCase("dawn") || args[1].equalsIgnoreCase("morning")) {
                            setTime(player, player.getWorld(), 23041, "time-sunrise");
                            return true;
                        } else if (args[1].equalsIgnoreCase("day")) {
                            setTime(player, player.getWorld(),1000, "time-day");
                            return true;
                        } else if (args[1].equalsIgnoreCase("sunset") || args[1].equalsIgnoreCase("dusk")) {
                            setTime(player, player.getWorld(),12610, "time-sunset");
                            return true;
                        } else if (args[1].equalsIgnoreCase("night")) {
                            setTime(player, player.getWorld(),13000, "time-night");
                            return true;
                        } else if (args[1].equalsIgnoreCase("midnight")) {
                            setTime(player, player.getWorld(),18000, "time-midnight");
                            return true;
                        } else {
                            try{
                                long time = Long.parseLong(args[1]);
                                setTime(player, player.getWorld(), time, "time-set");
                                return true;
                            }catch (NumberFormatException e){
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6" + args[1] + " &cis not a valid time."));
                                return false;
                            }
                        }
                    }else{
                        String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/time set <sunrise/day/sunset/night/midnight>");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                        return true;
                    }
                } else if (command.getName().equalsIgnoreCase("sunrise")) {
                    setTime(player, player.getWorld(), 23041, "time-sunrise");
                    return true;
                } else if (command.getName().equalsIgnoreCase("day")) {
                    setTime(player, player.getWorld(),1000, "time-day");
                    return true;
                } else if (command.getName().equalsIgnoreCase("sunset")) {
                    setTime(player, player.getWorld(),12610, "time-sunset");
                    return true;
                } else if (command.getName().equalsIgnoreCase("night")){
                    setTime(player, player.getWorld(),13000, "time-night");
                    return true;
                } else if (command.getName().equalsIgnoreCase("midnight")) {
                    setTime(player, player.getWorld(),18000, "time-midnight");
                    return true;
                }else{
                    String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/sunrise/day/sunset/night/midnight)");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                    return true;
                }
            }
        }else if (sender instanceof ConsoleCommandSender) {
            if (command.getName().equalsIgnoreCase("time") && args.length >= 2){
                if (args[0].equalsIgnoreCase("set")) {
                    if (args[1].equalsIgnoreCase("sunrise") || args[1].equalsIgnoreCase("dawn") || args[1].equalsIgnoreCase("morning")) {
                        setTime(sender, null, 23041, "time-sunrise");
                        return true;
                    } else if (args[1].equalsIgnoreCase("day")) {
                        setTime(sender, null,1000, "time-day");
                        return true;
                    } else if (args[1].equalsIgnoreCase("sunset") || args[1].equalsIgnoreCase("dusk")) {
                        setTime(sender, null,12610, "time-sunset");
                        return true;
                    } else if (args[1].equalsIgnoreCase("night")) {
                        setTime(sender, null,13000, "time-night");
                        return true;
                    } else if (args[1].equalsIgnoreCase("midnight")) {
                        setTime(sender, null,18000, "time-midnight");
                        return true;
                    } else {
                        try{
                            long time = Long.parseLong(args[1]);
                            setTime(Bukkit.getConsoleSender(), null, time, "time-set");
                            return true;
                        }catch (NumberFormatException e){
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6" + args[1] + " &cis not a valid time."));
                            return false;
                        }
                    }
                }else{
                    String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/time set <sunrise/day/sunset/night/midnight>");
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                    return true;
                }
            }else if (command.getName().equalsIgnoreCase("sunrise")) {
                setTime(sender, null, 23041, "time-sunrise");
                return true;
            } else if (command.getName().equalsIgnoreCase("day")) {
                setTime(sender, null,1000, "time-day");
                return true;
            } else if (command.getName().equalsIgnoreCase("sunset")) {
                setTime(sender, null,12610, "time-sunset");
                return true;
            } else if (command.getName().equalsIgnoreCase("night")){
                setTime(sender, null,13000, "time-night");
                return true;
            } else if (command.getName().equalsIgnoreCase("midnight")) {
                setTime(sender, null,18000, "time-midnight");
                return true;
            }else{
                String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/sunrise/day/sunset/night/midnight)");
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                return true;
            }
        } else if (sender instanceof BlockCommandSender){
            BlockCommandSender block = (BlockCommandSender) sender;
            if (command.getName().equalsIgnoreCase("time") && args.length >= 2) {
                if (args[0].equalsIgnoreCase("set")) {
                    if (args[1].equalsIgnoreCase("sunrise") || args[1].equalsIgnoreCase("dawn") || args[1].equalsIgnoreCase("morning")) {
                        setTime(block, block.getBlock().getWorld(), 23041, "time-sunrise");
                        return true;
                    } else if (args[1].equalsIgnoreCase("day")) {
                        setTime(block, block.getBlock().getWorld(),1000, "time-day");
                        return true;
                    } else if (args[1].equalsIgnoreCase("sunset") || args[1].equalsIgnoreCase("dusk")) {
                        setTime(block, block.getBlock().getWorld(),12610, "time-sunset");
                        return true;
                    } else if (args[1].equalsIgnoreCase("night")) {
                        setTime(block, block.getBlock().getWorld(),13000, "time-night");
                        return true;
                    } else if (args[1].equalsIgnoreCase("midnight")) {
                        setTime(block, block.getBlock().getWorld(),18000, "time-midnight");
                        return true;
                    } else {
                        try{
                            long time = Long.parseLong(args[1]);
                            setTime(block, block.getBlock().getWorld(), time, "time-set");
                            return true;
                        }catch (NumberFormatException e){
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6" + args[1] + " &cis not a valid time."));
                            return false;
                        }
                    }
                }else{
                    String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/time set <sunrise/day/sunset/night/midnight>");
                    block.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                    return true;
                }
            } else if (command.getName().equalsIgnoreCase("sunrise")) {
                setTime(block, block.getBlock().getWorld(), 23041, "time-sunrise");
                return true;
            } else if (command.getName().equalsIgnoreCase("day")) {
                setTime(block, block.getBlock().getWorld(),1000, "time-day");
                return true;
            } else if (command.getName().equalsIgnoreCase("sunset")) {
                setTime(block, block.getBlock().getWorld(),12610, "time-sunset");
                return true;
            } else if (command.getName().equalsIgnoreCase("night")){
                setTime(block, block.getBlock().getWorld(),13000, "time-night");
                return true;
            } else if (command.getName().equalsIgnoreCase("midnight")) {
                setTime(block, block.getBlock().getWorld(),18000, "time-midnight");
                return true;
            }else{
                String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/sunrise/day/sunset/night/midnight)");
                block.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                return true;
            }
        }
        return false;
    }

    public void setTime(CommandSender sender, World world, long time, String msg) {
        String message;
        if (world == null) {
            StringBuilder worldString = new StringBuilder();
            for (World allworld : Bukkit.getServer().getWorlds()) {
                if (worldString.length() > 0) {
                    worldString.append(", ");
                }
                worldString.append(allworld.getName());
                allworld.setTime(time);
            }
            message = Lang.fileConfig.getString(msg).replace("<world>", worldString.toString()).replace("<time>", String.valueOf(time));
        } else {
            sender.getServer().getWorld(world.getUID()).setTime(time);
            message = Lang.fileConfig.getString(msg).replace("<world>", world.getName()).replace("<time>", String.valueOf(time));
        }
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(message)));
    }
}
