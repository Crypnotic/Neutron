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

import java.util.Optional;
import java.util.UUID;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ConsoleCommandSource;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.crypnotic.neutron.api.Neutron;
import me.crypnotic.neutron.api.user.User;

// TODO: Consider whether this should support *any* CommandSource that isn't a player (ie plugin-provided CommandSources)
@RequiredArgsConstructor
public class ConsoleUser implements User<ConsoleCommandSource> {

    @Getter
    private final String name;

    @Getter
    @Setter
    private CommandSource replyRecipient;

    @Override
    public Optional<ConsoleCommandSource> getBase() {
        return Optional.of(Neutron.getNeutron().getProxy().getConsoleCommandSource());
    }

    @Override
    public String getNickname() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public void load() throws Exception {
        /* noop */
    }

    @Override
    public void save() throws Exception {
        /* noop */
    }

    @Override
    public Optional<UUID> getUUID() {
        return Optional.empty();
    }

    @Override
    public void setNickname(String nickname) {
        /* noop */
    }
}
