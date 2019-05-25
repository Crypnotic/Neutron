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
package me.crypnotic.neutron.api;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StateResult {

    private ExecutorService executor = Executors.newFixedThreadPool(2);

    @Getter
    private final boolean success;
    @Getter
    private boolean async;

    public void async() {
        this.async = true;
    }

    private void run(Runnable runnable) {
        if (async) {
            executor.execute(runnable);
        } else {
            runnable.run();
        }
    }

    public void success(Runnable runnable) {
        run(runnable);
    }

    public void fail(Runnable runnable) {
        run(runnable);
    }

    public void success(String message) {
        Neutron.getNeutron().getLogger().info(message);
    }

    public void fail(String message) {
        Neutron.getNeutron().getLogger().warn(message);
    }
    
    public static StateResult success() {
        return new StateResult(true);
    }
    
    public static StateResult fail() {
        return new StateResult(false);
    }
    
    public static StateResult of(boolean value) {
        return new StateResult(value);
    }
    
    public static StateResult of(StateResult... results) {
        for(StateResult result : results) {
            if (!result.isSuccess()) {
                return result;
            }
        }
        
        return StateResult.success();
    }
}
