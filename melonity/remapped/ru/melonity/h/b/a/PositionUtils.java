// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.b.a;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import ru.melonity.h.b.a.IIlIlllIIllIllIlllIIlIlIllIIlIIllIIllIIIllIlIIlllIIIllIIllIIlllIlllIIllIlllIllIIIlIIllIIllIIlllIIIllIIIlIIllIllIIlIIlIIlllIlIIlIllIlllIIlllIIIlIIIllIIllIIlIIIllIlllIIlIlIlllIIlIIllIIlIllIIlIIlllIIIlllIIlllIIlllIllIIlIllIIIlIIll;
import ru.melonity.h.b.a.IIlllIlIIllIlIIlIlllIlIlIIIlIIIlllIllIlIIllIlllIlIIllIllIIllIIlllIlllIIllIIllIIlIllIlIIIlIlllIIlIIIllIlllIllIllIllIIllIllIlIIlIllIIllIIIllIllIIlllIIlIllIIlIIIllIIIllIlIIIlIIlllIIIlIIllIIllIlIllIIllIIIllIlllIllIIl;
import ru.melonity.w.IIllIllIlIlllIIlllIIlllIlIIIllIIlIIllIIllIIlIIIlllIIIlIIlllIIlllIIIlIlIIlIIllIlIIIllIIlIIllIIlIIlIIlllIIIllIlllIIlIIlllIllIllIIlllIlllIIlllIlIIIllIIllIlIlIIIllIlIIIlIIIllIIllIIlIlllIllIllIIIlIIIlIIllIIllIlIIIlIIIlIIllIIIllIIlll;

@Environment(value = EnvType.CLIENT)
public class PositionUtils {
    private static final MinecraftClient client = MinecraftClient.getInstance();

    public static Vec3d calculatePositionSimple(LivingEntity entity, float tickDelta) {
        double playerEyeY = client.player.getEyeY();
        double entityY = entity.getY();
        double yDifference = playerEyeY - entityY;
        float standingEyeHeight = entity.getStandingEyeHeight();
        double halfHeight = standingEyeHeight / 2.0f;
        float entityHeight = entity.getHeight();
        double heightDifference = MathHelper.clamp(yDifference, halfHeight, entityHeight);
        boolean playerNotBlind = !client.player.isBlind();
        boolean playerNotSpectator = client.player.isSpectator();
        boolean entitySneaking = entity.isSneaking();
        float heightFactor = 1.0f;
        if (playerNotBlind && playerNotSpectator) {
            heightFactor = entitySneaking ? 0.8f : 0.6f;
        }
        double verticalOffset = heightDifference / (double) heightFactor;
        return entity.getPos().add(0.0, verticalOffset, 0.0).add(tickDelta, tickDelta / 2.0f, tickDelta).subtract(client.player.getVelocity());
    }

    public static Vec3d calculatePositionWithInterpolation(LivingEntity entity, boolean useCollision, float tickDelta) {
        Vec3d interpolatedPosition = calculatePositionToTarget(entity, true);
        Vec3d currentPosition = entity.getPos();
        Vec3d lastPosition = entity.prevPos;
        Vec3d motion = entity.getVelocity();
        float randomFactor = IIllIllIlIlllIIlllIIlllIlIIIllIIlIIllIIllIIlIIIlllIIIlIIlllIIlllIIIlIlIIlIIllIlIIIllIIlIIllIIlIIlIIlllIIIllIlllIIlIIlllIllIllIIlllIlllIIlllIlIIIllIIllIlIlIIIllIlIIIlIIIllIIllIIlIlllIllIllIIIlIIIlIIllIIllIlIIIlIIIlIIllIIIllIIlll.getRandomFactor(0.8, 1.2);
        Vec3d adjustedMotion = motion.multiply((double) randomFactor);
        Vec3d basePosition = lastPosition.add(adjustedMotion);
        randomFactor = IIllIllIlIlllIIlllIIlllIlIIIllIIlIIllIIllIIlIIIlllIIIlIIlllIIlllIIIlIlIIlIIllIlIIIllIIlIIllIIlIIlIIlllIIIllIlllIIlIIlllIllIllIIlllIlllIIlllIlIIIllIIllIlIlIIIllIlIIIlIIIllIIllIIlIlllIllIllIIIlIIIlIIllIIllIlIIIlIIIlIIllIIIllIIlll.getRandomFactor(0.8, 1.2);
        adjustedMotion = new Vec3d(0.0, 0.025 * (double) randomFactor * 2.0, 0.0);
        basePosition = basePosition.add(adjustedMotion);
        playerEyeY = client.player.getEyeY();
        entityY = entity.getY();
        yDifference = playerEyeY - entityY;
        standingEyeHeight = entity.getStandingEyeHeight();
        halfHeight = standingEyeHeight / 2.0f;
        entityHeight = entity.getHeight();
        heightDifference = MathHelper.clamp(yDifference, halfHeight, entityHeight);
        playerNotBlind = !client.player.isBlind();
        playerNotSpectator = client.player.isSpectator();
        entitySneaking = entity.isSneaking();
        heightFactor = 1.0f;
        if (playerNotBlind && playerNotSpectator) {
            heightFactor = entitySneaking ? 0.8f : 0.6f;
        }
        float distanceFactor = client.player.distanceTo(entity);
        if (distanceFactor > 1.0f) {
            baseEyeHeight = entity.getStandingEyeHeight();
            adjustedHeight = baseEyeHeight / 2.0f;
        } else {
            eyeHeightAdjustment = entity.getStandingEyeHeight();
            adjustedHeight = eyeHeightAdjustment - 0.2f;
        }
        entityHeight = entity.getHeight();
        verticalOffset = MathHelper.clamp(heightDifference / (double) heightFactor, adjustedHeight, entityHeight / 1.025f);
        float eyeDistance = client.player.distanceTo(entity);
        Vec3d baseResultPosition = interpolatedPosition != null ? interpolatedPosition : currentPosition;
        Vec3d targetPosition = entity.getPos().add(0.0, verticalOffset, 0.0);
        baseResultPosition = targetPosition;
        if (useCollision) {
            targetPosition = entity.getPos().add(0.0, verticalOffset, 0.0);
            baseResultPosition = targetPosition;
        }
        if (eyeDistance < 2.0f) {
            targetPosition = entity.getPos().add(0.0, verticalOffset, 0.0);
            baseResultPosition = targetPosition;
        }
        return baseResultPosition.add(tickDelta, tickDelta / 2.0f, tickDelta).subtract(client.player.getVelocity());
    }

    public static Vec3d calculatePositionAdjustment(LivingEntity entity) {
        double playerEyeY = client.player.getEyeY();
        double entityY = entity.getY();
        double yDifference = playerEyeY - entityY;
        float standingEyeHeight = entity.getStandingEyeHeight();
        double halfHeight = standingEyeHeight / 2.0f;
        float entityHeight = entity.getHeight();
        double heightDifference = MathHelper.clamp(yDifference, halfHeight, entityHeight);
        boolean playerNotBlind = !client.player.isBlind();
        boolean playerNotSpectator = client.player.isSpectator();
        boolean entitySneaking = entity.isSneaking();
        float heightFactor = 1.0f;
        if (playerNotBlind && playerNotSpectator) {
            heightFactor = entitySneaking ? 0.8f : 0.6f;
        }
        float distanceFactor = client.player.distanceTo(entity);
        if (distanceFactor > 1.0f) {
            baseFillHeight = entity.getStandingEyeHeight();
            adjustedHeight = baseFillHeight / 2.0f;
        } else {
            sneakingHeightOffset = entity.getStandingEyeHeight();
            adjustedHeight = sneakingHeightOffset - 0.2f;
        }
        double clampedHeight = MathHelper.clamp(heightDifference / (double) heightFactor, adjustedHeight, entity.getHeight() / 1.025f);
        double playerX = player.getX();
        double entityX = entity.getX();
        double xDifference = playerX - entityX;
        double playerZ = client.player.getZ();
        double entityZ = entity.getZ();
        double zDifference = playerZ - entityZ;
        double clampedXOffset = MathHelper.clamp(xDifference, -entity.getWidth() / 3.0f, entity.getWidth() / 3.0f);
        double clampedZOffset = MathHelper.clamp(zDifference, -entity.getWidth() / 3.0f, entity.getWidth() / 3.0f);
        return new Vec3d(entityX - playerX + clampedXOffset, entityY - player.getY() + clampedHeight, entityZ - playerZ + clampedZOffset);
    }

    public static boolean isPlayerAboveAir() {
        for (double xOffset = -0.31; xOffset <= 0.31; xOffset += 0.31) {
            for (double zOffset = -0.31; zOffset <= 0.31; zOffset += 0.31) {
                PlayerEntity player = client.player;
                Vec3d rotationVector = player.getRotationVector();
                float stepHeight = player.getStepHeight();
                for (double yOffset = (double) stepHeight; yOffset >= 0.0; yOffset -= 0.1) {
                    double playerX = player.getX();
                    double xPos = playerX + xOffset;
                    double playerY = player.getY();
                    double yPos = playerY + yOffset;
                    double playerZ = player.getZ();
                    double zPos = playerZ + zOffset;
                    BlockPos blockPos = new BlockPos(xPos, yPos, zPos);
                    BlockState blockState = client.world.getBlockState(blockPos);
                    if (blockState.getBlock() == Blocks.AIR) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static Vec3d interpolateEntityPosition(LivingEntity entity, boolean useCollision) {
        if (entity == null) {
            return Vec3d.ZERO;
        }
        return interpolatePosition(client.player.getRotationClient().applyContinuousRotation(), entity, useCollision);
    }

    public static Vec3d interpolatePosition(Vec3d cameraPosition, LivingEntity entity, boolean useCollision) {
        if (entity == null) {
            return Vec3d.ZERO;
        }
        double boxMargin = 0.0;
        double xComponent = cameraPosition.x;
        Box entityBounds = entity.getBoundingBox();
        double minX = entityBounds.minX + boxMargin;
        double maxX = entityBounds.maxX - boxMargin;
        double clampedX = MathHelper.clamp(xComponent, minX, maxX);
        double yComponent = cameraPosition.y;
        double minY = entityBounds.minY + boxMargin;
        double maxY = entityBounds.maxY - boxMargin;
        double clampedY = MathHelper.clamp(yComponent, minY, maxY);
        double zComponent = cameraPosition.z;
        double minZ = entityBounds.minZ + boxMargin;
        double maxZ = entityBounds.maxZ - boxMargin;
        double clampedZ = MathHelper.clamp(zComponent, minZ, maxZ);
        Vec3d interpolatedPosition = new Vec3d(clampedX, clampedY, clampedZ);
        if (!useCollision) {
            boolean collisionAtPosition = IIlllIlIIllIlIIlIlllIlIlIIIlIIIlllIllIlIIllIlllIlIIllIllIIllIIlllIlllIIllIIllIIlIllIlIIIlIlllIIlIIIllIlllIllIllIllIIllIllIlIIlIllIIllIIIllIllIIlllIIlIllIIlIIIllIIIllIlIIIlIIlllIIIlIIllIIllIlIllIIllIIIllIlllIllIIl.isCollidingAtPosition(interpolatedPosition);
            if (!collisionAtPosition) {
                IIlIlllIIllIllIlllIIlIlIllIIlIIllIIllIIIllIlIIlllIIIllIIllIIlllIlllIIllIlllIllIIIlIIllIIllIIlllIIIllIIIlIIllIllIIlIIlIIlllIlIIlIllIlllIIlllIIIlIIIllIIllIIlIIIllIlllIIlIlIlllIIlIIllIIlIllIIlIIlllIIIlllIIlllIIlllIllIIlIllIIIlIIll.updateEntityPosition(entity);
                Vec3d adjustedPosition = IIlIlllIIllIllIlllIIlIlIllIIlIIllIIllIIIllIlIIlllIIIllIIllIIlllIlllIIllIlllIllIIIlIIllIIllIIlllIIIllIIIlIIllIllIIlIIlIIlllIlIIlIllIlllIIlllIIIlIIIllIIllIIlIIIllIlllIIlIlIlllIIlIIllIIlIllIIlIIlllIIIlllIIlllIIlllIllIIlIllIIIlIIll.calculatePositionForEntity(entity);
                if (adjustedPosition != null) {
                    return adjustedPosition;
                }
            }
        }
        return interpolatedPosition;
    }

    public static double calculateDistance(Vec3d position) {
        PlayerEntity player = client.player;
        Vec3d playerPosition = player.getPos().add(0.0, (double) (player.getStandingEyeHeight() / 2.0f), 0.0);
        return playerPosition.distanceTo(position);
    }
}