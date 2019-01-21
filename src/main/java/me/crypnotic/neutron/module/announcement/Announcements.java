package me.crypnotic.neutron.module.announcement;

import java.util.List;

import com.moandjiezana.toml.Toml;
import com.velocitypowered.api.scheduler.ScheduledTask;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

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

    public static Announcements load(String id, Toml toml) {
        long interval = toml.getLong("interval");
        boolean maintainOrder = toml.getBoolean("maintain-order");
        String prefix = toml.contains("prefix") ? toml.getString("prefix") : "";
        List<String> messages = toml.getList("messages");

        return new Announcements(id, interval, maintainOrder, messages, prefix);
    }
}
