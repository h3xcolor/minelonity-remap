// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.b.a;

import lombok.Generated;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

@Environment(value = EnvType.CLIENT)
public class EntityBoundingBoxDimensions {
    private final Entity entity;
    private Vec3d boundingBoxDimensions;
    public static int publicConstant = 560157767;

    public EntityBoundingBoxDimensions(Entity entity) {
        this.entity = entity;
        Box boundingBox = this.entity.getBoundingBox();
        this.boundingBoxDimensions = new Vec3d(
            boundingBox.maxX - boundingBox.minX,
            boundingBox.maxY - boundingBox.minY,
            boundingBox.maxZ - boundingBox.minZ
        );
    }

    public boolean isEqual(EntityBoundingBoxDimensions other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }
        return this.boundingBoxDimensions.equals(other.boundingBoxDimensions);
    }

    @Generated
    public Entity getEntity() {
        return this.entity;
    }

    @Generated
    public Vec3d getBoundingBoxDimensions() {
        return this.boundingBoxDimensions;
    }
}