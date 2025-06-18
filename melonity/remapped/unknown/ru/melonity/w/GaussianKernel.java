// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.w;

import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.util.Map;

@Environment(value = EnvType.CLIENT)
public final class GaussianKernel {
    private static final Map<Float, FloatBuffer> kernelCache = Maps.newLinkedHashMap();

    public static FloatBuffer getKernel(float radius) {
        Float key = Float.valueOf(radius);
        if (kernelCache.containsKey(key)) {
            return kernelCache.get(key);
        }

        FloatBuffer buffer = BufferUtils.createFloatBuffer(256);
        FloatBuffer kernelBuffer = buffer;
        for (int i = 1; (float) i <= radius; ++i) {
            float value = computeGaussianValue(i, radius / 2.0f);
            kernelBuffer.put(value);
        }
        kernelBuffer.rewind();
        kernelCache.put(Float.valueOf(radius), kernelBuffer);
        return getKernel(radius);
    }

    private static float computeGaussianValue(float x, float sigma) {
        float scale = 1.0f / MathHelper.sqrt((float) (Math.PI * 2) * (sigma * sigma));
        return (float) (scale * Math.exp(-(x * x) / (2.0 * (sigma * sigma))));
    }

    private GaussianKernel() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}