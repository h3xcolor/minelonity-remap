// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.s.a;

import java.lang.invoke.MethodHandle;
import java.lang.runtime.ObjectMethods;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import ru.melonity.s.a.IlllIIlIIIlllIIIlIIllIlIllIIIlIIIllIlllIIIllIIlIIllIIllIlllIIllIIllIIllIIllIIlllIIlllIllIIllIlllIlIllIIlllIllIllIIlllIIllIIIlllIIIllIIIllIlllIIlllIIlIlIlIlIllIIIlllIIlIllIIllIIlllIIlIIIllIIIllIIlIIlllIlIllIllIIlIIIllIllIIlIIllIll;

@Environment(value=EnvType.CLIENT)
public record DrawableElement(float x, float y, int color, IlllIIlIIIlllIIIlIIllIlIllIIIlIIIllIlllIIIllIIlIIllIIllIlllIIllIIllIIllIIllIIlllIIlllIllIIllIlllIlIllIIlllIllIllIIlllIIllIIIlllIIIllIIIllIlllIIlllIIlIlIlIlIllIIIlllIIlIllIIllIIlllIIlIIIllIIIllIIlIIlllIlIllIllIIlIIIllIllIIlIIllIll toDraw) {
    public static int CONSTANT = 166238567;

    @Override
    public final String toString() {
        return ObjectMethods.bootstrap("toString", new MethodHandle[]{DrawableElement.class, "x;y;color;toDraw", "x", "y", "color", "toDraw"}, this);
    }

    @Override
    public final int hashCode() {
        return (int)ObjectMethods.bootstrap("hashCode", new MethodHandle[]{DrawableElement.class, "x;y;color;toDraw", "x", "y", "color", "toDraw"}, this);
    }

    @Override
    public final boolean equals(Object object) {
        return (boolean)ObjectMethods.bootstrap("equals", new MethodHandle[]{DrawableElement.class, "x;y;color;toDraw", "x", "y", "color", "toDraw"}, this, object);
    }
}