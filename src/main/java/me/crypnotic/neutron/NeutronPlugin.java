/*
* This file is part of Neutron, licensed under the MIT License
*
* Copyright (c) 2020 Crypnotic <crypnoticofficial@gmail.com>
* Copyright (c) 2020 Contributors
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
package me.crypnotic.neutron;

import java.nio.file.Path;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;

import lombok.Getter;
import me.crypnotic.neutron.api.Neutron;
import me.crypnotic.neutron.api.configuration.Configuration;
import me.crypnotic.neutron.event.StateHandler;
import me.crypnotic.neutron.manager.ModuleManager;
import me.crypnotic.neutron.manager.locale.LocaleManager;
import me.crypnotic.neutron.manager.user.UserManager;

@Plugin(id = "@ID@", name = "@NAME@", version = "@VERSION@", description = "@DESCRIPTION@")
public class NeutronPlugin {

    @Inject
    @Getter
    private ProxyServer proxy;
    @Inject
    @Getter
    private Logger logger;
    @Inject
    @DataDirectory
    @Getter
    private Path dataFolderPath;

    @Getter
    private Configuration configuration;

    private StateHandler stateHandler;

    @Getter
    private LocaleManager localeManager;
    @Getter
    private ModuleManager moduleManager;
    @Getter
    private UserManager userManager;

    public NeutronPlugin() {
        Neutron.setNeutron(this);
    }

    @Subscribe
    public void onProxyInitialize(ProxyInitializeEvent event) {
        this.configuration = Configuration.builder().folder(dataFolderPath).name("config.conf").build();

        this.stateHandler = new StateHandler(this);

        this.localeManager = new LocaleManager(this, configuration);
        this.userManager = new UserManager(configuration);
        this.moduleManager = new ModuleManager(this, configuration);

        stateHandler.init();

        proxy.getEventManager().register(this, new StateHandler(this));
    }
}
