package me.rocketmankianproductions.serveressentials.commands;

import me.rocketmankianproductions.serveressentials.ServerEssentials;
import me.rocketmankianproductions.serveressentials.file.Lang;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

import static me.rocketmankianproductions.serveressentials.ServerEssentials.plugin;

public class SE implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                boolean hasPerm = ServerEssentials.permissionChecker(player, "se.info");
                if (hasPerm) {
                    player.sendMessage(ChatColor.GREEN + "---------------------------"
                            + "\nServer Essentials Commands"
                            + "\n---------------------------"
                            + ChatColor.GOLD + "\n/se reload - Reloads Configuration"
                            + "\n/se version - Shows Plugin Version"
                            + "\n/se silentjoin - Toggles joining and leaving message"
                            + "\n/website - Shows Website Of Choice"
                            + "\n/discord - Shows Discord Of Choice"
                            + "\n/rules - Displays Rules"
                            + "\n/playtime - Displays Playtime"
                            + "\n/playtime (name) - Displays Playtime for another user"
                            + "\n/hurt - Hurts A Player For A Specified Amount Of Hearts"
                            + "\n/setspawn - Set the default spawnpoint"
                            + "\n/spawn - Teleports to spawn"
                            + "\n/deletespawn - Deletes the spawnpoint"
                            + "\n/sethome (name) - Creates a Home with the specified name"
                            + "\n/home (name) - Teleports you to a specified home"
                            + "\n/home (name) (home name) - Teleports you to Target's Home"
                            + "\n/deletehome (name) - Deletes the Home with the name you specified"
                            + "\n/sendhome (name) (homename) - Sends Target to their Home"
                            + "\n/listhomes (name) - Lists all the Target's homes"
                            + "\n/tp - Teleporting System"
                            + "\n/tphere - Teleport someone to you"
                            + "\n/tpall - Teleports every online player to your location"
                            + "\n/tpa - Request to Teleport to someone"
                            + "\n/tpacancel - Cancels the Teleport Request you have sent"
                            + "\n/tpahere - Request someone to Teleport to you"
                            + "\n/tpaccept - Accepts a Teleport Request"
                            + "\n/tpdeny - Denies a Teleport Request"
                            + "\n/heal - Heals the Target to full health"
                            + "\n/feed - Feeds the Target to full hunger"
                            + "\n/announce - Announces a Message to the entire Server"
                            + "\n/tptoggle - Toggles whether people can Teleport to you or not"
                            + "\n/invsee - Shows the Target's Inventory"
                            + "\n/gamemode creative - Sets you to Creative"
                            + "\n/gamemode survival - Sets you to Survival"
                            + "\n/gamemode adventure - Sets you to Adventure"
                            + "\n/gamemode spectator - Sets you to Spectator"
                            + "\n/kill - Kills the Target"
                            + "\n/msg (name) - Privately Messages the Player"
                            + "\n/r (message) - Message the last person who Messaged you"
                            + "\n/msgtoggle - Toggles whether people can Message you or not"
                            + "\n/craft - Opens the Crafting GUI"
                            + "\n/setwarp (name) - Sets a Warp with the Following Name"
                            + "\n/deletewarp (name) - Deletes the Warp with the Following Name"
                            + "\n/warp (name) - Warps to the Following Name"
                            + "\n/repair - Repairs the item you are holding"
                            + "\n/repair all - Repairs all items in your Inventory"
                            + "\n/sunrise - Changes Time to Sunrise"
                            + "\n/day - Changes Time to Day"
                            + "\n/sunset - Changes Time to Sunset"
                            + "\n/midnight - Changes Time to Midnight"
                            + "\n/vanish - Vanish Command"
                            + "\n/test join - Test's the Join Symbol"
                            + "\n/test leave - Test's the Leave Symbol"
                            + "\n/test welcome - Test's the Welcome Message"
                            + "\n/test permission - Test's the Custom Permission Message"
                            + "\n/test motd - Test's the MOTD"
                            + "\n/clear - Clears entire Inventory"
                            + "\n/god - Sets yourself, or the targeted player, into Godmode"
                            + "\n/hat - Puts whatever item you are holding onto your head"
                            + "\n/rename - Changes the Name for the item you are holding"
                            + "\n/lore - Changes the Lore for the item you are holding"
                            + "\n/speed (value) - Changes your Walking/Flying speed"
                            + "\n/clearchat - Clears the Chat"
                            + "\n/sendwarp - Sends Target to the specified Warp"
                            + "\n/socialspy - Enables or Disables the SocialSpy Feature"
                            + "\n/staffchat - Enables or Disables the StaffChat Command"
                            + "\n/trash - Opens the Trash Chute GUI"
                            + "\n/report (user) - Report specified User to Admins"
                            + "\n/reportbug (bug) - Report specified Bug to Admins"
                            + "\n/back - Teleports to Previous Location"
                            + "\n/convert - Converts Items into Block Form");
                    return true;
                }
            }else if (args.length == 1){
                if (args[0].equals("reload")) {
                    new Reload(plugin).run(sender, Arrays.copyOfRange(args, 1, args.length));
                }
                if (args[0].equals("version")) {
                    new Version(plugin).run(sender, Arrays.copyOfRange(args, 1, args.length));
                }
                if (args[0].equals("silentjoin")) {
                    new SilentJoin(plugin).run(sender, Arrays.copyOfRange(args, 1, args.length));
                }
                return true;
            }else{
                String msg = Lang.fileConfig.getString("incorrect-format").replace("<command>", "/se");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                return true;
            }
        } else if (args.length == 1 && sender instanceof ConsoleCommandSender) {
            if (sender instanceof ConsoleCommandSender) {
                if (args[0].equals("reload")) {
                    new Reload(plugin).run(sender, Arrays.copyOfRange(args, 1, args.length));
                }
                if (sender instanceof ConsoleCommandSender) {
                    if (args[0].equals("version")) {
                        new Version(plugin).run(sender, Arrays.copyOfRange(args, 1, args.length));
                    }
                    return true;
                }
            }
        }
        return false;
    }
}