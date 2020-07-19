package net.solismc.core.api.command;

import com.google.common.collect.Maps;
import net.solismc.core.api.plugin.SolisPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import static java.util.Locale.ENGLISH;

/**
 * Created by MrFishCakes on 18/07/2020 at 20:17
 * Copyrighted to MrFishCakes.
 */
public final class CommandManager {

    private Constructor<?> constructor;
    private SolisPlugin plugin;
    private CommandMap commandMap;
    private Map<String, PluginCommand> commands;

    /**
     * Create a new CommandManager
     *
     * @param plugin Plugin owning the commands registered
     * @since 1.2.1
     */
    public CommandManager(final SolisPlugin plugin) {
        try {
            this.constructor = PluginCommand.class.getDeclaredConstructor(
                    String.class, Plugin.class);
            this.constructor.setAccessible(true);

            this.plugin = plugin;
            commandMap = Bukkit.getCommandMap(); // Yay Paper
            this.commands = Maps.newHashMap();
        } catch (ReflectiveOperationException ex) {
            plugin.getLogger().log(Level.SEVERE, "There was an error with " +
                    PluginCommand.class.getName() + " constructor", ex);
        }
    }

    /**
     * Register a single command
     *
     * @param base The extending class
     * @see BaseCommand
     * @since 1.2.1
     */
    private void registerCommand(@NotNull BaseCommand base) {
        final Method[] methods = base.getClass().getDeclaredMethods();
        for (Method method : methods) {
            try {
                if (hasCommandAnnotation(method) && hasParameters(method)) {
                    final Command command = method.getAnnotation(Command.class);
                    PluginCommand pCommand = (PluginCommand) constructor.newInstance(command.name()
                            .toLowerCase(ENGLISH), plugin);
                    pCommand.setUsage(command.usage()).setDescription(command.desc())
                            .setPermission(command.permission());
                    pCommand.setExecutor((sender, cmd, label, args) -> {
                        try {
                            method.invoke(base, sender, args);
                            return true;
                        } catch (IllegalAccessException | InvocationTargetException ex) {
                            plugin.getLogger().log(Level.SEVERE, "There was an error running "
                                    + command.name().toUpperCase(ENGLISH), ex);
                            return false;
                        }
                    });

                    commands.put(command.name().toLowerCase(ENGLISH), pCommand);
                } else if (hasCompleterAnnotation(method) && hasParameters(method)) {
                    if (method.getReturnType() != List.class) continue; //TODO Improve?

                    final Completer completer = method.getAnnotation(Completer.class);
                    PluginCommand pCommand = commands.get(completer.name().toLowerCase());
                    pCommand.setTabCompleter((sender, cmd, alias, args) -> {
                        try {
                            return (List<String>) method.invoke(base, sender, args);
                        } catch (IllegalAccessException | InvocationTargetException ex) {
                            plugin.getLogger().log(Level.SEVERE, "There was an error creating a new " +
                                    "PluginCommand instance", ex);
                            return null;
                        }
                    });
                }
            } catch (ReflectiveOperationException ex) {
                ex.printStackTrace();
            }

            if (commands.isEmpty()) {
                plugin.getLogger()
                        .warning("No commands are registered but a CommandManager was initiated?");
                return;
            }
        }
    }

    /**
     * Register multiple commands
     *
     * @param base The extending class
     * @see CommandManager#registerCommand(BaseCommand)
     * @since 1.2.1
     */
    public void registerCommands(@NotNull BaseCommand... base) {
        for (BaseCommand cmd : base) {
            registerCommand(cmd);
        }

        final String pluginName = plugin.getName().toLowerCase(ENGLISH);
        commands.values().forEach(command -> {
            commandMap.register(pluginName + ":", command);
            plugin.getLogger().info("Registered command: " + command.getName().toUpperCase(ENGLISH));
        });
    }

    /**
     * Clear the class to prevent memory leaks
     *
     * @since 1.2.1
     */
    public void clear() {
        constructor.setAccessible(false); // To be safe
        constructor = null;
        plugin = null;
        commandMap = null;
        commands.clear();
        commands = null;
    }

    @Contract(pure = true)
    private boolean hasCommandAnnotation(@NotNull Method method) {
        return method.getAnnotation(Command.class) != null;
    }

    @Contract(pure = true)
    private boolean hasCompleterAnnotation(@NotNull Method method) {
        return method.getAnnotation(Completer.class) != null;
    }

    private boolean hasParameters(@NotNull Method method) {
        return method.getParameterTypes()[0] == CommandSender.class
                && method.getParameterTypes()[1] == String[].class;
    }

}
