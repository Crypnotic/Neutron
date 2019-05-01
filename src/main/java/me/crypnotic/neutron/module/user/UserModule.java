package me.crypnotic.neutron.module.user;

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

import me.crypnotic.neutron.api.module.AbstractModule;
import me.crypnotic.neutron.api.user.AbstractUser;
import me.crypnotic.neutron.module.user.holder.ConsoleUser;
import me.crypnotic.neutron.module.user.holder.PlayerUser;
import me.crypnotic.neutron.util.ConfigHelper;

// TODO: Should the module be responsible solely for storing data?
public class UserModule extends AbstractModule {

    private UserConfig config;
    private LoadingCache<UUID, PlayerUser> players;
    private ConsoleUser console;

    @Override
    public boolean init() {
        this.config = ConfigHelper.getSerializable(getRootNode(), new UserConfig());
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

    @Override
    public boolean reload() {
        return shutdown() && init();
    }

    @Override
    public boolean shutdown() {
        players.asMap().keySet().forEach(players::invalidate);

        return true;
    }

    @Override
    public String getName() {
        return "user";
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
