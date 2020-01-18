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
package me.crypnotic.neutron.manager.locale;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.crypnotic.neutron.NeutronPlugin;
import me.crypnotic.neutron.api.Reloadable;
import me.crypnotic.neutron.api.StateResult;
import me.crypnotic.neutron.api.configuration.Configuration;
import me.crypnotic.neutron.api.locale.LocaleMessageTable;
import me.crypnotic.neutron.util.ConfigHelper;
import me.crypnotic.neutron.util.FileHelper;

@RequiredArgsConstructor
public class LocaleManager implements Reloadable {

    private final NeutronPlugin neutron;
    private final Configuration configuration;

    private LocaleConfig config;
    private File folder;
    private Map<Locale, LocaleMessageTable> locales = new HashMap<Locale, LocaleMessageTable>();
    @Getter
    private Locale defaultLocale;

    @Override
    public StateResult init() {
        this.config = ConfigHelper.getSerializable(configuration.getNode("locale"), new LocaleConfig());
        if (config == null) {
            return StateResult.fail();
        }

        if (!config.isEnabled()) {
            neutron.getLogger().info("LocaleManager has been disabled. Only loading messages from default presets.");
            return StateResult.success();
        }

        this.folder = FileHelper.getOrCreateDirectory(neutron.getDataFolderPath(), "locales");

        loadDefaultLocale();

        if (!config.isAllowTranslations()) {
            neutron.getLogger().info("Locale translations have been disabled. Only loading messages from default locale.");
            return StateResult.success();
        }

        for (File file : folder.listFiles()) {
            LocaleMessageTable.load(file).ifPresent(table -> locales.put(table.getLocale(), table));
        }

        neutron.getLogger().info("Locales loaded: " + locales.size());

        return StateResult.success();
    }

    public LocaleMessageTable get(Locale locale) {
        if (locale == null) {
            return locales.get(defaultLocale);
        }

        LocaleMessageTable table = locales.get(locale);

        return table != null ? table : locales.get(defaultLocale);
    }

    private void loadDefaultLocale() {
        String fallbackLocaleName = config.getFallbackLocale();
        this.defaultLocale = Locale.forLanguageTag(fallbackLocaleName);

        if (defaultLocale != null) {
            File defaultLocaleFile = FileHelper.getOrCreateLocale(folder.toPath(), fallbackLocaleName + ".conf");
            if (defaultLocaleFile != null) {
                LocaleMessageTable.load(defaultLocaleFile).ifPresent(table -> locales.put(table.getLocale(), table));

                neutron.getLogger().info("Loaded fallback locale: " + fallbackLocaleName);

                return;
            } else {
                neutron.getLogger().warn("Could not find file for locale: " + fallbackLocaleName);
                neutron.getLogger().warn("Falling back to en_US");
            }
        } else {
            neutron.getLogger().warn("Unknown fallback locale specified: " + fallbackLocaleName);
            neutron.getLogger().warn("Falling back to en_US");
        }

        this.defaultLocale = Locale.forLanguageTag("en_US");
        FileHelper.getOrCreateLocale(folder.toPath(), "en_US.conf");
    }

    @Override
    public StateResult reload() {
        return StateResult.of(shutdown(), init());
    }

    @Override
    public StateResult shutdown() {
        locales.clear();

        return StateResult.success();
    }

    @Override
    public String getName() {
        return "LocaleManager";
    }
}
