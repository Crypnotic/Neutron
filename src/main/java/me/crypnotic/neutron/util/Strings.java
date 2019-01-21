package me.crypnotic.neutron.util;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerPing.SamplePlayer;

import net.kyori.text.TextComponent;
import net.kyori.text.serializer.ComponentSerializers;

public class Strings {

    @SuppressWarnings("deprecation")
    public static TextComponent color(String text) {
        return ComponentSerializers.LEGACY.deserialize(text, '&');
    }

    public static String format(String text, Object... params) {
        for (int i = 0; i < params.length; i++) {
            Object param = params[i];
            text = text.replace("{" + i + "}", param == null ? "null" : param.toString());
        }
        return text;
    }

    public static TextComponent formatAndColor(String text, Object... params) {
        return color(format(text, params));
    }

    public static Collection<Player> matchPlayer(ProxyServer proxy, String partialName) {
        // A better error message might be nice. This just mimics the previous output
        if (partialName == null) {
            throw new NullPointerException("partialName");
        }

        Optional<Player> exactMatch = proxy.getPlayer(partialName);
        if (exactMatch.isPresent()) {
            return Collections.singleton(exactMatch.get());
        }

        return proxy.getAllPlayers().stream().filter(player -> player.getUsername().regionMatches(true, 0, partialName, 0, partialName.length()))
                .collect(Collectors.toList());
    }

    public static Collection<RegisteredServer> matchServer(ProxyServer proxy, String partialName) {
        // A better error message might be nice. This just mimics the previous output
        if (partialName == null) {
            throw new NullPointerException("partialName");
        }

        Optional<RegisteredServer> exactMatch = proxy.getServer(partialName);
        if (exactMatch.isPresent()) {
            return Collections.singleton(exactMatch.get());
        }

        return proxy.getAllServers().stream()
                .filter(server -> server.getServerInfo().getName().regionMatches(true, 0, partialName, 0, partialName.length()))
                .collect(Collectors.toList());
    }

    @SuppressWarnings("deprecation")
    public static SamplePlayer toSamplePlayer(String text) {
        return new SamplePlayer(ComponentSerializers.LEGACY.serialize(color(text)), UUID.randomUUID());
    }
}
