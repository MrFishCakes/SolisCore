package net.solismc.core.commands;

import com.google.common.net.InetAddresses;
import net.solismc.core.Core;
import net.solismc.core.api.command.BaseCommand;
import net.solismc.core.api.command.Command;
import net.solismc.core.api.user.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

/**
 * Created by MrFishCakes on 19/07/2020 at 19:09
 * Copyrighted to MrFishCakes.
 */
public class UserCommand extends BaseCommand {

    @Command(name = "user", permission = "core.command.permission", usage = "/user <Player>",
            desc = "View information on a User")
    public void onUserCommand(CommandSender sender, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /user <Player>");
            return;
        }

        final Player player = Bukkit.getPlayer(args[0]);
        if (player == null) {
            sender.sendMessage(ChatColor.RED + args[0] + " is not online!");
            return;
        }

        Optional<User> optionalUser = Core.getUserManager().getUser(player.getUniqueId());
        if (!optionalUser.isPresent()) {
            sender.sendMessage(ChatColor.RED + args[0] + " is not online!");
            return;
        }

        User user = optionalUser.get();

        String message = "&eName: &7" + user.getName() +
                "\n" + "&eUUID: &7" + user.getUniqueId() + "\n" +
                "&eIP: &7{address}";
        if (sender.hasPermission("core.user.viewaddress")) {
            message = message.replace("{address}", InetAddresses.fromInteger(user.getAddress())
                    .getHostAddress());
        } else {
            message = message.replace("{address}", "Redacted");
        }
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }
}
