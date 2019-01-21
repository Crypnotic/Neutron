package me.crypnotic.neutron.module.announcement;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import com.moandjiezana.toml.Toml;
import com.velocitypowered.api.scheduler.ScheduledTask;

import me.crypnotic.neutron.module.AbstractModule;
import me.crypnotic.neutron.util.FileIO;

public class AnnouncementsModule extends AbstractModule {

    private File file;
    private Toml toml;
    private Map<String, Announcements> announcements;

    public boolean init() {
        this.file = FileIO.getOrCreate(getDataFolderPath(), "announcements.toml");
        this.toml = new Toml().read(file);
        this.announcements = new HashMap<String, Announcements>();

        for (Entry<String, Object> entry : toml.entrySet()) {
            String id = entry.getKey();
            if (announcements.containsKey(id)) {
                getLogger().warn("An announcement list has already been defined with the id: " + id);
                continue;
            }

            Announcements announcement = Announcements.load(id, toml.getTable(id));
            if (announcement != null) {
                announcements.put(id, announcement);
            } else {
                getLogger().warn("Failed to load announcement list: " + id);
            }

            ScheduledTask task = getProxy().getScheduler().buildTask(getPlugin(), new AnnouncementsTask(getPlugin(), announcement))
                    .repeat(announcement.getInterval(), TimeUnit.SECONDS).schedule();

            announcement.setTask(task);
        }

        getLogger().info("Announcements loaded: " + announcements.size());

        return true;
    }

    @Override
    public boolean reload() {
        return shutdown() && init();
    }

    @Override
    public boolean shutdown() {
        announcements.values().stream().map(Announcements::getTask).forEach(ScheduledTask::cancel);
        
        announcements.clear();

        return true;
    }

    @Override
    public String getName() {
        return "announcements";
    }
}
