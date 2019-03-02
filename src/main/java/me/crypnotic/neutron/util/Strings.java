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
package me.crypnotic.neutron.util;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
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
        if (text == null) {
            return null;
        }

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

        return matchPartial(proxy.getAllPlayers(), Player::getUsername, partialName);
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

        return matchPartial(proxy.getAllServers(), server -> server.getServerInfo().getName(), partialName);
    }

    public static <T> Collection<T> matchPartial(Collection<T> data, Function<T, String> function, String partialName) {
        if (partialName == null) {
            throw new NullPointerException("partialName");
        }

        return data.stream().filter(d -> function.apply(d).regionMatches(true, 0, partialName, 0, partialName.length())).collect(Collectors.toList());
    }

    @SuppressWarnings("deprecation")
    public static SamplePlayer[] toSamplePlayerArray(List<String> input) {
        SamplePlayer[] result = new SamplePlayer[input.size()];
        for (int i = 0; i < input.size(); i++) {
            result[i] = new SamplePlayer(ComponentSerializers.LEGACY.serialize(color(input.get(i))), UUID.randomUUID());
        }
        return result;
    }
}
