package me.crypnotic.neutron.command;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import me.crypnotic.neutron.api.command.CommandContext;
import me.crypnotic.neutron.api.command.CommandWrapper;
import me.crypnotic.neutron.api.locale.Message;
import me.crypnotic.neutron.util.Strings;

public class SendCommand implements CommandWrapper {

    @Override
    public void handle(CommandSource source, CommandContext context) throws CommandExitException {
        assertPermission(source, "neutron.command.send");
        assertUsage(source, context.size() > 1);

        RegisteredServer targetServer = getProxy().getServer(context.get(1).toLowerCase()).orElse(null);
        assertNotNull(source, targetServer, Message.UNKNOWN_SERVER, context.get(1));

        switch (context.get(0).toLowerCase()) {
        case "current":
            assertPlayer(source, Message.PLAYER_ONLY_SUBCOMMAND);

            Player player = (Player) source;
            ServerConnection currentServer = player.getCurrentServer().orElse(null);
            assertNotNull(player, currentServer, Message.NOT_CONNECTED_TO_SERVER);

            currentServer.getServer().getPlayersConnected().forEach(targetPlayer -> {
                targetPlayer.createConnectionRequest(targetServer).fireAndForget();
                message(targetPlayer, Message.SEND_MESSAGE, targetServer.getServerInfo().getName());
            });

            message(player, Message.SEND_CURRENT, targetServer.getServerInfo().getName());
            break;
        case "all":
            getProxy().getAllPlayers().forEach(targetPlayer -> {
                targetPlayer.createConnectionRequest(targetServer).fireAndForget();
                message(targetPlayer, Message.SEND_MESSAGE, targetServer.getServerInfo().getName());
            });

            message(source, Message.SEND_ALL, targetServer.getServerInfo().getName());
            break;
        default:
            Player targetPlayer = getProxy().getPlayer(context.get(0)).orElse(null);
            assertNotNull(source, targetPlayer, Message.UNKNOWN_PLAYER, context.get(0));

            targetPlayer.createConnectionRequest(targetServer).fireAndForget();
            message(targetPlayer, Message.SEND_MESSAGE, targetServer.getServerInfo().getName());
            message(source, Message.SEND_PLAYER, targetPlayer.getUsername(), targetServer.getServerInfo().getName());
            break;
        }
    }

    @Override
    public List<String> suggest(CommandSource source, String[] args) {
        if (args.length == 1) {
            List<String> result = Strings.matchPlayer(getProxy(), args[0]).stream().map(Player::getUsername).collect(Collectors.toList());

            /* Inject `current`/`all` subcommands */
            result.add("current");
            result.add("all");

            return result;
        } else if (args.length == 2) {
            return Strings.matchServer(getProxy(), args[1]).stream().map(server -> server.getServerInfo().getName()).collect(Collectors.toList());
        }
        return Arrays.asList();
    }

    @Override
    public String getUsage() {
        return "/send (player / current / all) (server)";
    }
}
