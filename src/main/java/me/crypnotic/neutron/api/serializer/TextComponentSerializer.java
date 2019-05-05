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
package me.crypnotic.neutron.api.serializer;

import com.google.common.reflect.TypeToken;

import me.crypnotic.neutron.util.StringHelper;
import net.kyori.text.TextComponent;
import net.kyori.text.serializer.ComponentSerializers;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;

public class TextComponentSerializer implements TypeSerializer<TextComponent> {

    @Override
    public TextComponent deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {
        if (!value.isVirtual()) {
            return StringHelper.color(value.getString());
        }
        return null;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void serialize(TypeToken<?> type, TextComponent obj, ConfigurationNode value) throws ObjectMappingException {
        if (obj != null) {
            value.setValue(ComponentSerializers.LEGACY.serialize(obj, '&'));
        }
    }
}
