package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
import me.rocketmankianproductions.serveressentials.file.UserFile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static me.rocketmankianproductions.serveressentials.ServerEssentials.hex;

public class Whois implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            if (ServerEssentials.permissionChecker(player, "se.whois")){
                if (args.length == 1){
                    OfflinePlayer targetOffline = Bukkit.getOfflinePlayer(args[0]);

                    // Note: Bukkit.getOfflinePlayer() rarely returns null,
                    // but we check if it has ever played before or is currently online
                    if (!targetOffline.hasPlayedBefore() && !targetOffline.isOnline()){
                        String msg = Lang.fileConfig.getString("target-offline");
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                        return true;
                    }

                    // 1. Calculate Playtime (Safe for offline players)
                    int ticks = targetOffline.getStatistic(Statistic.PLAY_ONE_MINUTE);
                    int days = ticks / (20 * 3600 * 24);
                    int rest = ticks % (20 * 3600 * 24);
                    int hours = rest / (20 * 3600);
                    rest = rest % (20 * 3600);
                    int minutes = rest / (20 * 60);
                    rest = rest % (20 * 60);
                    int seconds = rest / 20;

                    // 2. Fetch Config Variables (Safe for offline players)
                    String god = UserFile.config.getBoolean(targetOffline.getUniqueId() + ".godmode") ? "&atrue" : "&4false";
                    String vanish = UserFile.config.getBoolean(targetOffline.getUniqueId() + ".vanish") ? "&atrue" : "&4false";
                    String muted = UserFile.config.getBoolean(targetOffline.getUniqueId() + ".muted") ? "&atrue" : "&4false";
                    String op = targetOffline.isOp() ? "&atrue" : "&4false";

                    // 3. Initialize Online-Only Variables with Fallbacks
                    String health = "&cOffline";
                    String hunger = "&cOffline";
                    String exp = "&cOffline";
                    String location = "&cOffline";
                    String ipAddress = "&cOffline";
                    String gamemode = "&cOffline";
                    String fly = "&4false";
                    String flyindicator = "";
                    String speed = "&cOffline";

                    // 4. If the target is online, grab live data
                    if (targetOffline.isOnline() && targetOffline.getPlayer() != null) {
                        Player target = targetOffline.getPlayer();

                        health = String.format("%.1f/%.1f", target.getHealth(), target.getMaxHealth());
                        hunger = String.valueOf(target.getFoodLevel());
                        exp = target.getTotalExperience() + " (Level " + target.getLevel() + ")";
                        location = "(" + target.getWorld().getName() + ", " + target.getLocation().getBlockX() + ", " + target.getLocation().getBlockY() + ", " + target.getLocation().getBlockZ() + ")";

                        if (target.getAddress() != null) {
                            ipAddress = target.getAddress().getAddress().getHostAddress();
                        }

                        gamemode = target.getGameMode().toString().toLowerCase();

                        if (target.getAllowFlight()) {
                            fly = "&atrue";
                            flyindicator = target.isFlying() ? "&f(flying)" : "&f(not flying)";
                        }

                        float rawSpeed = target.isFlying() ? target.getFlySpeed() : target.getWalkSpeed();
                        speed = String.valueOf(rawSpeed);
                    }

                    // 5. Send compiled report
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "\n&6====== WhoIs: &e" + targetOffline.getName() + " &6======"
                            + "\n&6- Nickname: &f" + targetOffline.getName()
                            + "\n&6- UUID: &f" + targetOffline.getUniqueId()
                            + "\n&6- Health: &f" + health
                            + "\n&6- Hunger: &f" + hunger
                            + "\n&6- Exp: &f" + exp
                            + "\n&6- Location: &f" + location
                            + "\n&6- Playtime: &f" + days + " Days " + hours + " Hours " + minutes + " Minutes " + seconds + " Seconds"
                            + "\n&6- IP Address: &f" + ipAddress
                            + "\n&6- Gamemode: &f" + gamemode
                            + "\n&6- Godmode: &f" + god
                            + "\n&6- OP: &f" + op
                            + "\n&6- Fly Mode: &f" + fly + " " + flyindicator
                            + "\n&6- Speed: &f" + speed
                            + "\n&6- Vanish: &f" + vanish
                            + "\n&6- Muted: &f" + muted));
                    return true;

                } else {
                    String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/whois <player>");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                    return true;
                }
            }
        }
        return false;
    }
}