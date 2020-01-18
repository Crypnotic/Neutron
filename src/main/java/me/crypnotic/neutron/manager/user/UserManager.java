/*
* This file is part of Neutron, licensed under the MIT License
*
* Copyright (c) 2020 Crypnotic <crypnoticofficial@gmail.com>
* Copyright (c) 2020 Contributors
*
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in all
* copies or substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
* SOFTWARE.
*/
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
import me.crypnotic.neutron.api.Reloadable;
import me.crypnotic.neutron.api.StateResult;
import me.crypnotic.neutron.api.configuration.Configuration;
import me.crypnotic.neutron.api.user.User;
import me.crypnotic.neutron.manager.user.holder.ConsoleUser;
import me.crypnotic.neutron.manager.user.holder.PlayerUser;
import me.crypnotic.neutron.util.ConfigHelper;

// TODO: Should the module be responsible solely for storing data?
@RequiredArgsConstructor
public class UserManager implements Reloadable {

    private final Configuration configuration;

    private UserConfig config;
    private LoadingCache<UUID, PlayerUser> players;
    private ConsoleUser console;

    @Override
    public StateResult init() {
        this.config = ConfigHelper.getSerializable(configuration.getNode("user"), new UserConfig());
        if (config == null) {
            return StateResult.fail();
        }

        initCache();

        this.console = new ConsoleUser(config.getConsole().getName());

        return StateResult.success();
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
    public StateResult reload() {
        return StateResult.of(shutdown(), init());
    }

    @Override
    public StateResult shutdown() {
        players.cleanUp();

        return StateResult.success();
    }

    public Optional<User<? extends CommandSource>> getUser(UUID uuid) {
        try {
            return Optional.ofNullable(players.get(uuid));
        } catch (ExecutionException exception) {
            exception.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<User<? extends CommandSource>> getUser(CommandSource base) {
        if (base instanceof Player) {
            return getUser(((Player) base).getUniqueId());
        } else if (base instanceof ConsoleCommandSource) {
            return Optional.of(console);
        }
        return Optional.empty();
    }

    @Override
    public String getName() {
        return "UserManager";
    }
}
