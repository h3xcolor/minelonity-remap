// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.b.d;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector4d;
import ru.melonity.fabric.client.model.ILevelRenderer;
import ru.melonity.w.IIIllIIlllIlIIIllIllIIIlllIIlllIIlllIlIlIIlIIIllIIllIlllIIlllIIlIlllIIlIIlIIllIIlIIIlllIllIIlllIIlIllIIIllIIlIIlIIlIIllIIlIIlIIIllIIllIIlIIlIIlllIIlllIIIllIlIIllIllIIlllIlIIIlllIIllIIlllIIllIlllIIllIIIllIIllIIllIIIlllIlIIlllIIllIIllIIlll;

@Environment(value = EnvType.CLIENT)
public final class FrustumHelper {

    public static boolean isEntityInFrustum(Entity entity) {
        return ((ILevelRenderer) MinecraftClient.getInstance().gameRenderer).getFrustum().isVisible(entity.getBoundingBox());
    }

    public static boolean isPointInFrustum(Vec3d point) {
        return ((ILevelRenderer) MinecraftClient.getInstance().gameRenderer).getFrustum().isVisible(
            new Box(
                point.x - 0.5, point.y - 0.5, point.z - 0.5,
                point.x + 0.5, point.y + 0.5, point.z + 0.5
            )
        );
    }

    public static Vector4d getEntityScreenBoundingBox(Entity entity, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        Camera camera = client.gameRenderer.getCamera();
        Vec3d cameraPos = camera.getPos();
        Vec3d entityPos = entity.getLerpedPos(tickDelta);
        Box entityBox = entity.getBoundingBox();

        double minX = entityBox.minX - entity.getX() + entityPos.x - 0.05;
        double minY = entityBox.minY - entity.getY() + entityPos.y;
        double minZ = entityBox.minZ - entity.getZ() + entityPos.z - 0.05;
        double maxX = entityBox.maxX - entity.getX() + entityPos.x + 0.05;
        double maxY = entityBox.maxY - entity.getY() + entityPos.y + 0.15;
        double maxZ = entityBox.maxZ - entity.getZ() + entityPos.z + 0.05;

        Box adjustedBox = new Box(minX, minY, minZ, maxX, maxY, maxZ);

        Vec3d[] corners = {
            new Vec3d(adjustedBox.minX, adjustedBox.minY, adjustedBox.minZ),
            new Vec3d(adjustedBox.minX, adjustedBox.maxY, adjustedBox.minZ),
            new Vec3d(adjustedBox.maxX, adjustedBox.minY, adjustedBox.minZ),
            new Vec3d(adjustedBox.maxX, adjustedBox.maxY, adjustedBox.minZ),
            new Vec3d(adjustedBox.minX, adjustedBox.minY, adjustedBox.maxZ),
            new Vec3d(adjustedBox.minX, adjustedBox.maxY, adjustedBox.maxZ),
            new Vec3d(adjustedBox.maxX, adjustedBox.minY, adjustedBox.maxZ),
            new Vec3d(adjustedBox.maxX, adjustedBox.maxY, adjustedBox.maxZ)
        };

        Vector4d screenBounds = null;
        for (Vec3d corner : corners) {
            Vec3d screenCoords = projectToScreenCoordinates(corner.x, corner.y, corner.z);
            if (screenCoords.z > 0.0 && screenCoords.z < 1.0) {
                if (screenBounds == null) {
                    screenBounds = new Vector4d(screenCoords.x, screenCoords.y, screenCoords.x, screenCoords.y);
                } else {
                    screenBounds.x = Math.min(screenCoords.x, screenBounds.x);
                    screenBounds.y = Math.min(screenCoords.y, screenBounds.y);
                    screenBounds.z = Math.max(screenCoords.x, screenBounds.z);
                    screenBounds.w = Math.max(screenCoords.y, screenBounds.w);
                }
            }
        }
        return screenBounds;
    }

    private static Vec3d projectToScreenCoordinates(double x, double y, double z) {
        double[] coords = IIIllIIlllIlIIIllIllIIIlllIIlllIIlllIlIlIIlIIIllIIllIlllIIlllIIlIlllIIlIIlIIllIIlIIIlllIllIIlllIIlIllIIIllIIlIIlIIlIIllIIlIIlIIIllIIllIIlIIlIIlllIIlllIIIllIlIIllIllIIlllIlIIIlllIIllIIlllIIllIlllIIllIIIllIIllIIllIIIlllIlIIlllIIllIIllIIlll.projectToScreen(x, y, z);
        return new Vec3d(coords[0], coords[1], coords[2]);
    }

    private FrustumHelper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}