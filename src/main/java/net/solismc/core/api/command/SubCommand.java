package net.solismc.core.api.command;

/**
 * Created by MrFishCakes on 18/07/2020 at 20:32
 * Copyrighted to MrFishCakes.
 */
public @interface SubCommand {

    /**
     * Name for the sub-command
     *
     * @return Sub-command name
     * @since 1.2.0
     */
    String name();

    /**
     * Permission for the sub-command
     *
     * @return Sub-command permission
     * @since 1.2.0
     */
    String permission() default "";

}
