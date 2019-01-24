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
package me.crypnotic.neutron.api.command;

import java.util.Locale;
import java.util.Optional;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;

import lombok.SneakyThrows;
import me.crypnotic.neutron.api.INeutronAccessor;
import me.crypnotic.neutron.module.locale.LocaleModule;
import me.crypnotic.neutron.module.locale.LocaleMessage;
import me.crypnotic.neutron.module.locale.LocaleMessageTable;
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
        assertCustom(source, assertion, LocaleMessage.INVALID_USAGE, getUsage());
    }

    @SneakyThrows
    default void assertPlayer(CommandSource source, LocaleMessage message, Object... values) {
        assertCustom(source, source instanceof Player, message, values);
    }

    @SneakyThrows
    default void assertNull(CommandSource source, Object value, LocaleMessage message, Object... values) {
        assertCustom(source, value == null, message, values);
    }

    @SneakyThrows
    default void assertNotNull(CommandSource source, Object value, LocaleMessage message, Object... values) {
        assertCustom(source, value != null, message, values);
    }

    @SneakyThrows
    default void assertPermission(CommandSource source, String permission) {
        assertCustom(source, source.hasPermission(permission), LocaleMessage.NO_PERMISSION);
    }

    @SneakyThrows
    default void assertCustom(CommandSource source, boolean assertion, LocaleMessage message, Object... values) {
        if (!assertion) {
            message(source, message, values);

            throw new CommandExitException();
        }
    }

    default void message(CommandSource source, LocaleMessage message, Object... values) {
        source.sendMessage(getMessage(source, message, values));
    }

    default TextComponent getMessage(CommandSource source, LocaleMessage message, Object... values) {
        Locale locale = null;
        if (source instanceof Player) {
            locale = ((Player) source).getPlayerSettings().getLocale();
        }

        Optional<LocaleModule> module = getModuleManager().get(LocaleModule.class);
        if (module.isPresent() && module.get().isEnabled()) {
            LocaleMessageTable table = module.get().get(locale);
            if (table != null) {
                return table.get(message, values);
            }
        }
        return Strings.formatAndColor(message.getDefaultMessage(), values);
    }

    void handle(CommandSource source, CommandContext context) throws CommandExitException;

    String getUsage();

    public class CommandExitException extends Exception {
        private static final long serialVersionUID = -1299193476106186693L;
    }
}
