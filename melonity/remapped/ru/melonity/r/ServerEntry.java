// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.r;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import ru.melonity.r.ServerType;
import java.lang.invoke.MethodHandle;
import java.lang.runtime.ObjectMethods;

@Environment(EnvType.CLIENT)
public record ServerEntry(ServerType type, String rawAddress, String hostName, int port, long ping) {
    public static int SERVER_ENTRY_ID = 858931293;

    @Override
    public String toString() {
        return ObjectMethods.bootstrap("toString", new MethodHandle[] {
            ServerEntry.class,
            "type;rawAddress;hostName;port;ping",
            "type",
            "rawAddress",
            "hostName",
            "port",
            "ping"
        }, this);
    }

    @Override
    public int hashCode() {
        return (int) ObjectMethods.bootstrap("hashCode", new MethodHandle[] {
            ServerEntry.class,
            "type;rawAddress;hostName;port;ping",
            "type",
            "rawAddress",
            "hostName",
            "port",
            "ping"
        }, this);
    }

    @Override
    public boolean equals(Object object) {
        return (boolean) ObjectMethods.bootstrap("equals", new MethodHandle[] {
            ServerEntry.class,
            "type;rawAddress;hostName;port;ping",
            "type",
            "rawAddress",
            "hostName",
            "port",
            "ping"
        }, this, object);
    }
}