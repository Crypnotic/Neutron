package me.crypnotic.neutron.manager;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.moandjiezana.toml.Toml;

import lombok.Getter;
import me.crypnotic.neutron.api.INeutronAccessor;
import me.crypnotic.neutron.api.locale.Message;
import me.crypnotic.neutron.api.locale.MessageTable;
import me.crypnotic.neutron.util.FileIO;

public class LocaleManager implements INeutronAccessor {

    private File folder;
    private Map<Locale, MessageTable> locales = new HashMap<Locale, MessageTable>();
    @Getter
    private final Locale defaultLocale = Locale.forLanguageTag("en_US");

    public boolean init() {
        this.folder = FileIO.getOrCreateDirectory(getDataFolderPath(), "locales");

        /* Copy default en_US locale file if not found */
        FileIO.getOrCreateLocale(folder.toPath(), "en_US.toml");

        getLogger().warn("" + folder.listFiles().length);

        for (File file : folder.listFiles()) {
            loadMessageTable(file);
        }

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

    private void loadMessageTable(File file) {
        try {
            String localeName = file.getName().split("\\.")[0];

            Locale locale = Locale.forLanguageTag(localeName.replace("_", "-"));
            if (locale == null) {
                getLogger().warn("Unknown locale attempted to load: " + localeName);
                return;
            }

            Toml toml = new Toml().read(file);
            MessageTable table = new MessageTable(locale);
            for (Message message : Message.values()) {
                String content = toml.getString(message.getName());
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
