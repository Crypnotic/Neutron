package me.crypnotic.neutron.manager.user.holder;

import com.velocitypowered.api.command.CommandSource;
import lombok.Data;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.lang.ref.WeakReference;

@ConfigSerializable
@Data
class PlayerData {

    // Persistent data - this is saved to disk.

    @Setting(comment = "The version of this config. Don't change this!")
    private int configVersion = 1;

    @Setting(comment = "The player's last known username.")
    private String username;

    @Setting(comment = "The player's nickname.")
    private String nickname;

    // Non-persisted data - this is not saved when the user is unloaded.

    private WeakReference<CommandSource> replyRecipient = null;

    CommandSource getReplyRecipient() {
        return replyRecipient != null ? replyRecipient.get() : null;
    }

    void setReplyRecipient(CommandSource replyRecipient) {
        this.replyRecipient = new WeakReference<>(replyRecipient);
    }
}
