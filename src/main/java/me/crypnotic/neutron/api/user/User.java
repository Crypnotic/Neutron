package me.crypnotic.neutron.api.user;

import java.util.Optional;
import java.util.UUID;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;

public interface User<T extends CommandSource> {

    /**
     * Load the user's data from disk.
     *
     * @throws Exception if the data cannot be loaded.
     */
    void load() throws Exception;

    /**
     * Save the user's data to disk, if applicable.
     *
     * @throws Exception if the data cannot be saved.
     */
    void save() throws Exception;

    /**
     * Get the base CommandSource this user is associated with.
     *
     * @return the base CommandSource
     */
    Optional<T> getBase();

    /**
     * Get the display name to refer to the user. This may be their username or nickname.
     * The console's display name is set in the config.
     *
     * @return the user's username or nickname
     */
    String getName();

    /**
     * Get the nickname the user has set, if any.
     *
     * @return the nickname set, if any
     */
    String getNickname();

    /**
     * Get the CommandSource that the user will send a private message to when using /reply.
     *
     * @return the relevant CommandSource
     */
    CommandSource getReplyRecipient();

    /**
     * Get the user's username. This is undefined for non-player users.
     *
     * @return the username, if applicable
     */
    String getUsername();

    /**
     * Get the user's UUID.
     *
     * @return the user's UUID
     */
    Optional<UUID> getUUID();

    /**
     * Set the user's nickname.
     * This is ignored for the console user.
     *
     * @param nickname The nickname of the user.
     */
    void setNickname(String nickname);

    /**
     * Set the CommandSource that the user will send a private message to when using /reply.
     *
     * @param source the CommandSource to reply to
     */
    void setReplyRecipient(CommandSource source);

    /**
     * Determine whether the user is a player or not.
     *
     * @return whether or not the user is a player
     */
    default boolean isPlayer() {
        return getBase().isPresent() && getBase().get() instanceof Player;
    }
}
