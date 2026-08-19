package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
import me.rocketmankianproductions.serveressentials.file.UserFile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import static me.rocketmankianproductions.serveressentials.ServerEssentials.getPlugin;
import static me.rocketmankianproductions.serveressentials.ServerEssentials.hex;

public class SocialSpy implements CommandExecutor, Listener {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (command.getName().equalsIgnoreCase("socialspy")){
                if (ServerEssentials.permissionChecker(player, "se.socialspy")) {
                    if (!UserFile.config.getBoolean(player.getUniqueId() + ".spy")) {
                        UserFile.config.set(player.getUniqueId() + ".spy", true);
                        String msg = Lang.fileConfig.getString("socialspy-enabled");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                    } else {
                        UserFile.config.set(player.getUniqueId() + ".spy", false);
                        String msg = Lang.fileConfig.getString("socialspy-disabled");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                    }
                    try {
                        UserFile.config.save(UserFile.file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }else{
            String console = Lang.fileConfig.getString("console-invalid");
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', hex(console)));
        }
        return false;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCommandExecution(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String commandsocialspy = Lang.fileConfig.getString("socialspy-command").replace("<sender>", player.getName()).replace("<command>", event.getMessage());
        socialSpyCommand(player, commandsocialspy);
    }

    public static void socialSpyMessage(Player messager, Player recipient, String msgsocialspy, String msgsender, String msgrecipient){
        for (Player admin : Bukkit.getOnlinePlayers()) {
            if (UserFile.config.getBoolean(admin.getUniqueId() + ".spy")) {
                if (admin != messager && admin != recipient) {
                    admin.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msgsocialspy)));
                }
            }
        }
        messager.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msgsender)));
        recipient.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msgrecipient)));
        if (ServerEssentials.getPlugin().getConfig().getBoolean("msgsound")){
            // Get the sound name, volume, and pitch from the config
            String soundName = getPlugin().getConfig().getString("msgsoundName", "block.note_block.bell");
            double volume = getPlugin().getConfig().getDouble("msgsoundVolume", 1.0);
            double pitch = getPlugin().getConfig().getDouble("msgsoundPitch", 1.0);
            try {
                Sound sound = Sound.valueOf(soundName.toUpperCase().replace(".", "_"));
                recipient.playSound(recipient.getLocation(), sound, (float) volume, (float) pitch);
                messager.playSound(messager.getLocation(), sound, (float) volume, (float) pitch);
            } catch (IllegalArgumentException e) {
                // Handle invalid sound name
                Bukkit.getLogger().warning("Invalid sound name in config: " + soundName);
            }
        }
    }

    public static void socialSpyCommand(Player executor, String msgsocialspy){
        for (Player admin : Bukkit.getOnlinePlayers()) {
            if (UserFile.config.getBoolean(admin.getUniqueId() + ".spy")) {
                if (admin != executor) {
                    admin.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msgsocialspy)));
                }
            }
        }
    }
}