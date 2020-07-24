package net.solismc.core.api.user;

import com.google.common.net.InetAddresses;
import net.solismc.core.Core;
import net.solismc.core.api.permission.Rank;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Created by MrFishCakes on 19/07/2020 at 17:11
 * Copyrighted to MrFishCakes.
 */
public class User {

    private final UUID uniqueId;
    private final String name;
    private final int address;
    private Rank rank;

    /**
     * Create a new User from {@link Player}
     *
     * @param player Player to create from
     * @since 1.3.0
     */
    public User(@NotNull final Player player) {
        this(player.getUniqueId(), player.getName(), (player.getAddress() == null ? 0 :
                InetAddresses.coerceToInteger(player.getAddress().getAddress())));
    }

    /**
     * Create a new User from given parameters
     *
     * @param uuid    UUID of the User
     * @param name    Name of the User
     * @param address IP address of the User
     * @since 1.3.0
     */
    public User(@NotNull final UUID uuid, @NotNull final String name, final int address) {
        Validate.notNull(uuid, "User UUID cannot be null");
        Validate.notNull(uuid, "User name cannot be null");

        this.uniqueId = uuid;
        this.name = name;
        this.address = address;
        this.rank = Core.getRankManager().getDefaultRank();
    }

    /**
     * Save the current User
     *
     * @since 1.3.0
     */
    public final void save() {
        Core.getUserManager().saveUser(this);
    }

    /**
     * Get the {@link UUID} of the User
     *
     * @return User UUID
     * @since 1.3.0
     */
    public UUID getUniqueId() {
        return uniqueId;
    }

    /**
     * Get the name of the User
     *
     * @return User name
     * @since 1.3.0
     */
    public String getName() {
        return name;
    }

    /**
     * Get the IP address of the User
     *
     * @return User address
     * @since 1.3.0
     */
    public int getAddress() {
        return address;
    }

    /**
     * Get the rank of the User
     *
     * @return User rank
     * @since 1.4.0
     */
    public Rank getRank() {
        return rank;
    }

    /**
     * Set the User's rank
     *
     * @param rank Rank to set
     * @since 1.4.0
     */
    public void setRank(@NotNull final Rank rank) {
        Validate.notNull(rank, "rank is null");

        this.rank = rank;
    }

}
