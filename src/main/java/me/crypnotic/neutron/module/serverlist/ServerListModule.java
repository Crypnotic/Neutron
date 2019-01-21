package me.crypnotic.neutron.module.serverlist;

import com.moandjiezana.toml.Toml;
import com.velocitypowered.api.proxy.server.ServerPing.SamplePlayer;

import lombok.Getter;
import me.crypnotic.neutron.module.AbstractModule;
import me.crypnotic.neutron.util.Strings;
import net.kyori.text.TextComponent;

public class ServerListModule extends AbstractModule {

    private Toml toml;
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
        this.toml = getModuleManager().getMainConfig().getTable(getName());
        this.motd = Strings.color(toml.getString("motd"));

        this.playerCountType = PlayerCountType.valueOf(toml.getString("player-count-type"));
        this.maxPlayerCount = toml.getLong("max-player-count").intValue();

        this.serverPreviewType = ServerPreviewType.valueOf(toml.getString("server-preview-type"));
        this.previewMessages = toml.getList("preview-messages").stream().map(Object::toString).map(Strings::toSamplePlayer)
                .toArray(SamplePlayer[]::new);

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
