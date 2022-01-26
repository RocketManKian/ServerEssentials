package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.file.Lang;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Book implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            if (player.hasPermission("se.book")){
                if (args.length == 3){
                    if (player.getInventory().getItemInMainHand().getType() == Material.WRITTEN_BOOK){
                        if (args[0].equalsIgnoreCase("unsign")){
                            // Written Book
                            BookMeta bookMeta = (BookMeta)player.getInventory().getItemInMainHand().getItemMeta();
                            List<String> pages = bookMeta.getPages();
                            // Writable Book
                            ItemStack book = new ItemStack(Material.WRITABLE_BOOK);
                            BookMeta bookMeta2 = (BookMeta) book.getItemMeta();
                            int amount = bookMeta.getPageCount();
                            for (int i = 1; i <= amount; i++){
                                String page = bookMeta.getPage(i);
                                TextComponent message = new TextComponent(page);
                                bookMeta2.addPage(String.valueOf(message));
                            }
                            book.setItemMeta(bookMeta2);
                            player.getInventory().getItemInMainHand().setType(Material.WRITABLE_BOOK);
                        }
                    }else if (player.getInventory().getItemInMainHand().getType() == Material.WRITABLE_BOOK){
                        if (args[0].equalsIgnoreCase("sign")){
                        }
                    }
                }
            }else{
                player.sendMessage("No Permission");
            }
        }else{
            String console = Lang.fileConfig.getString("console-invalid");
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', console));
            return true;
        }
        return false;
    }
}
