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

public class InfoCommand implements CommandWrapper {

    @Override
    public void handle(CommandSource source, CommandContext context) throws CommandExitException {
        assertPermission(source, "neutron.command.info");
        assertUsage(source, context.size() > 0);

        Player target = getProxy().getPlayer(context.get(0)).orElse(null);
        assertNotNull(source, target, Message.UNKNOWN_PLAYER, context.get(0));

        ServerConnection server = target.getCurrentServer().orElse(null);

        message(source, Message.INFO_HEADER, target.getUsername());
        message(source, Message.INFO_UUID, target.getUniqueId().toString());
        message(source, Message.INFO_VERSION, target.getProtocolVersion());
        message(source, Message.INFO_LOCALE, target.getPlayerSettings().getLocale());
        message(source, Message.INFO_SERVER, server != null ? server.getServerInfo().getName() : "N/A");
        message(source, Message.INFO_PING, target.getPing());
    }

    @Override
    public String getUsage() {
        return "/info (player)";
    }

    @Override
    public List<String> suggest(CommandSource source, String[] args) {
        if (args.length == 0) {
            return Arrays.asList();
        }
        return Strings.matchPlayer(getProxy(), args[0]).stream().map(Player::getUsername).collect(Collectors.toList());
    }
}
