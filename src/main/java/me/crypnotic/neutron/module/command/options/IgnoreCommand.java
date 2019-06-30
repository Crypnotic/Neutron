package me.crypnotic.neutron.module.command.options;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import me.crypnotic.neutron.api.command.CommandContext;
import me.crypnotic.neutron.api.command.CommandWrapper;
import me.crypnotic.neutron.api.locale.LocaleMessage;
import me.crypnotic.neutron.api.user.User;

import java.util.Collection;

public class IgnoreCommand extends CommandWrapper {
    @Override
    public void handle(CommandSource source, CommandContext context) throws CommandExitException {
        assertPermission(source, "neutron.command.ignore");
        assertPlayer(source, LocaleMessage.PLAYER_ONLY_COMMAND);
        assertUsage(source, context.size() > 0);

        Collection<Player> matches = getNeutron().getProxy().matchPlayer(context.get(0));
        assertCustom(source, matches.size() != 0, LocaleMessage.UNKNOWN_PLAYER);
        assertCustom(source, matches.size() < 2, LocaleMessage.IGNORE_AMBIGUOUS_PLAYER);
        Player target = matches.stream().findFirst().get();

        User user = getUser(source).get();

        boolean newState = !user.isIgnoringPlayer(target);

        if (context.size() > 1) {
            newState = Boolean.parseBoolean(context.get(1));
        }

        user.setIgnoringPlayer(target, newState);

        message(source,
            user.isIgnoringPlayer(target) ? LocaleMessage.IGNORE_NOW_IGNORING : LocaleMessage.IGNORE_NOW_NOT_IGNORING,
            target.getUsername());
    }

    @Override
    public String getUsage() {
        return "/ignore (player)";
    }
}
