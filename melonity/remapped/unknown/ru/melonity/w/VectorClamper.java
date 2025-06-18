// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.w;

import lombok.Generated;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

@Environment(value = EnvType.CLIENT)
public final class VectorClamper {

    public static Vec3d clampVectorToBoundingBox(Vec3d vector, Box boundingBox) {
        return new Vec3d(
            MathHelper.clamp(vector.x, boundingBox.minX, boundingBox.maxX),
            MathHelper.clamp(vector.y, boundingBox.minY, boundingBox.maxY),
            MathHelper.clamp(vector.z, boundingBox.minZ, boundingBox.maxZ)
        );
    }

    public static Vec3d clampVectorToEntityBoundingBox(Vec3d vector, Entity entity) {
        return clampVectorToBoundingBox(vector, entity.getBoundingBox());
    }

    @Generated
    private VectorClamper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}