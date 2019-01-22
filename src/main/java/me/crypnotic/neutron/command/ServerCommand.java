package me.crypnotic.neutron.command;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import me.crypnotic.neutron.api.command.CommandContext;
import me.crypnotic.neutron.api.command.CommandWrapper;
import me.crypnotic.neutron.api.locale.Message;
import me.crypnotic.neutron.util.Strings;

public class ServerCommand implements CommandWrapper {

    @Override
    public void handle(CommandSource source, CommandContext context) throws CommandExitException {
        assertPlayer(source, Message.PLAYER_ONLY_COMMAND);
        assertPermission(source, "neutron.command.server");
        assertUsage(source, context.size() > 0);

        Player player = (Player) source;
        RegisteredServer server = getProxy().getServer(context.get(0)).orElse(null);

        assertNotNull(source, server, Message.UNKNOWN_SERVER, context.get(0));

        player.createConnectionRequest(server).fireAndForget();
    }

    @Override
    public String getUsage() {
        return "/server (name)";
    }

    @Override
    public List<String> suggest(CommandSource source, String[] args) {
        if (args.length == 1) {
            return Strings.matchServer(getProxy(), args[0]).stream().map(server -> server.getServerInfo().getName()).collect(Collectors.toList());
        }
        return Arrays.asList();
    }
}
