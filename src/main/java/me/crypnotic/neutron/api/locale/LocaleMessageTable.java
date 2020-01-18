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
package me.crypnotic.neutron.api.locale;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.crypnotic.neutron.api.Neutron;
import me.crypnotic.neutron.api.configuration.Configuration;
import me.crypnotic.neutron.util.StringHelper;
import net.kyori.text.Component;

@RequiredArgsConstructor
public class LocaleMessageTable {

    @Getter
    private final Locale locale;
    private final Map<LocaleMessage, String> messages = new HashMap<LocaleMessage, String>();

    public Component get(LocaleMessage key, Object... values) {
        String message = messages.get(key);

        return StringHelper.formatAndColor(message != null ? message : key.getDefaultMessage(), values);
    }

    public boolean set(LocaleMessage key, String message) {
        /* Return true if no entry existed previously */
        return messages.put(key, message) == null;
    }

    public static Optional<LocaleMessageTable> load(File file) {
        Configuration configuration = Configuration.builder().folder(file.getParentFile().toPath()).name(file.getName()).build();

        String name = file.getName().split("\\.")[0];
        Locale locale = Locale.forLanguageTag(name);
        if (locale == null) {
            Neutron.getNeutron().getLogger().warn("Unknown locale attempted to load: " + name);
            return Optional.empty();
        }

        LocaleMessageTable table = new LocaleMessageTable(locale);
        for (LocaleMessage message : LocaleMessage.values()) {
            String text = configuration.getNode(message.getName()).getString(message.getDefaultMessage());
            if (text == null || text.isEmpty()) {
                text = message.getDefaultMessage();
            }

            table.set(message, text);
        }
        
        return Optional.of(table);
    }
}
