package me.crypnotic.neutron.manager;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.moandjiezana.toml.Toml;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyReloadEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;

import lombok.Getter;
import me.crypnotic.neutron.api.INeutronAccessor;
import me.crypnotic.neutron.module.AbstractModule;
import me.crypnotic.neutron.module.announcement.AnnouncementsModule;
import me.crypnotic.neutron.module.serverlist.ServerListModule;
import me.crypnotic.neutron.util.FileIO;

public class ModuleManager implements INeutronAccessor {

    @Getter
    private File mainConfigFile;
    @Getter
    private Toml mainConfig;

    private Map<Class<? extends AbstractModule>, AbstractModule> modules = new HashMap<Class<? extends AbstractModule>, AbstractModule>();

    public boolean init() {
        try {
            this.mainConfigFile = FileIO.getOrCreate(getDataFolderPath(), "config.toml");
            this.mainConfig = new Toml().read(mainConfigFile);
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }

        modules.put(AnnouncementsModule.class, new AnnouncementsModule());
        modules.put(ServerListModule.class, new ServerListModule());

        int enabled = 0;
        for (AbstractModule module : modules.values()) {
            Toml table = mainConfig.getTable(module.getName());
            if (table == null) {
                getLogger().warn("Unknown module attempted to load: " + module.getName());
                continue;
            }

            module.setEnabled(table.getBoolean("enabled"));
            if (module.isEnabled()) {
                if (module.init()) {
                    enabled += 1;
                    continue;
                } else {
                    getLogger().warn("Module failed to initialize: " + module.getName());

                    module.setEnabled(false);

                    continue;
                }
            }
        }

        getProxy().getEventManager().register(getPlugin(), this);

        getLogger().info(String.format("Modules loaded: %d (enabled: %d)", modules.size(), enabled));

        return true;
    }

    @Subscribe
    public void onProxyReload(ProxyReloadEvent event) {
        try {
            this.mainConfigFile = FileIO.getOrCreate(getDataFolderPath(), "config.toml");
            this.mainConfig = new Toml().read(mainConfigFile);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        modules.clear();

        int enabled = 0;
        for (AbstractModule module : modules.values()) {
            Toml table = mainConfig.getTable(module.getName());
            if (table == null) {
                getLogger().warn("Unknown module attempted to reload: " + module.getName());
                continue;
            }

            module.setEnabled(table.getBoolean("enabled"));
            if (module.isEnabled()) {
                if (module.reload()) {
                    enabled += 1;
                    continue;
                } else {
                    getLogger().warn("Module failed to reload: " + module.getName());

                    module.setEnabled(false);

                    continue;
                }
            } else {

            }
        }

        getLogger().info(String.format("Modules reloaded: %d (enabled: %d)", modules.size(), enabled));
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        getLogger().info("Shutting down active modules...");

        modules.values().stream().filter(AbstractModule::isEnabled).forEach(AbstractModule::shutdown);
    }

    @SuppressWarnings("unchecked")
    public <T extends AbstractModule> Optional<T> get(Class<T> clazz) {
        return (Optional<T>) Optional.ofNullable(modules.get(clazz));
    }
}
