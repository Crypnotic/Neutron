package me.crypnotic.neutron.module.serverlist;

import java.util.ArrayList;

import com.velocitypowered.api.proxy.server.ServerPing.SamplePlayer;

import lombok.Getter;
import me.crypnotic.neutron.module.AbstractModule;
import me.crypnotic.neutron.util.Strings;
import net.kyori.text.TextComponent;
import ninja.leaping.configurate.ConfigurationNode;

public class ServerListModule extends AbstractModule {

    private ConfigurationNode root;

    @Getter
    private TextComponent motd;
    @Getter
    private PlayerCountType playerCountType;
    @Getter
    private int maxPlayerCount;
    @Getter
    private ServerPreviewType serverPreviewType;
    @Getter
    private SamplePlayer[] previewMessages;

    @Override
    public boolean init() {
        this.root = getModuleManager().getRoot().getNode(getName());
        this.motd = Strings.color(getOrSet(root, "motd", "&7This velocity proxy is proudly powered by &bNeutron", String.class));

        this.playerCountType = PlayerCountType.valueOf(getOrSet(root, "player-count-type", "STATIC", String.class));
        this.maxPlayerCount = getOrSet(root, "max-player-count", 500, Integer.class);

        this.serverPreviewType = ServerPreviewType.valueOf(getOrSet(root, "server-preview-type", "MESSAGE", String.class));
        this.previewMessages = Strings.toSamplePlayerArray(getOrSetList(root, "preview-messages", new ArrayList<String>(), String.class));

        getProxy().getEventManager().register(getPlugin(), new ServerListHandler(this));

        return true;
    }

    @Override
    public boolean reload() {
        return init();
    }

    @Override
    public boolean shutdown() {
        return true;
    }

    @Override
    public String getName() {
        return "serverlist";
    }

    public enum PlayerCountType {
        CURRENT,
        ONEMORE,
        STATIC;
    }

    public enum ServerPreviewType {
        EMPTY,
        MESSAGE,
        PLAYERS;
    }
}
