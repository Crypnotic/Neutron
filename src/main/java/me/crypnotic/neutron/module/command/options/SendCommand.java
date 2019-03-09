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
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import me.crypnotic.neutron.module.command.CommandContext;
import me.crypnotic.neutron.module.command.CommandWrapper;
import me.crypnotic.neutron.module.locale.LocaleMessage;

public class SendCommand extends CommandWrapper {

    @Override
    public void handle(CommandSource source, CommandContext context) throws CommandExitException {
        assertPermission(source, "neutron.command.send");
        assertUsage(source, context.size() > 1);

        RegisteredServer targetServer = getNeutron().getProxy().getServer(context.get(1).toLowerCase()).orElse(null);
        assertNotNull(source, targetServer, LocaleMessage.UNKNOWN_SERVER, context.get(1));

        switch (context.get(0).toLowerCase()) {
        case "current":
            assertPlayer(source, LocaleMessage.PLAYER_ONLY_SUBCOMMAND);

            Player player = (Player) source;
            ServerConnection currentServer = player.getCurrentServer().orElse(null);
            assertNotNull(player, currentServer, LocaleMessage.NOT_CONNECTED_TO_SERVER);

            currentServer.getServer().getPlayersConnected().forEach(targetPlayer -> {
                targetPlayer.createConnectionRequest(targetServer).fireAndForget();
                message(targetPlayer, LocaleMessage.SEND_MESSAGE, targetServer.getServerInfo().getName());
            });

            message(player, LocaleMessage.SEND_CURRENT, targetServer.getServerInfo().getName());
            break;
        case "all":
            getNeutron().getProxy().getAllPlayers().forEach(targetPlayer -> {
                targetPlayer.createConnectionRequest(targetServer).fireAndForget();
                message(targetPlayer, LocaleMessage.SEND_MESSAGE, targetServer.getServerInfo().getName());
            });

            message(source, LocaleMessage.SEND_ALL, targetServer.getServerInfo().getName());
            break;
        default:
            Player targetPlayer = getNeutron().getProxy().getPlayer(context.get(0)).orElse(null);
            assertNotNull(source, targetPlayer, LocaleMessage.UNKNOWN_PLAYER, context.get(0));

            targetPlayer.createConnectionRequest(targetServer).fireAndForget();
            message(targetPlayer, LocaleMessage.SEND_MESSAGE, targetServer.getServerInfo().getName());
            message(source, LocaleMessage.SEND_PLAYER, targetPlayer.getUsername(), targetServer.getServerInfo().getName());
            break;
        }
    }

    @Override
    public List<String> suggest(CommandSource source, String[] args) {
        if (args.length == 1) {
            List<String> result = getNeutron().getProxy().matchPlayer(args[0]).stream().map(Player::getUsername).collect(Collectors.toList());

            /* Inject `current`/`all` subcommands */
            result.add("current");
            result.add("all");

            return result;
        } else if (args.length == 2) {
            return getNeutron().getProxy().matchServer(args[0]).stream().map(s -> s.getServerInfo().getName()).collect(Collectors.toList());
        }
        return Arrays.asList();
    }

    @Override
    public String getUsage() {
        return "/send (player / current / all) (server)";
    }
}
