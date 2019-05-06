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
package me.crypnotic.neutron.module.command.options;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;

import me.crypnotic.neutron.api.command.CommandContext;
import me.crypnotic.neutron.api.command.CommandWrapper;
import me.crypnotic.neutron.api.user.User;
import me.crypnotic.neutron.module.locale.message.LocaleMessage;
import net.kyori.text.Component;
import net.kyori.text.TextComponent;

public class ReplyCommand extends CommandWrapper {

    @Override
    public void handle(CommandSource source, CommandContext context) throws CommandExitException {
        assertPermission(source, "neutron.command.reply");
        assertUsage(source, context.size() > 0);

        CommandSource target = getUser(source).map(User::getReplyRecipient).orElse(null);
        assertNotNull(source, target, LocaleMessage.REPLY_NO_RECIPIENT, context.get(0));

        String sourceName = source instanceof Player ? ((Player) source).getUsername() : "Console";
        String targetName = target instanceof Player ? ((Player) target).getUsername() : "Console";

        Component content = TextComponent.of(context.join(" "));
        Component sourceMessage = getMessage(source, LocaleMessage.MESSAGE_SENDER, targetName).append(content);
        Component targetMessage = getMessage(target, LocaleMessage.MESSAGE_RECEIVER, sourceName).append(content);

        source.sendMessage(sourceMessage);
        target.sendMessage(targetMessage);

        getUser(target).ifPresent(user -> user.setReplyRecipient(source));
    }

    @Override
    public String getUsage() {
        return "/reply (message)";
    }

    @Override
    public List<String> suggest(CommandSource source, String[] args) {
        if (args.length == 1) {
            return getNeutron().getProxy().matchPlayer(args[0]).stream().map(Player::getUsername).collect(Collectors.toList());
        }
        return Arrays.asList();
    }
}
