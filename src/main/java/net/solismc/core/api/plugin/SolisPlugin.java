package net.solismc.core.api.plugin;

import de.leonhard.storage.Config;
import de.leonhard.storage.LightningBuilder;
import de.leonhard.storage.internal.settings.ConfigSettings;
import de.leonhard.storage.internal.settings.ReloadSettings;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * Created by MrFishCakes on 17/07/2020 at 15:12
 * Copyrighted to MrFishCakes.
 */
public abstract class SolisPlugin extends JavaPlugin {

    private Config config = null;

    /**
     * Method called whilst the plugin is starting
     *
     * @since 1.0.0
     */
    public abstract void onStart();

    /**
     * Method called whilst the plugin is stopping
     *
     * @since 1.0.0
     */
    public abstract void onStop();

    /**
     * Get the main {@link Config} file for the plugin
     *
     * @return The main config file for the plugin
     * @since 1.0.0
     */
    public Config getMainConfig() {
        if (config == null) {
            saveDefaultConfig();
        }

        return config;
    }

    // Overridden so that we can implement our own saving
    @Override
    public void saveDefaultConfig() {
        getDataFolder().mkdirs();

        File configFile = new File(getDataFolder(), "config.yml");
        config = LightningBuilder.fromFile(configFile).setConfigSettings(ConfigSettings.PRESERVE_COMMENTS)
                .setReloadSettings(ReloadSettings.AUTOMATICALLY).addInputStreamFromResource("config.yml")
                .createConfig();
    }

    // Method is cancelled so that it doesn't produce errors
    @Override
    public void saveConfig() {
    }

    // Method is cancelled so that is doesn't produce errors
    @Override
    public void reloadConfig() {
    }

    @Override
    public void onEnable() {
        onStart();
    }

    @Override
    public void onDisable() {
        onStop();
    }

}