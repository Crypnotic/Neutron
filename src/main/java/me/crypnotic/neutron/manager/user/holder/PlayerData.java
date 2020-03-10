/*
* This file is part of Neutron, licensed under the MIT License
*
* Copyright (c) 2020 Crypnotic <crypnoticofficial@gmail.com>
* Copyright (c) 2020 Contributors
*
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in all
* copies or substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
* SOFTWARE.
*/
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
