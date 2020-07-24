package net.solismc.core.api.permission;

import com.google.common.collect.Sets;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
 * Created by MrFishCakes on 23/07/2020 at 17:05
 * Copyrighted to MrFishCakes.
 */
public abstract class Rank {

    private final String name, prefix;
    private final Set<String> permissions;
    private final Set<Rank> inherit;

    /**
     * Create a new Rank
     *
     * @param name   Name of the rank
     * @param prefix Prefix of the rank
     * @since 1.4.0
     */
    Rank(@NotNull final String name, @NotNull final String prefix) {
        Validate.notNull(name, "name is null");
        Validate.notNull(prefix, "prefix is null");

        this.name = name;
        this.prefix = prefix;
        this.permissions = Sets.newConcurrentHashSet();
        this.inherit = Sets.newConcurrentHashSet();
    }

    /**
     * Add a permission to the rank
     *
     * @param permission Permission to add
     * @return True if the rank didn't have permission, false otherwise
     * @since 1.4.0
     */
    public boolean addPermission(@NotNull final String permission) {
        Validate.notNull(permission, "permission is null");

        return permissions.add(permission);
    }

    /**
     * Add multiple permissions to the rank
     *
     * @param perms Permissions to add
     * @return True if the rank didn't have permission, false otherwise
     * @since 1.4.0
     */
    public boolean addPermissions(@NotNull final Collection<String> perms) {
        Validate.notNull(perms, "perms is null");

        return permissions.addAll(perms);
    }

    /**
     * Remove a permission from the rank
     *
     * @param permission Permission to remove
     * @return True if the rank had the permission, false otherwise
     * @since 1.4.0
     */
    public boolean removePermission(@NotNull final String permission) {
        Validate.notNull(permission, "permission is null");

        return permissions.remove(permission);
    }

    /**
     * Remove multiple permissions from the rank
     *
     * @param perms Permissions to remove
     * @return True if the rank had the permissions, false otherwise
     * @since 1.4.0
     */
    public boolean removePermissions(@NotNull final Collection<String> perms) {
        Validate.notNull(perms, "perms is null");

        return permissions.removeAll(perms);
    }

    /**
     * Get the Rank name
     *
     * @return Rank name
     * @since 1.4.0
     */
    @NotNull
    public final String getName() {
        return name;
    }

    /**
     * Get the Rank prefix
     *
     * @return Rank prefix
     * @since 1.4.0
     */
    @NotNull
    public final String getPrefix() {
        return prefix;
    }

    /**
     * Get the Rank permissions
     *
     * @return Rank permissions
     * @see Collections#unmodifiableSet(Set)
     * @since 1.4.0
     */
    @Contract(pure = true)
    @NotNull
    public final Set<String> getPermissions() {
        return Collections.unmodifiableSet(permissions);
    }

    /**
     * Get the Rank inheritance
     *
     * @return Rank inheritance
     * @see Collections#unmodifiableSet(Set)
     * @since 1.4.0
     */
    @Contract(pure = true)
    @NotNull
    public final Set<Rank> getInheritance() {
        return Collections.unmodifiableSet(inherit);
    }

}
