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
package me.crypnotic.neutron.manager.user.holder;

import static me.crypnotic.neutron.api.Neutron.getNeutron;

import java.lang.ref.WeakReference;
import java.util.Optional;
import java.util.UUID;

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
        String nickname = getNickname();
        return nickname == null ? getUsername() : "~" + nickname;
    }

    @Override
    public String getNickname() {
        String nickname = data.getNickname();
        return nickname == null || nickname.isEmpty() ? null : nickname;
    }

    @Override
    public Optional<UUID> getUUID() {
        return Optional.of(uuid);
    }

    @Override
    public void setNickname(String nickname) {
        data.setNickname(nickname);
    }

    public CommandSource getReplyRecipient() {
        return data.getReplyRecipient();
    }

    @Override
    public String getUsername() {
        Optional<Player> base = getBase();
        return base.isPresent() ? base.get().getUsername() : data.getUsername();

    }

    public void setReplyRecipient(CommandSource source) {
        data.setReplyRecipient(source);
    }
}
