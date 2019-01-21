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

public class InfoCommand implements CommandWrapper {

    @Override
    public void handle(CommandSource source, CommandContext context) throws CommandExitException {
        assertPermission(source, "neutron.command.info");
        assertUsage(source, context.size() > 0);

        Player target = getProxy().getPlayer(context.get(0)).orElse(null);
        assertNotNull(source, target, "&cUnknown player: {0}", context.get(0));

        ServerConnection server = target.getCurrentServer().orElse(null);

        message(source, "&l&7==> Information for player: &b{0}", target.getUsername());
        message(source, "&7Unique Id: &b{0}", target.getUniqueId().toString());
        message(source, "&7Minecraft Version: &b{0}", target.getProtocolVersion());
        message(source, "&7Locale: &b{0}", target.getPlayerSettings().getLocale());
        message(source, "&7Current Server: &b{0}", server != null ? server.getServerInfo().getName() : "N/A");
        message(source, "&7Ping: &b{0}", target.getPing());
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
