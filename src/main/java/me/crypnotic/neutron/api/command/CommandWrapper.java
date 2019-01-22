package me.crypnotic.neutron.api.command;

import java.util.Locale;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;

import lombok.SneakyThrows;
import me.crypnotic.neutron.api.INeutronAccessor;
import me.crypnotic.neutron.api.locale.Message;
import me.crypnotic.neutron.api.locale.MessageTable;
import me.crypnotic.neutron.util.Strings;
import net.kyori.text.TextComponent;

public interface CommandWrapper extends Command, INeutronAccessor {

    @Override
    default void execute(CommandSource source, String[] args) {
        try {
            handle(source, new CommandContext(args));
        } catch (CommandExitException exception) {
            /* Catch silently */
        }
    }

    @SneakyThrows
    default void assertUsage(CommandSource source, boolean assertion) {
        assertCustom(source, assertion, Message.INVALID_USAGE, getUsage());
    }

    @SneakyThrows
    default void assertPlayer(CommandSource source, Message message, Object... values) {
        assertCustom(source, source instanceof Player, message, values);
    }

    @SneakyThrows
    default void assertNull(CommandSource source, Object value, Message message, Object... values) {
        assertCustom(source, value == null, message, values);
    }

    @SneakyThrows
    default void assertNotNull(CommandSource source, Object value, Message message, Object... values) {
        assertCustom(source, value != null, message, values);
    }

    @SneakyThrows
    default void assertPermission(CommandSource source, String permission) {
        assertCustom(source, source.hasPermission(permission), Message.NO_PERMISSION);
    }

    @SneakyThrows
    default void assertCustom(CommandSource source, boolean assertion, Message message, Object... values) {
        if (!assertion) {
            message(source, message, values);

            throw new CommandExitException();
        }
    }

    default void message(CommandSource source, Message message, Object... values) {
        source.sendMessage(getMessage(source, message, values));
    }

    default void message(CommandSource source, String message, Object... values) {
        source.sendMessage(Strings.formatAndColor(message, values));
    }

    default TextComponent getMessage(CommandSource source, Message message, Object... values) {
        Locale locale = null;
        if (source instanceof Player) {
            locale = ((Player) source).getPlayerSettings().getLocale();
        }

        MessageTable table = getLocaleManager().get(locale);
        if (table == null) {
            return Strings.formatAndColor(message.getDefaultMessage(), values);
        } else {
            return table.get(message, values);
        }
    }

    void handle(CommandSource source, CommandContext context) throws CommandExitException;

    String getUsage();

    public class CommandExitException extends Exception {
        private static final long serialVersionUID = -1299193476106186693L;
    }
}
