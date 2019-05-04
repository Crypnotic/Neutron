package me.crypnotic.neutron.manager.user;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import com.velocitypowered.api.proxy.Player;

import lombok.RequiredArgsConstructor;
import me.crypnotic.neutron.api.configuration.Configuration;
import me.crypnotic.neutron.api.user.AbstractUser;
import me.crypnotic.neutron.manager.user.holder.ConsoleUser;
import me.crypnotic.neutron.manager.user.holder.PlayerUser;
import me.crypnotic.neutron.util.ConfigHelper;

// TODO: Should the module be responsible solely for storing data?
@RequiredArgsConstructor
public class UserManager {

    private final Configuration configuration;

    private UserConfig config;
    private LoadingCache<UUID, PlayerUser> players;
    private ConsoleUser console;

    public boolean init() {
        this.config = ConfigHelper.getSerializable(configuration.getNode("user"), new UserConfig());
        if (config == null) {
            return false;
        }

        initCache();

        this.console = new ConsoleUser(config.getConsole());

        return true;
    }

    private void initCache() {
        final CacheBuilder<UUID, PlayerUser> builder = CacheBuilder.newBuilder().weakKeys().removalListener(notification -> {
            try {
                notification.getValue().save();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        if (config.getCache().getMaxSize() >= 0) {
            builder.maximumSize(config.getCache().getMaxSize());
        }

        if (config.getCache().getExpiryMins() >= 0) {
            builder.expireAfterAccess(config.getCache().getExpiryMins(), TimeUnit.MINUTES);
        }

        players = builder.build(new CacheLoader<UUID, PlayerUser>() {
            @Override
            public PlayerUser load(UUID uuid) throws Exception {
                PlayerUser user = new PlayerUser(uuid);

                user.load();

                return user;
            }
        });
    }

    public Optional<AbstractUser<?>> getUser(UUID uuid) {
        try {
            return Optional.ofNullable(players.get(uuid));
        } catch (ExecutionException exception) {
            exception.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<AbstractUser<?>> getUser(CommandSource base) {
        if (base instanceof Player) {
            return getUser(((Player) base).getUniqueId());
        } else if (base instanceof ConsoleCommandSource) {
            return Optional.of(console);
        }
        return Optional.empty();
    }
}
