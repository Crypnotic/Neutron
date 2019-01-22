package me.crypnotic.neutron.command;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;

import me.crypnotic.neutron.api.command.CommandContext;
import me.crypnotic.neutron.api.command.CommandWrapper;
import me.crypnotic.neutron.api.locale.Message;
import me.crypnotic.neutron.util.Strings;

public class FindCommand implements CommandWrapper {

    @Override
    public void handle(CommandSource source, CommandContext context) throws CommandExitException {
        assertPermission(source, "neutron.command.find");
        assertUsage(source, context.size() > 0);

        Player target = getProxy().getPlayer(context.get(0)).orElse(null);
        assertNotNull(source, target, Message.UNKNOWN_PLAYER, context.get(0));

        ServerConnection server = target.getCurrentServer().get();
        /* We'll consider this offline as the Player is in a limbo state */
        assertNotNull(source, server, Message.PLAYER_OFFLINE, context.get(0));

        message(source, Message.FIND_MESSAGE, target.getUsername(), server.getServerInfo().getName());
    }

    @Override
    public List<String> suggest(CommandSource source, String[] args) {
        if (args.length == 1) {
            return Strings.matchPlayer(getProxy(), args[0]).stream().map(Player::getUsername).collect(Collectors.toList());
        }
        return Arrays.asList();
    }

    @Override
    public String getUsage() {
        return "/find (player)";
    }
}
