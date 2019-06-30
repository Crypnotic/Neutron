package me.crypnotic.neutron.manager.user.holder;

import static me.crypnotic.neutron.api.Neutron.getNeutron;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import com.google.common.collect.Sets;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;

import lombok.RequiredArgsConstructor;
import me.crypnotic.neutron.api.configuration.Configuration;
import me.crypnotic.neutron.api.user.User;
import me.crypnotic.neutron.util.ConfigHelper;
import ninja.leaping.configurate.ConfigurationNode;

@RequiredArgsConstructor
public class PlayerUser implements User<Player> {

    private final UUID uuid;
    private WeakReference<Player> base;

    private Configuration configuration;
    private PlayerData data;

    @Override
    public Optional<Player> getBase() {
        if (base == null || base.get() == null) {
            getNeutron().getProxy().getPlayer(uuid).ifPresent(player -> base = new WeakReference<>(player));
        }

        return Optional.ofNullable(base.get());
    }

    @Override
    public void load() throws Exception {
        this.configuration = Configuration.builder().folder(getNeutron().getDataFolderPath().resolve("users")).name(uuid.toString() + ".conf")
                .build();

        this.data = ConfigHelper.getSerializable(configuration.getNode(), new PlayerData());
    }

    @Override
    public void save() throws Exception {
        ConfigurationNode serialized = ConfigHelper.setSerializable(configuration.getNode(), data);

        configuration.setNode(serialized, serialized != null);
    }

    @Override
    public String getName() {
        Optional<Player> base = getBase();
        if (base.isPresent()) {
            return base.get().getUsername();
        }

        return data.getUsername();
    }

    @Override
    public Optional<UUID> getUUID() {
        return Optional.of(uuid);
    }

    public CommandSource getReplyRecipient() {
        return data.getReplyRecipient();
    }

    @Override
    public Set<UUID> getIgnoredPlayers() {
        return Collections.unmodifiableSet(data.getIgnoredPlayers());
    }

    public void setReplyRecipient(CommandSource source) {
        data.setReplyRecipient(source);
    }

    @Override
    public void setIgnoringPlayer(Player target, boolean ignore) {
        Set<UUID> newSet = Sets.newHashSet(data.getIgnoredPlayers());

        if (ignore) {
            newSet.add(target.getUniqueId());
        } else {
            newSet.remove(target.getUniqueId());
        }

        data.setIgnoredPlayers(Collections.unmodifiableSet(newSet));
    }
}
