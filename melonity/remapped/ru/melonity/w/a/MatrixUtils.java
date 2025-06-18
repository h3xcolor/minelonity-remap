// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.w.a;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_3532;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

@Environment(value = EnvType.CLIENT)
public class MatrixUtils extends ProjectionHandler {
    private static final float[] IDENTITY_MATRIX = new float[]{1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f};
    private static final FloatBuffer matrixBufferA = BufferUtils.createFloatBuffer(16);
    private static final FloatBuffer matrixBufferB = BufferUtils.createFloatBuffer(16);
    private static final FloatBuffer matrixBufferC = BufferUtils.createFloatBuffer(16);
    private static final float[] vectorA = new float[4];
    private static final float[] vectorB = new float[4];

    private static void setIdentityMatrix(FloatBuffer buffer) {
        int position = buffer.position();
        buffer.put(IDENTITY_MATRIX);
        buffer.position(position);
    }

    private static void multMatrixVec(FloatBuffer matrix, float[] srcVec, float[] dstVec) {
        for (int i = 0; i < 4; ++i) {
            dstVec[i] = matrix.get(i) * srcVec[0] + matrix.get(4 + i) * srcVec[1] + matrix.get(8 + i) * srcVec[2] + matrix.get(12 + i) * srcVec[3];
        }
    }

    private static boolean invertMatrix() {
        FloatBuffer tempBuffer = matrixBufferC;
        for (int i = 0; i < 16; ++i) {
            tempBuffer.put(i, matrixBufferB.get(i));
        }
        setIdentityMatrix(matrixBufferB);
        for (int pivot = 0; pivot < 4; ++pivot) {
            int maxRow = pivot;
            for (int row = pivot + 1; row < 4; ++row) {
                if (Math.abs(tempBuffer.get(row * 4 + pivot)) > Math.abs(tempBuffer.get(maxRow * 4 + pivot))) {
                    maxRow = row;
                }
            }
            if (maxRow != pivot) {
                for (int col = 0; col < 4; ++col) {
                    float swap = tempBuffer.get(pivot * 4 + col);
                    tempBuffer.put(pivot * 4 + col, tempBuffer.get(maxRow * 4 + col));
                    tempBuffer.put(maxRow * 4 + col, swap);
                    swap = matrixBufferB.get(pivot * 4 + col);
                    matrixBufferB.put(pivot * 4 + col, matrixBufferB.get(maxRow * 4 + col));
                    matrixBufferB.put(maxRow * 4 + col, swap);
                }
            }
            float diag = tempBuffer.get(pivot * 4 + pivot);
            if (diag == 0.0f) {
                return false;
            }
            for (int col = 0; col < 4; ++col) {
                tempBuffer.put(pivot * 4 + col, tempBuffer.get(pivot * 4 + col) / diag);
                matrixBufferB.put(pivot * 4 + col, matrixBufferB.get(pivot * 4 + col) / diag);
            }
            for (int row = 0; row < 4; ++row) {
                if (row == pivot) continue;
                float factor = tempBuffer.get(row * 4 + pivot);
                for (int col = 0; col < 4; ++col) {
                    tempBuffer.put(row * 4 + col, tempBuffer.get(row * 4 + col) - tempBuffer.get(pivot * 4 + col) * factor);
                    matrixBufferB.put(row * 4 + col, matrixBufferB.get(row * 4 + col) - matrixBufferB.get(pivot * 4 + col) * factor);
                }
            }
        }
        return true;
    }

    private static void multMatrices(FloatBuffer matA, FloatBuffer matB) {
        for (int row = 0; row < 4; ++row) {
            for (int col = 0; col < 4; ++col) {
                matrixBufferB.put(
                    row * 4 + col,
                    matA.get(row * 4) * matB.get(col) +
                    matA.get(row * 4 + 1) * matB.get(4 + col) +
                    matA.get(row * 4 + 2) * matB.get(8 + col) +
                    matA.get(row * 4 + 3) * matB.get(12 + col)
                );
            }
        }
    }

    public static void setupPerspectiveProjection(float fovY, float aspect, float zNear, float zFar) {
        float angleRad = (float)((fovY / 2.0f) * Math.PI / 180.0);
        float deltaZ = zFar - zNear;
        float sine = class_3532.method_15374(angleRad);
        if (deltaZ == 0.0f || sine == 0.0f || aspect == 0.0f) {
            return;
        }
        float cotangent = class_3532.method_15362(angleRad) / sine;
        setIdentityMatrix(matrixBufferA);
        matrixBufferA.put(0, cotangent / aspect);
        matrixBufferA.put(5, cotangent);
        matrixBufferA.put(10, -(zFar + zNear) / deltaZ);
        matrixBufferA.put(11, -1.0f);
        matrixBufferA.put(14, -2.0f * zNear * zFar / deltaZ);
        matrixBufferA.put(15, 0.0f);
        GL11.glMultMatrixf(matrixBufferA);
    }

    public static boolean projectPoint(float x, float y, float z, FloatBuffer modelView, FloatBuffer projection, IntBuffer viewport, FloatBuffer winPos) {
        float[] inVec = new float[]{x, y, z, 1.0f};
        float[] tempVec = new float[4];
        multMatrixVec(modelView, inVec, tempVec);
        multMatrixVec(projection, tempVec, inVec);
        if (inVec[3] == 0.0f) {
            return false;
        }
        float invW = 1.0f / inVec[3];
        inVec[0] = (inVec[0] * invW + 1.0f) * 0.5f;
        inVec[1] = (inVec[1] * invW + 1.0f) * 0.5f;
        inVec[2] = inVec[2] * invW;
        int viewX = viewport.get(0);
        int viewY = viewport.get(1);
        int viewWidth = viewport.get(2);
        int viewHeight = viewport.get(3);
        winPos.put(0, inVec[0] * viewWidth + viewX);
        winPos.put(1, inVec[1] * viewHeight + viewY);
        winPos.put(2, inVec[2]);
        return true;
    }

    public static boolean unprojectPoint(float winX, float winY, float winZ, FloatBuffer modelView, FloatBuffer projection, IntBuffer viewport, FloatBuffer objPos) {
        multMatrices(projection, modelView);
        if (!invertMatrix()) {
            return false;
        }
        vectorA[0] = winX;
        vectorA[1] = winY;
        vectorA[2] = winZ;
        vectorA[3] = 1.0f;
        int viewX = viewport.get(0);
        int viewY = viewport.get(1);
        int viewWidth = viewport.get(2);
        int viewHeight = viewport.get(3);
        vectorA[0] = (vectorA[0] - viewX) / viewWidth;
        vectorA[1] = (vectorA[1] - viewY) / viewHeight;
        vectorA[0] = vectorA[0] * 2.0f - 1.0f;
        vectorA[1] = vectorA[1] * 2.0f - 1.0f;
        vectorA[2] = vectorA[2] * 2.0f - 1.0f;
        multMatrixVec(matrixBufferB, vectorA, vectorB);
        if (vectorB[3] == 0.0f) {
            return false;
        }
        float invW = 1.0f / vectorB[3];
        objPos.put(objPos.position(), vectorB[0] * invW);
        objPos.put(objPos.position() + 1, vectorB[1] * invW);
        objPos.put(objPos.position() + 2, vectorB[2] * invW);
        return true;
    }

    public static void viewportToWindowCoords(float winX, float winY, float winWidth, float winHeight, IntBuffer viewport) {
        if (winWidth <= 0.0f || winHeight <= 0.0f) {
            return;
        }
        GL11.glTranslatef(
            (viewport.get(viewport.position() + 2) - 2.0f * (winX - viewport.get(viewport.position()))) / winWidth,
            (viewport.get(viewport.position() + 3) - 2.0f * (winY - viewport.get(viewport.position() + 1))) / winHeight,
            0.0f
        );
        GL11.glScalef(
            viewport.get(viewport.position() + 2) / winWidth,
            viewport.get(viewport.position() + 3) / winHeight,
            1.0f
        );
    }
}