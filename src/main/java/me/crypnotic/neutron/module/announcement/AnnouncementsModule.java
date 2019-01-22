package me.crypnotic.neutron.module.announcement;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.velocitypowered.api.scheduler.ScheduledTask;

import me.crypnotic.neutron.module.AbstractModule;
import me.crypnotic.neutron.util.FileIO;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;

public class AnnouncementsModule extends AbstractModule {

    private File file;
    private HoconConfigurationLoader loader;
    private ConfigurationNode root;
    private Map<String, Announcements> announcements = new HashMap<String, Announcements>();

    public boolean init() {
        try {
            this.file = FileIO.getOrCreate(getDataFolderPath(), "announcements.hocon");
            this.loader = HoconConfigurationLoader.builder().setFile(file).build();
            this.root = loader.load();
        } catch (IOException exception) {
            exception.printStackTrace();

            return false;
        }

        for (ConfigurationNode node : root.getChildrenMap().values()) {
            String id = node.getKey().toString();
            if (announcements.containsKey(id)) {
                getLogger().warn("An announcement list has already been defined with the id: " + id);
                continue;
            }

            Announcements announcement = Announcements.load(id, node);
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
