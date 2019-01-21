package me.crypnotic.neutron.command;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;

import me.crypnotic.neutron.api.command.CommandContext;
import me.crypnotic.neutron.api.command.CommandWrapper;
import me.crypnotic.neutron.util.Strings;

public class FindCommand implements CommandWrapper {

    @Override
    public void handle(CommandSource source, CommandContext context) throws CommandExitException {
        assertPermission(source, "neutron.command.find");
        assertUsage(source, context.size() > 0);

        Player target = getProxy().getPlayer(context.get(0)).orElse(null);
        assertNotNull(source, target, "&cUnknown player: {0}", context.get(0));

        ServerConnection server = target.getCurrentServer().get();
        /* We'll consider this offline as the Player is in a limbo state */
        assertNotNull(source, server, "&c{0} is currently offline.", context.get(0));

        source.sendMessage(Strings.formatAndColor("&b{0} &7is connected to &b{1}", target.getUsername(), server.getServerInfo().getName()));
    }

    @Override
    public List<String> suggest(CommandSource source, String[] args) {
        if (args.length == 0) {
            return Arrays.asList();
        }
        return Strings.matchPlayer(getProxy(), args[0]).stream().map(Player::getUsername).collect(Collectors.toList());
    }

    @Override
    public String getUsage() {
        return "/find (player)";
    }
}
