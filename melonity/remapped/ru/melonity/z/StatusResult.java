// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.z;

import java.lang.invoke.MethodHandle;
import java.lang.runtime.ObjectMethods;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value = EnvType.CLIENT)
public record StatusResult(boolean success, String message) {
    @Override
    public final String toString() {
        return ObjectMethods.bootstrap("toString", new MethodHandle[]{StatusResult.class, "success;message", "success", "message"}, this);
    }

    @Override
    public final int hashCode() {
        return (int) ObjectMethods.bootstrap("hashCode", new MethodHandle[]{StatusResult.class, "success;message", "success", "message"}, this);
    }

    @Override
    public final boolean equals(Object object) {
        return (boolean) ObjectMethods.bootstrap("equals", new MethodHandle[]{StatusResult.class, "success;message", "success", "message"}, this, object);
    }
}