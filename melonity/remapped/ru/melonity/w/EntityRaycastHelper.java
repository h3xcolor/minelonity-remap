// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.w;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Environment(EnvType.CLIENT)
public class EntityRaycastHelper {
    private static final MinecraftClient client = MinecraftClient.getInstance();

    public static Entity raycast(Entity entity, float pitch, float yaw, double maxDistance) {
        Entity player = client.player;
        if (player == null || client.world == null) {
            return null;
        }
        Vec3d playerPos = player.getCameraPosVec(1.0f);
        Vec3d rotationVec = getRotationVector(pitch, yaw);
        Vec3d rayEnd = playerPos.add(rotationVec.x * maxDistance, rotationVec.y * maxDistance, rotationVec.z * maxDistance);
        Box entityBox = entity.getBoundingBox();
        float entityRadius = entity.getTargetingMargin();
        Box expandedBox = entityBox.expand(entityRadius);
        EntityHitResult entityHit = raycastEntities(player, playerPos, rayEnd, expandedBox, e -> !e.isSpectator(), maxDistance);
        if (entityHit != null) {
            Entity hitEntity = entityHit.getEntity();
            double distanceSq = playerPos.squaredDistanceTo(entityHit.getPos());
            if (distanceSq > maxDistance * maxDistance) {
                Vec3i rayDir = new Vec3i(rotationVec.x, rotationVec.y, rotationVec.z);
                BlockPos blockPos = new BlockPos(rayDir);
                BlockHitResult blockHit = BlockHitResult.createMissed(rayEnd, null, blockPos);
                if (blockHit.getType() != null) {
                    return null;
                }
            }
            return hitEntity;
        }
        return null;
    }

    private static EntityHitResult raycastEntities(Entity sourceEntity, Vec3d startPos, Vec3d endPos, Box box, Predicate<Entity> predicate, double maxDistance) {
        World world = sourceEntity.getWorld();
        double currentMinDistance = maxDistance;
        Entity closestEntity = null;
        Vec3d closestHitPos = null;
        List<Entity> entities = world.getOtherEntities(sourceEntity, box, predicate);
        Iterator<Entity> iterator = entities.iterator();
        while (iterator.hasNext()) {
            Entity entity = iterator.next();
            Box entityBox = entity.getBoundingBox();
            float margin = entity.getTargetingMargin();
            Box expandedEntityBox = entityBox.expand(margin);
            Optional<Vec3d> optional = expandedEntityBox.raycast(startPos, endPos);
            boolean intersects = expandedEntityBox.contains(startPos);
            if (intersects) {
                if (currentMinDistance >= 0.0) {
                    closestEntity = entity;
                    closestHitPos = startPos;
                    currentMinDistance = 0.0;
                }
            } else if (optional.isPresent()) {
                Vec3d hitPos = optional.get();
                double distanceSq = startPos.squaredDistanceTo(hitPos);
                if (distanceSq < currentMinDistance || currentMinDistance == 0.0) {
                    if (entity.getRootVehicle() == sourceEntity.getRootVehicle()) {
                        if (currentMinDistance == 0.0) {
                            closestEntity = entity;
                            closestHitPos = hitPos;
                        }
                    } else {
                        closestEntity = entity;
                        closestHitPos = hitPos;
                        currentMinDistance = distanceSq;
                    }
                }
            }
        }
        if (closestEntity == null) {
            return null;
        }
        return new EntityHitResult(closestEntity, closestHitPos);
    }

    private static Vec3d getRotationVector(float pitch, float yaw) {
        float pitchRad = -pitch * ((float) Math.PI / 180) - (float) Math.PI;
        float yawRad = -yaw * ((float) Math.PI / 180);
        float cosPitch = MathHelper.cos(pitchRad);
        float sinPitch = MathHelper.sin(pitchRad);
        float cosYaw = -MathHelper.cos(yawRad);
        float sinYaw = MathHelper.sin(yawRad);
        return new Vec3d(cosPitch * cosYaw, sinYaw, sinPitch * cosYaw);
    }
}