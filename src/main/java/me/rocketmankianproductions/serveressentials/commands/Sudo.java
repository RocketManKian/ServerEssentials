package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.help.HelpTopic;
import org.jetbrains.annotations.NotNull;

public class Sudo implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            if (ServerEssentials.permissionChecker(player, "se.sudo")){
                if (args.length >= 2){
                    String cmdname = args[1];
                    String cmdargs = "";
                    boolean isCommand = checkCommand(cmdname, args);
                    if (isCommand){
                        Player target = Bukkit.getPlayer(args[0]);
                        if (target != null){
                            if (target != player) {
                                sudoSuccess(args, cmdargs, cmdname, target, player);
                                return true;
                            }else{
                                String msg = Lang.fileConfig.getString("target-self");
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            }
                        }else if (args[0].equalsIgnoreCase("%console%")){
                            if (args.length < 3){
                                String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/sudo %console% <command>");
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            }else{
                                Player target2 = Bukkit.getPlayer(args[2]);
                                sudoSuccess(args, cmdargs, cmdname, target2, null);
                                return true;
                            }
                        }else{
                            String msg = Lang.fileConfig.getString("target-offline");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                            return true;
                        }
                    }else{
                        String msg = Lang.fileConfig.getString("sudo-command-invalid");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg).replace("<command>", cmdname));
                        return true;
                    }
                }else{
                    String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/sudo (player) <command>");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    return true;
                }
            }
        }else if (sender instanceof ConsoleCommandSender || sender instanceof BlockCommandSender){
            if (args.length >= 2){
                String cmdname = args[1];
                String cmdargs = "";
                boolean isCommand = checkCommand(cmdname, args);
                if (isCommand){
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target != null) {
                        sudoSuccess(args, cmdargs, cmdname, target, sender);
                        return true;
                    }else{
                        String msg = Lang.fileConfig.getString("target-offline");
                        Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    }
                }else{
                    String msg = Lang.fileConfig.getString("sudo-command-invalid");
                    Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', msg).replace("<command>", cmdname));
                    return true;
                }
            }else{
                String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/sudo (player) <command>");
                Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', msg));
                return true;
            }
        }
        return false;
    }
    public boolean checkCommand(String cmd, String[] args){
        boolean isCommand = false;
        for (HelpTopic cmdlabel : Bukkit.getServer().getHelpMap().getHelpTopics()){
            if (cmd.equalsIgnoreCase("%console%")){
                String newcmd = args[2];
                if (cmdlabel.getName().equalsIgnoreCase(newcmd)){
                    isCommand = true;
                }
            }else{
                if (cmdlabel.getName().equalsIgnoreCase(cmd)){
                    isCommand = true;
                }
            }
        }
        return isCommand;
    }
    public void sudoSuccess(String[] args, String cmdargs, String cmdname, Player target, CommandSender sender){
        for (int i = 1; i < args.length; i++) {
            String arg = (args[i] + " ");
            cmdargs = (cmdargs + arg);
        }
        if (args[0].equalsIgnoreCase("%console%")){
            String newcmdname = args[1];
            ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
            String msg = Lang.fileConfig.getString("sudo-successful");
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', msg).replace("<command>", newcmdname).replace("<target>", args[2]));
            Bukkit.dispatchCommand(console, cmdargs.replace("/", ""));
        }else{
            String msg = Lang.fileConfig.getString("sudo-successful");
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg).replace("<command>", cmdname).replace("<target>", target.getName()));
            Bukkit.dispatchCommand(target, cmdargs.replace("/", ""));
        }
    }
}