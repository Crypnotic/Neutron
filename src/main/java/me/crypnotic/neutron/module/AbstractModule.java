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
package me.crypnotic.neutron.module;

import java.util.List;

import com.google.common.reflect.TypeToken;

import lombok.Getter;
import lombok.Setter;
import me.crypnotic.neutron.api.INeutronAccessor;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

public abstract class AbstractModule implements INeutronAccessor {

    @Getter
    @Setter
    private boolean enabled;

    public abstract boolean init();

    public abstract boolean reload();

    public abstract boolean shutdown();

    public abstract String getName();

    public <T> T getOrSet(ConfigurationNode node, String key, T fallback, Class<T> type) {
        ConfigurationNode value = node.getNode(key);
        if (value.isVirtual()) {
            value.setValue(fallback);
            return fallback;
        }

        try {
            return value.getValue(TypeToken.of(type));
        } catch (ObjectMappingException exception) {
            exception.printStackTrace();

            return fallback;
        }
    }

    public <T> List<T> getOrSetList(ConfigurationNode node, String key, List<T> fallback, Class<T> type) {
        ConfigurationNode value = node.getNode(key);
        if (value.isVirtual()) {
            value.setValue(fallback);
            return fallback;
        }

        try {
            return value.getList(TypeToken.of(type));
        } catch (ObjectMappingException exception) {
            exception.printStackTrace();

            return fallback;
        }
    }
}
