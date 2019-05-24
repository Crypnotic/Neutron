package me.crypnotic.neutron.module.connectmessage;

import lombok.Getter;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class ConnectMessageConfig {

    @Getter
    @Setting("allow-silent-join-quit")
    private boolean allowSilentJoinQuit = false;

}
