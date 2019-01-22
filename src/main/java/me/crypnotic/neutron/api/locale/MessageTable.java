package me.crypnotic.neutron.api.locale;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.crypnotic.neutron.util.Strings;
import net.kyori.text.TextComponent;

@RequiredArgsConstructor
public class MessageTable {

    @Getter
    private final Locale locale;
    private final Map<Message, String> messages = new HashMap<Message, String>();

    public TextComponent get(Message key, Object... values) {
        String message = messages.get(key);

        return Strings.formatAndColor(message != null ? message : key.getDefaultMessage(), values);
    }

    public boolean set(Message key, String message) {
        /* Return true if not entry existed previously */
        return messages.put(key, message) == null;
    }
}
