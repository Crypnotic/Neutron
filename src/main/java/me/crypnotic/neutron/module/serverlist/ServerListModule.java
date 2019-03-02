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

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerPing;
import com.velocitypowered.api.proxy.server.ServerPing.Players;
import com.velocitypowered.api.scheduler.ScheduledTask;

import lombok.Getter;
import me.crypnotic.neutron.api.module.AbstractModule;
import me.crypnotic.neutron.module.serverlist.ServerListConfig.PlayerCount;
import me.crypnotic.neutron.util.ConfigHelper;

public class ServerListModule extends AbstractModule {

    @Getter
    private ServerListConfig config;

    private ScheduledTask pingTask;
    @Getter
    private int maxPlayerPing;

    @Override
    public boolean init() {
        this.config = ConfigHelper.getSerializable(getRootNode(), new ServerListConfig());
        if (config == null) {
            return false;
        }

        getNeutron().getProxy().getEventManager().register(getNeutron(), new ServerListHandler(this, config));

        if (config.getPlayerCount().getAction() == PlayerCount.PlayerCountAction.PING) {
            this.pingTask = getNeutron().getProxy().getScheduler().buildTask(getNeutron(), new PingTask()).repeat(5, TimeUnit.MINUTES).schedule();
        }

        return true;
    }

    @Override
    public boolean reload() {
        return init();
    }

    @Override
    public boolean shutdown() {
        if (pingTask != null) {
            pingTask.cancel();
        }

        return true;
    }

    @Override
    public String getName() {
        return "serverlist";
    }

    public class PingTask implements Runnable {

        int buffer = 0;

        @Override
        public void run() {
            for (RegisteredServer server : getNeutron().getProxy().getAllServers()) {
                try {
                    ServerPing ping = server.ping().get();
                    Optional<Players> players = ping.getPlayers();

                    if (players.isPresent()) {
                        buffer += players.get().getMax();
                    }
                } catch (InterruptedException | ExecutionException exception) {
                    /* Catch silently */
                }

            }

            maxPlayerPing = buffer;
            buffer = 0;
        }
    }
}
