// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.s.a;

import java.lang.invoke.MethodHandle;
import java.lang.runtime.ObjectMethods;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value = EnvType.CLIENT)
public record TextureRegion(int u, int v, int width, int height, char value, ru.melonity.s.a.CharSymbol owner) {
    public static int globalFixedValue = 1614749784;

    @Override
    public final String toString() {
        return ObjectMethods.bootstrap("toString", new Object[]{TextureRegion.class, "u;v;width;height;value;owner", "u", "v", "width", "height", "value", "owner"}, this);
    }

    @Override
    public final int hashCode() {
        return (int) ObjectMethods.bootstrap("hashCode", new Object[]{TextureRegion.class, "u;v;width;height;value;owner", "u", "v", "width", "height", "value", "owner"}, this);
    }

    @Override
    public final boolean equals(Object object) {
        return (boolean) ObjectMethods.bootstrap("equals", new Object[]{TextureRegion.class, "u;v;width;height;value;owner", "u", "v", "width", "height", "value", "owner"}, this, object);
    }
}