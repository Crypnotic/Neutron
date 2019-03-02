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
package me.crypnotic.neutron.module.command;

import java.util.Locale;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import me.crypnotic.neutron.NeutronPlugin;
import me.crypnotic.neutron.api.Neutron;
import me.crypnotic.neutron.module.locale.LocaleMessage;
import me.crypnotic.neutron.module.locale.LocaleMessageTable;
import me.crypnotic.neutron.module.locale.LocaleModule;
import me.crypnotic.neutron.util.Strings;
import net.kyori.text.TextComponent;

public abstract class CommandWrapper implements Command {

    @Getter
    @Setter
    private boolean enabled;
    @Getter
    @Setter
    private String[] aliases;

    @Override
    public void execute(CommandSource source, String[] args) {
        try {
            handle(source, new CommandContext(args));
        } catch (CommandExitException exception) {
            /* Catch silently */
        }
    }

    @SneakyThrows
    public void assertUsage(CommandSource source, boolean assertion) {
        assertCustom(source, assertion, LocaleMessage.INVALID_USAGE, getUsage());
    }

    @SneakyThrows
    public void assertPlayer(CommandSource source, LocaleMessage message, Object... values) {
        assertCustom(source, source instanceof Player, message, values);
    }

    @SneakyThrows
    public void assertNull(CommandSource source, Object value, LocaleMessage message, Object... values) {
        assertCustom(source, value == null, message, values);
    }

    @SneakyThrows
    public void assertNotNull(CommandSource source, Object value, LocaleMessage message, Object... values) {
        assertCustom(source, value != null, message, values);
    }

    @SneakyThrows
    public void assertPermission(CommandSource source, String permission) {
        assertCustom(source, source.hasPermission(permission), LocaleMessage.NO_PERMISSION);
    }

    @SneakyThrows
    public void assertCustom(CommandSource source, boolean assertion, LocaleMessage message, Object... values) {
        if (!assertion) {
            message(source, message, values);

            throw new CommandExitException();
        }
    }

    public void message(CommandSource source, LocaleMessage message, Object... values) {
        source.sendMessage(getMessage(source, message, values));
    }

    public TextComponent getMessage(CommandSource source, LocaleMessage message, Object... values) {
        LocaleModule module = getNeutron().getModuleManager().get(LocaleModule.class);
        if (module.isEnabled()) {
            Locale locale = module.getDefaultLocale();
            if (source instanceof Player) {
                locale = ((Player) source).getPlayerSettings().getLocale();
            }

            LocaleMessageTable table = module.get(locale);
            if (table != null) {
                return table.get(message, values);
            }
        }
        return Strings.formatAndColor(message.getDefaultMessage(), values);
    }

    public abstract void handle(CommandSource source, CommandContext context) throws CommandExitException;

    public abstract String getUsage();

    public class CommandExitException extends Exception {
        private static final long serialVersionUID = -1299193476106186693L;
    }

    public NeutronPlugin getNeutron() {
        return Neutron.getNeutron();
    }
}
