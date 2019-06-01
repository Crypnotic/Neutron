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
package me.crypnotic.neutron.module.connectmessage;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;

import lombok.RequiredArgsConstructor;
import me.crypnotic.neutron.api.locale.LocaleMessage;
import me.crypnotic.neutron.util.StringHelper;

@RequiredArgsConstructor
public class ConnectMessageHandler {

    private final ConnectMessageModule module;
    private final ConnectMessageConfig config;

    @Subscribe
    public void onPlayerJoin(PostLoginEvent event) {
        if (config.isAllowSilentJoinQuit() && event.getPlayer().hasPermission("neutron.silentjoin")) {
            return;
        }

        StringHelper.broadcast(module.getNeutron().getProxy().getAllPlayers(), LocaleMessage.CONNECT_JOIN_MESSAGE, event.getPlayer().getUsername());
    }

    @Subscribe
    public void onPlayerQuit(DisconnectEvent event) {
        if (config.isAllowSilentJoinQuit() && event.getPlayer().hasPermission("neutron.silentquit")) {
            return;
        }

        StringHelper.broadcast(module.getNeutron().getProxy().getAllPlayers(), LocaleMessage.CONNECT_QUIT_MESSAGE, event.getPlayer().getUsername());
    }
}
