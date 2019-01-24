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
package me.crypnotic.neutron.module.announcement;

import java.util.List;

import com.velocitypowered.api.scheduler.ScheduledTask;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ninja.leaping.configurate.ConfigurationNode;

@RequiredArgsConstructor
public class Announcements {

    @Getter
    private final String id;
    @Getter
    private final long interval;
    @Getter
    private final boolean maintainOrder;
    @Getter
    private final List<String> messages;
    @Getter
    private final String prefix;

    @Getter
    @Setter
    private ScheduledTask task;

    public static Announcements load(String id, ConfigurationNode node) {
        long interval = node.getNode("interval").getLong();
        boolean maintainOrder = node.getNode("maintain-order").getBoolean();
        String prefix = node.getNode("prefix").getString("");
        List<String> messages = node.getNode("messages").getList(Object::toString);

        return new Announcements(id, interval, maintainOrder, messages, prefix);
    }
}
