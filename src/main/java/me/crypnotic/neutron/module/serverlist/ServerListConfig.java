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
package me.crypnotic.neutron.module.serverlist;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import net.kyori.text.TextComponent;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class ServerListConfig {

    @Getter
    @Setting("motd")
    private TextComponent motd = TextComponent.of("&7This velocity proxy is proudly powered by &bNeutron");

    @Getter
    @Setting("player-count")
    private PlayerCount playerCount;

    @Getter
    @Setting("server-preview")
    private ServerPreview serverPreview;

    @ConfigSerializable
    public static class PlayerCount {

        @Getter
        @Setting(value = "action", comment = "      # The server list player count has three different actions:\r\n" + "        # \r\n"
                + "        # CURRENT - player count matches the number of players online\r\n"
                + "        # ONEMORE - player count shows the number of players online plus 1 \r\n"
                + "        # PING - player count shows the sum of all backend servers' max player counts. Cached every 5 minutes\r\n"
                + "        # STATIC - player count will always be the number defined under `player-count`\r\n" + "        #\r\n"
                + "        # `player-count` is only used with the STATIC player count type")
        private PlayerCountAction action = PlayerCountAction.STATIC;

        @Getter
        @Setting("player-count")
        private int maxPlayerCount = 500;

        public static enum PlayerCountAction {
            CURRENT,
            ONEMORE,
            PING,
            STATIC;
        }
    }

    @ConfigSerializable
    public static class ServerPreview {

        @Getter
        @Setting(value = "action", comment = "      # The server list preview has three different actions:\r\n" + "        # \r\n"
                + "        # MESSAGE - preview will show the messages defined under `messages`\r\n"
                + "        # PLAYERS - preview matches the vanilla server preview of showing online players\r\n"
                + "        # EMPTY - preview is empty\r\n" + "        #\r\n" + "        # `messages` is only used with the MESSAGE preview type")
        private ServerPreviewAction action = ServerPreviewAction.MESSAGE;

        @Getter
        @Setting("messages")
        private List<String> messages = Arrays.asList("&7Powered by a &bNeutron");

        public static enum ServerPreviewAction {
            EMPTY,
            MESSAGE,
            PLAYERS;
        }
    }
}
