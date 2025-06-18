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

@Environment(value = EnvType.CLIENT)
public final class EntityTargetingHelper {

    public static Vec3d getDirectionVector(float pitch, float yaw) {
        float pitchRad = -pitch * ((float) Math.PI / 180);
        float yawRad = -yaw * ((float) Math.PI / 180) - (float) Math.PI;
        float cosPitch = MathHelper.cos(pitchRad);
        float sinYaw = MathHelper.sin(yawRad);
        float cosYaw = MathHelper.cos(yawRad);
        return new Vec3d(sinYaw * cosPitch, MathHelper.sin(pitchRad), cosYaw * cosPitch);
    }

    public static Entity getTargetedEntity(Entity sourceEntity, float pitch, float yaw, double maxDistance, boolean checkBlocks) {
        MinecraftClient client = MinecraftClient.getInstance();
        Entity clientPlayer = client.player;
        if (clientPlayer == null) {
            return null;
        }
        if (client.world == null) {
            return null;
        }
        Vec3d playerPos = clientPlayer.getCameraPosVec(1.0f);
        Vec3d direction = getDirectionVector(yaw, pitch);
        Vec3d endPos = playerPos.add(direction.multiply(maxDistance));
        Box searchBox = sourceEntity.getBoundingBox().expand(direction.multiply(maxDistance)).expand(1.0);
        EntityHitResult entityHitResult = raycastEntities(sourceEntity, playerPos, endPos, searchBox, entity -> !entity.isSpectator(), maxDistance);
        if (maxDistance > 3.0 && entityHitResult != null) {
            double playerToEntitySq = playerPos.squaredDistanceTo(entityHitResult.getPos());
            if (playerToEntitySq > maxDistance * maxDistance) {
                Vec3i endPosInt = new Vec3i((int) endPos.x, (int) endPos.y, (int) endPos.z);
                BlockPos blockPos = new BlockPos(endPosInt);
                BlockHitResult blockHitResult = BlockHitResult.createMissed(endPos, null, blockPos);
                if (blockHitResult != null) {
                    entityHitResult = null;
                }
            }
        }
        if (entityHitResult == null) {
            return null;
        }
        try {
            return entityHitResult.getEntity();
        } catch (ClassCastException e) {
            return null;
        }
    }

    public static EntityHitResult raycastEntities(Entity sourceEntity, Vec3d startPos, Vec3d endPos, Box searchBox, Predicate<Entity> predicate, double maxDistance) {
        World world = sourceEntity.getWorld();
        if (world == null) {
            return null;
        }
        double closestDistance = maxDistance;
        Entity closestEntity = null;
        Vec3d closestHitPos = null;
        List<Entity> entities = world.getOtherEntities(sourceEntity, searchBox, predicate);
        Iterator<Entity> iterator = entities.iterator();
        while (iterator.hasNext()) {
            Entity entity = iterator.next();
            Box entityBox = entity.getBoundingBox();
            float entityRadius = entity.getTargetingMargin();
            Box expandedBox = entityBox.expand(entityRadius);
            Optional<Vec3d> optional = expandedBox.raycast(startPos, endPos);
            boolean intersects = expandedBox.contains(startPos);
            if (intersects) {
                if (closestDistance >= 0.0) {
                    closestEntity = entity;
                    closestHitPos = optional.orElse(startPos);
                    closestDistance = 0.0;
                }
                continue;
            }
            if (!optional.isPresent()) {
                continue;
            }
            Vec3d hitPos = optional.get();
            double distanceSq = startPos.squaredDistanceTo(hitPos);
            if (distanceSq >= closestDistance && closestDistance != 0.0) {
                continue;
            }
            boolean isPassenger = entity.getRootVehicle() == sourceEntity.getRootVehicle();
            if (isPassenger) {
                if (closestDistance == 0.0) {
                    closestEntity = entity;
                    closestHitPos = hitPos;
                }
                continue;
            }
            closestEntity = entity;
            closestHitPos = hitPos;
            closestDistance = distanceSq;
        }
        if (closestEntity == null) {
            return null;
        }
        return new EntityHitResult(closestEntity, closestHitPos);
    }

    private EntityTargetingHelper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}