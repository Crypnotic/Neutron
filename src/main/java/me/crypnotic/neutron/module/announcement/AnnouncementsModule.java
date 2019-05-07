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

import java.util.HashMap;
import java.util.Map;

import com.velocitypowered.api.scheduler.ScheduledTask;

import me.crypnotic.neutron.api.configuration.Configuration;
import me.crypnotic.neutron.api.module.Module;
import me.crypnotic.neutron.util.ConfigHelper;
import ninja.leaping.configurate.ConfigurationNode;

public class AnnouncementsModule extends Module {

    private Configuration configuration;
    private Map<String, Announcement> announcements = new HashMap<String, Announcement>();

    public boolean init() {
        this.configuration = Configuration.builder().folder(getNeutron().getDataFolderPath()).name("announcements.conf").build();

        for (ConfigurationNode node : configuration.getNode().getChildrenMap().values()) {
            String id = node.getKey().toString();
            if (announcements.containsKey(id)) {
                getNeutron().getLogger().warn("An announcement list has already been defined with the id: " + id);
                continue;
            }

            AnnouncementData data = ConfigHelper.getSerializable(node, new AnnouncementData());
            if (data != null) {
                Announcement announcement = Announcement.create(getNeutron(), id, data);
                
                announcements.put(id, announcement);
            } else {
                getNeutron().getLogger().warn("Failed to load announcement list: " + id);
            }
        }

        getNeutron().getLogger().info("Announcements loaded: " + announcements.size());

        return true;
    }

    @Override
    public boolean reload() {
        return shutdown() && init();
    }

    @Override
    public boolean shutdown() {
        announcements.values().stream().map(Announcement::getTask).forEach(ScheduledTask::cancel);

        announcements.clear();

        return true;
    }

    @Override
    public String getName() {
        return "announcements";
    }
}
