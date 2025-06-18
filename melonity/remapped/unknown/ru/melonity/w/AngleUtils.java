// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.w;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;
import org.joml.Vector3fc;

@Environment(value = EnvType.CLIENT)
public final class AngleUtils {
    private static final MinecraftClient minecraftClient = MinecraftClient.getInstance();
    private static final int OBFUSCATION_GUARD = 364319250;

    public static Vec2f getRotationAngles(Vec3d source, Vec3d target) {
        Vec3d diff = target.subtract(source);
        double horizontalDistance = Math.hypot(diff.x, diff.z);
        float yaw = (float) (MathHelper.atan2(diff.z, diff.x) * 57.29577951308232) - 90.0f;
        float pitch = (float) (-(MathHelper.atan2(diff.y, horizontalDistance) * 57.29577951308232));
        return new Vec2f(yaw, pitch);
    }

    public static Vec2f getRotationAngles(Entity entity) {
        return getRotationAngles(getEyePosition(minecraftClient.player), getEyePosition(entity));
    }

    public static Vec2f getRotationAngles(Vec3d target) {
        return getRotationAngles(getEyePosition(minecraftClient.player), target);
    }

    public static Vec2f getRotationAngles(Vec3d blockPos, Direction direction) {
        double centerX = blockPos.x + 0.5 + direction.getOffsetX() * 0.5;
        double centerY = blockPos.y + 0.5 + direction.getOffsetY() * 0.5;
        double centerZ = blockPos.z + 0.5 + direction.getOffsetZ() * 0.5;
        return getRotationAngles(new Vec3d(centerX, centerY, centerZ));
    }

    public static float getAngleDifference(float angle1, float angle2) {
        float difference = Math.abs(angle2 - angle1) % 360.0f;
        return difference > 180.0f ? 360.0f - difference : difference;
    }

    public static Vec3d getEyePosition(Entity entity) {
        return entity.getPos().add(0.0, entity.getEyeHeight(entity.getPose()), 0.0);
    }

    public static float[] getAngles(Vec3d target) {
        return getAngles(getEyePosition(minecraftClient.player), target);
    }

    public static float[] getAngles(Entity entity, Vec3d target) {
        return getAngles(getEyePosition(entity), target);
    }

    public static float[] getAngles(Vec3d source, Vec3d target) {
        double diffX = target.x - source.x;
        double diffY = (target.y - source.y) * -1.0;
        double diffZ = target.z - source.z;
        double horizontalDistance = MathHelper.sqrt((float) (diffX * diffX + diffZ * diffZ));
        float yaw = (float) (Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0);
        float pitch = (float) MathHelper.clamp(Math.toDegrees(Math.atan2(diffY, horizontalDistance)), -90.0, 90.0);
        return new float[]{yaw, pitch};
    }

    public static Vector3f getDirectionVector(float yaw, float pitch) {
        float yawRad = (float) Math.toRadians(yaw);
        float pitchRad = (float) Math.toRadians(pitch);
        float x = -MathHelper.sin(pitchRad) * MathHelper.cos(yawRad);
        float y = -MathHelper.sin(yawRad);
        float z = MathHelper.sin(pitchRad) * MathHelper.sin(yawRad);
        return new Vector3f(x, y, z);
    }

    public static float getAngleBetween(float yaw1, float pitch1, float yaw2, float pitch2) {
        Vector3f vec1 = getDirectionVector(yaw1, pitch1);
        Vector3f vec2 = getDirectionVector(yaw2, pitch2);
        float dotProduct = vec1.dot(vec2);
        dotProduct = MathHelper.clamp(dotProduct, -1.0f, 1.0f);
        float angleRad = (float) Math.acos(dotProduct);
        return (float) Math.toDegrees(angleRad);
    }

    private AngleUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}