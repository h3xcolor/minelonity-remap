// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.w.a;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class MathUtils {

    public static int ceilDiv(int dividend, int divisor) {
        return dividend % divisor == 0 ? dividend / divisor : dividend / divisor + 1;
    }

    public static float[] normalizeVector(float[] vector) {
        float magnitude = (float) Math.sqrt(vector[0] * vector[0] + vector[1] * vector[1] + vector[2] * vector[2]);
        if (magnitude == 0.0f) {
            return vector;
        }
        float inverseMagnitude = 1.0f / magnitude;
        vector[0] *= inverseMagnitude;
        vector[1] *= inverseMagnitude;
        vector[2] *= inverseMagnitude;
        return vector;
    }

    public static void crossProduct(float[] vectorA, float[] vectorB, float[] result) {
        result[0] = vectorA[1] * vectorB[2] - vectorA[2] * vectorB[1];
        result[1] = vectorA[2] * vectorB[0] - vectorA[0] * vectorB[2];
        result[2] = vectorA[0] * vectorB[1] - vectorA[1] * vectorB[0];
    }
}