package me.crypnotic.neutron.module.announcement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.RequiredArgsConstructor;
import me.crypnotic.neutron.NeutronPlugin;
import me.crypnotic.neutron.util.Strings;
import net.kyori.text.TextComponent;

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

        TextComponent message = Strings.formatAndColor("{0}{1}", announcements.getPrefix(), localMessages.get(index));

        plugin.getProxy().broadcast(message);

        index += 1;
        if (index == localMessages.size()) {
            index = 0;
        }
    }
}
