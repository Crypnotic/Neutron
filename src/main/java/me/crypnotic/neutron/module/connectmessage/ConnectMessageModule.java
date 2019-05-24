package me.crypnotic.neutron.module.connectmessage;

import lombok.Getter;
import me.crypnotic.neutron.api.StateResult;
import me.crypnotic.neutron.api.module.Module;
import me.crypnotic.neutron.util.ConfigHelper;

public class ConnectMessageModule extends Module {

    @Getter
    private ConnectMessageConfig config;

    private ConnectMessageHandler handler;

    @Override
    public StateResult init() {
        this.config = ConfigHelper.getSerializable(getRootNode(), new ConnectMessageConfig());
        if (config == null) {
            return StateResult.fail();
        }

        handler = new ConnectMessageHandler(this, config);
        getNeutron().getProxy().getEventManager().register(getNeutron(), handler);

        return StateResult.success();
    }

    @Override
    public StateResult reload() {
        return StateResult.of(shutdown(), init());
    }

    @Override
    public StateResult shutdown() {
        getNeutron().getProxy().getEventManager().unregisterListener(getNeutron(), handler);
        return StateResult.success();
    }

    @Override
    public String getName() {
        return "connectmessages";
    }
}
