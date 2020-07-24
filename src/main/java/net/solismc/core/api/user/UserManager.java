package net.solismc.core.api.user;

import com.google.common.collect.Sets;
import com.google.common.io.Files;
import de.leonhard.storage.Json;
import de.leonhard.storage.LightningBuilder;
import de.leonhard.storage.Yaml;
import de.leonhard.storage.internal.settings.ConfigSettings;
import de.leonhard.storage.internal.settings.ReloadSettings;
import net.solismc.core.Core;
import net.solismc.core.api.permission.Rank;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Created by MrFishCakes on 19/07/2020 at 17:18
 * Copyrighted to MrFishCakes.
 */
public class UserManager implements Listener {

    private static boolean loaded = false;
    private Core plugin;
    private Set<User> users;
    private StorageMethod storageMethod;
    private File dataFolder;

    /**
     * Create a new UserManger
     *
     * @param plugin Core instance
     * @since 1.3.0
     */
    public UserManager(final Core plugin) {
        if (loaded) {
            throw new RuntimeException(getClass().getName() + " has already been initialised!");
        }

        loaded = true;
        this.plugin = plugin;
        this.users = Sets.newConcurrentHashSet();
        this.storageMethod = StorageMethod.valueOf(plugin.getMainConfig()
                .getOrDefault("User.StorageType", "JSON"));
        this.dataFolder = new File(plugin.getDataFolder(), "users");
        dataFolder.mkdirs();

        // Deletes all files that aren't needed
        for (final File file : dataFolder.listFiles()) {
            String extension = Files.getFileExtension(file.getName());
            if (storageMethod == StorageMethod.JSON && !extension.equalsIgnoreCase("json")) {
                file.delete();
                continue;
            }

            if (storageMethod == StorageMethod.YAML && !extension.equalsIgnoreCase("yml")) {
                file.delete();
            }
        }

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    /**
     * Get a User via {@link UUID}
     *
     * @param id UUID of the target User
     * @return Optional containing a User if found
     * @since 1.3.0
     */
    public Optional<User> getUser(UUID id) {
        return users.parallelStream().filter(Objects::nonNull).filter(user -> user.getUniqueId().equals(id))
                .findFirst();
    }

    /**
     * Load a User from the chosen storage system
     *
     * @param player Player to get information from
     * @since 1.3.0
     */
    public void loadUser(@NotNull Player player) {
        Validate.notNull(player, "Player cannot be null");

        if (storageMethod == StorageMethod.JSON) {
            File saveData = new File(dataFolder, player.getUniqueId().toString() + ".json");
            if (saveData.exists()) {
                Json jsonData = LightningBuilder.fromFile(saveData).setReloadSettings(ReloadSettings.MANUALLY)
                        .setConfigSettings(ConfigSettings.SKIP_COMMENTS).createJson();
                User user = new User(UUID.fromString(jsonData.getString("UniqueId")),
                        jsonData.getString("PlayerName"), jsonData.getInt("PlayerAddress"));

                Optional<Rank> optionalRank = Core.getRankManager().fromString(
                        jsonData.getString("PlayerRank"));
                optionalRank.ifPresent(user::setRank);
                users.add(user);
                return;
            }

            User user = new User(player);
            users.add(user);
        } else if (storageMethod == StorageMethod.YAML) {
            File saveData = new File(dataFolder, player.getUniqueId().toString() + ".yml");
            if (saveData.exists()) {
                Yaml yamlData = LightningBuilder.fromFile(saveData).setReloadSettings(ReloadSettings.MANUALLY)
                        .setConfigSettings(ConfigSettings.SKIP_COMMENTS).createYaml();
                User user = new User(UUID.fromString(yamlData.getString("UniqueId")),
                        yamlData.getString("PlayerName"), yamlData.getInt("PlayerAddress"));

                Optional<Rank> optionalRank = Core.getRankManager().fromString(
                        yamlData.getString("PlayerRank"));
                optionalRank.ifPresent(user::setRank);
                users.add(user);
                return;
            }

            User user = new User(player);
            users.add(user);
        }
    }

    /**
     * Save a User to the chosen storage system
     *
     * @param user User to save
     * @since 1.3.0
     */
    public void saveUser(@NotNull User user) {
        Validate.notNull(user, "User cannot be null");

        if (storageMethod == StorageMethod.JSON) {
            File saveData = new File(dataFolder, user.getUniqueId().toString() + ".json");
            Json jsonData = LightningBuilder.fromFile(saveData).setReloadSettings(ReloadSettings.MANUALLY)
                    .setConfigSettings(ConfigSettings.SKIP_COMMENTS).createJson();

            jsonData.set("UniqueId", user.getUniqueId().toString());
            jsonData.set("PlayerName", user.getName());
            jsonData.set("PlayerAddress", user.getAddress());
            jsonData.set("PlayerRank", user.getRank().getName());

            users.remove(user);
        } else if (storageMethod == StorageMethod.YAML) {
            File saveData = new File(dataFolder, user.getUniqueId().toString() + ".yml");
            Yaml yamlData = LightningBuilder.fromFile(saveData).setReloadSettings(ReloadSettings.MANUALLY)
                    .setConfigSettings(ConfigSettings.SKIP_COMMENTS).createYaml();

            yamlData.set("UniqueId", user.getUniqueId().toString());
            yamlData.set("PlayerName", user.getName());
            yamlData.set("PlayerAddress", user.getAddress());
            yamlData.set("PlayerRank", user.getRank().getName());

            users.remove(user);
        }
    }

    /**
     * Clear the class to prevent memory leaks
     *
     * @since 1.3.0
     */
    public void clear() {
        PlayerJoinEvent.getHandlerList().unregister(this);
        PlayerQuitEvent.getHandlerList().unregister(this);
        loaded = false;
        users.clear();
        users = null;
        storageMethod = null;
        dataFolder = null;
        plugin = null;
    }

    @EventHandler
    private void onPlayerJoin(final PlayerJoinEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () ->
                loadUser(event.getPlayer()));
    }

    @EventHandler
    private void onPlayerQuit(final PlayerQuitEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            Optional<User> optionalUser = getUser(event.getPlayer().getUniqueId());
            if (!optionalUser.isPresent()) return;

            User user = optionalUser.get();
            user.save();
        });
    }

    /**
     * Enum containing all the storage options
     *
     * @since 1.3.0
     */
    public enum StorageMethod {
        JSON, YAML
    }

}
