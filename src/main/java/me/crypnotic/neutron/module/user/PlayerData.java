package me.crypnotic.neutron.module.user;

import lombok.Data;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
@Data
class PlayerData {

    // Persistent data

    @Setting(comment = "The player's last known username.")
    private String username;

    // Non-persisted data

}
