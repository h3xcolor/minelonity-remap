// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.w;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_238;
import net.minecraft.class_241;
import net.minecraft.class_243;
import net.minecraft.class_310;
import net.minecraft.class_3532;
import org.joml.Vector4f;

@Environment(value = EnvType.CLIENT)
public final class EntityPositionUtils {
    private static final class_310 MINECRAFT = class_310.method_1551();

    public static class_243 clampVectorToBoundingBox(class_243 vec, class_238 box) {
        return new class_243(
            class_3532.method_15350(vec.field_1352, box.field_1323, box.field_1320),
            class_3532.method_15350(vec.field_1351, box.field_1322, box.field_1325),
            class_3532.method_15350(vec.field_1350, box.field_1321, box.field_1324)
        );
    }

    public static class_243 clampVectorToEntityBoundingBox(class_243 vec, class_1297 entity) {
        return clampVectorToBoundingBox(vec, entity.method_5829());
    }

    public static class_243 getEntityPositionRelativeToCamera(class_1297 entity) {
        class_243 cameraPos = MINECRAFT.field_1724.method_5836(MINECRAFT.method_60646().method_60637(true));
        return clampVectorToEntityBoundingBox(cameraPos, entity).method_1020(cameraPos);
    }

    public static double getDistanceToEntity(class_1297 entity) {
        return getEntityPositionRelativeToCamera(entity).method_1033();
    }

    public static double getDistanceToEntity(class_1309 entity) {
        return getDistanceToEntity((class_1297) entity);
    }

    public static class_243 findClosestPointOnEntityBoundingBox(class_1297 entity) {
        return findClosestPointOnEntityBoundingBox(
            MINECRAFT.field_1724.method_5836(MINECRAFT.method_60646().method_60637(true)),
            entity,
            Math.min(entity.method_17681(), entity.method_17682()) / 4.0f
        );
    }

    public static class_243 findClosestPointOnEntityBoundingBox(class_243 startPos, class_1297 entity, float expansionRadius) {
        if (entity == null) {
            return new class_243(0, 0, 0);
        }
        class_238 entityBox = entity.method_5829();
        class_238 expandedBox = entityBox.method_1014(-expansionRadius);
        class_243 center = expandedBox.method_1005();
        class_243 closestPoint = new class_243(0, 0, 0);
        double minDistance = Double.MAX_VALUE;

        for (double dx = 0.0; dx <= (expandedBox.field_1320 - expandedBox.field_1323) / 2.0; dx += 0.1) {
            for (double dy = 0.0; dy <= (expandedBox.field_1325 - expandedBox.field_1322) / 2.0; dy += 0.1) {
                for (double dz = 0.0; dz <= (expandedBox.field_1324 - expandedBox.field_1321) / 2.0; dz += 0.1) {
                    for (int signX : new int[]{-1, 1}) {
                        for (int signY : new int[]{-1, 1}) {
                            for (int signZ : new int[]{-1, 1}) {
                                double px = center.field_1352 + signX * dx;
                                double py = center.field_1351 + signY * dy;
                                double pz = center.field_1350 + signZ * dz;
                                class_243 candidatePoint = new class_243(px, py, pz);
                                class_241 tracedRay = EntityRayTracer.traceEntityRay(entity, candidatePoint);
                                class_1297 rayEntityResult = EntityRayTracer.getRaycastEntityResult(entity, tracedRay.field_1343, tracedRay.field_1342, 3.0);
                                if (rayEntityResult != null && rayEntityResult.method_5628() == entity.method_5628()) {
                                    double distance = startPos.method_1022(candidatePoint);
                                    if (distance < minDistance) {
                                        minDistance = distance;
                                        closestPoint = candidatePoint;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (closestPoint.method_1033() == 0) {
            return new class_243(
                class_3532.method_15350(startPos.field_1352, entityBox.field_1323, entityBox.field_1320),
                class_3532.method_15350(startPos.field_1351, entityBox.field_1322, entityBox.field_1325),
                class_3532.method_15350(startPos.field_1350, entityBox.field_1321, entityBox.field_1324)
            );
        }
        return closestPoint;
    }

    public static Vector4f getEntityYawPitch(class_1297 entity) {
        class_243 targetPos = findClosestPointOnEntityBoundingBox(entity).method_1020(MINECRAFT.field_1724.method_5836(MINECRAFT.method_60646().method_60637(true)));
        float yaw = (float) (Math.toDegrees(Math.atan2(targetPos.field_1350, targetPos.field_1352)) - 90.0);
        float pitch = (float) (-Math.toDegrees(Math.atan2(targetPos.field_1351, Math.sqrt(targetPos.field_1352 * targetPos.field_1352 + targetPos.field_1350 * targetPos.field_1350))));
        float adjustedYaw = class_3532.method_15393(yaw - MINECRAFT.field_1724.method_36454());
        float adjustedPitch = pitch - MINECRAFT.field_1724.method_36455();
        return new Vector4f(yaw, pitch, adjustedYaw, adjustedPitch);
    }

    public static Vector4f getEntityYawPitch(class_1309 entity) {
        return getEntityYawPitch((class_1297) entity);
    }

    public static double getEntityAngularDistance(class_1309 entity) {
        Vector4f angles = getEntityYawPitch(entity);
        float diffYaw = angles.z;
        float diffPitch = angles.w;
        return Math.sqrt(diffYaw * diffYaw + diffPitch * diffPitch);
    }

    public static double getEntityAngularDistance(class_1297 entity) {
        Vector4f angles = getEntityYawPitch(entity);
        float diffYaw = angles.z;
        float diffPitch = angles.w;
        return Math.sqrt(diffYaw * diffYaw + diffPitch * diffPitch);
    }

    private EntityPositionUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

interface EntityRayTracer {
    static class_241 traceEntityRay(class_1297 entity, class_243 pos) { return null; }
    static class_1297 getRaycastEntityResult(class_1297 entity, double x, double y, double z) { return null; }
}