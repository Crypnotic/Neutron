/*
* This file is part of Neutron, licensed under the MIT License
*
* Copyright (c) 2019 Crypnotic <crypnoticofficial@gmail.com>
*
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

        this.handler = new ConnectMessageHandler(this, config);
        
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
