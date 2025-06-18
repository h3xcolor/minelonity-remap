// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.w;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

@Environment(EnvType.CLIENT)
public class EntityPositionHelper {
    public Vec3d getEntityPosition(Entity entity) {
        return new Vec3d(entity.x, entity.y, entity.z);
    }

    public Vec3d getEntityWithMovementDelta(Entity entity) {
        Vec3d currentPosition = getEntityPosition(entity);
        Vec3d velocity = entity.getVelocity();
        double partialTickFactor = MinecraftClient.getInstance().getTickDelta();
        return currentPosition.add(velocity.multiply(partialTickFactor));
    }
}