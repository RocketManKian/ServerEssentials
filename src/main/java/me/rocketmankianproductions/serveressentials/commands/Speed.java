package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Speed implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            if (player.hasPermission("se.speed") || player.hasPermission("se.all")){
                if (args.length == 1){
                    if (args[0].equalsIgnoreCase("reset")){
                        player.setFlySpeed((float)0.1);
                        player.setWalkSpeed((float)0.2);
                        String msg = Lang.fileConfig.getString("speed-reset-success");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    }else{
                        int speed;
                        try {
                            speed = Integer.parseInt(args[0]);
                        } catch (NumberFormatException e){
                            String msg = Lang.fileConfig.getString("speed-invalid-number");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                            return true;
                        }
                        if (speed <1 || speed > 10){
                            String msg = Lang.fileConfig.getString("speed-invalid-number");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                            return true;
                        }
                        if (player.isFlying()){
                            player.setFlySpeed((float) speed / 10);
                            String msg = Lang.fileConfig.getString("speed-fly-success").replace("<speed>", String.valueOf((float)speed));
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                            return true;
                        }else if (!player.isFlying()){
                            player.setWalkSpeed((float) speed / 10);
                            String msg = Lang.fileConfig.getString("speed-walk-success").replace("<speed>", String.valueOf((float)speed));
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                            return true;
                        }
                    }
                }else if (args.length == 2){
                    if (args[0].equalsIgnoreCase("walk")){
                        int speed;
                        try {
                            speed = Integer.parseInt(args[1]);
                        } catch (NumberFormatException e){
                            String msg = Lang.fileConfig.getString("speed-invalid-number");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                            return true;
                        }
                        if (speed <1 || speed > 10){
                            String msg = Lang.fileConfig.getString("speed-invalid-number");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                            return true;
                        }
                        player.setWalkSpeed((float) speed / 10);
                        String msg = Lang.fileConfig.getString("speed-walk-success").replace("<speed>", String.valueOf((float)speed));
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    }else if (args[0].equalsIgnoreCase("fly")) {
                        int speed;
                        try {
                            speed = Integer.parseInt(args[1]);
                        } catch (NumberFormatException e) {
                            String msg = Lang.fileConfig.getString("speed-invalid-number");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                            return true;
                        }
                        if (speed < 1 || speed > 10) {
                            String msg = Lang.fileConfig.getString("speed-invalid-number");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                            return true;
                        }
                        player.setFlySpeed((float) speed / 10);
                        String msg = Lang.fileConfig.getString("speed-fly-success").replace("<speed>", String.valueOf((float) speed));
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    }else if (args[0].equalsIgnoreCase("reset") && args[1].equalsIgnoreCase("fly")) {
                        player.setFlySpeed((float)0.1);
                        String msg = Lang.fileConfig.getString("speed-reset-fly-success");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    }else if (args[0].equalsIgnoreCase("reset") && args[1].equalsIgnoreCase("walk")){
                        player.setWalkSpeed((float)0.2);
                        String msg = Lang.fileConfig.getString("speed-reset-walk-success");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    }else if (args[0].equalsIgnoreCase("reset") && player.hasPermission("se.speed.others")){
                        Player target = Bukkit.getPlayer(args[1]);
                        if (target != null){
                            if (target == player){
                                player.setFlySpeed((float)0.1);
                                player.setWalkSpeed((float)0.2);
                                String msg2 = Lang.fileConfig.getString("speed-reset-success");
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg2));
                                return true;
                            }else{
                                String targetname = target.getName();
                                target.setFlySpeed((float)0.1);
                                target.setWalkSpeed((float)0.2);
                                String msg = Lang.fileConfig.getString("speed-reset-success-target").replace("<target>", targetname);
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                String msg2 = Lang.fileConfig.getString("speed-reset-success");
                                target.sendMessage(ChatColor.translateAlternateColorCodes('&', msg2));
                                return true;
                            }
                        }else{
                            String msg = Lang.fileConfig.getString("target-offline");
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                            return true;
                        }
                    }
                }else if (args.length == 3){
                    if (player.hasPermission("se.speed.others")){
                        if (args[0].equalsIgnoreCase("walk")){
                            Player target = Bukkit.getPlayer(args[1]);
                            if (target != null){
                                String targetname = target.getName();
                                int speed;
                                try {
                                    speed = Integer.parseInt(args[2]);
                                } catch (NumberFormatException e){
                                    String msg = Lang.fileConfig.getString("speed-invalid-number");
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                    return true;
                                }
                                if (speed <1 || speed > 10){
                                    String msg = Lang.fileConfig.getString("speed-invalid-number");
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                    return true;
                                }
                                target.setWalkSpeed((float) speed / 10);
                                String msg = Lang.fileConfig.getString("speed-walk-success-target").replace("<target>", targetname).replace("<speed>", String.valueOf((float)speed));
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                String msg2 = Lang.fileConfig.getString("speed-walk-success").replace("<speed>", String.valueOf((float)speed));
                                target.sendMessage(ChatColor.translateAlternateColorCodes('&', msg2));
                                return true;
                            }else{
                                String msg = Lang.fileConfig.getString("target-offline");
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            }
                        }else if (args[0].equalsIgnoreCase("fly")) {
                            Player target = Bukkit.getPlayer(args[1]);
                            if (target != null) {
                                String targetname = target.getName();
                                int speed;
                                try {
                                    speed = Integer.parseInt(args[2]);
                                } catch (NumberFormatException e) {
                                    String msg = Lang.fileConfig.getString("speed-invalid-number");
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                    return true;
                                }
                                if (speed < 1 || speed > 10) {
                                    String msg = Lang.fileConfig.getString("speed-invalid-number");
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                    return true;
                                }
                                target.setFlySpeed((float) speed / 10);
                                String msg = Lang.fileConfig.getString("speed-fly-success-target").replace("<target>", targetname).replace("<speed>", String.valueOf((float) speed));
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                String msg2 = Lang.fileConfig.getString("speed-fly-success").replace("<speed>", String.valueOf((float) speed));
                                target.sendMessage(ChatColor.translateAlternateColorCodes('&', msg2));
                                return true;
                            } else {
                                String msg = Lang.fileConfig.getString("target-offline");
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            }
                        }
                    }else{
                        String perm = Lang.fileConfig.getString("no-permission-message").replace("<permission>", "se.speed.others");
                        player.sendMessage("hi");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', perm));
                        return true;
                    }
                }else{
                    String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/speed (fly/walk) <speed>");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    return true;
                }
            }else{
                String perm = Lang.fileConfig.getString("no-permission-message").replace("<permission>", "se.speed");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', perm));
                return true;
            }
        }else{
            String console = Lang.fileConfig.getString("console-invalid");
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', console));
            return true;
        }
        return false;
    }
}
