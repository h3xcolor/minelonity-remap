// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.f.b;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.math.BlockPos;
import ru.melonity.f.IIllIllIIllIlIIlIlIIlllIllIIllIIllIlIIllIIIllIIIllIIllIIIlIllIIlIIlllIllIIlIllIllIIlIIlIlIIIlIllIIllIlllIllIIIllIIlIIlllIllIIlIIIlIIllIIllIIlIlIIlIllIIlllIlIIlllIIlIlIIlIIlIlllIIlIIIlIIllIllIlllIIlllIIIlIIllIIIlll;

@Environment(value = EnvType.CLIENT)
public record ClientBlockPosition(BlockPos position) implements IIllIllIIllIlIIlIlIIlllIllIIllIIllIlIIllIIIllIIIllIIllIIIlIllIIlIIlllIllIIlIllIllIIlIIlIlIIIlIllIIllIlllIllIIIllIIlIIlllIllIIlIIIlIIllIIllIIlIlIIlIllIIlllIlIIlllIIlIlIIlIIlIlllIIlIIIlIIllIllIlllIIlllIIIlIIllIIIlll {
    public static int HASH_CONSTANT = 670154878;

    @Override
    public final String toString() {
        return ObjectMethods.bootstrap("toString", new MethodHandle[]{ClientBlockPosition.class, "position", "position"}, this);
    }

    @Override
    public final int hashCode() {
        return (int) ObjectMethods.bootstrap("hashCode", new MethodHandle[]{ClientBlockPosition.class, "position", "position"}, this);
    }

    @Override
    public final boolean equals(Object object) {
        return (boolean) ObjectMethods.bootstrap("equals", new MethodHandle[]{ClientBlockPosition.class, "position", "position"}, this, object);
    }
}