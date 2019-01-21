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
import me.crypnotic.neutron.util.Strings;

public class SendCommand implements CommandWrapper {

    @Override
    public void handle(CommandSource source, CommandContext context) throws CommandExitException {
        assertPermission(source, "neutron.command.send");
        assertUsage(source, context.size() > 1);

        RegisteredServer targetServer = getProxy().getServer(context.get(1).toLowerCase()).orElse(null);
        assertNotNull(source, targetServer, "&cUnknown server: {0}", context.get(1));

        switch (context.get(0).toLowerCase()) {
        case "current":
            assertPlayer(source, "&cOnly players can use this subcommand.");

            Player player = (Player) source;
            ServerConnection currentServer = player.getCurrentServer().orElse(null);
            assertNotNull(player, currentServer, "&cYou must be connected to a server to use this subcommand.");

            currentServer.getServer().getPlayersConnected().forEach(targetPlayer -> {
                targetPlayer.createConnectionRequest(targetServer).fireAndForget();
                message(targetPlayer, "&aYou have been sent to &b{0}", targetServer.getServerInfo().getName());
            });

            message(player, "&aAll players from your current server have been sent to &b{0}", targetServer.getServerInfo().getName());
            break;
        case "all":
            getProxy().getAllPlayers().forEach(targetPlayer -> {
                targetPlayer.createConnectionRequest(targetServer).fireAndForget();
                message(targetPlayer, "&aYou have been sent to &b{0}", targetServer.getServerInfo().getName());
            });

            message(source, "&aAll players have been sent to &b{0}", targetServer.getServerInfo().getName());
            break;
        default:
            Player targetPlayer = getProxy().getPlayer(context.get(0)).orElse(null);
            assertNotNull(source, targetPlayer, "&cUnknown player: {0}", context.get(0));

            targetPlayer.createConnectionRequest(targetServer).fireAndForget();
            message(targetPlayer, "&aYou have been sent to &b{0}", targetServer.getServerInfo().getName());
            message(source, "&b{0} &ahas been sent to &b{1}", targetPlayer.getUsername(), targetServer.getServerInfo().getName());
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
