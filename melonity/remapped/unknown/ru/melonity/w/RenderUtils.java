// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.w;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Rotation;
import net.minecraft.util.math.Vec3d;
import ru.melonity.Melonity;
import ru.melonity.s.c.IIllIIlIllIIlIlllIIllIIIllIIIllIlIIlIIIllIIlllIIllIllIlIIlllIIlllIlIlIIlllIIlllIIIllIIllIIlIIIllIIIlllIIIllIllIIIlllIIllIllIlllIIIllIIIllIIlIlIIllIIlIIIlllIIllIIllIllIlllIIIlIIlIIIlIIllIlIIllIlllIlIIIllIIIlIIIllIIIlIlIllIIllIlIll;

@Environment(EnvType.CLIENT)
public final class RenderUtils {

    public static double lerp(double start, double end, double progress) {
        return end + (start - end) * progress;
    }

    public static double getFrameTime() {
        MinecraftClient client = MinecraftClient.getInstance();
        int fps = client.getCurrentFps();
        if (fps > 0) {
            return 1.0 / fps;
        }
        return 1.0;
    }

    public static float interpolateWithFrameTime(float start, float end, float factor) {
        float t = MathHelper.clamp((float) (getFrameTime() * factor), 0.0f, 1.0f);
        return (1.0f - t) * start + t * end;
    }

    public static float randomFloat(double min, double max) {
        return (float) (Math.random() * (max - min) + min);
    }

    public static float lerp(float start, float end, float progress) {
        return end + (start - end) * progress;
    }

    public static Vec3d lerpVec3d(Vec3d start, Vec3d end, float progress) {
        return new Vec3d(
            lerp(start.x, end.x, progress),
            lerp(start.y, end.y, progress),
            lerp(start.z, end.z, progress)
        );
    }

    public static Rotation getLookAngles(Vec3d target) {
        Vec3d diff = target.subtract(MinecraftClient.getInstance().player.getPos());
        double horizontalDistance = Math.hypot(diff.x, diff.z);
        return new Rotation(
            (float) Math.toDegrees(Math.atan2(diff.z, diff.x)) - 90.0f,
            (float) -Math.toDegrees(Math.atan2(diff.y, horizontalDistance))
        );
    }

    public static Rotation getEntityLookAngles(Entity entity) {
        Vec3d diff = entity.getPos().subtract(MinecraftClient.getInstance().player.getPos());
        double horizontalDistance = Math.hypot(diff.x, diff.z);
        return new Rotation(
            (float) Math.toDegrees(Math.atan2(diff.z, diff.x)) - 90.0f,
            (float) -Math.toDegrees(Math.atan2(diff.y, horizontalDistance))
        );
    }

    public static boolean isPointInScaledRectangle(double x, double y, double width, double height, double pointX, double pointY) {
        IIllIIlIllIIlIlllIIllIIIllIIIllIlIIlIIIllIIlllIIllIllIlIIlllIIlllIlIlIIlllIIlllIIIllIIllIIlIIIllIIIlllIIIllIllIIIlllIIllIllIlllIIIllIIIllIIlIlIIllIIlIIIlllIIllIIllIllIlllIIIlIIlIIIlIIllIlIIllIlllIlIIIllIIIlIIIllIIIlIlIllIIllIlIll settings = Melonity.IllIllIlIIIllIIlIIllIIlllIIlllIIllIlllIIlIIllIIlllIlIllIIIllIllIIlllIlllIIlIIlIllIIllIlllIlllIIlIIlIIlIIlIIlllIllIIlIllIIIlllIIllIIIlIIIlllIlIIIlIlIlIIllIIIlIIllIlIIlIlIllIIlllIIllIIIlllIIllIIlIllIllIIllIllIIlllIIlIll();
        float scaleFactor = settings.IIllIIlIllIIlIlllIIllIIIllIIIllIlIIlIIIllIIlllIIllIllIlIIlllIIlllIlIlIIlllIIlllIIIllIIllIIlIIIllIIIlllIIIllIllIIIlllIIllIllIlllIIIllIIIllIIlIlIIllIIlIIIlllIIllIIllIllIlllIIIlIIlIIIlIIllIlIIllIlllIlIIIllIIIlIIIllIIIlIlIllIIllIlIll();
        x *= scaleFactor;
        y *= scaleFactor;
        width *= scaleFactor;
        height *= scaleFactor;
        return pointX >= x && pointY >= y && pointX <= x + width && pointY <= y + height;
    }

    public static float randomFloat(float min, float max) {
        return (float) (Math.random() * (max - min)) + min;
    }

    @Generated
    private RenderUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}