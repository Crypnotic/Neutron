package me.crypnotic.neutron.module;

import lombok.Getter;
import lombok.Setter;
import me.crypnotic.neutron.api.INeutronAccessor;

public abstract class AbstractModule implements INeutronAccessor {

    @Getter
    @Setter
    private boolean enabled;

    public abstract boolean init();

    public abstract boolean reload();

    public abstract boolean shutdown();

    public abstract String getName();
}
