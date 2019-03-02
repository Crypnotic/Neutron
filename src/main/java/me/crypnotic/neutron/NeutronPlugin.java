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
import me.crypnotic.neutron.manager.ModuleManager;

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
    private ModuleManager moduleManager;

    public NeutronPlugin() {
        Neutron.setNeutron(this);
        
        this.moduleManager = new ModuleManager(this);
    }

    @Subscribe
    public void onProxyInitialize(ProxyInitializeEvent event) {
        if (!moduleManager.init()) {
            logger.warn("Failed to initialize ModuleManager");
            return;
        }
    }
}
