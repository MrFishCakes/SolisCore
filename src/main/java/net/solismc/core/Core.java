package net.solismc.core;

import net.solismc.core.api.command.CommandManager;
import net.solismc.core.api.plugin.SolisPlugin;
import net.solismc.core.api.user.UserManager;
import net.solismc.core.commands.UserCommand;

public final class Core extends SolisPlugin {

    private static UserManager userManager;

    /**
     * Get the UserManager instance
     *
     * @return UserManager instance
     * @since 1.3.0
     */
    public static UserManager getUserManager() {
        return userManager;
    }

    @Override
    public void onStart() {
        saveDefaultConfig();
        userManager = new UserManager(this);
        CommandManager commandManager = new CommandManager(this);
        commandManager.registerCommands(new UserCommand());
        commandManager.clear();
    }

    @Override
    public void onStop() {
        userManager.clear();
        userManager = null;
    }
}
