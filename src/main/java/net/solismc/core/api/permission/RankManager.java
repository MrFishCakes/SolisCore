package net.solismc.core.api.permission;

import com.google.common.collect.Sets;
import net.solismc.core.api.plugin.InitializationException;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Created by MrFishCakes on 23/07/2020 at 17:42
 * Copyrighted to MrFishCakes.
 */
public class RankManager {

    private static boolean loaded = false;
    private Rank defaultRank;
    private Set<Rank> registeredRanks;

    /**
     * Create a new RankManager
     *
     * @throws InitializationException If the class has already been initialized
     * @since 1.4.0
     */
    public RankManager() throws InitializationException {
        if (loaded) {
            throw new InitializationException(this);
        }

        loaded = true;
        this.defaultRank = new MemberRank();
        this.registeredRanks = Sets.newHashSet();
        this.registeredRanks.add(new MemberRank());
    }

    /**
     * Get a {@link Rank} from a String
     *
     * @param input Rank name as a String
     * @return {@link Optional} containing a rank or null
     * @since 1.4.0
     */
    @NotNull
    public final Optional<Rank> fromString(String input) {
        return registeredRanks.parallelStream().filter(Objects::nonNull)
                .filter(rank -> rank.getName().equals(input)).findFirst();
    }

    /**
     * The default rank for the server
     *
     * @return Default rank
     * @since 1.4.0
     */
    public Rank getDefaultRank() {
        return defaultRank;
    }

    /**
     * Clear the class to prevent memory leaks
     *
     * @since 1.4.0
     */
    public void clear() {
        defaultRank = null;
        registeredRanks.clear();
        registeredRanks = null;
    }
}
