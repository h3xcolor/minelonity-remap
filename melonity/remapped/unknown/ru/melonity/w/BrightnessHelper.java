// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.w;

import lombok.Generated;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;

@Environment(EnvType.CLIENT)
public final class BrightnessHelper {
    private static final MinecraftClient minecraft = MinecraftClient.getInstance();
    public static int gammaConstant = 1140157482;

    public static float adjustBrightness(float value) {
        return getStepCount(value) * getBrightnessStep();
    }

    public static float getStepCount(float value) {
        return Math.round(value / getBrightnessStep());
    }

    public static float roundBrightness(float value) {
        float step = getBrightnessStep();
        return (float) Math.round(value / step) * step;
    }

    public static float getBrightnessStep() {
        return computeGammaAdjustedBrightness() * 0.15f;
    }

    public static float computeGammaAdjustedBrightness() {
        float gammaValue = (float) (minecraft.options.getGamma().getValue() * 0.6 + 0.2);
        return gammaValue * gammaValue * gammaValue * 8.0f;
    }

    @Generated
    private BrightnessHelper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}