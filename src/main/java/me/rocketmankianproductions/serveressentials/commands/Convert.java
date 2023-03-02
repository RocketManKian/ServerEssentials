package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static me.rocketmankianproductions.serveressentials.ServerEssentials.hex;

public class Convert implements CommandExecutor {

    private static ArrayList<Material> items = new ArrayList<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (ServerEssentials.permissionChecker(player, "se.convert")) {
                if (args.length == 0) {
                    ArrayList<Material> materials = setItems();
                    ItemStack inv = player.getInventory().getItemInMainHand();
                    if (materials.contains(Material.matchMaterial(inv.getType().name()))) {
                        itemChecker(player, inv, Material.NETHERITE_INGOT, Material.NETHERITE_BLOCK, 9, 0, false);
                        itemChecker(player, inv, Material.DIAMOND, Material.DIAMOND_BLOCK, 9, 0, false);
                        itemChecker(player, inv, Material.EMERALD, Material.EMERALD_BLOCK, 9, 0, false);
                        itemChecker(player, inv, Material.REDSTONE, Material.REDSTONE_BLOCK, 9, 0, false);
                        itemChecker(player, inv, Material.LAPIS_LAZULI, Material.LAPIS_BLOCK, 9, 0, false);
                        itemChecker(player, inv, Material.COAL, Material.COAL_BLOCK, 9, 0, false);
                        itemChecker(player, inv, Material.GOLD_INGOT, Material.GOLD_BLOCK, 9, 0, false);
                        itemChecker(player, inv, Material.IRON_INGOT, Material.IRON_BLOCK, 9, 0, false);
                        itemChecker(player, inv, Material.CLAY_BALL, Material.CLAY, 4, 0, false);
                        itemChecker(player, inv, Material.WHEAT, Material.HAY_BLOCK, 9, 0, false);
                        itemChecker(player, inv, Material.BONE_MEAL, Material.BONE_BLOCK, 9, 0, false);
                        itemChecker(player, inv, Material.SLIME_BALL, Material.SLIME_BLOCK, 9, 0, false);
                        itemChecker(player, inv, Material.STRING, Material.WHITE_WOOL, 4, 0, false);
                        itemChecker(player, inv, Material.QUARTZ, Material.QUARTZ_BLOCK, 4, 0, false);
                        itemChecker(player, inv, Material.NETHER_WART, Material.NETHER_WART_BLOCK, 9, 0, false);
                        itemChecker(player, inv, Material.MAGMA_CREAM, Material.MAGMA_BLOCK, 4, 0, false);
                        itemChecker(player, inv, Material.DRIED_KELP, Material.DRIED_KELP_BLOCK, 9, 0, false);
                        itemChecker(player, inv, Material.NETHER_BRICK, Material.NETHER_BRICKS, 4, 0, false);
                        itemChecker(player, inv, Material.POPPED_CHORUS_FRUIT, Material.PURPUR_BLOCK, 4, 0, false);
                        itemChecker(player, inv, Material.HONEYCOMB, Material.HONEYCOMB_BLOCK, 4, 0, false);
                        itemChecker(player, inv, Material.HONEY_BOTTLE, Material.HONEY_BLOCK, 4, 0, false);
                        if (Bukkit.getServer().getVersion().contains("1.17") || Bukkit.getServer().getVersion().contains("1.17.1") || Bukkit.getServer().getVersion().contains("1.18") || Bukkit.getServer().getVersion().contains("1.18.1") || Bukkit.getServer().getVersion().contains("1.18.1") || Bukkit.getServer().getVersion().contains("1.19")){
                            itemChecker(player, inv, Material.RAW_GOLD, Material.RAW_GOLD_BLOCK, 9, 0, false);
                            itemChecker(player, inv, Material.RAW_IRON, Material.RAW_IRON_BLOCK, 9, 0, false);
                            itemChecker(player, inv, Material.RAW_COPPER, Material.RAW_COPPER_BLOCK, 9, 0, false);
                            itemChecker(player, inv, Material.COPPER_INGOT, Material.COPPER_BLOCK, 9, 0, false);
                            itemChecker(player, inv, Material.AMETHYST_SHARD, Material.AMETHYST_BLOCK, 4, 0, false);
                        }
                    } else {
                        String msg = Lang.fileConfig.getString("convert-invalid").replace("<item>", inv.getType().name());
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                        return true;
                    }
                    return true;
                    //Convert All
                } else if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("all")) {
                        Inventory inv = player.getInventory();
                        for (int i = 0; i < inv.getSize(); i++) {
                            ItemStack testItem = inv.getItem(i);
                            if (testItem == null) {
                                if (i < 40){
                                    continue;
                                }else{
                                    return true;
                                }
                            }
                            itemChecker(player, testItem, Material.NETHERITE_INGOT, Material.NETHERITE_BLOCK, 9, i, true);
                            itemChecker(player, testItem, Material.DIAMOND, Material.DIAMOND_BLOCK, 9, i, true);
                            itemChecker(player, testItem, Material.EMERALD, Material.EMERALD_BLOCK, 9, i, true);
                            itemChecker(player, testItem, Material.REDSTONE, Material.REDSTONE_BLOCK, 9, i, true);
                            itemChecker(player, testItem, Material.LAPIS_LAZULI, Material.LAPIS_BLOCK, 9, i, true);
                            itemChecker(player, testItem, Material.COAL, Material.COAL_BLOCK, 9, i, true);
                            itemChecker(player, testItem, Material.GOLD_INGOT, Material.GOLD_BLOCK, 9, i, true);
                            itemChecker(player, testItem, Material.IRON_INGOT, Material.IRON_BLOCK, 9, i, true);
                            itemChecker(player, testItem, Material.CLAY_BALL, Material.CLAY, 4, i, true);
                            itemChecker(player, testItem, Material.WHEAT, Material.HAY_BLOCK, 9, i, true);
                            itemChecker(player, testItem, Material.BONE_MEAL, Material.BONE_BLOCK, 9, i, true);
                            itemChecker(player, testItem, Material.SLIME_BALL, Material.SLIME_BLOCK, 9, i, true);
                            itemChecker(player, testItem, Material.STRING, Material.WHITE_WOOL, 4, i, true);
                            itemChecker(player, testItem, Material.QUARTZ, Material.QUARTZ_BLOCK, 4, i, true);
                            itemChecker(player, testItem, Material.NETHER_WART, Material.NETHER_WART_BLOCK, 9, i, true);
                            itemChecker(player, testItem, Material.MAGMA_CREAM, Material.MAGMA_BLOCK, 4, i, true);
                            itemChecker(player, testItem, Material.DRIED_KELP, Material.DRIED_KELP_BLOCK, 9, i, true);
                            itemChecker(player, testItem, Material.NETHER_BRICK, Material.NETHER_BRICKS, 4, i, true);
                            itemChecker(player, testItem, Material.POPPED_CHORUS_FRUIT, Material.PURPUR_BLOCK, 4, i, true);
                            itemChecker(player, testItem, Material.HONEYCOMB, Material.HONEYCOMB_BLOCK, 4, i, true);
                            itemChecker(player, testItem, Material.HONEY_BOTTLE, Material.HONEY_BLOCK, 4, i, true);
                            if (Bukkit.getServer().getVersion().contains("1.17") || Bukkit.getServer().getVersion().contains("1.17.1") || Bukkit.getServer().getVersion().contains("1.18") || Bukkit.getServer().getVersion().contains("1.18.1") || Bukkit.getServer().getVersion().contains("1.18.1") || Bukkit.getServer().getVersion().contains("1.19")){
                                itemChecker(player, testItem, Material.RAW_GOLD, Material.RAW_GOLD_BLOCK, 9, i, true);
                                itemChecker(player, testItem, Material.RAW_IRON, Material.RAW_IRON_BLOCK, 9, i, true);
                                itemChecker(player, testItem, Material.RAW_COPPER, Material.RAW_COPPER_BLOCK, 9, i, true);
                                itemChecker(player, testItem, Material.COPPER_INGOT, Material.COPPER_BLOCK, 9, i, true);
                                itemChecker(player, testItem, Material.AMETHYST_SHARD, Material.AMETHYST_BLOCK, 4, i, true);
                            }
                        }
                    }else{
                        String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/convert all");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                        return true;
                    }
                }else{
                    String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/convert");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                    return true;
                }
            }
        }else{
            String console = Lang.fileConfig.getString("console-invalid");
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', hex(console)));
            return true;
        }
        return false;
    }

    public ArrayList<Material> setItems (){
        items.add(Material.NETHERITE_INGOT);

        items.add(Material.DIAMOND);

        items.add(Material.EMERALD);

        items.add(Material.REDSTONE);

        items.add(Material.LAPIS_LAZULI);

        items.add(Material.COAL);

        items.add(Material.GOLD_INGOT);

        items.add(Material.IRON_INGOT);

        items.add(Material.CLAY_BALL);

        items.add(Material.WHEAT);

        items.add(Material.BONE_MEAL);

        items.add(Material.SLIME_BALL);

        items.add(Material.STRING);

        items.add(Material.QUARTZ);

        items.add(Material.NETHER_WART);

        items.add(Material.MAGMA_CREAM);

        items.add(Material.DRIED_KELP);

        items.add(Material.NETHER_BRICK);

        items.add(Material.POPPED_CHORUS_FRUIT);

        items.add(Material.HONEY_BOTTLE);

        items.add(Material.HONEYCOMB);

        if (Bukkit.getServer().getVersion().contains("1.17") || Bukkit.getServer().getVersion().contains("1.17.1") || Bukkit.getServer().getVersion().contains("1.18") || Bukkit.getServer().getVersion().contains("1.18.1") || Bukkit.getServer().getVersion().contains("1.18.1") || Bukkit.getServer().getVersion().contains("1.19")){
            items.add(Material.RAW_GOLD);
            items.add(Material.RAW_IRON);
            items.add(Material.RAW_COPPER);
            items.add(Material.COPPER_INGOT);
            items.add(Material.AMETHYST_SHARD);
        }
        return items;
    }

    public void itemChecker(Player player, ItemStack itemStack, Material material1, Material material2, int amountnumber, int i, boolean all){
        if (itemStack.getType() == material1) {
            int amount;
            if (all){
                amount = player.getInventory().getItem(i).getAmount();
            }else{
                amount = player.getInventory().getItemInMainHand().getAmount();
            }
            if (amount >= amountnumber) {
                int blocks = amount / amountnumber;
                int remainder = amount % blocks;
                ItemStack item = new ItemStack(material2);
                item.setAmount(blocks);
                ItemStack item2 = new ItemStack(material1);
                item2.setAmount(remainder);
                player.getInventory().addItem(item2);
                if (all){
                    player.getInventory().setItem(i, item);
                }else{
                    player.getInventory().setItemInMainHand(item);
                }
                String msg = Lang.fileConfig.getString("convert-successful").replace("<amount>", String.valueOf(amount)).replace("<item>", material1.name()).replace("<total>", String.valueOf(blocks)).replace("<block>", material2.name());
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
            }
        }
    }
}