package me.rocketmankianproductions.serveressentials.commands;

import org.bukkit.Bukkit;
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
                System.out.println("hi");
                if (args.length == 3){
                    System.out.println("hi2");
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
                                bookMeta2.addPage(page);
                                player.sendMessage(page);
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
                player.sendMessage("no");
            }
        }
        return false;
    }
}
