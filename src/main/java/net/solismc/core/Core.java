package net.solismc.core;

import net.solismc.core.api.command.CommandManager;
import net.solismc.core.api.permission.RankManager;
import net.solismc.core.api.plugin.SolisPlugin;
import net.solismc.core.api.user.UserManager;
import net.solismc.core.commands.UserCommand;

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
        userManager = new UserManager(this);
        rankManager = new RankManager();

        CommandManager commandManager = new CommandManager(this);
        commandManager.registerCommands(new UserCommand());
        commandManager.clear();
    }

    @Override
    public void onStop() {
        rankManager.clear();
        rankManager = null;

        userManager.clear();
        userManager = null;
    }
}
