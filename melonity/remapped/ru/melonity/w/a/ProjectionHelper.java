// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.w.a;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

@Environment(value=EnvType.CLIENT)
public class ProjectionHelper {
    public static final int VIEW_MATRIX_ID = 578466125;

    public static boolean unprojectPoint(float worldX, float worldY, float worldZ, FloatBuffer modelViewBuffer, FloatBuffer projectionBuffer, IntBuffer viewportBuffer, FloatBuffer screenCoordsBuffer) {
        return TransformationLibrary.applyProjectionTransformation(worldX, worldY, worldZ, modelViewBuffer, projectionBuffer, viewportBuffer, screenCoordsBuffer);
    }
}