package me.crypnotic.neutron.manager;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyReloadEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;

import lombok.Getter;
import me.crypnotic.neutron.api.INeutronAccessor;
import me.crypnotic.neutron.module.AbstractModule;
import me.crypnotic.neutron.module.announcement.AnnouncementsModule;
import me.crypnotic.neutron.module.serverlist.ServerListModule;
import me.crypnotic.neutron.util.FileIO;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;

public class ModuleManager implements INeutronAccessor {

    @Getter
    private File file;
    @Getter
    private HoconConfigurationLoader loader;
    @Getter
    private ConfigurationNode root;

    private Map<Class<? extends AbstractModule>, AbstractModule> modules = new HashMap<Class<? extends AbstractModule>, AbstractModule>();

    public boolean init() {
        if (!loadConfig()) {
            return false;
        }

        modules.put(AnnouncementsModule.class, new AnnouncementsModule());
        modules.put(ServerListModule.class, new ServerListModule());

        int enabled = 0;
        for (AbstractModule module : modules.values()) {
            ConfigurationNode node = root.getNode(module.getName());
            if (node.isVirtual()) {
                getLogger().warn("Failed to load module: " + module.getName());
                continue;
            }

            module.setEnabled(node.getNode("enabled").getBoolean());
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
        int enabled = 0;
        for (AbstractModule module : modules.values()) {
            ConfigurationNode node = root.getNode(module.getName());
            if (node.isVirtual()) {
                getLogger().warn("Failed to reload module: " + module.getName());
                continue;
            }

            module.setEnabled(node.getNode("enabled").getBoolean());
            if (module.isEnabled()) {
                if (module.reload()) {
                    enabled += 1;

                    continue;
                } else {
                    getLogger().warn("Module failed to reload: " + module.getName());

                    module.setEnabled(false);

                    continue;
                }
            }
        }

        getLogger().info(String.format("Modules reloaded: %d (enabled: %d)", modules.size(), enabled));
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        getLogger().info("Shutting down active modules...");

        modules.values().stream().filter(AbstractModule::isEnabled).forEach(AbstractModule::shutdown);
    }

    private boolean loadConfig() {
        try {
            this.file = FileIO.getOrCreate(getDataFolderPath(), "config.hocon");
            this.loader = HoconConfigurationLoader.builder().setFile(file).build();
            this.root = loader.load();

            return true;
        } catch (IOException exception) {
            exception.printStackTrace();

            return false;
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends AbstractModule> Optional<T> get(Class<T> clazz) {
        return (Optional<T>) Optional.ofNullable(modules.get(clazz));
    }
}
