/*
* This file is part of Neutron, licensed under the MIT License
*
* Copyright (c) 2019 Crypnotic <crypnoticofficial@gmail.com>
*
*
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in all
* copies or substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
* SOFTWARE.
*/
package me.crypnotic.neutron.module.locale;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum LocaleMessage {

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

    public static LocaleMessage match(String key) {
        try {
            return LocaleMessage.valueOf(key.toUpperCase().replace("-", "_"));
        } catch (IllegalArgumentException exception) {
            return null;
        }
    }
}
