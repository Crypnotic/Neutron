package me.crypnotic.neutron.module.command.options;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import me.crypnotic.neutron.api.command.CommandContext;
import me.crypnotic.neutron.api.command.CommandWrapper;
import me.crypnotic.neutron.api.locale.LocaleMessage;
import me.crypnotic.neutron.api.user.User;
import net.kyori.text.Component;
import net.kyori.text.TextComponent;
import net.kyori.text.event.HoverEvent;

import java.util.*;
import java.util.stream.Collectors;

public class IgnoreCommand extends CommandWrapper {
    @Override
    public void handle(CommandSource source, CommandContext context) throws CommandExitException {
        assertPermission(source, "neutron.command.ignore");
        assertPlayer(source, LocaleMessage.PLAYER_ONLY_COMMAND);

        if (context.size() == 0) {
            handleList(source);
        } else {
            handleToggle(source, context);
        }
    }

    private void handleList(CommandSource source) throws CommandExitException {
        User<? extends CommandSource> user = getUser(source).get();

        if (user.getIgnoredPlayers().isEmpty()) {
            message(source, LocaleMessage.IGNORE_LIST_EMPTY);
            return;
        }

        Component message = getMessage(source, LocaleMessage.IGNORE_LIST_HEAD);

        for (UUID uuid : user.getIgnoredPlayers()) {
            Optional<User<? extends CommandSource>> optUser = getNeutron().getUserManager().getUser(uuid);

            if (optUser.isPresent()) {
                User<? extends CommandSource> ignored = optUser.get();
                message = message.append(getMessage(source, LocaleMessage.IGNORE_LIST_ITEM, ignored.getName()));
            } else {
                message = message.append(
                    getMessage(source, LocaleMessage.IGNORE_LIST_ITEM_UNKNOWN, uuid.toString())
                        .hoverEvent(HoverEvent.of(HoverEvent.Action.SHOW_TEXT, TextComponent.of(uuid.toString()))));
            }

            source.sendMessage(message);
        }

    }

    private void handleToggle(CommandSource source, CommandContext context) throws CommandExitException {
        Collection<Player> matches = getNeutron().getProxy().matchPlayer(context.get(0));
        assertCustom(source, matches.size() != 0, LocaleMessage.UNKNOWN_PLAYER);
        assertCustom(source, matches.size() < 2, LocaleMessage.IGNORE_AMBIGUOUS_PLAYER);
        Player target = matches.stream().findFirst().get();

        User<? extends CommandSource> user = getUser(source).get();

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
        return "/ignore [player]";
    }
}
