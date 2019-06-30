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
import java.util.Optional;
import java.util.stream.Collectors;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;

import me.crypnotic.neutron.api.command.CommandContext;
import me.crypnotic.neutron.api.command.CommandWrapper;
import me.crypnotic.neutron.api.event.UserPrivateMessageEvent;
import me.crypnotic.neutron.api.locale.LocaleMessage;
import me.crypnotic.neutron.api.user.User;
import net.kyori.text.Component;
import net.kyori.text.TextComponent;

public class MessageCommand extends CommandWrapper {

    @Override
    public void handle(CommandSource source, CommandContext context) throws CommandExitException {
        assertPermission(source, "neutron.command.message");
        assertUsage(source, context.size() > 1);

        Player target = getNeutron().getProxy().getPlayer(context.get(0)).orElse(null);
        assertNotNull(source, target, LocaleMessage.UNKNOWN_PLAYER, context.get(0));

        String sourceName = source instanceof Player ? ((Player) source).getUsername() : "Console";

        Component content = TextComponent.of(context.join(" ", 1));
        Component sourceMessage = getMessage(source, LocaleMessage.MESSAGE_SENDER, target.getUsername()).append(content);
        Component targetMessage = getMessage(target, LocaleMessage.MESSAGE_RECEIVER, sourceName).append(content);

        final Optional<User<? extends CommandSource>> sender = getUser(source);
        final Optional<User<? extends CommandSource>> recipient = getUser(target);

        // Ensure source is not ignoring target
        assertNotIgnoring(source, source, target, LocaleMessage.MESSAGE_IGNORING_TARGET);

        // Ensure target is not ignoring source
        if (source instanceof Player) {
            assertNotIgnoring(source, target, (Player) source, LocaleMessage.MESSAGE_IGNORED_BY_TARGET);
        }

        UserPrivateMessageEvent event = new UserPrivateMessageEvent(sender, recipient, content, false);

        getNeutron().getProxy().getEventManager().fire(event).thenAccept(resultEvent -> {
            UserPrivateMessageEvent.PrivateMessageResult result = resultEvent.getResult();
            if (result.isAllowed()) {
                source.sendMessage(sourceMessage);
                target.sendMessage(targetMessage);

                sender.ifPresent(user -> user.setReplyRecipient(target));
                recipient.ifPresent(user -> user.setReplyRecipient(source));
            } else {
                result.getReason().ifPresent(source::sendMessage);
            }
        });
    }

    @Override
    public String getUsage() {
        return "/message (player) (message)";
    }

    @Override
    public List<String> suggest(CommandSource source, String[] args) {
        if (args.length == 1) {
            return getNeutron().getProxy().matchPlayer(args[0]).stream().map(Player::getUsername).collect(Collectors.toList());
        }
        return Arrays.asList();
    }
}
