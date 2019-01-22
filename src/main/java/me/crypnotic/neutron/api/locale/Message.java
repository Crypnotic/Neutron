package me.crypnotic.neutron.api.locale;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Message {

    ALERT_MESSAGE("alert-message", "&7&l[&c&lALERT&7&l] &e{0}"),

    FIND_MESSAGE("find-message", "&b{0} &7is connected to &b{1}"),

    INFO_HEADER("info-header", "&l&7==> Information for player: &b{0}"),
    INFO_LOCALE("info-locale", "&7Locale: &b{0}"),
    INFO_PING("info-ping", "&7Ping: &b{0}"),
    INFO_SERVER("info-server", "&7Current Server: &b{0}"),
    INFO_UUID("info-uuid", "&7Unique Id: &b{0}"),
    INFO_VERSION("info-version", "&7Minecraft Version: &b{0}"),

    INVALID_USAGE("invalid-usage", "&cUsage: {0}"),

    LIST_MESSAGE("list-message", "&a[{0}] &e{1} player{2} online"),

    MESSAGE_SENDER("message-sender", "&b&lme » {0} &7> &o"),
    MESSAGE_RECEIVER("message-receiver", "&b&l{0} » me &7> &o"),

    NO_PERMISSION("no-permission", "&cYou don't have permission to execute this command."),
    NOT_CONNECTED_TO_SERVER("not-connected-to-server", "&cYou must be connected to a server to use this subcommand."),

    SEND_ALL("send-all", "&aAll players have been sent to &b{0}"),
    SEND_CURRENT("send-current", "&aAll players from your current server have been sent to &b{0}"),
    SEND_MESSAGE("send-message", "&aYou have been sent to &b{0}"),
    SEND_PLAYER("send-player", "&b{0} &ahas been sent to &b{1}"),

    PLAYER_OFFLINE("player-offline", "&c{0} is currently offline."),
    PLAYER_ONLY_COMMAND("player-only-command", "&cOnly players can use this subcommand."),
    PLAYER_ONLY_SUBCOMMAND("player-only-subcommand", "&cOnly players can use this subcommand."),

    UNKNOWN_PLAYER("unknown-player", "&cUnknown player: {0}"),
    UNKNOWN_SERVER("unknown-server", "&cUnknown server: {0}");

    @Getter
    private final String name;
    @Getter
    private final String defaultMessage;

    public static Message match(String key) {
        try {
            return Message.valueOf(key.toUpperCase().replace("-", "_"));
        } catch (IllegalArgumentException exception) {
            return null;
        }
    }
}
