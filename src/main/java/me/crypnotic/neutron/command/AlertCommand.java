package me.crypnotic.neutron.command;

import com.velocitypowered.api.command.CommandSource;

import lombok.RequiredArgsConstructor;
import me.crypnotic.neutron.api.command.CommandContext;
import me.crypnotic.neutron.api.command.CommandWrapper;
import me.crypnotic.neutron.api.locale.Message;

@RequiredArgsConstructor
public class AlertCommand implements CommandWrapper {

    @Override
    public void handle(CommandSource source, CommandContext context) throws CommandExitException {
        assertPermission(source, "neutron.command.alert");
        assertUsage(source, context.size() > 0);

        String message = context.join(" ");

        getProxy().getAllPlayers().forEach(target -> message(target, Message.ALERT_MESSAGE, message));

        /* Log to console since ProxyServer#broadcast doesn't do so */
        message(getProxy().getConsoleCommandSource(), Message.ALERT_MESSAGE, message);
    }

    @Override
    public String getUsage() {
        return "/alert (message)";
    }
}
