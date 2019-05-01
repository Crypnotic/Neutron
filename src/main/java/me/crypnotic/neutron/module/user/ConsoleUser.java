package me.crypnotic.neutron.module.user;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.crypnotic.neutron.api.Neutron;
import me.crypnotic.neutron.api.user.AbstractUser;
import ninja.leaping.configurate.ConfigurationNode;

import java.util.Optional;
import java.util.UUID;

// TODO: Consider whether this should support *any* CommandSource that isn't a player (ie plugin-provided CommandSources)
@RequiredArgsConstructor
public class ConsoleUser extends AbstractUser<ConsoleCommandSource> {

    private final ConfigurationNode root;

    @Getter
    private String name;

    @Override
    public Optional<ConsoleCommandSource> getBase() {
        return Optional.of(Neutron.getNeutron().getProxy().getConsoleCommandSource());
    }

    @Override
    public void load() throws Exception {
        name = root.getNode("name").getString("Console");
    }

    @Override
    public void save() throws Exception { /* noop */ }

    @Override
    public Optional<UUID> getUUID() {
        return Optional.empty();
    }
}
