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
                    boolean isCommand = checkCommand(cmdname);
                    if (isCommand){
                        Player target = Bukkit.getPlayer(args[0]);
                        if (target != null){
                            if (target != player) {
                                sudoSuccess(args, cmdargs, cmdname, target);
                                return true;
                            }else{
                                String msg = Lang.fileConfig.getString("target-self");
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
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
                boolean isCommand = checkCommand(cmdname);
                if (isCommand){
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target != null) {
                        sudoSuccess(args, cmdargs, cmdname, target);
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
    public boolean checkCommand(String cmd){
        boolean isCommand = false;
        for (HelpTopic cmdlabel : Bukkit.getServer().getHelpMap().getHelpTopics()){
            if (cmdlabel.getName().equalsIgnoreCase(cmd)){
                isCommand = true;
            }
        }
        return isCommand;
    }
    public void sudoSuccess(String[] args, String cmdargs, String cmdname, Player target){
        for (int i = 1; i < args.length; i++) {
            String arg = (args[i] + " ");
            cmdargs = (cmdargs + arg);
        }
        String msg = Lang.fileConfig.getString("sudo-successful");
        Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', msg).replace("<command>", cmdname).replace("<target>", target.getName()));
        Bukkit.dispatchCommand(target, cmdargs.replace("/", ""));
    }
}
