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

import java.util.Collection;
import java.util.stream.Collectors;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerInfo;

import me.crypnotic.neutron.module.command.CommandContext;
import me.crypnotic.neutron.module.command.CommandWrapper;
import me.crypnotic.neutron.module.locale.LocaleMessage;
import net.kyori.text.TextComponent;
import net.kyori.text.event.ClickEvent;
import net.kyori.text.event.HoverEvent;

public class GlistCommand extends CommandWrapper {

    @Override
    public void handle(CommandSource source, CommandContext context) throws CommandExitException {
        assertPermission(source, "neutron.command.glist");

        message(source, LocaleMessage.LIST_HEADER, getNeutron().getProxy().getPlayerCount());

        for (RegisteredServer server : getNeutron().getProxy().getAllServers()) {
            ServerInfo info = server.getServerInfo();
            Collection<Player> players = server.getPlayersConnected();

            String playerString = players.stream().map(Player::getUsername).sorted(String.CASE_INSENSITIVE_ORDER).collect(Collectors.joining(", "));

            TextComponent message = getMessage(source, LocaleMessage.LIST_MESSAGE, info.getName(), players.size());

            message = message.hoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.of(playerString)));
            message = message.clickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/server " + info.getName()));

            source.sendMessage(message);
        }
    }

    @Override
    public String getUsage() {
        return "/glist";
    }
}
