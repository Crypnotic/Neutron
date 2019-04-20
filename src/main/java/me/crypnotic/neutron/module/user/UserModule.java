package me.crypnotic.neutron.module.user;

import com.google.common.cache.*;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import com.velocitypowered.api.proxy.Player;
import me.crypnotic.neutron.api.module.AbstractModule;
import me.crypnotic.neutron.api.user.AbstractUser;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

// TODO: Should the module be responsible solely for storing data?
public class UserModule extends AbstractModule {

    private LoadingCache<UUID, PlayerUser> playerUsers;
    private ConsoleUser consoleUser;

    @Override
    public boolean init() {
        initCache();
        consoleUser = new ConsoleUser(getRootNode().getNode("console"));

        return true;
    }

    private void initCache() {
        final int maxSize = getRootNode().getNode("cache", "maxSize").getInt(100);
        final int expiryMins = getRootNode().getNode("cache", "expiry").getInt(30);

        final CacheBuilder<UUID, PlayerUser> builder = CacheBuilder.newBuilder()
            .weakKeys()
            .removalListener(notification -> {
                try {
                    notification.getValue().save();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

        if (maxSize >= 0) {
            builder.maximumSize(maxSize);
        }

        if (expiryMins >= 0) {
            builder.expireAfterAccess(expiryMins, TimeUnit.MINUTES);
        }

        playerUsers = builder.build(new CacheLoader<UUID, PlayerUser>() {
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
        playerUsers.asMap().keySet().forEach(playerUsers::invalidate);
        return true;
    }

    @Override
    public String getName() {
        return "user";
    }

    public AbstractUser getUser(UUID uuid) {
        try {
            return playerUsers.get(uuid);
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    public AbstractUser getUser(CommandSource base) {
        if (base instanceof Player) {
            return getUser(((Player) base).getUniqueId());
        } else if (base instanceof ConsoleCommandSource) {
            return consoleUser;
        }

        return null;
    }
}
