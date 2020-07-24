package net.solismc.core;

import net.solismc.core.api.command.CommandManager;
import net.solismc.core.api.permission.RankManager;
import net.solismc.core.api.plugin.InitializationException;
import net.solismc.core.api.plugin.SolisPlugin;
import net.solismc.core.api.user.UserManager;
import net.solismc.core.commands.UserCommand;
import org.bukkit.Bukkit;

import java.util.logging.Level;

public final class Core extends SolisPlugin {

    private static UserManager userManager;
    private static RankManager rankManager;

    /**
     * Get the UserManager instance
     *
     * @return UserManager instance
     * @since 1.3.0
     */
    public static UserManager getUserManager() {
        return userManager;
    }

    /**
     * Get the RankManager instance
     *
     * @return RankManager instance
     * @since 1.4.0
     */
    public static RankManager getRankManager() {
        return rankManager;
    }

    @Override
    public void onStart() {
        saveDefaultConfig();

        try {
            userManager = new UserManager(this);
            rankManager = new RankManager();
        } catch (InitializationException ex) {
            getLogger().log(Level.SEVERE, "Manager was already initialized", ex);
            Bukkit.shutdown();
            return;
        }

        CommandManager commandManager = new CommandManager(this);
        commandManager.registerCommands(new UserCommand());
        commandManager.clear();
    }

    @Override
    public void onStop() {
        if (rankManager != null) {
            rankManager.clear();
            rankManager = null;
        }

        if (userManager != null) {
            userManager.clear();
            userManager = null;
        }
    }
}
