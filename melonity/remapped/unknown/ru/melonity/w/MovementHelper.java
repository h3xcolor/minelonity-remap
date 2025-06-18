// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.w;

import lombok.Generated;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

@Environment(EnvType.CLIENT)
public final class MovementHelper {
    private static final MinecraftClient mc = MinecraftClient.getInstance();
    
    public static boolean isMoving() {
        return mc.player.input.sidewaysMovement != 0.0f || mc.player.input.forwardMovement != 0.0f;
    }

    public static boolean isNotColliding(float offset) {
        Vec3d playerPos = mc.player.getPos();
        if (playerPos.y < 0.0) {
            return false;
        }
        Box playerBox = mc.player.getBoundingBox();
        Box offsetBox = playerBox.offset(0.0, -offset, 0.0);
        Box expandedBox = offsetBox;
        Iterable<Entity> entities = mc.world.getOtherEntities(mc.player, expandedBox);
        Iterator<Entity> iterator = entities.iterator();
        return !iterator.hasNext();
    }

    public static double getHorizontalVelocity() {
        return Math.hypot(mc.player.getVelocity().x, mc.player.getVelocity().z);
    }

    public static void strafe(double speed) {
        if (!isMoving()) {
            return;
        }
        double movementDirection = getMovementDirection(true);
        mc.player.setVelocity(-Math.sin(movementDirection) * speed, mc.player.getVelocity().y, Math.cos(movementDirection) * speed);
    }

    public static double getMovementDirection(boolean convertToRadians) {
        float yaw = mc.player.getYaw();
        float forward = mc.player.input.forwardMovement;
        float sideways = mc.player.input.sidewaysMovement;
        float adjustedYaw = yaw;
        if (mc.player.input.sidewaysMovement < 0.0f) {
            adjustedYaw += 180.0f;
        }
        float multiplier = 1.0f;
        if (mc.player.input.sidewaysMovement < 0.0f) {
            multiplier = -0.5f;
        } else if (mc.player.input.sidewaysMovement > 0.0f) {
            multiplier = 0.5f;
        }
        if (mc.player.input.forwardMovement > 0.0f) {
            adjustedYaw -= 90.0f * multiplier;
        }
        if (mc.player.input.forwardMovement < 0.0f) {
            adjustedYaw += 90.0f * multiplier;
        }
        return convertToRadians ? Math.toRadians(adjustedYaw) : adjustedYaw;
    }

    public static double calculateDirection(float yaw, double forward, double sideways) {
        if (forward < 0.0) {
            yaw += 180.0f;
        }
        float multiplier = 1.0f;
        if (forward < 0.0) {
            multiplier = -0.5f;
        } else if (forward > 0.0) {
            multiplier = 0.5f;
        }
        if (sideways > 0.0) {
            yaw -= 90.0f * multiplier;
        }
        if (sideways < 0.0) {
            yaw += 90.0f * multiplier;
        }
        return Math.toRadians(yaw);
    }

    public static void updateMovementInput(MovementInput movementInput, float speed) {
        float forward = movementInput.getForward();
        float sideways = movementInput.getSideways();
        float playerYaw = mc.player.getYaw();
        double movementDirection = calculateDirection(playerYaw, forward, sideways);
        double directionDegrees = Math.toDegrees(movementDirection);
        double normalizedDirection = MathHelper.wrapDegrees(directionDegrees);
        if (forward == 0.0f && sideways == 0.0f) {
            return;
        }
        float bestForward = 0.0f;
        float bestSideways = 0.0f;
        float bestDifference = Float.MAX_VALUE;
        for (float f = -1.0f; f <= 1.0f; f += 1.0f) {
            for (float s = -1.0f; s <= 1.0f; s += 1.0f) {
                if (s == 0.0f && f == 0.0f) continue;
                double dir = calculateDirection(speed, f, s);
                double dirDeg = Math.toDegrees(dir);
                double normalizedDirDeg = MathHelper.wrapDegrees(dirDeg);
                double difference = Math.abs(normalizedDirection - normalizedDirDeg);
                if (difference < bestDifference) {
                    bestDifference = (float) difference;
                    bestForward = f;
                    bestSideways = s;
                }
            }
        }
        movementInput.setForward(bestForward);
        movementInput.setSideways(bestSideways);
    }

    public static double[] getMovementVector(double speed) {
        float forward = mc.player.input.movementForward;
        float sideways = mc.player.input.movementSideways;
        float yaw = mc.player.getYaw();
        if (forward != 0.0f) {
            if (sideways > 0.0f) {
                yaw += (forward > 0.0f ? -45 : 45);
            } else if (sideways < 0.0f) {
                yaw += (forward > 0.0f ? 45 : -45);
            }
            sideways = 0.0f;
            if (forward > 0.0f) {
                forward = 1.0f;
            } else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }
        double sin = Math.sin(Math.toRadians(yaw + 90.0f));
        double cos = Math.cos(Math.toRadians(yaw + 90.0f));
        double moveX = (double) forward * speed * cos + (double) sideways * speed * sin;
        double moveZ = (double) forward * speed * sin - (double) sideways * speed * cos;
        return new double[]{moveX, moveZ};
    }

    @Generated
    private MovementHelper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

interface MovementInput {
    float getForward();
    float getSideways();
    void setForward(float forward);
    void setSideways(float sideways);
}