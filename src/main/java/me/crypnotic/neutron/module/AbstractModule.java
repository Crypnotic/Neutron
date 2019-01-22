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
