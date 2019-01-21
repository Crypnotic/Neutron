package me.crypnotic.neutron.api;

import java.nio.file.Path;

import org.slf4j.Logger;

import com.velocitypowered.api.proxy.ProxyServer;

import me.crypnotic.neutron.NeutronPlugin;
import me.crypnotic.neutron.manager.ModuleManager;

public interface INeutronAccessor {

    default NeutronPlugin getPlugin() {
        return Neutron.getNeutron();
    }

    default ProxyServer getProxy() {
        return getPlugin().getProxy();
    }

    default Logger getLogger() {
        return getPlugin().getLogger();
    }

    default Path getDataFolderPath() {
        return getPlugin().getDataFolderPath();
    }

    default ModuleManager getModuleManager() {
        return getPlugin().getModuleManager();
    }
}
