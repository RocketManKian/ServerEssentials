package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static me.rocketmankianproductions.serveressentials.ServerEssentials.hex;

public class TeleportRequest implements CommandExecutor {

    public static HashMap<UUID, UUID> tpa = new HashMap<>();
    public static HashMap<UUID, UUID> tpahere = new HashMap<>();
    public static HashMap<UUID, Integer> teleportcancel = new HashMap<>();
    public static HashMap<UUID, Integer> teleportcooldown = new HashMap<>();
    public static HashMap<UUID, Integer> teleportherecooldown = new HashMap<>();
    public static HashMap<UUID, Integer> teleport = new HashMap<>();
    public static ArrayList<UUID> cancel = new ArrayList<>();
    public static ArrayList<UUID> cancel2 = new ArrayList<>();
    int time;
    public static int taskID;

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        int tpwait = ServerEssentials.plugin.getConfig().getInt("teleport-wait");
        Long delay = ServerEssentials.getPlugin().getConfig().getLong("teleport-cancel");
        int delay2 = (int) (delay * 20);
        int delay3 = delay2 / 20;
        // TPA
        Long delay4 = ServerEssentials.getPlugin().getConfig().getLong("tp-cooldown");
        int delay5 = (int) (delay4 * 20);
        int delay6 = delay5 / 20;
        if (!(sender instanceof Player)) {
            String console = Lang.fileConfig.getString("console-invalid");
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', hex(console)));
            return true;
        }
        Player player = (Player) sender;
        if (command.getName().equalsIgnoreCase("tpa")) {
            if (ServerEssentials.permissionChecker(player, "se.tpa")) {
                if (args.length >= 1) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target != null) {
                        if (target != player) {
                            if (!blacklistCheck(player, target, "teleport-request-blacklisted-world")){
                                if (!teleportcooldown.containsKey(player.getUniqueId()) || player.hasPermission("se.teleport.bypass")){
                                    if (TPToggle.fileConfig.getBoolean("tptoggle." + target.getName()) == false) {
                                        tpa.put(target.getUniqueId(), player.getUniqueId());
                                        teleportRequestMessage(player, target, "teleport-request-sent", "teleport-request-target-receive", "teleport-request-timeout-warning", "teleport-request-cancel-warning", "teleport-request-accept", "teleport-request-deny", delay3);
                                        if (teleportcancel.containsKey(target.getUniqueId()) && teleportcancel.get(target.getUniqueId()) != null) {
                                            cancelTimeout(target);
                                        }
                                        teleportcancel.put(target.getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((ServerEssentials.getPlugin()), new Runnable() {
                                            public void run() {
                                                if (tpa.containsKey(target.getUniqueId())) {
                                                    String msg = Lang.fileConfig.getString("teleport-request-timeout");
                                                    target.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                                                    tpa.remove(target.getUniqueId());
                                                }
                                            }
                                        }, delay2));
                                        teleportcooldown.put(player.getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((ServerEssentials.getPlugin()), new Runnable() {
                                            public void run() {
                                                teleportcooldown.remove(player.getUniqueId());
                                            }
                                        }, delay5));
                                        setTimer(delay6);
                                        startTimer();
                                        return true;
                                    } else {
                                        String msg = Lang.fileConfig.getString("teleport-disabled");
                                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                                        return true;
                                    }
                                }
                                if (teleportcooldown.containsKey(player.getUniqueId()) && teleportcooldown.get(player.getUniqueId()) != null) {
                                    String msg = Lang.fileConfig.getString("command-timeout").replace("<time>", String.valueOf(time));
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                                    return true;
                                }
                            }
                        } else {
                            String msg = Lang.fileConfig.getString("teleport-self");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                            return true;
                        }
                    } else {
                        String msg = Lang.fileConfig.getString("target-offline");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                        return true;
                    }
                }else{
                    String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/tpa (player)");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                    return true;
                }
            }
        }
        if (command.getName().equalsIgnoreCase("tpahere")) {
            if (ServerEssentials.permissionChecker(player, "se.tpahere")) {
                if (args.length == 1) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target != null) {
                        if (target != player) {
                            if (!blacklistCheck(player, target, "teleport-here-blacklisted-world")){
                                if (!teleportherecooldown.containsKey(player.getUniqueId()) || player.hasPermission("se.teleport.bypass")){
                                    if (TPToggle.fileConfig.getBoolean("tptoggle." + target.getName(), false) == false) {
                                        tpahere.put(target.getUniqueId(), player.getUniqueId());
                                        teleportRequestMessage(player, target, "teleport-here-request-sent", "teleport-here-request-target-receive", "teleport-here-request-timeout-warning", "teleport-here-request-cancel-warning", "teleport-here-request-accept", "teleport-here-request-deny", delay3);
                                        if (teleportcancel.containsKey(target.getUniqueId()) && teleportcancel.get(target.getUniqueId()) != null) {
                                            cancelTimeout(target);
                                        }
                                        teleportcancel.put(target.getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((ServerEssentials.getPlugin()), new Runnable() {
                                            public void run() {
                                                if (tpahere.containsKey(target.getUniqueId())) {
                                                    String msg = Lang.fileConfig.getString("teleport-here-request-timeout");
                                                    target.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                                                    tpahere.remove(target.getUniqueId());
                                                }
                                            }
                                        }, delay2));
                                        teleportherecooldown.put(player.getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((ServerEssentials.getPlugin()), new Runnable() {
                                            public void run() {
                                                teleportherecooldown.remove(player.getUniqueId());
                                            }
                                        }, delay5));
                                        setTimer(delay6);
                                        startTimer();
                                        return true;
                                    } else {
                                        String msg = Lang.fileConfig.getString("teleport-disabled");
                                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                                        return true;
                                    }
                                }
                                if (teleportherecooldown.containsKey(player.getUniqueId()) && teleportherecooldown.get(player.getUniqueId()) != null) {
                                    String msg = Lang.fileConfig.getString("command-timeout").replace("<time>", String.valueOf(time));
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                                    return true;
                                }
                            }
                        } else {
                            String msg = Lang.fileConfig.getString("teleport-self");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                            return true;
                        }
                    } else {
                        String msg = Lang.fileConfig.getString("target-offline");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                        return true;
                    }
                }else{
                    String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/tpahere (player)");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                    return true;
                }
            }
        }
        if (command.getName().equalsIgnoreCase("tpacancel")) {
            if (ServerEssentials.permissionChecker(player, "se.tpacancel")) {
                if (tpa.containsKey(getKey(tpa, player.getUniqueId())) || tpahere.containsKey(getKey(tpahere, player.getUniqueId()))) {
                    String msg = Lang.fileConfig.getString("teleport-cancel");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                    if (teleportcancel.containsKey(player.getUniqueId()) && teleportcancel.get(player.getUniqueId()) != null){
                        if (teleportcancel.get(getKey(tpa, player.getUniqueId())) != null){
                            Bukkit.getScheduler().cancelTask(teleportcancel.get(getKey(tpa, player.getUniqueId())));
                        }else if (teleportcancel.get(getKey(tpahere, player.getUniqueId())) != null){
                            Bukkit.getScheduler().cancelTask(teleportcancel.get(getKey(tpahere, player.getUniqueId())));
                        }
                    }
                    tpa.remove(getKey(tpa, player.getUniqueId()));
                    tpahere.remove(getKey(tpahere, player.getUniqueId()));
                    return true;
                } else if (!tpahere.containsKey(player.getUniqueId()) || !tpa.containsKey(player.getUniqueId())) {
                    String msg = Lang.fileConfig.getString("teleport-no-request");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                    return true;
                }
            }
        }
        if (command.getName().equalsIgnoreCase("tpaccept")) {
            if (ServerEssentials.permissionChecker(player, "se.tpaccept")) {
                if (ServerEssentials.plugin.getConfig().getInt("teleport-wait") == 0){
                    if (tpa.containsKey(player.getUniqueId())) {
                        Player target = Bukkit.getPlayer(tpa.get(player.getUniqueId()));
                        Teleport.teleportSave(target);
                        target.teleport(player);
                        teleportSuccessMessage(target, player, "tpa");
                        cancelTimeout(player);
                        tpa.remove(player.getUniqueId());
                        return true;
                    } else if (tpahere.containsKey(player.getUniqueId())) {
                        Player target = Bukkit.getPlayer(tpahere.get(player.getUniqueId()));
                        Teleport.teleportSave(player);
                        player.teleport(target);
                        teleportSuccessMessage(player, target, "tpahere");
                        cancelTimeout(player);
                        tpahere.remove(player.getUniqueId());
                        return true;
                    } else if (tpahere.get(player.getUniqueId()) == null || tpa.get(player.getUniqueId()) == null) {
                        String msg = Lang.fileConfig.getString("teleport-no-request-accept");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                        return true;
                    }
                }else{
                    if (ServerEssentials.plugin.getConfig().getBoolean("teleport-movement-cancel")){
                        if (tpa.containsKey(player.getUniqueId())) {
                            Player target = Bukkit.getPlayer(tpa.get(player.getUniqueId()));
                            cancelTimeout(player);
                            cancel.add(target.getUniqueId());
                            String msg = Lang.fileConfig.getString("teleport-accept-request-target").replace("<target>", target.getName());
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                            String msg2 = Lang.fileConfig.getString("teleport-wait-message").replace("<player>", player.getName()).replace("<time>", String.valueOf(tpwait));
                            target.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg2)));
                            tpwait = tpwait * 20;
                            tpa.remove(player.getUniqueId());
                            teleportSuccess(player, target, tpwait, "tpa", true);
                            return true;
                        } else if (tpahere.containsKey(player.getUniqueId())) {
                            Player target = Bukkit.getPlayer(tpahere.get(player.getUniqueId()));
                            cancelTimeout(player);
                            cancel2.add(player.getUniqueId());
                            String msg = Lang.fileConfig.getString("teleport-accept-request").replace("<sender>", player.getName());
                            Bukkit.getPlayer(tpahere.get(player.getUniqueId())).sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                            String msg2 = Lang.fileConfig.getString("teleport-wait-message").replace("<player>", target.getName()).replace("<time>", String.valueOf(tpwait));
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg2)));
                            tpwait = tpwait * 20;
                            tpahere.remove(player.getUniqueId());
                            teleportSuccess(player, target, tpwait, "tpahere", true);
                            return true;
                        } else if (tpahere.get(player.getUniqueId()) == null || tpa.get(player.getUniqueId()) == null) {
                            String msg = Lang.fileConfig.getString("teleport-no-request-accept");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                            return true;
                        }
                    }else{
                        if (tpa.containsKey(player.getUniqueId())) {
                            Player target = Bukkit.getPlayer(tpa.get(player.getUniqueId()));
                            cancelTimeout(player);
                            String msg = Lang.fileConfig.getString("teleport-accept-request-target").replace("<target>", target.getName());
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                            String msg2 = Lang.fileConfig.getString("teleport-wait-message").replace("<player>", player.getName()).replace("<time>", String.valueOf(tpwait));
                            target.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg2)));
                            tpwait = tpwait * 20;
                            tpa.remove(player.getUniqueId());
                            teleportSuccess(player, target, tpwait, "tpa", false);
                            return true;
                        } else if (tpahere.containsKey(player.getUniqueId())) {
                            Player target = Bukkit.getPlayer(tpahere.get(player.getUniqueId()));
                            cancelTimeout(player);
                            String msg = Lang.fileConfig.getString("teleport-wait-message").replace("<player>", target.getName()).replace("<time>", String.valueOf(tpwait));
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                            String msg2 = Lang.fileConfig.getString("teleport-accept-request").replace("<sender>", player.getName());
                            Bukkit.getPlayer(tpahere.get(player.getUniqueId())).sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg2)));
                            tpwait = tpwait * 20;
                            tpahere.remove(player.getUniqueId());
                            teleportSuccess(player, target, tpwait, "tpahere", false);
                            return true;
                        } else if (tpahere.get(player.getUniqueId()) == null || tpa.get(player.getUniqueId()) == null) {
                            String msg = Lang.fileConfig.getString("teleport-no-request-accept");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                            return true;
                        }
                    }
                }
            }
        }
        if (command.getName().equalsIgnoreCase("tpdeny")) {
            if (ServerEssentials.permissionChecker(player, "se.tpdeny")) {
                if (tpa.containsKey(player.getUniqueId())) {
                    Player target = Bukkit.getPlayer(tpa.get(player.getUniqueId()));
                    String msg = Lang.fileConfig.getString("teleport-deny-request-target").replace("<target>", target.getName());
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                    String msg2 = Lang.fileConfig.getString("teleport-deny-request").replace("<sender>", player.getName());
                    Bukkit.getPlayer(tpa.get(player.getUniqueId())).sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg2)));
                    if (teleportcancel.containsKey(player.getUniqueId()) && teleportcancel.get(player.getUniqueId()) != null){
                        Bukkit.getScheduler().cancelTask(teleportcancel.get(player.getUniqueId()));
                    }
                    tpa.remove(player.getUniqueId());
                    return true;
                } else if (tpahere.containsKey(player.getUniqueId())) {
                    Player target = Bukkit.getPlayer(tpahere.get(player.getUniqueId()));
                    String msg = Lang.fileConfig.getString("teleport-deny-request-target").replace("<target>", target.getName());
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                    String msg2 = Lang.fileConfig.getString("teleport-deny-request").replace("<sender>", player.getName());
                    Bukkit.getPlayer(tpahere.get(player.getUniqueId())).sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg2)));
                    if (teleportcancel.containsKey(player.getUniqueId()) && teleportcancel.get(player.getUniqueId()) != null){
                        Bukkit.getScheduler().cancelTask(teleportcancel.get(player.getUniqueId()));
                    }
                    tpahere.remove(player.getUniqueId());
                    return true;
                } else if (!tpahere.containsKey(player.getUniqueId()) || !tpa.containsKey(player.getUniqueId())) {
                    String msg = Lang.fileConfig.getString("teleport-no-request-deny");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                    return true;
                }
            }
        }
        return true;
    }

    public <K, V> K getKey(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static void cancelTimeout (Player target){
        Bukkit.getScheduler().cancelTask(teleportcancel.get(target.getUniqueId()));
    }

    public void setTimer(int amount) {
        time = amount;
    }
    public void startTimer() {
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        taskID = scheduler.scheduleSyncRepeatingTask(ServerEssentials.plugin, new Runnable() {
            @Override
            public void run() {
                if(time == 0) {
                    stopTimer();
                    return;
                }
                time = time - 1;
            }
        }, 0L, 20L);
    }
    public void stopTimer() {
        Bukkit.getScheduler().cancelTask(taskID);
    }

    public void teleportRequestMessage(Player player, Player target, String requestsent, String targetreceive, String timeoutwarning, String teleportcancelwarning, String teleportrequestaccept, String teleportrequestdeny, int delay3){
        String msg = Lang.fileConfig.getString(requestsent).replace("<target>", target.getName());
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
        TextComponent message = new TextComponent(ChatColor.translateAlternateColorCodes('&', hex(Lang.fileConfig.getString(teleportcancelwarning))));
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.RED + "Cancel Teleport Here Request")));
        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpacancel"));
        player.spigot().sendMessage(message);
        String msg3 = Lang.fileConfig.getString(targetreceive).replace("<sender>", player.getName());
        target.sendMessage(ChatColor.translateAlternateColorCodes('&', msg3));
        TextComponent message2 = new TextComponent(ChatColor.translateAlternateColorCodes('&', hex(Lang.fileConfig.getString(teleportrequestaccept))));
        message2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.GREEN + "Accept Teleport Request")));
        message2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaccept"));
        target.spigot().sendMessage(message2);
        TextComponent message3 = new TextComponent(ChatColor.translateAlternateColorCodes('&', hex(Lang.fileConfig.getString(teleportrequestdeny))));
        message3.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.RED + "Deny Teleport Request")));
        message3.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpdeny"));
        target.spigot().sendMessage(message3);
        String msg4 = Lang.fileConfig.getString(timeoutwarning).replace("<time>", String.valueOf(delay3));
        target.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg4)));
    }

    public void teleportSuccessMessage(Player player, Player target, String tp){
        String msg = Lang.fileConfig.getString("teleport-target-success").replace("<sender>", player.getName());
        String msg2 = Lang.fileConfig.getString("teleport-success").replace("<target>", target.getName());
        if (tp.equalsIgnoreCase("tpahere")){
            target.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg2)));
        }else if (tp.equalsIgnoreCase("tpa")){
            target.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg2)));
        }
    }

    public static Boolean blacklistCheck(Player player, Player target, String lang){
        boolean blacklistedworld = false;
        if (ServerEssentials.plugin.getConfig().getBoolean("enable-teleport-blacklist")){
            for (String worlds : ServerEssentials.plugin.getConfig().getStringList("teleport-blacklist")) {
                if (player.getWorld().getName().equalsIgnoreCase(worlds)) {
                    if (ServerEssentials.plugin.getConfig().getBoolean("enable-teleport-in-world")){
                        String world = player.getWorld().getName();
                        if (target.getWorld().getName() != world){
                            String msg = Lang.fileConfig.getString(lang);
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                            blacklistedworld = true;
                        }
                    }else{
                        String msg = Lang.fileConfig.getString(lang);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', hex(msg)));
                        blacklistedworld = true;
                    }
                }
            }
        }
        return blacklistedworld;
    }

    public void teleportSuccess(Player player, Player target, int tpwait, String tp, boolean movement){
        if (teleport.containsKey(player.getUniqueId()) && teleport.get(player.getUniqueId()) != null) {
            Bukkit.getScheduler().cancelTask(teleport.get(player.getUniqueId()));
        }
        teleport.put(player.getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((ServerEssentials.plugin), new Runnable() {
            public void run() {
                if (movement){
                    if (cancel.contains(target.getUniqueId()) || cancel2.contains(player.getUniqueId())){
                        if (teleport.containsKey(player.getUniqueId())) {
                            // Teleporting Player
                            if (tp.equalsIgnoreCase("tpahere")){
                                Teleport.teleportSave(player);
                                player.teleport(target);
                                teleportSuccessMessage(player, target, "tpahere");
                                tpahere.remove(target.getUniqueId());
                            }else if (tp.equalsIgnoreCase("tpa")){
                                Teleport.teleportSave(target);
                                target.teleport(player);
                                teleportSuccessMessage(target, player, "tpa");
                                tpa.remove(player.getUniqueId());
                            }
                            teleport.remove(player.getUniqueId());
                            cancel.remove(target.getUniqueId());
                            cancel2.remove(player.getUniqueId());
                        }
                    }
                }else{
                    if (teleport.containsKey(player.getUniqueId())) {
                        // Teleporting Player
                        if (tp.equalsIgnoreCase("tpahere")){
                            Teleport.teleportSave(player);
                            player.teleport(target);
                            teleportSuccessMessage(player, target, "tpahere");
                            tpahere.remove(target.getUniqueId());
                        }else if (tp.equalsIgnoreCase("tpa")){
                            Teleport.teleportSave(target);
                            target.teleport(player);
                            teleportSuccessMessage(target, player, "tpa");
                            tpa.remove(player.getUniqueId());
                        }
                        teleport.remove(player.getUniqueId());
                    }
                }
            }
        }, tpwait));
    }
}
