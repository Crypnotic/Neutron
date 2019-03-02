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
package me.crypnotic.neutron.module.command;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.crypnotic.neutron.api.module.AbstractModule;
import ninja.leaping.configurate.ConfigurationNode;

public class CommandModule extends AbstractModule {

    private Map<Commands, CommandWrapper> commands = new HashMap<Commands, CommandWrapper>();

    @Override
    public boolean init() {
        ConfigurationNode options = getRootNode().getNode("options");
        if (options.isVirtual()) {
            getNeutron().getLogger().warn("No config entry found for command module options");
            return false;
        }

        for (Commands spec : Commands.values()) {
            ConfigurationNode node = options.getNode(spec.getKey());
            if (node.isVirtual()) {
                getNeutron().getLogger().warn("No config entry for command: " + spec.getKey());
                continue;
            }

            CommandWrapper wrapper = spec.getSupplier().get();

            List<String> aliases = node.getNode("aliases").getList(Object::toString);

            wrapper.setEnabled(node.getNode("enabled").getBoolean());
            wrapper.setAliases(aliases.toArray(new String[aliases.size()]));

            if (wrapper.isEnabled()) {
                getNeutron().getProxy().getCommandManager().register(wrapper, wrapper.getAliases());
            }

            commands.put(spec, wrapper);
        }

        return true;
    }

    @Override
    public boolean reload() {
        return shutdown() && init();
    }

    @Override
    public boolean shutdown() {
        commands.values().stream().map(CommandWrapper::getAliases).flatMap(Arrays::stream)
                .forEach(getNeutron().getProxy().getCommandManager()::unregister);

        commands.clear();

        return true;
    }

    @Override
    public String getName() {
        return "command";
    }
}
