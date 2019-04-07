/*
* This file is part of Neutron, licensed under the MIT License
*
* Copyright (c) 2019 Crypnotic <crypnoticofficial@gmail.com>
*
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
package me.crypnotic.neutron.module.serverlist;

import java.util.stream.Collectors;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.ServerPing;
import com.velocitypowered.api.proxy.server.ServerPing.Builder;

import lombok.RequiredArgsConstructor;
import me.crypnotic.neutron.util.Strings;

@RequiredArgsConstructor
public class ServerListHandler {

    private final ServerListModule module;
    private final ServerListConfig config;

    @Subscribe
    public void onServerListPing(ProxyPingEvent event) {
        if (!module.isEnabled()) {
            return;
        }

        ServerPing original = event.getPing();

        int playerCount = module.getNeutron().getProxy().getPlayerCount();

        Builder builder = ServerPing.builder();

        builder.version(original.getVersion());
        builder.onlinePlayers(playerCount);
        
        original.getFavicon().ifPresent(builder::favicon);
        
        builder.description(config.getMotd());

        switch (config.getPlayerCount().getAction()) {
        case CURRENT:
            builder.maximumPlayers(playerCount);
            break;
        case ONEMORE:
            builder.maximumPlayers(playerCount + 1);
            break;
        case PING:
            builder.maximumPlayers(module.getMaxPlayerPing());
            break;
        case STATIC:
            builder.maximumPlayers(config.getPlayerCount().getMaxPlayerCount());
            break;
        }

        switch (config.getServerPreview().getAction()) {
        case MESSAGE:
            builder.samplePlayers(Strings.toSamplePlayerArray(config.getServerPreview().getMessages()));
            break;
        case PLAYERS:
            builder.samplePlayers(Strings.toSamplePlayerArray(
                    module.getNeutron().getProxy().getAllPlayers().stream().map(Player::getUsername).collect(Collectors.toList())));
            break;
        case EMPTY:
            break;
        }

        event.setPing(builder.build());
    }
}
