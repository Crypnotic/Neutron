package me.crypnotic.neutron.command;

import java.util.Collection;
import java.util.stream.Collectors;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerInfo;

import me.crypnotic.neutron.api.command.CommandContext;
import me.crypnotic.neutron.api.command.CommandWrapper;
import me.crypnotic.neutron.util.Strings;
import net.kyori.text.TextComponent;
import net.kyori.text.event.ClickEvent;
import net.kyori.text.event.HoverEvent;

public class ListCommand implements CommandWrapper {

    @Override
    public void handle(CommandSource source, CommandContext context) throws CommandExitException {
        assertPermission(source, "neutron.command.list");

        for (RegisteredServer server : getProxy().getAllServers()) {
            ServerInfo info = server.getServerInfo();
            Collection<Player> players = server.getPlayersConnected();

            String playerString = players.stream().map(Player::getUsername).sorted(String.CASE_INSENSITIVE_ORDER).collect(Collectors.joining(", "));

            TextComponent message = Strings.formatAndColor("&a[{0}] &e{1} player{2} online", info.getName(), players.size(),
                    players.size() == 1 ? "" : "s");

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
