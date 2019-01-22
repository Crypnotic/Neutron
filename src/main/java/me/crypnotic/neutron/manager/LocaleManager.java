package me.crypnotic.neutron.manager;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyReloadEvent;

import lombok.Getter;
import me.crypnotic.neutron.api.INeutronAccessor;
import me.crypnotic.neutron.api.locale.Message;
import me.crypnotic.neutron.api.locale.MessageTable;
import me.crypnotic.neutron.util.FileIO;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;

public class LocaleManager implements INeutronAccessor {

    private File folder;
    private Map<Locale, MessageTable> locales = new HashMap<Locale, MessageTable>();
    @Getter
    private final Locale defaultLocale = Locale.forLanguageTag("en_US");

    public boolean init() {
        this.folder = FileIO.getOrCreateDirectory(getDataFolderPath(), "locales");

        /* Copy default en_US locale file if not found */
        FileIO.getOrCreateLocale(folder.toPath(), "en_US.hocon");

        for (File file : folder.listFiles()) {
            loadMessageTable(file);
        }

        getProxy().getEventManager().register(getPlugin(), this);
        
        getLogger().info("Locales loaded: " + locales.size());

        return true;
    }

    public MessageTable get(Locale locale) {
        if (locale == null) {
            return locales.get(defaultLocale);
        }

        MessageTable table = locales.get(locale);

        return table != null ? table : locales.get(defaultLocale);
    }

    @Subscribe
    public void onProxyReload(ProxyReloadEvent event) {
        locales.clear();

        init();
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

            MessageTable table = new MessageTable(locale);
            for (Message message : Message.values()) {
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
}
