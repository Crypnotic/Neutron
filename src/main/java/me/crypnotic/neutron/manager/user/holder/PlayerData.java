package me.crypnotic.neutron.manager.user.holder;

import com.velocitypowered.api.command.CommandSource;
import lombok.Data;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.lang.ref.WeakReference;
import java.util.Set;
import java.util.UUID;

@ConfigSerializable
@Data
class PlayerData {

    // Persistent data - this is saved to disk.

    @Setting(comment = "The version of this config. Don't change this!")
    private int configVersion = 1;

    @Setting(comment = "The player's last known username.")
    private String username;

    @Setting(comment = "Players that this player is ignoring")
    private Set<UUID> ignoredPlayers;

    // Non-persisted data - this is not saved when the user is unloaded.

    private WeakReference<CommandSource> replyRecipient = null;

    public CommandSource getReplyRecipient() {
        return replyRecipient != null ? replyRecipient.get() : null;
    }

    public void setReplyRecipient(CommandSource replyRecipient) {
        this.replyRecipient = new WeakReference<>(replyRecipient);
    }
}
