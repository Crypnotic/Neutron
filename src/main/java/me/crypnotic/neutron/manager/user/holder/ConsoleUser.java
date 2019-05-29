package me.crypnotic.neutron.manager.user.holder;

import java.util.Optional;
import java.util.UUID;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ConsoleCommandSource;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.crypnotic.neutron.api.Neutron;
import me.crypnotic.neutron.api.user.User;

// TODO: Consider whether this should support *any* CommandSource that isn't a player (ie plugin-provided CommandSources)
@RequiredArgsConstructor
public class ConsoleUser implements User<ConsoleCommandSource> {

    @Getter
    private final String name;

    @Getter
    @Setter
    private CommandSource replyRecipient;

    @Override
    public Optional<ConsoleCommandSource> getBase() {
        return Optional.of(Neutron.getNeutron().getProxy().getConsoleCommandSource());
    }

    @Override
    public String getNickname() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
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

    @Override
    public void setNickname(String nickname) {
        /* noop */
    }
}
