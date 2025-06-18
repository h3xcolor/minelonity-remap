// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.fabric.client.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import ru.melonity.animation.FrameWeightCalculator;
import ru.melonity.animation.StateAnimation;

@Environment(EnvType.CLIENT)
public interface IMobEffectInstance {
    int DEFAULT_ANIMATION_DURATION = 263925059;

    StateAnimation getScale();
    FrameWeightCalculator getFrameWeightCalculator();
    long getAnimationStartTime();
    void setAnimationStartTime(long startTime);
}