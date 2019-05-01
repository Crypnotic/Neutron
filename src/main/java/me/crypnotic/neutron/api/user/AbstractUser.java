package me.crypnotic.neutron.api.user;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;

import java.util.Optional;
import java.util.UUID;

public abstract class AbstractUser<T extends CommandSource> {

    public abstract Optional<T> getBase();

    public abstract void load() throws Exception;

    public abstract void save() throws Exception;

    public boolean isPlayer() {
        return getBase().isPresent() && getBase().get() instanceof Player;
    }

    public abstract String getName();

    public abstract Optional<UUID> getUUID();

}
