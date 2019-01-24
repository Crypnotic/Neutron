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
package me.crypnotic.neutron.module.locale;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import lombok.Getter;
import me.crypnotic.neutron.module.AbstractModule;
import me.crypnotic.neutron.util.FileIO;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;

public class LocaleModule extends AbstractModule {

    private File folder;
    private Map<Locale, LocaleMessageTable> locales = new HashMap<Locale, LocaleMessageTable>();
    @Getter
    private final Locale defaultLocale = Locale.forLanguageTag("en_US");

    @Override
    public boolean init() {
        this.folder = FileIO.getOrCreateDirectory(getDataFolderPath(), "locales");

        /* Copy default en_US locale file if not found */
        FileIO.getOrCreateLocale(folder.toPath(), "en_US.conf");

        for (File file : folder.listFiles()) {
            loadMessageTable(file);
        }

        getLogger().info("Locales loaded: " + locales.size());

        return true;
    }

    public LocaleMessageTable get(Locale locale) {
        if (locale == null) {
            return locales.get(defaultLocale);
        }

        LocaleMessageTable table = locales.get(locale);

        return table != null ? table : locales.get(defaultLocale);
    }

    private void loadMessageTable(File file) {
        try {
            String localeName = file.getName().split("\\.")[0];

            Locale locale = Locale.forLanguageTag(localeName.replace("_", "-"));
            if (locale == null) {
                getLogger().warn("Unknown locale attempted to load: " + localeName);
                return;
            }

            HoconConfigurationLoader loader = HoconConfigurationLoader.builder().setFile(file).build();
            ConfigurationNode node = loader.load();

            LocaleMessageTable table = new LocaleMessageTable(locale);
            for (LocaleMessage message : LocaleMessage.values()) {
                String content = node.getNode(message.getName()).getString(message.getDefaultMessage());
                if (content == null || content.isEmpty()) {
                    content = message.getDefaultMessage();
                }

                table.set(message, content);
            }

            locales.put(locale, table);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public boolean reload() {
        return shutdown() && init();
    }

    @Override
    public boolean shutdown() {
        locales.clear();

        return true;
    }

    @Override
    public String getName() {
        return "locale";
    }
}
