package net.solismc.core.api.plugin;

import org.jetbrains.annotations.NotNull;

/**
 * Created by MrFishCakes on 24/07/2020 at 13:32
 * Copyrighted to MrFishCakes.
 */
public class InitializationException extends IllegalStateException {

    /**
     * Create a new InitializationException
     *
     * @param clazz The class throwing the error
     * @since 1.4.1
     */
    public InitializationException(@NotNull Class<?> clazz) {
        super(clazz.getName() + " has already been initialized");
    }

    /**
     * Create a new InitializationException
     *
     * @param object The object throwing the error
     * @see InitializationException(Class)
     * @since 1.4.1
     */
    public InitializationException(@NotNull Object object) {
        this(object.getClass());
    }

}
