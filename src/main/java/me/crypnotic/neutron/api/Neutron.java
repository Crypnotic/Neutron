package me.crypnotic.neutron.api;

import me.crypnotic.neutron.NeutronPlugin;

public class Neutron {

    private static Object LOCK = new Object();
    private static NeutronPlugin instance;

    public static final NeutronPlugin getNeutron() {
        return instance;
    }

    public static final void setNeutron(NeutronPlugin plugin) {
        if (plugin == null) {
            throw new IllegalStateException("NeutronPlugin instance cannot be null");
        }

        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    instance = plugin;

                    return;
                }
            }
        }

        throw new IllegalStateException("NeutronPlugin instance cannot be redefined");
    }
}
