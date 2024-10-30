package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import java.lang.reflect.Field;

import static me.rocketmankianproductions.serveressentials.ServerEssentials.hex;

public class Jump implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (commandSender instanceof Player){
            Player player = (Player) commandSender;
            if (ServerEssentials.permissionChecker(player, "se.jump")){
                PotionEffectType JUMP_BOOST;
                try {
                    // Try to get the JUMP_BOOST field from PotionEffectType
                    Field jumpBoostField = PotionEffectType.class.getField("JUMP_BOOST");
                    JUMP_BOOST = (PotionEffectType) jumpBoostField.get(null);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    // Fallback if JUMP_BOOST does not exist
                    JUMP_BOOST = PotionEffectType.getByName("JUMP");
                }
                String jumpmsg = Lang.fileConfig.getString("jump-message");
                String jumpremovemsg = Lang.fileConfig.getString("jump-remove-message");
                if (args.length == 0){
                    if (player.hasPotionEffect(JUMP_BOOST)){
                        player.removePotionEffect(JUMP_BOOST);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(jumpremovemsg)));
                        return true;
                    }else{
                        player.addPotionEffect(new PotionEffect(JUMP_BOOST, Integer.MAX_VALUE, 2));
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(jumpmsg)));
                        return true;
                    }
                }else if (args.length >= 1){
                    OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
                    if (target.isOnline()){
                        if (target.getPlayer().hasPotionEffect(JUMP_BOOST)){
                            target.getPlayer().removePotionEffect(JUMP_BOOST);
                            target.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', hex(jumpremovemsg)));
                        }else{
                            target.getPlayer().addPotionEffect(new PotionEffect(JUMP_BOOST, Integer.MAX_VALUE, 2));
                            target.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', hex(jumpmsg)));
                        }
                        return true;
                    }else{
                        String msg = Lang.fileConfig.getString("target-offline");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
