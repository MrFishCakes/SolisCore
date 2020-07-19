package net.solismc.core.api.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by MrFishCakes on 19/07/2020 at 15:09
 * Copyrighted to MrFishCakes.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Completer {

    /**
     * Name of the {@link Command} the completer is completing for
     *
     * @return Completer / command name
     * @since 1.2.1
     */
    String name();

}
