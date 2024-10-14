package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

import static me.rocketmankianproductions.serveressentials.ServerEssentials.hex;

public class Anvil implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (commandSender instanceof Player){
            Player player = (Player) commandSender;
            if (ServerEssentials.permissionChecker(player, "se.anvil")) {
                new AnvilGUI.Builder()
                        .onClick((slot, stateSnapshot) -> { // Either use sync or async variant, not both
                            return Collections.emptyList();
                        })
                        .interactableSlots(AnvilGUI.Slot.INPUT_LEFT, AnvilGUI.Slot.INPUT_RIGHT, AnvilGUI.Slot.OUTPUT)
                        .title("Repair & Name")                                       //set the title of the GUI (only works in 1.14+)
                        .plugin(ServerEssentials.getPlugin())                                          //set the plugin instance
                        .open(player);
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 3.0F, 2.0F);
                return true;
            }
        }else{
            String console = Lang.fileConfig.getString("console-invalid");
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', hex(console)));
            return true;
        }
        return false;
    }
}
