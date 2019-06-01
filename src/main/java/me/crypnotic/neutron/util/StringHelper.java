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
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.ServerPing.SamplePlayer;

import me.crypnotic.neutron.api.Neutron;
import me.crypnotic.neutron.api.locale.LocaleMessage;
import me.crypnotic.neutron.api.locale.LocaleMessageTable;
import me.crypnotic.neutron.manager.locale.LocaleManager;
import net.kyori.text.Component;
import net.kyori.text.TextComponent;
import net.kyori.text.format.Style;
import net.kyori.text.serializer.gson.GsonComponentSerializer;
import net.kyori.text.serializer.legacy.LegacyComponentSerializer;

public class StringHelper {

    public static Component color(String text) {
        if (text == null) {
            return null;
        }

        return LegacyComponentSerializer.INSTANCE.deserialize(text, '&');
    }

    public static Component serialize(String json) {
        if (json == null) {
            return null;
        }

        return GsonComponentSerializer.INSTANCE.deserialize(json);
    }

    public static String format(String text, Object... params) {
        for (int i = 0; i < params.length; i++) {
            Object param = params[i];
            text = text.replace("{" + i + "}", param == null ? "null" : param.toString());
        }
        return text;
    }

    public static Component formatAndColor(String text, Object... params) {
        return color(format(text, params));
    }

    public static Component append(Component root, Component child) {
        List<Component> rootChildren = root.children();
        Component lastRootChild = rootChildren.get(rootChildren.size() - 1);
        Style lastRootChildStyle = lastRootChild.style();

        Component result = root.append(TextComponent.builder().content("").style(lastRootChildStyle).append(child).build());

        return result;
    }

    public static SamplePlayer[] toSamplePlayerArray(List<String> input) {
        SamplePlayer[] result = new SamplePlayer[input.size()];
        for (int i = 0; i < input.size(); i++) {
            result[i] = new SamplePlayer(LegacyComponentSerializer.INSTANCE.serialize(color(input.get(i))), UUID.randomUUID());
        }
        return result;
    }

    public static void message(CommandSource source, LocaleMessage message, Object... values) {
        source.sendMessage(getMessage(source, message, values));
    }

    public static Component getMessage(CommandSource source, LocaleMessage message, Object... values) {
        LocaleManager manager = Neutron.getNeutron().getLocaleManager();
        Locale locale = manager.getDefaultLocale();
        if (source instanceof Player) {
            locale = ((Player) source).getPlayerSettings().getLocale();
        }

        LocaleMessageTable table = manager.get(locale);
        if (table != null) {
            return table.get(message, values);
        }
        return StringHelper.formatAndColor(message.getDefaultMessage(), values);
    }
    
    public static void broadcast(Collection<CommandSource> recipients, LocaleMessage message, Object... values) {
        recipients.forEach(target -> message(target, message, values));
    }
}
