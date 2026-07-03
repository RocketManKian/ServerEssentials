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
                if (command.getName().equalsIgnoreCase("time")) {
                    if (args.length == 0){
                        long ticks = player.getWorld().getTime();
                        String msg = Lang.fileConfig.getString("time-message").replace("<world>", player.getWorld().getName()).replace("<24time>", convertTicksTo24Time(ticks)).replace("<12time>", convertTicksTo12Time(ticks)).replace("<time>", String.valueOf(ticks));
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                        return true;
                    }else if (args.length >= 2){
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
                                try {
                                    if (args[1].contains(":")) {
                                        String[] timeParts = args[1].split(":");
                                        if (timeParts.length != 2) {
                                            throw new IllegalArgumentException("Invalid time format. Use HH:mm.");
                                        }

                                        int hour = Integer.parseInt(timeParts[0]);
                                        int minute = Integer.parseInt(timeParts[1]);

                                        // Validate the hour and minute ranges
                                        if (hour < 0 || hour > 23 || minute < 0 || minute > 59) {
                                            throw new IllegalArgumentException("Invalid time format. Hour should be 0-23 and minute 0-59.");
                                        }

                                        // Convert the 24-hour time to Minecraft ticks
                                        long timeInTicks = convertTimeToTicks(hour, minute);
                                        setTime(player, player.getWorld(), timeInTicks, "time-set");
                                    } else {
                                        // Assume it's a number (ticks)
                                        long time = Long.parseLong(args[1]);
                                        setTime(player, player.getWorld(), time, "time-set");
                                    }
                                    return true;
                                } catch (NumberFormatException e) {
                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6" + args[1] + " &cis not a valid number or time format."));
                                    return false;
                                } catch (IllegalArgumentException e) {
                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + e.getMessage()));
                                    return false;
                                }
                            }
                        }else{
                            String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/time set <sunrise/day/sunset/night/midnight>");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                            return true;
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
            if (command.getName().equalsIgnoreCase("time")){
                if (args.length == 1){
                    if (Bukkit.getWorld(args[0]) != null){
                        long ticks = Bukkit.getWorld(args[0]).getTime();
                        String msg = Lang.fileConfig.getString("time-message").replace("<world>", args[0]).replace("<24time>", convertTicksTo24Time(ticks)).replace("<12time>", convertTicksTo12Time(ticks)).replace("<time>", String.valueOf(ticks));
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                        return true;
                    }else{
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6World is &cinvalid"));
                        return true;
                    }
                }else if (args.length >= 2){
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
                            try {
                                if (args[1].contains(":")) {
                                    String[] timeParts = args[1].split(":");
                                    if (timeParts.length != 2) {
                                        throw new IllegalArgumentException("Invalid time format. Use HH:mm.");
                                    }

                                    int hour = Integer.parseInt(timeParts[0]);
                                    int minute = Integer.parseInt(timeParts[1]);

                                    // Validate the hour and minute ranges
                                    if (hour < 0 || hour > 23 || minute < 0 || minute > 59) {
                                        throw new IllegalArgumentException("Invalid time format. Hour should be 0-23 and minute 0-59.");
                                    }

                                    // Convert the 24-hour time to Minecraft ticks
                                    long timeInTicks = convertTimeToTicks(hour, minute);
                                    setTime(sender, null, timeInTicks, "time-set");
                                } else {
                                    // Assume it's a number (ticks)
                                    long time = Long.parseLong(args[1]);
                                    setTime(sender, null, time, "time-set");
                                }
                                return true;
                            } catch (NumberFormatException e) {
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6" + args[1] + " &cis not a valid number or time format."));
                                return false;
                            } catch (IllegalArgumentException e) {
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + e.getMessage()));
                                return false;
                            }
                        }
                    }else{
                        String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/time set <sunrise/day/sunset/night/midnight>");
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                        return true;
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
            if (command.getName().equalsIgnoreCase("time")) {
                if (args.length == 0){
                    long ticks = block.getBlock().getWorld().getTime();
                    String msg = Lang.fileConfig.getString("time-message").replace("<world>", block.getBlock().getWorld().getName()).replace("<24time>", convertTicksTo24Time(ticks)).replace("<12time>", convertTicksTo12Time(ticks)).replace("<time>", String.valueOf(ticks));
                    block.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                    return true;
                }else if (args.length >= 2){
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
                            try {
                                if (args[1].contains(":")) {
                                    String[] timeParts = args[1].split(":");
                                    if (timeParts.length != 2) {
                                        throw new IllegalArgumentException("Invalid time format. Use HH:mm.");
                                    }

                                    int hour = Integer.parseInt(timeParts[0]);
                                    int minute = Integer.parseInt(timeParts[1]);

                                    // Validate the hour and minute ranges
                                    if (hour < 0 || hour > 23 || minute < 0 || minute > 59) {
                                        throw new IllegalArgumentException("Invalid time format. Hour should be 0-23 and minute 0-59.");
                                    }

                                    // Convert the 24-hour time to Minecraft ticks
                                    long timeInTicks = convertTimeToTicks(hour, minute);
                                    setTime(block, block.getBlock().getWorld(), timeInTicks, "time-set");
                                } else {
                                    // Assume it's a number (ticks)
                                    long time = Long.parseLong(args[1]);
                                    setTime(block, block.getBlock().getWorld(), time, "time-set");
                                }
                                return true;
                            } catch (NumberFormatException e) {
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6" + args[1] + " &cis not a valid number or time format."));
                                return false;
                            } catch (IllegalArgumentException e) {
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + e.getMessage()));
                                return false;
                            }
                        }
                    }else{
                        String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/time set <sunrise/day/sunset/night/midnight>");
                        block.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                        return true;
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

    public static String convertTicksTo24Time(long ticks) {
        // In-game time starts at 0 ticks (6:00 AM) and resets every 24000 ticks.
        ticks = ticks % 24000; // Wrap the ticks within the 24-hour cycle

        // Convert ticks to in-game hours and minutes
        int hour24 = (int)((ticks / 1000) + 6) % 24;  // +6 to adjust for 6:00 AM start
        int minute = (int)((ticks % 1000) / 16.66);   // 1000 ticks per hour, so divide to get minutes

        // Create a 24-hour time string
        String time24Hour = String.format("%02d:%02d", hour24, minute);
        return time24Hour;
    }

    public static String convertTicksTo12Time(long ticks) {
        // In-game time starts at 0 ticks (6:00 AM) and resets every 24000 ticks.
        ticks = ticks % 24000; // Wrap the ticks within the 24-hour cycle

        // Convert ticks to in-game hours and minutes
        int hour24 = (int)((ticks / 1000) + 6) % 24;  // +6 to adjust for 6:00 AM start
        int minute = (int)((ticks % 1000) / 16.66);   // 1000 ticks per hour, so divide to get minutes

        // Create a 24-hour time string
        String time24Hour = String.format("%02d:%02d", hour24, minute);

        // Convert to 12-hour format
        int hour12 = hour24 % 12;
        if (hour12 == 0) hour12 = 12;  // Adjust for 12 AM/PM
        String period = hour24 < 12 ? "AM" : "PM";

        // Create a 12-hour time string
        String time12Hour = String.format("%02d:%02d %s", hour12, minute, period);

        // Return both formats for display
        return time12Hour;
    }

    public static long convertTimeToTicks(int hour, int minute) {
        // Handle wrap-around for hours less than 6 (before 6:00 AM)
        if (hour < 6) {
            hour += 24; // Wrap around to the next day
        }

        // Convert hours and minutes to ticks
        long ticks = ((hour - 6) * 1000) + (long)(minute * 16.66);

        // Wrap ticks within the 24,000 tick cycle (one in-game day)
        ticks = ticks % 24000;

        return ticks;
    }
}