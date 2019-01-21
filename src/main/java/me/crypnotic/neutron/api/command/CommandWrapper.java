package me.crypnotic.neutron.api.command;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;

import lombok.SneakyThrows;
import me.crypnotic.neutron.api.INeutronAccessor;
import me.crypnotic.neutron.util.Strings;

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
        assertCustom(source, assertion, "&cUsage: {0}", getUsage());
    }

    @SneakyThrows
    default void assertPlayer(CommandSource source, String message, Object... values) {
        assertCustom(source, source instanceof Player, message, values);
    }

    @SneakyThrows
    default void assertNull(CommandSource source, Object value, String message, Object... values) {
        assertCustom(source, value == null, message, values);
    }

    @SneakyThrows
    default void assertNotNull(CommandSource source, Object value, String message, Object... values) {
        assertCustom(source, value != null, message, values);
    }

    @SneakyThrows
    default void assertPermission(CommandSource source, String permission) {
        assertCustom(source, source.hasPermission(permission), "&cYou don't have permission to execute this command.");
    }

    @SneakyThrows
    default void assertCustom(CommandSource source, boolean assertion, String message, Object... values) {
        if (!assertion) {
            message(source, message, values);

            throw new CommandExitException();
        }
    }

    default void message(CommandSource source, String message, Object... values) {
        source.sendMessage(Strings.formatAndColor(message, values));
    }

    void handle(CommandSource source, CommandContext context) throws CommandExitException;

    String getUsage();

    public class CommandExitException extends Exception {
        private static final long serialVersionUID = -1299193476106186693L;
    }
}
