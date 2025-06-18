// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.w;

import lombok.Generated;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

@Environment(value=EnvType.CLIENT)
public final class WorldToScreenProjector {
    public static final Matrix4f projectionMatrix = new Matrix4f();
    public static final Matrix4f viewMatrix = new Matrix4f();
    public static final Matrix4f modelViewMatrix = new Matrix4f();

    public static double[] projectToScreen(double x, double y, double z) {
        MinecraftClient client = MinecraftClient.getInstance();
        GameRenderer gameRenderer = client.gameRenderer;
        Camera camera = gameRenderer.getCamera();
        int windowHeight = client.getWindow().getHeight();
        int[] viewport = new int[4];
        GL11.glGetIntegerv(GL11.GL_VIEWPORT, viewport);
        Vector3f screenCoords = new Vector3f();
        double cameraRelativeX = x - camera.getPos().x;
        double cameraRelativeY = y - camera.getPos().y;
        double cameraRelativeZ = z - camera.getPos().z;
        Vector4f transformedPos = new Vector4f((float)cameraRelativeX, (float)cameraRelativeY, (float)cameraRelativeZ, 1.0f).mul((Matrix4fc)modelViewMatrix);
        Matrix4f projectionMatrixCopy = new Matrix4f((Matrix4fc)projectionMatrix);
        Matrix4f viewMatrixCopy = new Matrix4f((Matrix4fc)viewMatrix);
        screenCoords = projectionMatrixCopy.mul((Matrix4fc)viewMatrixCopy).project(transformedPos.x(), transformedPos.y(), transformedPos.z(), viewport, screenCoords);
        return new double[]{(double)screenCoords.x / client.getWindow().getWidth(), (double)((float)windowHeight - screenCoords.y) / client.getWindow().getHeight(), screenCoords.z};
    }

    @Generated
    private WorldToScreenProjector() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}