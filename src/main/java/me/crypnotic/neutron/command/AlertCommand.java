package me.crypnotic.neutron.command;

import com.velocitypowered.api.command.CommandSource;

import lombok.RequiredArgsConstructor;
import me.crypnotic.neutron.api.command.CommandContext;
import me.crypnotic.neutron.api.command.CommandWrapper;
import me.crypnotic.neutron.util.Strings;
import net.kyori.text.TextComponent;

@RequiredArgsConstructor
public class AlertCommand implements CommandWrapper {

    @Override
    public void handle(CommandSource source, CommandContext context) throws CommandExitException {
        assertPermission(source, "neutron.command.alert");
        assertUsage(source, context.size() > 0);

        TextComponent message = Strings.formatAndColor("&7&l[&c&lALERT&7&l] &e" + context.join(" "));

        getProxy().broadcast(message);

        /* Log to console since ProxyServer#broadcast doesn't do so */
        getProxy().getConsoleCommandSource().sendMessage(message);
    }

    @Override
    public String getUsage() {
        return "/alert (message)";
    }
}
