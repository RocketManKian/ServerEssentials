package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.file.Lang;
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

public class Convert implements CommandExecutor {

    private static ArrayList<Material> items = new ArrayList<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("se.convert")) {
                if (args.length == 0) {
                    ArrayList<Material> materials = getItems();
                    Material inv = player.getInventory().getItemInMainHand().getType();
                    if (materials.contains(inv)) {
                        if (inv == Material.NETHERITE_INGOT) {
                            int amount = player.getInventory().getItemInMainHand().getAmount();
                            if (amount >= 9) {
                                int blocks = amount / 9;
                                int remainder = amount % blocks;
                                player.sendMessage(String.valueOf(remainder));
                                ItemStack item = new ItemStack(Material.NETHERITE_BLOCK);
                                item.setAmount(blocks);
                                ItemStack item2 = new ItemStack(Material.NETHERITE_INGOT);
                                item2.setAmount(remainder);
                                player.getInventory().addItem(item2);
                                player.getInventory().setItemInMainHand(item);
                                String msg = Lang.fileConfig.getString("convert-successful").replace("<amount>", String.valueOf(amount)).replace("<item>", Material.NETHERITE_INGOT.name()).replace("<total>", String.valueOf(blocks)).replace("<block>", Material.NETHERITE_BLOCK.name());
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            } else {
                                String msg = Lang.fileConfig.getString("convert-unsuccessful").replace("<item>", Material.NETHERITE_INGOT.name());
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            }
                        } else if (inv == Material.DIAMOND) {
                            int amount = player.getInventory().getItemInMainHand().getAmount();
                            if (amount >= 9) {
                                int blocks = amount / 9;
                                int remainder = amount % blocks;
                                player.sendMessage(String.valueOf(remainder));
                                ItemStack item = new ItemStack(Material.DIAMOND_BLOCK);
                                item.setAmount(blocks);
                                ItemStack item2 = new ItemStack(Material.DIAMOND);
                                item2.setAmount(remainder);
                                player.getInventory().addItem(item2);
                                player.getInventory().setItemInMainHand(item);
                                String msg = Lang.fileConfig.getString("convert-successful").replace("<amount>", String.valueOf(amount)).replace("<item>", Material.DIAMOND.name()).replace("<total>", String.valueOf(blocks)).replace("<block>", Material.DIAMOND_BLOCK.name());
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            } else {
                                String msg = Lang.fileConfig.getString("convert-unsuccessful").replace("<item>", Material.DIAMOND.name());
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            }
                        } else if (inv == Material.EMERALD) {
                            int amount = player.getInventory().getItemInMainHand().getAmount();
                            if (amount >= 9) {
                                int blocks = amount / 9;
                                int remainder = amount % blocks;
                                player.sendMessage(String.valueOf(remainder));
                                ItemStack item = new ItemStack(Material.EMERALD_BLOCK);
                                item.setAmount(blocks);
                                ItemStack item2 = new ItemStack(Material.EMERALD);
                                item2.setAmount(remainder);
                                player.getInventory().addItem(item2);
                                player.getInventory().setItemInMainHand(item);
                                String msg = Lang.fileConfig.getString("convert-successful").replace("<amount>", String.valueOf(amount)).replace("<item>", Material.EMERALD.name()).replace("<total>", String.valueOf(blocks)).replace("<block>", Material.EMERALD_BLOCK.name());
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            } else {
                                String msg = Lang.fileConfig.getString("convert-unsuccessful").replace("<item>", Material.EMERALD.name());
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            }
                        } else if (inv == Material.REDSTONE) {
                            int amount = player.getInventory().getItemInMainHand().getAmount();
                            if (amount >= 9) {
                                int blocks = amount / 9;
                                int remainder = amount % blocks;
                                player.sendMessage(String.valueOf(remainder));
                                ItemStack item = new ItemStack(Material.REDSTONE_BLOCK);
                                item.setAmount(blocks);
                                ItemStack item2 = new ItemStack(Material.REDSTONE);
                                item2.setAmount(remainder);
                                player.getInventory().addItem(item2);
                                player.getInventory().setItemInMainHand(item);
                                String msg = Lang.fileConfig.getString("convert-successful").replace("<amount>", String.valueOf(amount)).replace("<item>", Material.REDSTONE.name()).replace("<total>", String.valueOf(blocks)).replace("<block>", Material.REDSTONE_BLOCK.name());
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            } else {
                                String msg = Lang.fileConfig.getString("convert-unsuccessful").replace("<item>", Material.REDSTONE.name());
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            }
                        } else if (inv == Material.LAPIS_LAZULI) {
                            int amount = player.getInventory().getItemInMainHand().getAmount();
                            if (amount >= 9) {
                                int blocks = amount / 9;
                                int remainder = amount % blocks;
                                player.sendMessage(String.valueOf(remainder));
                                ItemStack item = new ItemStack(Material.LAPIS_BLOCK);
                                item.setAmount(blocks);
                                ItemStack item2 = new ItemStack(Material.LAPIS_LAZULI);
                                item2.setAmount(remainder);
                                player.getInventory().addItem(item2);
                                player.getInventory().setItemInMainHand(item);
                                String msg = Lang.fileConfig.getString("convert-successful").replace("<amount>", String.valueOf(amount)).replace("<item>", Material.LAPIS_LAZULI.name()).replace("<total>", String.valueOf(blocks)).replace("<block>", Material.LAPIS_BLOCK.name());
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            } else {
                                String msg = Lang.fileConfig.getString("convert-unsuccessful").replace("<item>", Material.LAPIS_LAZULI.name());
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            }
                        } else if (inv == Material.COAL) {
                            int amount = player.getInventory().getItemInMainHand().getAmount();
                            if (amount >= 9) {
                                int blocks = amount / 9;
                                int remainder = amount % blocks;
                                player.sendMessage(String.valueOf(remainder));
                                ItemStack item = new ItemStack(Material.COAL_BLOCK);
                                item.setAmount(blocks);
                                ItemStack item2 = new ItemStack(Material.COAL);
                                item2.setAmount(remainder);
                                player.getInventory().addItem(item2);
                                player.getInventory().setItemInMainHand(item);
                                String msg = Lang.fileConfig.getString("convert-successful").replace("<amount>", String.valueOf(amount)).replace("<item>", Material.COAL.name()).replace("<total>", String.valueOf(blocks)).replace("<block>", Material.COAL_BLOCK.name());
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            } else {
                                String msg = Lang.fileConfig.getString("convert-unsuccessful").replace("<item>", Material.COAL.name());
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            }
                        } else if (inv == Material.RAW_GOLD) {
                            int amount = player.getInventory().getItemInMainHand().getAmount();
                            if (amount >= 9) {
                                int blocks = amount / 9;
                                int remainder = amount % blocks;
                                player.sendMessage(String.valueOf(remainder));
                                ItemStack item = new ItemStack(Material.RAW_GOLD_BLOCK);
                                item.setAmount(blocks);
                                ItemStack item2 = new ItemStack(Material.RAW_GOLD);
                                item2.setAmount(remainder);
                                player.getInventory().addItem(item2);
                                player.getInventory().setItemInMainHand(item);
                                String msg = Lang.fileConfig.getString("convert-successful").replace("<amount>", String.valueOf(amount)).replace("<item>", Material.RAW_GOLD.name()).replace("<total>", String.valueOf(blocks)).replace("<block>", Material.RAW_GOLD_BLOCK.name());
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            } else {
                                String msg = Lang.fileConfig.getString("convert-unsuccessful").replace("<item>", Material.RAW_GOLD.name());
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            }
                        } else if (inv == Material.GOLD_INGOT) {
                            int amount = player.getInventory().getItemInMainHand().getAmount();
                            if (amount >= 9) {
                                int blocks = amount / 9;
                                int remainder = amount % blocks;
                                player.sendMessage(String.valueOf(remainder));
                                ItemStack item = new ItemStack(Material.GOLD_BLOCK);
                                item.setAmount(blocks);
                                ItemStack item2 = new ItemStack(Material.GOLD_INGOT);
                                item2.setAmount(remainder);
                                player.getInventory().addItem(item2);
                                player.getInventory().setItemInMainHand(item);
                                String msg = Lang.fileConfig.getString("convert-successful").replace("<amount>", String.valueOf(amount)).replace("<item>", Material.GOLD_INGOT.name()).replace("<total>", String.valueOf(blocks)).replace("<block>", Material.GOLD_BLOCK.name());
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            } else {
                                String msg = Lang.fileConfig.getString("convert-unsuccessful").replace("<item>", Material.GOLD_INGOT.name());
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            }
                        } else if (inv == Material.RAW_IRON) {
                            int amount = player.getInventory().getItemInMainHand().getAmount();
                            if (amount >= 9) {
                                int blocks = amount / 9;
                                int remainder = amount % blocks;
                                player.sendMessage(String.valueOf(remainder));
                                ItemStack item = new ItemStack(Material.RAW_IRON_BLOCK);
                                item.setAmount(blocks);
                                ItemStack item2 = new ItemStack(Material.RAW_IRON);
                                item2.setAmount(remainder);
                                player.getInventory().addItem(item2);
                                player.getInventory().setItemInMainHand(item);
                                String msg = Lang.fileConfig.getString("convert-successful").replace("<amount>", String.valueOf(amount)).replace("<item>", Material.RAW_IRON.name()).replace("<total>", String.valueOf(blocks)).replace("<block>", Material.RAW_IRON_BLOCK.name());
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            } else {
                                String msg = Lang.fileConfig.getString("convert-unsuccessful").replace("<item>", Material.RAW_IRON.name());
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            }
                        } else if (inv == Material.IRON_INGOT) {
                            int amount = player.getInventory().getItemInMainHand().getAmount();
                            if (amount >= 9) {
                                int blocks = amount / 9;
                                int remainder = amount % blocks;
                                player.sendMessage(String.valueOf(remainder));
                                ItemStack item = new ItemStack(Material.IRON_BLOCK);
                                item.setAmount(blocks);
                                ItemStack item2 = new ItemStack(Material.IRON_INGOT);
                                item2.setAmount(remainder);
                                player.getInventory().addItem(item2);
                                player.getInventory().setItemInMainHand(item);
                                String msg = Lang.fileConfig.getString("convert-successful").replace("<amount>", String.valueOf(amount)).replace("<item>", Material.IRON_INGOT.name()).replace("<total>", String.valueOf(blocks)).replace("<block>", Material.IRON_BLOCK.name());
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            } else {
                                String msg = Lang.fileConfig.getString("convert-unsuccessful").replace("<item>", Material.IRON_INGOT.name());
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            }
                        } else if (inv == Material.RAW_COPPER) {
                            int amount = player.getInventory().getItemInMainHand().getAmount();
                            if (amount >= 9) {
                                int blocks = amount / 9;
                                int remainder = amount % blocks;
                                player.sendMessage(String.valueOf(remainder));
                                ItemStack item = new ItemStack(Material.RAW_COPPER_BLOCK);
                                item.setAmount(blocks);
                                ItemStack item2 = new ItemStack(Material.RAW_COPPER);
                                item2.setAmount(remainder);
                                player.getInventory().addItem(item2);
                                player.getInventory().setItemInMainHand(item);
                                String msg = Lang.fileConfig.getString("convert-successful").replace("<amount>", String.valueOf(amount)).replace("<item>", Material.RAW_COPPER.name()).replace("<total>", String.valueOf(blocks)).replace("<block>", Material.RAW_COPPER_BLOCK.name());
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            } else {
                                String msg = Lang.fileConfig.getString("convert-unsuccessful").replace("<item>", Material.RAW_COPPER.name());
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            }
                        } else if (inv == Material.COPPER_INGOT) {
                            int amount = player.getInventory().getItemInMainHand().getAmount();
                            if (amount >= 9) {
                                int blocks = amount / 9;
                                int remainder = amount % blocks;
                                player.sendMessage(String.valueOf(remainder));
                                ItemStack item = new ItemStack(Material.COPPER_BLOCK);
                                item.setAmount(blocks);
                                ItemStack item2 = new ItemStack(Material.COPPER_INGOT);
                                item2.setAmount(remainder);
                                player.getInventory().addItem(item2);
                                player.getInventory().setItemInMainHand(item);
                                String msg = Lang.fileConfig.getString("convert-successful").replace("<amount>", String.valueOf(amount)).replace("<item>", Material.COPPER_INGOT.name()).replace("<total>", String.valueOf(blocks)).replace("<block>", Material.COPPER_BLOCK.name());
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            } else {
                                String msg = Lang.fileConfig.getString("convert-unsuccessful").replace("<item>", Material.COPPER_INGOT.name());
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            }
                        } else if (inv == Material.CLAY_BALL) {
                            int amount = player.getInventory().getItemInMainHand().getAmount();
                            if (amount >= 4) {
                                int blocks = amount / 4;
                                int remainder = amount % blocks;
                                player.sendMessage(String.valueOf(remainder));
                                ItemStack item = new ItemStack(Material.CLAY);
                                item.setAmount(blocks);
                                ItemStack item2 = new ItemStack(Material.CLAY_BALL);
                                item2.setAmount(remainder);
                                player.getInventory().addItem(item2);
                                player.getInventory().setItemInMainHand(item);
                                String msg = Lang.fileConfig.getString("convert-successful").replace("<amount>", String.valueOf(amount)).replace("<item>", Material.CLAY_BALL.name()).replace("<total>", String.valueOf(blocks)).replace("<block>", Material.CLAY.name());
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            } else {
                                String msg = Lang.fileConfig.getString("convert-unsuccessful").replace("<item>", Material.CLAY_BALL.name());
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            }
                        } else if (inv == Material.WHEAT) {
                            int amount = player.getInventory().getItemInMainHand().getAmount();
                            if (amount >= 9) {
                                int blocks = amount / 9;
                                int remainder = amount % blocks;
                                player.sendMessage(String.valueOf(remainder));
                                ItemStack item = new ItemStack(Material.HAY_BLOCK);
                                item.setAmount(blocks);
                                ItemStack item2 = new ItemStack(Material.WHEAT);
                                item2.setAmount(remainder);
                                player.getInventory().addItem(item2);
                                player.getInventory().setItemInMainHand(item);
                                String msg = Lang.fileConfig.getString("convert-successful").replace("<amount>", String.valueOf(amount)).replace("<item>", Material.WHEAT.name()).replace("<total>", String.valueOf(blocks)).replace("<block>", Material.HAY_BLOCK.name());
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            } else {
                                String msg = Lang.fileConfig.getString("convert-unsuccessful").replace("<item>", Material.WHEAT.name());
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            }
                        } else if (inv == Material.BONE_MEAL) {
                            int amount = player.getInventory().getItemInMainHand().getAmount();
                            if (amount >= 9) {
                                int blocks = amount / 9;
                                int remainder = amount % blocks;
                                player.sendMessage(String.valueOf(remainder));
                                ItemStack item = new ItemStack(Material.BONE_BLOCK);
                                item.setAmount(blocks);
                                ItemStack item2 = new ItemStack(Material.BONE_MEAL);
                                item2.setAmount(remainder);
                                player.getInventory().addItem(item2);
                                player.getInventory().setItemInMainHand(item);
                                String msg = Lang.fileConfig.getString("convert-successful").replace("<amount>", String.valueOf(amount)).replace("<item>", Material.BONE_MEAL.name()).replace("<total>", String.valueOf(blocks)).replace("<block>", Material.BONE_BLOCK.name());
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            } else {
                                String msg = Lang.fileConfig.getString("convert-unsuccessful").replace("<item>", Material.BONE_MEAL.name());
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            }
                        } else if (inv == Material.SLIME_BALL) {
                            int amount = player.getInventory().getItemInMainHand().getAmount();
                            if (amount >= 9) {
                                int blocks = amount / 9;
                                int remainder = amount % blocks;
                                player.sendMessage(String.valueOf(remainder));
                                ItemStack item = new ItemStack(Material.SLIME_BLOCK);
                                item.setAmount(blocks);
                                ItemStack item2 = new ItemStack(Material.SLIME_BALL);
                                item2.setAmount(remainder);
                                player.getInventory().addItem(item2);
                                player.getInventory().setItemInMainHand(item);
                                String msg = Lang.fileConfig.getString("convert-successful").replace("<amount>", String.valueOf(amount)).replace("<item>", Material.SLIME_BALL.name()).replace("<total>", String.valueOf(blocks)).replace("<block>", Material.SLIME_BLOCK.name());
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            } else {
                                String msg = Lang.fileConfig.getString("convert-unsuccessful").replace("<item>", Material.SLIME_BALL.name());
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            }
                        } else if (inv == Material.STRING) {
                            int amount = player.getInventory().getItemInMainHand().getAmount();
                            if (amount >= 4) {
                                int blocks = amount / 4;
                                int remainder = amount % blocks;
                                player.sendMessage(String.valueOf(remainder));
                                ItemStack item = new ItemStack(Material.WHITE_WOOL);
                                item.setAmount(blocks);
                                ItemStack item2 = new ItemStack(Material.STRING);
                                item2.setAmount(remainder);
                                player.getInventory().addItem(item2);
                                player.getInventory().setItemInMainHand(item);
                                String msg = Lang.fileConfig.getString("convert-successful").replace("<amount>", String.valueOf(amount)).replace("<item>", Material.STRING.name()).replace("<total>", String.valueOf(blocks)).replace("<block>", Material.WHITE_WOOL.name());
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            } else {
                                String msg = Lang.fileConfig.getString("convert-unsuccessful").replace("<item>", Material.STRING.name());
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            }
                        } else if (inv == Material.QUARTZ) {
                            int amount = player.getInventory().getItemInMainHand().getAmount();
                            if (amount >= 4) {
                                int blocks = amount / 4;
                                int remainder = amount % blocks;
                                player.sendMessage(String.valueOf(remainder));
                                ItemStack item = new ItemStack(Material.QUARTZ_BLOCK);
                                item.setAmount(blocks);
                                ItemStack item2 = new ItemStack(Material.QUARTZ);
                                item2.setAmount(remainder);
                                player.getInventory().addItem(item2);
                                player.getInventory().setItemInMainHand(item);
                                String msg = Lang.fileConfig.getString("convert-successful").replace("<amount>", String.valueOf(amount)).replace("<item>", Material.QUARTZ.name()).replace("<total>", String.valueOf(blocks)).replace("<block>", Material.QUARTZ_BLOCK.name());
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            } else {
                                String msg = Lang.fileConfig.getString("convert-unsuccessful").replace("<item>", Material.QUARTZ.name());
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            }
                        } else if (inv == Material.NETHER_WART) {
                            int amount = player.getInventory().getItemInMainHand().getAmount();
                            if (amount >= 9) {
                                int blocks = amount / 9;
                                int remainder = amount % blocks;
                                player.sendMessage(String.valueOf(remainder));
                                ItemStack item = new ItemStack(Material.NETHER_WART_BLOCK);
                                item.setAmount(blocks);
                                ItemStack item2 = new ItemStack(Material.NETHER_WART);
                                item2.setAmount(remainder);
                                player.getInventory().addItem(item2);
                                player.getInventory().setItemInMainHand(item);
                                String msg = Lang.fileConfig.getString("convert-successful").replace("<amount>", String.valueOf(amount)).replace("<item>", Material.NETHER_WART.name()).replace("<total>", String.valueOf(blocks)).replace("<block>", Material.NETHER_WART_BLOCK.name());
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            } else {
                                String msg = Lang.fileConfig.getString("convert-unsuccessful").replace("<item>", Material.NETHER_WART.name());
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            }
                        } else if (inv == Material.MAGMA_CREAM) {
                            int amount = player.getInventory().getItemInMainHand().getAmount();
                            if (amount >= 4) {
                                int blocks = amount / 4;
                                int remainder = amount % blocks;
                                player.sendMessage(String.valueOf(remainder));
                                ItemStack item = new ItemStack(Material.MAGMA_BLOCK);
                                item.setAmount(blocks);
                                ItemStack item2 = new ItemStack(Material.MAGMA_CREAM);
                                item2.setAmount(remainder);
                                player.getInventory().addItem(item2);
                                player.getInventory().setItemInMainHand(item);
                                String msg = Lang.fileConfig.getString("convert-successful").replace("<amount>", String.valueOf(amount)).replace("<item>", Material.MAGMA_CREAM.name()).replace("<total>", String.valueOf(blocks)).replace("<block>", Material.MAGMA_BLOCK.name());
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            } else {
                                String msg = Lang.fileConfig.getString("convert-unsuccessful").replace("<item>", Material.MAGMA_CREAM.name());
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            }
                        } else if (inv == Material.DRIED_KELP) {
                            int amount = player.getInventory().getItemInMainHand().getAmount();
                            if (amount >= 9) {
                                int blocks = amount / 9;
                                int remainder = amount % blocks;
                                player.sendMessage(String.valueOf(remainder));
                                ItemStack item = new ItemStack(Material.DRIED_KELP_BLOCK);
                                item.setAmount(blocks);
                                ItemStack item2 = new ItemStack(Material.DRIED_KELP);
                                item2.setAmount(remainder);
                                player.getInventory().addItem(item2);
                                player.getInventory().setItemInMainHand(item);
                                String msg = Lang.fileConfig.getString("convert-successful").replace("<amount>", String.valueOf(amount)).replace("<item>", Material.DRIED_KELP.name()).replace("<total>", String.valueOf(blocks)).replace("<block>", Material.DRIED_KELP_BLOCK.name());
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            } else {
                                String msg = Lang.fileConfig.getString("convert-unsuccessful").replace("<item>", Material.DRIED_KELP.name());
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            }
                        } else if (inv == Material.NETHER_BRICK) {
                            int amount = player.getInventory().getItemInMainHand().getAmount();
                            if (amount >= 4) {
                                int blocks = amount / 4;
                                int remainder = amount % blocks;
                                player.sendMessage(String.valueOf(remainder));
                                ItemStack item = new ItemStack(Material.NETHER_BRICKS);
                                item.setAmount(blocks);
                                ItemStack item2 = new ItemStack(Material.NETHER_BRICK);
                                item2.setAmount(remainder);
                                player.getInventory().addItem(item2);
                                player.getInventory().setItemInMainHand(item);
                                String msg = Lang.fileConfig.getString("convert-successful").replace("<amount>", String.valueOf(amount)).replace("<item>", Material.NETHER_BRICK.name()).replace("<total>", String.valueOf(blocks)).replace("<block>", Material.NETHER_BRICKS.name());
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            } else {
                                String msg = Lang.fileConfig.getString("convert-unsuccessful").replace("<item>", Material.NETHER_BRICK.name());
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            }
                        } else if (inv == Material.AMETHYST_SHARD) {
                            int amount = player.getInventory().getItemInMainHand().getAmount();
                            if (amount >= 4) {
                                int blocks = amount / 4;
                                int remainder = amount % blocks;
                                player.sendMessage(String.valueOf(remainder));
                                ItemStack item = new ItemStack(Material.AMETHYST_BLOCK);
                                item.setAmount(blocks);
                                ItemStack item2 = new ItemStack(Material.AMETHYST_SHARD);
                                item2.setAmount(remainder);
                                player.getInventory().addItem(item2);
                                player.getInventory().setItemInMainHand(item);
                                String msg = Lang.fileConfig.getString("convert-successful").replace("<amount>", String.valueOf(amount)).replace("<item>", Material.AMETHYST_SHARD.name()).replace("<total>", String.valueOf(blocks)).replace("<block>", Material.AMETHYST_BLOCK.name());
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            } else {
                                String msg = Lang.fileConfig.getString("convert-unsuccessful").replace("<item>", Material.AMETHYST_SHARD.name());
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            }
                        } else if (inv == Material.POPPED_CHORUS_FRUIT) {
                            int amount = player.getInventory().getItemInMainHand().getAmount();
                            if (amount >= 4) {
                                int blocks = amount / 4;
                                int remainder = amount % blocks;
                                player.sendMessage(String.valueOf(remainder));
                                ItemStack item = new ItemStack(Material.PURPUR_BLOCK);
                                item.setAmount(blocks);
                                ItemStack item2 = new ItemStack(Material.POPPED_CHORUS_FRUIT);
                                item2.setAmount(remainder);
                                player.getInventory().addItem(item2);
                                player.getInventory().setItemInMainHand(item);
                                String msg = Lang.fileConfig.getString("convert-successful").replace("<amount>", String.valueOf(amount)).replace("<item>", Material.POPPED_CHORUS_FRUIT.name()).replace("<total>", String.valueOf(blocks)).replace("<block>", Material.PURPUR_BLOCK.name());
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            } else {
                                String msg = Lang.fileConfig.getString("convert-unsuccessful").replace("<item>", Material.POPPED_CHORUS_FRUIT.name());
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            }
                        } else if (inv == Material.HONEYCOMB) {
                            int amount = player.getInventory().getItemInMainHand().getAmount();
                            if (amount >= 4) {
                                int blocks = amount / 4;
                                int remainder = amount % blocks;
                                player.sendMessage(String.valueOf(remainder));
                                ItemStack item = new ItemStack(Material.HONEYCOMB_BLOCK);
                                item.setAmount(blocks);
                                ItemStack item2 = new ItemStack(Material.HONEYCOMB);
                                item2.setAmount(remainder);
                                player.getInventory().addItem(item2);
                                player.getInventory().setItemInMainHand(item);
                                String msg = Lang.fileConfig.getString("convert-successful").replace("<amount>", String.valueOf(amount)).replace("<item>", Material.HONEYCOMB.name()).replace("<total>", String.valueOf(blocks)).replace("<block>", Material.HONEYCOMB_BLOCK.name());
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            } else {
                                String msg = Lang.fileConfig.getString("convert-unsuccessful").replace("<item>", Material.HONEYCOMB.name());
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            }
                        } else if (inv == Material.HONEY_BOTTLE) {
                            int amount = player.getInventory().getItemInMainHand().getAmount();
                            if (amount >= 4) {
                                int blocks = amount / 4;
                                int remainder = amount % blocks;
                                player.sendMessage(String.valueOf(remainder));
                                ItemStack item = new ItemStack(Material.HONEY_BLOCK);
                                item.setAmount(blocks);
                                ItemStack item2 = new ItemStack(Material.HONEY_BOTTLE);
                                item2.setAmount(remainder);
                                player.getInventory().addItem(item2);
                                player.getInventory().setItemInMainHand(item);
                                String msg = Lang.fileConfig.getString("convert-successful").replace("<amount>", String.valueOf(amount)).replace("<item>", Material.HONEY_BOTTLE.name()).replace("<total>", String.valueOf(blocks)).replace("<block>", Material.HONEY_BLOCK.name());
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            } else {
                                String msg = Lang.fileConfig.getString("convert-unsuccessful").replace("<item>", Material.HONEY_BOTTLE.name());
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                return true;
                            }
                        }
                    } else {
                        String msg = Lang.fileConfig.getString("convert-invalid").replace("<item>", inv.name());
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
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
                            if (testItem.getType() == Material.NETHERITE_INGOT) {
                                int amount = player.getInventory().getItem(i).getAmount();
                                if (amount >= 9) {
                                    int blocks = amount / 9;
                                    int remainder = amount % blocks;
                                    ItemStack item = new ItemStack(Material.NETHERITE_BLOCK);
                                    item.setAmount(blocks);
                                    ItemStack item2 = new ItemStack(Material.NETHERITE_INGOT);
                                    item2.setAmount(remainder);
                                    player.getInventory().addItem(item2);
                                    player.getInventory().setItem(i, item);
                                    String msg = Lang.fileConfig.getString("convert-successful").replace("<amount>", String.valueOf(amount)).replace("<item>", Material.NETHERITE_INGOT.name()).replace("<total>", String.valueOf(blocks)).replace("<block>", Material.NETHERITE_BLOCK.name());
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                    continue;
                                }
                            } else if (testItem.getType() == Material.DIAMOND) {
                                int amount = player.getInventory().getItem(i).getAmount();
                                if (amount >= 9) {
                                    int blocks = amount / 9;
                                    int remainder = amount % blocks;
                                    ItemStack item = new ItemStack(Material.DIAMOND_BLOCK);
                                    item.setAmount(blocks);
                                    ItemStack item2 = new ItemStack(Material.DIAMOND);
                                    item2.setAmount(remainder);
                                    player.getInventory().addItem(item2);
                                    player.getInventory().setItem(i, item);
                                    String msg = Lang.fileConfig.getString("convert-successful").replace("<amount>", String.valueOf(amount)).replace("<item>", Material.DIAMOND.name()).replace("<total>", String.valueOf(blocks)).replace("<block>", Material.DIAMOND_BLOCK.name());
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                    continue;
                                }
                            } else if (testItem.getType() == Material.EMERALD) {
                                int amount = player.getInventory().getItem(i).getAmount();
                                if (amount >= 9) {
                                    int blocks = amount / 9;
                                    int remainder = amount % blocks;
                                    ItemStack item = new ItemStack(Material.EMERALD_BLOCK);
                                    item.setAmount(blocks);
                                    ItemStack item2 = new ItemStack(Material.EMERALD);
                                    item2.setAmount(remainder);
                                    player.getInventory().addItem(item2);
                                    player.getInventory().setItem(i, item);
                                    String msg = Lang.fileConfig.getString("convert-successful").replace("<amount>", String.valueOf(amount)).replace("<item>", Material.EMERALD.name()).replace("<total>", String.valueOf(blocks)).replace("<block>", Material.EMERALD_BLOCK.name());
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                    continue;
                                }
                            } else if (testItem.getType() == Material.REDSTONE) {
                                int amount = player.getInventory().getItem(i).getAmount();
                                if (amount >= 9) {
                                    int blocks = amount / 9;
                                    int remainder = amount % blocks;
                                    ItemStack item = new ItemStack(Material.REDSTONE_BLOCK);
                                    item.setAmount(blocks);
                                    ItemStack item2 = new ItemStack(Material.REDSTONE);
                                    item2.setAmount(remainder);
                                    player.getInventory().addItem(item2);
                                    player.getInventory().setItem(i, item);
                                    String msg = Lang.fileConfig.getString("convert-successful").replace("<amount>", String.valueOf(amount)).replace("<item>", Material.REDSTONE.name()).replace("<total>", String.valueOf(blocks)).replace("<block>", Material.REDSTONE_BLOCK.name());
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                    continue;
                                }
                            } else if (testItem.getType() == Material.LAPIS_LAZULI) {
                                int amount = player.getInventory().getItem(i).getAmount();
                                if (amount >= 9) {
                                    int blocks = amount / 9;
                                    int remainder = amount % blocks;
                                    ItemStack item = new ItemStack(Material.LAPIS_BLOCK);
                                    item.setAmount(blocks);
                                    ItemStack item2 = new ItemStack(Material.LAPIS_LAZULI);
                                    item2.setAmount(remainder);
                                    player.getInventory().addItem(item2);
                                    player.getInventory().setItem(i, item);
                                    String msg = Lang.fileConfig.getString("convert-successful").replace("<amount>", String.valueOf(amount)).replace("<item>", Material.LAPIS_LAZULI.name()).replace("<total>", String.valueOf(blocks)).replace("<block>", Material.LAPIS_BLOCK.name());
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                    continue;
                                }
                            } else if (testItem.getType() == Material.COAL) {
                                int amount = player.getInventory().getItem(i).getAmount();
                                if (amount >= 9) {
                                    int blocks = amount / 9;
                                    int remainder = amount % blocks;
                                    ItemStack item = new ItemStack(Material.COAL_BLOCK);
                                    item.setAmount(blocks);
                                    ItemStack item2 = new ItemStack(Material.COAL);
                                    item2.setAmount(remainder);
                                    player.getInventory().addItem(item2);
                                    player.getInventory().setItem(i, item);
                                    String msg = Lang.fileConfig.getString("convert-successful").replace("<amount>", String.valueOf(amount)).replace("<item>", Material.COAL.name()).replace("<total>", String.valueOf(blocks)).replace("<block>", Material.COAL_BLOCK.name());
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                    continue;
                                }
                            } else if (testItem.getType() == Material.RAW_GOLD) {
                                int amount = player.getInventory().getItem(i).getAmount();
                                if (amount >= 9) {
                                    int blocks = amount / 9;
                                    int remainder = amount % blocks;
                                    ItemStack item = new ItemStack(Material.RAW_GOLD_BLOCK);
                                    item.setAmount(blocks);
                                    ItemStack item2 = new ItemStack(Material.RAW_GOLD);
                                    item2.setAmount(remainder);
                                    player.getInventory().addItem(item2);
                                    player.getInventory().setItem(i, item);
                                    String msg = Lang.fileConfig.getString("convert-successful").replace("<amount>", String.valueOf(amount)).replace("<item>", Material.RAW_GOLD.name()).replace("<total>", String.valueOf(blocks)).replace("<block>", Material.RAW_GOLD_BLOCK.name());
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                    continue;
                                }
                            } else if (testItem.getType() == Material.GOLD_INGOT) {
                                int amount = player.getInventory().getItem(i).getAmount();
                                if (amount >= 9) {
                                    int blocks = amount / 9;
                                    int remainder = amount % blocks;
                                    ItemStack item = new ItemStack(Material.GOLD_BLOCK);
                                    item.setAmount(blocks);
                                    ItemStack item2 = new ItemStack(Material.GOLD_INGOT);
                                    item2.setAmount(remainder);
                                    player.getInventory().addItem(item2);
                                    player.getInventory().setItem(i, item);
                                    String msg = Lang.fileConfig.getString("convert-successful").replace("<amount>", String.valueOf(amount)).replace("<item>", Material.GOLD_INGOT.name()).replace("<total>", String.valueOf(blocks)).replace("<block>", Material.GOLD_BLOCK.name());
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                    continue;
                                }
                            } else if (testItem.getType() == Material.RAW_IRON) {
                                int amount = player.getInventory().getItem(i).getAmount();
                                if (amount >= 9) {
                                    int blocks = amount / 9;
                                    int remainder = amount % blocks;
                                    ItemStack item = new ItemStack(Material.RAW_IRON_BLOCK);
                                    item.setAmount(blocks);
                                    ItemStack item2 = new ItemStack(Material.RAW_IRON);
                                    item2.setAmount(remainder);
                                    player.getInventory().addItem(item2);
                                    player.getInventory().setItem(i, item);
                                    String msg = Lang.fileConfig.getString("convert-successful").replace("<amount>", String.valueOf(amount)).replace("<item>", Material.RAW_IRON.name()).replace("<total>", String.valueOf(blocks)).replace("<block>", Material.RAW_IRON_BLOCK.name());
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                    continue;
                                }
                            } else if (testItem.getType() == Material.IRON_INGOT) {
                                int amount = player.getInventory().getItem(i).getAmount();
                                if (amount >= 9) {
                                    int blocks = amount / 9;
                                    int remainder = amount % blocks;
                                    ItemStack item = new ItemStack(Material.IRON_BLOCK);
                                    item.setAmount(blocks);
                                    ItemStack item2 = new ItemStack(Material.IRON_INGOT);
                                    item2.setAmount(remainder);
                                    player.getInventory().addItem(item2);
                                    player.getInventory().setItem(i, item);
                                    String msg = Lang.fileConfig.getString("convert-successful").replace("<amount>", String.valueOf(amount)).replace("<item>", Material.IRON_INGOT.name()).replace("<total>", String.valueOf(blocks)).replace("<block>", Material.IRON_BLOCK.name());
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                    continue;
                                }
                            } else if (testItem.getType() == Material.RAW_COPPER) {
                                int amount = player.getInventory().getItem(i).getAmount();
                                if (amount >= 9) {
                                    int blocks = amount / 9;
                                    int remainder = amount % blocks;
                                    ItemStack item = new ItemStack(Material.RAW_COPPER_BLOCK);
                                    item.setAmount(blocks);
                                    ItemStack item2 = new ItemStack(Material.RAW_COPPER);
                                    item2.setAmount(remainder);
                                    player.getInventory().addItem(item2);
                                    player.getInventory().setItem(i, item);
                                    String msg = Lang.fileConfig.getString("convert-successful").replace("<amount>", String.valueOf(amount)).replace("<item>", Material.RAW_COPPER.name()).replace("<total>", String.valueOf(blocks)).replace("<block>", Material.RAW_COPPER_BLOCK.name());
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                    continue;
                                }
                            } else if (testItem.getType() == Material.COPPER_INGOT) {
                                int amount = player.getInventory().getItem(i).getAmount();
                                if (amount >= 9) {
                                    int blocks = amount / 9;
                                    int remainder = amount % blocks;
                                    ItemStack item = new ItemStack(Material.COPPER_BLOCK);
                                    item.setAmount(blocks);
                                    ItemStack item2 = new ItemStack(Material.COPPER_INGOT);
                                    item2.setAmount(remainder);
                                    player.getInventory().addItem(item2);
                                    player.getInventory().setItem(i, item);
                                    String msg = Lang.fileConfig.getString("convert-successful").replace("<amount>", String.valueOf(amount)).replace("<item>", Material.COPPER_INGOT.name()).replace("<total>", String.valueOf(blocks)).replace("<block>", Material.COPPER_BLOCK.name());
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                    continue;
                                }
                            } else if (testItem.getType() == Material.CLAY_BALL) {
                                int amount = player.getInventory().getItem(i).getAmount();
                                if (amount >= 4) {
                                    int blocks = amount / 4;
                                    int remainder = amount % blocks;
                                    ItemStack item = new ItemStack(Material.CLAY);
                                    item.setAmount(blocks);
                                    ItemStack item2 = new ItemStack(Material.CLAY_BALL);
                                    item2.setAmount(remainder);
                                    player.getInventory().addItem(item2);
                                    player.getInventory().setItem(i, item);
                                    String msg = Lang.fileConfig.getString("convert-successful").replace("<amount>", String.valueOf(amount)).replace("<item>", Material.CLAY_BALL.name()).replace("<total>", String.valueOf(blocks)).replace("<block>", Material.CLAY.name());
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                    continue;
                                }
                            } else if (testItem.getType() == Material.WHEAT) {
                                int amount = player.getInventory().getItem(i).getAmount();
                                if (amount >= 9) {
                                    int blocks = amount / 9;
                                    int remainder = amount % blocks;
                                    ItemStack item = new ItemStack(Material.HAY_BLOCK);
                                    item.setAmount(blocks);
                                    ItemStack item2 = new ItemStack(Material.WHEAT);
                                    item2.setAmount(remainder);
                                    player.getInventory().addItem(item2);
                                    player.getInventory().setItem(i, item);
                                    String msg = Lang.fileConfig.getString("convert-successful").replace("<amount>", String.valueOf(amount)).replace("<item>", Material.WHEAT.name()).replace("<total>", String.valueOf(blocks)).replace("<block>", Material.HAY_BLOCK.name());
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                    continue;
                                }
                            } else if (testItem.getType() == Material.BONE_MEAL) {
                                int amount = player.getInventory().getItem(i).getAmount();
                                if (amount >= 9) {
                                    int blocks = amount / 9;
                                    int remainder = amount % blocks;
                                    ItemStack item = new ItemStack(Material.BONE_BLOCK);
                                    item.setAmount(blocks);
                                    ItemStack item2 = new ItemStack(Material.BONE_MEAL);
                                    item2.setAmount(remainder);
                                    player.getInventory().addItem(item2);
                                    player.getInventory().setItem(i, item);
                                    String msg = Lang.fileConfig.getString("convert-successful").replace("<amount>", String.valueOf(amount)).replace("<item>", Material.BONE_MEAL.name()).replace("<total>", String.valueOf(blocks)).replace("<block>", Material.BONE_BLOCK.name());
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                    continue;
                                }
                            } else if (testItem.getType() == Material.SLIME_BALL) {
                                int amount = player.getInventory().getItem(i).getAmount();
                                if (amount >= 9) {
                                    int blocks = amount / 9;
                                    int remainder = amount % blocks;
                                    ItemStack item = new ItemStack(Material.SLIME_BLOCK);
                                    item.setAmount(blocks);
                                    ItemStack item2 = new ItemStack(Material.SLIME_BALL);
                                    item2.setAmount(remainder);
                                    player.getInventory().addItem(item2);
                                    player.getInventory().setItem(i, item);
                                    String msg = Lang.fileConfig.getString("convert-successful").replace("<amount>", String.valueOf(amount)).replace("<item>", Material.SLIME_BALL.name()).replace("<total>", String.valueOf(blocks)).replace("<block>", Material.SLIME_BLOCK.name());
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                    continue;
                                }
                            } else if (testItem.getType() == Material.STRING) {
                                int amount = player.getInventory().getItem(i).getAmount();
                                if (amount >= 4) {
                                    int blocks = amount / 4;
                                    int remainder = amount % blocks;
                                    ItemStack item = new ItemStack(Material.WHITE_WOOL);
                                    item.setAmount(blocks);
                                    ItemStack item2 = new ItemStack(Material.STRING);
                                    item2.setAmount(remainder);
                                    player.getInventory().addItem(item2);
                                    player.getInventory().setItem(i, item);
                                    String msg = Lang.fileConfig.getString("convert-successful").replace("<amount>", String.valueOf(amount)).replace("<item>", Material.STRING.name()).replace("<total>", String.valueOf(blocks)).replace("<block>", Material.WHITE_WOOL.name());
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                    continue;
                                }
                            } else if (testItem.getType() == Material.QUARTZ) {
                                int amount = player.getInventory().getItem(i).getAmount();
                                if (amount >= 4) {
                                    int blocks = amount / 4;
                                    int remainder = amount % blocks;
                                    ItemStack item = new ItemStack(Material.QUARTZ_BLOCK);
                                    item.setAmount(blocks);
                                    ItemStack item2 = new ItemStack(Material.QUARTZ);
                                    item2.setAmount(remainder);
                                    player.getInventory().addItem(item2);
                                    player.getInventory().setItem(i, item);
                                    String msg = Lang.fileConfig.getString("convert-successful").replace("<amount>", String.valueOf(amount)).replace("<item>", Material.QUARTZ.name()).replace("<total>", String.valueOf(blocks)).replace("<block>", Material.QUARTZ_BLOCK.name());
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                    continue;
                                }
                            } else if (testItem.getType() == Material.NETHER_WART) {
                                int amount = player.getInventory().getItem(i).getAmount();
                                if (amount >= 9) {
                                    int blocks = amount / 9;
                                    int remainder = amount % blocks;
                                    ItemStack item = new ItemStack(Material.NETHER_WART_BLOCK);
                                    item.setAmount(blocks);
                                    ItemStack item2 = new ItemStack(Material.NETHER_WART);
                                    item2.setAmount(remainder);
                                    player.getInventory().addItem(item2);
                                    player.getInventory().setItem(i, item);
                                    String msg = Lang.fileConfig.getString("convert-successful").replace("<amount>", String.valueOf(amount)).replace("<item>", Material.NETHER_WART.name()).replace("<total>", String.valueOf(blocks)).replace("<block>", Material.NETHER_WART_BLOCK.name());
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                    continue;
                                }
                            } else if (testItem.getType() == Material.MAGMA_CREAM) {
                                int amount = player.getInventory().getItem(i).getAmount();
                                if (amount >= 4) {
                                    int blocks = amount / 4;
                                    int remainder = amount % blocks;
                                    ItemStack item = new ItemStack(Material.MAGMA_BLOCK);
                                    item.setAmount(blocks);
                                    ItemStack item2 = new ItemStack(Material.MAGMA_CREAM);
                                    item2.setAmount(remainder);
                                    player.getInventory().addItem(item2);
                                    player.getInventory().setItem(i, item);
                                    String msg = Lang.fileConfig.getString("convert-successful").replace("<amount>", String.valueOf(amount)).replace("<item>", Material.MAGMA_CREAM.name()).replace("<total>", String.valueOf(blocks)).replace("<block>", Material.MAGMA_BLOCK.name());
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                    continue;
                                }
                            } else if (testItem.getType() == Material.DRIED_KELP) {
                                int amount = player.getInventory().getItem(i).getAmount();
                                if (amount >= 9) {
                                    int blocks = amount / 9;
                                    int remainder = amount % blocks;
                                    ItemStack item = new ItemStack(Material.DRIED_KELP_BLOCK);
                                    item.setAmount(blocks);
                                    ItemStack item2 = new ItemStack(Material.DRIED_KELP);
                                    item2.setAmount(remainder);
                                    player.getInventory().addItem(item2);
                                    player.getInventory().setItem(i, item);
                                    String msg = Lang.fileConfig.getString("convert-successful").replace("<amount>", String.valueOf(amount)).replace("<item>", Material.DRIED_KELP.name()).replace("<total>", String.valueOf(blocks)).replace("<block>", Material.DRIED_KELP_BLOCK.name());
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                    continue;
                                }
                            } else if (testItem.getType() == Material.NETHER_BRICK) {
                                int amount = player.getInventory().getItem(i).getAmount();
                                if (amount >= 4) {
                                    int blocks = amount / 4;
                                    int remainder = amount % blocks;
                                    ItemStack item = new ItemStack(Material.NETHER_BRICKS);
                                    item.setAmount(blocks);
                                    ItemStack item2 = new ItemStack(Material.NETHER_BRICK);
                                    item2.setAmount(remainder);
                                    player.getInventory().addItem(item2);
                                    player.getInventory().setItem(i, item);
                                    String msg = Lang.fileConfig.getString("convert-successful").replace("<amount>", String.valueOf(amount)).replace("<item>", Material.NETHER_BRICK.name()).replace("<total>", String.valueOf(blocks)).replace("<block>", Material.NETHER_BRICKS.name());
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                    continue;
                                }
                            } else if (testItem.getType() == Material.AMETHYST_SHARD) {
                                int amount = player.getInventory().getItem(i).getAmount();
                                if (amount >= 4) {
                                    int blocks = amount / 4;
                                    int remainder = amount % blocks;
                                    ItemStack item = new ItemStack(Material.AMETHYST_BLOCK);
                                    item.setAmount(blocks);
                                    ItemStack item2 = new ItemStack(Material.AMETHYST_SHARD);
                                    item2.setAmount(remainder);
                                    player.getInventory().addItem(item2);
                                    player.getInventory().setItem(i, item);
                                    String msg = Lang.fileConfig.getString("convert-successful").replace("<amount>", String.valueOf(amount)).replace("<item>", Material.AMETHYST_SHARD.name()).replace("<total>", String.valueOf(blocks)).replace("<block>", Material.AMETHYST_BLOCK.name());
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                    continue;
                                }
                            } else if (testItem.getType() == Material.POPPED_CHORUS_FRUIT) {
                                int amount = player.getInventory().getItem(i).getAmount();
                                if (amount >= 4) {
                                    int blocks = amount / 4;
                                    int remainder = amount % blocks;
                                    ItemStack item = new ItemStack(Material.PURPUR_BLOCK);
                                    item.setAmount(blocks);
                                    ItemStack item2 = new ItemStack(Material.POPPED_CHORUS_FRUIT);
                                    item2.setAmount(remainder);
                                    player.getInventory().addItem(item2);
                                    player.getInventory().setItem(i, item);
                                    String msg = Lang.fileConfig.getString("convert-successful").replace("<amount>", String.valueOf(amount)).replace("<item>", Material.POPPED_CHORUS_FRUIT.name()).replace("<total>", String.valueOf(blocks)).replace("<block>", Material.PURPUR_BLOCK.name());
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                    continue;
                                }
                            } else if (testItem.getType() == Material.HONEYCOMB) {
                                int amount = player.getInventory().getItem(i).getAmount();
                                if (amount >= 4) {
                                    int blocks = amount / 4;
                                    int remainder = amount % blocks;
                                    ItemStack item = new ItemStack(Material.HONEYCOMB_BLOCK);
                                    item.setAmount(blocks);
                                    ItemStack item2 = new ItemStack(Material.HONEYCOMB);
                                    item2.setAmount(remainder);
                                    player.getInventory().addItem(item2);
                                    player.getInventory().setItem(i, item);
                                    String msg = Lang.fileConfig.getString("convert-successful").replace("<amount>", String.valueOf(amount)).replace("<item>", Material.HONEYCOMB.name()).replace("<total>", String.valueOf(blocks)).replace("<block>", Material.HONEYCOMB_BLOCK.name());
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                    continue;
                                }
                            } else if (testItem.getType() == Material.HONEY_BOTTLE) {
                                int amount = player.getInventory().getItem(i).getAmount();
                                if (amount >= 4) {
                                    int blocks = amount / 4;
                                    int remainder = amount % blocks;
                                    ItemStack item = new ItemStack(Material.HONEY_BLOCK);
                                    item.setAmount(blocks);
                                    ItemStack item2 = new ItemStack(Material.HONEY_BOTTLE);
                                    item2.setAmount(remainder);
                                    player.getInventory().addItem(item2);
                                    player.getInventory().setItem(i, item);
                                    String msg = Lang.fileConfig.getString("convert-successful").replace("<amount>", String.valueOf(amount)).replace("<item>", Material.HONEY_BOTTLE.name()).replace("<total>", String.valueOf(blocks)).replace("<block>", Material.HONEY_BLOCK.name());
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                    continue;
                                }
                            }
                        }
                    }
                }
            }else{
                String perm = Lang.fileConfig.getString("no-permission-message").replace("<permission>", "se.convert");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', perm));
                return true;
            }
        }
        return false;
    }

    public static void setItems (){
        items.add(Material.NETHERITE_INGOT);

        items.add(Material.DIAMOND);

        items.add(Material.EMERALD);

        items.add(Material.REDSTONE);

        items.add(Material.LAPIS_LAZULI);

        items.add(Material.COAL);

        items.add(Material.RAW_GOLD);
        items.add(Material.GOLD_INGOT);

        items.add(Material.RAW_IRON);
        items.add(Material.IRON_INGOT);

        items.add(Material.RAW_COPPER);
        items.add(Material.COPPER_INGOT);

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

        items.add(Material.AMETHYST_SHARD);

        items.add(Material.POPPED_CHORUS_FRUIT);

        items.add(Material.HONEY_BOTTLE);

        items.add(Material.HONEYCOMB);
    }

    public static ArrayList<Material> getItems() {
        return items;
    }
}