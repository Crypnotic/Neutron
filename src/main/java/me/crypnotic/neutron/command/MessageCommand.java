package me.crypnotic.neutron.command;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;

import me.crypnotic.neutron.api.command.CommandContext;
import me.crypnotic.neutron.api.command.CommandWrapper;
import me.crypnotic.neutron.util.Strings;
import net.kyori.text.TextComponent;

public class MessageCommand implements CommandWrapper {

    @Override
    public void handle(CommandSource source, CommandContext context) throws CommandExitException {
        assertPermission(source, "neutron.command.message");
        assertUsage(source, context.size() > 1);

        Player target = getProxy().getPlayer(context.get(0)).orElse(null);
        assertNotNull(source, target, "&cUnknown player: {0}", context.get(0));

        String sourceName = source instanceof Player ? ((Player) source).getUsername() : "Console";

        TextComponent content = TextComponent.of(context.join(" ", 1));
        TextComponent sourceMessage = Strings.formatAndColor("&b&lme » {0} &7> &o", target.getUsername()).append(content);
        TextComponent targetMessage = Strings.formatAndColor("&b&l{0} » me &7> &o", sourceName).append(content);

        source.sendMessage(sourceMessage);
        target.sendMessage(targetMessage);
    }

    @Override
    public String getUsage() {
        return "/message (player) (message)";
    }

    @Override
    public List<String> suggest(CommandSource source, String[] args) {
        if (args.length == 0) {
            return Arrays.asList();
        }
        return Strings.matchPlayer(getProxy(), args[0]).stream().map(Player::getUsername).collect(Collectors.toList());
    }
}
