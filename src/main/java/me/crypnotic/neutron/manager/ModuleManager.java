/*
* This file is part of Neutron, licensed under the MIT License
*
* Copyright (c) 2019 Crypnotic <crypnoticofficial@gmail.com>
*
*
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in all
* copies or substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
* SOFTWARE.
*/
package me.crypnotic.neutron.manager;

import java.util.HashMap;
import java.util.Map;

import com.google.common.reflect.TypeToken;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyReloadEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;

import lombok.RequiredArgsConstructor;
import me.crypnotic.neutron.NeutronPlugin;
import me.crypnotic.neutron.api.configuration.Configuration;
import me.crypnotic.neutron.api.module.AbstractModule;
import me.crypnotic.neutron.api.serializer.ComponentSerializer;
import me.crypnotic.neutron.module.announcement.AnnouncementsModule;
import me.crypnotic.neutron.module.command.CommandModule;
import me.crypnotic.neutron.module.locale.LocaleModule;
import me.crypnotic.neutron.module.serverlist.ServerListModule;
import net.kyori.text.Component;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializers;

@RequiredArgsConstructor
public class ModuleManager {

    private final NeutronPlugin neutron;
    private final Configuration configuration;

    private Map<Class<? extends AbstractModule>, AbstractModule> modules = new HashMap<Class<? extends AbstractModule>, AbstractModule>();

    public boolean init() {
        modules.put(AnnouncementsModule.class, new AnnouncementsModule());
        modules.put(CommandModule.class, new CommandModule());
        modules.put(LocaleModule.class, new LocaleModule());
        modules.put(ServerListModule.class, new ServerListModule());

        registerSerializers();

        int enabled = 0;
        for (AbstractModule module : modules.values()) {
            ConfigurationNode node = configuration.getNode(module.getName());
            if (node.isVirtual()) {
                neutron.getLogger().warn("Failed to load module: " + module.getName());
                continue;
            }

            module.setEnabled(node.getNode("enabled").getBoolean());
            if (module.isEnabled()) {
                if (module.init()) {
                    enabled += 1;

                    continue;
                } else {
                    neutron.getLogger().warn("Module failed to initialize: " + module.getName());

                    module.setEnabled(false);

                    continue;
                }
            }
        }

        neutron.getProxy().getEventManager().register(neutron, this);

        neutron.getLogger().info(String.format("Modules loaded: %d (enabled: %d)", modules.size(), enabled));

        return true;
    }

    private void registerSerializers() {
        TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(Component.class), new ComponentSerializer());
    }

    @Subscribe
    public void onProxyReload(ProxyReloadEvent event) {
        if (!configuration.reload()) {
            neutron.getLogger().warn("Failed to reload config on proxy reload");
        }

        int enabled = 0;
        for (AbstractModule module : modules.values()) {
            ConfigurationNode node = configuration.getNode(module.getName());
            if (node.isVirtual()) {
                neutron.getLogger().warn("Failed to reload module: " + module.getName());
                continue;
            }

            boolean newState = node.getNode("enabled").getBoolean();

            if (module.isEnabled() && !newState) {
                module.shutdown();

                module.setEnabled(newState);
            } else if (newState) {
                module.setEnabled(newState);

                if (module.reload()) {
                    enabled += 1;

                    continue;
                } else {
                    neutron.getLogger().warn("Module failed to reload: " + module.getName());

                    module.setEnabled(false);

                    continue;
                }
            }
        }

        neutron.getLogger().info(String.format("Modules reloaded: %d (enabled: %d)", modules.size(), enabled));
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        neutron.getLogger().info("Shutting down active modules...");

        modules.values().stream().filter(AbstractModule::isEnabled).forEach(AbstractModule::shutdown);
    }

    public boolean save() {
        return configuration.save();
    }

    public <T extends AbstractModule> T get(Class<T> clazz) {
        return clazz.cast(modules.get(clazz));
    }

    public ConfigurationNode getRoot() {
        return configuration.getNode();
    }
}
