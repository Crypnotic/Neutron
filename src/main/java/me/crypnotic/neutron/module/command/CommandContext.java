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
package me.crypnotic.neutron.module.command;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CommandContext {

    private final String[] arguments;

    public String get(int index) {
        if (index >= size()) {
            throw new IllegalArgumentException("Index: " + index + " > Length: " + size());
        }
        return arguments[index];
    }

    public Integer getInteger(int index) {
        try {
            return Integer.valueOf(get(index));
        } catch (NumberFormatException exception) {
            return -1;
        }
    }

    public String join(String delimeter) {
        return String.join(" ", arguments);
    }

    public String join(String delimeter, int start) {
        return join(delimeter, start, size());
    }

    public String join(String delimeter, int start, int end) {
        StringBuilder builder = new StringBuilder();
        for (int i = start; i < end; i++) {
            builder.append(get(i));
            if (i < end) {
                builder.append(delimeter);
            }
        }
        return builder.toString();
    }

    public int size() {
        return arguments.length;
    }
}
