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
package me.crypnotic.neutron.api.configuration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import com.google.common.base.Preconditions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import me.crypnotic.neutron.util.FileHelper;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

@AllArgsConstructor
public class Configuration {

    @Getter
    private final File folder;
    @Getter
    private final File file;
    @Getter
    private final ConfigurationLoader<?> loader;
    private ConfigurationNode node;

    public boolean reload() {
        ConfigurationNode fallback = node;
        try {

            this.node = loader.load();

            return true;
        } catch (IOException exception) {
            exception.printStackTrace();

            node = fallback;

            return false;
        }
    }

    public boolean save() {
        try {
            loader.save(node);

            return true;
        } catch (IOException exception) {
            exception.printStackTrace();

            return false;
        }
    }

    public ConfigurationNode getNode(Object... values) {
        return node.getNode(values);
    }

    public void setNode(ConfigurationNode node) {
        setNode(node, false);
    }

    public void setNode(ConfigurationNode node, boolean save) {
        this.node = node;

        if (save) {
            save();
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        @Setter
        @Accessors(fluent = true, chain = true)
        private Path folder;

        @Setter
        @Accessors(fluent = true, chain = true)
        private String name;

        public Configuration build() {
            Preconditions.checkNotNull(folder);
            Preconditions.checkNotNull(name);

            try {
                File file = FileHelper.getOrCreate(folder, name);
                ConfigurationLoader<?> loader = HoconConfigurationLoader.builder()
                        .setDefaultOptions(ConfigurationOptions.defaults().setShouldCopyDefaults(true)).setFile(file).build();

                ConfigurationNode node = loader.load();

                return new Configuration(folder.toFile(), file, loader, node);
            } catch (IOException exception) {
                exception.printStackTrace();
            }

            return null;
        }
    }
}
