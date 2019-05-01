package me.crypnotic.neutron.module.user.holder;

import java.util.Optional;
import java.util.UUID;

import com.velocitypowered.api.proxy.ConsoleCommandSource;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.crypnotic.neutron.api.Neutron;
import me.crypnotic.neutron.api.user.AbstractUser;

// TODO: Consider whether this should support *any* CommandSource that isn't a player (ie plugin-provided CommandSources)
@RequiredArgsConstructor
public class ConsoleUser extends AbstractUser<ConsoleCommandSource> {

    @Getter
    private final String name;

    @Override
    public Optional<ConsoleCommandSource> getBase() {
        return Optional.of(Neutron.getNeutron().getProxy().getConsoleCommandSource());
    }

    @Override
    public void load() throws Exception {
        /* noop */
    }

    @Override
    public void save() throws Exception {
        /* noop */
    }

    @Override
    public Optional<UUID> getUUID() {
        return Optional.empty();
    }
}
