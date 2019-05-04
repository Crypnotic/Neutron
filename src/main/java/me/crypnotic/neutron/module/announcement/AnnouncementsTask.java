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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gson.JsonParseException;
import lombok.RequiredArgsConstructor;
import me.crypnotic.neutron.NeutronPlugin;
import me.crypnotic.neutron.util.StringHelper;
import net.kyori.text.Component;
import net.kyori.text.TextComponent;
import net.kyori.text.serializer.ComponentSerializers;

@RequiredArgsConstructor
public class AnnouncementsTask implements Runnable {

    private final NeutronPlugin plugin;
    private final Announcements announcements;

    private List<String> localMessages;

    private volatile int index = 0;

    @Override
    public void run() {
        if (index == 0) {
            if (localMessages == null) {
                /* Create a local copy to avoid reading or shuffling the master copy */
                this.localMessages = new ArrayList<String>(announcements.getMessages());
            }

            if (!announcements.isMaintainOrder()) {
                Collections.shuffle(localMessages);
            }
        }

        TextComponent message;

        try {
            Component announcement = ComponentSerializers.JSON.deserialize(localMessages.get(index));
            message = StringHelper.formatAndColor(announcements.getPrefix()).append(announcement);
        } catch (JsonParseException e) {
            message = StringHelper.formatAndColor("{0}{1}", announcements.getPrefix(), localMessages.get(index));
        }

        plugin.getProxy().broadcast(message);

        index += 1;
        if (index == localMessages.size()) {
            index = 0;
        }
    }
}
