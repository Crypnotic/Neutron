package me.crypnotic.neutron.module.serverlist;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.server.ServerPing;
import com.velocitypowered.api.proxy.server.ServerPing.Builder;
import com.velocitypowered.api.proxy.server.ServerPing.SamplePlayer;

import lombok.RequiredArgsConstructor;
import me.crypnotic.neutron.api.INeutronAccessor;

@RequiredArgsConstructor
public class ServerListHandler implements INeutronAccessor {

    private final ServerListModule module;

    @Subscribe
    public void onServerListPing(ProxyPingEvent event) {
        if (!module.isEnabled()) {
            return;
        }

        ServerPing original = event.getPing();

        int playerCount = getProxy().getPlayerCount();

        Builder builder = ServerPing.builder();

        builder.version(original.getVersion());
        builder.onlinePlayers(playerCount);

        builder.description(module.getMotd());

        switch (module.getPlayerCountType()) {
        case CURRENT:
            builder.maximumPlayers(playerCount);
            break;
        case ONEMORE:
            builder.maximumPlayers(playerCount + 1);
            break;
        case STATIC:
            builder.maximumPlayers(module.getMaxPlayerCount());
            break;
        }

        switch (module.getServerPreviewType()) {
        case MESSAGE:
            builder.samplePlayers(module.getPreviewMessages());
            break;
        case PLAYERS:
            builder.samplePlayers(getProxy().getAllPlayers().stream().map(player -> new SamplePlayer(player.getUsername(), player.getUniqueId()))
                    .toArray(SamplePlayer[]::new));
            break;
        case EMPTY:
            break;
        }
        
        event.setPing(builder.build());
    }
}
