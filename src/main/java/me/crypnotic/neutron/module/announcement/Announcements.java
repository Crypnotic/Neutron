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
