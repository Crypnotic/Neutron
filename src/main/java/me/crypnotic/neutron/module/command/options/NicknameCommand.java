package me.crypnotic.neutron.module.command.options;

import com.velocitypowered.api.command.CommandSource;
import me.crypnotic.neutron.api.command.CommandContext;
import me.crypnotic.neutron.api.command.CommandWrapper;
import me.crypnotic.neutron.api.locale.LocaleMessage;
import me.crypnotic.neutron.api.user.User;

public class NicknameCommand extends CommandWrapper {
    @Override
    public void handle(CommandSource source, CommandContext context) throws CommandExitException {
        assertPermission(source, "neutron.command.nickname");
        assertUsage(source, context.size() > 0);

        User user = getUser(source).orElseThrow(CommandExitException::new);

        assertCustom(source, user.isPlayer(), LocaleMessage.PLAYER_ONLY_COMMAND);

        switch (context.get(0)) {
            case "get":
                if (user.getNickname() != null) {
                    message(source, LocaleMessage.NICKNAME_CURRENT, user.getNickname());
                } else {
                    message(source, LocaleMessage.NICKNAME_NONE);
                }
                break;
            case "clear":
                user.setNickname(null);
                message(source, LocaleMessage.NICKNAME_CLEAR);
                break;
            case "set":
                assertUsage(source, context.size() > 1);
                user.setNickname(context.get(1));
                message(source, LocaleMessage.NICKNAME_SET, user.getNickname());
                break;
            default:
                assertUsage(source, false);
        }
    }

    @Override
    public String getUsage() {
        return "/nickname <get|clear|set <nickname>>";
    }
}
