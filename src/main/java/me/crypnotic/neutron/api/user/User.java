package me.crypnotic.neutron.api.user;

import java.util.Optional;
import java.util.UUID;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;

public interface User<T extends CommandSource> {

    Optional<T> getBase();

    void load() throws Exception;

    void save() throws Exception;

    default boolean isPlayer() {
        return getBase().isPresent() && getBase().get() instanceof Player;
    }

    String getName();

    Optional<UUID> getUUID();

    CommandSource getReplyRecipient();

    void setReplyRecipient(CommandSource source);
}
