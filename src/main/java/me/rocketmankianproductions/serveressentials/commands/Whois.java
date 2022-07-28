package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Whois implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            if (ServerEssentials.permissionChecker(player, "se.whois")){
                if (args.length == 1){
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target == null){
                        String msg = Lang.fileConfig.getString("target-offline");
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        return true;
                    }else{
                        // Calculating Playtime
                        int ticks = player.getStatistic(Statistic.PLAY_ONE_MINUTE);
                        int rest = 0;
                        // Ticks divided by 20 = seconds. Seconds x 60 = Minute. Minute x 60 = Hour. Hour x 24 = Day.
                        int days = ticks / (20 * 3600 * 24);
                        rest = ticks % (20 * 3600 * 24);
                        int hours = rest / (20 * 3600);
                        rest = rest % (20 * 3600);
                        int minutes = rest / (20 * 60);
                        rest = rest % (20 * 60);
                        int seconds = rest / 20;
                        String god = "&4false";
                        if (God.god_toggle.contains(target.getName())){
                            god = "&atrue";
                        }
                        String op = "&4false";
                        if (target.isOp()){
                            op = "&atrue";
                        }
                        String fly = "&4false";
                        String flyindicator = "";
                        if (target.getAllowFlight()){
                            flyindicator = "&f(not flying)";
                            if (target.isFlying()){
                                flyindicator = "&f(flying)";
                            }
                            fly = "&atrue";
                        }
                        float speed = 0;
                        if (target.isFlying()){
                            speed = target.getFlySpeed();
                        }else if (!target.isFlying()){
                            speed = target.getWalkSpeed();
                        }
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "\n&6====== WhoIs: &e" + target.getName() + " &6======"
                                + "\n&6- Nickname: &f" + target.getDisplayName()
                                + "\n&6- UUID: &f" + target.getUniqueId()
                                + "\n&6- Health: &f" + target.getHealth()
                                + "\n&6- Hunger: &f" + target.getFoodLevel()
                                + "\n&6- Exp: &f" + target.getTotalExperience() + " (Level " + target.getLevel() + ")"
                                + "\n&6- Location: &f(" + target.getWorld().getName() + ", " + target.getLocation().getBlockX() + ", " + target.getLocation().getBlockY() + ", " + target.getLocation().getBlockZ() + ")"
                                + "\n&6- Playtime: &f" + days + " Days " + hours + " Hours " + minutes + " Minutes " + seconds + " Seconds"
                                + "\n&6- IP Address: &f" + target.getAddress().getAddress().getHostAddress()
                                + "\n&6- Gamemode: &f" + target.getGameMode()
                                + "\n&6- Godmode: &f" + god
                                + "\n&6- OP: &f" + op
                                + "\n&6- Fly Mode: &f" + fly + " " + flyindicator
                                + "\n&6- Speed: &f" + speed));
                    }
                }else{
                    String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/whois <player>");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    return true;
                }
            }
        }
        return false;
    }
}
