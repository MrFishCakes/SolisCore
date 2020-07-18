package net.solismc.core;

import net.solismc.core.api.plugin.SolisPlugin;

public final class Core extends SolisPlugin {

    @Override
    public void onStart() {
        saveDefaultConfig();
    }

    @Override
    public void onStop() {

    }
}
