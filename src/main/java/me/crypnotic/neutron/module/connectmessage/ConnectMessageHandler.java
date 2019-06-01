package me.crypnotic.neutron.module.connectmessage;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import lombok.RequiredArgsConstructor;
import me.crypnotic.neutron.api.locale.LocaleMessage;
import me.crypnotic.neutron.util.StringHelper;
import net.kyori.text.Component;

@RequiredArgsConstructor
public class ConnectMessageHandler {

    private final ConnectMessageModule module;
    private final ConnectMessageConfig config;

    @Subscribe
    public void onPlayerJoin(PostLoginEvent event) {
        if (!module.isEnabled()) return;
        if (config.isAllowSilentJoinQuit() && event.getPlayer().hasPermission("neutron.silentjoin")) return;

        Component message = StringHelper.getMessage(event.getPlayer(), LocaleMessage.CONNECT_JOIN_MESSAGE, event.getPlayer().getUsername());
        module.getNeutron().getProxy().broadcast(message);
    }

    @Subscribe
    public void onPlayerQuit(DisconnectEvent event) {
        if (!module.isEnabled()) return;
        if (config.isAllowSilentJoinQuit() && event.getPlayer().hasPermission("neutron.silentquit")) return;

        Component message = StringHelper.getMessage(event.getPlayer(), LocaleMessage.CONNECT_QUIT_MESSAGE, event.getPlayer().getUsername());
        module.getNeutron().getProxy().broadcast(message);
    }

}
