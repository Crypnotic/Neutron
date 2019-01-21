package me.crypnotic.neutron.api.command;

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
