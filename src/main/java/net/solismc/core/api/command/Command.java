package net.solismc.core.api.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by MrFishCakes on 18/07/2020 at 20:10
 * Copyrighted to MrFishCakes.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Command {

    /**
     * Primary name for the command
     *
     * @return Command name
     * @since 1.2.0
     */
    String name();

    /**
     * Permission to run the command
     *
     * @return Command permission
     * @since 1.2.0
     */
    String permission() default "";

    /**
     * Usage for the command
     *
     * @return Command usage
     * @since 1.2.0
     */
    String usage() default "";

    /**
     * Description of the command
     *
     * @return Command description
     * @since 1.2.0
     */
    String desc() default "";

    /**
     * Aliases that run the command
     *
     * @return Command aliases
     * @since 1.2.0
     */
    String[] aliases() default {};

    /**
     * If the command only allowing {@link org.bukkit.entity.Player} access
     *
     * @return True for player only, otherwise false
     * @since 1.2.0
     */
    boolean playerOnly() default false;

}
