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
                ConfigurationLoader<?> loader = HoconConfigurationLoader.builder().setFile(file).build();
                ConfigurationNode node = loader.load();

                return new Configuration(folder.toFile(), file, loader, node);
            } catch (IOException exception) {
                exception.printStackTrace();
            }

            return null;
        }
    }
}
