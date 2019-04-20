package me.crypnotic.neutron.module.user;

import com.google.common.reflect.TypeToken;
import com.velocitypowered.api.proxy.Player;
import lombok.RequiredArgsConstructor;
import me.crypnotic.neutron.api.Neutron;
import me.crypnotic.neutron.api.user.AbstractUser;
import me.crypnotic.neutron.util.ConfigHelper;
import me.crypnotic.neutron.util.FileIO;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Optional;
import java.util.UUID;

import static me.crypnotic.neutron.api.Neutron.getNeutron;

@RequiredArgsConstructor
public class PlayerUser extends AbstractUser<Player> {

    private final UUID uuid;
    private WeakReference<Player> base;

    private PlayerData data;

    private File file = FileIO.getOrCreate(getNeutron().getDataFolderPath().resolve("users"), uuid.toString() + ".conf");
    private HoconConfigurationLoader loader = HoconConfigurationLoader.builder().setFile(file).build();
    private CommentedConfigurationNode node;

    @Override
    public Optional<Player> getBase() {
        if (base == null || base.get() == null) {
            getNeutron().getProxy().getPlayer(uuid).ifPresent(player -> base = new WeakReference<>(player));
        }

        return Optional.ofNullable(base.get());
    }

    @Override
    public void load() throws Exception {
        this.node = loader.load();
        this.data = ConfigHelper.getSerializable(node, new PlayerData());
    }

    @Override
    public void save() throws Exception {
        node.setValue(data);
        loader.save(node);
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
}
