package net.solismc.core;

import net.solismc.core.api.user.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Created by MrFishCakes on 20/07/2020 at 18:30
 * Copyrighted to MrFishCakes.
 */
public final class Utils {

    public static void sendMessage(@NotNull User user, String message) {
        final Player player = Bukkit.getPlayer(user.getUniqueId());
        if (player != null) player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                message.replaceAll("''", "'")));
    }

}
